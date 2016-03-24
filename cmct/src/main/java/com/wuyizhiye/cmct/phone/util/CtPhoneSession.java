package com.wuyizhiye.cmct.phone.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.cmct.phone.model.ApiCallBack;

/**
 * @ClassName CtPhoneSession
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
public class CtPhoneSession {

	public volatile static ConcurrentHashMap<String,ApiCallBack> callBackMap;
	private static final long timeout = 1*60*60 ;//1个小时(单位秒)
	public static Logger log =Logger.getLogger(CtPhoneSession.class);
	static{
		if(callBackMap == null){
			callBackMap = new ConcurrentHashMap<String, ApiCallBack>();
		}
	}
	
	public synchronized static void add(ApiCallBack callBack){
				
		if(callBack == null || StringUtils.isEmpty(callBack.getvSessionsId())){
			return ;
		}
		
		callBack.setLastTime(DateUtil.getCurDate());
		if(null!=callBackMap && callBackMap.containsKey(callBack.getvSessionsId())){
			ApiCallBack cb = callBackMap.get(callBack.getvSessionsId());
			
			callBack.setStatusAnswCount(cb.getStatusAnswCount());//把上一个状态计数值设置进去
			callBack.setStatusRingCount(cb.getStatusRingCount());
		
			if("RINGING".equals(callBack.getvCallState())){
				if(callBack.getStatusRingCount()==null){
					callBack.setStatusRingCount(1);
				}else{
					if(callBack.getStatusRingCount()<=1){
						callBack.setStatusRingCount(callBack.getStatusRingCount()+1);
					}
				}
//				callBack.setStatusRingCount(callBack.getStatusRingCount()==null?1:callBack.getStatusRingCount()==2?callBack.getStatusRingCount():callBack.getStatusRingCount()+1);
			}
			if("ANSWERED".equals(callBack.getvCallState())){
				callBack.setStatusAnswCount(callBack.getStatusAnswCount()==null?1:callBack.getStatusAnswCount()+1);
			}
			callBackMap.put(callBack.getvSessionsId(), callBack);
			
		}else{//状态第一次进入
			if("RINGING".equals(callBack.getvCallState())){
				callBack.setStatusRingCount(1);
			}
			if("ANSWERED".equals(callBack.getvCallState())){
				callBack.setStatusAnswCount(1);
			}
			callBackMap.put(callBack.getvSessionsId(), callBack);
		}
	}
	
	public static ApiCallBack get(String sessionId){
		return callBackMap.get(sessionId);
	}
	
	public static boolean has(String sessionId){
		return callBackMap.containsKey(sessionId);
	}
	
	public static void remove(String sessionId){
		callBackMap.remove(sessionId);
	}
	
	public static void clearTimeOut(){
		Date now = DateUtil.getCurDate() ;
		try{
			ConcurrentHashMap<String, ApiCallBack> tempMap = (ConcurrentHashMap<String, ApiCallBack>) BeanUtils.cloneBean(callBackMap)  ;
			List<String> delList = new ArrayList<String>();
			for(String key : tempMap.keySet()){
				ApiCallBack cb = tempMap.get(key);
				if(cb == null || cb.getLastTime() == null ){
					delList.add(key);
				}
				if(DateUtil.getDifferSecound(now, cb.getLastTime()) >= timeout){
					delList.add(key);
				}
			}
			for(String delKey : delList){
				callBackMap.remove(delKey);
			}
			log.error("CtPhoneSession-date:"+DateUtil.convertDateToStr(new Date(), DateUtil.GENERAL_FORMHMS)+"-callBackSize:"+delList.size());
		}catch (Exception e) {
			//清理session异常
			e.printStackTrace();
		}
	}
	
	public static void test(){
		String[] sArr = new String[]{"send","linking","linked","hood","end"};
		long st = DateUtil.getCurDate().getTime();
		long et = DateUtil.getCurDate().getTime();
		if(callBackMap == null ){
			callBackMap = new ConcurrentHashMap<String, ApiCallBack>();
		}
		while((et - st) / 1000 < 60){
			long ss = (et - st ) / 1000 ;
			Random random = new Random();
			int k = random.nextInt(5);
			if(ss > 1){
				ApiCallBack acb = new ApiCallBack();
				acb.setvSessionsId("1000"+k);
				acb.setvCallState(sArr[k]);
				callBackMap.put("1000"+k, acb);
			}
			et = DateUtil.getCurDate().getTime();
		}
		
		for(String key : callBackMap.keySet()){
//			System.out.println("key:"+key+"  value:"+callBackMap.get(key).getvCallState());
		}
		
	}
	
	public static void sortMap(){
		List<Map.Entry<String, ApiCallBack>> callBacks = new ArrayList<Map.Entry<String, ApiCallBack>>(callBackMap.entrySet());  
		Collections.sort(callBacks, new Comparator<Map.Entry<String, ApiCallBack>>() {  
		    public int compare(Map.Entry<String, ApiCallBack> o1,  
		            Map.Entry<String, ApiCallBack> o2) {  
		        return ( Integer.parseInt(o1.getValue().getvStateTime())-Integer.parseInt(o2.getValue().getvStateTime()));  
		    }  
		});  
	}
}
