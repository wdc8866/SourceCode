package com.yqjr.framework.component.shiro;

import java.io.Serializable;

import com.yqjr.modules.user.model.UserModel;

public class Principal implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id; // 编号
	private String loginName; // 登录名
	private String name; // 姓名
	private boolean mobileLogin; // 是否手机登录

	public Principal(UserModel user, boolean mobileLogin) {
		this.id = user.getId();
		this.loginName = user.getLoginName();
		this.name = user.getUserName();
		this.mobileLogin = mobileLogin;
	}

	public Long getId() {
		return id;
	}

	public String getLoginName() {
		return loginName;
	}

	public String getName() {
		return name;
	}

	public boolean isMobileLogin() {
		return mobileLogin;
	}

	@Override
	public String toString() {
		return id + "|" + loginName + "|" + name;
	}

}
