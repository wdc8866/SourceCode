package com.yqjr.modules.dict.entity;

import com.yqjr.framework.base.BaseTreeEntity;
import com.yqjr.modules.dict.entity.Dict;

public class Dict extends BaseTreeEntity<Dict> {

	private static final long serialVersionUID = -3027740166937951998L;

	private String value;

	private String label;

	private String type;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yqjr.framework.base.BaseTreeEntity#getParent()
	 */
	@Override
	public Dict getParent() {
		return parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yqjr.framework.base.BaseTreeEntity#setParent(java.lang.Object)
	 */
	@Override
	public void setParent(Dict parent) {
		this.parent = parent;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

}