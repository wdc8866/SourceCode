/**
 * 
 */
package com.yqjr.framework.component.mybatis;

import static org.mybatis.generator.internal.util.JavaBeansUtil.getJavaBeansField;
import static org.mybatis.generator.internal.util.JavaBeansUtil.getJavaBeansGetter;
import static org.mybatis.generator.internal.util.JavaBeansUtil.getJavaBeansSetter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.BaseColumnListElementGenerator;
import org.springframework.stereotype.Service;

import com.yqjr.framework.annotation.FrameworkDao;
import com.yqjr.framework.base.BaseCondition;
import com.yqjr.framework.base.BaseDao;
import com.yqjr.framework.base.BaseEntity;
import com.yqjr.framework.base.BaseModel;
import com.yqjr.framework.base.BaseService;
import com.yqjr.framework.component.context.ThreadContext;

/**
 * ClassName: FrameworkPluginAdapter <br>
 * Description: 框架代码生成工具,主要针对mybatis的entity,model及mapper.xml进行生成,基于mbg <br>
 * Create By: admin <br>
 * Create Date: 2017年5月8日 下午6:22:20 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public class FrameworkPluginAdapter extends PluginAdapter {

	private static final String TYPE_ARGS_KEY = "framework.type.args";

	private List<String> warnings = null;

	private static final String[] IGNORE_PROPERTIES = new String[] { "id", "remarks", "createBy", "createDate",
			"updateBy", "updateDate", "status" };

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mybatis.generator.api.Plugin#validate(java.util.List)
	 */
	@Override
	public boolean validate(List<String> warnings) {
		this.warnings = warnings;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mybatis.generator.api.PluginAdapter#clientGenerated(org.mybatis.
	 * generator.api.dom.java.Interface,
	 * org.mybatis.generator.api.dom.java.TopLevelClass,
	 * org.mybatis.generator.api.IntrospectedTable)
	 */
	/**
	 * Description: DAO生成方法 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月11日 下午5:19:10
	 *
	 * @param interfaze
	 * @param topLevelClass
	 * @param introspectedTable
	 * @return
	 */
	@Override
	public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {

		String javaName = context.getProperty("javaName");
		String packageName = context.getProperty("packageName");

		// 获取SuperInterface
		Set<FullyQualifiedJavaType> superInterfaces = interfaze.getSuperInterfaceTypes();
		Iterator<FullyQualifiedJavaType> iter = superInterfaces.iterator();
		FullyQualifiedJavaType javaType = null;
		while (iter.hasNext()) {
			FullyQualifiedJavaType tmp = iter.next();
			if (BaseDao.class.getName().equals(tmp.getFullyQualifiedName())) {
				javaType = tmp;
				superInterfaces.remove(tmp);
				break;
			}
		}
		// 如果为空则初始化
		if (javaType == null) {
			javaType = new FullyQualifiedJavaType(BaseDao.class.getName());
		}
		// 添加SuperInterfaces泛型
		List<FullyQualifiedJavaType> typeArgs = ThreadContext.getInstance().getObject(TYPE_ARGS_KEY);
		// 使用完毕后清除
		ThreadContext.getInstance().remove(TYPE_ARGS_KEY);
		List<FullyQualifiedJavaType> interfaceArgs = javaType.getTypeArguments();
		interfaceArgs.addAll(typeArgs);
		FullyQualifiedJavaType conditionType = new FullyQualifiedJavaType(
				String.format("%s.condition.%sCondition", packageName, javaName));
		interfaceArgs.add(conditionType);
		interfaze.addSuperInterface(javaType);
		for (FullyQualifiedJavaType typeArg : typeArgs) {
			interfaze.addImportedType(typeArg);
		}
		interfaze.addImportedType(conditionType);

		// 添加@FrameworkDao
		FullyQualifiedJavaType annotationType = new FullyQualifiedJavaType(FrameworkDao.class.getName());
		FullyQualifiedJavaType entityType = new FullyQualifiedJavaType(BaseDao.class.getName());
		interfaze.addImportedType(annotationType);
		interfaze.addImportedType(entityType);
		interfaze.addAnnotation("@FrameworkDao");

		context.addProperty("daoTypeName", interfaze.getType().getShortNameWithoutTypeArguments());

		return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mybatis.generator.api.PluginAdapter#clientInsertMethodGenerated(org.
	 * mybatis.generator.api.dom.java.Method,
	 * org.mybatis.generator.api.dom.java.Interface,
	 * org.mybatis.generator.api.IntrospectedTable)
	 */
	@Override
	public boolean clientInsertMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mybatis.generator.api.PluginAdapter#
	 * clientInsertSelectiveMethodGenerated(org.mybatis.generator.api.dom.java.
	 * Method, org.mybatis.generator.api.dom.java.Interface,
	 * org.mybatis.generator.api.IntrospectedTable)
	 */
	@Override
	public boolean clientInsertSelectiveMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mybatis.generator.api.PluginAdapter#modelBaseRecordClassGenerated(org
	 * .mybatis.generator.api.dom.java.TopLevelClass,
	 * org.mybatis.generator.api.IntrospectedTable)
	 */
	/**
	 * Description: entity生成方法 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月11日 下午5:19:27
	 *
	 * @param topLevelClass
	 * @param introspectedTable
	 * @return
	 */
	@Override
	public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		// 获取SuperClass
		FullyQualifiedJavaType superClass = topLevelClass.getSuperClass();
		// 获取泛型
		FullyQualifiedJavaType idType = getIdentifyType(topLevelClass);
		FullyQualifiedJavaType entityType = topLevelClass.getType();
		// 添加superClass泛型
		List<FullyQualifiedJavaType> typeArgs = superClass.getTypeArguments();
		typeArgs.add(idType);
		typeArgs.add(entityType);
		// 后续生成时使用
		ThreadContext.getInstance().put(TYPE_ARGS_KEY, typeArgs);
		topLevelClass.setSuperClass(superClass);
		// 添加import
		topLevelClass.addImportedType(new FullyQualifiedJavaType(BaseEntity.class.getName()));
		// 处理ID
		Iterator<Field> iterator = topLevelClass.getFields().iterator();
		while (iterator.hasNext()) {
			Field field = iterator.next();
			if ("id".equals(field.getName())) {
				iterator.remove();
			}
		}
		context.addProperty("idTypeName", idType.getShortNameWithoutTypeArguments());
		context.addProperty("entityTypeName", entityType.getShortNameWithoutTypeArguments());

		return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mybatis.generator.api.PluginAdapter#sqlMapDocumentGenerated(org.
	 * mybatis.generator.api.dom.xml.Document,
	 * org.mybatis.generator.api.IntrospectedTable)
	 */
	/**
	 * Description: mapper.xml生成方法 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月11日 下午5:19:27
	 *
	 * @param document
	 * @param introspectedTable
	 * @return
	 */
	@Override
	public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {

		// 清空原始document
		document.getRootElement().getElements().clear();
		// 生成BaseResultMap
		executeGenerator(new ResultMapElementGenerator(), introspectedTable, document);
		// 生成BaseColumnList
		executeGenerator(new BaseColumnListElementGenerator(), introspectedTable, document);
		// 生成BaseWhereCondition
		executeGenerator(new WhereElementGenerator(), introspectedTable, document);
		// 生成insert
		executeGenerator(new InsertElementGenerator(), introspectedTable, document);
		// 生成update
		executeGenerator(new UpdateElementGenerator(), introspectedTable, document);
		// 生成delete
		executeGenerator(new DeleteElementGenerator(), introspectedTable, document);
		// 生成id
		executeGenerator(new IdElementGenerator(), introspectedTable, document);
		// 生成get
		executeGenerator(new GetElementGenerator(), introspectedTable, document);
		// 生成findList
		executeGenerator(new FindListElementGenerator(), introspectedTable, document);
		// 生成findByCondition
		executeGenerator(new FindConditionElementGenerator(), introspectedTable, document);
		return super.sqlMapDocumentGenerated(document, introspectedTable);
	}

	private void executeGenerator(AbstractXmlElementGenerator elementGenerator, IntrospectedTable introspectedTable,
			Document document) {
		elementGenerator.setContext(context);
		elementGenerator.setIntrospectedTable(introspectedTable);
		elementGenerator.setProgressCallback(null);
		elementGenerator.setWarnings(warnings);
		elementGenerator.addElements(document.getRootElement());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mybatis.generator.api.PluginAdapter#
	 * contextGenerateAdditionalJavaFiles(org.mybatis.generator.api.
	 * IntrospectedTable)
	 */
	/**
	 * Description: 其他文件生成方法 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月11日 下午5:19:27
	 *
	 * @param introspectedTable
	 * @return
	 */
	@Override
	public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
		List<GeneratedJavaFile> extensionFiles = new ArrayList<GeneratedJavaFile>();
		// 获取上下文变量
		String javaName = context.getProperty("javaName");
		String targetProject = context.getProperty("targetProject");
		String packageName = context.getProperty("packageName");

		// 生成service文件
		TopLevelClass serviceClass = new TopLevelClass(String.format("%s.service.%sService", packageName, javaName));
		serviceClass.setVisibility(JavaVisibility.PUBLIC);
		// 添加继承
		FullyQualifiedJavaType superClass = new FullyQualifiedJavaType("BaseService");
		superClass.addTypeArgument(new FullyQualifiedJavaType(context.getProperty("idTypeName")));
		superClass.addTypeArgument(new FullyQualifiedJavaType(context.getProperty("daoTypeName")));
		superClass.addTypeArgument(new FullyQualifiedJavaType(context.getProperty("entityTypeName")));
		superClass.addTypeArgument(
				new FullyQualifiedJavaType(String.format("%s.condition.%sCondition", packageName, javaName)));
		superClass
				.addTypeArgument(new FullyQualifiedJavaType(String.format("%s.model.%sModel", packageName, javaName)));
		serviceClass.setSuperClass(superClass);
		// 添加引用
		serviceClass.addImportedType(String.format("%s.dao.%s", packageName, context.getProperty("daoTypeName")));
		serviceClass.addImportedType(String.format("%s.entity.%s", packageName, context.getProperty("entityTypeName")));
		serviceClass.addImportedType(BaseService.class.getName());
		serviceClass.addImportedType(Service.class.getName());
		serviceClass.addImportedType(String.format("%s.condition.%sCondition", packageName, javaName));
		serviceClass.addImportedType(String.format("%s.model.%sModel", packageName, javaName));
		// 添加注解
		serviceClass.addAnnotation("@Service");
		GeneratedJavaFile serviceFile = new GeneratedJavaFile(serviceClass, targetProject, context.getJavaFormatter());
		extensionFiles.add(serviceFile);

		// 生成model文件
		TopLevelClass modelClass = new TopLevelClass(String.format("%s.model.%sModel", packageName, javaName));
		modelClass.setVisibility(JavaVisibility.PUBLIC);
		// 添加继承
		superClass = new FullyQualifiedJavaType("BaseModel");
		superClass.addTypeArgument(new FullyQualifiedJavaType(context.getProperty("idTypeName")));
		superClass.addTypeArgument(new FullyQualifiedJavaType(modelClass.getType().getShortNameWithoutTypeArguments()));
		modelClass.setSuperClass(superClass);
		modelClass.addImportedType(BaseModel.class.getName());
		// 添加model内容
		for (IntrospectedColumn introspectedColumn : introspectedTable.getBaseColumns()) {
			if (ArrayUtils.contains(IGNORE_PROPERTIES, introspectedColumn.getJavaProperty())) {
				continue;
			}
			// 添加属性
			Field field = getJavaBeansField(introspectedColumn, context, introspectedTable);
			modelClass.addField(field);
			modelClass.addImportedType(field.getType());
			// 添加get方法
			Method method = getJavaBeansGetter(introspectedColumn, context, introspectedTable);
			modelClass.addMethod(method);
			// 添加set方法
			if (!introspectedTable.isImmutable()) {
				method = getJavaBeansSetter(introspectedColumn, context, introspectedTable);
				modelClass.addMethod(method);
			}
		}
		GeneratedJavaFile modelFile = new GeneratedJavaFile(modelClass, targetProject, context.getJavaFormatter());
		extensionFiles.add(modelFile);

		// 生成condition文件
		TopLevelClass conditionClass = new TopLevelClass(
				String.format("%s.condition.%sCondition", packageName, javaName));
		conditionClass.setVisibility(JavaVisibility.PUBLIC);
		// 添加继承
		superClass = new FullyQualifiedJavaType("BaseCondition");
		superClass.addTypeArgument(
				new FullyQualifiedJavaType(String.format("%s.condition.%sCondition", packageName, javaName)));
		conditionClass.setSuperClass(superClass);
		conditionClass
				.addImportedType(String.format("%s.entity.%s", packageName, context.getProperty("entityTypeName")));
		conditionClass.addImportedType(BaseCondition.class.getName());
		// 添加condition内容
		for (IntrospectedColumn introspectedColumn : introspectedTable.getBaseColumns()) {
			if (ArrayUtils.contains(IGNORE_PROPERTIES, introspectedColumn.getJavaProperty())) {
				continue;
			}
			// 添加属性
			Field field = getJavaBeansField(introspectedColumn, context, introspectedTable);
			conditionClass.addField(field);
			conditionClass.addImportedType(field.getType());
			// 添加get方法
			Method method = getJavaBeansGetter(introspectedColumn, context, introspectedTable);
			conditionClass.addMethod(method);
			// 添加set方法
			if (!introspectedTable.isImmutable()) {
				method = getJavaBeansSetter(introspectedColumn, context, introspectedTable);
				conditionClass.addMethod(method);
			}
		}
		GeneratedJavaFile conditionFile = new GeneratedJavaFile(conditionClass, targetProject,
				context.getJavaFormatter());
		extensionFiles.add(conditionFile);

		return extensionFiles;
	}

	/**
	 * Description: 获取ID的字段类型 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月15日 下午1:18:28
	 *
	 * @param topLevelClass
	 * @return
	 */
	private FullyQualifiedJavaType getIdentifyType(TopLevelClass topLevelClass) {
		// 查找ID对应的类型
		String idProperty = context.getProperty("tableIdProperty");
		for (Field field : topLevelClass.getFields()) {
			if (idProperty.equals(field.getName())) {
				// 如果是基础类型则返回包装类
				if (field.getType().isPrimitive()) {
					return field.getType().getPrimitiveTypeWrapper();
				}
				return field.getType();
			}
		}
		// 如果查询失败默认返回string型
		return FullyQualifiedJavaType.getStringInstance();
	}
}
