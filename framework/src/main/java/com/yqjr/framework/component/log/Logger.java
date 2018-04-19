package com.yqjr.framework.component.log;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.util.Assert;

import com.yqjr.framework.component.context.ThreadContext;
import com.yqjr.framework.datatype.BizzException;
import com.yqjr.framework.utils.Constants;

/**
 * ClassName: Logger <br>
 * Description: 框架日志组件,基于apache log4j封装<br>
 * Create By: admin <br>
 * Create Date: 2017年4月17日 下午3:29:44 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public class Logger {

	private static final String PROPERTY_NAME = "log4j.xml";

	private static Logger instance = null;

	private String configPath = null;

	/**
	 * 初始化logger对象
	 */
	private Logger() {
		// 检查framework.env.home变量是否存在
		if (System.getenv(Constants.ENV) != null) {
			configPath = System.getenv(Constants.ENV) + File.separator + PROPERTY_NAME;
		}
		// 不存在则检查环境变量
		else {
			URL url = Thread.currentThread().getContextClassLoader().getResource(PROPERTY_NAME);
			Assert.notNull(url, String.format("配置文件%s不存在", PROPERTY_NAME));
			try {
				configPath = URLDecoder.decode(url.getPath(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new BizzException(e);
			}
		}
		// 初始化log4j配置
		init();
	}

	/**
	 * Description: 初始化log4j配置 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年4月17日 下午3:44:36
	 *
	 */
	private void init() {
		File file = new File(this.configPath);
		Assert.isTrue(file.exists(), String.format("配置文件%s不存在", PROPERTY_NAME));
		LogManager.resetConfiguration();
		DOMConfigurator.configureAndWatch(this.configPath);
	}

	/**
	 * Description: 获取Logger实例 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年4月17日 下午3:38:58
	 *
	 * @return
	 */
	public static Logger getLogger() {
		if (instance == null) {
			synchronized (Logger.class) {
				if (instance == null) {
					instance = new Logger();
				}
			}
		}
		return instance;
	}

	/**
	 * Description: 按照debug级别,以framework logger配置记录日志 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年4月17日 下午3:39:18
	 *
	 * @param message
	 *            待记录信息
	 */
	public void debug(String message) {
		org.apache.log4j.Logger logger = getLogger(Constants.FRAMEWORK_CATEGORY);
		if (logger.isDebugEnabled()) {
			logger.debug(formatMessage(message));
		}
	}

	/**
	 * Description: 按照info级别,以framework logger配置记录日志 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年4月17日 下午3:39:18
	 *
	 * @param message
	 *            待记录信息
	 */
	public void info(String message) {
		org.apache.log4j.Logger logger = getLogger(Constants.FRAMEWORK_CATEGORY);
		if (logger.isInfoEnabled()) {
			logger.info(formatMessage(message));
		}
	}

	/**
	 * Description: 按照error级别,以framework logger配置记录日志 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年4月17日 下午3:39:18
	 *
	 * @param message
	 *            待记录信息
	 * @param exception
	 *            异常信息
	 */
	public void error(String message, Throwable exception) {
		org.apache.log4j.Logger logger = getLogger(Constants.FRAMEWORK_CATEGORY);
		logger.error(formatMessage(message), exception);
	}

	/**
	 * Description: 按照warn级别,以framework logger配置记录日志 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年4月17日 下午3:39:18
	 *
	 * @param message
	 *            待记录信息
	 */
	public void warn(String message) {
		org.apache.log4j.Logger logger = getLogger(Constants.FRAMEWORK_CATEGORY);
		logger.warn(formatMessage(message));
	}

	/**
	 * Description: 按照debug级别,以指定logger配置记录日志 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年4月17日 下午3:41:33
	 *
	 * @param category
	 *            logger配置
	 * @param message
	 *            待记录信息
	 */
	public void debug(String category, String message) {
		org.apache.log4j.Logger logger = getLogger(category);
		if (logger.isDebugEnabled()) {
			logger.debug(formatMessage(message));
		}
	}

	/**
	 * Description: 按照info级别,以指定logger配置记录日志 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年4月17日 下午3:41:33
	 *
	 * @param category
	 *            logger配置
	 * @param message
	 *            待记录信息
	 */
	public void info(String category, String message) {
		org.apache.log4j.Logger logger = getLogger(category);
		if (logger.isInfoEnabled()) {
			logger.info(formatMessage(message));
		}
	}

	/**
	 * Description: 按照error级别,以指定logger配置记录日志 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年4月17日 下午3:42:55
	 *
	 * @param category
	 *            logger配置
	 * @param message
	 *            待记录信息
	 * @param exception
	 *            异常信息
	 */
	public void error(String category, String message, Throwable exception) {
		org.apache.log4j.Logger logger = getLogger(category);
		logger.error(formatMessage(message), exception);
	}

	/**
	 * Description: 按照warn级别,以指定logger配置记录日志 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年4月17日 下午3:41:33
	 *
	 * @param category
	 *            logger配置
	 * @param message
	 *            待记录信息
	 */
	public void warn(String category, String message) {
		org.apache.log4j.Logger logger = getLogger(category);
		logger.warn(formatMessage(message));
	}

	/**
	 * Description: 获取log4j logger对象 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年4月17日 下午3:42:34
	 *
	 * @param category
	 * @return
	 */
	private org.apache.log4j.Logger getLogger(String category) {
		Assert.hasText(category);
		// 由于log4j内部做了缓存,所以此处不做缓存处理了
		return org.apache.log4j.Logger.getLogger(category);
	}

	/**
	 * Description: 格式化日志,添加线程Id和sessionId <br>
	 * Create By: admin <br>
	 * Create Data: 2016年12月22日 下午4:56:50
	 *
	 * @param message
	 * @return
	 */
	private String formatMessage(String message) {
		StringBuffer logMessage = new StringBuffer();
		logMessage.append(String.format("%08d", Thread.currentThread().getId())).append("\t");
		String sessionId = ThreadContext.getInstance().getString(Constants.SESSIONID);
		if (StringUtils.isNotBlank(sessionId)) {
			logMessage.append(sessionId).append("\t");
		}
		logMessage.append(message);
		return logMessage.toString();
	}
}
