/**
 * 
 */
package com.yqjr.framework.base;

import javax.xml.ws.Holder;

import com.yqjr.framework.component.config.Configuration;
import com.yqjr.framework.component.log.InteractiveLogger;
import com.yqjr.framework.component.log.Logger;
import com.yqjr.framework.component.mapper.JsonMapper;
import com.yqjr.framework.datatype.BizzException;
import com.yqjr.framework.datatype.Date;
import com.yqjr.framework.datatype.ExceptionCode;
import com.yqjr.modules.serverlog.model.ServerLogModel;

/**
 * ClassName: BaseWebService <br>
 * Description: 框架webservice基类,用于处理框架webservice的通用逻辑 <br>
 * Create By: admin <br>
 * Create Date: 2017年5月19日 下午5:35:04 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public abstract class BaseWebService<Header extends BaseHeader, Request extends BaseRequestDTO<Request>, Response extends BaseResponseDTO<Response>> {

	private static final String CATEGORY = BaseWebService.class.getName();

	private static Logger logger = Logger.getLogger();

	private static final boolean SAVE_MESSAGE = Configuration.getConfig()
			.getBooleanValue("framework.interactivelog.savemessage");

	/**
	 * Description: 业务处理逻辑 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年6月15日 上午10:57:59
	 *
	 * @param header
	 * @param request
	 * @param response
	 */
	public void process(Holder<Header> header, Request request, Holder<Response> response) {

		long begin_timestamp = System.currentTimeMillis();
		Header headerObject = header.value;
		InteractiveLogger serverLogger = InteractiveLogger.getInteractiveLogger(InteractiveLogger.SERVER_LOGGER);
		boolean updateLog = false;
		try {
			// Header校验
			headerObject.validate();
			headerObject.extValidate();

			logger.info(CATEGORY, "接收到服务请求:" + headerObject);

			// 登记交易流水
			ServerLogModel log = new ServerLogModel();
			log.setSerialNo(headerObject.getSerialNo());
			log.setChannel(headerObject.getChannel());
			log.setServiceCode(headerObject.getServiceCode());
			log.setTransactionTime(new Date(headerObject.getTransactionTime(), "yyyyMMddHHmmss"));
			log.setToken(headerObject.getToken());
			log.setRequestTime(new Date());
			if (SAVE_MESSAGE)
				log.setReceiveMessage(JsonMapper.toJsonString(request));
			serverLogger.saveLog(log);
			updateLog = true;

			// RequestDTO检查
			request.validate();

			// 执行业务逻辑
			response.value = doProcess(request);

			// 返回应答信息
			ExceptionCode success = ExceptionCode.WS_0000;
			headerObject.setReplyCode(success.getReplyCode());
			headerObject.setReplyText(success.getReplyText());

			// 更新交易流水状态
			if (updateLog) {
				log = new ServerLogModel();
				log.setSerialNo(headerObject.getSerialNo());
				log.setResponseTime(new Date());
				log.setResponseToken(headerObject.getToken());
				log.setSendMessage(JsonMapper.toJsonString(response.value));
				log.setReplyCode(success.getReplyCode());
				log.setReplyText(success.getReplyText());
				serverLogger.performanceSaveLog(log);
			}

			logger.info(CATEGORY, String.format("服务%s执行成功,执行耗时%d毫秒", headerObject.toString(),
					(System.currentTimeMillis() - begin_timestamp)));
		} catch (Exception e) {
			logger.error(CATEGORY, String.format("服务%s执行失败,执行耗时%d毫秒", headerObject.toString(),
					(System.currentTimeMillis() - begin_timestamp)), e);
			BizzException bizEx = null;
			if (e instanceof BizzException) {
				bizEx = ((BizzException) e);
			} else {
				bizEx = new BizzException(ExceptionCode.WS_9999);
			}
			headerObject.setReplyCode(bizEx.getReplyCode());
			String extMessage = bizEx.getErrorMessage();
			headerObject.setReplyText(bizEx.getReplyText() + extMessage);

			// 更新交易流水状态
			try {
				if (updateLog) {
					ServerLogModel log = new ServerLogModel();
					log.setSerialNo(headerObject.getSerialNo());
					log.setResponseTime(new Date());
					log.setResponseToken(headerObject.getToken());
					if (SAVE_MESSAGE)
						log.setSendMessage(JsonMapper.toJsonString(response.value));
					log.setReplyCode(bizEx.getReplyCode());
					log.setReplyText(bizEx.getReplyText());
					serverLogger.performanceSaveLog(log);
				}
			} catch (Exception ex) {
				logger.error(CATEGORY, "交易流水更新异常", ex);
			}
		}
	}

	/**
	 * Description: 服务业务逻辑处理 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年6月15日 上午10:58:25
	 *
	 * @param request
	 * @return
	 */
	public abstract Response doProcess(Request request);

}
