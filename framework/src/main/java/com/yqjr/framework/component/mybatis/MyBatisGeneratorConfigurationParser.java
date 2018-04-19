package com.yqjr.framework.component.mybatis;

/**
 * 基于org.mybatis.generator.config.xml.MyBatisGeneratorConfigurationParser修改<br>
 * 简化配置文件内容,部分内容从全局配置文件中读取<br>
 * 
 * @author admin
 */
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.config.ColumnOverride;
import org.mybatis.generator.config.CommentGeneratorConfiguration;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.config.JavaClientGeneratorConfiguration;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;
import org.mybatis.generator.config.JavaTypeResolverConfiguration;
import org.mybatis.generator.config.ModelType;
import org.mybatis.generator.config.PluginConfiguration;
import org.mybatis.generator.config.PropertyHolder;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.config.SqlMapGeneratorConfiguration;
import org.mybatis.generator.config.TableConfiguration;
import org.springframework.util.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.yqjr.framework.base.BaseEntity;
import com.yqjr.modules.user.entity.User;

/**
 * This class parses configuration files into the new Configuration API
 * 
 * @author Jeff Butler
 */
public class MyBatisGeneratorConfigurationParser {

	/**
	 * 模块名称
	 */
	private String javaName;

	/**
	 * 模块包名称
	 */
	private String packageName;

	/**
	 * 工程目录
	 */
	private String targetProject;

	/**
	 * 配置文件
	 */
	private String configFile;

	public MyBatisGeneratorConfigurationParser(String javaName, String packageName, String configFile) {
		this.javaName = javaName;
		this.packageName = packageName;
		this.targetProject = "src/main/java";
		this.configFile = configFile;
	}

	/**
	 * Description: 解析配置文件 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月15日 上午11:34:24
	 *
	 * @return
	 */
	public Configuration parseConfiguration() {
		Configuration configuration = new Configuration();
		parseContext(configuration);
		return configuration;
	}

	/**
	 * Description: 解析context配置 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月15日 上午11:34:52
	 *
	 * @param configuration
	 */
	private void parseContext(Configuration configuration) {
		ModelType mt = ModelType.FLAT;
		// 初始化解析容器
		Context context = new Context(mt);
		// 默认使用模块名称作为ID
		context.setId(javaName);
		configuration.addContext(context);
		// 读取配置文件
		Element rootNode = loadDocument();
		// 解析table配置
		parseTable(context, getTableNode(rootNode));
		// 添加后续使用属性
		context.addProperty("targetProject", targetProject);
		context.addProperty("packageName", packageName);
		context.addProperty("javaName", javaName);
		// 解析插件FrameworkPluginAdapter
		parsePlugin(context);
		// 解析注释
		parseCommentGenerator(context);
		// 解析类型解析器
		parseJavaTypeResolver(context);
		// 解析jdbc连接器
		parseJdbcConnection(configuration, context, getJdbcNode(rootNode));
		// 解析实体生成器
		parseJavaModelGenerator(context);
		// 解析mybatis映射文件生成器
		parseSqlMapGenerator(context);
		// 解析dao生成器
		parseJavaClientGenerator(context);
	}

