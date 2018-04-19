package com.yqjr.modules.sequence.model;

import com.yqjr.framework.base.BaseModel;

public class SequenceModel extends BaseModel<Integer, SequenceModel> {

	private static final long serialVersionUID = -2699647527766276111L;

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

	/* (non-Javadoc)
	 * @see com.yqjr.framework.base.BaseModel#getTransactionalKey()
	 */
	@Override
	public String getTransactionalKey() {
		// TODO Auto-generated method stub
		return null;
	}
}