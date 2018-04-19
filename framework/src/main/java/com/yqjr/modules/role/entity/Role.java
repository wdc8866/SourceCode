package com.yqjr.modules.role.entity;

import com.yqjr.framework.base.BaseEntity;
import com.yqjr.modules.role.entity.Role;

public class Role extends BaseEntity<Long, Role> {

	private static final long serialVersionUID = -1463409347435644685L;

	private String name;

	private String enName;

	private String roleType;

	private String dataScope;

	private String isSys;

	private String useable;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
}