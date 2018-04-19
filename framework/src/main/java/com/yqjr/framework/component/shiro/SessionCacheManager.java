package com.yqjr.framework.component.shiro;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yqjr.framework.utils.ServletUtil;

/**
 * 
 * ClassName: SessionCacheManager <br>
 * Description: 自定义授权缓存管理类 <br>
 * Create By: Wanglei <br>
 * Create Date: 2017年5月12日 下午5:53:43 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 *
 */
public class SessionCacheManager implements CacheManager {

	@Override
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {
		return new SessionCache<K, V>(name);
	}

	/**
	 * SESSION缓存管理类
	 */
	public class SessionCache<K, V> implements Cache<K, V> {

		private Logger logger = LoggerFactory.getLogger(getClass());
		
		private String cacheKeyName = null;

		public SessionCache(String cacheKeyName) {
			this.cacheKeyName = cacheKeyName;
		}
		
		public Session getSession(){
			Session session = null;
			try{
				Subject subject = SecurityUtils.getSubject();
				session = subject.getSession(false);
				if (session == null){
					session = subject.getSession();
				}
			}catch (InvalidSessionException e){
				logger.error("Invalid session error", e);
			}catch (UnavailableSecurityManagerException e2){
				logger.error("Unavailable SecurityManager error", e2);
			}
			return session;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public V get(K key) throws CacheException {
			if (key == null){
				return null;
			}
			
			V v = null;
			HttpServletRequest request = ServletUtil.getRequest();
			if (request != null){
				v = (V)request.getAttribute(cacheKeyName);
				if (v != null){
					return v;
				}
			}
			
			V value = null;
			value = (V)getSession().getAttribute(cacheKeyName);
			logger.debug("get {} {} {}", cacheKeyName, key, request != null ? request.getRequestURI() : "");
			
			if (request != null && value != null){
				request.setAttribute(cacheKeyName, value);
			}
			return value;
		}

		@Override
		public V put(K key, V value) throws CacheException {
			if (key == null){
				return null;
			}

			getSession().setAttribute(cacheKeyName, value);
			
			if (logger.isDebugEnabled()){
				HttpServletRequest request = ServletUtil.getRequest();
				logger.debug("put {} {} {}", cacheKeyName, key, request != null ? request.getRequestURI() : "");
			}
			
			return value;
		}

		@SuppressWarnings("unchecked")
		@Override
		public V remove(K key) throws CacheException {
			
			V value = null;
			value = (V)getSession().removeAttribute(cacheKeyName);
			logger.debug("remove {} {}", cacheKeyName, key);
			
			return value;
		}

		@Override
		public void clear() throws CacheException {
			getSession().removeAttribute(cacheKeyName);
			logger.debug("clear {}", cacheKeyName);
		}

		@Override
		public int size() {
			logger.debug("invoke session size abstract size method not supported.");
			return 0;
		}

		@Override
		public Set<K> keys() {
			logger.debug("invoke session keys abstract size method not supported.");
			return new HashSet<K>();
		}

		@Override
		public Collection<V> values() {
			logger.debug("invoke session values abstract size method not supported.");
			return Collections.emptyList();
		}
	}
}
