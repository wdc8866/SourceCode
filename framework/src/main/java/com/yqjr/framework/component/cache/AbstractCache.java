/**
 * 
 */
package com.yqjr.framework.component.cache;

import org.apache.commons.lang3.StringUtils;

import com.yqjr.framework.component.cache.map.MapCache;
import com.yqjr.framework.component.config.Configuration;
import com.yqjr.framework.datatype.BizzException;

/**
 * ClassName: CacheFactory <br>
 * Description: 缓存组件工厂 <br>
 * Create By: admin <br>
 * Create Date: 2017年5月4日 下午4:32:42 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public abstract class AbstractCache implements ICache {

	private static final String CACHE_TYPE = "map";

	public static final String MAP_CACHE = "map";

	public static final String EHCACHE_CACHE = "ehcache";

	/**
	 * Description: 获取系统默认缓存对象 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月4日 下午4:36:34
	 *
	 * @return
	 */
	public static ICache getCache() {
		// 获取全局缓存类型配置
		String cacheType = Configuration.getConfig().getStringValue("framework.cache.type");
		cacheType = StringUtils.isBlank(cacheType) ? CACHE_TYPE : cacheType.trim();
		return getCache(cacheType);
	}

	/**
	 * Description: 根据指定类型获取缓存对象 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月17日 上午11:13:43
	 *
	 * @param cacheType
	 *            缓存类型
	 * @return
	 */
	public static ICache getCache(String cacheType) {
		if ("map".equalsIgnoreCase(cacheType)) {
			return MapCache.getInstance();
		} else {
			throw new BizzException("暂不支持的缓存类型");
		}
	}

}
