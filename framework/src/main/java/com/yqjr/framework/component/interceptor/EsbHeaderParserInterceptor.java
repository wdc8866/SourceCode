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
import com.yqjr.framework.datatype.InteractiveException;

/**
 * ClassName: EsbHeaderParserInterceptor <br>
 * Description: ESB通讯接收拦截器,解析ESBHeader <br>
 * Create By: admin <br>
 * Create Date: 2017年6月2日 下午1:23:04 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 *
 */
public class EsbHeaderParserInterceptor extends AbstractSoapInterceptor {

	/**
	 * @param p
	 */
	public EsbHeaderParserInterceptor() {
		super(Phase.PRE_PROTOCOL);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.cxf.interceptor.Interceptor#handleMessage(org.apache.cxf.
	 * message.Message)
	 */
	@Override
	public void handleMessage(SoapMessage message) throws Fault {
		ESBHeader esbHeader = ThreadContext.getInstance().getObject(WebServiceClient.WS_ESBHEADER_KEY);
		if (esbHeader != null) {
			try {
				// ESBHeader解析
				esbHeader.parseEsbHeader(message, esbHeader);
			} catch (Exception e) {
				throw new Fault(new InteractiveException("ESB报文头解析失败", e));
			}
			// 判断通讯层交易是否成功
			if (!"OSB-C000".equals(esbHeader.getReplyCode())) {
				throw new Fault(new InteractiveException(esbHeader.getReplyCode(), esbHeader.getReplyText()));
			}
		}
	}
}
