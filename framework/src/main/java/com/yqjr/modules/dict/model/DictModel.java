package com.yqjr.modules.dict.model;

import com.yqjr.framework.base.BaseModel;
import com.yqjr.modules.dict.entity.Dict;

public class DictModel extends BaseModel<Integer, DictModel> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1599946597467827710L;

	private String parentIds;

	private Integer sort;

	private String value;

	private String label;

	private String type;

	private Dict parent;

	public Dict getParent() {
		return parent;
	}

	public void setParent(Dict parent) {
		this.parent = parent;
	}
	
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see com.yqjr.framework.base.BaseModel#getTransactionalKey()
	 */
	@Override
	public String getTransactionalKey() {
		// TODO Auto-generated method stub
		return null;
	}
}