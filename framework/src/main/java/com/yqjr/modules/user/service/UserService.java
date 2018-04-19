package com.yqjr.modules.user.service;

import com.yqjr.framework.base.BaseService;
import com.yqjr.framework.component.esb.IReplyParser;
import com.yqjr.framework.component.esb.Reply;
import com.yqjr.framework.component.esb.WebServiceClient;
import com.yqjr.framework.datatype.BizzException;
import com.yqjr.framework.utils.MD5Utils;
import com.yqjr.framework.utils.StringUtils;
import com.yqjr.framework.utils.UserUtils;
import com.yqjr.modules.role.model.RoleModel;
import com.yqjr.modules.role.service.RoleService;
import com.yqjr.modules.user.condition.UserCondition;
import com.yqjr.modules.user.dao.UserDao;
import com.yqjr.modules.user.entity.User;
import com.yqjr.modules.user.model.TransModel;
import com.yqjr.modules.user.model.UserModel;

import framework.wsclient.dto.BankNoInquiry;
import framework.wsclient.dto.BankNoInquiryRequestDTO;
import framework.wsclient.dto.PacpHeader;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService extends BaseService<Long, UserDao, User, UserCondition, UserModel> {

	@Autowired
	private RoleService roleService;

	/**
	 * Description: 获取用户及角色信息 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年6月27日 下午4:16:39
	 *
	 * @return
	 */
	public UserModel getUserModel(UserModel userModel) {
		if (userModel.getId() != null) {
			userModel = id(userModel.getId());
			userModel.setOldLoginName(userModel.getLoginName());
			List<RoleModel> roleList = roleService.findListByUser(userModel);
			userModel.setRoleList(roleList);
			List<String> roleIdList = new ArrayList<String>();
			for (RoleModel role : roleList) {
				roleIdList.add(String.valueOf(role.getId()));
			}
			userModel.setRoleIdList(roleIdList);
		}
		return userModel;
	}

	/**
	 * Description: 用户插入\更新业务处理 <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年6月7日 下午1:32:48
	 *
	 * @param user
	 */
	@Transactional
	public void saveUser(UserModel userModel) {
		// db opt
		// ws call dto userModel - > dto
		// db opt
		// ws call OSB-c000 replyCode !=0

		// 拼装页面角色CheckBox
		if (CollectionUtils.isNotEmpty(userModel.getRoleIdList())) {
			List<RoleModel> roleList = new ArrayList<RoleModel>();
			for (String roleId : userModel.getRoleIdList()) {
				RoleModel role = new RoleModel();
				role.setId(Long.valueOf(roleId));
				roleList.add(role);
			}
			userModel.setRoleList(roleList);
		} else {
			throw new BizzException("保存用户'" + userModel.getLoginName() + "'失败，未设置角色");
		}
		// 如果新密码为空，则不更换密码
		if (StringUtils.isNotBlank(userModel.getNewPassword())) {
			userModel.setPassword(MD5Utils.encode(userModel.getNewPassword()));
		}
		if (userModel.getId() == null
				&& !"true".equals(checkUnique(userModel.getOldLoginName(), userModel.getLoginName(), userModel))) {
			throw new BizzException("保存用户'" + userModel.getLoginName() + "'失败，登录名已存在");
		}
		// 新增用户
		if (userModel.getId() == null) {
			userModel.setId(super.save(userModel));
		}
		// 更新用户
		else {
			update(userModel);
		}
		// 维护用户角色关系
		User user = toEntity(userModel, User.class);
		if (userModel.getId() != null) {
			dao.deleteUserRole(user);
		}
		dao.insertUserRole(user, userModel.getRoleList());
		// 清除当前用户缓存
		if (userModel.getLoginName().equals(UserUtils.getUser().getLoginName())) {
			// UserUtils.clearCache();
		}
	}

	@Transactional
	public void test(TransModel transModel) {
		
		// 数据库事务
		UserModel user = new UserModel();
		user.setId(123l);
		save(user);
		
		// 调用服务 
		PacpHeader header = new PacpHeader();
		header.setTxnRefNo("1231231");
		header.setTxnDate("txndate");
		header.setChannel("FMK");
		BankNoInquiryRequestDTO dto = new BankNoInquiryRequestDTO();
		dto.setPayeeAcctNo("F21123123");
		dto.setPayeeBankName("BANK");
		dto.setPayeeName("S212312312");
		BankNoInquiry bankNoInquiry = WebServiceClient.getInstance().getWsClient("pacp.005", BankNoInquiry.class,new IReplyParser() {
			
			@Override
			public Reply parseReply(Object response) {
				String replyCode = "0";
				String replyText= "交易成功";
				Reply reply = new Reply(replyCode,replyText,Reply.Status.OK);
				return reply;
			}
		});
		bankNoInquiry.bankNoInquiryRequest(header, dto);
		
		// 数据库事务
		user = new UserModel();
		user.setId(124l);
		save(user);
		
		

	}

}