	/**
	 * Description: 读取配置文件 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年6月6日 上午8:23:01
	 *
	 * @return
	 */
	private Element loadDocument() {
		InputStream in = null;
		try {
			// 从当前环境变量中获取配置文件
			in = Thread.currentThread().getContextClassLoader().getResourceAsStream(configFile);
			// 根据配置文件初始化Document
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(in);
			return document.getDocumentElement();
		} catch (Exception e) {
			System.out.println("配置文件加载失败," + configFile);
			throw new RuntimeException(e);
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	/**
	 * Description: 获取简化配置文件后的table节点 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月15日 上午11:36:48
	 *
	 * @return
	 */
	private Node getTableNode(Element rootNode) {
		NodeList nodeList = rootNode.getElementsByTagName("table");
		if (nodeList == null) {
			throw new RuntimeException("table节点配置异常");
		}
		return nodeList.item(0);
	}

	/**
	 * Description: 获取JDBC配置节点 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年6月6日 上午11:27:09
	 *
	 * @param document
	 * @return
	 */
	private Node getJdbcNode(Element rootNode) {
		NodeList nodeList = rootNode.getElementsByTagName("jdbc");
		if (nodeList == null || nodeList.getLength() != 1) {
			throw new RuntimeException("jdbc节点配置异常");
		}
		return nodeList.item(0);
	}

	/**
	 * Description: 解析sqlMap <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月15日 上午11:40:51
	 *
	 * @param context
	 */
	protected void parseSqlMapGenerator(Context context) {
		SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = new SqlMapGeneratorConfiguration();
		context.setSqlMapGeneratorConfiguration(sqlMapGeneratorConfiguration);
		sqlMapGeneratorConfiguration.setTargetPackage(String.format("%s.xml", packageName));
		sqlMapGeneratorConfiguration.setTargetProject(targetProject);
	}

	/**
	 * Description: 解析注释 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月26日 下午7:06:49
	 *
	 * @param context
	 * @param node
	 */
	protected void parseCommentGenerator(Context context) {
		CommentGeneratorConfiguration commentGeneratorConfiguration = new CommentGeneratorConfiguration();
		context.setCommentGeneratorConfiguration(commentGeneratorConfiguration);
		commentGeneratorConfiguration.addProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_ALL_COMMENTS,
				String.valueOf(false));
	}

	/**
	 * Description: 解析java类型解析器 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月16日 下午3:01:37
	 *
	 * @param context
	 */
	protected void parseJavaTypeResolver(Context context) {
		JavaTypeResolverConfiguration javaTypeResolverConfiguration = new JavaTypeResolverConfiguration();
		context.setJavaTypeResolverConfiguration(javaTypeResolverConfiguration);
		javaTypeResolverConfiguration.setConfigurationType(FrameworkJavaTypeResolver.class.getName());
	}

	/**
	 * Description: 解析table <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月15日 上午11:42:08
	 *
	 * @param context
	 * @param node
	 */
	protected void parseTable(Context context, Node node) {
		TableConfiguration tc = new TableConfiguration(context);
		context.addTableConfiguration(tc);
		Properties attributes = parseAttributes(node);
		String schema = attributes.getProperty("schema"); //$NON-NLS-1$
		String tableName = attributes.getProperty("tableName"); //$NON-NLS-1$
		// 新增ID属性解析
		String idColumn = attributes.getProperty("id") == null ? "id" : attributes.getProperty("id");
		context.addProperty("tableIdColumn", idColumn);

		if (stringHasValue(schema)) {
			tc.setSchema(schema);
		}
		if (stringHasValue(tableName)) {
			tc.setTableName(tableName);
		}
		tc.setDomainObjectName(javaName);
		// 关闭原有生成组件功能
		tc.setInsertStatementEnabled(true);
		tc.setSelectByPrimaryKeyStatementEnabled(false);
		tc.setSelectByExampleStatementEnabled(false);
		tc.setUpdateByPrimaryKeyStatementEnabled(false);
		tc.setDeleteByPrimaryKeyStatementEnabled(false);
		tc.setDeleteByExampleStatementEnabled(false);
		tc.setCountByExampleStatementEnabled(false);
		tc.setUpdateByExampleStatementEnabled(false);
		tc.setMapperName(javaName + "Dao");

		// 添加表公共字段
		addBaseColumn(tc);

		// 添加自定义字段
		NodeList nodeList = node.getChildNodes();
		String idProperty = attributes.getProperty("id") == null ? "id" : null;
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node childNode = nodeList.item(i);
			if (childNode.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			if ("columnOverride".equals(childNode.getNodeName())) { //$NON-NLS-1$
				ColumnOverride co = parseColumnOverride(tc, childNode);
				if (idColumn.equalsIgnoreCase(co.getColumnName())) {
					idProperty = co.getJavaProperty();
				}
			}
		}
		Assert.notNull(idProperty, "检查id属性配置是否正确");
		context.addProperty("tableIdProperty", idProperty);
	}

	/**
	 * Description: 添加公共字段 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月16日 上午10:58:01
	 *
	 * @param tc
	 */
	private void addBaseColumn(TableConfiguration tc) {

		// ID
		ColumnOverride co = new ColumnOverride("ID");
		co.setJavaProperty("id");
		tc.addColumnOverride(co);

		// createBy
		co = new ColumnOverride("CREATEBY");
		co.setJavaProperty("createBy");
		co.setJavaType(User.class.getName());
		tc.addColumnOverride(co);

		// updateBy
		co = new ColumnOverride("UPDATEBY");
		co.setJavaProperty("updateBy");
		co.setJavaType(User.class.getName());
		tc.addColumnOverride(co);

		// createDate
		co = new ColumnOverride("CREATEDATE");
		co.setJavaProperty("createDate");
		tc.addColumnOverride(co);

		// updateDate
		co = new ColumnOverride("UPDATEDATE");
		co.setJavaProperty("updateDate");
		tc.addColumnOverride(co);

		// remark
		co = new ColumnOverride("REMARKS");
		co.setJavaProperty("remarks");
		tc.addColumnOverride(co);

		// status
		co = new ColumnOverride("STATUS");
		co.setJavaProperty("status");
		tc.addColumnOverride(co);
	}

	/**
	 * Description: 解析表格 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月15日 上午11:55:17
	 *
	 * @param tc
	 * @param node
	 */
	private ColumnOverride parseColumnOverride(TableConfiguration tc, Node node) {
		Properties attributes = parseAttributes(node);
		String column = attributes.getProperty("column"); //$NON-NLS-1$
		String property = attributes.getProperty("property"); //$NON-NLS-1$
		String javaType = attributes.getProperty("javaType"); //$NON-NLS-1$
		String jdbcType = attributes.getProperty("jdbcType"); //$NON-NLS-1$
		String typeHandler = attributes.getProperty("typeHandler"); //$NON-NLS-1$

		ColumnOverride co = new ColumnOverride(column);

		if (stringHasValue(property)) {
			co.setJavaProperty(property);
		} else {
			// 如果没有指定property属性,则按照下划线进行分词
			String[] words = column.split("_");
			for (int i = 0; i < words.length; i++) {
				words[i] = StringUtils.capitalize(words[i].toLowerCase());
			}
			co.setJavaProperty(StringUtils.uncapitalize(StringUtils.join(words, "")));
		}
		if (stringHasValue(javaType)) {
			co.setJavaType(javaType);
		}
		if (stringHasValue(jdbcType)) {
			co.setJdbcType(jdbcType);
		}
		if (stringHasValue(typeHandler)) {
			co.setTypeHandler(typeHandler);
		}
		tc.addColumnOverride(co);

		return co;
	}

	private void parsePlugin(Context context) {
		PluginConfiguration pluginConfiguration = new PluginConfiguration();
		context.addPluginConfiguration(pluginConfiguration);
		pluginConfiguration.setConfigurationType(FrameworkPluginAdapter.class.getName());
	}

	protected void parseJavaModelGenerator(Context context) {
		JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = new JavaModelGeneratorConfiguration();
		context.setJavaModelGeneratorConfiguration(javaModelGeneratorConfiguration);
		javaModelGeneratorConfiguration.setTargetPackage(String.format("%s.entity", packageName));
		javaModelGeneratorConfiguration.setTargetProject(targetProject);
		// 添加实体类继承关系
		javaModelGeneratorConfiguration.addProperty(PropertyRegistry.ANY_ROOT_CLASS, BaseEntity.class.getName());
	}

	private void parseJavaClientGenerator(Context context) {
		JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = new JavaClientGeneratorConfiguration();
		context.setJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration);
		javaClientGeneratorConfiguration.setTargetPackage(String.format("%s.dao", packageName));
		javaClientGeneratorConfiguration.setTargetProject(targetProject);
		javaClientGeneratorConfiguration.setConfigurationType("XMLMAPPER");
	}

