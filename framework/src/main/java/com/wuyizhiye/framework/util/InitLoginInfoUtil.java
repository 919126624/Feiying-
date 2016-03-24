package com.wuyizhiye.framework.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.NoPermissionException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.wuyizhiye.base.dao.impl.QueryExecutorImpl;
import com.wuyizhiye.base.spring.ApplicationContextAware;
import com.wuyizhiye.base.spring.DataSourceHolder;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.base.util.SystemConfig;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.org.service.OrgService;
import com.wuyizhiye.basedata.org.service.PositionService;
import com.wuyizhiye.basedata.person.enums.UserTypeEnum;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.model.PersonPosition;
import com.wuyizhiye.basedata.person.model.Skin;
import com.wuyizhiye.basedata.redis.serialize.TransCoderUtil;
import com.wuyizhiye.basedata.util.OperationLogUtil;
import com.wuyizhiye.basedata.util.OrgUtils;
import com.wuyizhiye.basedata.util.RedisHolder;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.basedata.util.UserTypeUtil;
import com.wuyizhiye.framework.listener.LoginTypeEnum;
import com.wuyizhiye.framework.listener.OnlineUser;
import com.wuyizhiye.framework.redis.LoginInfoUtil;

/**
 * @ClassName InitLoginInfoUtil
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Component(value="initLoginInfoUtil")
public class InitLoginInfoUtil {

	private static Logger log=Logger.getLogger(InitLoginInfoUtil.class); 
	
	@Autowired
	private OrgService orgService;
	
	@Autowired
	private PositionService positionService;
	
	public static InitLoginInfoUtil getInstance(){
		return ApplicationContextAware.getApplicationContext().getBean("initLoginInfoUtil", InitLoginInfoUtil.class);
	}
	/**
	 * 获取request
	 * @return
	 */
	private HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		
	}

	/**
	 * 获取session
	 * @return
	 */
	private HttpSession getSession() {
		return ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest().getSession();
	}
	/**
	 * 初始化登录用户信息
	 * @param person
	 * @throws NoPermissionException 
	 */
	public String initLoginUserInfo(HttpServletResponse response,Person person) throws NoPermissionException{
		Map<String,Object> info = new HashMap<String,Object>();
		//设置当前数据源
		getSession().setAttribute("currentDataSource", DataSourceHolder.getDataSource());
		DataSourceHolder.setDataSource((String) getSession().getAttribute("currentDataSource"));
		//设置登录系统的IP
		person.setCurrentLoginIp(getIpAddr(getRequest()));
		//设置当前用户
		SystemUtil.setCurrentUser(person);
		
		info.put(LoginInfoUtil.KEYSET.CURRENTUSER, person);
		info.put(LoginInfoUtil.KEYSET.CURRENTDATASOURCE, DataSourceHolder.getDataSource());
		long begin2 = System.currentTimeMillis();
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("person", person.getId());
		param.put("primary", 1);
		List<PersonPosition> pps = QueryExecutorImpl.getInstance().execQuery("com.wuyizhiye.basedata.person.dao.PersonPositionDao.select", param, PersonPosition.class);
		if(pps.size()>0){
			setSessionInfo(person, pps.get(0),info);
			//设置用户皮肤
			setUserSkinInfo(person,info);
		}else{
			getSession().invalidate();
			throw new NoPermissionException("没有任职信息，不允许登录");
		}
		getSession().setAttribute("currentUser", person);
		Long time2 = System.currentTimeMillis() - begin2;
		BigDecimal second2 = new BigDecimal(time2).divide(new BigDecimal("1000.000")).setScale(3);
		log.error("获取cu:"+second2+"秒");
		//记录操作日志
		String qhd=getRequest().getHeader("user-agent");
		String loginStr = "登录";
		if(qhd!=null && qhd.indexOf("Windows")<0){
			loginStr = "移动设备_登录";
		}
		OperationLogUtil.saveOperationLog(getRequest().getRequestURI(), loginStr , null, null);
		long begin1 = System.currentTimeMillis();
		loadPersonalPermission(info);
		Long time1 = System.currentTimeMillis() - begin1;
		BigDecimal second1 = new BigDecimal(time1).divide(new BigDecimal("1000.000")).setScale(3);
		log.error("获取cu:"+second1+"秒");
		//如果有并且人员信息一致沿用之前的cookie
		String pkcookie = getCookieValue(LoginInfoUtil.CLIENT_LOGIN_INFO, getRequest());
		String primaryKey = "";
		if(StringUtils.isNotNull(pkcookie)){
			if(person.getId().equals(pkcookie.substring(pkcookie.lastIndexOf("|")+1))){
				primaryKey = pkcookie;
			}
		}
		if(StringUtils.isEmpty(primaryKey)){
			primaryKey = 
				SystemUtil.getCustomerOnlySign()+"|"+(isMobileDevice()?LoginTypeEnum.MOBILE:LoginTypeEnum.PC)+DateUtil.getCurDate().getTime()+"|"+person.getId();
		}
		Cookie cookie = new Cookie(LoginInfoUtil.CLIENT_LOGIN_INFO, primaryKey);
		cookie.setPath("/");
		cookie.setMaxAge(-1);
		response.addCookie(cookie);
		
		info.put(LoginInfoUtil.KEYSET.BROWSER, getSession().getAttribute("browser"));
		info.put(LoginInfoUtil.KEYSET.SIGNATURE, getSession().getAttribute("signatureKey"));
		info.put(LoginInfoUtil.KEYSET.SINGLESOURCE, getSession().getAttribute("singlesource"));
		SystemUtil.setCurrentRedisKey(primaryKey);
		this.initOnlinePerson(info);
		long begin = System.currentTimeMillis();
		RedisHolder.getRedisClient().addRedisInfo(TransCoderUtil.serialString(primaryKey), LoginInfoUtil.toMap(info),
				SystemUtil.getExpireTime());
		Long time = System.currentTimeMillis() - begin;
		BigDecimal second = new BigDecimal(time).divide(new BigDecimal("1000.000")).setScale(3);
		log.error("redis保存人员信息耗时:"+second+"秒");
		SystemUtil.setCurrentRedisKey(primaryKey);
		SystemUtil.setCurrentSignature((String) getSession().getAttribute("signatureKey"));
		return primaryKey;
	}
	
	
	private void setSessionInfo(Person person,PersonPosition personPosition,Map<String,Object> info){
		Org org = orgService.getEntityById(personPosition.getPosition().getBelongOrg().getId());
		Position position = positionService.getEntityById(personPosition.getPosition().getId());
		position.setBelongOrg(org);
		/*if(org.getControlUnit()!=null){
			//当前CU
			Org cu = orgService.getEntityById(org.getControlUnit().getId());
			getSession().setAttribute("currentCU", cu);
			SystemUtil.setCurrentControlUnit(cu);
		}*/
		
		/*****放了当前CU  孙海涛   2014-11-03 start ****/
		Org cu = OrgUtils.getCUByOrg(org.getId());
		getSession().setAttribute("currentCU", cu);
		SystemUtil.setCurrentControlUnit(cu);
		/************** 2014-11-03 end*************/
		
		
		/*****放了岗位 added 2014-07-15 start ****/
		Person currentPerson = SystemUtil.getCurrentUser();
		currentPerson.setPersonPosition(personPosition);
		SystemUtil.setCurrentUser(currentPerson);
		/************** added 2014-07=15 end*************/
		
		
		getSession().setAttribute("currentTime", new Date());
		//当前职位
		getSession().setAttribute("currentPosition", position);
		SystemUtil.setCurrentPosition(position);
		//当前组织
		getSession().setAttribute("currentOrg", org);
		SystemUtil.setCurrentOrg(org);
		//用户类型
		UserTypeEnum ute = UserTypeUtil.getUserType(person, position);
		getSession().setAttribute("currentUserType", ute);
		SystemUtil.setCurrentUserType(ute);
		getSession().setAttribute("loginIP", getRequest().getRemoteAddr());
		
		info.put(LoginInfoUtil.KEYSET.CURRENTUSER, currentPerson);
		info.put(LoginInfoUtil.KEYSET.CURRENTCU, cu);
		info.put(LoginInfoUtil.KEYSET.CURRENTORG, org);
		info.put(LoginInfoUtil.KEYSET.CURRENTPOSITION, position);
		info.put(LoginInfoUtil.KEYSET.CURRENTUSERTYPE, null==ute?null:ute.getValue());
	}
	
	/**
	 * 设置用户皮肤
	 * @param person
	 */
	private void setUserSkinInfo(Person person,Map<String,Object> info){
		//系统默认皮肤
		String sysSkin = SystemConfig.getParameter("skinName");
		//获取用户自定义皮肤
		Map<String,Object> skinParam = new HashMap<String,Object>();
		skinParam.put("personId", person.getId());
		List<Skin> skinList = QueryExecutorImpl.getInstance().execQuery(Skin.MAPPER + ".select", skinParam, Skin.class);
		Skin skin = null ;
		if(skinList!=null && skinList.size() > 0){
			skin = skinList.get(0) ;
		}
		if(!StringUtils.isEmpty(sysSkin)){
			getSession().setAttribute("skinName",sysSkin);
		}else{
			getSession().setAttribute("skinName",skin == null ? "styleBlue" : skin.getSkinName() );
		}
		info.put(LoginInfoUtil.KEYSET.CURRENTSKIN, getSession().getAttribute("skinName"));
	}
	
	@SuppressWarnings("rawtypes")
	private void loadPersonalPermission(Map<String,Object> info){
		//权限集
		Set<String> personPermission= new HashSet<String>();
		Map<String,String> permissionMap = new HashMap<String, String>();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("person", SystemUtil.getCurrentUser().getId());
		param.put("position", SystemUtil.getCurrentPosition().getId());
		List<Map> pms = QueryExecutorImpl.getInstance().execQuery("com.wuyizhiye.basedata.permission.dao.PersonPermissionDao.getPersonPermission", param, Map.class);
		for(Map m : pms){
			if(m.get("permissionItemMethod")!= null && m.get("permissionItemId")!=null){
				permissionMap.put(m.get("permissionItemId").toString(), m.get("permissionItemMethod").toString());
				personPermission.add(m.get("permissionItemMethod").toString());
			}
		}
		List<String> pplist = new ArrayList<String>(personPermission);
		getSession().setAttribute(LoginInfoUtil.KEYSET.PERSONALPERMISSIONMAP, permissionMap);
		getSession().setAttribute(LoginInfoUtil.KEYSET.PERSONALPERMISSION, pplist);
		info.put(LoginInfoUtil.KEYSET.PERSONALPERMISSION, pplist);
		info.put(LoginInfoUtil.KEYSET.PERSONALPERMISSIONMAP, permissionMap);
	}
	
	private void initOnlinePerson(Map<String,Object> info){
		OnlineUser user = new OnlineUser();
		user.setId(SystemUtil.getCurrentUser().getId());
		user.setNumber(SystemUtil.getCurrentUser().getNumber());
		user.setUserName(SystemUtil.getCurrentUser().getName());
		Org org =SystemUtil.getCurrentOrg();
		user.setOrgName(null!=org?org.getName():"");
		Position position = SystemUtil.getCurrentPosition();
		user.setPositionName(null!=position?position.getName():"");
		user.setLoginDate(DateUtil.convertDateToStr(new Date(),"MM/dd HH:mm"));
		user.setSessionId(SystemUtil.getCurrentRedisKey());
		user.setControlUnit(SystemUtil.getCurrentControlUnit());
		//user.setLoginIp(sess.getAttribute(Constants.CURRENT_IP).toString());
		user.setPositionNum(null!=position?position.getNumber():"");
		user.setShortNum(SystemUtil.getCurrentUser().getShortNumber());
		user.setPhoto(SystemUtil.getCurrentUser().getPhoto());
		if(isMobileDevice()){
			user.setLoginType(LoginTypeEnum.MOBILE);
		}else{
			user.setLoginType(LoginTypeEnum.PC);
		}
		info.put(LoginInfoUtil.KEYSET.CURRENTONLINEINFO, user);
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

	protected boolean isMobileDevice(){
		String qhd=getRequest().getHeader("user-agent");
		if(qhd!=null){
			if(qhd.indexOf("Android")>-1){
				return true;
			}else if(qhd.indexOf("iPhone")>-1){
				return true;
			}else if(qhd.indexOf("iPad")>-1){
				return true;
			}else if(qhd.indexOf("Windows")<0 && qhd.indexOf("Linux")<0){//根据请求的客户端信息判断 如果是非PC登录
				return true;
			}
		}
		return false;
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
}
