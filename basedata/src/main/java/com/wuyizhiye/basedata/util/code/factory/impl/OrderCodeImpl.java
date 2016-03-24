package com.wuyizhiye.basedata.util.code.factory.impl;

import java.util.Calendar;
import java.util.UUID;

import com.wuyizhiye.base.util.SystemConfig;
import com.wuyizhiye.basedata.util.code.factory.CodeI;

/**
 * 订单编码生成工具
 * @author zhugj
 *
 */
public class OrderCodeImpl implements CodeI{  
	private static OrderCodeImpl orderCodeImpl=null;
	public static OrderCodeImpl getInstance() {  
		if(orderCodeImpl==null){
			orderCodeImpl=new OrderCodeImpl();
		}
		return orderCodeImpl;
		
	}
    public static String machineId=null;
    static{
    	if(machineId == null || "".equals(machineId)){
    		machineId = SystemConfig.getParameter("machine.id");
    	}
    }
    private static long value=0l;
      
  
    /** 
     * 生成订单编号 
     * @return 
     */  
    public  synchronized String getCode() { 
    	if(value==0){
    		value =Calendar.getInstance().getTimeInMillis()*100;
    	}else{
    		value++;
    	}
    	if(machineId==null){
    		return value+"";
    	}
        return machineId+""+value+"";  
        
    } 

    /** 
     * 一个用户在一分钟内可以下
     * 前缀是userID
     * @return 
     */  
    /** 
     * 通过uuid生成订单号
     * @return 
     */  
    public static String getOrderNoByUUID() {  
    	String machineId = "1";//集群号,如部署9台,可定义1到9对应
    	String OrderNo =
    			machineId+
    			UUID.randomUUID().toString().replaceAll("-", "");  
    	return OrderNo;
    }  
}  
