/**
 * 
 */
package com.yqjr.framework.component.advisor;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yqjr.framework.base.BaseModel;
import com.yqjr.framework.component.context.SpringContext;
import com.yqjr.framework.component.context.ThreadContext;
import com.yqjr.framework.datatype.BizzException;
import com.yqjr.framework.datatype.Date;

/**
 * ClassName: TransactionCheckAdvisor <br>
 * Description: 事务校验 <br>
 * Create By: admin <br>
 * Create Date: 2017年12月28日 上午11:05:02 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
@Aspect
@Component
public class TransactionCheckAdvisor {

	public static final String TRANSACTION_INVOKER = "framework.transaction.invoker";

	/**
	 * Description: 用于事务控制处理 <br>
	 * 考虑到事务传播,引入调用链概念,即A->B->C会产生服务调用链 <br>
	 * 调用链中的每个节点属性见InvokerInfo <br>
	 * Create By: admin <br>
	 * Create Date: 2017年4月20日 上午8:54:03
	 *
	 * @param pjp
	 * @param transactional
	 * @return
	 * @throws Throwable
	 */
	@SuppressWarnings("rawtypes")
	@Around(value = "@annotation(transactional)")
	public Object transaction(ProceedingJoinPoint pjp, Transactional transactional) throws Throwable {
		// 检查方法参数
		Object object = pjp.getArgs()[0];
		// 进行事务控制条件
		// 1-service方法第一个参数为BaseModel实例,且实例返回的业务主键不为空
		// 2-事务传播机制不为never或readonly
		if (object != null && object instanceof BaseModel
				&& StringUtils.isNotBlank(((BaseModel) object).getTransactionalKey())
				&& Propagation.NEVER != transactional.propagation() && !transactional.readOnly()) {
			return controlInvoke(pjp, transactional, ((BaseModel) object).getTransactionalKey());
		}
		// 否则正常执行
		else {
			return pjp.proceed();
		}
	}

	/**
	 * Description: 控制调用,执行此方法时会检查业务主键对应的分布式调用是否出现异常,如发现执行过程中存在异常将会对业务主键进行锁定
	 * <br>
	 * Create By: admin <br>
	 * Create Date: 2017年7月12日 上午8:12:06
	 *
	 * @param pjp
	 * @param transactional
	 * @return
	 * @throws Throwable
	 */
	private Object controlInvoke(ProceedingJoinPoint pjp, Transactional transactional, String transactionalKey)
			throws Throwable {
		String className = pjp.getSignature().getDeclaringTypeName();
		String methodName = pjp.getSignature().getName();
		// 事务状态检查
		AdvisorSupport advisorSupport = SpringContext.getInstance().getBeanWithName("advisorSupport");
		TransactionInvoker transactionInvoker = new TransactionInvoker();
		transactionInvoker.setClassName(className);
		transactionInvoker.setMethodName(methodName);
		transactionInvoker.setTransactionalKey(transactionalKey);
		if (!advisorSupport.checkTransactionStatus(transactionInvoker)) {
			throw new BizzException("存在异常事务,请及时处理:" + transactionalKey);
		}
		ThreadContext context = ThreadContext.getInstance();
		transactionInvoker = context.getObject(TRANSACTION_INVOKER);
		if (transactionInvoker == null) {
			transactionInvoker = new TransactionInvoker();
			transactionInvoker.setClassName(className);
			transactionInvoker.setMethodName(methodName);
			transactionInvoker.setTransactionalKey(transactionalKey);
			transactionInvoker.setTransactionTime(new Date());
			context.put(TRANSACTION_INVOKER, transactionInvoker);
		}
		transactionInvoker.setPropagation(transactional.propagation(), className, methodName);
		Object res = null;
		try {
			res = pjp.proceed();
		} catch (Throwable e) {
			// 判断当前方法是否为主事务入口,如果是的话登记事务信息
			if (transactionInvoker.isMainTransaction(className, methodName)) {
				saveTransactionKey(transactionInvoker);
			}
			throw e;
		} finally {
			transactionInvoker.continueIfSuspend(className, methodName);
		}
		return res;
	}

	/**
	 * Description: 登记业务主键信息 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年7月12日 下午1:36:44
	 *
	 * @param transactionInvoker
	 */
	private void saveTransactionKey(TransactionInvoker transactionInvoker) {
		AdvisorSupport advisorSupport = SpringContext.getInstance().getBeanWithName("advisorSupport");
		advisorSupport.saveTransactionKey(transactionInvoker);
	}

}
