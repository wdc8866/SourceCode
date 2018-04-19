/**
 * 
 */
package com.yqjr.framework.component.cache.map;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.Assert;

import com.yqjr.framework.component.cache.ICache;

/**
 * ClassName: MapCache <br>
 * Description: 基于JVM内存的缓存实现 <br>
 * Create By: admin <br>
 * Create Date: 2017年5月4日 下午4:31:33 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public class MapCache implements ICache {

	private static Map<String, Map<String, Object>> cache = new HashMap<String, Map<String, Object>>();

	private static final String FRAMEWORK_CACHE = "framework.cache";

	private static MapCache instance = null;

	private MapCache() {
	}

	public static MapCache getInstance() {
		if (instance == null) {
			synchronized (MapCache.class) {
				if (instance == null) {
					instance = new MapCache();
				}
			}
		}
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yqjr.framework.component.cache.ICache#get(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object get(String key) {
		Assert.hasText(key);
		return getCache(FRAMEWORK_CACHE).get(key.trim());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yqjr.framework.component.cache.ICache#put(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public void put(String key, Object value) {
		Assert.hasText(key);
		Assert.notNull(value);
		getCache(FRAMEWORK_CACHE).put(key.trim(), value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yqjr.framework.component.cache.ICache#putIfAbsent(java.lang.String,
	 * java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object putIfAbsent(String key, Object value) {
		Assert.hasText(key);
		Assert.notNull(value);
		return getCache(FRAMEWORK_CACHE).putIfAbsent(key.trim(), value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yqjr.framework.component.cache.ICache#remove(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object remove(String key) {
		Assert.hasText(key);
		return getCache(FRAMEWORK_CACHE).remove(key.trim());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yqjr.framework.component.cache.ICache#clear()
	 */
	@Override
	public void clear() {
		getCache(FRAMEWORK_CACHE).clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yqjr.framework.component.cache.ICache#get(java.lang.String,
	 * java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object get(String cacheName, String key) {
		Assert.hasText(cacheName);
		Assert.hasText(key);
		return getCache(cacheName.trim()).get(key.trim());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yqjr.framework.component.cache.ICache#put(java.lang.String,
	 * java.lang.String, java.lang.Object)
	 */
	@Override
	public void put(String cacheName, String key, Object value) {
		Assert.hasText(cacheName);
		Assert.hasText(key);
		Assert.notNull(value);
		getCache(cacheName.trim()).put(key.trim(), value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yqjr.framework.component.cache.ICache#putIfAbsent(java.lang.String,
	 * java.lang.String, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object putIfAbsent(String cacheName, String key, Object value) {
		Assert.hasText(cacheName);
		Assert.hasText(key);
		Assert.notNull(value);
		return getCache(cacheName.trim()).putIfAbsent(key.trim(), value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yqjr.framework.component.cache.ICache#remove(java.lang.String,
	 * java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object remove(String cacheName, String key) {
		Assert.hasText(cacheName);
		Assert.hasText(key);
		return getCache(cacheName.trim()).remove(key.trim());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yqjr.framework.component.cache.ICache#clear(java.lang.String)
	 */
	@Override
	public void clear(String cacheName) {
		Assert.hasText(cacheName);
		getCache(cacheName.trim()).clear();
	}

	/**
	 * Description: 根据缓存名称获取缓存实例,如果不存在则创建 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月5日 上午8:44:34
	 *
	 * @param cacheName
	 * @return
	 */
	private ConcurrentHashMap<String, Object> getCache(String cacheName) {
		Assert.hasText(cacheName);
		ConcurrentHashMap<String, Object> scache = (ConcurrentHashMap<String, Object>) cache.get(cacheName);
		if (scache == null) {
			synchronized (this) {
				if ((scache = (ConcurrentHashMap<String, Object>) cache.get(cacheName)) == null) {
					scache = new ConcurrentHashMap<String, Object>();
					cache.put(cacheName, scache);
				}
			}
		}
		return scache;
	}

}
