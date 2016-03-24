package com.wuyizhiye.basedata.util.code.factory.impl;

import java.util.List;

import com.wuyizhiye.base.dao.QueryExecutor;
import com.wuyizhiye.base.spring.ApplicationContextAware;
import com.wuyizhiye.base.util.SystemConfig;
import com.wuyizhiye.basedata.util.code.factory.CodeI;

/**
 * 订单编码生成工具
 * @author zhugj
 *
 */
public class GoodsCodeImpl implements CodeI{  

	private static GoodsCodeImpl goodsCodeImpl=null;
	public static GoodsCodeImpl getInstance() {  
		if(goodsCodeImpl==null){
			goodsCodeImpl=new GoodsCodeImpl();
		}
		return goodsCodeImpl;
		
	}
    public static long numberGoods=0;
    public static String machineId=null;
    static{
    	if(machineId == null || "".equals(machineId)){
    		machineId = SystemConfig.getParameter("machine.id");
    	}
    }

	private static QueryExecutor getQueryExecutor() {
		return ApplicationContextAware.getApplicationContext().getBean("queryExecutor", QueryExecutor.class);
	}
    /** 
     * 用户生成商品编号 
     * @return 
     */  
    public  synchronized String getCode() {
    	if(numberGoods==0){
			int temp=0;//默认为0
			try{
	    		String numberMax=getQueryExecutor().execOneEntity("com.wuyizhiye.ebbase.goods.dao.GoodsDao.getGoodsNumberMax", null, String.class);
				int numMax =Integer.parseInt(numberMax.replace("GD", ""));//截取字符GD并且转Integer
				temp=numMax;
				if(temp>0){
					numberGoods=temp;
				}else{
					numberGoods++;
				}
			}catch (Exception e) {
	    		List<String> numberList=getQueryExecutor().execQuery("com.wuyizhiye.ebbase.goods.dao.GoodsDao.getGoodsNumber", null, String.class);
	    		if(numberList==null || numberList.size()==0){
	    			numberGoods++;
	    		}else{
	    			for (int i=0;i<numberList.size();i++) {
						try{
							int num =Integer.parseInt(numberList.get(i).replace("GD", ""));//截取字符GD并且转Integer
	
							if(num>temp){
								temp=num;
							}
						}catch (NumberFormatException e1) {
							if((i+1)==numberList.size() && temp==0){//如果抛异常(转换错误)知道集合最后temp还是0,就 默认+1
								temp++;
							}
						}
					}
	    			if(temp>0){
	    				numberGoods=temp;
	    			}else{
	    				numberGoods++;
	    			}
	    		}
			}
    	}
		numberGoods++;
    	StringBuffer numberBuffer=new StringBuffer("GD");
    	if(machineId!=null){
    		numberBuffer.append(machineId);
    	}
    	String numGoodsString=String.valueOf(numberGoods);
    	if(numGoodsString.length()<6){
    		int numleng=6-numGoodsString.length();
    		for(int i=0;i<numleng;i++){
    			numberBuffer.append("0");
    		}
    		numberBuffer.append(numGoodsString);
    	}else{
    		numberBuffer.append(numGoodsString);
    	}

        return numberBuffer.toString();  
    } 
}  
