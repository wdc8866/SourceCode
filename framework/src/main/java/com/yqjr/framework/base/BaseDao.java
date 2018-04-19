/**
 * 
 */
package com.yqjr.framework.base;

import java.util.List;

import com.yqjr.framework.annotation.FrameworkDao;

/**
 * ClassName: BaseDao <br>
 * Description: 数据访问层基类 <br>
 * Create By: admin <br>
 * Create Date: 2017年4月17日 下午4:06:17 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
@SuppressWarnings("rawtypes")
@FrameworkDao
public interface BaseDao<ID, T extends BaseEntity<ID, T>, C extends BaseCondition> {

	/**
	 * 插入数据
	 * 
	 * @param entity
	 * @return
	 */
	public int insert(T entity);

	/**
	 * 更新数据
	 * 
	 * @param entity
	 * @return
	 */
	public int update(T entity);

	/**
	 * 删除数据
	 * 
	 * @param entity
	 * @return
	 */
	public int delete(T entity);

	/**
	 * Description: 根据ID查询数据 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年4月26日 下午8:04:04
	 *
	 * @param id
	 * @return
	 */
	public T id(ID id);

	/**
	 * 获取单条数据
	 * 
	 * @param entity
	 * @return
	 */
	public T get(T entity);

	/**
	 * 查询实体条件查询列表数据
	 * 
	 * @param entity
	 * @return
	 */
	public List<T> findList(T entity);

	/**
	 * Description: 根据条件查询列表数据 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年4月27日 上午8:16:16
	 *
	 * @param condition
	 *            查询条件
	 * @return
	 */
	public List<?> findByCondition(C condition);

}
