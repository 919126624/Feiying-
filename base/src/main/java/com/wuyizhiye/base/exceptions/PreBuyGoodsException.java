package com.wuyizhiye.base.exceptions;

/***
 * 预售商品批量下单异常 
 * @author 蔡鹰
 * @date 2016-1-14
 */
public class PreBuyGoodsException extends RuntimeException{
	private static final long serialVersionUID = -1144002882814822148L;
	
	public PreBuyGoodsException(String msg) {
		super(msg);
	}
}
