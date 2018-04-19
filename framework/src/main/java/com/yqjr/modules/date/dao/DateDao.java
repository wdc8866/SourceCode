/**
 * 
 */
package com.yqjr.modules.date.dao;

import java.util.Date;

import com.yqjr.framework.annotation.FrameworkDao;

/**
 * ClassName: DateDao <br>
 * Description: 框架组件数据访问方法 <br>
 * Create By: admin <br>
 * Create Date: 2017年4月17日 下午4:09:36 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
@FrameworkDao
public interface DateDao {
	/**
	 * Description: 获取数据库时间 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年4月17日 下午4:11:24
	 *
	 * @return
	 */
	public Date getDefaultDateTime();

	/**
	 * Description: 获取数据库时间 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年4月17日 下午4:14:38
	 *
	 * @param sql
	 *            获取系统日期SQL
	 * @return
	 */
	public Date getSpecialDateTime(String sql);
}
