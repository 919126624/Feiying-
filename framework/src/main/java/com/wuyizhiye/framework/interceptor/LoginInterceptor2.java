package com.wuyizhiye.framework.interceptor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.danga.MemCached.MemCachedClient;
import com.wuyizhiye.base.spring.DataSourceHolder;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.person.enums.UserTypeEnum;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.util.MemcachedUtils;
import com.wuyizhiye.basedata.util.SystemUtil;

/**
 * @ClassName LoginInterceptor2
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
public class LoginInterceptor2 extends HandlerInterceptorAdapter {
	private Set<String> excludePath = new HashSet<String>();
	/**
	 * 请求开始处理时，设置线程变量
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String path = request.getServletPath();
		HttpSession session = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest().getSession();
		
		//当前数据中心
		DataSourceHolder.setDataSource((String) session.getAttribute("currentDataSource"));
				
		for (String str : excludePath) {
			if (path.indexOf(str) != -1 || "/".equals(path)) {
				return true;
			}
		}
		if(!excludePath.contains(path)){
			boolean noLogin = false;
			if(MemcachedUtils.usingMemcache()){
				//使用memcache则从memcached里面判断
				String memcacheKey = getCookieValue(MemcachedUtils.CLIENT_PRIMARY_KEY, request);
				if(StringUtils.isEmpty(memcacheKey)){
					MemcachedUtils.setClientPrimaryKey(memcacheKey);
					MemCachedClient mc = MemcachedUtils.getClientInstance();
					Map<String,Object> memContext = (Map<String, Object>) mc.get(memcacheKey);
					if(memContext != null && memContext.get("currentUser") != null){
						MemcachedUtils.setContext(memContext);
						noLogin = true;
					}
				}
			}else{
				if(session.getAttribute("currentUser")==null){
					noLogin = true;
				}
			}
			if(noLogin){
				//普通请求
				if(request.getHeader("x-requested-with")==null){
					request.getRequestDispatcher("/login").forward(request, response);
					return false;
				}else{
					request.getRequestDispatcher("/login").forward(request, response);
					return false;
					//throw new NotLoginException("OUTOF-LOGIN");
				}
			}
		}
		if(MemcachedUtils.usingMemcache()){
			Map<String,Object> context = MemcachedUtils.getContext();
			//当前登录用户
			SystemUtil.setCurrentUser((Person) context.get("currentUser"));
			//当前CU
			SystemUtil.setCurrentControlUnit((Org) context.get("currentCU"));
			//当前组织
			SystemUtil.setCurrentOrg((Org) context.get("currentOrg"));
			//当前职位
			SystemUtil.setCurrentPosition((Position) context.get("currentPosition"));
			//当前用户类型
			SystemUtil.setCurrentUserType((UserTypeEnum)context.get("currentUserType"));
			
			session.setAttribute("currentUser", (Person) context.get("currentUser"));
			session.setAttribute("currentCU", (Org) context.get("currentCU"));
			session.setAttribute("currentOrg", (Org) context.get("currentOrg"));
			session.setAttribute("currentPosition", (Org) context.get("currentPosition"));
			session.setAttribute("currentUserType", (Org) context.get("currentUserType"));
		}else{
			//当前登录用户
			SystemUtil.setCurrentUser((Person) session.getAttribute("currentUser"));
			//当前CU
			SystemUtil.setCurrentControlUnit((Org) session.getAttribute("currentCU"));
			//当前组织
			SystemUtil.setCurrentOrg((Org) session.getAttribute("currentOrg"));
			//当前职位
			SystemUtil.setCurrentPosition((Position) session.getAttribute("currentPosition"));
			//当前用户类型
			SystemUtil.setCurrentUserType((UserTypeEnum)session.getAttribute("currentUserType"));
		}
		return super.preHandle(request, response, handler);
	}
	
	private String getCookieValue(String name,HttpServletRequest request){
		Cookie[] cookies = request.getCookies();
		if(cookies != null){
			for(Cookie cookie : cookies){
				if(name.equals(cookie.getName())){
					return cookie.getValue();
				}
			}
		}
		return null;
	}
	
	/**
	 * 请求的所有操作结束后，清理线程变量
	 */
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		//清空线程变量
		SystemUtil.setCurrentControlUnit(null);
		SystemUtil.setCurrentOrg(null);
		SystemUtil.setCurrentPosition(null);
		SystemUtil.setCurrentUser(null);
		SystemUtil.setCurrentUserType(null);
		if(MemcachedUtils.usingMemcache()){
			//如果使用memcached,则保存当前context到memcached
			String memcacheKey = MemcachedUtils.getClientPrimaryKey();
			Map<String,Object> context = MemcachedUtils.getContext();
			if(!StringUtils.isEmpty(memcacheKey) && context != null){
				MemcachedUtils.saveContext(memcacheKey, context);
			}
		}
		super.afterCompletion(request, response, handler, ex);
	}
	
	public void setExcludePath(String[] paths){
		if(paths!=null){
			for(String s : paths){
				excludePath.add(s);
			}
		}
	}
}
