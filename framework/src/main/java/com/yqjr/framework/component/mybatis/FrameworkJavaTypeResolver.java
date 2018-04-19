/**
 * 
 */
package com.yqjr.framework.component.mybatis;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import java.sql.Types;

import org.apache.ibatis.type.JdbcType;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;

import com.yqjr.framework.datatype.Date;

/**
 * ClassName: FrameworkJavaTypeResolver <br>
 * Description: 框架类型解析器 <br>
 * Create By: admin <br>
 * Create Date: 2017年5月16日 下午2:47:28 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public class FrameworkJavaTypeResolver extends JavaTypeResolverDefaultImpl {

	public FrameworkJavaTypeResolver() {
		super();
		// 更改默认匹配类型
		typeMap.put(Types.DATE, new JdbcTypeInformation("DATE", //$NON-NLS-1$
				new FullyQualifiedJavaType(Date.class.getName())));
		typeMap.put(Types.TIME, new JdbcTypeInformation("DATE", //$NON-NLS-1$
				new FullyQualifiedJavaType(Date.class.getName())));
		typeMap.put(Types.TIMESTAMP, new JdbcTypeInformation("DATE", //$NON-NLS-1$
				new FullyQualifiedJavaType(Date.class.getName())));
	}

	/**
	 * Description: 处理typeHandler <br>
	 * Create By: admin <br>
	 * Create Date: 2017年6月1日 下午6:34:22
	 *
	 * @param introspectedColumn
	 * @return
	 */
	public static String getTypeHandler(IntrospectedColumn introspectedColumn) {
		JdbcType jdbcType = JdbcType.forCode(introspectedColumn.getJdbcType());
		String typeHandlerName = introspectedColumn.getTypeHandler();
		switch (jdbcType) {
		case DATE:
			typeHandlerName = Date.getTypeHandlerName();
			break;
		case TIME:
			typeHandlerName = Date.getTypeHandlerName();
			break;
		case TIMESTAMP:
			typeHandlerName = Date.getTypeHandlerName();
			break;
		// TODO
		default:
			break;
		}
		return typeHandlerName;
	}

	/**
	 * Description: 处理jdbcType <br>
	 * Create By: admin <br>
	 * Create Date: 2017年6月1日 下午7:22:47
	 *
	 * @param introspectedColumn
	 * @return
	 */
	public static String getJdbcType(IntrospectedColumn introspectedColumn) {
		JdbcType jdbcType = JdbcType.forCode(introspectedColumn.getJdbcType());
		String jdbcTypeName = introspectedColumn.getJdbcTypeName();
		switch (jdbcType) {
		case DATE:
			jdbcTypeName = "DATE";
			break;
		case TIME:
			jdbcTypeName = "TIME";
			break;
		case TIMESTAMP:
			jdbcTypeName = "TIMESTAMP";
			break;
		// TODO
		default:
			break;
		}
		return jdbcTypeName;
	}

	/**
	 * Description: 处理java属性 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年6月1日 下午6:40:12
	 *
	 * @param introspectedColumn
	 * @param isParameter
	 *            是否位于参数位置
	 * @return
	 */
	public static String getJavaProperty(IntrospectedColumn introspectedColumn, boolean isParameter) {
		// user类型特殊处理
		String javaProperty = introspectedColumn.getJavaProperty();
		if ("user".equalsIgnoreCase(introspectedColumn.getFullyQualifiedJavaType().getShortName())
				&& ("createby".equalsIgnoreCase(introspectedColumn.getJavaProperty())
						|| "updateby".equalsIgnoreCase(introspectedColumn.getJavaProperty()))) {
			javaProperty = String.format("%s.id", javaProperty);
		}
		// TODO
		// 如果存在其他特殊类型在此处处理

		// 判断是否位于参数位置,如果是的话需要添加额外信息
		if (isParameter) {
			StringBuilder sb = new StringBuilder();
			sb.append("#{"); //$NON-NLS-1$
			sb.append(javaProperty);
			sb.append(",jdbcType="); //$NON-NLS-1$
			sb.append(getJdbcType(introspectedColumn));
			String typeHandler = getTypeHandler(introspectedColumn);
			if (stringHasValue(typeHandler)) {
				sb.append(",typeHandler="); //$NON-NLS-1$
				sb.append(typeHandler);
			}
			sb.append('}');
			return sb.toString();
		}
		return javaProperty;
	}

}
