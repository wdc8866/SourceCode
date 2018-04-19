package com.yqjr.modules.login.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yqjr.framework.base.BaseController;
import com.yqjr.framework.component.shiro.FormAuthenticationFilter;
import com.yqjr.framework.component.shiro.Principal;
import com.yqjr.framework.utils.StringUtils;
import com.yqjr.framework.utils.UserUtils;

/**
 * 
 * ClassName: LoginController <br>
 * Description: 登录控制器 <br>
 * Create By: Wanglei <br>
 * Create Date: 2017年5月5日 下午1:50:48 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 *
 */
@Controller
public class LoginController extends BaseController {

	/**
	 * 
	 * Description: 跳转至登录页面 <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年5月5日 下午1:52:26
	 *
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
		Principal principal = UserUtils.getPrincipal();
		// 如果已经登录，则跳转到管理首页
		if (principal != null && !principal.isMobileLogin()) {
			return "redirect:/index";
		}
		return "framework/login";
	}

	/**
	 * 
	 * Description: 登录失败处理 <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年5月5日 下午1:57:38
	 *
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String loginFail(HttpServletRequest request, HttpServletResponse response, Model model) {
		Principal principal = UserUtils.getPrincipal();
		// 如果已经登录，则跳转到管理首页
		if (principal != null) {
			return "redirect:/index";
		}
		String username = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
		boolean rememberMe = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM);
		boolean mobile = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_MOBILE_PARAM);
		String exception = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
		String message = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM);

		if (StringUtils.isBlank(message) || StringUtils.equals(message, "null")) {
			message = "用户或密码错误, 请重试.";
		}

		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, username);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM, rememberMe);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_MOBILE_PARAM, mobile);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME, exception);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM, message);

		return "framework/login";
	}

	/**
	 * 
	 * Description: 跳转至首页 <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年5月5日 下午1:58:09
	 *
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "index")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		// Principal principal = UserUtils.getPrincipal();
		return "framework/index";
	}

	@RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response,Model model){  
		UserUtils.getSubject().logout();
		return "framework/login";
    } 
}
