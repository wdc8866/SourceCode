/**
 * 
 */
package com.yqjr.framework.component.advisor.statistics;

import org.springframework.stereotype.Component;

import com.yqjr.framework.component.advisor.IExtensionPoint;
import com.yqjr.framework.component.context.ThreadContext;
import com.yqjr.framework.component.log.Logger;

/**
 * ClassName: StatisticsExtension <br>
 * Description: 扩展点-统计服务执行情况 <br>
 * Create By: admin <br>
 * Create Date: 2017年4月20日 上午9:39:48 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
@Component
public class StatisticsExtension implements IExtensionPoint {

	private static final String SERVICE_TIMESTAMP = "framework.service.timestamp";

	private Logger logger = Logger.getLogger();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yqjr.framework.component.advisor.IExtensionPoint#preServiceInvoke(
	 * java.lang.String, java.lang.String, java.lang.Object[])
	 */
	@Override
	public void preServiceInvoke(String className, String methodName, Object[] args) {
		ThreadContext.getInstance().put(SERVICE_TIMESTAMP, System.currentTimeMillis());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yqjr.framework.component.advisor.IExtensionPoint#afterServiceInvoke(
	 * java.lang.String, java.lang.String, java.lang.Object)
	 */
	@Override
	public void afterServiceInvoke(String className, String methodName, Object res) {
		long begin = ThreadContext.getInstance().getLong(SERVICE_TIMESTAMP);
		String cost = String.valueOf(System.currentTimeMillis() - begin);
		// TODO 统计信息暂时写日志,以后可以保存数据库或推送监控平台
		logger.debug(String.format("服务调用%s类的%s方法成功,执行时间:%s毫秒", className, methodName, cost));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yqjr.framework.component.advisor.IExtensionPoint#preThrowException(
	 * java.lang.String, java.lang.String, java.lang.Exception)
	 */
	@Override
	public void preThrowException(String className, String methodName, Throwable e) {
		long begin = ThreadContext.getInstance().getLong(SERVICE_TIMESTAMP);
		String cost = String.valueOf(System.currentTimeMillis() - begin);
		// TODO 统计信息暂时写日志,以后可以保存数据库或推送监控平台
		logger.error("服务调用异常", e);
		logger.debug(String.format("服务调用%s类的%s方法失败,执行时间:%s毫秒", className, methodName, cost));
	}

}
