/**
 * 
 */
package com.yqjr.framework.component.bean;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;

import com.yqjr.framework.component.config.Configuration;
import com.yqjr.framework.component.context.SpringContext;
import com.yqjr.framework.component.log.Logger;
import com.yqjr.framework.datatype.BizzException;
import com.yqjr.framework.utils.FileUtils;

/**
 * ClassName: ExtensionComponentLoader <br>
 * Description: 扩展组件加载器 <br>
 * Create By: admin <br>
 * Create Date: 2017年5月3日 上午10:48:57 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public class ExtensionComponentLoader {

	private Logger logger = Logger.getLogger();

	private static ExtensionComponentLoader instance = null;

	private ConfigurableApplicationContext context;

	private ExtensionComponentLoader() {
		context = (ConfigurableApplicationContext) SpringContext.getInstance().getApplicationContext();
	}

	public static ExtensionComponentLoader getInstance() {
		if (instance == null) {
			synchronized (ExtensionComponentLoader.class) {
				if (instance == null) {
					instance = new ExtensionComponentLoader();
				}
			}
		}
		return instance;
	}

	/**
	 * Description: 加载扩展组件 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月3日 上午10:50:53
	 */
	public void loadComponent() {
		// 读取框架配置文件
		String components = Configuration.getConfig().getStringValue("framework.components");
		if (StringUtils.isBlank(components)) {
			logger.info("尚未配置扩展组件,加载完毕");
		}
		// 加载配置组件
		else {
			logger.info("加载扩展组件开始...");
			// 初始化beanDefinition
			XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(
					(BeanDefinitionRegistry) context.getBeanFactory());
			beanDefinitionReader.setResourceLoader(context);
			beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(context));
			// 加载配置文件
			String[] componentConfigs = components.split(",");
			for (String config : componentConfigs)
				try {
					Assert.hasText(config, "组件配置信息不能为空");
					String filePath = FileUtils.getFilePath(config);
					logger.info("获取到组件配置信息:" + filePath);
					beanDefinitionReader.loadBeanDefinitions(context.getResources(filePath));
					logger.info("加载扩展组件完毕");
				} catch (Exception e) {
					logger.error("加载扩展组件异常", e);
					throw new BizzException(e);
				}
		}
	}

}
