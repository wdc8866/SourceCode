package com.yqjr.framework.datatype;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * ClassName: DateTypeHandler <br>
 * Description: Mybatis类型转换器,Date <br>
 * Create By: admin <br>
 * Create Date: 2017年2月27日 上午8:55:18 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public class DateTypeHandler extends BaseTypeHandler<Date> {

	/**
	 * Description: 类型转换 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月27日 上午9:07:53
	 *
	 * @param rsc
	 * @return
	 */
	private Date transferType(Object rsc) {
		// 如果为null则返回null
		if (rsc == null) {
			return null;
		}
		// oracle timestamp
		if (rsc instanceof oracle.sql.TIMESTAMP) {
			try {
				Timestamp ts = ((oracle.sql.TIMESTAMP) rsc).timestampValue();
				return new Date(ts);
			} catch (SQLException e) {
				throw new BizzException(e);
			}
		}
		// oracle date
		if (rsc instanceof oracle.sql.DATE) {
			return new Date(((oracle.sql.DATE) rsc).dateValue());
		}
		// timestamp
		else if (rsc instanceof java.sql.Timestamp) {
			return new Date((java.sql.Timestamp) rsc);
		}
		// date
		else if (rsc instanceof java.sql.Date) {
			return new Date((java.sql.Date) rsc);
		}
		// 其他情况抛异常
		else {
			throw new BizzException("非法的类型转换器配置DateTypeHandler");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.type.BaseTypeHandler#setNonNullParameter(java.sql.
	 * PreparedStatement, int, java.lang.Object,
	 * org.apache.ibatis.type.JdbcType)
	 */
	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Date parameter, JdbcType jdbcType)
			throws SQLException {
		if (jdbcType == JdbcType.TIMESTAMP) {
			ps.setTimestamp(i, parameter.toTimeStamp());
		} else if (jdbcType == JdbcType.DATE) {
			ps.setDate(i, parameter.toSqlDate());
		} else {
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
	public Date getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return transferType(rs.getObject(columnName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.type.BaseTypeHandler#getNullableResult(java.sql.
	 * ResultSet, int)
	 */
	@Override
	public Date getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return transferType(rs.getObject(columnIndex));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.type.BaseTypeHandler#getNullableResult(java.sql.
	 * CallableStatement, int)
	 */
	@Override
	public Date getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return transferType(cs.getObject(columnIndex));
	}

}
