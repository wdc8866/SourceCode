package com.yqjr.framework.utils;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.Validate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.yqjr.framework.component.log.Logger;
import com.yqjr.framework.component.shiro.Principal;

/**
 * 
 * ClassName: ServletUtil <br>
 * Description: Servlet及Http操作类 <br>
 * Create By: Wanglei <br>
 * Create Date: 2017年5月12日 下午3:44:03 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 *
 */
public class ServletUtil {

	private static Logger logger = Logger.getLogger();
	
	/**
	 * 常用数值定义
	 */
	public static final long ONE_YEAR_SECONDS = 60 * 60 * 24 * 365;
	
	/**
	 * 静态文件后缀
	 */
	private final static String[] staticFiles = StringUtils.split(".css,.js,.png,.jpg,.gif,.jpeg,.bmp,.ico,.swf,.psd,.htc,.htm,.html,.crx,.xpi,.exe,.ipa,.apk", ",");
	
	/**
	 * 动态映射URL后缀
	 */
	private final static String urlSuffix = ".html";

	/**
	 * 
	 * Description: 设置客户端缓存过期时间 的Header. <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年5月12日 下午3:57:19
	 *
	 * @param response
	 * @param expiresSeconds
	 */
	public static void setExpiresHeader(HttpServletResponse response, long expiresSeconds) {
		/**
		 * Http 1.0 header, set a fix expires date.
		 */
		response.setDateHeader("Expires", System.currentTimeMillis() + expiresSeconds * 1000);
		/**
		 * Http 1.1 header, set a time after now.
		 */
		response.setHeader("Cache-Control", "private, max-age=" + expiresSeconds);
	}

	/**
	 * 
	 * Description: 设置禁止客户端缓存的Header. <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年5月12日 下午3:57:25
	 *
	 * @param response
	 */
	public static void setNoCacheHeader(HttpServletResponse response) {
		/**
		 * Http 1.0 header
		 */
		response.setDateHeader("Expires",1L);
		response.addHeader("PRAGMA", "no-cache");
		/**
		 * Http 1.1 header
		 */
		response.setHeader("Cache-Control", "no-cache, no-store, max-age=0");
	}

	/**
	 * 
	 * Description: 设置LastModified Header. <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年5月12日 下午3:58:00
	 *
	 * @param response
	 * @param lastModifiedDate
	 */
	public static void setLastModifiedHeader(HttpServletResponse response, long lastModifiedDate) {
		response.setDateHeader("Last-Modified", lastModifiedDate);
	}

	/**
	 * 
	 * Description: 设置Etag Header. <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年5月12日 下午3:58:07
	 *
	 * @param response
	 * @param etag
	 */
	public static void setEtag(HttpServletResponse response, String etag) {
		response.setHeader("ETag", etag);
	}

	/**
	 * 
	 * Description: 根据浏览器If-Modified-Since Header, 计算文件是否已被修改.如果无修改, checkIfModify返回false ,设置304 not modify status.
	 * Create By: Wanglei <br>
	 * Create Date: 2017年5月12日 下午3:58:15
	 *
	 * @param request
	 * @param response
	 * @param lastModified 内容的最后修改时间.
	 * @return
	 */
	public static boolean checkIfModifiedSince(HttpServletRequest request, HttpServletResponse response,
			long lastModified) {
		long ifModifiedSince = request.getDateHeader("If-Modified-Since");
		if ((ifModifiedSince != -1) && (lastModified < ifModifiedSince + 1000)) {
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			return false;
		}
		return true;
	}

