package com.wuyizhiye.framework.interceptor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.wuyizhiye.base.dao.QueryExecutor;
import com.wuyizhiye.base.dao.impl.QueryExecutorImpl;
import com.wuyizhiye.base.spring.ApplicationContextAware;
import com.wuyizhiye.base.spring.DataSourceHolder;
import com.wuyizhiye.base.util.SystemConfig;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.person.enums.UserTypeEnum;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.redis.dao.RedisDao;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.redis.LoginInfoUtil;
import com.wuyizhiye.framework.redis.RedisSessionUtil;
import com.wuyizhiye.framework.util.InitLoginInfoUtil;

/**
 * @ClassName LoginInterceptor
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
	private Set<String> excludePath = new HashSet<String>();
	private static Logger log=Logger.getLogger(LoginInterceptor.class); 
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String path = request.getServletPath();
		HttpSession session = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest().getSession();
		boolean noLogin = true;
		String loginInfoKey = getCookieValue(LoginInfoUtil.CLIENT_LOGIN_INFO, request);
		RedisDao redisDao = 
			ApplicationContextAware.getApplicationContext().getBean(RedisDao.class);
		if(!StringUtils.isEmpty(loginInfoKey)){			
			if(redisDao.useRedis()){
				Map<String,Object> memContext = RedisSessionUtil.getContextFromRedis(loginInfoKey);
				if(memContext != null && memContext.get(LoginInfoUtil.KEYSET.CURRENTUSER) != null){
					RedisSessionUtil.setContext(memContext);
					noLogin = false;
					SystemUtil.setCurrentRedisKey(loginInfoKey);
				}else{
					noLogin = true;
				}	
			}else{
				if(null!=session.getAttribute(LoginInfoUtil.KEYSET.CURRENTUSER)){
					noLogin = false;
				}
			}
		}else{
			if(null!=session.getAttribute(LoginInfoUtil.KEYSET.CURRENTUSER)){
				noLogin = false;
			}
		}
		
		int port =request.getServerPort();
		String scheme = request.getScheme();
		String basePath = request.getScheme()+"://"+request.getServerName() + ((("http".equals(scheme) && port == 80) ||("https".equals(scheme)  && port == 443)) ? "" : ":" + port) + request.getContextPath();		
		SystemUtil.setBase(basePath);
		String imgBase = StringUtils.isEmpty(SystemConfig.getParameter("imageBase"))?(basePath+"/images"):SystemConfig.getParameter("imageBase");	
		SystemUtil.setImageBase(imgBase);
		request.setAttribute("base",basePath);
		request.setAttribute("imgBase",imgBase);
		request.setAttribute("imgPrePath",basePath+"/default/style/images");
		request.setAttribute("initImagePath",
				StringUtils.isEmpty(SystemConfig.getParameter("defaultBase"))?basePath+"/default":SystemConfig.getParameter("defaultBase"));
		if(redisDao.useRedis()){
			request.setAttribute("skinName",null==RedisSessionUtil.getContext()?"styleBlue":(null==RedisSessionUtil.getContext().get(LoginInfoUtil.KEYSET.CURRENTSKIN)?"styleBlue":RedisSessionUtil.getContext().get(LoginInfoUtil.KEYSET.CURRENTSKIN)));
		}else{
			request.setAttribute("skinName",null==session.getAttribute(LoginInfoUtil.KEYSET.CURRENTSKIN)?"styleBlue":session.getAttribute(LoginInfoUtil.KEYSET.CURRENTSKIN));
		}
		String uiDataCenter = request.getParameter("dataCenter");
		if(!StringUtils.isEmpty(uiDataCenter)){
			DataSourceHolder.setDataSource(uiDataCenter);
			request.setAttribute("currentDataSource", uiDataCenter);
		}else{
			//当前数据中心
			if(redisDao.useRedis()){
				DataSourceHolder.setDataSource(null==RedisSessionUtil.getContext()?"":(null==RedisSessionUtil.getContext().get(LoginInfoUtil.KEYSET.CURRENTDATASOURCE)?"":(String)RedisSessionUtil.getContext().get(LoginInfoUtil.KEYSET.CURRENTDATASOURCE)));
			}else{
				request.setAttribute("skinName",null==session.getAttribute(LoginInfoUtil.KEYSET.CURRENTSKIN)?"styleBlue":session.getAttribute(LoginInfoUtil.KEYSET.CURRENTSKIN));
				DataSourceHolder.setDataSource((String) session.getAttribute("currentDataSource"));
			}
		}
		//判断微信用户访问系统
		String wechatId = request.getParameter("wechatId");
		if(session.getAttribute("currentUser") == null && !StringUtils.isEmpty(wechatId)){
			String cid = request.getParameter("cid");//数据中心
			DataSourceHolder.setDataSource(cid);
			Map<String,Object> param = new HashMap<String,Object>();
			QueryExecutor queryExecutor = QueryExecutorImpl.getInstance() ;
			Person person = null;
			String isqiye = request.getParameter("isqiye");
			if(StringUtils.isEmpty(isqiye)){
				param.put("wechatId", wechatId);
				person 
				= queryExecutor.execOneEntity("com.wuyizhiye.basedata.person.dao.PersonDao.getPersonByWechatId", param, Person.class);
				person.setPosition(person.getPostion());
			}else{
				param.put("number", wechatId);
				person 
				= queryExecutor.execOneEntity("com.wuyizhiye.basedata.person.dao.PersonDao.selectPersonList", param, Person.class);
				if(null!=person) person.setPosition(person.getPersonPosition().getPosition());
				
			}
			
			if(person == null || StringUtils.isEmpty(person.getId())){//找不对对应ERP用户返回
				return true;
			}	
			InitLoginInfoUtil.getInstance().initLoginUserInfo(response, person);
			noLogin = false;
		}
		//如果是过滤登陆
		for (String str : excludePath) {
			if (path.startsWith(str) || "/".equals(path)) {
				if(redisDao.useRedis()){
					Map<String,Object> memContext = RedisSessionUtil.getContext();
					if(null!=memContext){
						SystemUtil.setCurrentUser((Person) memContext.get("currentUser"));
						//System.out.println(Thread.currentThread().getId()+":"+SystemUtil.getCurrentUser().getName()+"::"+session.getId());
						//当前CU
						SystemUtil.setCurrentControlUnit((Org) memContext.get("currentCU"));
						//当前组织
						SystemUtil.setCurrentOrg((Org) memContext.get("currentOrg"));
						//当前职位
						SystemUtil.setCurrentPosition((Position) memContext.get("currentPosition"));
						//当前用户类型
						SystemUtil.setCurrentUserType(null==memContext.get("currentUserType")?
								null:UserTypeEnum.valueOf(memContext.get("currentUserType").toString()));
						SystemUtil.setCurrentSignature((String) memContext.get("signatureKey"));
						request.setAttribute("currentUser", memContext.get("currentUser"));
						request.setAttribute("currentOrg", memContext.get("currentOrg"));
						request.setAttribute("currentCU", memContext.get("currentCU"));
						request.setAttribute("currentPosition", memContext.get("currentPosition"));
						request.setAttribute("currentDataSource", DataSourceHolder.getDataSource());
					}
				}else{
					//当前登录用户
					SystemUtil.setCurrentUser((Person) session.getAttribute("currentUser"));
					//System.out.println(Thread.currentThread().getId()+":"+SystemUtil.getCurrentUser().getName()+"::"+session.getId());
					//当前CU
					SystemUtil.setCurrentControlUnit((Org) session.getAttribute("currentCU"));
					//当前组织
					SystemUtil.setCurrentOrg((Org) session.getAttribute("currentOrg"));
					//当前职位
					SystemUtil.setCurrentPosition((Position) session.getAttribute("currentPosition"));
					//当前用户类型
					SystemUtil.setCurrentUserType(null==session.getAttribute("currentUserType")?
							null:UserTypeEnum.valueOf(session.getAttribute("currentUserType").toString()));
					SystemUtil.setCurrentSignature((String) session.getAttribute("signatureKey"));
				}
				return true;
			}
		}
		if(!excludePath.contains(path)){
			if(noLogin){
				//普通请求
				if(request.getHeader("x-requested-with")==null){
//					String port = request.getLocalPort()==80?"":":"+request.getLocalPort();
//					path = request.getScheme()+"://"+request.getServerName()+port+request.getContextPath()+"/login";
//					response.sendRedirect(path);
					/*Cookie[] cookies = request.getCookies();
					if(cookies!=null){
						for(Cookie c : cookies){
							if("userNameTemp".equals(c.getName())){
								request.setAttribute("userName",URLDecoder.decode(c.getValue(),"utf-8"));
							}
							if("passwordTemp".equals(c.getName())){
								request.setAttribute("password",URLDecoder.decode(c.getValue(),"utf-8"));
							}
						}
					}*/
					
					request.getRequestDispatcher("/login").forward(request, response);
					return false;
				}else{
					request.getRequestDispatcher("/login").forward(request, response);
					return false;
					//throw new NotLoginException("OUTOF-LOGIN");
				}
			}
		}
		if(redisDao.useRedis()){
			Map<String,Object> memContext = RedisSessionUtil.getContext();
			if(null!=memContext){
				SystemUtil.setCurrentUser((Person) memContext.get("currentUser"));
				//System.out.println(Thread.currentThread().getId()+":"+SystemUtil.getCurrentUser().getName()+"::"+session.getId());
				//当前CU
				SystemUtil.setCurrentControlUnit((Org) memContext.get("currentCU"));
				//当前组织
				SystemUtil.setCurrentOrg((Org) memContext.get("currentOrg"));
				//当前职位
				SystemUtil.setCurrentPosition((Position) memContext.get("currentPosition"));
				//当前用户类型
				SystemUtil.setCurrentUserType(null==memContext.get("currentUserType")?
						null:UserTypeEnum.valueOf(memContext.get("currentUserType").toString()));
				SystemUtil.setCurrentSignature((String) memContext.get("signatureKey"));
				request.setAttribute("currentUser", memContext.get("currentUser"));
				request.setAttribute("currentOrg", memContext.get("currentOrg"));
				request.setAttribute("currentCU", memContext.get("currentCU"));
				request.setAttribute("currentPosition", memContext.get("currentPosition"));
				request.setAttribute("currentDataSource", DataSourceHolder.getDataSource());
			}
		}else{
			//当前登录用户
			SystemUtil.setCurrentUser((Person) session.getAttribute("currentUser"));
			//System.out.println(Thread.currentThread().getId()+":"+SystemUtil.getCurrentUser().getName()+"::"+session.getId());
			//当前CU
			SystemUtil.setCurrentControlUnit((Org) session.getAttribute("currentCU"));
			//当前组织
			SystemUtil.setCurrentOrg((Org) session.getAttribute("currentOrg"));
			//当前职位
			SystemUtil.setCurrentPosition((Position) session.getAttribute("currentPosition"));
			//当前用户类型
			SystemUtil.setCurrentUserType(null==session.getAttribute("currentUserType")?
					null:UserTypeEnum.valueOf(session.getAttribute("currentUserType").toString()));
			SystemUtil.setCurrentSignature((String) session.getAttribute("signatureKey"));
		}
		
		return super.preHandle(request, response, handler);
	}
	
	public void setExcludePath(String[] paths){
		if(paths!=null){
			for(String s : paths){
				excludePath.add(s);
			}
		}
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
		SystemUtil.setCurrentRedisKey(null);
		DataSourceHolder.setDataSource(null);
		RedisSessionUtil.setContext(null);
		String loginInfoKey = getCookieValue(LoginInfoUtil.CLIENT_LOGIN_INFO, request);
		if(!StringUtils.isEmpty(loginInfoKey)){
			RedisDao redisDao = 
				ApplicationContextAware.getApplicationContext().getBean(RedisDao.class);
			
			//如果是过滤
			String path = request.getServletPath();
			if("/basedata/info/allCount".equals(path)
					|| "/basedata/info/getMsg".equals(path) 
					||"basedata/cchatNew/priLetterMsg".equals(path)){
				return;
			}
			
			if(null!=RedisSessionUtil.getUpdateContext()){
				RedisSessionUtil.saveContext(loginInfoKey, RedisSessionUtil.getUpdateContext(),SystemUtil.getExpireTime());
			}else{
				redisDao.expireByte(loginInfoKey, SystemUtil.getExpireTime());
			}
			//redisDao.expire(SystemUtil.getCustomerOnlySign()+"|"+loginInfoKey.substring(loginInfoKey.lastIndexOf("|")+1), 
			//		SystemUtil.getExpireTime());
			
		}
		RedisSessionUtil.setUpdateContext(null);
		
		super.afterCompletion(request, response, handler, ex);
	}
}
