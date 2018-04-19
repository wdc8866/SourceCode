package com.yqjr.modules.sequence.dao;

import com.yqjr.framework.annotation.FrameworkDao;
import com.yqjr.framework.base.BaseDao;
import com.yqjr.modules.sequence.condition.SequenceCondition;
import com.yqjr.modules.sequence.entity.Sequence;

@FrameworkDao
public interface SequenceDao extends BaseDao<Integer, Sequence, SequenceCondition> {

	public long getOracleSequence(String sequenceName);

	public Sequence getSequenceForUpdate(String sequenceName);

}