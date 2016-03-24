package com.wuyizhiye.base.jpush.push;

import java.util.HashMap;
import java.util.Map;

/**
 * JPush 状态码
 * @author li.biao
 */
public class JPushCode {

	private static Map<Integer,String> codeDesc = new HashMap<Integer,String>() ;
	private static JPushCode jpushCode ;
	
	public static Integer C_0000 = 0 ;
	public static Integer C_999 = 999 ;
	public static Integer C_1000 = 1000 ;
	public static Integer C_1001 = 1001 ;
	public static Integer C_1002 = 1002 ;
	public static Integer C_1003 = 1003 ;
	public static Integer C_1004 = 1004 ;
	public static Integer C_1005 = 1005 ;
	public static Integer C_1008 = 1008 ;
	public static Integer C_1011 = 1011 ;
	public static Integer C_1020 = 1020 ;
	public static Integer C_1030 = 1030 ;
	
	private JPushCode(){
		
	}
	
	public static JPushCode getInstance(){
		if(jpushCode == null ){
			jpushCode = new JPushCode();
			initDesc() ;
		}
		return jpushCode ;
	}
	
	private static void initDesc(){
		
		codeDesc.put(C_0000, "成功");
		
		codeDesc.put(C_999, "推送链接异常");
		codeDesc.put(C_1000, "系统内部错误");//500 服务器端内部逻辑错误，请稍后重试。
		codeDesc.put(C_1001, "只支持 HTTP Post 方法");//405 
		codeDesc.put(C_1002, "缺少了必须的参数");//400
		codeDesc.put(C_1003, "参数值不合法");//400
		codeDesc.put(C_1004, "验证失败");//401
		codeDesc.put(C_1005, "消息体太大");//400 Android平台Notification+Message长度限制为1000字节； 
		//iOS Notification 中 “iOS”:{ } 及大括号内的总体长度不超过：2000个字节（包括自定义参数和符号），iOS 的 Message部分长度不超过 1000 字节； 
		//WinPhone平台Notification长度限制为1000字节
		codeDesc.put(C_1008, "app_key参数非法");//400
		codeDesc.put(C_1011, "没有满足条件的推送目标");//400 请检查audience
		codeDesc.put(C_1020, "只支持 HTTPS 请求");//404
		codeDesc.put(C_1030, "内部服务超时");//503
	}
	
	/**
	 * 获取状态码描述
	 * @param code 状态码
	 * @return 描述
	 */
	public String getDescByCode(Integer code){
		return codeDesc.get(code);
	}
}
