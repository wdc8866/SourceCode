package com.yqjr.framework.component.mybatis;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import com.yqjr.framework.base.BaseEntity;
import com.yqjr.framework.datatype.Date;

/**
 * ClassName: DeleteElementGenerator <br>
 * Description: delete方法生成,采用逻辑删除的方式 <br>
 * Create By: admin <br>
 * Create Date: 2017年5月15日 上午11:14:05 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public class DeleteElementGenerator extends AbstractXmlElementGenerator {

	@Override
	public void addElements(XmlElement parentElement) {

		XmlElement answer = new XmlElement("update");
		answer.addAttribute(new Attribute("id", "delete"));
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
		sb.setLength(0);
		// 逻辑状态位
		sb.append("status = " + BaseEntity.DELETE);
		dynamicElement.addElement(new TextElement(sb.toString()));
		// 删除人
		XmlElement isNotNullElement = new XmlElement("if"); //$NON-NLS-1$
		sb.setLength(0);
		sb.append("updateBy"); //$NON-NLS-1$
		sb.append(" != null"); //$NON-NLS-1$
		isNotNullElement.addAttribute(new Attribute("test", sb.toString())); //$NON-NLS-1$
		dynamicElement.addElement(isNotNullElement);
		sb.setLength(0);
		sb.append("updateBy");
		sb.append(" = "); //$NON-NLS-1$
		sb.append("#{updateBy.id}"); //$NON-NLS-1$
		sb.append(',');
		isNotNullElement.addElement(new TextElement(sb.toString()));
		// 删除时间
		isNotNullElement = new XmlElement("if"); //$NON-NLS-1$
		sb.setLength(0);
		sb.append("updateDate"); //$NON-NLS-1$
		sb.append(" != null"); //$NON-NLS-1$
		isNotNullElement.addAttribute(new Attribute("test", sb.toString())); //$NON-NLS-1$
		dynamicElement.addElement(isNotNullElement);
		sb.setLength(0);
		sb.append("updateDate");
		sb.append(" = "); //$NON-NLS-1$
		sb.append(String.format("#{updateDate,jdbcType=TIMESTAMP,typeHandler=%s}", Date.getTypeHandlerName())); //$NON-NLS-1$
		isNotNullElement.addElement(new TextElement(sb.toString()));
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
