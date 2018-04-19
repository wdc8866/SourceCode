package com.yqjr.modules.menu.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.yqjr.framework.base.BaseService;
import com.yqjr.framework.component.sequence.AbstractSequenceManager;
import com.yqjr.framework.utils.CommonUtils;
import com.yqjr.framework.utils.Constants;
import com.yqjr.framework.utils.StringUtils;
import com.yqjr.modules.menu.condition.MenuCondition;
import com.yqjr.modules.menu.dao.MenuDao;
import com.yqjr.modules.menu.entity.Menu;
import com.yqjr.modules.menu.model.Item;
import com.yqjr.modules.menu.model.MenuModel;
import com.yqjr.modules.menu.model.TreeModel;
import com.yqjr.modules.user.entity.User;
import com.yqjr.modules.user.model.UserModel;

@Service
public class MenuService extends BaseService<Integer, MenuDao, Menu, MenuCondition, MenuModel> {

	public static Integer rootId = 1;

	public void saveMenu(MenuModel menu) {

		// 获取父节点实体
		// menu.setParent(dao.id(Integer.valueOf(menu.getParentId())));

		// 获取修改前的parentIds，用于更新子节点的parentIds
		String oldParentIds = menu.getParentIds();
		menu.setParent(dao.id(menu.getParent().getId()));
		// 设置新的父节点串
		menu.setParentId(String.valueOf(menu.getParent().getId()));
		menu.setParentIds(menu.getParent().getParentIds() + menu.getParent().getId() + ",");

		// 保存或更新实体
		if (menu.getId() == null) {
			menu.setId(AbstractSequenceManager.getSequenceManager().generateIntSeq(Constants.DEFAULT_SEQ_NAME));
			super.save(menu);
		} else {
			super.update(menu);
		}

		// 更新子节点 parentIds
		Menu m = new Menu();
		m.setParentIds("%," + menu.getId() + ",%");
		List<Menu> list = dao.findByParentIdsLike(m);
		for (Menu e : list) {
			e.setParentIds(e.getParentIds().replace(oldParentIds, menu.getParentIds()));
			dao.updateParentIds(e);
		}
	}

	/**
	 * 
	 * Description: 菜单排序 <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年7月3日 下午1:57:15
	 *
	 * @param list
	 * @param sourcelist
	 * @param parentId
	 * @param cascade
	 */
	public void sortList(List<MenuModel> list, List<MenuModel> sourcelist, Integer parentId, boolean cascade) {
		for (int i = 0; i < sourcelist.size(); i++) {
			MenuModel e = sourcelist.get(i);
			if (e.getParent() != null && e.getParent().getId() != null && e.getParent().getId().equals(parentId)) {
				list.add(e);
				if (cascade) {
					// 判断是否还有子节点, 有则继续获取子节点
					for (int j = 0; j < sourcelist.size(); j++) {
						MenuModel child = sourcelist.get(j);
						if (child.getParent() != null && child.getParent().getId() != null && child.getParent().getId().equals(e.getId())) {
							sortList(list, sourcelist, e.getId(), true);
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * Description: 查询菜单信息（树形） <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年6月12日 下午1:46:32
	 *
	 * @param menuId
	 * @param parentId
	 * @return
	 */
	public List<Item> findMenuTree(String menuId, String parentId) {
		Set<String> checkId = new HashSet<String>();
		if (StringUtils.isNoneBlank(menuId)) {
			Menu condition = new Menu();
			condition.setId(Integer.valueOf(menuId));
			List<Menu> menuList = dao.findList(condition);
			for (Menu menu : menuList) {
				checkId.add(String.valueOf(menu.getId()));
			}
		}
		List<Map<String, Object>> treeList = dao.findForTree(parentId);
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
			//treeModel.setNodeData(false);
			tree.add(treeModel);
		}
		return CommonUtils.buildTree(tree);
	}

	/**
	 * Description: 根据用户查询所属菜单信息 <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年6月13日 下午3:44:41
	 *
	 * @param user
	 * @return
	 */
	public List<MenuModel> findByUser(UserModel userModel) {
		User user = toEntity(userModel, User.class);
		List<MenuModel> list = toModels(dao.findByUser(user), MenuModel.class);
		List<MenuModel> target = new ArrayList<MenuModel>();
		sortList(target, list, 1, true);
		return target;
	}

	public List<MenuModel> getMenuList() {
		List<MenuModel> target = new ArrayList<MenuModel>();
		List<MenuModel> sourcelist = findList(new MenuModel());
		sortList(target, sourcelist, MenuService.rootId, true);
		return target;
	}

	public MenuModel getMenuModel(MenuModel menu) {
		if (menu.getId() != null) {
			menu = id(menu.getId());
			menu.setParentId(String.valueOf(menu.getParent().getId()));
		}
		menu.setParent(getParentById(Integer.valueOf(menu.getParentId())));
		/*
		 * if (menu.getParent()==null||menu.getParent().getId()==null){ Menu
		 * parent = new Menu(); parent.setId(MenuService.rootId);
		 * menu.setParent(parent); }
		 */
		// 获取排序号，最末节点排序号+30
		if (menu.getId() == null) {
			List<MenuModel> list = new ArrayList<MenuModel>();
			List<MenuModel> sourcelist = findList(new MenuModel());
			sortList(list, sourcelist, menu.getParent().getId(), false);
			if (list.size() > 0) {
				menu.setSort(list.get(list.size() - 1).getSort() + 30);
			}
		}
		return menu;
	}

	public Menu getParentById(Integer id) {
		return dao.id(id);
	}
}