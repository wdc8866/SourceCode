/**
 * 
 */
package com.yqjr.framework.datatype;

import org.springframework.util.Assert;

/**
 * ClassName: OrderBy <br>
 * Description: 排序条件 <br>
 * Create By: admin <br>
 * Create Date: 2017年4月24日 下午4:34:33 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public class OrderBy {

	/**
	 * 排序
	 */
	public static final String ASC = "ASC";
	public static final String DESC = "DESC";

	/**
	 * 排序字段
	 */
	private String orderByColumn;

	/**
	 * 升序/降序
	 */
	private String order = ASC;

	/**
	 * @param orderByColumn 排序列名称
	 * @param order ASC/DESC
	 */
	public OrderBy(String orderByColumn, String order) {
		this.orderByColumn = orderByColumn;
		this.order = order;
	}

	/**
	 * @return the orderByColumn
	 */
	public String getOrderByColumn() {
		return orderByColumn;
	}

	/**
	 * @param orderByColumn
	 *            the orderByColumn to set
	 */
	public void setOrderByColumn(String orderByColumn) {
		this.orderByColumn = orderByColumn;
	}

	/**
	 * @return the order
	 */
	public String getOrder() {
		return order;
	}

	/**
	 * @param order
	 *            the order to set
	 */
	public void setOrder(String order) {
		Assert.isTrue(ASC.equalsIgnoreCase(order) || DESC.equalsIgnoreCase(order));
		this.order = order;
	}
}
