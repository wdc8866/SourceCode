/**
 * 
 */
package com.yqjr.framework.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Repository;

/**
 * ClassName: FrameworkDao <br>
 * Description: 框架Dao,同原框架MybatisDao <br>
 * Create By: admin <br>
 * Create Date: 2017年5月3日 上午8:55:05 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Repository
public @interface FrameworkDao {

	String value() default "";

}
