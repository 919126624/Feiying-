package com.wuyizhiye.basedata.permission.interceptor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.wuyizhiye.base.annotation.Dependence;
import com.wuyizhiye.base.dao.QueryExecutor;
import com.wuyizhiye.base.dao.impl.QueryExecutorImpl;
import com.wuyizhiye.base.spring.ApplicationContextAware;
import com.wuyizhiye.base.spring.DataSourceHolder;
import com.wuyizhiye.basedata.permission.controller.PermissionItemListController;
import com.wuyizhiye.basedata.permission.exception.NoPermissionException;
import com.wuyizhiye.basedata.permission.service.PermissionItemService;
import com.wuyizhiye.basedata.redis.serialize.TransCoderUtil;
import com.wuyizhiye.basedata.util.RedisHolder;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.redis.LoginInfoUtil;
import com.wuyizhiye.framework.redis.RedisSessionUtil;

/**
 * @ClassName PermissionInterceptor
 * @Description 权限拦截器
 * 以web请求为入口，采用方法拦截的方式
 * @author li.biao
 * @date 2015-4-7
 */
@SuppressWarnings("unchecked")
public class PermissionInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		HandlerMethod hm = (HandlerMethod) handler;
		Class<?> beanClass = hm.getBeanType();
		Method method = hm.getMethod();
		String className =  beanClass.getName();
		String methodName = method.getName();
		String permissionMethodName = className + "." + methodName;
		HttpSession session = request.getSession();
		Set<String> wholePermissionMethod = getPermissionMethods(session.getServletContext());
		if(wholePermissionMethod != null && wholePermissionMethod.contains(permissionMethodName)){
			//存在此方法权限控制
			validatePermission(session, permissionMethodName);
		}else{
			//判断是否依赖于某权限
			Dependence dep = method.getAnnotation(Dependence.class);
			if(dep!=null){
				String dm = dep.method();
				if(dm.indexOf(".")>0){
					//指定了依赖的类和方法
					permissionMethodName = dm;
				}else{
					//本类和方法
					permissionMethodName = className + "." + dm;
				}
				if(wholePermissionMethod != null && wholePermissionMethod.contains(permissionMethodName)){
					//存在依赖方法权限控制
					validatePermission(session, permissionMethodName);
				}
			}
		}
		return super.preHandle(request, response, handler);
	}
	
	private void validatePermission(HttpSession session,String permissionMethodName){
		List<String> personPermissions = getPersonPermission(session);
		if(personPermissions == null || !personPermissions.contains(permissionMethodName)){
			//throw new NoPermissionException("NO-PERMISSION");
		}
	}
	
	/**
	 * 获取系统权限项
	 * @param context
	 * @return
	 */
	private Set<String> getPermissionMethods(ServletContext context){
		Set<String> permissionMethods = (Set<String>) context.getAttribute("WHOLE-PERMISSIONMETHODS-" + DataSourceHolder.getDataSource());
		if(permissionMethods==null){
			PermissionItemService c = ApplicationContextAware.getApplicationContext().getBean(PermissionItemService.class);
			c.loadPermissions();
			permissionMethods = (Set<String>) context.getAttribute("WHOLE-PERMISSIONMETHODS-" + DataSourceHolder.getDataSource());
		}
		return permissionMethods;
	}
	
	/**
	 * 获取用户权限项
	 * @param session
	 * @return
	 */
	private List<String> getPersonPermission(HttpSession session){
		List<String> personPermission = null;
		if(RedisHolder.getRedisClient().useRedis()){
				personPermission =  (List<String>)RedisSessionUtil.getContext().get(LoginInfoUtil.KEYSET.PERSONALPERMISSION);
		}else{
				personPermission = (List<String>) session.getAttribute(LoginInfoUtil.KEYSET.PERSONALPERMISSION);
		}
		return personPermission;
	}
}
