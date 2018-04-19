package com.yqjr.modules.user.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import com.yqjr.modules.role.model.RoleModel;
import com.yqjr.modules.role.service.RoleService;
import com.yqjr.modules.user.condition.UserCondition;
import com.yqjr.modules.user.model.UserModel;
import com.yqjr.modules.user.service.UserService;

/**
 * 
 * ClassName: UserController <br>
 * Description: 用户管理控制器 <br>
 * Create By: Wanglei <br>
 * Create Date: 2017年5月24日 上午10:19:02 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 *
 */
@Controller
@RequestMapping(value = "/sys/user")
public class UserController extends BaseController {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@RequestMapping(value = { "list" })
	public String list(UserCondition userCondition, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
//		String inFileName = "C:/Users/Wanglei/Desktop/学习计划/model.xlsx";
//		String outFileName = "C:/Users/Wanglei/Desktop/学习计划/王雷工作写实201709.xlsx";
//		String modelSheetName = "20170101";
//		String firstDay = "20170901";
//		String lastDay = "20170930";
//		
//		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
//		File file = new File(inFileName);
//		InputStream in = null;
//		OutputStream out = null;
//		try {
//			in = new FileInputStream(file);
//			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(in);
//			Calendar cal1 = Calendar.getInstance();
//			cal1.setTime(sf.parse(firstDay));
//			Calendar cal2 = Calendar.getInstance();
//			cal2.setTime(sf.parse(lastDay));
//			int count = cal2.get(Calendar.DAY_OF_MONTH) - cal1.get(Calendar.DAY_OF_MONTH);
//			
//			int iNum  = xssfWorkbook.getSheetIndex(modelSheetName);
//			for(int i = 0; i < count; i++){
//				cal1.add(Calendar.DAY_OF_MONTH, 1);
//				xssfWorkbook.cloneSheet(iNum);
//				xssfWorkbook.setSheetName(iNum + i + 1, sf.format(cal1.getTime()));
//			}
//
//			//写入新文件
//			out = new FileOutputStream(outFileName);
//			xssfWorkbook.write(out);
//			in.close();
//			out.close();
//		} catch ( ParseException  e) {
//			e.printStackTrace();
//		} catch( IOException e){
//			e.printStackTrace();
//		}
		
		return "framework/system/user/userList";
	}

	@ResponseBody
	@RequestMapping(value = { "listData" })
	public Page<UserModel> listData(UserCondition userCondition, HttpServletRequest request,HttpServletResponse response) {
		Page<UserModel> page = new Page<UserModel>(UserModel.class, request, response);
		userService.findPage(page, userCondition);
		return page;
	}

	@RequestMapping(value = "form")
	public String form(UserModel userModel, Model model) {
		model.addAttribute("userModel", userService.getUserModel(userModel));
		model.addAttribute("roleList", roleService.findList(new RoleModel()));
		return "framework/system/user/userForm";
	}

	@RequestMapping(value = "save")
	public String save(UserModel userModel, HttpServletRequest request, Model model,
			RedirectAttributes redirectAttributes) throws Exception {
		// 参数校验
		if (!beanValidator(model, userModel)) {
			return form(userModel, model);
		}
		// 执行用户保存
		try {
			userService.saveUser(userModel);
		} catch (BizzException e) {
			addMessage(model,super.MESSAGE_TYPE_ERROR, e.getErrorMessage());
			return form(userModel, model);
		}
		addMessage(redirectAttributes,  super.MESSAGE_TYPE_SUCCESS,"保存用户'" + userModel.getLoginName() + "'成功");
		return "redirect:/sys/user/list";
	}

	@RequestMapping(value = "delete")
	public String delete(String ids, RedirectAttributes redirectAttributes) throws Exception {
		if (StringUtils.isNoneBlank(ids)) {
			userService.batchDelete(ids);
			addMessage(redirectAttributes, super.MESSAGE_TYPE_SUCCESS, "删除用户成功");
		}
		return "redirect:/sys/user/list";
	}

	@ResponseBody
	@RequestMapping(value = "checkLoginName")
	public String checkLoginName(String oldLoginName, String loginName, UserModel userModel) {
		return userService.checkUnique(oldLoginName, loginName, userModel);
	}

}
