/**
 * 
 */
package com.yqjr.framework.component.mybatis;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

/**
 * ClassName: ResultMapElementGenerator <br>
 * Description: 参考ResultMapWithoutBLOBsElementGenerator,并根据框架实际情况修改 <br>
 * Create By: admin <br>
 * Create Date: 2017年5月16日 下午1:24:49 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public class ResultMapElementGenerator extends AbstractXmlElementGenerator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.
	 * AbstractXmlElementGenerator#addElements(org.mybatis.generator.api.dom.xml
	 * .XmlElement)
	 */
	@Override
	public void addElements(XmlElement parentElement) {
		XmlElement answer = new XmlElement("resultMap"); //$NON-NLS-1$
		answer.addAttribute(new Attribute("id", //$NON-NLS-1$
				introspectedTable.getBaseResultMapId()));
		answer.addAttribute(new Attribute("type", //$NON-NLS-1$
				introspectedTable.getBaseRecordType()));
		context.getCommentGenerator().addComment(answer);
		addResultMapElements(answer);
		parentElement.addElement(answer);
	}

	private void addResultMapElements(XmlElement answer) {
		List<IntrospectedColumn> columns;
		columns = introspectedTable.getBaseColumns();
		for (IntrospectedColumn introspectedColumn : columns) {
			XmlElement resultElement = new XmlElement("result"); //$NON-NLS-1$

			resultElement.addAttribute(new Attribute("column", //$NON-NLS-1$
					MyBatis3FormattingUtilities.getRenamedColumnNameForResultMap(introspectedColumn)));
			resultElement.addAttribute(
					new Attribute("property", FrameworkJavaTypeResolver.getJavaProperty(introspectedColumn, false))); //$NON-NLS-1$
			resultElement.addAttribute(new Attribute("jdbcType", //$NON-NLS-1$
					FrameworkJavaTypeResolver.getJdbcType(introspectedColumn)));
			// 如果指定了typehandler则使用指定的typehandler
			String typeHandler = FrameworkJavaTypeResolver.getTypeHandler(introspectedColumn);
			if (stringHasValue(typeHandler)) {
				resultElement.addAttribute(new Attribute("typeHandler", typeHandler)); //$NON-NLS-1$
			}
			answer.addElement(resultElement);
		}
	}

}
