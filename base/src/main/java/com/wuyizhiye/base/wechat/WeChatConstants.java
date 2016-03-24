package com.wuyizhiye.base.wechat;


public final class WeChatConstants {
	/*
	 *经济圈微信订阅号
	 * 2695510035@qq.com
	 * os123456
	*/
	public static String APPID = "wx76cc9a85e060b842";
	public static String APPSECRET = "";//未知
	
	public static class EVENT{
		public static String subscribe="subscribe";
		public static String unsubscribe="unsubscribe";
		public static String click="CLICK";
		public static String location="LOCATION";
		public static String scan="scan";
	}
	
	public static class EVENTKEY{
		public static String waitApp="WAITAPPROVE";
		public static String workOa="WORKOA";
		public static String whsd="WHSD";
		public static String jylt="JYLT";
		public static String dnyt="DNYT";
		public static String jybem="JYBEM";
		public static String sjsz="SJSZ";
		public static String viewroom="VIEWROOM";
		public static String newcenter="NEWCENTER";
		public static String institution="INSTITUTION";
	}
	
	public static class MSGTYPE{
		public static String event="event";
		public static String text="text";
		public static String image="image";
		public static String voice="voice";
		public static String video="video";
		public static String location="location";
		public static String link="link";
		public static String news="news";
		public static String service="transfer_customer_service";
	}
	
	public static class USERINFO{
		public static String nickname="nickname";
		public static String sex="sex";
		public static String headimgurl="headimgurl";
		public static String city="city";
		public static String province="province";
		public static String country="country";
	}
	
	public static class BASEINFO{
		public static String refresh_token="refresh_token";
		public static String openid="openid";
		public static String scope="scope";
	}
	
	public static class ERROR{
		public static String errcode="errcode";
		public static String errmsg="errmsg";
	}
}
