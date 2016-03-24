package com.wuyizhiye.cmct.phone.constant;

/**
 * @ClassName FjCtCmctConstant
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
public class FjCtCmctConstant {

	//public static final String SPID = "954814" ;
	public static final String SPID = "521656" ;
	public static final String SP_PWD = "dingjian521" ;
	//public static final String BASE_URL = "http://110.84.128.78:8088/httpIntf/dealIntf" ;
	public static final String BASE_URL = "http://202.109.211.100:8088/httpIntf/dealIntf" ;
	/**
	 * 测试临时APPID
	 */
	public static final String APPID = "237" ;
	
	public static final String memberKey = "dingjianMemberKey" ;
	/**
	 * 接口方法类
	 * @author li.biao
	 */
	public static class Method {
		/**
		 * 电信拨号
		 */
		public static final String Dial = "Dial" ; 
		
		/**
		 * 新版电信拨号
		 */
		public static final String Dial2 = "Dial2" ; 
		
		/**
		 * 电信拨号挂断
		 */
		public static final String DialStop = "DialStop" ; 
		
		/**
		 * 号码归属
		 */
		public static final String HcodeSearch = "HcodeSearch" ; 
		
		/**
		 * 电信账单查询
		 */
		public static final String IMS_BILL = "IMS_BILL";
		
		/**
		 * 电信余额查询
		 */
		public static final String IMS_ACCOUNT = "IMS_ACCOUNT";
		
		/**
		 * 电信营销接口新增
		 */
		public static final String Marketing1 = "Marketing1";
		
		/**
		 * 电信营销接口新增2
		 */
		public static final String Marketing2 = "Marketing2";
		
		
		/**
		 * 批量发送营销任务暂停接口
		 */
		public static final String MARKET_STOP = "MARKET_STOP";
		
		/**
		 * 批量发送营销任务启动接口
		 */
		public static final String MARKET_BEGIN = "MARKET_BEGIN";
		
		/**
		 * 批量发送营销任务关闭接口
		 */
		public static final String MARKET_CLOSE = "MARKET_CLOSE";
	
	}
}
