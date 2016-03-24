package com.wuyizhiye.framework.listener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.wuyizhiye.basedata.person.model.Person;

/**
 * @ClassName WashCustomerListener
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
public class WashCustomerListener {

	public volatile static ConcurrentHashMap<String,WashCustomer> washCustomers;
	
	public volatile static ConcurrentHashMap<String,String> washMap;
	
	private static Logger log=Logger.getLogger(WashCustomerListener.class); 
	
	/**
	 * 
	 */
	static{
		if(washCustomers == null){
			 washCustomers = new ConcurrentHashMap<String,WashCustomer>();
			 washMap = new ConcurrentHashMap<String,String>();
		}
	}
	
	public WashCustomerListener(){
		if(washCustomers == null){
			 washCustomers = new ConcurrentHashMap<String,WashCustomer>();
			 washMap = new ConcurrentHashMap<String,String>();
		}
	}
	
	/**
	 * 添加 修改拉取客户占用数据
	 * @param currentPerson
	 * @throws Exception 
	 */
	public synchronized static void addCustomer(Person user,WashCustomer uiCustomer) throws Exception{
		if(washMap.containsKey(uiCustomer.getCustomerId())){
			log.error("同时取出同一位客户,报错："+uiCustomer.getCustomerId());
			throw new Exception("同时取出同一位客户,报错");
		}
		WashCustomer customer = null;
		if(washCustomers.containsKey(user.getId())){
			customer = washCustomers.get(user.getId());		
			if(customer!=null){
				//更新则去除之前的customer Id
				washMap.remove(customer.getCustomerId());			
				customer.setCustomerId(uiCustomer.getCustomerId());
				customer.setCustomerName(uiCustomer.getCustomerName());
				customer.setHangTime(new Date());																
			}else{
				customer.setUserId(user.getId());
				customer.setUserName(user.getName());
				customer.setCustomerId(uiCustomer.getCustomerId());
				customer.setCustomerName(uiCustomer.getCustomerName());
				customer.setHangTime(new Date());
			}
			washCustomers.put(user.getId(), customer);
			washMap.put(uiCustomer.getCustomerId(), "1");
			//log.error("重新占用客："+uiCustomer.getCustomerId());
		}else{
			//增加 占用新客户
			customer = new WashCustomer();
			customer.setUserId(user.getId());
			customer.setUserName(user.getName());
			customer.setCustomerId(uiCustomer.getCustomerId());
			customer.setCustomerName(uiCustomer.getCustomerName());
			customer.setHangTime(new Date());
			washCustomers.put(user.getId(), customer);
			washMap.put(uiCustomer.getCustomerId(), "1");
			//log.error("占用新客户："+uiCustomer.getCustomerId());
			
		}
	}
	
	/**
	 * 移除占用的客
	 * @param user
	 */
	public static void removeCustomer(Person user ){
		if(washCustomers!=null){
			if(null!=washCustomers.get(user.getId()))
				washMap.remove(washCustomers.get(user.getId()).getCustomerId());
			
			washCustomers.remove(user.getId());
			
		}
		log.error("客户被释放：userId="+user.getId());
	}
	
	/**
	 * 获取所有占用的客
	 * @return
	 */
	public static List<String> getAllCustomerId(){
		List<String> idList = new ArrayList<String>();
		for(String key : washMap.keySet()){
			idList.add(key);
		}
		return idList ;
	}
	
	/**
	 * 占用客中是否已经存在该客
	 * @param customerId
	 * @return
	 */
	public static boolean hasCustomer(String customerId){
		
		boolean hasFlag = washMap.containsKey(customerId);
		/*for(String key : washCustomers.keySet()){
			WashCustomer customer = washCustomers.get(key);
			if(customerId.equals(customer.getCustomerId())){
				hasFlag = true ;
				break ;
			}
		}*/
		if(hasFlag){
			log.error("==拉取出的客户已经被占用==：customerId="+customerId);
		}
		return hasFlag ;
		
	}
	
}
