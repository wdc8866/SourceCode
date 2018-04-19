package com.yqjr.modules.role.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yqjr.framework.annotation.FrameworkDao;
import com.yqjr.framework.base.BaseDao;
import com.yqjr.modules.menu.entity.Menu;
import com.yqjr.modules.role.condition.RoleCondition;
import com.yqjr.modules.role.entity.Role;
import com.yqjr.modules.user.entity.User;

@FrameworkDao
public interface RoleDao extends BaseDao<Long, Role, RoleCondition> {

	/**
	 * Description: 根据用户查询对应角色列表 <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年6月8日 下午3:11:23
	 *
	 * @param user
	 * @return
	 */
	List<Role> findListByUser(User user);

	/**
	 * Description: 删除角色菜单关系 <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年6月9日 上午9:39:17
	 *
	 * @param role
	 */
	void deleteRoleMenu(Role role);

	/**
	 * Description: 维护角色菜单关系 <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年6月9日 上午9:39:21
	 *
	 * @param role
	 * @param menuList
	 */
	void insertRoleMenu(@Param("role") Role role, @Param("menuList") List<Menu> menuList);
}