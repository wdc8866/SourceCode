/**
 * 
 */
package com.yqjr.framework.base;

/**
 * ClassName: BaseCloneable <br>
 * Description: 克隆基类 <br>
 * Create By: admin <br>
 * Create Date: 2017年4月26日 下午2:25:42 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public interface BaseCloneable<T> extends Cloneable {

	/**
	 * Description: 克隆方法 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年4月26日 下午2:26:39
	 *
	 * @return
	 */
	public T clone();

}
