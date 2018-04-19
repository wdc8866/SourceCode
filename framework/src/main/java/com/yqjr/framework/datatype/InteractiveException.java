/**
 * 
 */
package com.yqjr.framework.datatype;

/**
 * ClassName: InteractiveException <br>
 * Description: 远程调用通讯异常定义 <br>
 * Create By: admin <br>
 * Create Date: 2017年6月5日 上午11:17:48 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public class InteractiveException extends Exception {

	private static final long serialVersionUID = -5997416121747954983L;

	/**
	 * 通讯层响应码
	 */
	private String replyCode;

	/**
	 * 通讯层响应信息
	 */
	private String replyText;

	public InteractiveException(String replyCode, String replyText) {
		this.replyCode = replyCode;
		this.replyText = replyText;
	}

	public InteractiveException(String message, Throwable e) {
		super(message, e);
		this.replyCode = "999999";
		this.replyText = "远程接口调用出现未知异常";
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

}
