/**
 * 
 */
package framework.extension;

import org.springframework.stereotype.Service;

import com.yqjr.framework.component.advisor.IExtensionPoint;

/**
 * ClassName: PaymentExtension <br>
 * Description: 扩展点测试 <br>
 * Create By: admin <br>
 * Create Date: 2017年4月20日 上午9:51:59 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
@Service
public class PaymentExtension implements IExtensionPoint {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yqjr.framework.component.advisor.IExtensionPoint#preServiceInvoke(
	 * java.lang.String, java.lang.String, java.lang.Object[])
	 */
	@Override
	public void preServiceInvoke(String className, String methodName, Object[] args) {
		System.out.println("执行业务系统扩展点preServiceInvoke");
		System.out.println(className);
		System.out.println(methodName);
		System.out.println(args);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yqjr.framework.component.advisor.IExtensionPoint#afterServiceInvoke(
	 * java.lang.String, java.lang.String, java.lang.Object)
	 */
	@Override
	public void afterServiceInvoke(String className, String methodName, Object res) {
		System.out.println("执行业务系统扩展点afterServiceInvoke");
		System.out.println(className);
		System.out.println(methodName);
		System.out.println(res);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yqjr.framework.component.advisor.IExtensionPoint#preThrowException(
	 * java.lang.String, java.lang.String, java.lang.Exception)
	 */
	@Override
	public void preThrowException(String className, String methodName, Throwable e) {
		System.out.println("执行业务系统扩展点preThrowException");
		System.out.println(className);
		System.out.println(methodName);
		System.out.println(e);

	}

}
