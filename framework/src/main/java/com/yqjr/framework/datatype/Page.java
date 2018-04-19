/**
 * Copyright &copy; YQJR All rights reserved.
 */
package com.yqjr.framework.datatype;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yqjr.framework.component.config.Configuration;
import com.yqjr.framework.utils.CookieUtils;

/**
 * 分页类
 * 
 * @author
 * @version 2013-7-2
 * @param <T>
 */
public class Page<D> {

	private Class<D> clazz;

	/**
	 * 当前页码
	 */
	private int pageNo = 1;

	/**
	 * 页面大小 <br>
	 * -1表示不进行分页<br>
	 */
	private int pageSize = Configuration.getConfig().getIntValue("page.pageSize");

	/**
	 * 总记录数<br>
	 * -1表示不查询总数<br>
	 */
	private long count;

	/**
	 * 首页索引
	 */
	private int first;

	/**
	 * 尾页索引
	 */
	private int last;

	/**
	 * 上一页索引
	 */
	private int prev;

	/**
	 * 下一页索引
	 */
	private int next;

	/**
	 * 总页数
	 */
	private int totalPage;

	/**
	 * 是否是第一页
	 */
	private boolean firstPage;
	/**
	 * 是否是最后一页
	 */
	private boolean lastPage;

	/**
	 * 显示页面长度
	 */
	private int length = 8;

	/**
	 * 前后显示页面长度
	 */
	private int slider = 1;

	/**
	 * 分页数据
	 */
	private List<D> list = new ArrayList<D>();

	/**
	 * 排序信息
	 */
	private OrderBy[] orderBy;

	/**
	 * js分页函数名
	 */
	private String funcName = "page";

	/**
	 * js函数参数
	 */
	private String funcParam = "";

	public Page(Class<D> clazz) {
		this.pageSize = -1;
	}

	/**
	 * 构造方法
	 * 
	 * @param request
	 *            传递 repage 参数，来记住页码
	 * @param response
	 *            用于设置 Cookie，记住页码
	 */
	public Page(Class<D> clazz, HttpServletRequest request, HttpServletResponse response) {
		this(clazz, request, response, -2);
	}

	/**
	 * 构造方法
	 * 
	 * @param request
	 *            传递 repage 参数，来记住页码
	 * @param response
	 *            用于设置 Cookie，记住页码
	 * @param defaultPageSize
	 *            默认分页大小，如果传递 -1 则为不分页，返回所有数据
	 */
	public Page(Class<D> clazz, HttpServletRequest request, HttpServletResponse response, int defaultPageSize) {
		// 设置页码参数（传递repage参数，来记住页码）
		String no = request.getParameter("pageNo");
		if (StringUtils.isNumeric(no)) {
			CookieUtils.setCookie(response, "pageNo", no);
			this.setPageNo(Integer.parseInt(no));
		} else if (request.getParameter("repage") != null) {
			no = CookieUtils.getCookie(request, "pageNo");
			if (StringUtils.isNumeric(no)) {
				this.setPageNo(Integer.parseInt(no));
			}
		}
		// 设置页面大小参数（传递repage参数，来记住页码大小）
		String size = request.getParameter("pageSize");
		if (StringUtils.isNumeric(size)) {
			CookieUtils.setCookie(response, "pageSize", size);
			this.setPageSize(Integer.parseInt(size));
		} else if (request.getParameter("repage") != null) {
			no = CookieUtils.getCookie(request, "pageSize");
			if (StringUtils.isNumeric(size)) {
				this.setPageSize(Integer.parseInt(size));
			}
		} else if (defaultPageSize != -2) {
			this.pageSize = defaultPageSize;
		}
		// 设置排序字段
		String sidx = request.getParameter("sidx");
		String sord = request.getParameter("sord");
		if (StringUtils.isNoneBlank(sidx) && StringUtils.isNoneBlank(sord)) {
			OrderBy order = new OrderBy(sidx, sord);
			OrderBy[] orderBy = new OrderBy[] { order };
			this.setOrderBy(orderBy);
		}
		this.clazz = clazz;
	}

	/**
	 * 构造方法
	 * 
	 * @param pageNo
	 *            当前页码
	 * @param pageSize
	 *            分页大小
	 */
	public Page(int pageNo, int pageSize) {
		this(pageNo, pageSize, 0);
	}

	/**
	 * 构造方法
	 * 
	 * @param pageNo
	 *            当前页码
	 * @param pageSize
	 *            分页大小
	 * @param count
	 *            数据条数
	 */
	public Page(int pageNo, int pageSize, long count) {
		this(pageNo, pageSize, count, new ArrayList<D>());
	}