	protected void parseJdbcConnection(Configuration configuration, Context context, Node node) {
		JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration();
		context.setJdbcConnectionConfiguration(jdbcConnectionConfiguration);
		// 解析jdbc节点
		Properties attributes = parseAttributes(node);
		String jdbcUrl = attributes.getProperty("jdbcUrl"); //$NON-NLS-1$
		String jdbcJar = attributes.getProperty("jdbcJar"); //$NON-NLS-1$
		String jdbcUser = attributes.getProperty("jdbcUser"); //$NON-NLS-1$
		String jdbcPass = attributes.getProperty("jdbcPass"); //$NON-NLS-1$
		// 节点校验
		Assert.hasText(jdbcUrl);
		Assert.hasText(jdbcJar);
		Assert.hasText(jdbcUser);
		Assert.hasText(jdbcPass);
		// 配置jdbc
		jdbcConnectionConfiguration.setDriverClass("oracle.jdbc.driver.OracleDriver");
		jdbcConnectionConfiguration.setConnectionURL(jdbcUrl);
		jdbcConnectionConfiguration.setUserId(jdbcUser);
		jdbcConnectionConfiguration.setPassword(jdbcPass);
		// 配置jdbcJar
		configuration.addClasspathEntry(jdbcJar);
	}

	protected void parseProperty(PropertyHolder propertyHolder, Node node) {
		Properties attributes = parseAttributes(node);

		String name = attributes.getProperty("name"); //$NON-NLS-1$
		String value = attributes.getProperty("value"); //$NON-NLS-1$

		propertyHolder.addProperty(name, value);
	}

	protected Properties parseAttributes(Node node) {
		Properties attributes = new Properties();
		NamedNodeMap nnm = node.getAttributes();
		for (int i = 0; i < nnm.getLength(); i++) {
			Node attribute = nnm.item(i);
			String value = attribute.getNodeValue();
			attributes.put(attribute.getNodeName(), value);
		}
		return attributes;
	}
}
