/**
 * 
 */
package com.yqjr.framework.component.dialect.db;

import com.yqjr.framework.component.dialect.Dialect;
import com.yqjr.framework.datatype.BizzException;

/**
 * ClassName: AbstractDialect <br>
 * Description: 数据库方言公共方法 <br>
 * Create By: admin <br>
 * Create Date: 2017年4月28日 下午2:38:32 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public abstract class AbstractDialect implements Dialect {

	/**
	 * 获取count sql
	 */
	public String getCountSql(String sql) {
		StringBuffer countSql = new StringBuffer();
		countSql.append("select count(1) from ( ");
		countSql.append(sql.trim());
		countSql.append(" ) count_tmp");
		return countSql.toString();
	}

	/**
	 * Description: 根据数据库类型获取数据库方言 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月3日 下午7:20:27
	 *
	 * @param dbType
	 * @return
	 */
	public static Dialect getDialect(String dbType) {
		if ("oracle".equalsIgnoreCase(dbType)) {
			return new OracleDialect();
		} else if ("mysql".equalsIgnoreCase(dbType)) {
			return new MySQLDialect();
		}
		throw new BizzException("不能支持的dbType" + dbType);
	}
}
