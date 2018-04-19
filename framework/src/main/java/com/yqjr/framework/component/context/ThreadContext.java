package com.yqjr.framework.component.context;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.util.Assert;

/**
 * ClassName: ThreadContext <br>
 * Description: 线程上下文,用于存放线程生命周期数据 <br>
 * Create By: admin <br>
 * Create Date: 2016年12月22日 下午2:54:07 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public class ThreadContext {

	private static ThreadContext instance = null;

	private static ThreadLocal<Map<String, Object>> context = null;

	private ThreadContext() {
		context = new ThreadLocal<Map<String, Object>>() {
			@Override
			protected Map<String, Object> initialValue() {
				return new HashMap<String, Object>();
			}
		};
	}

	public static ThreadContext getInstance() {
		if (instance == null) {
			synchronized (ThreadContext.class) {
				if (instance == null) {
					instance = new ThreadContext();
				}
			}
		}
		return instance;
	}

	/**
	 * Description: 将数据放入线程上下文 <br>
	 * Create By: admin <br>
	 * Create Data: 2016年12月22日 下午2:48:35
	 *
	 * @param key
	 * @param obj
	 */
	public void put(String key, Object obj) {
		Assert.hasText(key);
		Assert.notNull(obj);

		context.get().put(key, obj);
	}

	/**
	 * Description: 将数据从线程上下文中取出 <br>
	 * Create By: admin <br>
	 * Create Data: 2016年12月22日 下午2:52:47
	 *
	 * @param key
	 */
	public String getString(String key) {
		Assert.hasText(key);
		return (String) context.get().get(key);
	}

	@SuppressWarnings("unchecked")
	public <T> T getObject(String key) {
		Assert.hasText(key);
		return (T) context.get().get(key);
	}

	public int getInt(String key) {
		Assert.hasText(key);
		return (Integer) context.get().get(key);
	}

	public long getLong(String key) {
		Assert.hasText(key);
		return (Long) context.get().get(key);
	}

	public boolean getBoolean(String key) {
		Assert.hasText(key);
		return (Boolean) context.get().get(key);
	}

	public Map<String, Object> getContext() {
		return context.get();
	}

	public void copy(Map<String, Object> context) {
		Assert.notNull(context);
		if (context.size() > 0) {
			Iterator<String> iter = context.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				put(key, context.get(key));
			}
		}
	}

	/**
	 * Description: 重置线程上下文 <br>
	 * Create By: admin <br>
	 * Create Data: 2016年12月22日 下午2:53:42
	 */
	public void reset() {
		context.get().clear();
	}

	/**
	 * Description: 清理数据信息 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年6月2日 下午3:52:55
	 *
	 * @param key
	 */
	public void remove(String key) {
		context.get().remove(key);
	}
}
