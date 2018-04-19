package com.yqjr.modules.role.web;

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
import com.yqjr.framework.datatype.BizzException;
import com.yqjr.framework.datatype.Page;
import com.yqjr.framework.utils.StringUtils;
import com.yqjr.modules.menu.model.Item;
import com.yqjr.modules.role.condition.RoleCondition;
import com.yqjr.modules.role.model.RoleModel;
import com.yqjr.modules.role.service.RoleService;

/**
 * ClassName: RoleController <br>
 * Description: 角色管理控制器 <br>
 * Create By: Wanglei <br>
 * Create Date: 2017年6月6日 下午1:47:32 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 *
 */
@Controller
@RequestMapping(value = "/sys/role")
public class RoleController extends BaseController {

	@Autowired
	private RoleService roleService;

	@RequestMapping(value = { "list", "" })
	public String list(RoleCondition roleCondition, Model model) {
		List<RoleModel> list = roleService.findList(new RoleModel());
		model.addAttribute("list", list);
		return "framework/system/role/roleList";
	}

	@ResponseBody
	@RequestMapping(value = { "listData" })
	public Page<RoleModel> listData(RoleCondition roleCondition, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		Page<RoleModel> page = roleService.findPage(new Page<RoleModel>(RoleModel.class, request, response),
				roleCondition);
		return page;
	}

	@ResponseBody
	@RequestMapping(value = { "treeData" })
	public List<Item> treeData(HttpServletRequest request, HttpServletResponse response, Model model) {
		List<Item> treeData = roleService.findMenuTreeForRole(request.getParameter("roleId"),
				request.getParameter("parent_id"));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", treeData);
		return treeData;
	}

	@RequestMapping(value = "form")
	public String form(RoleModel roleModel, Model model) {
		model.addAttribute("roleModel", roleService.getRoleModel(roleModel));
		return "framework/system/role/roleForm";
	}

	@RequestMapping(value = "save")
	public String saverRole(RoleModel roleModel, Model model, RedirectAttributes redirectAttributes) throws Exception {
		if (!beanValidator(model, roleModel)) {
			return form(roleModel, model);
		}
		// 执行用户保存
		try {
			roleService.saveRole(roleModel);
		} catch (BizzException e) {
			addMessage(model,super.MESSAGE_TYPE_ERROR, e.getErrorMessage());
			return form(roleModel, model);
		}
		addMessage(redirectAttributes,super.MESSAGE_TYPE_SUCCESS, "保存角色'" + roleModel.getName() + "'成功");
		return "redirect:/sys/role/";
	}

	@RequestMapping(value = "delete")
	public String delete(String ids, RedirectAttributes redirectAttributes) throws Exception {
		if (StringUtils.isNoneBlank(ids)) {
			roleService.batchDelete(ids);
		}
		addMessage(redirectAttributes,super.MESSAGE_TYPE_SUCCESS,"删除角色成功");
		return "redirect:/sys/role/list";
	}

	/**
	 * 验证角色名是否有效
	 * 
	 * @param oldName
	 * @param name
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "checkName")
	public String checkName(String oldName, String name, RoleModel roleModel) {
		return roleService.checkUnique(oldName, name, roleModel);
	}

	/**
	 * 验证角色英文名是否有效
	 * 
	 * @param oldName
	 * @param name
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "checkEnname")
	public String checkEnname(String oldEnname, String enname, RoleModel roleModel) {
		return roleService.checkUnique(oldEnname, enname, roleModel);
	}
}
