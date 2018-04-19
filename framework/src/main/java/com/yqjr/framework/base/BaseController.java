/**
 * 
 */
package com.yqjr.framework.base;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yqjr.framework.component.dict.DictLoader;
import com.yqjr.framework.component.log.Logger;
import com.yqjr.framework.component.mapper.JsonMapper;
import com.yqjr.framework.component.validator.BeanValidators;
import com.yqjr.framework.datatype.BizzException;
import com.yqjr.framework.datatype.Date;

/**
 * ClassName: BaseController <br>
 * Description: controller基类 <br>
 * Create By: admin <br>
 * Create Date: 2017年5月2日 下午3:51:16 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public abstract class BaseController {

	/**
	 * 日志对象
	 */
	protected Logger logger = Logger.getLogger();

	/**
	 * 管理基础路径
	 */
	@Value("${adminPath}")
	protected String adminPath;

	/**
	 * 前端基础路径
	 */
	@Value("${frontPath}")
	protected String frontPath;

	/**
	 * 前端URL后缀
	 */
	@Value("${urlSuffix}")
	protected String urlSuffix;

	/**
	 * 验证Bean实例对象
	 */
	@Autowired
	protected Validator validator;

	protected String MESSAGE_TYPE_INFO = DictLoader.getInstance().getValue("message_type", "信息");
	protected String MESSAGE_TYPE_SUCCESS = DictLoader.getInstance().getValue("message_type", "成功");
	protected String MESSAGE_TYPE_WARNING = DictLoader.getInstance().getValue("message_type", "警告");
	protected String MESSAGE_TYPE_ERROR = DictLoader.getInstance().getValue("message_type", "失败");
	
	/**
	 * 服务端参数有效性验证
	 * 
	 * @param object
	 *            验证的实体对象
	 * @param groups
	 *            验证组
	 * @return 验证成功：返回true；严重失败：将错误信息添加到 message 中
	 */
	protected boolean beanValidator(Model model, Object object, Class<?>... groups) {
		try {
			BeanValidators.validateWithException(validator, object, groups);
		} catch (ConstraintViolationException ex) {
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
			list.add(0, "数据验证失败：");
			addMessage(model,MESSAGE_TYPE_ERROR, list.toArray(new String[] {}));
			return false;
		}
		return true;
	}

	/**
	 * 服务端参数有效性验证
	 * 
	 * @param object
	 *            验证的实体对象
	 * @param groups
	 *            验证组
	 * @return 验证成功：返回true；严重失败：将错误信息添加到 flash message 中
	 */
	protected boolean beanValidator(RedirectAttributes redirectAttributes, Object object, Class<?>... groups) {
		try {
			BeanValidators.validateWithException(validator, object, groups);
		} catch (ConstraintViolationException ex) {
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
			list.add(0, "数据验证失败：");
			addMessage(redirectAttributes, MESSAGE_TYPE_ERROR,list.toArray(new String[] {}));
			return false;
		}
		return true;
	}

	/**
	 * 服务端参数有效性验证
	 * 
	 * @param object
	 *            验证的实体对象
	 * @param groups
	 *            验证组，不传入此参数时，同@Valid注解验证
	 * @return 验证成功：继续执行；验证失败：抛出异常跳转400页面。
	 */
	protected void beanValidator(Object object, Class<?>... groups) {
		BeanValidators.validateWithException(validator, object, groups);
	}

	/**
	 * 添加Model消息
	 * 
	 * @param message
	 */
	protected void addMessage(Model model, String type,String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages) {
			sb.append(message).append(messages.length > 1 ? "<br/>" : "");
		}
		model.addAttribute("frameMessageContent", sb.toString());
		model.addAttribute("frameMessageType", type);
	}

	/**
	 * 添加Flash消息
	 * 
	 * @param message
	 */
	protected void addMessage(RedirectAttributes redirectAttributes,String type, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages) {
			sb.append(message).append(messages.length > 1 ? "<br/>" : "");
		}
		redirectAttributes.addFlashAttribute("frameMessageContent", sb.toString());
		redirectAttributes.addFlashAttribute("frameMessageType", type);
	}

	/**
	 * 客户端返回JSON字符串
	 * 
	 * @param response
	 * @param object
	 * @return
	 */
	protected void renderString(HttpServletResponse response, Object object) {
		renderString(response, JsonMapper.toJsonString(object), "application/json");
	}

	/**
	 * 客户端返回字符串
	 * 
	 * @param response
	 * @param string
	 * @return
	 */
	protected void renderString(HttpServletResponse response, String string, String type) {
		try {
			response.reset();
			response.setContentType(type);
			response.setCharacterEncoding("utf-8");
			response.getWriter().print(string);
		} catch (IOException e) {
			logger.error("reponse io exception", e);
			throw new BizzException(e);
		}
	}

	/**
	 * 参数绑定异常
	 */
	@ExceptionHandler({ BindException.class, ConstraintViolationException.class, ValidationException.class })
	public String bindException(Exception ex, HttpServletRequest request) {
		// 编译错误信息
		StringBuilder sb = null;
		if (ex instanceof BindException) {
			sb = new StringBuilder("数据绑定异常：\n");
			for (ObjectError e : ((BindException) ex).getGlobalErrors()) {
				sb.append(e.getDefaultMessage() + "(" + e.getObjectName() + ")\n");
			}
			for (FieldError e : ((BindException) ex).getFieldErrors()) {
				sb.append(e.getDefaultMessage() + "(" + e.getField() + ")\n");
			}
			logger.error(ex.getMessage(), ex);
		} else if (ex instanceof ConstraintViolationException) {
			sb = new StringBuilder("参数校验异常：\n");
			for (ConstraintViolation<?> v : ((ConstraintViolationException) ex).getConstraintViolations()) {
				sb.append(v.getMessage() + "(" + v.getPropertyPath() + ")\n");
			}
		} else {
			sb = new StringBuilder("未知异常：\n");
			sb.append(ex.getMessage());
		}
		request.setAttribute("frameMessageContent", sb.toString());
		request.setAttribute("frameMessageType", MESSAGE_TYPE_ERROR);
		return "framework/error/400";
	}

	/**
	 * 授权登录异常
	 */
	@ExceptionHandler({ AuthenticationException.class })
	public String authenticationException() {
		return "framework/error/403";
	}

	/**
	 * Description: 系统内部异常 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月3日 上午9:46:19
	 *
	 * @return
	 */
	@ExceptionHandler({ BizzException.class })
	public String internalException() {
		return "framework/error/500";
	}

	/**
	 * 初始化数据绑定 <br>
	 * 1. 将所有传递进来的String进行HTML编码，防止XSS攻击<br>
	 * 2. 将字段中Date类型转换为String类型<br>
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// TODO 日期、时间、金额自动转换
		// String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
		binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
			}

			@Override
			public String getAsText() {
				Object value = getValue();
				return value != null ? value.toString() : "";
			}
		});
		// Date 类型转换
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(new Date(text));
			}
		});
	}
}
