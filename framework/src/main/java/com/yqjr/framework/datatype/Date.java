package com.yqjr.framework.datatype;

import java.io.Serializable;
import java.sql.Timestamp;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.util.Assert;

import com.yqjr.framework.component.config.Configuration;
import com.yqjr.framework.component.context.SpringContext;
import com.yqjr.framework.component.dialect.db.AbstractDialect;
import com.yqjr.framework.component.dialect.db.DBDateInfo;
import com.yqjr.framework.utils.StringUtils;
import com.yqjr.modules.date.dao.DateDao;

/**
 * ClassName: Date <br>
 * Description: 基于joda日期类 <br>
 * Create By: admin <br>
 * Create Date: 2017年2月21日 下午1:31:52 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public class Date implements Serializable {

	private static final long serialVersionUID = 8604954013805675746L;

	/**
	 * 日期获取模式
	 */
	private static DateMode mode = DateMode.OS;

	/**
	 * 日期默认格式
	 */
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static final DateTimeFormatter formatter = DateTimeFormat.forPattern(DEFAULT_DATE_FORMAT);

	static {
		mode = Enum.valueOf(DateMode.class, Configuration.getConfig().getStringValue("framework.timemode"));
	}

	private enum DateMode {
		DB, OS;
	}

	private DateTime jodaDateTime;

	/**
	 * 获取系统时间<br>
	 * 根据配置文件中配置的信息,支持从操作系统/数据库两种方式的时间获取<br>
	 * 参考payment.timeMode配置<br>
	 */
	public Date() {
		switch (mode) {
		case OS:
			jodaDateTime = new DateTime();
			break;
		case DB:
			DateDao dateDao = SpringContext.getInstance().getBeanWithName("dateDao");
			jodaDateTime = new DateTime(dateDao.getDefaultDateTime());
			break;
		default:
			throw new BizzException("不能支持的timeMode");
		}
	}

	/**
	 * 从指定的日期表中获取时间
	 * 
	 * @param tableName
	 * @param column
	 * @param where
	 */
	public Date(String tableName, String column, String where) {
		if (mode != DateMode.DB) {
			throw new BizzException("无法从DB中获取时间,请修改框架参数");
		}
		DateDao dateDao = SpringContext.getInstance().getBeanWithName("dateDao");
		Assert.hasText(tableName, "日期表不能为空");
		Assert.hasText(column, "日期列名不能为空");
		DBDateInfo dbDateInfo = AbstractDialect
				.getDialect(Configuration.getConfig().getStringValue("framework.db.type")).getDateFormat();
		String sql = String.format("SELECT %s(%s||' '||%s(%s,'%s'),'%s') FROM %s WHERE %s",
				dbDateInfo.getToDateMethod(), column.trim(), dbDateInfo.getToCharMethod(),
				dbDateInfo.getSysDateMethod(), dbDateInfo.getHHMMSS(), dbDateInfo.getDefaultDateFormat(),
				tableName.trim(), where);
		jodaDateTime = new DateTime(dateDao.getSpecialDateTime(sql));
	}

	public Date(int year, int month, int day) {
		jodaDateTime = new DateTime(year, month, day, 0, 0, 0, 0);
	}

	public Date(long time) {
		jodaDateTime = new DateTime(time, DateTimeZone.getDefault());
	}

	public Date(java.util.Date date) {
		jodaDateTime = new DateTime(date);
	}

	public Date(java.sql.Date date) {
		this(date.getTime());
	}

	public Date(Timestamp timestamp) {
		this(timestamp.getTime());
	}

	public Date(DateTime jodaDateTime) {
		Assert.notNull(jodaDateTime);
		this.jodaDateTime = jodaDateTime;
	}

	/**
	 * 通过字符串的形式构建时间<br>
	 * 字符串格式 yyyy-MM-dd HH:mm:ss<br>
	 * 
	 * @param time
	 */
	public Date(String time) {
		jodaDateTime = formatter.parseDateTime(time);
	}

	public Date(String time, String formatter) {
		jodaDateTime = DateTimeFormat.forPattern(formatter).parseDateTime(time);
	}

	/**
	 * Description: 转换成java.sql.Timestamp <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月21日 下午3:49:59
	 *
	 * @return
	 */
	public Timestamp toTimeStamp() {
		return new Timestamp(jodaDateTime.getMillis());
	}

	/**
	 * Description: 转换成java.util.Date <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月21日 下午3:48:45
	 *
	 * @return
	 */
	public java.util.Date toDate() {
		return jodaDateTime.toDate();
	}

	/**
	 * Description: 转换成java.sql.Date <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月27日 上午9:01:17
	 *
	 * @return
	 */
	public java.sql.Date toSqlDate() {
		return new java.sql.Date(jodaDateTime.getMillis());
	}

	@Override
	public String toString() {
		return jodaDateTime.toString(formatter);
	}

	/**
	 * Description: 按照指定的日期格式输出 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月27日 上午10:37:32
	 *
	 * @param formatter
	 *            日期格式
	 * @return
	 */
	public String toString(String formatter) {
		Assert.hasText(formatter, "输出格式不能为空");
		return jodaDateTime.toString(formatter);
	}

	/**
	 * Description: 日期格式化 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月27日 上午11:58:11
	 *
	 * @param formatter
	 * @return
	 */
	public Date parse(String str, String formatter) {
		Assert.hasText(str, "日期不能为空");
		Assert.hasText(formatter, "格式不能为空");
		jodaDateTime = DateTime.parse(str, DateTimeFormat.forPattern(formatter));
		return this;
	}

	/**
	 * Description: 获取日期 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月27日 下午1:47:45
	 *
	 * @return
	 */
	public String getDate() {
		return jodaDateTime.toString("yyyy-MM-dd");
	}

	/**
	 * Description: 获取月份 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月27日 上午11:56:30
	 *
	 * @return
	 */
	public int getMonth() {
		return jodaDateTime.getMonthOfYear();
	}

	/**
	 * Description: 获取当月日期 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月27日 上午10:47:02
	 *
	 * @return
	 */
	public int getDayOfMonth() {
		return jodaDateTime.getDayOfMonth();
	}

	/**
	 * Description: 获取年 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月27日 上午10:47:26
	 *
	 * @return
	 */
	public int getYear() {
		return jodaDateTime.getYear();
	}

	/**
	 * Description: 是否在某时间之前 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月27日 上午10:48:46
	 *
	 * @param date
	 * @return
	 */
	public boolean isBefore(Date date) {
		Assert.notNull(date);
		return jodaDateTime.isBefore(date.getMillis());
	}

	/**
	 * Description: 是否在某时间之后 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月27日 上午10:50:10
	 *
	 * @param date
	 * @return
	 */
	public boolean isAfter(Date date) {
		Assert.notNull(date);
		return jodaDateTime.isAfter(date.getMillis());
	}

	/**
	 * Description: 时间比较 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月27日 上午10:51:07
	 *
	 * @param date
	 * @return
	 */
	public int compareTo(Date date) {
		if (this.isAfter(date))
			return 1;
		else if (this.isBefore(date)) {
			return -1;
		} else
			return 0;
	}

	/**
	 * Description: 复制指定的时间生成实例 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月27日 上午10:54:18
	 *
	 * @param date
	 * @return
	 */
	public static Date copy(Date date) {
		DateTime jodaDateTime = new DateTime(date.getMillis());
		return new Date(jodaDateTime);
	}

	/**
	 * Description: 获取操作系统时间 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月27日 上午11:29:43
	 *
	 * @return
	 */
	public static Date newOSDate() {
		DateTime jodaDateTime = new DateTime();
		return new Date(jodaDateTime);
	}

	/**
	 * Description: +days后的时间 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月27日 上午10:57:56
	 *
	 * @param days
	 * @return
	 */
	public Date plusDays(int days) {
		Assert.isTrue(days > 0);
		jodaDateTime = jodaDateTime.plusDays(days);
		return this;
	}

	/**
	 * Description: -days后的时间 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月27日 上午11:05:40
	 *
	 * @param days
	 */
	public Date minusDays(int days) {
		Assert.isTrue(days > 0);
		jodaDateTime = jodaDateTime.minusDays(days);
		return this;
	}

	/**
	 * Description: 同getTimes <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月27日 上午11:06:27
	 *
	 * @return
	 */
	public long getMillis() {
		return jodaDateTime.getMillis();
	}

	/**
	 * Description: +months后的时间 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月27日 上午11:07:10
	 *
	 * @return
	 */
	public Date plusMonths(int months) {
		Assert.isTrue(months > 0);
		jodaDateTime = jodaDateTime.plusMonths(months);
		return this;
	}

	/**
	 * Description: -months后的时间 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月27日 上午11:18:46
	 *
	 * @param months
	 */
	public Date minusMonths(int months) {
		Assert.isTrue(months > 0);
		jodaDateTime = jodaDateTime.minusMonths(months);
		return this;
	}

	/**
	 * Description: +years后的时间 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月27日 上午11:31:44
	 *
	 * @param years
	 * @return
	 */
	public Date plusYears(int years) {
		Assert.isTrue(years > 0);
		jodaDateTime = jodaDateTime.plusYears(years);
		return this;
	}

	/**
	 * Description: -years后的时间 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月27日 上午11:32:04
	 *
	 * @param years
	 * @return
	 */
	public Date minusYears(int years) {
		Assert.isTrue(years > 0);
		jodaDateTime = jodaDateTime.minusYears(years);
		return this;
	}

	/**
	 * Description: +minutes后的时间 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月27日 上午11:32:18
	 *
	 * @param minutes
	 * @return
	 */
	public Date plusMinutes(int minutes) {
		Assert.isTrue(minutes > 0);
		jodaDateTime = jodaDateTime.plusMinutes(minutes);
		return this;
	}

	/**
	 * Description: -minutes后的时间 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月27日 上午11:32:40
	 *
	 * @param minutes
	 * @return
	 */
	public Date minusMinutes(int minutes) {
		Assert.isTrue(minutes > 0);
		jodaDateTime = jodaDateTime.minusMinutes(minutes);
		return this;
	}

	/**
	 * Description: 获取MyBatis TypeHandler名称<br>
	 * Create By: admin <br>
	 * Create Date: 2017年6月1日 下午5:33:24
	 *
	 * @return
	 */
	public static String getTypeHandlerName() {
		return DateTypeHandler.class.getName();
	}

}
