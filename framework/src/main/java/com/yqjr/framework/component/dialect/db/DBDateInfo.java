/**
 * 
 */
package com.yqjr.framework.component.dialect.db;

/**
 * ClassName: DBDateInfo <br>
 * Description: 数据库日期相关<br>
 * Create By: admin <br>
 * Create Date: 2017年4月21日 上午8:42:37 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public class DBDateInfo {

	/**
	 * 年格式
	 */
	private String year;

	/**
	 * 月格式
	 */
	private String month;

	/**
	 * 日格式
	 */
	private String day;

	/**
	 * 小时格式
	 */
	private String hour;

	/**
	 * 分钟格式
	 */
	private String minute;

	/**
	 * 秒格式
	 */
	private String second;

	/**
	 * 日期转字符串方法名称
	 */
	private String toCharMethod;

	/**
	 * 字符串转日期方法名称
	 */
	private String toDateMethod;

	/**
	 * 获取系统时间方法名称
	 */
	private String sysDateMethod;

	public DBDateInfo(String year, String month, String day, String hour, String minute, String second) {
		this.year = year;
		this.month = month;
		this.day = day;
		this.hour = hour;
		this.minute = minute;
		this.second = second;
	}

	/**
	 * @return the year
	 */
	public String getYear() {
		return year;
	}

	/**
	 * @return the month
	 */
	public String getMonth() {
		return month;
	}

	/**
	 * @return the day
	 */
	public String getDay() {
		return day;
	}

	/**
	 * @return the hour
	 */
	public String getHour() {
		return hour;
	}

	/**
	 * @return the minute
	 */
	public String getMinute() {
		return minute;
	}

	/**
	 * @return the second
	 */
	public String getSecond() {
		return second;
	}

	/**
	 * @return the toCharMethod
	 */
	public String getToCharMethod() {
		return toCharMethod;
	}

	/**
	 * @param toCharMethod
	 *            the toCharMethod to set
	 */
	public void setToCharMethod(String toCharMethod) {
		this.toCharMethod = toCharMethod;
	}

	/**
	 * @return the toDateMethod
	 */
	public String getToDateMethod() {
		return toDateMethod;
	}

	/**
	 * @param toDateMethod
	 *            the toDateMethod to set
	 */
	public void setToDateMethod(String toDateMethod) {
		this.toDateMethod = toDateMethod;
	}

	/**
	 * @return the sysDateMethod
	 */
	public String getSysDateMethod() {
		return sysDateMethod;
	}

	/**
	 * @param sysDateMethod
	 *            the sysDateMethod to set
	 */
	public void setSysDateMethod(String sysDateMethod) {
		this.sysDateMethod = sysDateMethod;
	}

	public String getHHMMSS() {
		return String.format("%s:%s:%s", hour, minute, second);
	}

	public String getYYMMDD() {
		return String.format("%s-%s-%s", year, month, day);
	}

	public String getDefaultDateFormat() {
		return getYYMMDD() + " " + getHHMMSS();
	}
}
