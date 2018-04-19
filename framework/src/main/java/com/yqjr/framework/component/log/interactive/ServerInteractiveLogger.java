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
import com.yqjr.framework.datatype.ExceptionCode;
import com.yqjr.modules.serverlog.entity.ServerLog;
import com.yqjr.modules.serverlog.model.ServerLogModel;
import com.yqjr.modules.serverlog.service.ServerLogService;

/**
 * ClassName: ServerInteractiveLogger <br>
 * Description: 服务端交互式日志记录组件 <br>
 * Create By: admin <br>
 * Create Date: 2017年6月7日 上午8:15:05 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
@Service
public class ServerInteractiveLogger extends InteractiveLogger {

	@Autowired
	private ServerLogService serverLogService;

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
		Assert.isInstanceOf(ServerLogModel.class, logModel, "参数类型不符");
		ServerLogModel serverLogModel = (ServerLogModel) logModel;
		if (serverLogService.checkExists(serverLogModel.getSerialNo())) {
			throw new BizzException(ExceptionCode.WS_1001);
		}
		serverLogService.save(serverLogModel);
	}

	/**
	 * Description: 更新日期记录状态 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年6月15日 下午3:51:43
	 *
	 * @param logEntity
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateLog(BaseModel<String, ?> logModel) {
		Assert.notNull(logModel);
		Assert.isInstanceOf(ServerLogModel.class, logModel, "参数类型不符");
		serverLogService.update((ServerLogModel) logModel);
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
		Assert.isInstanceOf(ServerLogModel.class, logModel, "参数类型不符");
		bufferQueue.add((ServerLogModel) logModel);
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
		List<ServerLog> entities = new ArrayList<ServerLog>();
		for (BaseModel<String, ?> model : logs) {
			ServerLog entity = serverLogService.toEntity((ServerLogModel) model, ServerLog.class);
			entities.add(entity);
		}
		serverLogService.batchUpdate(entities);
	}

}
