/**
 * 
 */
package com.yqjr.framework.component.advisor;

/**
 * ClassName: IExtensionPoint <br>
 * Description: 服务调用扩展点接口 <br>
 * Create By: admin <br>
 * Create Date: 2017年4月20日 上午8:55:20 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public interface IExtensionPoint {

	/**
	 * Description: 服务调用前执行方法 <br>
	 * 方法内对args的修改将影响服务调用参数<br>
	 * Create By: admin <br>
	 * Create Date: 2017年4月20日 上午8:57:09
	 *
	 * @param args
	 *            服务调用参数
	 */
	public void preServiceInvoke(String className, String methodName, Object[] args);

	/**
	 * Description: 服务调用后执行方法 <br>
	 * 方法内对res的修改将影响服务调用结果<br>
	 * Create By: admin <br>
	 * Create Date: 2017年4月20日 上午8:57:24
	 *
	 * @param res
	 *            调用结果
	 */
	public void afterServiceInvoke(String className, String methodName, Object res);

	/**
	 * Description: 抛出服务异常前执行方法 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年4月20日 上午9:54:23
	 *
	 * @param e
	 */
	public void preThrowException(String className, String methodName, Throwable e);

}
