/**
 * 
 */
package com.yqjr.framework.base;

import com.yqjr.framework.datatype.BizzException;

/**
 * ClassName: BaseRequestDTO <br>
 * Description: 框架请求DTO基类 <br>
 * Create By: admin <br>
 * Create Date: 2017年5月19日 下午5:35:33 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public abstract class BaseRequestDTO<T> extends BaseBean<T> {

	private static final long serialVersionUID = -3511914927639318019L;

	public abstract void validate() throws BizzException;

}
