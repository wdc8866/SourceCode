package com.yqjr.framework.component.csrf;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.RequestDataValueProcessor;

/**
 * ClassName: CsrfRequestDataValueProcessor <br>
 * Description: <form:form>实现类：自动创建hidden的csrfToken域 <br>
 * Create By: Wanglei <br>
 * Create Date: 2017年6月14日 下午4:35:13 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 *
 */
@Component("requestDataValueProcessor")
public class CsrfRequestDataValueProcessor implements RequestDataValueProcessor{

	@Override
	public String processAction(HttpServletRequest request, String action, String httpMethod) {
		return action;
	}

	@Override
	public String processFormFieldValue(HttpServletRequest request, String name, String value, String type) {
		return value;
	}

	/**
     * 
     * Description: 此处是当使用spring的taglib标签<form:form>创建表单时候，增加的隐藏域参数 <br>
     * Create By: Wanglei <br>
     * Create Date: 2017年6月15日 上午8:20:03
     *
     * @param request
     * @return
     */
	@Override
	public Map<String, String> getExtraHiddenFields(HttpServletRequest request) {
		String token = CsrfTokenManager.createTokenForSession(request.getSession());
        Map<String, String> hiddenFields = new HashMap<String,String>();
        hiddenFields.put(CsrfTokenManager.CSRF_PARAM_NAME, token);
        return hiddenFields;
	}

	@Override
	public String processUrl(HttpServletRequest request, String url) {
		return url;
	}

}
