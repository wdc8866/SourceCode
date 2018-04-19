/**
 * 
 */
package com.yqjr.framework.component.log.interactive;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.yqjr.framework.base.BaseModel;
import com.yqjr.framework.component.log.InteractiveLogger;
import com.yqjr.framework.datatype.BizzException;
import com.yqjr.modules.clientlog.entity.ClientLog;
import com.yqjr.modules.clientlog.model.ClientLogModel;
import com.yqjr.modules.clientlog.service.ClientLogService;

/**
 * ClassName: ClientInteractiveLogger <br>
 * Description: 客户端交互式日志记录组件 <br>
 * Create By: admin <br>
 * Create Date: 2017年6月7日 上午8:14:48 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
@Service
public class ClientInteractiveLogger extends InteractiveLogger {

	@Autowired
	private ClientLogService clientLogService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yqjr.framework.component.log.InteractiveLogger#saveLog(com.yqjr.
	 * framework.base.BaseEntity)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveLog(BaseModel<String, ?> logModel) {
		Assert.notNull(logModel);
		Assert.isInstanceOf(ClientLogModel.class, logModel, "参数类型不符");
		clientLogService.save((ClientLogModel) logModel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yqjr.framework.component.log.InteractiveLogger#performanceSaveLog(com
	 * .yqjr.framework.base.BaseEntity)
	 */
	@Override
	public void performanceSaveLog(BaseModel<String, ?> logModel) {
		Assert.notNull(logModel);
		Assert.isInstanceOf(ClientLogModel.class, logModel, "参数类型不符");
		bufferQueue.add((ClientLogModel) logModel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yqjr.framework.component.log.InteractiveLogger#updateLog(com.yqjr.
	 * framework.base.BaseEntity)
	 */
	@Override
	public void updateLog(BaseModel<String, ?> logModel) {
		throw new BizzException("暂不支持");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yqjr.framework.component.log.InteractiveLogger#batchProcess(java.util
	 * .List)
	 */
	@Override
	public void batchProcess(List<BaseModel<String, ?>> logs) {
		List<ClientLog> entities = new ArrayList<ClientLog>();
		for (BaseModel<String, ?> model : logs) {
			ClientLog entity = clientLogService.toEntity((ClientLogModel) model, ClientLog.class);
			entities.add(entity);
		}
		clientLogService.batchSave(entities);
	}

}
