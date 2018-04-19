package com.yqjr.modules.clientlog.dao;

import com.yqjr.framework.annotation.FrameworkDao;
import com.yqjr.framework.base.BaseDao;
import com.yqjr.modules.clientlog.condition.ClientLogCondition;
import com.yqjr.modules.clientlog.entity.ClientLog;

@FrameworkDao
public interface ClientLogDao extends BaseDao<String, ClientLog, ClientLogCondition> {
}