	/**
	 * 构造方法
	 * 
	 * @param pageNo
	 *            当前页码
	 * @param pageSize
	 *            分页大小
	 * @param count
	 *            数据条数
	 * @param list
	 *            本页数据对象列表
	 */
	public Page(int pageNo, int pageSize, long count, List<D> list) {
		this.setCount(count);
		this.setPageNo(pageNo);
		this.pageSize = pageSize;
		this.list = list;
	}

	/**
	 * 初始化参数
	 */
	public void initialize() {

		// 1
		this.first = 1;

		this.last = (int) (count / (this.pageSize < 1 ? 20 : this.pageSize) + first - 1);

		if (this.count % this.pageSize != 0 || this.last == 0) {
			this.last++;
		}

		if (this.last < this.first) {
			this.last = this.first;
		}

		if (this.pageNo <= 1) {
			this.pageNo = this.first;
			this.firstPage = true;
		}

		if (this.pageNo >= this.last) {
			this.pageNo = this.last;
			this.lastPage = true;
		}

		if (this.pageNo < this.last - 1) {
			this.next = this.pageNo + 1;
		} else {
			this.next = this.last;
		}

		if (this.pageNo > 1) {
			this.prev = this.pageNo - 1;
		} else {
			this.prev = this.first;
		}

		// 2
		if (this.pageNo < this.first) {// 如果当前页小于首页
			this.pageNo = this.first;
		}

		if (this.pageNo > this.last) {// 如果当前页大于尾页
			this.pageNo = this.last;
		}

	}

