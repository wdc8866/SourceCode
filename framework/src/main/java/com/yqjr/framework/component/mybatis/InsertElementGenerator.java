/**
 * 
 */
package com.yqjr.framework.component.mybatis;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;
import org.mybatis.generator.config.GeneratedKey;

/**
 * ClassName: InsertElementGenerator <br>
 * Description: 参考InsertSelectiveElementGenerator,并根据框架实际情况修改 <br>
 * Create By: admin <br>
 * Create Date: 2017年5月12日 上午8:20:49 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public class InsertElementGenerator extends AbstractXmlElementGenerator {

	@Override
	public void addElements(XmlElement parentElement) {
		XmlElement answer = new XmlElement("insert"); //$NON-NLS-1$
		// 修改id名称固定为insert
		answer.addAttribute(new Attribute("id", "insert")); //$NON-NLS-1$
		FullyQualifiedJavaType parameterType = introspectedTable.getRules().calculateAllFieldsClass();
		answer.addAttribute(new Attribute("parameterType", //$NON-NLS-1$
				parameterType.getFullyQualifiedName()));
		context.getCommentGenerator().addComment(answer);

		GeneratedKey gk = introspectedTable.getGeneratedKey();
		if (gk != null) {
			IntrospectedColumn introspectedColumn = introspectedTable.getColumn(gk.getColumn());
			// if the column is null, then it's a configuration error. The
			// warning has already been reported
			if (introspectedColumn != null) {
				if (gk.isJdbcStandard()) {
					answer.addAttribute(new Attribute("useGeneratedKeys", "true")); //$NON-NLS-1$ //$NON-NLS-2$
					answer.addAttribute(new Attribute("keyProperty", introspectedColumn.getJavaProperty())); //$NON-NLS-1$
					answer.addAttribute(new Attribute("keyColumn", introspectedColumn.getActualColumnName())); //$NON-NLS-1$
				} else {
					answer.addElement(getSelectKey(introspectedColumn, gk));
				}
			}
		}

		StringBuilder sb = new StringBuilder();
		sb.append("insert into "); //$NON-NLS-1$
		sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
		answer.addElement(new TextElement(sb.toString()));

		// 添加trim标签(列名)
		XmlElement insertTrimElement = new XmlElement("trim"); //$NON-NLS-1$
		insertTrimElement.addAttribute(new Attribute("prefix", "(")); //$NON-NLS-1$ //$NON-NLS-2$
		insertTrimElement.addAttribute(new Attribute("suffix", ")")); //$NON-NLS-1$ //$NON-NLS-2$
		insertTrimElement.addAttribute(new Attribute("suffixOverrides", ",")); //$NON-NLS-1$ //$NON-NLS-2$
		answer.addElement(insertTrimElement);

		// 添加trim标签(值)
		XmlElement valuesTrimElement = new XmlElement("trim"); //$NON-NLS-1$
		valuesTrimElement.addAttribute(new Attribute("prefix", "values (")); //$NON-NLS-1$ //$NON-NLS-2$
		valuesTrimElement.addAttribute(new Attribute("suffix", ")")); //$NON-NLS-1$ //$NON-NLS-2$
		valuesTrimElement.addAttribute(new Attribute("suffixOverrides", ",")); //$NON-NLS-1$ //$NON-NLS-2$
		answer.addElement(valuesTrimElement);

		for (IntrospectedColumn introspectedColumn : ListUtilities
				.removeIdentityAndGeneratedAlwaysColumns(introspectedTable.getAllColumns())) {

			if (introspectedColumn.isSequenceColumn() || introspectedColumn.getFullyQualifiedJavaType().isPrimitive()) {
				sb.setLength(0);
				sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
				sb.append(',');
				insertTrimElement.addElement(new TextElement(sb.toString()));

				sb.setLength(0);
				sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
				sb.append(',');
				valuesTrimElement.addElement(new TextElement(sb.toString()));

				continue;
			}

			XmlElement insertNotNullElement = new XmlElement("if"); //$NON-NLS-1$
			sb.setLength(0);
			sb.append(introspectedColumn.getJavaProperty());
			sb.append(" != null"); //$NON-NLS-1$
			insertNotNullElement.addAttribute(new Attribute("test", sb.toString())); //$NON-NLS-1$

			sb.setLength(0);
			sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
			sb.append(',');
			insertNotNullElement.addElement(new TextElement(sb.toString()));
			insertTrimElement.addElement(insertNotNullElement);

			XmlElement valuesNotNullElement = new XmlElement("if"); //$NON-NLS-1$
			sb.setLength(0);
			sb.append(introspectedColumn.getJavaProperty());
			sb.append(" != null"); //$NON-NLS-1$
			valuesNotNullElement.addAttribute(new Attribute("test", sb.toString())); //$NON-NLS-1$

			sb.setLength(0);
			sb.append(FrameworkJavaTypeResolver.getJavaProperty(introspectedColumn, true));
			sb.append(',');
			valuesNotNullElement.addElement(new TextElement(sb.toString()));
			valuesTrimElement.addElement(valuesNotNullElement);
		}

		parentElement.addElement(answer);

	}

}