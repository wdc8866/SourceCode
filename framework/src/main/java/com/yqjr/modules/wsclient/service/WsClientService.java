package com.yqjr.modules.wsclient.service;

import com.yqjr.framework.base.BaseService;
import com.yqjr.modules.wsclient.condition.WsClientCondition;
import com.yqjr.modules.wsclient.dao.WsClientDao;
import com.yqjr.modules.wsclient.entity.WsClient;
import com.yqjr.modules.wsclient.model.WsClientModel;

import org.springframework.stereotype.Service;

@Service
public class WsClientService extends BaseService<Integer, WsClientDao, WsClient, WsClientCondition, WsClientModel> {
}