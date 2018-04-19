/**
 * 
 */
package com.yqjr.framework.component.cache;

/**
 * ClassName: ICache <br>
 * Description: 框架缓存接口 <br>
 * Create By: admin <br>
 * Create Date: 2017年5月4日 下午3:45:11 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public interface ICache {

	/**
	 * Description: 从系统默认缓存中获取数据 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月4日 下午3:50:21
	 *
	 * @param key
	 * @return
	 */
	public <T> T get(String key);

	/**
	 * Description: 向系统缓存中添加数据 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月4日 下午3:51:03
	 *
	 * @param key
	 * @param value
	 */
	public void put(String key, Object value);

	/**
	 * Description: 向系统缓存中存放数据,如果存在则返回当前数据,不存在则放入 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月4日 下午3:51:48
	 *
	 * @param key
	 * @param value
	 * @return 当前数据
	 */
	public <T> T putIfAbsent(String key, Object value);

	/**
	 * Description: 从系统缓存中移除数据 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月4日 下午3:51:26
	 *
	 * @param key
	 * @return
	 */
	public <T> T remove(String key);

	/**
	 * Description: 清空系统缓存 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月4日 下午3:54:11
	 */
	public void clear();

	/**
	 * Description: 从指定缓存中获取数据 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月4日 下午3:55:06
	 *
	 * @param cacheName
	 *            缓存名称
	 * @param key
	 * @return
	 */
	public <T> T get(String cacheName, String key);

	/**
	 * Description: 向指定缓存中存放数据 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月4日 下午3:55:27
	 *
	 * @param cacheName
	 *            缓存名称
	 * @param key
	 * @param value
	 */
	public void put(String cacheName, String key, Object value);

	/**
	 * Description: 向指定缓存中存放数据 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月4日 下午3:56:20
	 *
	 * @param cacheName
	 * @param key
	 * @param value
	 */
	public <T> T putIfAbsent(String cacheName, String key, Object value);

	/**
	 * Description: 从指定缓存中移除数据 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月4日 下午3:55:48
	 *
	 * @param cacheName
	 * @param key
	 * @return
	 */
	public <T> T remove(String cacheName, String key);

	/**
	 * Description: 清空指定缓存 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月4日 下午3:56:51
	 *
	 * @param cacheName
	 */
	public void clear(String cacheName);

}
