package com.yqjr.modules.menu.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yqjr.framework.base.BaseController;
import com.yqjr.framework.component.dict.DictLoader;
import com.yqjr.framework.datatype.OrderBy;
import com.yqjr.framework.datatype.Page;
import com.yqjr.framework.utils.StringUtils;
import com.yqjr.framework.utils.UserUtils;
import com.yqjr.modules.menu.condition.MenuCondition;
import com.yqjr.modules.menu.entity.Menu;
import com.yqjr.modules.menu.model.Item;
import com.yqjr.modules.menu.model.MenuModel;
import com.yqjr.modules.menu.service.MenuService;

/**
 * ClassName: MenuControll <br>
 * Description: 菜单管理控制器 <br>
 * Create By: Wanglei <br>
 * Create Date: 2017年6月6日 上午7:52:49 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 *
 */
@Controller
@RequestMapping(value = "/sys/menu")
public class MenuController extends BaseController {

	@Autowired
	private MenuService menuService;

	/**
	 * 
	 * Description: 获取左侧菜单数据 <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年6月13日 下午3:42:27
	 *
	 * @param menu
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "menuData" })
	public List<MenuModel> menuData(MenuModel menu, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		return menuService.findByUser(UserUtils.getUser());
	}

	@RequestMapping(value = { "list", "" })
	public String list(MenuCondition menuCondition, Model model) {
		model.addAttribute("list", menuService.getMenuList());
		return "framework/system/menu/menuList";
	}

	@ResponseBody
	@RequestMapping(value = { "listData" })
	public Page<MenuModel> listData(MenuCondition menuCondition, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		if (StringUtils.isBlank(menuCondition.getParentId())) {
			return null;
		} else {
			Page<MenuModel> conditionPage = new Page<MenuModel>(MenuModel.class, request, response);
			OrderBy order = new OrderBy("sort", "asc");
			OrderBy[] orderBy = new OrderBy[] { order };
			conditionPage.setOrderBy(orderBy);
			Page<MenuModel> page = menuService.findPage(conditionPage,menuCondition);
			return page;
		}
	}

	@RequestMapping(value = "form")
	public String form(MenuModel menu, Model model) {
		model.addAttribute("menuList", menuService.findList(new MenuModel()));
		model.addAttribute("menu", menuService.getMenuModel(menu));
		return "framework/system/menu/menuForm";
	}

	@RequestMapping(value = "save")
	public String save(MenuModel menu, Model model, RedirectAttributes redirectAttributes) throws Exception {
		menuService.saveMenu(menu);
		addMessage(redirectAttributes, DictLoader.getInstance().getValue("message_type", "成功"),"菜单"+menu.getName()+"保存成功");
		return "redirect:/sys/menu/list";
	}

	@RequestMapping(value = "delete")
	public String delete(String ids, RedirectAttributes redirectAttributes) throws Exception {
		if (StringUtils.isNoneBlank(ids)) {
			menuService.batchDelete(ids);
			addMessage(redirectAttributes,super.MESSAGE_TYPE_SUCCESS, "删除菜单成功");
		}
		return "redirect:/sys/menu/list";
	}

	@ResponseBody
	@RequestMapping(value = { "treeData" })
	public List<Item> treeData(Menu menu, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<Item> treeData = menuService.findMenuTree(request.getParameter("menuId"),request.getParameter("parent_id"));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", treeData);
		return treeData;
	}
}