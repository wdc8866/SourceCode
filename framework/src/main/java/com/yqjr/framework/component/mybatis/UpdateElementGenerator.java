package com.yqjr.framework.component.mybatis;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

/**
 * ClassName: InsertElementGenerator <br>
 * Description: 参考UpdateByExampleSelectiveElementGenerator,并根据框架实际情况修改 <br>
 * Create By: admin <br>
 * Create Date: 2017年5月12日 上午8:20:49 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public class UpdateElementGenerator extends AbstractXmlElementGenerator {

	public UpdateElementGenerator() {
		super();
	}

	@Override
	public void addElements(XmlElement parentElement) {
		XmlElement answer = new XmlElement("update"); //$NON-NLS-1$
		answer.addAttribute(new Attribute("id", "update")); //$NON-NLS-1$
		FullyQualifiedJavaType parameterType = introspectedTable.getRules().calculateAllFieldsClass();
		answer.addAttribute(new Attribute("parameterType", //$NON-NLS-1$
				parameterType.getFullyQualifiedName()));
		context.getCommentGenerator().addComment(answer);

		StringBuilder sb = new StringBuilder();
		sb.append("update "); //$NON-NLS-1$
		sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
		answer.addElement(new TextElement(sb.toString()));

		XmlElement dynamicElement = new XmlElement("set"); //$NON-NLS-1$
		answer.addElement(dynamicElement);

		for (IntrospectedColumn introspectedColumn : ListUtilities
				.removeGeneratedAlwaysColumns(introspectedTable.getAllColumns())) {
			// update字段时排除ID字段
			if (introspectedColumn.getActualColumnName().equalsIgnoreCase(context.getProperty("tableIdColumn"))) {
				continue;
			}
			XmlElement isNotNullElement = new XmlElement("if"); //$NON-NLS-1$
			sb.setLength(0);
			sb.append(introspectedColumn.getJavaProperty("")); //$NON-NLS-1$
			sb.append(" != null"); //$NON-NLS-1$
			isNotNullElement.addAttribute(new Attribute("test", sb.toString())); //$NON-NLS-1$
			dynamicElement.addElement(isNotNullElement);

			sb.setLength(0);
			sb.append(MyBatis3FormattingUtilities.getAliasedEscapedColumnName(introspectedColumn));
			sb.append(" = "); //$NON-NLS-1$
			sb.append(FrameworkJavaTypeResolver.getJavaProperty(introspectedColumn, true));
			sb.append(',');

			isNotNullElement.addElement(new TextElement(sb.toString()));
		}
		// 添加where条件
		XmlElement whereElement = new XmlElement("where");
		String idColumns = context.getProperty("tableIdColumn");
		String idProperty = context.getProperty("tableIdProperty");
		sb.setLength(0);
		sb.append(String.format("%s = #{%s}", idColumns, idProperty));
		whereElement.addElement(new TextElement(sb.toString()));

		answer.addElement(whereElement);
		parentElement.addElement(answer);
	}

}
