package com.yqjr.framework.component.advisor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;

import com.yqjr.framework.datatype.Date;

/**
 * ClassName: InvokerInfo <br>
 * Description: 事务调用信息 <br>
 * Create By: admin <br>
 * Create Date: 2017年1月14日 下午5:26:19 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public class TransactionInvoker {

	/**
	 * 数据ID
	 */
	private Integer Id;

	/**
	 * 类名称
	 */
	private String className;

	/**
	 * 方法名称
	 */
	private String methodName;

	/**
	 * 标识本次调用的业务主键信息
	 */
	private String transactionalKey;

	/**
	 * 调用时间
	 */
	private Date transactionTime;

	/**
	 * 引起主事务中断的key值,采用className+methodName组成
	 */
	private String suspendInvokerKey;

	/**
	 * 当前事务状态
	 */
	private TransactionStatus tranactionStatus;

	/**
	 * 业务锁定状态 <br>
	 * 0-未锁定 <br>
	 * 1-锁定中 <br>
	 */
	private Integer lock = 0;

	/**
	 * 本次服务调用涉及的分布式调用明细信息
	 */
	private List<RemoteInvoker> remoteInvokers = new ArrayList<RemoteInvoker>();

	/**
	 * @return the id
	 */
	public Integer getId() {
		return Id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		Id = id;
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className
	 *            the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @return the methodName
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * @param methodName
	 *            the methodName to set
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	/**
	 * @return the transactionalKey
	 */
	public String getTransactionalKey() {
		return transactionalKey;
	}

	/**
	 * @param transactionalKey
	 *            the transactionalKey to set
	 */
	public void setTransactionalKey(String transactionalKey) {
		this.transactionalKey = transactionalKey;
	}

	/**
	 * Description: 根据事务传播属性判断主事务状态 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年7月12日 上午11:37:16
	 *
	 * @param propagation
	 * @param className
	 * @param methodName
	 */
	public void setPropagation(Propagation propagation, String className, String methodName) {

		// 判断当前事务状态是否为中断,如果中断则不会改变主事务状态
		if (tranactionStatus == TransactionStatus.SUSPEND)
			return;

		// 如果不为中断状态则需要根据事务传播属性进行判断
		switch (propagation) {
		case REQUIRED:
			tranactionStatus = TransactionStatus.RUNNING;
			break;
		case REQUIRES_NEW:
			// 如果当前事务状态为空则会新开启事务,否则主事务状态将中断
			if (tranactionStatus == null)
				tranactionStatus = TransactionStatus.RUNNING;
			else {
				tranactionStatus = TransactionStatus.SUSPEND;
				suspendInvokerKey = className + methodName;
			}
			break;
		case SUPPORTS:// 状态保持不处理
			break;
		case MANDATORY:// 状态保持不处理
			break;
		case NEVER:// 状态保持不处理
			break;
		case NOT_SUPPORTED:
			// 更改主事务状态为中断
			tranactionStatus = TransactionStatus.SUSPEND;
			suspendInvokerKey = className + methodName;
			break;
		case NESTED:// 嵌套事务内的分布式调用理论上也需要回滚,所以此处仍要保持主事务状态不中断
			break;
		default:
			throw new RuntimeException("unsupport propagation," + propagation);
		}

	}

	/**
	 * @return the transactionTime
	 */
	public Date getTransactionTime() {
		return transactionTime;
	}

	/**
	 * @param transactionTime
	 *            the transactionTime to set
	 */
	public void setTransactionTime(Date transactionTime) {
		this.transactionTime = transactionTime;
	}

	/**
	 * @return the suspendInvokerKey
	 */
	public String getSuspendInvokerKey() {
		return suspendInvokerKey;
	}

	/**
	 * @return the tranactionStatus
	 */
	public TransactionStatus getTranactionStatus() {
		return tranactionStatus;
	}

	/**
	 * @return the lock
	 */
	public Integer getLock() {
		return lock;
	}

	/**
	 * @param lock
	 *            the lock to set
	 */
	public void setLock(Integer lock) {
		this.lock = lock;
	}

	/**
	 * @return the remoteInvokers
	 */
	public List<RemoteInvoker> getRemoteInvokers() {
		return remoteInvokers;
	}

	/**
	 * @param remoteInvokers
	 *            the remoteInvokers to set
	 */
	public void setRemoteInvokers(List<RemoteInvoker> remoteInvokers) {
		this.remoteInvokers = remoteInvokers;
	}

	/**
	 * Description: 向队列中添加调用明细信息 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年7月12日 上午10:06:54
	 *
	 * @param remoteInvoker
	 */
	public void add(RemoteInvoker remoteInvoker) {
		this.remoteInvokers.add(remoteInvoker);
	}

	/**
	 * Description: 判断当前执行方式是否为主事务方法 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年7月12日 上午11:00:08
	 *
	 * @return
	 */
	public boolean isMainTransaction(String className, String methodName) {
		return this.className.equals(className) && this.methodName.equals(methodName);
	}

	/**
	 * Description: 恢复中断主事务 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年7月12日 上午11:53:46
	 *
	 * @param className
	 * @param methodName
	 */
	public void continueIfSuspend(String className, String methodName) {
		String key = className + methodName;
		if (key.equals(this.suspendInvokerKey)) {
			tranactionStatus = TransactionStatus.RUNNING;
			this.suspendInvokerKey = null;
		}
	}

	enum TransactionStatus {
		RUNNING, // 正在运行
		SUSPEND;// 中断
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TransactionInvoker [className=" + className + ", methodName=" + methodName + ", transactionalKey="
				+ transactionalKey + ", transactionTime=" + transactionTime + "]";
	}

}
