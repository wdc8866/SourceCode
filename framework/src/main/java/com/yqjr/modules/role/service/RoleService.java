package com.yqjr.modules.role.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yqjr.framework.base.BaseService;
import com.yqjr.framework.datatype.BizzException;
import com.yqjr.framework.utils.CommonUtils;
import com.yqjr.framework.utils.StringUtils;
import com.yqjr.modules.menu.dao.MenuDao;
import com.yqjr.modules.menu.entity.Menu;
import com.yqjr.modules.menu.model.Item;
import com.yqjr.modules.menu.model.TreeModel;
import com.yqjr.modules.role.condition.RoleCondition;
import com.yqjr.modules.role.dao.RoleDao;
import com.yqjr.modules.role.entity.Role;
import com.yqjr.modules.role.model.RoleModel;
import com.yqjr.modules.user.entity.User;
import com.yqjr.modules.user.model.UserModel;

@Service
public class RoleService extends BaseService<Long, RoleDao, Role, RoleCondition, RoleModel> {

	@Autowired
	private MenuDao menuDao;

	public RoleModel getRoleModel(RoleModel roleModel) {
		if (roleModel.getId() != null) {
			roleModel = id(roleModel.getId());
		} else {
			roleModel.setIsSys("1");
			roleModel.setDataScope("1");
			roleModel.setRoleType("user");
		}
		return roleModel;
	}

	/**
	 * Description: 根据用户查询对应角色列表 <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年6月8日 下午3:10:41
	 *
	 * @param user
	 * @return
	 */
	public List<RoleModel> findListByUser(UserModel user) {
		List<RoleModel> list = toModels(dao.findListByUser(toEntity(user, User.class)), RoleModel.class, false);
		return list;
	}

	/**
	 * Description: 根据角色信息查询菜单列表（树形） <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年6月8日 下午5:31:18
	 *
	 * @param roleId
	 * @param parentId
	 * @return
	 */
	public List<Item> findMenuTreeForRole(String roleId, String parentId) {
		Set<String> checkId = new HashSet<String>();
		if (StringUtils.isNoneBlank(roleId)) {
			Role condition = new Role();
			condition.setId(Long.valueOf(roleId));
			List<Menu> menuList = menuDao.findByRole(condition);
			for (Menu menu : menuList) {
				checkId.add(String.valueOf(menu.getId()));
			}
		}
		List<Map<String, Object>> treeList = menuDao.findForTree(parentId);
		List<TreeModel> tree = new ArrayList<TreeModel>();
		for (Map<String, Object> row : treeList) {

			TreeModel treeModel = new TreeModel();
			treeModel.setNodeId(row.get("ID").toString());
			treeModel.setNodeName(row.get("NAME").toString());
			treeModel.setParentId(row.get("PARENT_ID").toString());
			treeModel.setParentIds(row.get("PARENT_IDS").toString());
			long count = Long.parseLong(row.get("COUNTS").toString());
			treeModel.setParent(count > 0);
			treeModel.setNodeData(checkId.contains(row.get("ID").toString()) || treeModel.isParent());
			// treeModel.setNodeData(false);
			tree.add(treeModel);
		}
		return CommonUtils.buildTree(tree);
	}

	/**
	 * Description: 角色插入\更新业务处理 <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年6月9日 上午9:13:08
	 *
	 * @param roleModel
	 */
	public void saveRole(RoleModel roleModel) {

		if (!"true".equals(checkUnique(roleModel.getOldName(), roleModel.getName(), roleModel))) {
			throw new BizzException("保存角色'" + roleModel.getName() + "'失败, 角色名已存在");
		}
		if (StringUtils.isNoneBlank(roleModel.getMenuIds())) {
			for (String menuId : roleModel.getMenuIds().split(",")) {
				Menu menu = new Menu();
				menu.setId(Integer.valueOf(menuId));
				roleModel.getMenuList().add(menu);
			}
		}
		// 新增
		if (roleModel.getId() == null) {
			roleModel.setId(save(roleModel));
		} else {
			update(roleModel);
		}
		Role role = toEntity(roleModel, Role.class);
		if (roleModel.getId() != null) {
			dao.deleteRoleMenu(role);
		}
		// 更新角色与菜单关联
		if (CollectionUtils.isNotEmpty(roleModel.getMenuList())) {
			dao.insertRoleMenu(role, roleModel.getMenuList());
		}
	}
}