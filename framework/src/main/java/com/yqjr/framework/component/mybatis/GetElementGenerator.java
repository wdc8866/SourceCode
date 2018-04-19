package com.yqjr.framework.component.mybatis;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

/**
 * ClassName: GetElementGenerator <br>
 * Description: 单条查询方法 <br>
 * Create By: admin <br>
 * Create Date: 2017年5月15日 上午11:21:35 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public class GetElementGenerator extends AbstractXmlElementGenerator {

	@Override
	public void addElements(XmlElement parentElement) {
		XmlElement answer = new XmlElement("select"); //$NON-NLS-1$
		answer.addAttribute(new Attribute("id", "get")); //$NON-NLS-1$
		FullyQualifiedJavaType parameterType = introspectedTable.getRules().calculateAllFieldsClass();
		answer.addAttribute(new Attribute("parameterType", parameterType.getFullyQualifiedName()));
		answer.addAttribute(new Attribute("resultMap", "BaseResultMap"));
		context.getCommentGenerator().addComment(answer);
		// 引用公共列
		StringBuffer sb = new StringBuffer("select");
		answer.addElement(new TextElement(sb.toString()));
		XmlElement sqlElement = new XmlElement("include");
		sqlElement.addAttribute(new Attribute("refid", "Base_Column_List"));
		answer.addElement(sqlElement);

		sb.setLength(0);
		sb.append("from ");
		sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
		answer.addElement(new TextElement(sb.toString()));

		// 引用公共条件
		sb.setLength(0);
		XmlElement ifElement = new XmlElement("if");
		ifElement.addAttribute(new Attribute("test", "_parameter != null"));
		XmlElement includeWhere = new XmlElement("include");
		includeWhere.addAttribute(new Attribute("refid", "Base_Where_Condition"));
		ifElement.addElement(includeWhere);
		answer.addElement(ifElement);
		parentElement.addElement(answer);
	}
}
