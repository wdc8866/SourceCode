/**
 * 
 */
package com.yqjr.framework.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Service;

/**
 * ClassName: FrameworkWebService <br>
 * Description: 框架webservice注解 <br>
 * Create By: admin <br>
 * Create Date: 2017年5月16日 下午6:26:47 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface FrameworkWebService {

	public String serviceName() default "";

	public String nameSpace() default "";

	public String url();

}
