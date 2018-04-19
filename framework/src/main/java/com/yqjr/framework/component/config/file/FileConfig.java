/**
 * 
 */
package com.yqjr.framework.component.config.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.yqjr.framework.component.config.Configuration;
import com.yqjr.framework.component.log.Logger;
import com.yqjr.framework.component.watcher.FileWatcher;
import com.yqjr.framework.utils.FileUtils;

/**
 * ClassName: FileConfigLoader <br>
 * Description: property文件配置加载组件 <br>
 * Create By: admin <br>
 * Create Date: 2017年4月17日 下午3:54:08 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public class FileConfig extends Configuration {

	private static FileConfig instance = null;

	private static final String PROPERTY_NAME = "applications.properties";

	private Logger logger = Logger.getLogger();

	private String configPath = null;

	/**
	 * Description: 获取实例 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年4月17日 下午3:57:47
	 *
	 * @return
	 */
	public static FileConfig getInstance() {
		if (instance == null) {
			synchronized (FileConfig.class) {
				if (instance == null) {
					instance = new FileConfig();
				}
			}
		}
		return instance;
	}

	/**
	 * 初始化FileConfigLoader
	 */
	private FileConfig() {
		// 获取用户配置文件路径
		this.configPath = FileUtils.getFilePath(PROPERTY_NAME);
		FileWatcher watcher = new FileWatcher("配置文件监听者", PROPERTY_NAME) {
			@Override
			protected void doOnChange() {
				// 初始化配置信息
				init();
			}
		};
		// 初始化配置信息
		long lastModified = init();
		watcher.setLastModified(lastModified);
		watcher.start();
	}

	protected long init() {
		Properties properties = new Properties();
		File file = new File(this.configPath);
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			properties.load(in);
		} catch (Exception e) {
			logger.error("配置文件读取异常", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// 装载配置信息
		for (Object property : properties.keySet()) {
			String key = (String) property;
			setProperties(key, properties.getProperty(key));
		}
		return file.lastModified();
	}

}
