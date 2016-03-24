package com.wuyizhiye.basedata.util.code.factory.factory;

import com.wuyizhiye.basedata.util.code.factory.CodeFactory;
import com.wuyizhiye.basedata.util.code.factory.CodeI;
import com.wuyizhiye.basedata.util.code.factory.impl.GoodsCodeImpl;

public  class GoodsCodeFactory  extends CodeFactory {

	private static GoodsCodeFactory goodsCodeFactory=null;
	public static GoodsCodeFactory getInstance() {  
		if(goodsCodeFactory==null){
			goodsCodeFactory=new GoodsCodeFactory();
		}
		return goodsCodeFactory;
		
	}
	public CodeI create(){
		return GoodsCodeImpl.getInstance();
	}
}
