/**
 * 
 */
package com.yqjr.framework.component.sequence.oracle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.yqjr.framework.component.sequence.AbstractSequenceManager;
import com.yqjr.modules.sequence.service.SequenceService;

/**
 * ClassName: OracleSequenceManager <br>
 * Description: Oracle序列管理器,基于Oracle的Sequence实现 <br>
 * Create By: admin <br>
 * Create Date: 2017年5月17日 上午10:06:00 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
@Service
public class OracleSequenceManager extends AbstractSequenceManager {

	@Autowired
	private SequenceService sequenceService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yqjr.framework.component.sequence.ISequenceManager#generateLongSeq(
	 * java.lang.String)
	 */
	@Override
	public long generateLongSeq(String seqName) {
		Assert.hasText(seqName);
		return sequenceService.getOracleSequence(seqName);
	}

}
