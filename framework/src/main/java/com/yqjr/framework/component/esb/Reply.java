/**
 * 
 */
package com.yqjr.framework.component.esb;

import org.springframework.util.Assert;

import com.yqjr.framework.datatype.Date;

/**
 * ClassName: Reply <br>
 * Description: Reply信息 <br>
 * Create By: admin <br>
 * Create Date: 2017年7月17日 上午8:34:43 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public class Reply {

	private String replyCode;

	private String replyText;

	private Status status;

	private Date reponseTime;

	public Reply(String replyCode, String replyText, Status status) {
		Assert.hasText(replyCode, "replyCode can't be empty");
		Assert.hasText(replyText, "replyText can't be empty");
		this.replyCode = replyCode;
		this.replyText = replyText;
		this.status = status;
		this.reponseTime = new Date();
	}

	public enum Status {
		OK, ERROR
	}

	/**
	 * @return the replyCode
	 */
	public String getReplyCode() {
		return replyCode;
	}

	/**
	 * @return the replyText
	 */
	public String getReplyText() {
		return replyText;
	}

	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @return the reponseTime
	 */
	public Date getReponseTime() {
		return reponseTime;
	}

}
