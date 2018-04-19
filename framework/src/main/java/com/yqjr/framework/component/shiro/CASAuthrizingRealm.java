package com.yqjr.framework.component.shiro;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cas.CasAuthenticationException;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.cas.CasToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.CollectionUtils;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.TicketValidationException;
import org.jasig.cas.client.validation.TicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

public class CASAuthrizingRealm extends CasRealm {  
	  
    @Autowired  
    private CacheManager cacheManager;  
  
    public CASAuthrizingRealm() {  
        super();  
        setCacheManager(cacheManager);  
    }  
    
    /**
     * token认证
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {  
        CasToken casToken = (CasToken) token;  
        if (token == null)  
            return null;  
        String ticket = (String) casToken.getCredentials();  
        if (!StringUtils.hasText(ticket))  
            return null;  
        TicketValidator ticketValidator = ensureTicketValidator();  
        try {  
            Assertion casAssertion = ticketValidator.validate(ticket, getCasService());  
            AttributePrincipal casPrincipal = casAssertion.getPrincipal();  
            String userId = casPrincipal.getName();  
            Map<String,Object> attributes = casPrincipal.getAttributes();  
            casToken.setUserId(userId);  
            String rememberMeAttributeName = getRememberMeAttributeName();  
            String rememberMeStringValue = (String) attributes.get(rememberMeAttributeName);  
            boolean isRemembered = rememberMeStringValue != null && Boolean.parseBoolean(rememberMeStringValue);  
            if (isRemembered)  
                casToken.setRememberMe(true);  
            List<Object> principals = CollectionUtils.asList(new Object[] { userId, attributes });  
            PrincipalCollection principalCollection = new SimplePrincipalCollection(principals, getName());  
            doGetAuthorizationInfo(principalCollection);
            // 这里可以拿到Cas的登录账号信息,加载到对应权限体系信息放到缓存中...  
			
            return new SimpleAuthenticationInfo(principalCollection, ticket);  
        } catch (TicketValidationException e) {  
            throw new CasAuthenticationException((new StringBuilder()).append("Unable to validate ticket [")  
                    .append(ticket).append("]").toString(), e);  
        }  
    }  
    
    /**
     * 获取权限信息
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {  
//        SimplePrincipalCollection principalCollection = (SimplePrincipalCollection) principals;  
//		List listPrincipals = principalCollection.asList();  
//        String principalJson =(String) listPrincipals.get(0);
//		Map<String,Object> attributes = JSONUtil.getMapFromJson(principalJson);
//        List<Role> roleList = JSONUtil.getDTOList(attributes.get("roleList")+"",Role.class);
//        List<Menu> menuList = JSONUtil.getDTOList(attributes.get("menuList")+"",Menu.class);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();  
        //增加角色信息
//        for(Role role : roleList){
//        	simpleAuthorizationInfo.addRole(role.getEnname());
//        }
        //增加权限表示
//        for(Menu menu : menuList){
//        	simpleAuthorizationInfo.addStringPermission(menu.getPermission());
//        }
        //添加用户权限
        simpleAuthorizationInfo.addStringPermission("user");
        return simpleAuthorizationInfo;  
    }  
  
    /**
     * 重写退出时缓存处理方法
     */
    protected void doClearCache(PrincipalCollection principals) {  
        Object principal = principals.getPrimaryPrincipal();  
        try {  
            getCache().remove(principal);  
        } catch (CacheException e) {  
        }  
    }  
  
    /**
     * 获取缓存管理器的缓存堆实例
     * @return
     * @throws CacheException
     */
    protected Cache<Object, Object> getCache() throws CacheException {  
        return cacheManager.getCache("SHIRO_CACHE");  
    }  
  
    public CacheManager getCacheManager() {  
        return cacheManager;  
    }  
  
    public void setCacheManager(CacheManager cacheManager) {  
        this.cacheManager = cacheManager;  
    }  
  
}  
