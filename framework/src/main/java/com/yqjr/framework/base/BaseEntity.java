/**
 * 
 */
package com.yqjr.framework.base;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.yqjr.framework.component.mapper.BeanMapper;
import com.yqjr.framework.component.sequence.AbstractSequenceManager;
import com.yqjr.framework.datatype.Date;
import com.yqjr.framework.utils.Constants;
import com.yqjr.framework.utils.Reflections;
import com.yqjr.framework.utils.UserUtils;
import com.yqjr.modules.user.entity.User;
import com.yqjr.modules.user.model.UserModel;

/**
 * ClassName: BaseEntity <br>
 * Description: 实体基类 <br>
 * Create By: admin <br>
 * Create Date: 2017年4月19日 上午10:57:00 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public abstract class BaseEntity<ID, T> extends BaseBean<T> {

	private static final long serialVersionUID = 1587278644584263790L;

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
	protected Date createDate;

	/**
	 * 数据更改者
	 */
	protected User updateBy;

	/**
	 * 数据修改时间
	 */
	protected Date updateDate;

	/**
	 * 数据状态
	 */
	protected Integer deleteStatus = NORMAL;

	/**
	 * 数据版本
	 */
	protected Long dataVersion;

	/**
	 * 自动生成ID
	 */
	protected boolean isAutoId = true;

	/**
	 * Description: 获取数据唯一标识(由子类实现) <br>
	 * Create By: admin <br>
	 * Create Data: 2017年4月19日 上午11:18:17
	 *
	 * @return
	 */
	public abstract ID getId();

	public abstract void setId(ID id);

	/**
	 * Description: 自动生成ID <br>
	 * Create By: admin <br>
	 * Create Date: 2017年6月28日 上午10:55:29
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ID autoId() {
		if (this.id == null) {
			String id = AbstractSequenceManager.getSequenceManager().generateStringSeq(Constants.DEFAULT_SEQ_NAME, 0);
			Class<ID> clazz = Reflections.getClassGenricType(this.getClass(), 0);
			if (clazz == Long.class) {
				return (ID) Long.valueOf(id);
			} else if (clazz == Integer.class) {
				return (ID) Integer.valueOf(id);
			} else {
				return (ID) id;
			}
		} else
			return id;
	}

	/**
	 * Description: 插入数据前动作<br>
	 * Create By: admin <br>
	 * Create Data: 2017年4月19日 上午11:19:08
	 */
	public void preInsert() {
		if (isAutoId) {
			this.id = autoId();
		}
		UserModel userModel = UserUtils.getUser();
		User user = new User();
		BeanMapper.beanToBean(userModel, user);
		if (user.getId() > 0) {
			this.updateBy = user;
			this.createBy = user;
		}
		this.updateDate = new Date();
		this.createDate = this.updateDate;
	}

	/**
	 * Description: 更新数据前动作(由子类实现) <br>
	 * Create By: admin <br>
	 * Create Data: 2017年4月19日 上午11:19:27
	 */
	public void preUpdate() {
		UserModel userModel = UserUtils.getUser();
		User user = new User();
		BeanMapper.beanToBean(userModel, user);
		if (user.getId() > 0) {
			this.updateBy = user;
		}
		this.updateDate = new Date();
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
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate
	 *            the createDate to set
	 */
	public void setCreateDate(Date createDate) {
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
	public Date getUpdateDate() {
		return updateDate;
	}

	/**
	 * @param updateDate
	 *            the updateDate to set
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * @return the isAutoId
	 */
	public boolean isAutoId() {
		return isAutoId;
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
	 * @param isAutoId
	 *            the isAutoId to set
	 */
	public void setAutoId(boolean isAutoId) {
		this.isAutoId = isAutoId;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
