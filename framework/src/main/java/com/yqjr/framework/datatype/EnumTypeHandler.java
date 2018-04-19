/**
 * 
 */
package com.yqjr.framework.datatype;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.util.Assert;

import com.yqjr.framework.base.BaseEnum;

/**
 * ClassName: EnumTypeHandler <br>
 * Description: Mybatis类型转换器,枚举<br>
 * Create By: admin <br>
 * Create Date: 2017年4月19日 下午1:14:32 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public class EnumTypeHandler<E extends Enum<E> & BaseEnum<E>> extends BaseTypeHandler<BaseEnum<E>> {

	private Class<E> clazz;

	public EnumTypeHandler(Class<E> clazz) {
		Assert.notNull(clazz);
		this.clazz = clazz;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.type.BaseTypeHandler#setNonNullParameter(java.sql.
	 * PreparedStatement, int, java.lang.Object,
	 * org.apache.ibatis.type.JdbcType)
	 */
	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, BaseEnum<E> parameter, JdbcType jdbcType)
			throws SQLException {
		ps.setInt(i, parameter.getValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.type.BaseTypeHandler#getNullableResult(java.sql.
	 * ResultSet, java.lang.String)
	 */
	@Override
	public BaseEnum<E> getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return getEnum(rs.getInt(columnName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.type.BaseTypeHandler#getNullableResult(java.sql.
	 * ResultSet, int)
	 */
	@Override
	public BaseEnum<E> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return getEnum(rs.getInt(columnIndex));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.type.BaseTypeHandler#getNullableResult(java.sql.
	 * CallableStatement, int)
	 */
	@Override
	public BaseEnum<E> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return getEnum(cs.getInt(columnIndex));
	}

	/**
	 * Description: 转换枚举值 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年4月19日 下午2:02:51
	 *
	 * @param value
	 */
	private E getEnum(int value) {
		E[] enums = clazz.getEnumConstants();
		for (E e : enums) {
			if (((BaseEnum<E>) e).getValue() == value) {
				return e;
			}
		}
		throw new BizzException("非法的枚举值");
	}

}
