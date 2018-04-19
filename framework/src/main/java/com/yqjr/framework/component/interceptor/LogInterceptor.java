package com.yqjr.framework.component.interceptor;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.yqjr.framework.component.log.Logger;
import com.yqjr.framework.utils.DateUtils;

/**
 * 
 * ClassName: LogInterceptor <br>
 * Description: 页面请求交易拦截器 <br>
 * Create By: Wanglei <br>
 * Create Date: 2017年5月24日 上午9:34:32 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 *
 */
public class LogInterceptor implements HandlerInterceptor {

	private static final ThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<Long>("ThreadLocal StartTime");

	private static Logger logger = Logger.getLogger();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		long beginTime = System.currentTimeMillis();// 1、开始时间
		startTimeThreadLocal.set(beginTime); // 线程绑定变量（该数据只有当前请求的线程可见）
		logger.info(String.format("开始计时: %s  URI: %s", new SimpleDateFormat("hh:mm:ss.SSS").format(beginTime),request.getRequestURI()));
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (modelAndView != null) {
			logger.info("ViewName: " + modelAndView.getViewName());
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

		// 保存日志
		// LogUtils.saveLog(request, handler, ex, null);

		// 打印JVM信息。
		long beginTime = startTimeThreadLocal.get();// 得到线程绑定的局部变量（开始时间）
		long endTime = System.currentTimeMillis(); // 2、结束时间
		logger.info(String.format("计时结束：%s  耗时：%s  URI: %s  最大内存: %sm  已分配内存: %sm  已分配内存中的剩余空间: %sm  最大可用内存: %sm", 
				new SimpleDateFormat("hh:mm:ss.SSS").format(endTime), 
				DateUtils.formatDateTime(endTime - beginTime),
				request.getRequestURI(),
				Runtime.getRuntime().maxMemory() / 1024 / 1024,
				Runtime.getRuntime().totalMemory() / 1024 / 1024, 
				Runtime.getRuntime().freeMemory() / 1024 / 1024,
				(Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory()) / 1024 / 1024));
	}

}
