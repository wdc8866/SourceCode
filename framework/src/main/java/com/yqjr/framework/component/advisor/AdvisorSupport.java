/**
 * 
 */
package com.yqjr.framework.component.advisor;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yqjr.framework.component.log.Logger;

/**
 * ClassName: AdvisorSupport <br>
 * Description: 切面服务数据库访问支持 <br>
 * Create By: admin <br>
 * Create Date: 2017年7月10日 下午3:37:37 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
@Service
public class AdvisorSupport {

	private static Logger logger = Logger.getLogger();

	@Autowired
	private AdvisorSupportDao advisorSupportDao;

	/**
	 * Description: 登记业务主键 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年7月10日 下午3:41:30
	 *
	 * @param transactionalKey
	 * @param list
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveTransactionKey(TransactionInvoker transactionInvoker) {
		List<RemoteInvoker> list = transactionInvoker.getRemoteInvokers();
		// 如果整个事务中出现分布式事务,则需要插入
		if (CollectionUtils.isNotEmpty(list)) {
			logger.info("业务代码执行异常,登记事务登记表,业务代码信息:" + transactionInvoker);
			// 判断交易信息中是否包含分布式写事务
			for(RemoteInvoker remoteInvoker : list){
				if("W".equals(remoteInvoker.getTransType())){
					transactionInvoker.setLock(1);
					break;
				}
			}
			advisorSupportDao.saveTransactionInvoker(transactionInvoker);
			advisorSupportDao.saveRemoteInokers(transactionInvoker.getRemoteInvokers());
		}
	}

	/**
	 * Description: 业务主键检查 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年7月10日 下午3:42:27
	 *
	 * @return
	 */
	public boolean checkTransactionStatus(TransactionInvoker transactionInvoker) {
		long res = advisorSupportDao.getTransactionInvoker(transactionInvoker);
		return res == 0;
	}

}
