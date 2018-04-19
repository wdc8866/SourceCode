/**
 * 
 */
package com.yqjr.framework.component.esb;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.yqjr.framework.component.advisor.TransactionInvoker;
import com.yqjr.framework.component.advisor.RemoteInvoker;
import com.yqjr.framework.component.advisor.TransactionCheckAdvisor;
import com.yqjr.framework.component.cache.AbstractCache;
import com.yqjr.framework.component.cache.ICache;
import com.yqjr.framework.component.config.Configuration;
import com.yqjr.framework.component.context.SpringContext;
import com.yqjr.framework.component.context.ThreadContext;
import com.yqjr.framework.component.interceptor.EsbHeaderParserInterceptor;
import com.yqjr.framework.component.interceptor.EsbHeaderAdderInterceptor;
import com.yqjr.framework.component.interceptor.ReceiverLoggingInterceptor;
import com.yqjr.framework.component.interceptor.SenderLoggingInterceptor;
import com.yqjr.framework.component.log.InteractiveLogger;
import com.yqjr.framework.component.log.Logger;
import com.yqjr.framework.component.mapper.JsonMapper;
import com.yqjr.framework.component.sequence.AbstractSequenceManager;
import com.yqjr.framework.datatype.InteractiveException;
import com.yqjr.framework.utils.Constants;
import com.yqjr.framework.datatype.Date;
import com.yqjr.modules.clientlog.model.ClientLogModel;
import com.yqjr.modules.wsclient.model.WsClientModel;
import com.yqjr.modules.wsclient.service.WsClientService;

