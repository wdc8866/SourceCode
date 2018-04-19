/**
 * 
 */
package com.yqjr.framework.base;

import org.apache.commons.lang3.StringUtils;

import com.yqjr.framework.utils.Reflections;

/**
 * ClassName: BaseTreeEntity <br>
 * Description: 树型基类 <br>
 * Create By: admin <br>
 * Create Date: 2017年5月4日 下午3:05:23 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public abstract class BaseTreeEntity<T> extends BaseEntity<Integer, T> {

	private static final long serialVersionUID = 983283187139091836L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yqjr.framework.base.BaseBean#getClazz()
	 */
	@Override
	public Class<T> getClazz() {
		return Reflections.getClassGenricType(this.getClass());
	}

	/**
	 * 父节点信息
	 */
	protected T parent;

	/**
	 * 树节点寻址路径
	 */
	protected String parentIds;

	/**
	 * 节点名称
	 */
	protected String nodeName;

	/**
	 * 节点排序值
	 */
	protected Integer sort;

	public abstract T getParent();

	public abstract void setParent(T parent);

	/**
	 * @return the parentIds
	 */
	public String getParentIds() {
		return parentIds;
	}

	/**
	 * @param parentIds
	 *            the parentIds to set
	 */
	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	/**
	 * @return the nodeName
	 */
	public String getNodeName() {
		return nodeName;
	}

	/**
	 * @param nodeName
	 *            the nodeName to set
	 */
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	/**
	 * @return the sort
	 */
	public Integer getSort() {
		return sort;
	}

	/**
	 * @param sort
	 *            the sort to set
	 */
	public void setSort(Integer sort) {
		this.sort = sort;
	}

	/**
	 * Description: 获取父节点ID <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月4日 下午3:30:58
	 *
	 * @return
	 */
	public String getParentId() {
		String id = null;
		if (parent != null) {
			id = (String) Reflections.getFieldValue(parent, "id");
		}
		return StringUtils.isNotBlank(id) ? id : "0";
	}

}
