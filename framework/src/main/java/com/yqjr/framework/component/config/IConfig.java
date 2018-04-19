/**
 * 
 */
package com.yqjr.framework.component.config;

/**
 * ClassName: IConfig <br>
 * Description: 系统配置读取 <br>
 * Create By: admin <br>
 * Create Date: 2017年4月17日 下午3:02:22 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public interface IConfig {

	/**
	 * Description: 读取string类型的参数配置 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年4月17日 下午3:08:30
	 *
	 * @param key
	 *            参数索引
	 * @return
	 */
	public String getStringValue(String key);

	/**
	 * Description: 读取int类型的参数配置 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年4月17日 下午3:08:30
	 *
	 * @param key
	 *            参数索引
	 * @return
	 */
	public int getIntValue(String key);

	/**
	 * Description: 读取long类型的参数配置 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年4月17日 下午3:08:30
	 *
	 * @param key
	 *            参数索引
	 * @return
	 */
	public long getLongValue(String key);

	/**
	 * Description: 读取boolean类型的参数配置 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年4月17日 下午3:08:30
	 *
	 * @param key
	 *            参数索引
	 * @return
	 */
	public boolean getBooleanValue(String key);

}
