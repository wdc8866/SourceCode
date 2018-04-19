package com.yqjr.modules.dict.dao;

import java.util.List;
import java.util.Map;

import com.yqjr.framework.annotation.FrameworkDao;
import com.yqjr.framework.base.BaseDao;
import com.yqjr.modules.dict.condition.DictCondition;
import com.yqjr.modules.dict.entity.Dict;

@FrameworkDao
public interface DictDao extends BaseDao<Integer, Dict, DictCondition> {

	/**
	 * Description: 根据parentId查询数据字典（树状） <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年7月6日 上午9:31:43
	 *
	 * @param parentId
	 * @return
	 */
	List<Map<String, Object>> findForTree(String parentId);

	/**
	 * Description: 查询子节点 <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年7月7日 下午2:39:59
	 *
	 * @param d
	 * @return
	 */
	List<Dict> findByParentIdsLike(Dict d);

	/**
	 * Description: 更新子节点 <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年7月7日 下午2:40:05
	 *
	 * @param e
	 */
	void updateParentIds(Dict e);
}