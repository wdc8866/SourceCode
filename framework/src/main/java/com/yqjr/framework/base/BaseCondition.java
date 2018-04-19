/**
 * 
 */
package com.yqjr.framework.base;

/**
 * ClassName: BaseCondition <br>
 * Description: 查询条件基类 <br>
 * Create By: admin <br>
 * Create Date: 2017年4月24日 下午3:20:59 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public class BaseCondition<C extends BaseCondition<C>> {

	public static final Integer NORMAL = 1;
	public static final Integer DELETE = 0;

	/**
	 * 开始时间
	 */
	private String startDate;

	/**
	 * 截止时间
	 */
	private String endDate;

	/**
	 * 数据状态
	 */
	private Integer deleteStatus = NORMAL;

	/**
	 * 用户ID
	 */
	private long userId;

	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the deleteStatus
	 */
	public Integer getDeleteStatus() {
		return deleteStatus;
	}

	/**
	 * @param deleteStatus
	 *            the deleteStatus to set
	 */
	public void setDeleteStatus(Integer deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

}
