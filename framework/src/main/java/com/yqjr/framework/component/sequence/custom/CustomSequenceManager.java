/**
 * 
 */
package com.yqjr.framework.component.sequence.custom;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.yqjr.framework.component.cache.AbstractCache;
import com.yqjr.framework.component.cache.ICache;
import com.yqjr.framework.component.sequence.AbstractSequenceManager;
import com.yqjr.modules.sequence.entity.Sequence;
import com.yqjr.modules.sequence.service.SequenceService;

/**
 * ClassName: CustomSequenceManager <br>
 * Description: 客户自定义Sequence实现,基于物理表实现 <br>
 * Create By: admin <br>
 * Create Date: 2017年5月17日 上午10:10:54 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
@Service
public class CustomSequenceManager extends AbstractSequenceManager {

	@Autowired
	private SequenceService sequenceService;

	private static final String SEQUENCE_NAME = "framework.sequence.cache";

	/**
	 * 序列缓存
	 */
	private ICache sequenceCache = AbstractCache.getCache();

	/**
	 * 序列执行器
	 */
	private class SequenceExecutor {
		/**
		 * 序列名称
		 */
		private String sequenceName;

		/**
		 * 当前值
		 */
		private AtomicLong current = new AtomicLong(0);

		/**
		 * 缓存序列数据
		 */
		private AtomicInteger cache = new AtomicInteger(0);

		/**
		 * @return the current
		 */
		public long nextValue() {
			if (cache.decrementAndGet() < 0) {
				synchronized (this) {
					// 如果当前cache为负值则需要从数据库中获取
					// 考虑到并发时此处的cache值可能为多个,在cache初始化完毕后,直接从本地缓冲中获取,但仍需要从新缓冲中取走一个值
					if (cache.get() < 0 || cache.decrementAndGet() < 0) {
						return nextValueFromDB(this);
					} else {
						return current.getAndIncrement();
					}
				}
			} else {
				return current.getAndIncrement();
			}
		}

		/**
		 * @param current
		 *            the current to set
		 */
		public void setCurrent(long current) {
			this.current = new AtomicLong(current);
		}

		/**
		 * @return the sequenceName
		 */
		public String getSequenceName() {
			return sequenceName;
		}

		/**
		 * @param sequenceName
		 *            the sequenceName to set
		 */
		public void setSequenceName(String sequenceName) {
			this.sequenceName = sequenceName;
		}

		/**
		 * @param cache
		 *            the cache to set
		 */
		public void setCache(int cache) {
			this.cache.set(cache);
		}

	}

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
		SequenceExecutor sequenceExecutor = sequenceCache.get(SEQUENCE_NAME, seqName);
		if (sequenceExecutor == null) {
			synchronized (this) {
				sequenceExecutor = sequenceCache.get(SEQUENCE_NAME, seqName);
				if (sequenceExecutor == null) {
					// 初始化序列执行器
					sequenceExecutor = new SequenceExecutor();
					sequenceExecutor.setSequenceName(seqName);
					long current = nextValueFromDB(sequenceExecutor);
					sequenceCache.put(SEQUENCE_NAME, seqName, sequenceExecutor);
					return current;
				}
			}
		}
		return sequenceExecutor.nextValue();
	}

	/**
	 * Description: 从数据库中获取nextValue <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月27日 上午10:58:42
	 *
	 * @return
	 */
	private long nextValueFromDB(SequenceExecutor sequenceExecutor) {
		// 读取数据库中序列配置并更新序列执行器
		Sequence sequence = sequenceService.updateCustomSequence(sequenceExecutor.getSequenceName());
		sequenceExecutor.setCache(sequence.getSeqCache());
		sequenceExecutor.setCurrent(sequence.getCurrentValue().longValue());
		return sequenceExecutor.nextValue();
	}

}
