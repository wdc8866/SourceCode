package com.yqjr.modules.sequence.entity;

import com.yqjr.framework.base.BaseEntity;
import com.yqjr.modules.sequence.entity.Sequence;

public class Sequence extends BaseEntity<Integer, Sequence> {

	private static final long serialVersionUID = 1085906850902494356L;

	private String seqName;

	private Long currentValue;

	private Integer seqCache;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSeqName() {
		return seqName;
	}

	public void setSeqName(String seqName) {
		this.seqName = seqName;
	}

	public Long getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(Long currentValue) {
		this.currentValue = currentValue;
	}

	public Integer getSeqCache() {
		return seqCache;
	}

	public void setSeqCache(Integer seqCache) {
		this.seqCache = seqCache;
	}
}