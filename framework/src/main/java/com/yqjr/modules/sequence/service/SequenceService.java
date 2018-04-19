package com.yqjr.modules.sequence.service;

import com.yqjr.framework.base.BaseService;
import com.yqjr.modules.sequence.condition.SequenceCondition;
import com.yqjr.modules.sequence.dao.SequenceDao;
import com.yqjr.modules.sequence.entity.Sequence;
import com.yqjr.modules.sequence.model.SequenceModel;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
public class SequenceService extends BaseService<Integer, SequenceDao, Sequence, SequenceCondition, SequenceModel> {

	/**
	 * Description: 根据指定序列名称获取序列号 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月27日 上午7:53:10
	 *
	 * @param sequenceName
	 * @return
	 */
	public long getOracleSequence(String sequenceName) {
		return dao.getOracleSequence(sequenceName);
	}

	/**
	 * Description: 更新执行器信息 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月27日 上午11:43:52
	 *
	 * @param sequenceName
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Sequence updateCustomSequence(String sequenceName) {
		Assert.hasText(sequenceName);
		// 读取数据库中序列配置
		Sequence sequence = new Sequence();
		sequence.setSeqName(sequenceName);
		sequence = dao.getSequenceForUpdate(sequenceName);
		Assert.notNull(sequence);
		// 更新序列配置
		Sequence updateSequence = new Sequence();
		updateSequence.setId(sequence.getId());
		updateSequence.setCurrentValue(sequence.getCurrentValue().longValue() + sequence.getSeqCache());
		dao.update(updateSequence);
		return sequence;
	}

}