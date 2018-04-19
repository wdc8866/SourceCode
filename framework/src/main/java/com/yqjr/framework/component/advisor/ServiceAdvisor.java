/**
 * 
 */
package com.yqjr.framework.component.advisor;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.yqjr.framework.component.config.Configuration;
import com.yqjr.framework.component.context.SpringContext;
import com.yqjr.framework.component.context.ThreadContext;
import com.yqjr.framework.datatype.BizzException;

/**
 * ClassName: ServiceAdvisor <br>
 * Description: 业务服务调用切面,主要实现以下逻辑 <br>
 * 1-公共异常处理,封装异常BizzException<br>
 * 2-事务控制,当出现分布式事务时,如果主交易回滚则允许重新提交,直至异常处理完毕<br>
 * 3-扩展点,方法调用前和调用后增加公共方法<br>
 * Create By: admin <br>
 * Create Date: 2017年4月19日 下午4:10:56 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
@Aspect
@Component
@Order(Integer.MIN_VALUE)
public class ServiceAdvisor {

	private static final String METHOD_IN_FLAG = "framework.methodin.flag";

	/**
	 * Description: 用于扩展服务调用及异常处理 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年4月20日 上午8:50:37
	 *
	 * @param pjp
	 * @return
	 */
	@Around(value = "@within(com.yqjr.framework.annotation.FrameworkService)")
	public Object invoke(ProceedingJoinPoint pjp) {
		// 获取调用信息
		String className = pjp.getSignature().getDeclaringTypeName();
		String methodName = pjp.getSignature().getName();

		// 初始化主方法进入标记
		ThreadContext context = ThreadContext.getInstance();
		String mainMethodFlag = className + methodName;
		if (context.getString(METHOD_IN_FLAG) == null) {
			context.put(METHOD_IN_FLAG, mainMethodFlag);
		}

		// 判断是否需要服务统计
		IExtensionPoint statistics = null;
		if (Configuration.getConfig().getBooleanValue("framework.statistics")) {
			statistics = SpringContext.getInstance().getBeanWithName("statisticsExtension");
		}

		String extension = Configuration.getConfig().getStringValue("application.extension");
		// 判断是否存在应用扩展点
		IExtensionPoint appExtension = null;
		if (StringUtils.isNotBlank(extension)) {
			try {
				Object object = SpringContext.getInstance().getBeanWithClassName(extension);
				if (!(object instanceof IExtensionPoint)) {
					throw new BizzException("<extension>定义的类必须实现IExtensionPoint");
				} else {
					appExtension = (IExtensionPoint) object;
				}
			} catch (BizzException be) {
				throw be;
			} catch (Exception e) {
				throw new BizzException(e);
			}
		}
		try {
			// 执行统计
			if (statistics != null)
				statistics.preServiceInvoke(className, methodName, pjp.getArgs());
			// 执行应用扩展点
			if (appExtension != null) {
				appExtension.preServiceInvoke(className, methodName, pjp.getArgs());
			}

			// 执行服务方法调用
			Object res = pjp.proceed();

			// 执行应用扩展点
			if (appExtension != null) {
				appExtension.afterServiceInvoke(className, methodName, pjp.getArgs());
			}
			// 执行统计
			if (statistics != null)
				statistics.afterServiceInvoke(className, methodName, res);

			return res;
		} catch (Throwable e) {
			// 执行应用扩展点
			if (appExtension != null) {
				appExtension.preThrowException(className, methodName, e);
			}
			// 执行统计
			if (statistics != null)
				statistics.preThrowException(className, methodName, e);
			// 异常处理
			if (e instanceof BizzException) {
				throw (BizzException) e;
			} else {
				throw new BizzException(e);
			}
		} finally {
			if (mainMethodFlag.equals(context.getString(METHOD_IN_FLAG))) {
				ThreadContext.getInstance().reset();
			}
		}
	}

}
