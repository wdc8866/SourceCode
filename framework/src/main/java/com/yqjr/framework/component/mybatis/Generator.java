/**
 * 
 */
package com.yqjr.framework.component.mybatis;

import java.util.ArrayList;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.springframework.util.Assert;

/**
 * ClassName: Generator <br>
 * Description: 运行执行xml生成代码 <br>
 * Create By: admin <br>
 * Create Date: 2017年5月8日 下午7:06:41 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public class Generator {

	/**
	 * Description: 框架代码生成方法 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月15日 下午3:38:44
	 *
	 * @param modelPackage
	 *            模块名称,需指定模块完整包路径,例如:用户模块User,此参数需传递com.yqjr.modules.user
	 * @param className
	 *            实体类名称
	 * @param configFileName
	 *            配置文件名称,配置文件需在环境变量下能够读取到
	 */
	public static void generate(String modelPackage, String className, String configFileName) {
		Assert.hasText(modelPackage);
		Assert.hasText(className);
		Assert.hasText(configFileName);
		try {
			MyBatisGeneratorConfigurationParser parser = new MyBatisGeneratorConfigurationParser(className,
					modelPackage, configFileName.trim());
			Configuration config = parser.parseConfiguration();
			DefaultShellCallback callback = new DefaultShellCallback(true);
			MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, new ArrayList<String>());
			myBatisGenerator.generate(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
