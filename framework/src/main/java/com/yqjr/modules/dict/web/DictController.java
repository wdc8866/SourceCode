package com.yqjr.modules.dict.web;

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
import com.yqjr.framework.datatype.Page;
import com.yqjr.framework.utils.StringUtils;
import com.yqjr.modules.dict.condition.DictCondition;
import com.yqjr.modules.dict.entity.Dict;
import com.yqjr.modules.dict.model.DictModel;
import com.yqjr.modules.dict.service.DictService;
import com.yqjr.modules.menu.model.Item;

/**
 * 
 * ClassName: DictController <br>
 * Description: 数据字典控制器 <br>
 * Create By: Wanglei <br>
 * Create Date: 2017年7月6日 上午9:20:23 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 *
 */
@Controller
@RequestMapping(value = "/sys/dict")
public class DictController extends BaseController {

	@Autowired
	private DictService dictService;

	@RequestMapping(value = { "list", "" })
	public String list(DictCondition dictCondition, Model model) {
		return "framework/system/dict/dictList";
	}

	@ResponseBody
	@RequestMapping(value = { "listData" })
	public Page<DictModel> listData(DictCondition dictCondition, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		if (dictCondition.getParentId() == null) {
			return null;
		} else {
			Page<DictModel> conditionPage = new Page<DictModel>(DictModel.class, request, response);
			return dictService.findPage(conditionPage,dictCondition);
		}
	}

	@RequestMapping(value = "form")
	public String form(DictModel dictModel, Model model) {
		model.addAttribute("dictModel", dictService.get(dictModel));
		return "framework/system/dict/dictForm";
	}

	@RequestMapping(value = "save")
	public String save(DictModel dict, Model model, RedirectAttributes redirectAttributes) throws Exception {
		dictService.saveModel(dict);
		addMessage(redirectAttributes, DictLoader.getInstance().getValue("message_type", "成功"),"数据字典"+dict.getLabel()+"保存成功");
		return "redirect:/sys/dict/list";
	}

	@RequestMapping(value = "delete")
	public String delete(String ids, RedirectAttributes redirectAttributes) throws Exception {
		if (StringUtils.isNoneBlank(ids)) {
			dictService.batchDelete(ids);
			addMessage(redirectAttributes,super.MESSAGE_TYPE_SUCCESS, "删除数据字典成功");
		}
		return "redirect:/sys/dict/list";
	}

	@ResponseBody
	@RequestMapping(value = { "treeData" })
	public List<Item> treeData(Dict menu, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<Item> treeData = dictService.findDictTree(request.getParameter("menuId"),request.getParameter("parent_id"));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", treeData);
		return treeData;
	}
}