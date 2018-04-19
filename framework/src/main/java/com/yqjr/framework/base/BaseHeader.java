/**
 * 
 */
package com.yqjr.framework.base;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotBlank;

import com.yqjr.framework.component.context.SpringContext;
import com.yqjr.framework.component.validator.BeanValidators;
import com.yqjr.framework.component.validator.DateFormator;
import com.yqjr.framework.datatype.BizzException;
import com.yqjr.framework.datatype.ExceptionCode;

/**
 * ClassName: BaseHeader <br>
 * Description: 框架webservice报文头 <br>
 * 为标准化webservice服务,设定webservice报文头,如字段不满足应用可自行扩展 <br>
 * Create By: admin <br>
 * Create Date: 2017年5月19日 下午7:05:33 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
@XmlType(propOrder = { "serialNo", "channel", "serviceCode", "transactionTime", "token", "replyCode", "replyText" })
public abstract class BaseHeader {

	/**
	 * 交易流水号
	 */
	@NotBlank
	private String serialNo;

	/**
	 * 交易渠道
	 */
	@NotBlank
	private String channel;

	/**
	 * 交易码
	 */
	@NotBlank
	private String serviceCode;

	/**
	 * 渠道端交易时间(yyyyMMddHHmmss)
	 */
	@DateFormator(format = "yyyyMMddHHmmss")
	private String transactionTime;

	/**
	 * 交易签名信息
	 */
	private String token;

	/**
	 * 响应码
	 */
	private String replyCode;

	/**
	 * 响应信息
	 */
	private String replyText;

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
	 * @return the channel
	 */
	public String getChannel() {
		return channel;
	}

	/**
	 * @param channel
	 *            the channel to set
	 */
	public void setChannel(String channel) {
		this.channel = channel;
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
	 * @return the transactionTime
	 */
	public String getTransactionTime() {
		return transactionTime;
	}

	/**
	 * @param transactionTime
	 *            the transactionTime to set
	 */
	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token
	 *            the token to set
	 */
	public void setToken(String token) {
		this.token = token;
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
	 * Description: 公共报文头校验 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年6月15日 上午11:39:13
	 *
	 * @throws BizzException
	 */
	public void validate() throws BizzException {
		Validator validator = SpringContext.getInstance().getBeanWithName("validator");
		try {
			BeanValidators.validateWithException(validator, this);
		} catch (ConstraintViolationException e) {
			throw new BizzException(ExceptionCode.WS_0001, BeanValidators.extractPropertyAndMessage(e).toString());
		}
	}

	/**
	 * Description: 扩展字段校验 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年6月15日 上午11:40:51
	 *
	 * @throws BizzException
	 */
	public abstract void extValidate() throws BizzException;

	/**
	 * 打印报文头信息
	 */
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
