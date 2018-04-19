/**
 * Copyright &copy; YQJR All rights reserved.
 */
package com.yqjr.framework.component.dialect.db;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Mysql方言的实现
 *
 * @author poplar.yfyang
 * @version 1.0 2010-10-10 下午12:31
 * @since JDK 1.5
 */
public class MySQLDialect extends AbstractDialect {

	@Override
	public String getLimitString(String sql, int offset, int limit) {
		return getLimitString(sql, offset, Integer.toString(offset), Integer.toString(limit));
	}

	public boolean supportsLimit() {
		return true;
	}

	/**
	 * 将sql变成分页sql语句,提供将offset及limit使用占位符号(placeholder)替换.
	 * 
	 * <pre>
	 * 如mysql
	 * dialect.getLimitString("select * from user", 12, ":offset",0,":limit") 将返回
	 * select * from user limit :offset,:limit
	 * </pre>
	 *
	 * @param sql
	 *            实际SQL语句
	 * @param offset
	 *            分页开始纪录条数
	 * @param offsetPlaceholder
	 *            分页开始纪录条数－占位符号
	 * @param limitPlaceholder
	 *            分页纪录条数占位符号
	 * @return 包含占位符的分页sql
	 */
	public String getLimitString(String sql, int offset, String offsetPlaceholder, String limitPlaceholder) {
		StringBuilder stringBuilder = new StringBuilder(sql);
		stringBuilder.append(" limit ");
		if (offset > 0) {
			stringBuilder.append(offsetPlaceholder).append(",").append(limitPlaceholder);
		} else {
			stringBuilder.append(limitPlaceholder);
		}
		return stringBuilder.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yqjr.framework.component.dialect.Dialect#getDateFormat()
	 */
	@Override
	public DBDateInfo getDateFormat() {
		DBDateInfo dateInfo = new DBDateInfo("%Y", "%m", "%d", "%H", "%i", "%S");
		dateInfo.setToCharMethod("DATE_FORMAT");
		dateInfo.setToDateMethod("STR_TO_DATE");
		dateInfo.setSysDateMethod("NOW()");
		return dateInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yqjr.framework.component.dialect.Dialect#getCountSql(java.lang.
	 * String)
	 */
	@Override
	public String getCountSql(String sql) {
		// mysql 要删除语句中的order by
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(sql.trim());
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return super.getCountSql(sb.toString());
	}

}
