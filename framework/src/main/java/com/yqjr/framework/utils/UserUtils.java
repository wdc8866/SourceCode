package com.yqjr.framework.utils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.yqjr.framework.component.context.SpringContext;
import com.yqjr.framework.component.shiro.Principal;
import com.yqjr.modules.role.dao.RoleDao;
import com.yqjr.modules.role.entity.Role;
import com.yqjr.modules.role.service.RoleService;
import com.yqjr.modules.user.condition.UserCondition;
import com.yqjr.modules.user.dao.UserDao;
import com.yqjr.modules.user.entity.User;
import com.yqjr.modules.user.model.UserModel;
import com.yqjr.modules.user.service.UserService;


/**
 * 
 * ClassName: UserUtils <br>
 * Description: 当前登录User操作类 <br>
 * Create By: Wanglei <br>
 * Create Date: 2017年5月5日 上午10:51:20 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 *
 */
public class UserUtils {

	private static SpringContext springContext = SpringContext.getInstance();
	private static UserDao userDao = springContext.getBeanWithClass(UserDao.class);
	private static UserService userService = springContext.getBeanWithClass(UserService.class);
	private static RoleDao roleDao = springContext.getBeanWithClass(RoleDao.class);
	private static RoleService roleService = springContext.getBeanWithClass(RoleService.class);
	
	/**
	 * 
	 * Description: 根据ID获取用户 <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年5月5日 上午10:54:32
	 *
	 * @param id
	 * @return
	 */
	public static UserModel get(Long id){
		UserModel user = userService.id(id);
		if (user == null){
			return null;
		}
		user.setRoleList(roleService.findListByUser(user));
		return user;
	}
	
	/**
	 * 根据登录名获取用户
	 * @param loginName
	 * @return 取不到返回null
	 */
	public static UserModel getByLoginName(String loginName){
		UserModel condition = new UserModel();
		condition.setLoginName(loginName);
		UserModel user = userService.get(condition);
		if (user == null){
			return null;
		}
		user.setRoleList(roleService.findListByUser(user));
		return user;
	}
	
	/**
	 * 清除当前用户缓存
	 */
//	public static void clearCache(){
//		removeCache(CACHE_ROLE_LIST);
//		removeCache(CACHE_MENU_LIST);
//		removeCache(CACHE_AREA_LIST);
//		removeCache(CACHE_OFFICE_LIST);
//		removeCache(CACHE_OFFICE_ALL_LIST);
//		UserUtils.clearCache(getUser());
//	}
	
	/**
	 * 清除指定用户缓存
	 * @param user
	 */
//	public static void clearCache(User user){
//		CacheUtils.remove(USER_CACHE, USER_CACHE_ID_ + user.getId());
//		CacheUtils.remove(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName());
//		CacheUtils.remove(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getOldLoginName());
//	}
	
	/**
	 * 获取当前用户
	 * @return 取不到返回 new User()
	 */
	public static UserModel getUser(){
		Principal principal = getPrincipal();
		UserModel virtualUser = new UserModel();
		virtualUser.setId(-1L);
		virtualUser.setLoginName("virtualUser");
		virtualUser.setUserName("虚拟用户");
		if (principal!=null){
			UserModel user = get(principal.getId());
			if (user != null){
				return user;
			}
			return new UserModel();
		}
		return virtualUser;
	}

	/**
	 * 获取当前用户角色列表
	 * @return
	 */
//	public static List<Role> getRoleList(){
//		@SuppressWarnings("unchecked")
//		List<Role> roleList = (List<Role>)getCache(CACHE_ROLE_LIST);
//		if (roleList == null){
//			User user = getUser();
//			if (user.isAdmin()){
//				roleList = roleDao.findAllList(new Role());
//			}else{
//				Role role = new Role();
//				role.getSqlMap().put("dsf", BaseService.dataScopeFilter(user.getCurrentUser(), "o", "u"));
//				roleList = roleDao.findList(role);
//			}
//			putCache(CACHE_ROLE_LIST, roleList);
//		}
//		return roleList;
//	}
	
