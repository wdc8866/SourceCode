package com.yqjr.modules.wsclient.dao;

import com.yqjr.framework.annotation.FrameworkDao;
import com.yqjr.framework.base.BaseDao;
import com.yqjr.modules.wsclient.condition.WsClientCondition;
import com.yqjr.modules.wsclient.entity.WsClient;

@FrameworkDao
public interface WsClientDao extends BaseDao<Integer, WsClient, WsClientCondition> {
}