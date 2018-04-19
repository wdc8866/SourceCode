/**
 * 
 */
package com.yqjr.framework.component.advisor;

import com.yqjr.framework.datatype.Date;

/**
 * ClassName: RemoteInvoker <br>
 * Description: 远程调用信息 <br>
 * Create By: admin <br>
 * Create Date: 2017年7月12日 上午10:00:05 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public class RemoteInvoker {

	/**
	 * 数据ID
	 */
	private Integer Id;

	/**
	 * 事务调用者信息(主事务)
	 */
	private TransactionInvoker transactionInvoker;

	/**
	 * 远程调用交易码
	 */
	private String serviceCode;

	/**
	 * 远程调用服务名称
	 */
	private String serviceName;

	/**
	 * 调用流水号
	 */
	private String serialNo;

	/**
	 * 调用方式
	 */
	private String invokeType;

	/**
	 * 交易类型
	 */
	private String transType;

	/**
	 * 请求时间
	 */
	private Date requestTime;

	/**
	 * 应答时间
	 */
	private Date responseTime;

	/**
	 * 应答码
	 */
	private String replyCode;

	/**
	 * 应答信息
	 */
	private String replyText;

	/**
	 * 调用状态
	 */
	private Integer invokeStatus;

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
	 * @return the transactionInvoker
	 */
	public TransactionInvoker getTransactionInvoker() {
		return transactionInvoker;
	}

	/**
	 * @param transactionInvoker
	 *            the transactionInvoker to set
	 */
	public void setTransactionInvoker(TransactionInvoker transactionInvoker) {
		this.transactionInvoker = transactionInvoker;
	}

	/**
	 * @return the serviceCode
	 */
	public String getServiceCode() {
		return serviceCode;
	}

	/**
	 * @param serviceCode
	 *            the serviceCode to set
	 */
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * @param serviceName
	 *            the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * @return the serialNo
	 */
	public String getSerialNo() {
		return serialNo;
	}

	/**
	 * @param serialNo
	 *            the serialNo to set
	 */
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	/**
	 * @return the invokeType
	 */
	public String getInvokeType() {
		return invokeType;
	}

	/**
	 * @param invokeType
	 *            the invokeType to set
	 */
	public void setInvokeType(String invokeType) {
		this.invokeType = invokeType;
	}

	/**
	 * @return the transType
	 */
	public String getTransType() {
		return transType;
	}

	/**
	 * @param transType
	 *            the transType to set
	 */
	public void setTransType(String transType) {
		this.transType = transType;
	}

	/**
	 * @return the requestTime
	 */
	public Date getRequestTime() {
		return requestTime;
	}

	/**
	 * @param requestTime
	 *            the requestTime to set
	 */
	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

	/**
	 * @return the responseTime
	 */
	public Date getResponseTime() {
		return responseTime;
	}

	/**
	 * @param responseTime
	 *            the responseTime to set
	 */
	public void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}

	/**
	 * @return the replyCode
	 */
	public String getReplyCode() {
		return replyCode;
	}

	/**
	 * @param replyCode
	 *            the replyCode to set
	 */
	public void setReplyCode(String replyCode) {
		this.replyCode = replyCode;
	}

	/**
	 * @return the replyText
	 */
	public String getReplyText() {
		return replyText;
	}

	/**
	 * @param replyText
	 *            the replyText to set
	 */
	public void setReplyText(String replyText) {
		this.replyText = replyText;
	}

	/**
	 * @return the invokeStatus
	 */
	public Integer getInvokeStatus() {
		return invokeStatus;
	}

	/**
	 * @param invokeStatus
	 *            the invokeStatus to set
	 */
	public void setInvokeStatus(Integer invokeStatus) {
		this.invokeStatus = invokeStatus;
	}

}
