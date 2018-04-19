/**
 * 
 */
package com.yqjr.framework.base;

import com.yqjr.modules.user.entity.User;

/**
 * ClassName: BaseModel <br>
 * Description: Model基类 <br>
 * Create By: admin <br>
 * Create Date: 2017年4月26日 上午11:32:43 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public abstract class BaseModel<ID, M> extends BaseBean<M> {

	private static final long serialVersionUID = -822738806393168223L;

	public static final Integer NORMAL = 1;
	public static final Integer DELETE = 0;

	/**
	 * 数据唯一标识
	 */
	protected ID id;

	/**
	 * 数据备注
	 */
	protected String remarks;

	/**
	 * 数据创建者
	 */
	protected User createBy;

	/**
	 * 数据创建时间
	 */
	protected String createDate;

	/**
	 * 数据更改者
	 */
	protected User updateBy;

	/**
	 * 数据修改时间
	 */
	protected String updateDate;

	/**
	 * 数据状态
	 */
	protected Integer deleteStatus = NORMAL;;

	/**
	 * 数据版本
	 */
	protected Long dataVersion = 0l;

	/**
	 * @return the id
	 */
	public ID getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(ID id) {
		this.id = id;
	}

	/**
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * @param remarks
	 *            the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**
	 * @return the createBy
	 */
	public User getCreateBy() {
		return createBy;
	}

	/**
	 * @param createBy
	 *            the createBy to set
	 */
	public void setCreateBy(User createBy) {
		this.createBy = createBy;
	}

	/**
	 * @return the createDate
	 */
	public String getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate
	 *            the createDate to set
	 */
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the updateBy
	 */
	public User getUpdateBy() {
		return updateBy;
	}

	/**
	 * @param updateBy
	 *            the updateBy to set
	 */
	public void setUpdateBy(User updateBy) {
		this.updateBy = updateBy;
	}

	/**
	 * @return the updateDate
	 */
	public String getUpdateDate() {
		return updateDate;
	}

	/**
	 * @param updateDate
	 *            the updateDate to set
	 */
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
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
	 * @return the dataVersion
	 */
	public Long getDataVersion() {
		return dataVersion;
	}

	/**
	 * @param dataVersion
	 *            the dataVersion to set
	 */
	public void setDataVersion(Long dataVersion) {
		this.dataVersion = dataVersion;
	}
	
	/**
	 * Description: 获取业务主键信息,返回当前model类的业务主键信息字符串 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年7月9日 下午9:35:30
	 *
	 * @return
	 */
	public abstract String getTransactionalKey();

}
