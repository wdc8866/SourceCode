package com.yqjr.framework.component.mybatis;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

/**
 * ClassName: WhereElementGenerator <br>
 * Description: 公共where条件方法生成 <br>
 * Create By: admin <br>
 * Create Date: 2017年5月15日 上午11:26:01 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 *
 */
public class WhereElementGenerator extends AbstractXmlElementGenerator {

	@Override
	public void addElements(XmlElement parentElement) {
		XmlElement answer = new XmlElement("sql"); //$NON-NLS-1$
		context.getCommentGenerator().addComment(answer);
		answer.addAttribute(new Attribute("id", "Base_Where_Condition"));
		XmlElement where = new XmlElement("where");
		answer.addElement(where);
		StringBuffer sb = new StringBuffer();
		for (IntrospectedColumn introspectedColumn : ListUtilities
				.removeGeneratedAlwaysColumns(introspectedTable.getAllColumns())) {

			XmlElement isNotNullElement = new XmlElement("if"); //$NON-NLS-1$
			sb.setLength(0);
			sb.append(introspectedColumn.getJavaProperty("")); //$NON-NLS-1$
			sb.append(" != null"); //$NON-NLS-1$
			isNotNullElement.addAttribute(new Attribute("test", sb.toString())); //$NON-NLS-1$
			where.addElement(isNotNullElement);

			sb.setLength(0);
			sb.append("and ");
			sb.append(MyBatis3FormattingUtilities.getAliasedEscapedColumnName(introspectedColumn));
			sb.append(" = "); //$NON-NLS-1$
			sb.append(FrameworkJavaTypeResolver.getJavaProperty(introspectedColumn, true));

			isNotNullElement.addElement(new TextElement(sb.toString()));
		}
		parentElement.addElement(answer);
	}
}
