/**
 * 
 */
package com.yqjr.framework.component.sequence;

import java.util.UUID;

import org.codehaus.plexus.util.StringUtils;
import org.springframework.util.Assert;

import com.yqjr.framework.component.config.Configuration;
import com.yqjr.framework.component.context.SpringContext;
import com.yqjr.framework.datatype.BizzException;
import com.yqjr.framework.datatype.Date;

/**
 * ClassName: AbstractSequenceManager <br>
 * Description: 序列管理器 <br>
 * Create By: admin <br>
 * Create Date: 2017年5月17日 上午10:04:58 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public abstract class AbstractSequenceManager implements ISequenceManager {

	private static final String DEFAULT_SEQUENCE_TYPE = "oracle";

	protected static final String DEFAULT_FORMAT = "yyyyMMddHHmmss";

	public static final String ORACLE_SEQUENCE = "oracle";

	public static final String CUSTOM_SEQUENCE = "custom";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yqjr.framework.component.sequence.ISequenceManager#generateUUID()
	 */
	@Override
	public String generateUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * Description: 获取系统默认序列管理器 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月17日 上午10:18:50
	 *
	 * @return
	 */
	public static ISequenceManager getSequenceManager() {
		String sequenceType = Configuration.getConfig().getStringValue("framework.sequence.type");
		sequenceType = StringUtils.isBlank(sequenceType) ? DEFAULT_SEQUENCE_TYPE : sequenceType.trim();
		return getSequenceManager(sequenceType);
	}

	/**
	 * Description: 根据指定类型获取序列管理器 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月17日 上午10:17:12
	 *
	 * @param sequenceType
	 *            序列类型 oracle/custom
	 * @return
	 */
	public static ISequenceManager getSequenceManager(String sequenceType) {
		// oracle序列管理器
		if ("oracle".equalsIgnoreCase(sequenceType)) {
			return SpringContext.getInstance().getBeanWithName("oracleSequenceManager");
		}
		// custom序列管理器
		else if ("custom".equalsIgnoreCase(sequenceType)) {
			return SpringContext.getInstance().getBeanWithName("customSequenceManager");
		} else {
			throw new BizzException("暂不支持的序列管理器类型");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yqjr.framework.component.sequence.ISequenceManager#generateIntSeq(
	 * java.lang.String)
	 */
	@Override
	public int generateIntSeq(String seqName) {
		long seq = generateLongSeq(seqName);
		if (seq > Integer.MAX_VALUE) {
			throw new BizzException("seqvalue grant than Integer max value,please use generateLongSeq");
		}
		return (int) seq;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yqjr.framework.component.sequence.ISequenceManager#generateStringSeq(
	 * java.lang.String, int)
	 */
	@Override
	public String generateStringSeq(String seqName, int len) {
		if (len > 0) {
			return StringUtils.leftPad(String.valueOf(generateLongSeq(seqName)), len, "0");
		} else {
			return String.valueOf(generateLongSeq(seqName));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yqjr.framework.component.sequence.ISequenceManager#generateSerialNo(
	 * java.lang.String, int)
	 */
	@Override
	public String generateSerialNo(String seqName, int len) {
		String seq = generateStringSeq(seqName, len);
		Date currentDate = new Date();
		return currentDate.toString(DEFAULT_FORMAT) + seq;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yqjr.framework.component.sequence.ISequenceManager#generateSerialNo(
	 * java.lang.String, com.yqjr.framework.datatype.Date, int)
	 */
	@Override
	public String generateSerialNo(String seqName, Date date, int len) {
		Assert.notNull(date);
		String seq = generateStringSeq(seqName, len);
		return date.toString(DEFAULT_FORMAT) + seq;
	}

}
