/**
 * 
 */
package com.yqjr.framework.component.interceptor;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;

import com.yqjr.framework.component.context.ThreadContext;
import com.yqjr.framework.component.esb.ESBHeader;
import com.yqjr.framework.component.esb.WebServiceClient;

/**
 * ClassName: EsbHeaderAdderInterceptor <br>
 * Description: ESB通讯接出拦截器,添加ESBHeader<br>
 * Create By: admin <br>
 * Create Date: 2017年6月2日 下午1:06:23 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public class EsbHeaderAdderInterceptor extends AbstractSoapInterceptor {

	public EsbHeaderAdderInterceptor() {
		super(Phase.PRE_STREAM);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.cxf.interceptor.Interceptor#handleMessage(org.apache.cxf.
	 * message.Message)
	 */
	@Override
	public void handleMessage(SoapMessage message) throws Fault {
		// 从上下文中获取esbHeader信息并添加至soapHeader中
		ESBHeader esbHeader = ThreadContext.getInstance().getObject(WebServiceClient.WS_ESBHEADER_KEY);
		if (esbHeader != null)
			message.getHeaders().add(esbHeader.getEsbHeader());
	}

}
