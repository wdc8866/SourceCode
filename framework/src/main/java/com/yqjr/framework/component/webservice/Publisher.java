/**
 * 
 */
package com.yqjr.framework.component.webservice;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import javax.jws.WebParam;
import javax.jws.WebParam.Mode;
import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.ws.Holder;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.springframework.util.Assert;

import com.yqjr.framework.annotation.FrameworkWebService;
import com.yqjr.framework.base.BaseWebService;
import com.yqjr.framework.component.context.SpringContext;
import com.yqjr.framework.component.interceptor.ReceiverLoggingInterceptor;
import com.yqjr.framework.component.interceptor.SenderLoggingInterceptor;
import com.yqjr.framework.component.log.Logger;
import com.yqjr.framework.datatype.BizzException;

/**
 * ClassName: Publisher <br>
 * Description: 框架webservice发布组件 <br>
 * Create By: admin <br>
 * Create Date: 2017年5月16日 下午6:30:00 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public class Publisher {

	private static Publisher instance = null;

	private Map<String, Object> registWs = null;

	private static final String CATEGORY = Publisher.class.getName();

	private static Logger logger = Logger.getLogger();

	private Publisher() {
		registWs = SpringContext.getInstance().getBeansWithAnnotation(FrameworkWebService.class);
	}

	/**
	 * Description: 获取publishWebService实例 <br>
	 * Create By: infohold <br>
	 * Create Data: 2017年5月16日 下午2:10:07
	 *
	 * @return publishWebService
	 */
	public static Publisher getInstance() {
		if (instance == null) {
			synchronized (Publisher.class) {
				if (instance == null) {
					instance = new Publisher();
				}
			}
		}
		return instance;
	}

	/**
	 * Description: 发布注册的ws <br>
	 * Create By: infohold <br>
	 * Create Data: 2016年3月21日 上午9:40:54
	 *
	 */
	public void publish() {

		if (registWs.isEmpty()) {
			logger.info(CATEGORY, "未发现使用@FrameworkWebService标注的webservice");
			return;
		}

		logger.info(CATEGORY, "扫描webservice服务...");
		// 遍历使用注解注册的服务列表
		for (Object object : registWs.values()) {

			// 继承关系校验
			Assert.isInstanceOf(BaseWebService.class, object,
					"使用@FrameworkWebService标注的webservice实现类必须继承com.yqjr.framework.base.BaseWebService,异常服务:" + object);

			// 校验是否实现接口
			Class<?>[] clazzes = object.getClass().getInterfaces();
			Assert.notEmpty(clazzes, "@FrameworkWebService标注的服务必须实现接口,异常服务:" + object);
			// 校验实现接口的注解内容
			Class<?> serviceInterface = null;
			for (Class<?> c : clazzes) {
				WebService webservice = c.getAnnotation(WebService.class);
				if (webservice != null) {
					serviceInterface = c;
					break;
				}
			}
			Assert.notNull(serviceInterface, "接口必须使用@WebService注解,异常服务:" + object);

			// 校验泛型内容
			Type genType = object.getClass().getGenericSuperclass();
			Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

			// 校验接口方法内容
			Method method = null;
			try {
				method = serviceInterface.getDeclaredMethod("process", Holder.class, (Class<?>) params[1],
						Holder.class);
				Type header = method.getGenericParameterTypes()[0];
				Assert.isInstanceOf(ParameterizedType.class, header);
				Assert.isTrue(((ParameterizedType) header).getActualTypeArguments()[0] == params[0],
						"header参数非法,异常服务:" + serviceInterface);
				Type response = method.getGenericParameterTypes()[2];
				Assert.isInstanceOf(ParameterizedType.class, response);
				Assert.isTrue(((ParameterizedType) response).getActualTypeArguments()[0] == params[2],
						"response参数非法,异常服务:" + serviceInterface);
			} catch (Exception e) {
				throw new BizzException("接口应包含process(Header,RequestDTO,ResponseDTO)方法,异常服务:" + serviceInterface, e);
			}
			// 校验接口方法参数注解内容
			Annotation annotations[][] = method.getParameterAnnotations();
			Assert.isInstanceOf(WebParam.class, annotations[0][0],
					"process方法参数1必须使用@WebParam,异常服务:" + serviceInterface);
			Assert.isInstanceOf(WebParam.class, annotations[1][0],
					"process方法参数2必须使用@WebParam,异常服务:" + serviceInterface);
			Assert.isInstanceOf(WebParam.class, annotations[2][0],
					"process方法参数3必须使用@WebParam,异常服务:" + serviceInterface);
			WebParam headerAnnotation = (WebParam) annotations[0][0];
			WebParam requestAnnotation = (WebParam) annotations[1][0];
			WebParam responseAnnotation = (WebParam) annotations[2][0];
			Assert.isTrue("header".equals(headerAnnotation.name()) && Mode.INOUT == headerAnnotation.mode(),
					"process方法参数1,name=header,mode=INOUT,异常服务:" + serviceInterface);
			Assert.isTrue("request".equals(requestAnnotation.name()) && Mode.IN == requestAnnotation.mode(),
					"process方法参数2,name=request,mode=IN,异常服务:" + serviceInterface);
			Assert.isTrue("response".equals(responseAnnotation.name()) && Mode.OUT == responseAnnotation.mode(),
					"process方法参数3,name=response,mode=INOUT,异常服务:" + serviceInterface);

			// 校验完毕开始配置服务
			JaxWsServerFactoryBean jwsFactory = new JaxWsServerFactoryBean();
			// 获取注解配置信息
			FrameworkWebService wsConf = object.getClass().getAnnotation(FrameworkWebService.class);
			String url = wsConf.url();
			// 发布服务
			ReceiverLoggingInterceptor receiverLogger = SpringContext.getInstance().getBeanWithName("receiverLogger");
			SenderLoggingInterceptor senderLogger = SpringContext.getInstance().getBeanWithName("senderLogger");
			jwsFactory.getInInterceptors().add(receiverLogger);
			jwsFactory.getOutInterceptors().add(senderLogger);
			if (StringUtils.isNotEmpty(wsConf.serviceName()) && StringUtils.isNotEmpty(wsConf.nameSpace())) {
				jwsFactory.setServiceName(new QName(wsConf.nameSpace(), wsConf.serviceName()));
			}
			jwsFactory.setServiceClass(serviceInterface);
			jwsFactory.setAddress(url);
			jwsFactory.setServiceBean(object);
			jwsFactory.create();
		}
		logger.info(CATEGORY, "服务发布完毕!");
	}

}
