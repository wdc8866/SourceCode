/**
 * Copyright &copy; YQJR All rights reserved.
 */
package com.yqjr.framework.component.dialect;

import com.yqjr.framework.component.dialect.db.DBDateInfo;

/**
 * 类似hibernate的Dialect,只包含部分功能<br>
 * 只保留了常用的Oralce和MySQL两种方言<br>
 *
 * @author poplar.yfyang
 * @version 1.0 2011-11-18 下午12:31
 * @since JDK 1.5
 */
public interface Dialect {

	/**
	 * 数据库本身是否支持分页当前的分页查询方式 <br>
	 * 如果数据库不支持的话，则不进行数据库分页
	 *
	 * @return true：支持当前的分页查询方式
	 */
	public boolean supportsLimit();

	/**
	 * 将sql转换为分页SQL，分别调用分页sql
	 *
	 * @param sql
	 *            SQL语句
	 * @param offset
	 *            开始条数
	 * @param limit
	 *            每页显示多少纪录条数
	 * @return 分页查询的sql
	 */
	public String getLimitString(String sql, int offset, int limit);

	/**
	 * Description: 获取日期格式 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年4月21日 上午8:18:35
	 *
	 * @return 日期格式
	 */
	public DBDateInfo getDateFormat();

	/**
	 * Description: 生成count sql <br>
	 * Create By: admin <br>
	 * Create Date: 2017年4月28日 上午8:22:10
	 *
	 * @param sql
	 * @return
	 */
	public String getCountSql(String sql);

}
