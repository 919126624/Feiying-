package com.wuyizhiye.basedata.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.alibaba.druid.pool.DruidDataSource;
import com.wuyizhiye.base.spring.ApplicationContextAware;
import com.wuyizhiye.base.spring.DataSourceHolder;
import com.wuyizhiye.base.spring.RoutingDataSource;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.base.util.SystemConfig;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.person.enums.UserTypeEnum;
import com.wuyizhiye.basedata.person.model.Person;

/**
 * @ClassName SystemUtil
 * @Description 系统工具类
 * @author li.biao
 * @date 2015-4-2
 */
public class SystemUtil {
	private static ThreadLocal<Person> currentUser = new ThreadLocal<Person>();
	private static ThreadLocal<Org> currentControlUnit = new ThreadLocal<Org>();
	private static ThreadLocal<Org> currentOrg = new ThreadLocal<Org>();
	private static ThreadLocal<Position> currentPosition = new ThreadLocal<Position>();
	private static ThreadLocal<UserTypeEnum> currentUserType = new ThreadLocal<UserTypeEnum>();
	private static ThreadLocal<String> base = new ThreadLocal<String>();
	private static ThreadLocal<String> imageBase = new ThreadLocal<String>();
	private static ThreadLocal<String> currentRedisKey = new ThreadLocal<String>();
	private static ThreadLocal<String> currentSignature = new ThreadLocal<String>();
	/**
	 * 获得系统访问根路径
	 * @return
	 */
	public static String getBase(){
		return base.get();
	}
	
	public static void setBase(String s){
		base.set(s);
	}
	
	/**
	 * 获得系统访问根路径
	 * @return
	 */
	public static String getImageBase(){
		return imageBase.get();
	}
	
	public static void setImageBase(String s){
		imageBase.set(s);
	}
	/**
	 * 获得系统当前人的redis key
	 * @return
	 */
	public static String getCurrentRedisKey(){
		return currentRedisKey.get();
	}
	
	public static void setCurrentRedisKey(String s){
		currentRedisKey.set(s);
	}
	
	/**
	 * 获取当前登录用户
	 * @return
	 */
	public static Person getCurrentUser(){
		return currentUser.get();
	}
	
	public static void setCurrentUser(Person user){
		currentUser.set(user);
	}
	
	public static void setCurrentControlUnit(Org org){
		currentControlUnit.set(org);
	}
	
	/**
	 * 获取当前组织单元
	 * @return
	 */
	public static Org getCurrentControlUnit(){
		return currentControlUnit.get();
	}
	
	public static void setCurrentOrg(Org org){
		currentOrg.set(org);
	}
	
	public static void setCurrentPosition(Position position){
		currentPosition.set(position);
	}
	
	/**
	 * 获取当前组织
	 * @return
	 */
	public static Org getCurrentOrg(){
		return currentOrg.get();
	}
	
	/**
	 * 获取当前职位
	 * @return
	 */
	public static Position getCurrentPosition(){
		return currentPosition.get();
	}
	
	/**
	 * 获取当前登录用户类型
	 * @return
	 */
	public static UserTypeEnum getCurrentUserType(){
		return currentUserType.get();
	}
	
	public static void setCurrentUserType(UserTypeEnum type){
		currentUserType.set(type);
	}
	
	public static String getCurrentSignature() {
		return currentSignature.get();
	}

	public static void setCurrentSignature(String signature) {
		currentSignature.set(signature);
	}

	/**
	 * 获取数据中心列表,按beanName排序
	 * @return
	 */
	public static Map<String, String> getDataSourceList(){
		ApplicationContext ctx = ApplicationContextAware.getApplicationContext();
		String[] beanNames = ctx.getBeanNamesForType(DruidDataSource.class);
		List<String> names = new ArrayList<String>();
		for(String n : beanNames){
			names.add(n);
		}
		Collections.sort(names);
		Map<String,String> ds = new LinkedHashMap<String, String>();
		for(String beanName : names){
			String label = SystemConfig.getParameter(beanName);
			ds.put(beanName, StringUtils.isEmpty(label)?beanName:label);
		}
		return ds;
	}
	
	/**
	 * 获取默认数据中心
	 * @return
	 */
	public static String getDefaultSource(){
		ApplicationContext ctx = ApplicationContextAware.getApplicationContext();
		RoutingDataSource rdata = ctx.getBean("dataSource",RoutingDataSource.class);		
		return rdata.getDefaultSourceKey();
	}
	/**
	 * 得到数据中心列表 返回list
	 */
	public static List<String> getDataSourceSingleList(){
		ApplicationContext ctx = ApplicationContextAware.getApplicationContext();
		String[] beanNames = ctx.getBeanNamesForType(DruidDataSource.class);
		List<String> names = new ArrayList<String>();
		for(String n : beanNames){
			names.add(n);
		}
		return names;
	}
	
	/**
	 * 拼接数据中心名+/作为图片上传路径,图片上传使用
	 * @return
	 */
	public  static String getCurrentDataCenter(){
		return DataSourceHolder.getDataSource()+"/";
	}
	
	public static String getImagePath(){
		return SystemConfig.getParameter("image_path")+getCurrentDataCenter();
	}
	
	public static String getAttachPath(){
		return SystemConfig.getParameter("attachment_path")+getCurrentDataCenter();
	}
	
	public static String getCustomerNumber(){
		//返回客户编码,如果没有找到license则返回机器码,防止报错
		return null==Validate.getCurrLicenseInfo()?
				Validate.getStaticSignature():Validate.getCurrLicenseInfo().getCustomerno();
	}
	
	public static String getCustomerOnlySign(){
		//数据中心+应用服务器ID
		return SystemConfig.getParameter("serverid")+DataSourceHolder.getDataSource();
	}
	
	public static int getExpireTime(){
		return 	StringUtils.isEmpty(SystemConfig.getParameter("expiretime"))?
				30:Integer.parseInt(SystemConfig.getParameter("expiretime"));
	}
	
	public static String getTomcatName(){
		//取倒数第二个
		String tomcatdir = System.getProperty("java.io.tmpdir");
		String[] ary = tomcatdir.split("/");
		return ary[ary.length-2];
	}

}
