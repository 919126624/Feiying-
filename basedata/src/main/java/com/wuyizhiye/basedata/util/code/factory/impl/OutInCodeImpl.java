package com.wuyizhiye.basedata.util.code.factory.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import com.wuyizhiye.base.util.SystemConfig;
import com.wuyizhiye.basedata.util.code.factory.CodeI;

/**
 * 订单编码生成工具
 * @author zhugj
 *
 */
public class OutInCodeImpl implements CodeI{  

	public OutInCodeImpl(){
	}
	private static OutInCodeImpl outInCodeImpl=null;
	public static OutInCodeImpl getInstance() {  
		if(outInCodeImpl==null){
			outInCodeImpl=new OutInCodeImpl();
		}
		return outInCodeImpl;
		
	}
    public static String machineId=null;
    static{
    	if(machineId == null || "".equals(machineId)){
    		machineId = SystemConfig.getParameter("machine.id");
    	}
    }

    private static long value=0l;
	public  synchronized  String getCode(){

    	if(value==0){
    		value =Calendar.getInstance().getTimeInMillis()*10;
    	}else{
    		value++;
    	}
    	if(machineId==null){
    		return value+"";
    	}
        return machineId+""+value+"";  
	}
	
}  
