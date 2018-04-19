/**
 * 
 */
package com.yqjr.framework.base;

/**
 * ClassName: BaseEnum <br>
 * Description: 枚举基类 <br>
 * Create By: admin <br>
 * Create Date: 2017年4月19日 上午11:09:18 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public interface BaseEnum<E extends Enum<E>> {

	/**
	 * Description: 获取int型枚举值 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年4月19日 上午11:10:47
	 *
	 * @return
	 */
	public int getValue();

	/**
	 * Description: 获取枚举类型 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年4月28日 下午7:01:23
	 *
	 * @return
	 */
	public E valueOf();

}
