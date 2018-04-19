package com.yqjr.modules.sequence.condition;

import com.yqjr.framework.base.BaseCondition;

public class SequenceCondition extends BaseCondition<SequenceCondition> {
	private String seqName;

	private Long currentValue;

	private Integer seqCache;

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