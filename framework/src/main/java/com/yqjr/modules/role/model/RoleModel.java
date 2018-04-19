package com.yqjr.modules.role.model;

import java.util.ArrayList;
import java.util.List;

import com.yqjr.framework.base.BaseModel;
import com.yqjr.modules.menu.entity.Menu;

public class RoleModel extends BaseModel<Long, RoleModel> {

	private static final long serialVersionUID = -6878551729916147387L;

	private String name;

	private String oldName;

	private String enName;

	private String roleType;

	private String dataScope;

	private String isSys;

	private String useable;

	private List<Menu> menuList = new ArrayList<Menu>();

	private String menuIds;// 菜单树Id集合 ‘，’分割

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public String getDataScope() {
		return dataScope;
	}

	public void setDataScope(String dataScope) {
		this.dataScope = dataScope;
	}

	public String getIsSys() {
		return isSys;
	}

	public void setIsSys(String isSys) {
		this.isSys = isSys;
	}

	public String getUseable() {
		return useable;
	}

	public void setUseable(String useable) {
		this.useable = useable;
	}

	public List<Menu> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<Menu> menuList) {
		this.menuList = menuList;
	}

	public String getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}

	/* (non-Javadoc)
	 * @see com.yqjr.framework.base.BaseModel#getTransactionalKey()
	 */
	@Override
	public String getTransactionalKey() {
		// TODO Auto-generated method stub
		return null;
	}

}