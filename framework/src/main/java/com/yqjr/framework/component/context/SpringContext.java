/**
 * 
 */
package com.yqjr.framework.component.context;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import com.yqjr.framework.component.cache.AbstractCache;
import com.yqjr.framework.component.cache.ICache;
import com.yqjr.framework.datatype.BizzException;

/**
 * ClassName: SpringContext <br>
 * Description: spring容器封装类,以静态变量保存Spring ApplicationContext<br>
 * Create By: admin <br>
 * Create Date: 2017年4月17日 下午4:38:17 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public class SpringContext {

	private static SpringContext instance = null;

	private ApplicationContext applicationContext = null;

	private ICache classCache = AbstractCache.getCache("map");

	private ICache methodCache = AbstractCache.getCache("map");

	private static final String BEAN_CACHE = "framework.bean.cache";

	private static final String METHOD_CACHE = "framework.method.cache";

	private SpringContext() {
	}

	public static SpringContext getInstance() {
		if (instance == null) {
			synchronized (SpringContext.class) {
				if (instance == null) {
					instance = new SpringContext();
				}
			}
		}
		return instance;
	}

	/**
	 * Description: 设置applicationContext,在系统启动类,spring容器初始化完毕后设置 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年4月17日 下午6:10:25
	 *
	 * @param applicationContext
	 */
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	/**
	 * @return the applicationContext
	 */
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * Description: 根据bean名称获取实例 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月7日 下午4:36:27
	 *
	 * @param beanName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getBeanWithName(String beanName) {
		Assert.hasText(beanName);
		return (T) applicationContext.getBean(beanName);
	}

	/**
	 * Description: 根据bean类型获取实例 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月7日 下午4:39:54
	 *
	 * @param clazz
	 * @return
	 */
	public <T> T getBeanWithClass(Class<T> clazz) {
		Assert.notNull(clazz);
		return applicationContext.getBean(clazz);
	}

	/**
	 * Description: 根据bean类型获取实例 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月7日 下午4:36:43
	 *
	 * @param className
	 * @return
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public <T> T getBeanWithClassName(String className) {
		Assert.hasText(className);
		Class<?> clazz = getClassWithName(className);
		return (T) getBeanWithClass(clazz);
	}

	/**
	 * Description: 根据注解类型获取springcontext中的bean <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月17日 上午8:13:10
	 *
	 * @param clazz
	 * @return
	 */
	public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> clazz) {
		return applicationContext.getBeansWithAnnotation(clazz);
	}

	/**
	 * Description: 通过类名获取class <br>
	 * Create By: infohold <br>
	 * Create Data: 2016年3月14日 下午2:42:17
	 *
	 * @param className
	 *            类名
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Class getClassWithName(String className) {
		Assert.notNull(className);
		Class<?> clazz = classCache.get(BEAN_CACHE, className);
		if (clazz == null) {
			synchronized (className) {
				if ((clazz = classCache.get(BEAN_CACHE, className)) == null) {
					try {
						clazz = Class.forName(className);
						classCache.put(BEAN_CACHE, className, clazz);
					} catch (ClassNotFoundException e) {
						throw new BizzException(String.format("指定的类名不存在,%s", className), e);
					}
				}
			}
		}
		return clazz;
	}

	/**
	 * Description: 查询method对象,不支持重载,即同名方法只有一个,方法名不区分大小写 <br>
	 * Create By: infohold <br>
	 * Create Data: 2016年3月14日 下午3:47:33
	 *
	 * @param clazz
	 *            检索类
	 * @param methodName
	 *            方法名称
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Method getMethodWithName(Class clazz, String methodName) {

		Assert.notNull(clazz);
		Assert.notNull(methodName);

		String key = clazz.getName() + "-" + methodName;
		Method method = methodCache.get(METHOD_CACHE, key);
		if (method == null) {
			synchronized (key) {
				if ((method = methodCache.get(METHOD_CACHE, key)) == null) {
					Method[] methods = clazz.getMethods();
					for (Method tmp : methods) {
						if (methodName.equalsIgnoreCase(tmp.getName())) {
							method = tmp;
							methodCache.put(METHOD_CACHE, key, method);
							break;
						}
					}
					if (method == null) {
						throw new BizzException(String.format("类%s不存在%s方法", clazz.getName(), methodName));
					}
				}
			}
		}
		return method;
	}

	/**
	 * Description: 根据指定类名获取方法 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年6月2日 上午10:52:25
	 *
	 * @param className
	 * @param methodName
	 * @return
	 */
	public Method getMethodWithName(String className, String methodName) {
		return getMethodWithName(getClassWithName(className), methodName);
	}
}
