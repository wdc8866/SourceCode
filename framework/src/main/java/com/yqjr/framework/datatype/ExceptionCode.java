/**
 * 
 */
package com.yqjr.framework.datatype;

/**
 * ClassName: ReplyCode <br>
 * Description: WebService应答码 <br>
 * Create By: admin <br>
 * Create Date: 2017年6月15日 上午11:14:44 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public enum ExceptionCode {

	WS_0000("WS_0000", "交易成功"), // 交易成功时返回
	WS_9999("WS_9999", "未知异常"), // 系统处理异常但未进行处理,最外层捕获后返回

	// 交易码定义原则
	// 1-2位 服务类型 WS-WebService服务应答码 RS-Rest服务应答码
	// 3位 固定_
	// 4位 0-参数校验引起的异常 1-业务逻辑判断引起的异常 2-业务处理引起的异常 3-外调其他系统引起的异常 ...
	// 5-7位 具体异常序号

	WS_0001("WS_0001", "服务报文头校验失败"),
	WS_1001("WS_1001", "交易流水号重复");

	private ExceptionCode(String replyCode, String replyText) {
		this.replyCode = replyCode;
		this.replyText = replyText;
	}

	private String replyCode;
	private String replyText;

	/**
	 * @return the replyText
	 */
	public String getReplyText() {
		return replyText;
	}

	/**
	 * @return the replyCode
	 */
	public String getReplyCode() {
		return replyCode;
	}

}
