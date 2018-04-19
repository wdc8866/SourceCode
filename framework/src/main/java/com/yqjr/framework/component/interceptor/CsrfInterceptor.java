package com.yqjr.framework.component.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.yqjr.framework.component.csrf.CsrfTokenManager;

/**
 * ClassName: CsrfInterceptor <br>
 * Description: 预防Csrf攻击拦截器 <br>
 * Create By: Wanglei <br>
 * Create Date: 2017年6月14日 下午4:38:33 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 *
 */
public class CsrfInterceptor extends HandlerInterceptorAdapter {
	
	/**
	 * 
	 * Description: 请求前，POST提交进行基于Session的校验 <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年6月16日 上午9:51:46
	 *
	 * @param data
	 * @return
	 */
	@Override
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
        if ("POST".equalsIgnoreCase(request.getMethod())) {
        	CsrfTokenManager.checkTokenFromSession(request);
        }
        return true;
    }
	
	
}
