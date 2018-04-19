package com.yqjr.modules.user.entity;

import com.yqjr.framework.base.BaseEntity;
import com.yqjr.framework.datatype.Amount;
import com.yqjr.framework.datatype.Date;
import com.yqjr.modules.user.entity.User;

public class User extends BaseEntity<Long, User> {

	private static final long serialVersionUID = -5657109292206468702L;

	//private Amount userNo;

	private String loginName;

	private String password;

	private String userName;

	private String email;

	private String phone;

	private Boolean canLogin;

	private String lastLoginIp;

	private Date lastLoginDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getLastLoginIp() {
		return lastLoginIp;
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
}