	/**
	 * 
	 * Description: 根据浏览器 If-None-Match Header, 计算Etag是否已无效.如果Etag有效, checkIfNoneMatch返回false, 设置304 not modify status. <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年5月12日 下午3:58:44
	 *
	 * @param request
	 * @param response
	 * @param etag 内容的ETag
	 * @return
	 */
	public static boolean checkIfNoneMatchEtag(HttpServletRequest request, HttpServletResponse response, String etag) {
		String headerValue = request.getHeader("If-None-Match");
		if (headerValue != null) {
			boolean conditionSatisfied = false;
			if (!"*".equals(headerValue)) {
				StringTokenizer commaTokenizer = new StringTokenizer(headerValue, ",");

				while (!conditionSatisfied && commaTokenizer.hasMoreTokens()) {
					String currentToken = commaTokenizer.nextToken();
					if (currentToken.trim().equals(etag)) {
						conditionSatisfied = true;
					}
				}
			} else {
				conditionSatisfied = true;
			}

			if (conditionSatisfied) {
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				response.setHeader("ETag", etag);
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * Description: 设置让浏览器弹出下载对话框的Header. <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年5月12日 下午3:59:01
	 *
	 * @param response
	 * @param fileName 下载后的文件名.
	 */
	public static void setFileDownloadHeader(HttpServletResponse response, String fileName) {
		try {
			// 中文文件名支持
			String encodedfileName = new String(fileName.getBytes(), "ISO8859-1");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName + "\"");
		} catch (UnsupportedEncodingException e) {
			e.getMessage();
		}
	}

	/**
	 * 
	 * Description: TODO <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年5月12日 下午3:59:13
	 *
	 * @param request
	 * @param prefix
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix) {
		Validate.notNull(request, "Request must not be null");
		Enumeration paramNames = request.getParameterNames();
		Map<String, Object> params = new TreeMap<String, Object>();
		String pre = prefix;
		if (pre == null) {
			pre = "";
		}
		while (paramNames != null && paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			if ("".equals(pre) || paramName.startsWith(pre)) {
				String unprefixed = paramName.substring(pre.length());
				String[] values = request.getParameterValues(paramName);
				if (values == null || values.length == 0) {
					values = new String[]{};
					// Do nothing, no values found at all.
				} else if (values.length > 1) {
					params.put(unprefixed, values);
				} else {
					params.put(unprefixed, values[0]);
				}
			}
		}
		return params;
	}

	/**
	 * 
	 * Description: 组合Parameters生成Query String的Parameter部分,并在paramter name上加上prefix. <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年5月12日 下午4:00:11
	 *
	 * @param params
	 * @param prefix
	 * @return
	 */
	public static String encodeParameterStringWithPrefix(Map<String, Object> params, String prefix) {
		StringBuilder queryStringBuilder = new StringBuilder();

		String pre = prefix;
		if (pre == null) {
			pre = "";
		}
		Iterator<Entry<String, Object>> it = params.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			queryStringBuilder.append(pre).append(entry.getKey()).append("=").append(entry.getValue());
			if (it.hasNext()) {
				queryStringBuilder.append("&");
			}
		}
		return queryStringBuilder.toString();
	}

	/**
	 * 
	 * Description: 客户端对Http Basic验证的 Header进行编码. <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年5月12日 下午4:00:23
	 *
	 * @param userName
	 * @param password
	 * @return
	 */
	public static String encodeHttpBasic(String userName, String password) {
		String encode = userName + ":" + password;
		return "Basic " + Encodes.encodeBase64(encode.getBytes());
	}
	
	/**
	 * 
	 * Description: 是否是Ajax异步请求 <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年5月12日 下午4:00:33
	 *
	 * @param request
	 * @return
	 */
	public static boolean isAjaxRequest(HttpServletRequest request){
		
		String accept = request.getHeader("accept");
		String xRequestedWith = request.getHeader("X-Requested-With");
		Principal principal = UserUtils.getPrincipal();

		// 如果是异步请求或是手机端，则直接返回信息
		return ((accept != null && accept.indexOf("application/json") != -1 
			|| (xRequestedWith != null && xRequestedWith.indexOf("XMLHttpRequest") != -1)
			|| (principal != null && principal.isMobileLogin())));
	}
	
	/**
	 * 
	 * Description: 获取当前请求对象 <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年5月12日 下午4:00:43
	 *
	 * @return
	 */
	public static HttpServletRequest getRequest(){
		try{
			return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		}catch(Exception e){
			return null;
		}
	}

	/**
	 * 
	 * Description: 判断访问URI是否是静态文件请求 <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年5月12日 下午4:00:51
	 *
	 * @param uri
	 * @return
	 */
    public static boolean isStaticFile(String uri){
		if (staticFiles == null){
			try {
				throw new Exception("检测到“app.properties”中没有配置“web.staticFile”属性。配置示例：\n#静态文件后缀\n"
					+"web.staticFile=.css,.js,.png,.jpg,.gif,.jpeg,.bmp,.ico,.swf,.psd,.htc,.crx,.xpi,.exe,.ipa,.apk");
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}
		}
//		if ((StringUtils.startsWith(uri, "/static/") || StringUtils.endsWithAny(uri, sfs)) 
//				&& !StringUtils.endsWithAny(uri, ".jsp") && !StringUtils.endsWithAny(uri, ".java")){
//			return true;
//		}
		if (StringUtils.endsWithAny(uri, staticFiles) && !StringUtils.endsWithAny(uri, urlSuffix)
				&& !StringUtils.endsWithAny(uri, ".jsp") && !StringUtils.endsWithAny(uri, ".java")){
			return true;
		}
		return false;
    }
}