/**
 * ClassName: EsbClient <br>
 * Description: ESB客户端组件 <br>
 * Create By: admin <br>
 * Create Date: 2017年5月31日 下午6:56:46 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
@Service
public class WebServiceClient {

	private static WebServiceClient instance = null;

	private static Logger logger = Logger.getLogger();

	private static final String CATEGORY = WebServiceClient.class.getName();

	private static final String WSCLIENT_CACHE = "framework.wsclient.cache";

	public static final String WS_ESBHEADER_KEY = "framework.wsclient.esbHeader";

	private static final boolean SAVE_MESSAGE = Configuration.getConfig()
			.getBooleanValue("framework.interactivelog.savemessage");

	private static final long DEFAULT_READ_TIMEOUT = Configuration.getConfig()
			.getLongValue("framework.wsclient.read.timeout");

	private static final long DEFAULT_CONNECT_TIMEOUT = Configuration.getConfig()
			.getLongValue("framework.wsclient.connect.timeout");

	private ICache cache = AbstractCache.getCache();

	@Autowired
	private WsClientService wsClientService;

	/**
	 * 渠道标志
	 */
	private String channel = Configuration.getConfig().getStringValue("framework.wsclient.channel");

	/**
	 * 流水号长度
	 */
	private int length = Configuration.getConfig().getIntValue("framework.wsclient.sequence.length");

	/**
	 * 序列名称
	 */
	private String sequenceName = Configuration.getConfig().getStringValue("framework.wsclient.sequence.name");

	private WebServiceClient() {

	}

	public static WebServiceClient getInstance() {
		if (instance == null) {
			synchronized (WebServiceClient.class) {
				if (instance == null) {
					instance = SpringContext.getInstance().getBeanWithName("webServiceClient");
				}
			}
		}
		return instance;
	}

	/**
	 * Description: 客户端服务初始化 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年6月2日 上午9:26:45
	 */
	public void init() {
		List<WsClientModel> list = wsClientService.findList(null);
		for (WsClientModel client : list) {
			logger.info(CATEGORY, "检索到配置:" + client.toString());
			cache.put(WSCLIENT_CACHE, client.getServiceCode(), client);
		}
	}

	/**
	 * Description: 服务调用 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年7月6日 下午4:40:50
	 *
	 * @param serviceClass
	 * @param method
	 * @param esbHeader
	 * @param request
	 * @return
	 * @throws InteractiveException
	 */
	@SuppressWarnings("unchecked")
	private <T> T esbCall(String serviceClass, Method method, ESBHeader esbHeader, final IReplyParser replyParser,
			Object... request) throws InteractiveException {
		// 从数据库中读取客户端配置信息
		WsClientModel wsClient = new WsClientModel();
		wsClient.setServiceCode(esbHeader.getServiceCode());
		wsClient = wsClientService.get(wsClient);
		Assert.notNull(wsClient, "can't find wsClient use serviceCode" + esbHeader.getServiceCode());
		wsClient.setClassName(serviceClass);
		ClientLogModel log = null;
		RemoteInvoker remoteInvoker = new RemoteInvoker();
		try {
			// 将ESBHeader添加至交易上下文
			ThreadContext.getInstance().put(WS_ESBHEADER_KEY, esbHeader);

			// 初始化调用日志
			log = new ClientLogModel();
			log.setMsgRefNo(esbHeader.getMsgRef());
			log.setChannel(channel);
			log.setServiceCode(esbHeader.getServiceCode());
			log.setExternalReferenceNo(esbHeader.getExternalReferenceNo());
			Date date = new Date();
			log.setTransactionDate(date);
			log.setRequestTime(date);
			if (SAVE_MESSAGE)
				log.setSendMessage(JsonMapper.toJsonString(request));

			// 初始化RemoteInvoker
			remoteInvoker
					.setId(AbstractSequenceManager.getSequenceManager().generateIntSeq(Constants.DEFAULT_SEQ_NAME));
			remoteInvoker.setInvokeType("WS");
			remoteInvoker.setRequestTime(date);
			remoteInvoker.setSerialNo(esbHeader.getExternalReferenceNo());
			remoteInvoker.setServiceCode(wsClient.getServiceCode());
			remoteInvoker.setServiceName(wsClient.getServiceName());
			remoteInvoker.setTransType(wsClient.getTransType());

			// 服务调用
			logger.info(CATEGORY, String.format("调用%s开始,交易码:%s,ESB通讯流水号:%s", wsClient.getRemarks(),
					wsClient.getServiceCode(), esbHeader.getExternalReferenceNo()));
			Object response = method.invoke(createClient(wsClient), request);
			logger.info(CATEGORY, String.format("调用%s成功", wsClient.getRemarks()));

			// 应答报文解析
			Reply reply = replyParser.parseReply(response);
			String replyCode = reply.getReplyCode();
			String replyText = reply.getReplyText();
			Date responseTime = reply.getReponseTime();
			Reply.Status status = reply.getStatus();

			// 补充应答信息
			log.setResponseTime(new Date());
			log.setReplyCode(esbHeader.getReplyCode());
			log.setReplyText(esbHeader.getReplyText());
			log.setResponseTime(responseTime);
			if (SAVE_MESSAGE)
				log.setReceiveMessage(JsonMapper.toJsonString(response));

			// 补充RemoteInvoker信息
			remoteInvoker.setResponseTime(responseTime);
			remoteInvoker.setReplyCode(replyCode);
			remoteInvoker.setReplyText(replyText);
			remoteInvoker.setInvokeStatus(status == Reply.Status.OK ? 1 : 0);

			// 状态判断
			if (status != Reply.Status.OK) {
				throw new InteractiveException(replyCode, replyText);
			}
			return (T) response;
		} catch (Exception e) {
			logger.error(CATEGORY, String.format("调用%s失败", wsClient.getRemarks()), e);
			if (e instanceof InteractiveException) {
				throw (InteractiveException) e;
			}
			InteractiveException ce = null;
			// 找到原始异常信息
			if (e instanceof InvocationTargetException) {
				Throwable targetException = ((InvocationTargetException) e).getTargetException();
				// 如果为产生的调用异常则直接抛出
				if (targetException.getCause() instanceof InteractiveException) {
					ce = (InteractiveException) targetException.getCause();
				} else {
					ce = new InteractiveException("调用远程接口异常", e.getCause());
				}
			}
			// 如果不是包装异常则直接使用CallException抛出
			else {
				ce = new InteractiveException("调用远程接口异常", e);
			}

			// 补充日志应答信息
			log.setResponseTime(new Date());
			log.setReplyCode(ce.getReplyCode());
			log.setReplyText(ce.getReplyText());

			// 补充RemoteInvoker信息
			remoteInvoker.setInvokeStatus(0);
			remoteInvoker.setResponseTime(new Date());

			throw ce;
		} finally {
			// 记录日志
			if (log != null) {
				InteractiveLogger.getInteractiveLogger(InteractiveLogger.CLIENT_LOGGER).performanceSaveLog(log);
			}
			// 执行清理
			ThreadContext.getInstance().remove(WS_ESBHEADER_KEY);
			// 添加至remoteInvoker
			TransactionInvoker transactionInvoker = ThreadContext.getInstance()
					.getObject(TransactionCheckAdvisor.TRANSACTION_INVOKER);
			remoteInvoker.setTransactionInvoker(transactionInvoker);
			transactionInvoker.add(remoteInvoker);
		}
	}

	/**
	 * Description: 服务调用 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年7月6日 下午4:41:04
	 *
	 * @param serviceClass
	 * @param method
	 * @param serviceCode
	 * @param request
	 * @return
	 * @throws InteractiveException
	 */
	@SuppressWarnings("unchecked")
	private <T> T wsCall(String serviceClass, Method method, String serviceCode, final IReplyParser replyParser,
			Object... request) throws InteractiveException {
		// 从数据库中读取客户端配置信息
		WsClientModel wsClient = new WsClientModel();
		wsClient.setServiceCode(serviceCode);
		wsClient = wsClientService.get(wsClient);
		Assert.notNull(wsClient, "can't find wsClient use serviceCode" + serviceCode);
		wsClient.setClassName(serviceClass);
		String serialNo = AbstractSequenceManager.getSequenceManager().generateSerialNo(sequenceName, length);
		ClientLogModel log = null;
		RemoteInvoker remoteInvoker = new RemoteInvoker();
		try {
			// 初始化调用日志
			log = new ClientLogModel();
			log.setMsgRefNo(serialNo);
			log.setChannel(channel);
			log.setServiceCode(serviceCode);
			log.setExternalReferenceNo(serialNo);
			Date date = new Date();
			log.setTransactionDate(date);
			log.setRequestTime(date);
			if (SAVE_MESSAGE)
				log.setSendMessage(JsonMapper.toJsonString(request));

			// 初始化RemoteInvoker
			remoteInvoker
					.setId(AbstractSequenceManager.getSequenceManager().generateIntSeq(Constants.DEFAULT_SEQ_NAME));
			remoteInvoker.setInvokeType("WS");
			remoteInvoker.setRequestTime(date);
			remoteInvoker.setSerialNo(serialNo);
			remoteInvoker.setServiceCode(wsClient.getServiceCode());
			remoteInvoker.setServiceName(wsClient.getServiceName());
			remoteInvoker.setTransType(wsClient.getTransType());

			// 服务调用
			logger.info(CATEGORY, String.format("调用%s开始,交易码:%s,通讯流水号:%s", wsClient.getServiceName(),
					wsClient.getServiceCode(), serialNo));
			Object response = method.invoke(createClient(wsClient), request);
			logger.info(CATEGORY, String.format("调用%s成功", wsClient.getServiceName()));

			// 应答报文解析
			Reply reply = replyParser.parseReply(response);
			String replyCode = reply.getReplyCode();
			String replyText = reply.getReplyText();
			Date responseTime = reply.getReponseTime();
			Reply.Status status = reply.getStatus();

			// 补充日志应答信息
			log.setResponseTime(responseTime);
			log.setReplyCode(replyCode);
			log.setReplyText(replyText);
			if (SAVE_MESSAGE)
				log.setReceiveMessage(JsonMapper.toJsonString(response));

			// 补充RemoteInvoker信息
			remoteInvoker.setResponseTime(responseTime);
			remoteInvoker.setReplyCode(replyCode);
			remoteInvoker.setReplyText(replyText);
			remoteInvoker.setInvokeStatus(status == Reply.Status.OK ? 1 : 0);

			// 状态判断
			if (status != Reply.Status.OK) {
				throw new InteractiveException(replyCode, replyText);
			}
			return (T) response;
		} catch (Exception e) {
			logger.error(CATEGORY, String.format("调用%s失败", wsClient.getServiceName()), e);
			if (e instanceof InteractiveException) {
				throw (InteractiveException) e;
			}
			InteractiveException ce = null;
			if (e instanceof InvocationTargetException) {
				Throwable targetException = ((InvocationTargetException) e).getTargetException();
				// 如果为产生的调用异常则直接抛出
				if (targetException.getCause() instanceof InteractiveException) {
					ce = (InteractiveException) targetException.getCause();
				} else {
					ce = new InteractiveException("调用远程接口异常", e.getCause());
				}
			}
			// 如果不是包装异常则直接使用CallException抛出
			else {
				ce = new InteractiveException("调用远程接口异常", e);
			}
			// 补充日志应答信息
			log.setResponseTime(new Date());
			log.setReplyCode(ce.getReplyCode());
			log.setReplyText(ce.getReplyText());

			// 补充RemoteInvoker信息
			remoteInvoker.setInvokeStatus(0);
			remoteInvoker.setResponseTime(new Date());

			throw ce;
		} finally {
			// 记录日志
			if (log != null) {
				InteractiveLogger.getInteractiveLogger(InteractiveLogger.CLIENT_LOGGER).performanceSaveLog(log);
			}
			// 添加至transactionInvoker
			TransactionInvoker transactionInvoker = ThreadContext.getInstance()
					.getObject(TransactionCheckAdvisor.TRANSACTION_INVOKER);
			remoteInvoker.setTransactionInvoker(transactionInvoker);
			transactionInvoker.add(remoteInvoker);
		}
	}

	/**
	 * Description: 生成CXF客户端 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年6月2日 上午10:06:48
	 *
	 * @param callConfig
	 * @return
	 */
	private Object createClient(WsClientModel wsClientModel) {
		// 从缓存中获取client对象
		Object client = cache.get(WSCLIENT_CACHE, wsClientModel.getServiceCode());
		if (client == null) {
			synchronized (this) {
				if ((client = cache.get(WSCLIENT_CACHE, wsClientModel.getServiceCode())) == null) {
					// 根据配置信息初始化CXF客户端
					JaxWsProxyFactoryBean wsProxyFactory = new JaxWsProxyFactoryBean();
					wsProxyFactory.setServiceClass(
							SpringContext.getInstance().getClassWithName(wsClientModel.getClassName()));
					wsProxyFactory.setAddress(wsClientModel.getUrl());
					// 配置拦截器
					ReceiverLoggingInterceptor receiverLogger = SpringContext.getInstance()
							.getBeanWithName("receiverLogger");
					EsbHeaderParserInterceptor esbHeaderParser = SpringContext.getInstance()
							.getBeanWithName("esbHeaderParser");
					SenderLoggingInterceptor senderLogger = SpringContext.getInstance().getBeanWithName("senderLogger");
					EsbHeaderAdderInterceptor esbHeaderAdder = SpringContext.getInstance()
							.getBeanWithName("esbHeaderAdder");

					wsProxyFactory.getInInterceptors().add(receiverLogger);
					wsProxyFactory.getInInterceptors().add(esbHeaderParser);
					wsProxyFactory.getOutInterceptors().add(senderLogger);
					wsProxyFactory.getOutInterceptors().add(esbHeaderAdder);
					client = wsProxyFactory.create();
					cache.put(WSCLIENT_CACHE, wsClientModel.getServiceCode(), client);
				}
			}
		}
		// 设置超时时间
		if (wsClientModel.getTimeout() != -1) {
			Client proxy = ClientProxy.getClient(client);
			HTTPConduit conduit = (HTTPConduit) proxy.getConduit();
			HTTPClientPolicy policy = new HTTPClientPolicy();
			policy.setConnectionTimeout(DEFAULT_CONNECT_TIMEOUT * 1000);
			policy.setReceiveTimeout(
					(wsClientModel.getTimeout() == 0 ? DEFAULT_READ_TIMEOUT : wsClientModel.getTimeout()) * 1000);
			conduit.setClient(policy);
		}
		return client;
	}

	/**
	 * Description: 获取ESB客户端组件实例 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年7月6日 下午4:21:13
	 *
	 * @param esbHeader
	 * @param clazz
	 * @return
	 */
	public <T> T getEsbClient(final ESBHeader esbHeader, final Class<T> clazz, final IReplyParser replyParser) {
		// ESBHeader校验
		Assert.notNull(esbHeader, "ESBHeader can't be null");
		Assert.notNull(replyParser, "replyParser can't be null");
		esbHeader.verify();
		@SuppressWarnings("unchecked")
		T instance = (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] { clazz }, new InvocationHandler() {
			@Override
			public Object invoke(Object object, Method method, Object[] request) throws InteractiveException {
				return esbCall(clazz.getName(), method, esbHeader, replyParser, request);
			}
		});
		return instance;
	}

	/**
	 * Description: 获取普通WS客户端实例 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年7月6日 下午4:38:39
	 *
	 * @param serviceCode
	 * @return
	 */
	public <T> T getWsClient(final String serviceCode, final Class<T> clazz, final IReplyParser replyParser) {
		Assert.hasText(serviceCode, "serivceCode can't be null");
		Assert.notNull(replyParser, "replyParser can't be null");
		@SuppressWarnings("unchecked")
		T instance = (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] { clazz }, new InvocationHandler() {
			@Override
			public Object invoke(Object object, Method method, Object[] request) throws InteractiveException {
				return wsCall(clazz.getName(), method, serviceCode, replyParser, request);
			}
		});
		return instance;
	}

}
