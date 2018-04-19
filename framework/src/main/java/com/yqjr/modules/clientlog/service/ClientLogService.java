package com.yqjr.modules.clientlog.service;

import com.yqjr.framework.base.BaseService;
import com.yqjr.modules.clientlog.condition.ClientLogCondition;
import com.yqjr.modules.clientlog.dao.ClientLogDao;
import com.yqjr.modules.clientlog.entity.ClientLog;
import com.yqjr.modules.clientlog.model.ClientLogModel;

import org.springframework.stereotype.Service;

@Service
public class ClientLogService extends BaseService<String, ClientLogDao, ClientLog, ClientLogCondition, ClientLogModel> {

}