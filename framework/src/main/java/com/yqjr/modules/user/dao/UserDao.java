package com.yqjr.modules.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yqjr.framework.annotation.FrameworkDao;
import com.yqjr.framework.base.BaseDao;
import com.yqjr.modules.role.model.RoleModel;
import com.yqjr.modules.user.condition.UserCondition;
import com.yqjr.modules.user.entity.User;

@FrameworkDao
public interface UserDao extends BaseDao<Long, User, UserCondition> {

	/**
	 * Description: 插入用户角色关系 <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年6月7日 下午5:01:50
	 *
	 * @param entity
	 * @param roleList
	 */
	void insertUserRole(@Param("user") User user, @Param("roleList") List<RoleModel> roleList);

	/**
	 * Description: 删除用户角色关系 <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年6月7日 下午5:01:56
	 *
	 * @param entity
	 */
	void deleteUserRole(User user);

}