package com.wuyizhiye.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName Dependence
 * @Description  注解:权限依赖
 * @author li.biao
 * @date 2015-4-1
 */
@Target(ElementType.METHOD)  
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Dependence {
	
	/**
	 * @Title method
	 * @Description method=包名.类名.方法名[直接取此类此方法的权限] method=方法名[取本类下此方法的权限]
	 * @param @return
	 * @return String
	 * @throws
	 */
	public String method();
	
}