	/**
	 * 获取当前用户授权菜单
	 * @return
	 */
//	public static List<Menu> getMenuList(){
//		@SuppressWarnings("unchecked")
//		List<Menu> menuList = (List<Menu>)getCache(CACHE_MENU_LIST);
//		//if (menuList == null){
//			User user = getUser();
//			/*if (user.isAdmin()){
//				menuList = menuDao.findAllList(new Menu());
//			}else{*/
//				Menu m = new Menu();
//				m.setUserId(user.getId());
//				menuList = menuDao.findByUserId(m);
//			//}
//			putCache(CACHE_MENU_LIST, menuList);
//		//}
//		return menuList;
//	}
	
//	public static List<Menu> getMenuListBySystemId(String systemId){
//		List<Menu> menuList = (List<Menu>)getCache(CACHE_MENU_LIST);
//		User user = getUser();
//		Menu m = new Menu();
//		m.setUserId(user.getId());
//		com.yqjr.base.modules.sys.entity.System s = new com.yqjr.base.modules.sys.entity.System();
//		s.setId(systemId);
//		m.setSystem(s);
//		menuList = menuDao.findByUserId(m);
////		List<Menu> sourcelist = Lists.newArrayList();
////		Menu.sortList(sourcelist, menuList, Menu.getRootId(), true);
////		for(Menu mm : sourcelist){
////			if(mm.getIsShow().equals("1")){
////				System.out.println(mm.getName() + "  " + mm.getUrl()+ "  " + mm.getSystem().getId());
////			}
////			
////		}
//		return menuList;
//	}
	
	/**
	 * 获取当前用户常用菜单
	 * @return
	 */
//	public static List<Menu> getMyMenuList(){
//		@SuppressWarnings("unchecked")
//		List<Menu> menuList = (List<Menu>)getCache(CACHE_MY_MENU_LIST);
//		//if (menuList == null){
//		User user = getUser();
//		menuList = menuDao.findMyMenuByUserId(user);
//		List<Menu> delList = Lists.newArrayList();
//		for(Menu delMenu : menuList){
//			if(StringUtils.isBlank(delMenu.getUrl())){
//				delList.add(delMenu);
//			}
//		}
//		menuList.removeAll(delList);
//		putCache(CACHE_MY_MENU_LIST, menuList);
//		//}
//		return menuList;
//	}
	
	/**
	 * 获取授权主要对象
	 */
	public static Subject getSubject(){
		return SecurityUtils.getSubject();
	}
	
	/**
	 * 获取当前登录者对象
	 */
	public static Principal getPrincipal(){
		try{
			Subject subject = SecurityUtils.getSubject();
			Principal p=null;
			Object o=subject.getPrincipal();
			if  (o!=null && o instanceof Principal) {
				 p = (Principal)o;
			} 
//			else if (o!=null && o instanceof String){
//				Map<String,Object> roleType = Maps.newHashMap();
//				roleType.put("roleList", Role.class);
//				roleType.put("menuList", Menu.class);
//				User u = (User) JSONUtil.getDTO((String) o, User.class,roleType);
//				CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + u.getId(), u);
//				CacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + u.getLoginName(), u);
//				p=new Principal(u,false);
//			}
			
			if (p != null){
				return p;
			}
//			subject.logout();
		}catch (UnavailableSecurityManagerException e) {
			
		}catch (InvalidSessionException e){
			
		}
		return null;
	}
	
	public static Session getSession(){
		try{
			Subject subject = SecurityUtils.getSubject();
			Session session = subject.getSession(false);
			if (session == null){
				session = subject.getSession();
			}
			if (session != null){
				return session;
			}
//			subject.logout();
		}catch (InvalidSessionException e){
			
		}
		return null;
	}
	
	// ============== User Cache ==============
	
	public static Object getCache(String key) {
		return getCache(key, null);
	}
	
	public static Object getCache(String key, Object defaultValue) {
//		Object obj = getCacheMap().get(key);
		Object obj = getSession().getAttribute(key);
		return obj==null?defaultValue:obj;
	}

	public static void putCache(String key, Object value) {
//		getCacheMap().put(key, value);
		getSession().setAttribute(key, value);
	}

	public static void removeCache(String key) {
//		getCacheMap().remove(key);
		getSession().removeAttribute(key);
	}
	
//	public static Map<String, Object> getCacheMap(){
//		Principal principal = getPrincipal();
//		if(principal!=null){
//			return principal.getCacheMap();
//		}
//		return new HashMap<String, Object>();
//	}
	
}
