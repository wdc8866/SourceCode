/**
 * 
 */
package com.yqjr.modules.user.model;

/**
 * ClassName: TransModel <br>
 * Description: TODO <br>
 * Create By: admin <br>
 * Create Date: 2017年7月10日 下午6:38:51 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 *
 */
public class TransModel extends UserModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2884153950513465166L;
	private String transKey;

	/**
	 * @return the transKey
	 */
	public String getTransKey() {
		return transKey;
	}

	/**
	 * @param transKey
	 *            the transKey to set
	 */
	public void setTransKey(String transKey) {
		this.transKey = transKey;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yqjr.modules.user.model.UserModel#getTransactionalKey()
	 */
	@Override
	public String getTransactionalKey() {
		return transKey;
	}

}
