package com.yqjr.framework.datatype;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * ClassName: AmountTypeHandler <br>
 * Description: 金额转换器 <br>
 * Create By: Wanglei <br>
 * Create Date: 2017年7月10日 上午9:48:27 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 *
 */
public class AmountTypeHandler extends BaseTypeHandler<Amount> {

	/**
	 * 
	 * Description: 类型转换 <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年7月10日 上午9:54:06
	 *
	 * @param rsc
	 * @return
	 */
	private Amount transferType(Object rsc) {
		// 如果为null则返回null
 		if(rsc instanceof BigDecimal){
			BigDecimal decimal = (BigDecimal) rsc;
			return new Amount(decimal);
		}else if( rsc == null){
			return null;
		}
		// 其他情况抛异常
		else {
			throw new BizzException("非法的类型转换器配置DateTypeHandler");
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.type.BaseTypeHandler#getNullableResult(java.sql.
	 * ResultSet, java.lang.String)
	 */
	@Override
	public Amount getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return transferType(rs.getObject(columnName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.type.BaseTypeHandler#getNullableResult(java.sql.
	 * ResultSet, int)
	 */
	@Override
	public Amount getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return transferType(rs.getObject(columnIndex));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.type.BaseTypeHandler#getNullableResult(java.sql.
	 * CallableStatement, int)
	 */
	@Override
	public Amount getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return transferType(cs.getObject(columnIndex));
	}

	/* (non-Javadoc)
	 * @see org.apache.ibatis.type.BaseTypeHandler#setNonNullParameter(java.sql.PreparedStatement, int, java.lang.Object, org.apache.ibatis.type.JdbcType)
	 */
	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Amount parameter, JdbcType jdbcType)
			throws SQLException {
		if (jdbcType == JdbcType.DECIMAL) {
			ps.setBigDecimal(i, parameter.getAmount());
		}else {
			throw new BizzException("非法的类型转换器配置DateTypeHandler");
		}
	}

}
