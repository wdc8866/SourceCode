/**
 * 
 */
package com.yqjr.framework.datatype;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * ClassName: BizzException <br>
 * Description: 业务异常定义 <br>
 * Create By: admin <br>
 * Create Date: 2017年4月19日 下午4:29:55 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public class BizzException extends RuntimeException {

	private static final long serialVersionUID = 4344839228380310245L;

	/**
	 * 错误信息
	 */
	private String errorMessage;

	/**
	 * 业务数据(需要通过异常对象携带业务数据)
	 */
	private Object bizzData;

	/**
	 * 应答码
	 */
	private ExceptionCode exceptionCode;

	/**
	 * @return the bizzData
	 */
	public Object getBizzData() {
		return bizzData;
	}

	/**
	 * @param bizzData
	 *            the bizzData to set
	 */
	public void setBizzData(Object bizzData) {
		this.bizzData = bizzData;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	public BizzException(ExceptionCode exceptionCode) {
		this.exceptionCode = exceptionCode;
	}

	public BizzException(ExceptionCode exceptionCode, String errorMessage) {
		this.exceptionCode = exceptionCode;
		this.errorMessage = errorMessage;
	}

	public BizzException(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}

	public BizzException(Throwable cause) {
		super(cause);
		this.errorMessage = cause.toString();
	}

	public BizzException(String errorMessage, Throwable cause) {
		super(errorMessage, cause);
		this.errorMessage = errorMessage;
	}

	/**
	 * Description: 获取异常堆栈信息 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年4月19日 下午6:45:22
	 *
	 * @return
	 */
	public String getStackTraceInfo() {
		ByteArrayOutputStream baos = null;
		PrintWriter writer = null;
		String exceptionStack = null;
		try {
			baos = new ByteArrayOutputStream();
			writer = new PrintWriter(baos);
			this.printStackTrace(writer);
			writer.flush();
			exceptionStack = baos.toString();
		} finally {
			try {
				baos.close();
				writer.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return exceptionStack;
	}

	/**
	 * Description: 获取应答信息 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年6月15日 下午2:00:37
	 *
	 * @return
	 */
	public String getReplyText() {
		return exceptionCode.getReplyText();
	}

	/**
	 * Description: 获取应答码 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年6月15日 下午2:00:46
	 *
	 * @return
	 */
	public String getReplyCode() {
		return exceptionCode.getReplyCode();
	}
}