	/**
	 * 默认输出当前分页标签 <div class="page">${page}</div>
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		long total = pageNo * pageSize;
		long total1 = total > count ? count : total;
		sb.append("<div class=\"col-xs-6\"><div class=\"dataTables_info\">第" + pageNo + "条至" + total1 + ",共 " + count
				+ " 条记录");
		sb.append("</div></div><div class=\"col-xs-6\"><div class=\"dataTables_paginate paging_simple_numbers\">");
		sb.append("<ul class=\"pagination\">\n");
		// 如果是首页
		if (pageNo == first) {
			sb.append("<li class=\"paginate_button previous disabled\"><a href=\"#\">上一页</a></li>\n");
		} else {
			sb.append("<li class=\"paginate_button previous\"><a href=\"javascript:\" onclick=\"" + funcName + "("
					+ prev + "," + pageSize + ",'" + funcParam + "');\">&#171; 上一页</a></li>\n");
		}
		int begin = pageNo - (length / 2);
		if (begin < first) {
			begin = first;
		}
		int end = begin + length - 1;
		if (end >= last) {
			end = last;
			begin = end - length + 1;
			if (begin < first) {
				begin = first;
			}
		}
		if (begin > first) {
			int i = 0;
			for (i = first; i < first + slider && i < begin; i++) {
				sb.append("<li class=\"paginate_button\"><a href=\"javascript:\" onclick=\"" + funcName + "(" + i + ","
						+ pageSize + ",'" + funcParam + "');\">" + (i + 1 - first) + "</a></li>\n");
			}
			if (i < begin) {
				sb.append("<li class=\"paginate_button disabled\"><a href=\"javascript:\">...</a></li>\n");
			}
		}
		for (int i = begin; i <= end; i++) {
			if (i == pageNo) {
				sb.append("<li class=\"paginate_button active\"><a href=\"javascript:\">" + (i + 1 - first)
						+ "</a></li>\n");
			} else {
				sb.append("<li class=\"paginate_button\"><a href=\"javascript:\" onclick=\"" + funcName + "(" + i + ","
						+ pageSize + ",'" + funcParam + "');\">" + (i + 1 - first) + "</a></li>\n");
			}
		}
		if (last - end > slider) {
			sb.append("<li class=\"paginate_button disabled\"><a href=\"javascript:\">...</a></li>\n");
			end = last - slider;
		}
		for (int i = end + 1; i <= last; i++) {
			sb.append("<li class=\"paginate_button\"><a href=\"javascript:\" onclick=\"" + funcName + "(" + i + ","
					+ pageSize + ",'" + funcParam + "');\">" + (i + 1 - first) + "</a></li>\n");
		}
		if (pageNo == last) {
			sb.append("<li class=\"paginate_button disabled\"><a href=\"javascript:\">下一页</a></li>\n");
		} else {
			sb.append("<li class=\"paginate_button\"><a href=\"javascript:\" onclick=\"" + funcName + "(" + next + ","
					+ pageSize + ",'" + funcParam + "');\">" + "下一页</a></li>\n");
		}
		sb.append("</ul>\n");
		return sb.toString();
	}

	public String getOrderBy() {
		// 如果orderBy信息为空则不排序
		if (orderBy == null)
			return null;
		StringBuffer tmp = new StringBuffer();
		for (int i = 0; i < orderBy.length; i++) {
			tmp.append(String.format("%s %s,", orderBy[i].getOrderByColumn(), orderBy[i].getOrder()));
		}
		String orderBySql = tmp.substring(0, tmp.length() - 1);
		// SQL过滤，防止注入
		String reg = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"
				+ "(\\b(select|update|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";
		Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
		if (sqlPattern.matcher(orderBySql).find()) {
			return "";
		}
		return orderBySql;
	}

	/**
	 * @param orderBy
	 *            the orderBy to set
	 */
	public void setOrderBy(OrderBy[] orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * 获取分页HTML代码
	 * 
	 * @return
	 */
	public String getHtml() {
		return toString();
	}

	/**
	 * 获取设置总数
	 * 
	 * @return
	 */
	public long getCount() {
		return count;
	}

	/**
	 * 设置数据总数
	 * 
	 * @param count
	 */
	public void setCount(long count) {
		this.count = count;
		if (pageSize >= count) {
			pageNo = 1;
		}
	}

	/**
	 * 获取当前页码
	 * 
	 * @return
	 */
	public int getPageNo() {
		return pageNo;
	}

	/**
	 * 设置当前页码
	 * 
	 * @param pageNo
	 */
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	/**
	 * 获取页面大小
	 * 
	 * @return
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 设置页面大小（最大500）
	 * 
	 * @param pageSize
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize <= 0 ? 10 : pageSize;// > 500 ? 500 : pageSize;
	}

	/**
	 * 首页索引
	 * 
	 * @return
	 */
	@JsonIgnore
	public int getFirst() {
		return first;
	}

	/**
	 * 尾页索引
	 * 
	 * @return
	 */
	public int getLast() {
		return last;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = getLast();
	}

	/**
	 * 获取页面总数
	 * 
	 * @return getLast();
	 */
	public int getTotalPage() {
		this.totalPage = getLast();
		return totalPage;
	}

	/**
	 * 是否为第一页
	 * 
	 * @return
	 */
	@JsonIgnore
	public boolean isFirstPage() {
		return firstPage;
	}

	/**
	 * 是否为最后一页
	 * 
	 * @return
	 */
	@JsonIgnore
	public boolean isLastPage() {
		return lastPage;
	}

	/**
	 * 上一页索引值
	 * 
	 * @return
	 */
	@JsonIgnore
	public int getPrev() {
		if (isFirstPage()) {
			return pageNo;
		} else {
			return pageNo - 1;
		}
	}

	/**
	 * 下一页索引值
	 * 
	 * @return
	 */
	@JsonIgnore
	public int getNext() {
		if (isLastPage()) {
			return pageNo;
		} else {
			return pageNo + 1;
		}
	}

	/**
	 * 获取本页数据对象列表
	 * 
	 * @return List<T>
	 */
	public List<D> getList() {
		return list;
	}

	/**
	 * 设置本页数据对象列表
	 * 
	 * @param list
	 */
	public Page<D> setList(List<D> list) {
		this.list = list;
		initialize();
		return this;
	}

	/**
	 * 获取点击页码调用的js函数名称 function ${page.funcName}(pageNo){location=
	 * "${ctx}/list-${category.id}${urlSuffix}?pageNo="+i;}
	 * 
	 * @return
	 */
	@JsonIgnore
	public String getFuncName() {
		return funcName;
	}

	/**
	 * 设置点击页码调用的js函数名称，默认为page，在一页有多个分页对象时使用。
	 * 
	 * @param funcName
	 *            默认为page
	 */
	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}

	/**
	 * 获取分页函数的附加参数
	 * 
	 * @return
	 */
	@JsonIgnore
	public String getFuncParam() {
		return funcParam;
	}

	/**
	 * 设置分页函数的附加参数
	 * 
	 * @return
	 */
	public void setFuncParam(String funcParam) {
		this.funcParam = funcParam;
	}

	public boolean isDisabled() {
		return this.pageSize == -1;
	}

	/**
	 * 是否进行总数统计
	 * 
	 * @return this.count==-1
	 */
	@JsonIgnore
	public boolean isNotCount() {
		return this.count == -1;
	}

	public int getFirstResult() {
		int firstResult = (getPageNo() - 1) * getPageSize();
		if (firstResult >= getCount()) {
			firstResult = 0;
		}
		return firstResult;
	}

	public int getMaxResults() {
		return getPageSize();
	}

	public void setLast(int last) {
		this.last = last;
	}

	public Class<D> getClazz() {
		return clazz;
	}
}
