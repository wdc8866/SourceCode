/**
 * 
 */
package com.yqjr.sample;

import org.springframework.stereotype.Service;

import com.yqjr.framework.component.advisor.IExtensionPoint;
import com.yqjr.framework.component.context.ThreadContext;
import com.yqjr.framework.component.datasource.RouteInfo;
import com.yqjr.framework.utils.Constants;

/**
 * ClassName: SampleExtensionPoint <br>
 * Description: TODO <br>
 * Create By: admin <br>
 * Create Date: 2017年12月27日 上午10:57:02 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
@Service
public class SampleExtensionPoint implements IExtensionPoint {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yqjr.framework.component.advisor.IExtensionPoint#preServiceInvoke(
	 * java.lang.String, java.lang.String, java.lang.Object[])
	 */
	@Override
	public void preServiceInvoke(String className, String methodName, Object[] args) {
		ThreadContext.getInstance().put(Constants.ROUTEID, new RouteInfo("payment1"));
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
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yqjr.framework.component.advisor.IExtensionPoint#preThrowException(
	 * java.lang.String, java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void preThrowException(String className, String methodName, Throwable e) {
		// TODO Auto-generated method stub

	}

}
