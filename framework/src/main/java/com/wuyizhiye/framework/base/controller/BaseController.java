package com.wuyizhiye.framework.base.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.wuyizhiye.base.dao.QueryExecutor;
import com.wuyizhiye.base.spring.ApplicationContextAware;
import com.wuyizhiye.base.spring.DataSourceHolder;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.redis.dao.RedisDao;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.redis.LoginInfoUtil;
import com.wuyizhiye.framework.redis.RedisSessionUtil;

/**
 * @ClassName BaseController
 * @Description 控制器基类
 * @author li.biao
 * @date 2015-4-7
 */
public abstract class BaseController {
	private Logger log = Logger.getLogger(BaseController.class);
	private static ThreadLocal<Map<String,Object>> outPutMsg = new ThreadLocal<Map<String,Object>>();
	@Resource(name="queryExecutor")
	protected QueryExecutor queryExecutor;
	/**
	 * 获取request
	 * @return
	 */
	protected HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		
	}

	/**
	 * 获取session
	 * @return
	 */
	protected HttpSession getSession() {
		return ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest().getSession();
	}

	protected ServletContext getServletContext() {
		return ContextLoader.getCurrentWebApplicationContext()
				.getServletContext();
	}
	
	/**
	 * 取当前登录用户信息
	 *  reason:因为现在没有User对象
	 * @return
	 */
	
	public Person getCurrentUser() {
		Person currentPerson = 	SystemUtil.getCurrentUser();
		return currentPerson;
	}
	
	/**
	 * 将控制单元的id放到Map中
	 * @param params
	 */
	public void putControlUnitIdToMap(Map params){
		//如果是集团的id的话，则不控制CU数据隔离
		if(null!=SystemUtil.getCurrentControlUnit()&&!"000000000000000000000000000000F".equalsIgnoreCase(SystemUtil.getCurrentControlUnit().getId())){
			 if(params==null ||  params.get("noFilterCU")==null || "false".equals(params.get("noFilterCU").toString())){  
				params.put("controlUnitId", SystemUtil.getCurrentControlUnit().getId());	//得到控制单元的Id
			}
		}
	}
	
	public String getString(String name) {
		return getString(name, null);
	}

	public String getString(String name, String defaultValue) {
		String resultStr = getRequest().getParameter(name);
		if (resultStr == null || "".equals(resultStr)
				|| "null".equals(resultStr) || "undefined".equals(resultStr)) {
			return defaultValue;
		} else {
			return resultStr;
		}
	}
	
	/**
	 * 输出，同时清空outPutMsg
	 * @param response
	 * @param result
	 */

	public void outPrint(HttpServletResponse response, Object result) {
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter out = response.getWriter();
            out.print(result.toString());
            getOutputMsg().clear();
        } catch (IOException e) {
        	log.error(e);
        }
    }
	
	/**
	 * json转换config
	 * @return
	 */
	protected JsonConfig getDefaultJsonConfig(){
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof Date){
					return sdf.format(value);
				}
				return null;
			}
			
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof Date){
					return sdf.format(value);
				}
				return null;
			}
		});
		return config;
	}

	/**
	 * 线程绑定，其内容会在outPrint方法调用后清空
	 * @return the outputMsg
	 */
	public Map<String,Object> getOutputMsg() {
		Map<String,Object> output = outPutMsg.get();
		if(output==null){
			output = new HashMap<String, Object>();
			outPutMsg.set(output);
		}
		return output;
	}
	
	protected void setOutputMsg(String key,String value){
		getOutputMsg().put(key, value);
	}
	
	@SuppressWarnings("rawtypes")
	protected Map<String,String> getParamMap(){
		Map<String,String> parameters = new HashMap<String, String>();
		Map map = getRequest().getParameterMap(); 
		Set keys = map.keySet();
		for(Object key : keys){
			parameters.put(key.toString(), getRequest().getParameter(key.toString()));
		}
		return parameters;
	}
	
	/**
	 * 将参数封装成Map<String,Object>空字符串转为null
	 * @return
	 * add by hlf 2013-2-10
	 */
	@SuppressWarnings("rawtypes")
	protected Map<String,Object> getParaMap(){
		Map<String,Object> parameters = new HashMap<String, Object>();
		Map map = getRequest().getParameterMap();
		Set keys = map.keySet();
		for(Object key : keys){
			Object o=getRequest().getParameter(key.toString());
			if(o instanceof String ){
				if("".equals(o)){
					o=null;
				}
			}
			parameters.put(key.toString(), o);
		}
		this.putControlUnitIdToMap(parameters);
		return parameters;
	}

	public int getInt(String name) {
		return getInt(name, 0);
	}

	public int getInt(String name, int defaultValue) {
		String resultStr = getRequest().getParameter(name);
		if (resultStr != null) {
			try {
				return Integer.parseInt(resultStr);
			} catch (Exception e) {
				return defaultValue;
			}
		}
		return defaultValue;
	}

	public BigDecimal getBigDecimal(String name) {
		return getBigDecimal(name, null);
	}
	
	public BigDecimal getBigDecimal(String name, BigDecimal defaultValue) {
		String resultStr = getRequest().getParameter(name);
		if (resultStr != null) {
			try {
				return BigDecimal.valueOf(Double.parseDouble(resultStr));
			} catch (Exception e) {
				return defaultValue;
			}
		}
		return defaultValue;
	}
	
	/**
	 * 判断当前人员是否有该权限项的权限
	 * @param permissionId 权限项的ID
	 * @return
	 */
	@SuppressWarnings({ "unchecked"})
	public boolean hasPermission(String permissionId){
		Map<String,String> permissionMap = (Map<String, String>) getSessionValue(LoginInfoUtil.KEYSET.PERSONALPERMISSIONMAP);
		return (permissionMap!=null&&permissionMap.containsKey(permissionId))?checkPermission(permissionMap.get(permissionId)):false;
	}
	
	/**
	 * 检查当前用户是否拥有指定权限
	 * @param method 权限对应的controller类名+方法名
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean checkPermission(String method){
		List<String> personPermission = (List<String>) this.getSessionValue(LoginInfoUtil.KEYSET.PERSONALPERMISSION);
		Set<String> wholePermission = (Set<String>) getSession().getServletContext().getAttribute("WHOLE-PERMISSIONMETHODS-" + DataSourceHolder.getDataSource());
		if(wholePermission!= null && wholePermission.contains(method)){
			//有权限控制的才验证
			return personPermission!=null?personPermission.contains(method):false;
		}else{
			//无权限控制的直接通过
			return true;
		}
	}
	
	/**
	 * 得到IP地址
	 * @param request
	 * @return
	 */
	public String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	public void putCommonPath(){
		int port = getRequest().getServerPort();
		String scheme = getRequest().getScheme();
		String path = getRequest().getScheme()+"://"+getRequest().getServerName() + ((("http".equals(scheme) && port == 80) ||("https".equals(scheme)  && port == 443)) ? "" : ":" + port) + getRequest().getContextPath();
		getSession().setAttribute("base",path);
		getSession().setAttribute("imgBase",path+"/images");
		getSession().setAttribute("imgPrePath",path+"/default/style/images");
		getSession().setAttribute("initImagePath",path+"/default");
	}
	
	protected String getBasePath(){
		int port = getRequest().getServerPort();
		String scheme = getRequest().getScheme();
		String path = getRequest().getScheme()+"://"+getRequest().getServerName() + ((("http".equals(scheme) && port == 80) ||("https".equals(scheme)  && port == 443)) ? "" : ":" + port) + getRequest().getContextPath();
		return path;
	}
	
	protected RedisDao getRedisClient(){
		return ApplicationContextAware.getApplicationContext().getBean(RedisDao.class);
	}
	
	protected boolean isMobileDevice(){
		String qhd=getRequest().getHeader("user-agent");
		if(qhd!=null){
			if(qhd.indexOf("Android")>-1){
				return true;
			}else if(qhd.indexOf("iPhone")>-1){
				return true;
			}else if(qhd.indexOf("iPad")>-1){
				return true;
			}else if(qhd.indexOf("Windows")<0 && qhd.indexOf("Linux")<0 && qhd.indexOf("Apple") < 0){//根据请求的客户端信息判断 如果是非PC登录
				return true;
			}
		}
		return false;
	}
	
	protected String getCookieValue(String key){
		Cookie[] cookie = getRequest().getCookies();
		if(null==cookie || cookie.length==0) return null;
		for (int i = 0; i < cookie.length; i++) {
		Cookie cook = cookie[i];
		if(cook.getName().equalsIgnoreCase(key)){ //获取键 
			return cook.getValue().toString();
			}
		}
		return null;
	}
	
	public void setSessionValue(String key,Object v){
		if(this.getRedisClient().useRedis()){
			Map<String,Object> upctx = RedisSessionUtil.getUpdateContext();
			Map<String,Object> ctx = RedisSessionUtil.getContext();
			if(null==ctx) ctx = new HashMap<String,Object>();
			if(null==upctx) upctx = new HashMap<String,Object>();
			upctx.put(key, v);
			ctx.put(key, v);
			RedisSessionUtil.setContext(ctx);
			RedisSessionUtil.setUpdateContext(upctx);
		}else{
			this.getSession().setAttribute(key, v);
		}
	}
	
	public Object getSessionValue(String key){
		Object obj = null;
		if(this.getRedisClient().useRedis()){
			Map<String,Object> ctx = RedisSessionUtil.getContext();
			if(null==ctx) return null;
			 obj =  ctx.get(key);
		}else{
			obj = this.getSession().getAttribute(key);
		}
		return obj;
	}
	
	public void setSessionValueList(Map<String,Object> data){
		if(this.getRedisClient().useRedis()){
			Map<String,Object> upctx = RedisSessionUtil.getUpdateContext();
			Map<String,Object> ctx = RedisSessionUtil.getContext();
			if(null==ctx) ctx = new HashMap<String,Object>();
			if(null==upctx) upctx = new HashMap<String,Object>();
			ctx.putAll(data);
			upctx.putAll(data);
			RedisSessionUtil.setUpdateContext(upctx);
			RedisSessionUtil.setContext(ctx);
		}else{
			Set<String> keyset = data.keySet();
			Iterator<String> ite = keyset.iterator();
			while(ite.hasNext()){
				String key = ite.next();
				this.getSession().setAttribute(key,data.get(key));
			}
		}
	}
	
}
