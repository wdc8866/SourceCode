package com.yqjr.modules.dict.condition;

import com.yqjr.framework.base.BaseCondition;

public class DictCondition extends BaseCondition<DictCondition> {
	private Integer id;

	private Integer parentId;

	private String parentIds;

	private Integer sort;

	private String value;

	private String label;

	private String type;

	private DictCondition parent;

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public DictCondition getParent() {
		return parent;
	}

	public void setParent(DictCondition parent) {
		this.parent = parent;
	}
	
	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
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
}