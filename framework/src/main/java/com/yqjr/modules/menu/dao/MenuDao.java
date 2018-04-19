package com.yqjr.modules.menu.dao;

import java.util.List;
import java.util.Map;

import com.yqjr.framework.annotation.FrameworkDao;
import com.yqjr.framework.base.BaseDao;
import com.yqjr.modules.menu.condition.MenuCondition;
import com.yqjr.modules.menu.entity.Menu;
import com.yqjr.modules.role.entity.Role;
import com.yqjr.modules.user.entity.User;

@FrameworkDao
public interface MenuDao extends BaseDao<Integer, Menu, MenuCondition> {

	/**
	 * Description: 根据角色查询菜单信息 <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年6月9日 上午9:42:17
	 *
	 * @param condition
	 * @return
	 */
	List<Menu> findByRole(Role condition);

	/**
	 * Description: 根据parentId查询菜单（树状） <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年6月9日 上午9:42:37
	 *
	 * @param parentId
	 * @return
	 */
	List<Map<String, Object>> findForTree(String parentId);

	/**
	 * Description: TODO <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年6月12日 下午4:44:10
	 *
	 * @param m
	 * @return
	 */
	List<Menu> findByParentIdsLike(Menu menu);

	/**
	 * Description: TODO <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年6月12日 下午4:44:20
	 *
	 * @param e
	 */
	void updateParentIds(Menu menu);

	/**
	 * Description: 根据用户查询所属菜单信息 <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年6月13日 下午3:45:35
	 *
	 * @param user
	 * @return
	 */
	List<Menu> findByUser(User user);
}