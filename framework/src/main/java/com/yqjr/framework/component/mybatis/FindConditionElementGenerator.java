package com.yqjr.framework.component.mybatis;

import org.apache.commons.lang3.ArrayUtils;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import com.yqjr.framework.component.config.Configuration;
import com.yqjr.framework.component.dialect.Dialect;
import com.yqjr.framework.component.dialect.db.AbstractDialect;
import com.yqjr.framework.component.dialect.db.DBDateInfo;

/**
 * ClassName: FindConditionElementGenerator <br>
 * Description: 条件查询方法生成Condition条件 <br>
 * Create By: admin <br>
 * Create Date: 2017年5月15日 上午11:17:57 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public class FindConditionElementGenerator extends AbstractXmlElementGenerator {

	private static final String[] IGNORE_PROPERTIES = new String[] { "id", "createBy", "createDate", "updateBy",
			"updateDate", "remarks" };

	@Override
	public void addElements(XmlElement parentElement) {
		XmlElement answer = new XmlElement("select"); //$NON-NLS-1$
		answer.addAttribute(new Attribute("id", "findByCondition")); //$NON-NLS-1$
		answer.addAttribute(new Attribute("parameterType", String.format("%s.condition.%sCondition",
				context.getProperty("packageName"), context.getProperty("javaName"))));
		answer.addAttribute(new Attribute("resultMap", "BaseResultMap"));
		context.getCommentGenerator().addComment(answer);

		StringBuffer sb = new StringBuffer("select");
		answer.addElement(new TextElement(sb.toString()));
		// 引用公共列
		XmlElement sqlElement = new XmlElement("include");
		sqlElement.addAttribute(new Attribute("refid", "Base_Column_List"));
		answer.addElement(sqlElement);
		sb.setLength(0);
		sb.append("from ");
		sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
		answer.addElement(new TextElement(sb.toString()));
		sb.setLength(0);
		XmlElement ifElement = new XmlElement("if");
		ifElement.addAttribute(new Attribute("test", "_parameter != null"));
		// 通过表结构拼装查询条件
		XmlElement whereElement = new XmlElement("where");
		for (IntrospectedColumn introspectedColumn : ListUtilities
				.removeGeneratedAlwaysColumns(introspectedTable.getAllColumns())) {
			if (ArrayUtils.contains(IGNORE_PROPERTIES, introspectedColumn.getJavaProperty())) {
				continue;
			}
			XmlElement isNotNullElement = new XmlElement("if"); //$NON-NLS-1$
			sb.setLength(0);
			sb.append(introspectedColumn.getJavaProperty("")); //$NON-NLS-1$
			sb.append(" != null"); //$NON-NLS-1$
			isNotNullElement.addAttribute(new Attribute("test", sb.toString())); //$NON-NLS-1$
			whereElement.addElement(isNotNullElement);

			sb.setLength(0);
			sb.append(" and ");
			sb.append(MyBatis3FormattingUtilities.getAliasedEscapedColumnName(introspectedColumn));
			sb.append(" = "); //$NON-NLS-1$
			sb.append(FrameworkJavaTypeResolver.getJavaProperty(introspectedColumn, true));

			isNotNullElement.addElement(new TextElement(sb.toString()));
		}
		// 获取basecondition中条件
		getBaseCondition(whereElement);

		ifElement.addElement(whereElement);
		answer.addElement(ifElement);
		parentElement.addElement(answer);
	}

	/**
	 * Description: 生成baseCondition中内容 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月15日 下午8:08:06
	 *
	 * @param whereElement
	 */
	private void getBaseCondition(XmlElement whereElement) {

		// startDate
		StringBuffer sb = new StringBuffer();
		XmlElement isNotNullElement = new XmlElement("if"); //$NON-NLS-1$
		sb.setLength(0);
		sb.append("startDate"); // $NON-NLS-1$
		sb.append(" != null"); //$NON-NLS-1$
		isNotNullElement.addAttribute(new Attribute("test", sb.toString())); //$NON-NLS-1$
		whereElement.addElement(isNotNullElement);
		// 添加属性值
		sb.setLength(0);
		sb.append("<![CDATA[  and updateDate >= " + getToDate("#{startDate}") + " ]]> ");
		isNotNullElement.addElement(new TextElement(sb.toString()));

		// endDate
		isNotNullElement = new XmlElement("if"); //$NON-NLS-1$
		sb.setLength(0);
		sb.append("endDate"); // $NON-NLS-1$
		sb.append(" != null"); //$NON-NLS-1$
		isNotNullElement.addAttribute(new Attribute("test", sb.toString())); //$NON-NLS-1$
		whereElement.addElement(isNotNullElement);
		// 添加属性值
		sb.setLength(0);
		sb.append("<![CDATA[  and updateDate < " + getToDate("#{endDate}") + "+1 ]]> ");
		isNotNullElement.addElement(new TextElement(sb.toString()));

		// userId
		isNotNullElement = new XmlElement("if"); //$NON-NLS-1$
		sb.setLength(0);
		sb.append("userId > 0"); // $NON-NLS-1$
		isNotNullElement.addAttribute(new Attribute("test", sb.toString())); //$NON-NLS-1$
		whereElement.addElement(isNotNullElement);
		// 添加属性值
		sb.setLength(0);
		sb.append("and createBy = #{userId}");
		isNotNullElement.addElement(new TextElement(sb.toString()));
	}

	private String getToDate(String parameter) {
		Dialect dialect = AbstractDialect
				.getDialect(Configuration.getConfig().getStringValue("framework.db.type"));
		DBDateInfo dbDateInfo = dialect.getDateFormat();
		return String.format("%s(%s,'%s')", dbDateInfo.getToDateMethod(), parameter, dbDateInfo.getYYMMDD());
	}

}
