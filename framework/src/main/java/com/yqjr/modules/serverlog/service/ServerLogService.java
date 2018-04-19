package com.yqjr.modules.serverlog.service;

import com.yqjr.framework.base.BaseService;
import com.yqjr.modules.serverlog.condition.ServerLogCondition;
import com.yqjr.modules.serverlog.dao.ServerLogDao;
import com.yqjr.modules.serverlog.entity.ServerLog;
import com.yqjr.modules.serverlog.model.ServerLogModel;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class ServerLogService extends BaseService<String, ServerLogDao, ServerLog, ServerLogCondition, ServerLogModel> {

	/**
	 * Description: 流水号重复校验 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年6月16日 上午10:04:48
	 *
	 * @param serialNo
	 * @return
	 */
	public boolean checkExists(String serialNo) {
		Assert.hasText(serialNo);
		if (dao.checkExists(serialNo) != null)
			return true;
		return false;
	}

}