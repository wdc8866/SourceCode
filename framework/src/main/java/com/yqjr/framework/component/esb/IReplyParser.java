/**
 * 
 */
package com.yqjr.framework.component.esb;

/**
 * ClassName: IReplyParser <br>
 * Description: 应答码解析 <br>
 * Create By: admin <br>
 * Create Date: 2017年7月14日 下午4:21:30 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public interface IReplyParser {

	public Reply parseReply(Object response);

}
