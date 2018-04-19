package com.yqjr.modules.serverlog.dao;

import com.yqjr.framework.annotation.FrameworkDao;
import com.yqjr.framework.base.BaseDao;
import com.yqjr.modules.serverlog.condition.ServerLogCondition;
import com.yqjr.modules.serverlog.entity.ServerLog;

@FrameworkDao
public interface ServerLogDao extends BaseDao<String, ServerLog, ServerLogCondition> {

	/**
	 * Description: 流水号重复校验 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年6月16日 上午10:04:48
	 *
	 * @param serialNo
	 * @return
	 */
	public String checkExists(String serialNo);

}