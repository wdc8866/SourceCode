package com.yqjr.framework.component.mybatis;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

/**
 * ClassName: IdElementGenerator <br>
 * Description: 根据ID检索 <br>
 * Create By: admin <br>
 * Create Date: 2017年5月15日 上午11:22:32 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public class IdElementGenerator extends AbstractXmlElementGenerator {

	@Override
	public void addElements(XmlElement parentElement) {
		XmlElement answer = new XmlElement("select"); //$NON-NLS-1$
		answer.addAttribute(new Attribute("id", "id")); //$NON-NLS-1$
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

		// 添加ID条件
		sb.setLength(0);
		XmlElement ifElement = new XmlElement("if");
		ifElement.addAttribute(new Attribute("test", "_parameter != null"));
		sb.append("where ");
		String idColumns = context.getProperty("tableIdColumn");
		String idProperty = context.getProperty("tableIdProperty");
		sb.append(String.format("%s = #{%s}", idColumns, idProperty));
		ifElement.addElement(new TextElement(sb.toString()));
		answer.addElement(ifElement);
		parentElement.addElement(answer);
	}

}
