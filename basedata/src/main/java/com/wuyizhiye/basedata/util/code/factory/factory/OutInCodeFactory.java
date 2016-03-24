package com.wuyizhiye.basedata.util.code.factory.factory;

import com.wuyizhiye.basedata.util.code.factory.CodeFactory;
import com.wuyizhiye.basedata.util.code.factory.CodeI;
import com.wuyizhiye.basedata.util.code.factory.impl.OutInCodeImpl;

public  class OutInCodeFactory extends CodeFactory {

	private static OutInCodeFactory outInCodeFactory=null;
	public static OutInCodeFactory getInstance() {  
		if(outInCodeFactory==null){
			outInCodeFactory=new OutInCodeFactory();
		}
		return outInCodeFactory;
		
	}
	
	public CodeI create(){
		return OutInCodeImpl.getInstance();
	}
}
