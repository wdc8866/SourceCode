/**
 * 
 */
package com.yqjr.framework.component.advisor;

import java.util.List;

import com.yqjr.framework.annotation.FrameworkDao;

/**
 * ClassName: AdvisorSupportDao <br>
 * Description: 切面服务数据库访问支持 <br>
 * Create By: admin <br>
 * Create Date: 2017年7月10日 下午3:43:09 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
@FrameworkDao
public interface AdvisorSupportDao {

	public void saveTransactionInvoker(TransactionInvoker transactionInvoker);

	public void saveRemoteInokers(List<RemoteInvoker> list);

	public long getTransactionInvoker(TransactionInvoker transactionInvoker);

}
