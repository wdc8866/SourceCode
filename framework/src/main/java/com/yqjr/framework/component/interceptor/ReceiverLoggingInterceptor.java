/**
 * 
 */
package com.yqjr.framework.component.interceptor;

import org.apache.cxf.interceptor.LoggingInInterceptor;

import com.yqjr.framework.component.log.Logger;

/**
 * ClassName: ReceiverLoggingInterceptor <br>
 * Description: 扩展原有LoggingInInterceptor,使用框架日志组件 <br>
 * Create By: admin <br>
 * Create Date: 2017年6月2日 上午11:23:59 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public class ReceiverLoggingInterceptor extends LoggingInInterceptor {

	private Logger logger = Logger.getLogger();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.cxf.interceptor.AbstractLoggingInterceptor#log(java.util.
	 * logging.Logger, java.lang.String)
	 */
	@Override
	protected void log(java.util.logging.Logger logger, String message) {
		message = transform(message);
		if (writer != null) {
			writer.println(message);
			writer.flush();
		} else {
			// 使用框架自定义logger实现日志记录
			this.logger.info(message);
		}
	}
}
