package com.yqjr.modules.user.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

import com.yqjr.framework.base.BaseModel;
import com.yqjr.framework.datatype.Amount;
import com.yqjr.framework.datatype.Date;
import com.yqjr.modules.role.model.RoleModel;

public class UserModel extends BaseModel<Long, UserModel> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2735414692797221434L;

	//private Amount userNo;

	private String loginName;

	private String oldLoginName;

	private String password;

	private String newPassword;

	private String confirmNewPassword;

	private String userName;

	@NotEmpty(message = "邮箱信息不能为空")
	private String email;

	private String phone;

	private Boolean canLogin;

	private String lastLoginIp;

	private Date lastLoginDate;

	private String roleIds;

	private List<RoleModel> roleList = new ArrayList<RoleModel>();

	@NotEmpty(message = "角色信息不能为空")
	private List<String> roleIdList = new ArrayList<String>();

	public List<RoleModel> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<RoleModel> roleList) {
		this.roleList = roleList;
	}

	public String getConfirmNewPassword() {
		return confirmNewPassword;
	}

	public void setConfirmNewPassword(String confirmNewPassword) {
		this.confirmNewPassword = confirmNewPassword;
	}

	public String getOldLoginName() {
		return oldLoginName;
	}

	public void setOldLoginName(String oldLoginName) {
		this.oldLoginName = oldLoginName;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	/**
	 * @return the canLogin
	 */
	public Boolean getCanLogin() {
		return canLogin;
	}

	/**
	 * @param canLogin
	 *            the canLogin to set
	 */
	public void setCanLogin(Boolean canLogin) {
		this.canLogin = canLogin;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}

	public List<String> getRoleIdList() {
		return roleIdList;
	}

	public void setRoleIdList(List<String> roleIdList) {
		this.roleIdList = roleIdList;
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