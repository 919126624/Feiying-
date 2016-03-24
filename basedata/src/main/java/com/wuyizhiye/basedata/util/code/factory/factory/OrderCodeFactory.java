package com.wuyizhiye.basedata.util.code.factory.factory;

import com.wuyizhiye.basedata.util.code.factory.CodeFactory;
import com.wuyizhiye.basedata.util.code.factory.CodeI;
import com.wuyizhiye.basedata.util.code.factory.impl.OrderCodeImpl;

public  class OrderCodeFactory  extends CodeFactory {

	private static OrderCodeFactory orderCodeFactory=null;
	public static OrderCodeFactory getInstance() {  
		if(orderCodeFactory==null){
			orderCodeFactory=new OrderCodeFactory();
		}
		return orderCodeFactory;
		
	}
	public CodeI create(){
		return OrderCodeImpl.getInstance();
	}
}
