/**
 * Copyright &copy; YQJR All rights reserved.
 */
package com.yqjr.framework.component.shiro;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;

import com.yqjr.framework.component.log.Logger;
import com.yqjr.framework.utils.DateUtils;
import com.yqjr.framework.utils.ServletUtil;
import com.yqjr.framework.utils.StringUtils;


/**
 * 
 * ClassName: CacheSessionDAO <br>
 * Description: 系统安全认证实现类 <br>
 * Create By: Wanglei <br>
 * Create Date: 2017年5月12日 下午5:08:58 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 *
 */
public class CacheSessionDAO extends EnterpriseCacheSessionDAO implements SessionDAO {

	private static Logger logger = Logger.getLogger();
	
    public CacheSessionDAO() {
        super();
    }

    @Override
    protected void doUpdate(Session session) {
    	if (session == null || session.getId() == null) {  
            return;
        }
    	
    	HttpServletRequest request = ServletUtil.getRequest();
		if (request != null){
			String uri = request.getServletPath();
			// 如果是静态文件，则不更新SESSION
			if (ServletUtil.isStaticFile(uri)){
				return;
			}
			// 如果是视图文件，则不更新SESSION TODO
			if (StringUtils.startsWith(uri, "/WEB-INF/views/")
					&& StringUtils.endsWith(uri, ".jsp")){
				return;
			}
			// 手动控制不更新SESSION TODO   FALSE NO
			String updateSession = request.getParameter("updateSession");
			if ("false".equals(updateSession) || "0".equals(updateSession)){
				return;
			}
		}
    	super.doUpdate(session);
    	logger.debug("update {} {}", session.getId()+"|"+ request != null ? request.getRequestURI() : "");
    }

    @Override
    protected void doDelete(Session session) {
    	if (session == null || session.getId() == null) {  
            return;
        }
    	
    	super.doDelete(session);
    	logger.debug("delete {} ", String.valueOf(session.getId()));
    }

    @Override
    protected Serializable doCreate(Session session) {
		HttpServletRequest request = ServletUtil.getRequest();
		if (request != null){
			String uri = request.getServletPath();
			// 如果是静态文件，则不创建SESSION
			if (ServletUtil.isStaticFile(uri)){
		        return null;
			}
		}
		super.doCreate(session);
		logger.debug("doCreate {} {}", session+"|"+request != null ? request.getRequestURI() : "");
    	return session.getId();
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
		return super.doReadSession(sessionId);
    }
    
    @Override
    public Session readSession(Serializable sessionId) throws UnknownSessionException {
    	try{
    		Session s = null;
    		HttpServletRequest request = ServletUtil.getRequest();
    		if (request != null){
    			String uri = request.getServletPath();
    			// 如果是静态文件，则不获取SESSION
    			if (ServletUtil.isStaticFile(uri)){
    				return null;
    			}
    			s = (Session)request.getAttribute("session_"+sessionId);
    		}
    		if (s != null){
    			return s;
    		}

    		Session session = super.readSession(sessionId);
    		logger.debug("readSession {} {}", sessionId+"|"+request == null ? request.getRequestURI() : "");
    		
    		if (request != null && session != null){
    			request.setAttribute("session_"+sessionId, session);
    		}
    		
    		return session;
    	}catch (UnknownSessionException e) {
			return null;
		}
    }

    /**
     * 
     * Description: 获取活动会话 <br>
     * Create By: Wanglei <br>
     * Create Date: 2017年5月12日 下午5:21:43
     *
     * @param includeLeave 是否包括离线（最后访问时间大于3分钟为离线会话）
     * @return
     */
	public Collection<Session> getActiveSessions(boolean includeLeave) {
		return getActiveSessions(includeLeave, null, null);
	}
    
	/**
	 * 
	 * Description: 获取活动会话 <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年5月12日 下午5:21:57
	 *
	 * @param includeLeave 是否包括离线（最后访问时间大于3分钟为离线会话）
	 * @param principal 根据登录者对象获取活动会话
	 * @param filterSession 不为空，则过滤掉（不包含）这个会话。
	 * @return
	 */
	public Collection<Session> getActiveSessions(boolean includeLeave, Object principal, Session filterSession) {
		// 如果包括离线，并无登录者条件。
		if (includeLeave && principal == null){
			return getActiveSessions();
		}
		Set<Session> sessions = new HashSet<Session>();
		for (Session session : getActiveSessions()){
			boolean isActiveSession = false;
			// 不包括离线并符合最后访问时间小于等于3分钟条件。
			if (includeLeave || DateUtils.pastMinutes(session.getLastAccessTime()) <= 3){
				isActiveSession = true;
			}
			// 符合登陆者条件。
			if (principal != null){
				PrincipalCollection pc = (PrincipalCollection)session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
				if (principal.toString().equals(pc != null ? pc.getPrimaryPrincipal().toString() : StringUtils.EMPTY)){
					isActiveSession = true;
				}
			}
			// 过滤掉的SESSION
			if (filterSession != null && filterSession.getId().equals(session.getId())){
				isActiveSession = false;
			}
			if (isActiveSession){
				sessions.add(session);
			}
		}
		return sessions;
	}
	
}
