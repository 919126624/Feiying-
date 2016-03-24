package com.wuyizhiye.framework.listener;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.wuyizhiye.base.dao.QueryExecutor;
import com.wuyizhiye.base.module.enums.ModuleEnum;
import com.wuyizhiye.base.module.model.ModuleLic;
import com.wuyizhiye.base.spring.ApplicationContextAware;
import com.wuyizhiye.base.spring.DataSourceHolder;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.redis.dao.RedisDao;
import com.wuyizhiye.basedata.util.LicenseInfo;
import com.wuyizhiye.basedata.util.RedisHolder;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.basedata.util.Validate;
import com.wuyizhiye.framework.redis.LoginInfoUtil;

/**
 * @ClassName OnlineListener
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 * 
 * @edit 添加空值判断
 * @author longxiaoqiong
 * @date 2015-12-25
 * @content Hashtable中for循环中对象user为空判断
 */
public class OnlineListener implements HttpSessionListener,
		HttpSessionAttributeListener {

	//public static Hashtable<String,OnlineUser> onLineUsers;
	//public static Hashtable<String,OnlineUser> onLineUsersForMobile;//移动端在线人数 
	
	public static Map<String,Hashtable<String,OnlineUser>> onLineUsersMap ;
	public static Map<String,Hashtable<String,OnlineUser>> onLineUsersForMobileMap ;
	
	private static Logger log=Logger.getLogger(OnlineListener.class); 
	
	public OnlineListener(){
		if(onLineUsersMap == null){
			onLineUsersMap = new HashMap<String, Hashtable<String,OnlineUser>>();
		}
		if(onLineUsersForMobileMap == null){
			onLineUsersForMobileMap = new HashMap<String, Hashtable<String,OnlineUser>>();
		}
	}
	
	public static Hashtable<String,OnlineUser> getOnLineUsers(){
		Hashtable<String,OnlineUser> c = null;
		RedisDao redisdao = ApplicationContextAware.getApplicationContext().getBean(RedisDao.class);
		if(redisdao.useRedis()){
			c = new Hashtable<String, OnlineUser>();
			//long begin = System.currentTimeMillis();
			List<OnlineUser> onPclist =
				redisdao.getByteListData(SystemUtil.getCustomerOnlySign()+"|"+LoginTypeEnum.PC+"*",LoginInfoUtil.KEYSET.CURRENTONLINEINFO, OnlineUser.class);
			//Long time = System.currentTimeMillis() - begin;
			//BigDecimal second = new BigDecimal(time).divide(new BigDecimal("1000.000")).setScale(3);
			//log.error("redis取出所有在线人员信息耗时:"+second+"秒");
			for(OnlineUser u : onPclist){
				if(u!=null){
					c.put(u.getId(), u);
				}
			}
		}else{
			c  = onLineUsersMap.get(DataSourceHolder.getDataSource()) ;
		}	
		if (c == null ){
			c = new Hashtable<String, OnlineUser>();
		}
		return c;
	}
	
	public static Hashtable<String,OnlineUser> getOnLineUsersForMobile(){
		Hashtable<String,OnlineUser> c = null;
		RedisDao redisdao = ApplicationContextAware.getApplicationContext().getBean(RedisDao.class);
		if(redisdao.useRedis()){
			c = new Hashtable<String, OnlineUser>();
			List<OnlineUser> onMobilelist =
				redisdao.getByteListData(SystemUtil.getCustomerOnlySign()+"|"+LoginTypeEnum.MOBILE+"*",LoginInfoUtil.KEYSET.CURRENTONLINEINFO, OnlineUser.class);
				for(OnlineUser u : onMobilelist){
					c.put(u.getId(), u);
				}
		}else{
			c  = onLineUsersForMobileMap.get(DataSourceHolder.getDataSource()) ;
		}	
		if (c == null ){
			c = new Hashtable<String, OnlineUser>();
		}
		return c;
	}
	
	/**
	 * 添加 修改在线用户信息
	 * @param currentPerson
	 */
	private void addOnlineUser(Person currentPerson,HttpSession sess){
		OnlineUser user = null;
		String qhd=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("user-agent");
		if(isMobileDevice()){//根据请求的客户端信息判断 如果是非PC登录
			Hashtable<String,OnlineUser> onLineUsersForMobile = new Hashtable<String, OnlineUser>() ;
			if(onLineUsersForMobileMap.get(DataSourceHolder.getDataSource()) != null){
				onLineUsersForMobile = onLineUsersForMobileMap.get(DataSourceHolder.getDataSource()) ;
			}
			if(onLineUsersForMobile.containsKey(currentPerson.getId())){
				user = onLineUsersForMobile.get(currentPerson.getId());			
				user.setLoginDate(DateUtil.convertDateToStr(new Date(),"MM/dd HH:mm"));//存在则修改登录时间
				//登陆将之前session注销
				//HttpSession oldsession = LocalSessionContext.getSession(user.getSessionId());
				//若为特殊登陆则不注销之前sess
				//if(null==sess.getAttribute("specialLogin")&&null!=oldsession)
				//{
				//oldsession.invalidate();
				//user.setLoginRemove("yes");
				//}
				user.setSessionId(sess.getId());
//				user.setIsOnline("yes");
				//user.setLoginIp(sess.getAttribute(Constants.CURRENT_IP).toString());
				//user.setPositionNum(sess.getAttribute("PositionType").toString());  
				onLineUsersForMobile.put(currentPerson.getId(), user); 
				onLineUsersForMobileMap.put(DataSourceHolder.getDataSource(), onLineUsersForMobile);
			}else {
				//增加 在线用户
				user = new OnlineUser();
				user.setId(currentPerson.getId());
				user.setNumber(currentPerson.getNumber());
				user.setUserName(currentPerson.getName());
				Org org = (Org)sess.getAttribute("currentOrg");
				user.setOrgName(null!=org?org.getName():"");
				Position position = (Position)sess.getAttribute("currentPosition");
				user.setPositionName(null!=position?position.getName():"");
				user.setLoginDate(DateUtil.convertDateToStr(new Date(),"MM/dd HH:mm"));
				user.setSessionId(sess.getId());
				//user.setLoginIp(sess.getAttribute(Constants.CURRENT_IP).toString());
				user.setPositionNum(null!=position?position.getNumber():"");
				user.setShortNum(currentPerson.getShortNumber());
				user.setPhoto(currentPerson.getPhoto());
//				user.setIsOnline("yes"); 
				user.setLoginType(LoginTypeEnum.MOBILE);//登陆类型为 移动设备登陆
				onLineUsersForMobile.put(currentPerson.getId(), user);
				onLineUsersForMobileMap.put(DataSourceHolder.getDataSource(), onLineUsersForMobile);
				Validate.addMobileOnLineSize(onLineUsersForMobile.size(),sess);
			}
		}else{
			Hashtable<String,OnlineUser> onLineUsers = new Hashtable<String, OnlineUser>() ;
			if(onLineUsersMap.get(DataSourceHolder.getDataSource()) != null){
				onLineUsers = onLineUsersMap.get(DataSourceHolder.getDataSource()) ;
			}
			if(onLineUsers.containsKey(currentPerson.getId())){
				user = onLineUsers.get(currentPerson.getId());			
				user.setLoginDate(DateUtil.convertDateToStr(new Date(),"MM/dd HH:mm"));//存在则修改登录时间
				//登陆将之前session注销
				//HttpSession oldsession = LocalSessionContext.getSession(user.getSessionId());
				//若为特殊登陆则不注销之前sess
				//if(null==sess.getAttribute("specialLogin")&&null!=oldsession)
				//{
				//oldsession.invalidate();
				//user.setLoginRemove("yes");
				//}
				user.setSessionId(sess.getId());
				user.setControlUnit(SystemUtil.getCurrentControlUnit());
//				user.setIsOnline("yes");
				//user.setLoginIp(sess.getAttribute(Constants.CURRENT_IP).toString());
				//user.setPositionNum(sess.getAttribute("PositionType").toString());  
				onLineUsers.put(currentPerson.getId(), user); 
				onLineUsersMap.put(DataSourceHolder.getDataSource(), onLineUsers);
			}else {
				//增加 在线用户
				user = new OnlineUser();
				user.setId(currentPerson.getId());
				user.setNumber(currentPerson.getNumber());
				user.setUserName(currentPerson.getName());
				Org org = (Org)sess.getAttribute("currentOrg");
				user.setOrgName(null!=org?org.getName():"");
				Position position = (Position)sess.getAttribute("currentPosition");
				user.setPositionName(null!=position?position.getName():"");
				user.setLoginDate(DateUtil.convertDateToStr(new Date(),"MM/dd HH:mm"));
				user.setSessionId(sess.getId());
				user.setControlUnit(SystemUtil.getCurrentControlUnit());
				//user.setLoginIp(sess.getAttribute(Constants.CURRENT_IP).toString());
				user.setPositionNum(null!=position?position.getNumber():"");
				user.setShortNum(currentPerson.getShortNumber());
				user.setPhoto(currentPerson.getPhoto());
//				user.setIsOnline("yes"); 
				user.setLoginType(LoginTypeEnum.PC);//登陆类型为 计算机登陆
				onLineUsers.put(currentPerson.getId(), user);
				onLineUsersMap.put(DataSourceHolder.getDataSource(), onLineUsers);
				//Validate.onLineUserSize += 1 ;
//				Validate.addOnLineSize(onLineUsers.size(),sess);
			}
			
		}
	}
	public void attributeAdded(HttpSessionBindingEvent se) {
		if(se.getName().equals("currentUser"))
		{
		Person currentPerson = (Person)se.getSession().getAttribute("currentUser");
		if(currentPerson!=null){
			addOnlineUser(currentPerson,se.getSession());
		}
		}
	}
	
	public void attributeReplaced(HttpSessionBindingEvent se) {		
		
		if(se.getName().equals("currentUser"))
		{
		Person oldPerson = null;
		if(null!=se.getValue())
			oldPerson = (Person)se.getValue();
		Person currentPerson = (Person)se.getSession().getAttribute("currentUser");
		//当只是改变登陆人属性时不操作,即只当当前人员改变时才进行人员替换操作
			if (StringUtils.isNotNull(oldPerson)
					&& StringUtils.isNotNull(currentPerson)
					&& !oldPerson.getId().equals(currentPerson.getId())) {

//				getOnLineUsers().remove(oldPerson.getId());
//				getOnLineUsersForMobile().remove(oldPerson.getId());
				//因为上面的方式，当浏览器退出的时候tomcat去调用注销有问题，因为得不到数据源，所以数据删除不了 modified by taking.wang 2014-10-18 16:17
				removeUserFromSession(oldPerson.getId(), LoginTypeEnum.PC);
				removeUserFromSession(oldPerson.getId(), LoginTypeEnum.MOBILE);
				log.debug(oldPerson.getName() + "已被替换");
				//if(Validate.onLineUserSize > 0){
				//	Validate.onLineUserSize -= 1 ;
				//}
				
				addOnlineUser(currentPerson, se.getSession());
				log.debug(currentPerson.getName() + "登录进来");

			}
		}
	}

	public void attributeRemoved(HttpSessionBindingEvent se) {
		if(se.getName().equals("currentUser"))
		{			
			Person p = (Person)se.getValue();
			if(null!=p){
//				getOnLineUsers().remove(p.getId());
//				getOnLineUsersForMobile().remove(p.getId());
				//因为上面的方式，当浏览器退出的时候tomcat去调用注销有问题，因为得不到数据源，所以数据删除不了 modified by taking.wang 2014-10-18 16:17
				removeUserFromSession(p.getId(), LoginTypeEnum.PC);
				removeUserFromSession(p.getId(), LoginTypeEnum.MOBILE);
				log.debug(p.getName()+"注销");
				//if(Validate.onLineUserSize > 0){
				//	Validate.onLineUserSize -= 1 ;
				//}
			
			}
		}
	}
	
	/**
	 * 从session中移除数据
	 * @param personId
	 * @param loginType
	 */
	private void removeUserFromSession(String personId,LoginTypeEnum loginType){
		if(null != onLineUsersMap && onLineUsersMap.size() >0){
			if(null != DataSourceHolder.getDataSource()){	//如果当前数据源不为空的话，则和正常的删除一样
				getOnLineUsers().remove(personId);
				getOnLineUsersForMobile().remove(personId);
			} else {
				
				for(Map.Entry<String, Hashtable<String,OnlineUser>> entry : onLineUsersMap.entrySet())
				{
					if(loginType.PC.toString().equalsIgnoreCase(loginType.toString())){
						this.removeOnlineUser(personId,onLineUsersMap);
					} else {
						this.removeOnlineUser(personId,onLineUsersForMobileMap);
					}
				} 
			}
		}
	}
	
	/**
	 * 从map中移除数据
	 * @param onLineUsersMap
	 */
	public void removeOnlineUser(String personId,Map<String,Hashtable<String,OnlineUser>> onLineUsersMap){
		
		boolean jumpFlag = false;
		for(Map.Entry<String, Hashtable<String,OnlineUser>> entry : onLineUsersMap.entrySet())
		{
			Hashtable<String, OnlineUser> onlineUserTableData = entry.getValue();
			Hashtable<String, OnlineUser> finalyData = onlineUserTableData;
			for(String key : onlineUserTableData.keySet()){
				if(personId.equalsIgnoreCase(key)){
					finalyData.remove(personId);
					
					/*********************清理CMCT的已注销人员占线start added by taking.wang 2014-10-18*********************/
					QueryExecutor queryExecutor = (QueryExecutor) ApplicationContextAware.getApplicationContext().getBean("queryExecutor");
					Map<ModuleEnum,ModuleLic> modules = Validate.getCurrPerms();
					//包含了通讯模块，话务下线
					if(modules!=null && modules.containsKey(ModuleEnum.CMCT)){
						Map<String,Object> param = new HashMap<String,Object>();
						param.put("currUserId", personId);
						queryExecutor.executeUpdate("com.wuyizhiye.cmct.phone.dao.PhoneMemberDao.cleanCurrUser", param);
					}
					/***********************清理CMCT的已注销人员占线end***************/
					
					jumpFlag= true;
					break;
				}
			}
			if(jumpFlag){
				entry.setValue(finalyData);
				break;
			}
		} 
	}
	

	public void sessionCreated(HttpSessionEvent se) {
		LocalSessionContext.AddSession(se.getSession());
	}

	public void sessionDestroyed(HttpSessionEvent se) {		
		Person currentPerson = (Person)se.getSession().getAttribute("currentUser");
		if(currentPerson!=null){
			log.debug((null==currentPerson?"":currentPerson.getName())+"进入session注销事件");
//			getOnLineUsers().remove(currentPerson.getId());
//			getOnLineUsersForMobile().remove(currentPerson.getId());
			//因为上面的方式，当浏览器退出的时候tomcat去调用注销有问题，因为得不到数据源，所以数据删除不了 modified by taking.wang 2014-10-18 16:17
			removeUserFromSession(currentPerson.getId(), LoginTypeEnum.PC);
			removeUserFromSession(currentPerson.getId(), LoginTypeEnum.MOBILE);
			//if(Validate.onLineUserSize > 0){
			//	Validate.onLineUserSize -= 1 ;
			//}
	
		}
		
		LocalSessionContext.DelSession(se.getSession());	
	}
	
	protected boolean isMobileDevice(){
		String qhd=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("user-agent");
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
	
	public static Integer getCurrPcOnLine(){
		RedisDao redisdao = RedisHolder.getRedisClient();
		if(redisdao.useRedis()){
			 Set<String> set =
			 redisdao.getListKey(SystemUtil.getCustomerOnlySign()+"|"+LoginTypeEnum.PC+"*");
			 Set<String> pset = new HashSet<String>();
			 for(String str : set){
				 pset.add(str.substring(str.lastIndexOf("|")+1));
			 }
			 return pset.size();
		}else{
			Hashtable<String,OnlineUser> c  = getOnLineUsers();
			return c.size();
		}
	}
	
	public static Integer getCurrMobileOnLine(){
		RedisDao redisdao = RedisHolder.getRedisClient();
		if(redisdao.useRedis()){
			 Set<String> set =
			redisdao.getListKey(SystemUtil.getCustomerOnlySign()+"|"+LoginTypeEnum.MOBILE+"*");
			 Set<String> pset = new HashSet<String>();
			 for(String str : set){
				 pset.add(str.substring(str.lastIndexOf("|")+1));
			 }
			 return pset.size();
		}else{
			Hashtable<String,OnlineUser> c  = getOnLineUsersForMobile();
			return c.size();
		}
	}
	
	public static boolean exceedMobileLicense(){
		LicenseInfo li = Validate.getCurrLicenseInfo();
		if(null==li) {
			li = new LicenseInfo();
			li.setMobileallowsize(0);
			li.setPcallowsize(0);
		}
		int mobileallow = li.getMobileallowsize();
		if((mobileallow==0&&getCurrMobileOnLine()>0)
				||(mobileallow>0&&getCurrMobileOnLine()>=mobileallow)){
			return true;
		}
		return false;
	}
	
	public static boolean exceedLicense(){
		LicenseInfo li = Validate.getCurrLicenseInfo();
		if(null==li) {
			li = new LicenseInfo();
			li.setMobileallowsize(0);
			li.setPcallowsize(0);
		}
		int pcallow = li.getPcallowsize();
		if((pcallow==0&&getCurrPcOnLine()>0)
				||(pcallow>0&&getCurrPcOnLine()>=pcallow)){
			return true;
		}		
		return false;
	}
	
	public static void inValidateUser(String userid){
		RedisDao redisdao = ApplicationContextAware.getApplicationContext().getBean(RedisDao.class);
		if(!redisdao.useRedis()){
			Hashtable<String, OnlineUser> tab = OnlineListener.getOnLineUsers();
			OnlineUser user = tab.get(userid);
			inValidateUser(user);
		}else{
			Hashtable<String, OnlineUser> tab = OnlineListener.getOnLineUsers();
			Hashtable<String, OnlineUser> mobiletab = OnlineListener.getOnLineUsersForMobile();
			OnlineUser user = tab.get(userid);
			inValidateUser(user);
			 user = mobiletab.get(userid);
			 inValidateUser(user);
		}
		
	}
	
	public static void inValidateUser(OnlineUser user){
		RedisDao redisdao = ApplicationContextAware.getApplicationContext().getBean(RedisDao.class);
		if(!redisdao.useRedis()){
			if(user!=null){
				LocalSessionContext.getSession(user.getSessionId()).invalidate();
			}
		}else{
			if(user!=null){
			String rediskey = user.getSessionId();
			redisdao.expireByte(rediskey, 0);
			redisdao.expire(SystemUtil.getCustomerOnlySign()+"|"+rediskey.substring(rediskey.lastIndexOf("|")+1), 0);
			}
		}
	}
	
	public static void inValidateAllUser(){
		Hashtable<String, OnlineUser> pcOnline = OnlineListener
		.getOnLineUsers();
		Hashtable<String, OnlineUser> mOnline = OnlineListener
				.getOnLineUsersForMobile();
		Set<String> pckeys = pcOnline.keySet();
		Set<String> mkeys = mOnline.keySet();
		for (String key : pckeys) {
			OnlineUser user = pcOnline.get(key);
			inValidateUser(user);
		}
		for (String key : mkeys) {
			OnlineUser user = pcOnline.get(key);
			inValidateUser(user);
		}
	}
	
}
