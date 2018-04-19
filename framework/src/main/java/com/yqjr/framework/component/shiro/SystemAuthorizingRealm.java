package com.yqjr.framework.component.shiro;

import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.yqjr.framework.component.context.SpringContext;
import com.yqjr.framework.datatype.Date;
import com.yqjr.framework.utils.StringUtils;
import com.yqjr.modules.user.model.UserModel;
import com.yqjr.modules.user.service.UserService;

/**
 * 
 * ClassName: SystemAuthorizingRealm <br>
 * Description: 自定义Shiro登录Realm <br>
 * Create By: Wanglei <br>
 * Create Date: 2017年6月8日 上午8:33:41 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 *
 */
@Service
public class SystemAuthorizingRealm extends AuthorizingRealm {

	/**
	 * 该类Service不能使用@Autowired，不能在初始化时使用SpringContext.getInstance().
	 * getBeanWithClass(UserService.class) 会引起@Transactional失效
	 * 解决办法：改写getUserSerivce(),再调用时获取
	 */
	private UserService userService;

	/**
	 * 认证回调函数, 登录时调用
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;

		// 校验登录验证码
		// if (LoginController.isValidateCodeLogin(token.getUsername(), false,
		// false)){
		// Session session = UserUtils.getSession();
		// String code =
		// (String)session.getAttribute(ValidateCodeServlet.VALIDATE_CODE);
		// if (token.getCaptcha() == null ||
		// !token.getCaptcha().toUpperCase().equals(code)){
		// throw new AuthenticationException("msg:验证码错误, 请重试.");
		// }
		// }
		// 校验用户名密码
		UserModel condition = new UserModel();
		condition.setLoginName(token.getUsername());
		condition.setCanLogin(true);
		UserModel userModel = getUserService().get(condition);
		if (userModel != null) {
			if (!userModel.getCanLogin()) {
				throw new AuthenticationException("msg:该已帐号禁止登录.");
			}
			// 保存上次登录信息
			userModel.setLastLoginIp(StringUtils.getRemoteAddr(
					((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()));
			userModel.setLastLoginDate(new Date());
			getUserService().update(userModel);
			return new SimpleAuthenticationInfo(new Principal(userModel, token.isMobileLogin()), // 登陆主体
					userModel.getPassword(), // md5加密后密码
					getName());
		} else {
			throw new AuthenticationException("msg:用户不存在或者密码错误.");
		}
	}

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Principal principal = (Principal) getAvailablePrincipal(principals);
		UserModel condition = new UserModel();
		condition.setLoginName(principal.getLoginName());
		UserModel user = getUserService().get(condition);
		if (user != null) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			// List<Menu> list = UserUtils.getMenuList();
			// for (Menu menu : list){
			// if (StringUtils.isNotBlank(menu.getPermission())){
			// // 添加基于Permission的权限信息
			// for (String permission :
			// StringUtils.split(menu.getPermission(),",")){
			// info.addStringPermission(permission);
			// }
			// }
			// }
			// 添加用户权限
			info.addStringPermission("user");
			// 添加用户角色信息
			// for (Role role : user.getRoleList()){
			// info.addRole(role.getEnname());
			// }
			// 更新登录IP和时间
			getUserService().update(user);
			// 记录登录日志
			// LogUtils.saveLog(Servlets.getRequest(), "系统登录");
			return info;
		} else {
			return null;
		}
	}

	@Override
	protected void checkPermission(Permission permission, AuthorizationInfo info) {
		authorizationValidate(permission);
		super.checkPermission(permission, info);
	}

	@Override
	protected boolean[] isPermitted(List<Permission> permissions, AuthorizationInfo info) {
		if (permissions != null && !permissions.isEmpty()) {
			for (Permission permission : permissions) {
				authorizationValidate(permission);
			}
		}
		return super.isPermitted(permissions, info);
	}

	@Override
	public boolean isPermitted(PrincipalCollection principals, Permission permission) {
		authorizationValidate(permission);
		return super.isPermitted(principals, permission);
	}

	@Override
	protected boolean isPermittedAll(Collection<Permission> permissions, AuthorizationInfo info) {
		if (permissions != null && !permissions.isEmpty()) {
			for (Permission permission : permissions) {
				authorizationValidate(permission);
			}
		}
		return super.isPermittedAll(permissions, info);
	}

	/**
	 * 授权验证方法
	 * 
	 * @param permission
	 */
	private void authorizationValidate(Permission permission) {
		// 模块授权预留接口
	}

	/**
	 * 设定密码校验的Hash算法与迭代次数
	 */
	@PostConstruct
	public void initCredentialsMatcher() {
		// HashedCredentialsMatcher matcher = new
		// HashedCredentialsMatcher(SystemService.HASH_ALGORITHM);
		// matcher.setHashIterations(SystemService.HASH_INTERATIONS);
		setCredentialsMatcher(new MD5CredentialsMatcher());
	}

	public UserService getUserService() {
		if (userService == null) {
			userService = SpringContext.getInstance().getBeanWithClass(UserService.class);
		}
		return userService;
	}

}
