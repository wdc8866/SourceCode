package com.yqjr.framework.component.csrf;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;

import com.yqjr.framework.component.config.Configuration;
import com.yqjr.framework.component.log.Logger;
import com.yqjr.framework.utils.DESUtil;

/**
 * ClassName: CsrfTokenManager <br>
 * Description: 预防Csrf攻击Token工具 <br>
 * Create By: Wanglei <br>
 * Create Date: 2017年6月14日 下午4:04:47 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 *
 */
public class CsrfTokenManager {
	
	private static Logger logger = Logger.getLogger();
	
    public static final String CSRF_TOKEN_KEY = "CSRFToken_key";
    
    public static final String CSRF_TOKEN_VALUE = "CSRFToken_value";
    
    public static final String CSRF_RESPONSE = "CsrfResponse";
    
    public static final String CSRF_PARAM_NAME = "CSRFToken";
 
    // session中csrfToken参数名称
    public static final String CSRF_TOKEN_FOR_SESSION_ATTR_NAME = CsrfTokenManager.class.getName() + ".tokenval";
 
    private CsrfTokenManager() {
    };
 
    /**
     * 
     * Description: 在cookie中创建csrfToken <br>
     * Create By: Wanglei <br>
     * Create Date: 2017年6月15日 上午8:20:03
     *
     * @param request
     * @param response
     * @return
     */
    public static String createTokenForCookie(HttpServletRequest request,HttpServletResponse response) {
        String token = null;
        synchronized (request) {
        	try {
	        	String cookie_key = UUID.randomUUID().toString();
	    		String cookie_value = UUID.randomUUID().toString();
	    		request.setAttribute(CSRF_TOKEN_KEY, cookie_key);
	    		request.setAttribute(CSRF_TOKEN_VALUE, DESUtil.encryptDESandBASE64(cookie_value));
	    		Cookie cookieKey = new SimpleCookie(cookie_key);
	    		cookieKey.setPath(request.getContextPath());
	    		cookieKey.setHttpOnly(true);
				cookieKey.setValue(URLEncoder.encode(cookie_value, "utf-8"));
	    		cookieKey.readValue(request, response);
	    		cookieKey.saveTo(request, response);
	    		
	    		/*Cookie cookieValue = new SimpleCookie(cookie_value);
	    		cookieValue.setPath(request.getContextPath());
	    		cookieValue.setHttpOnly(true);
	    		cookieValue.setValue(URLEncoder.encode(cookie_value, "utf-8"));
	    		cookieValue.readValue(request, response);
	    		cookieValue.saveTo(request, response);*/
        	} catch (UnsupportedEncodingException e) {
        		logger.error("创建CsrfCookie失败！", e);
			}
        }
        return token;
    }
    
    /**
     * 
     * Description: 在session中创建csrfToken <br>
     * Create By: Wanglei <br>
     * Create Date: 2017年6月16日 上午9:50:13
     *
     * @param httpSession
     * @return
     */
    public static String createTokenForSession(HttpSession httpSession){
    	String token = null;
        synchronized (httpSession) {
        	token = (String) httpSession.getAttribute(CSRF_TOKEN_FOR_SESSION_ATTR_NAME);
            if (null == token) {
                token = UUID.randomUUID().toString();
                httpSession.setAttribute(CSRF_TOKEN_FOR_SESSION_ATTR_NAME, token);
            }
        }
        return token;
    }
    
    /**
     * 
     * Description: 严重Session中的csrfToken <br>
     * Create By: Wanglei <br>
     * Create Date: 2017年6月16日 上午9:50:29
     *
     * @param request
     */
    public static void checkTokenFromSession(HttpServletRequest request){
    	//CSRF校验控制
    	if(Configuration.getConfig().getBooleanValue("framework.csrf")){
    		String CsrfToken = CsrfTokenManager.getTokenFromRequest(request);
    		if (CsrfToken == null || !CsrfToken.equals(request.getSession().getAttribute(
    				CsrfTokenManager.CSRF_TOKEN_FOR_SESSION_ATTR_NAME))) {
    			throw new AuthenticationException("非法请求");
    		}
    	}
    }
    
    /**
     * 
     * Description: 获取表单中csrfToken <br>
     * Create By: Wanglei <br>
     * Create Date: 2017年6月16日 上午9:50:45
     *
     * @param request
     * @return
     */
    public static String getTokenFromRequest(HttpServletRequest request) {
        return request.getParameter(CSRF_PARAM_NAME);
    }
}
