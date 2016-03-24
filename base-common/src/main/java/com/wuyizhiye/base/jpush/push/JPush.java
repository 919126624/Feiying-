package com.wuyizhiye.base.jpush.push;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

import com.wuyizhiye.base.jpush.util.DaMaiChang;
import com.wuyizhiye.base.util.DateUtil;

/**
 * 推送主要处理类
 * @author li.biao
 * @since 2015-8-27
 */
public class JPush {
	
	private static Logger logger=Logger.getLogger(JPush.class);
	private static String appKey ;
	private static String masterSecret ;
	private static JPushClient jpush ;
	private JPush(){
		
	}
	static{
		jpush = new JPushClient(DaMaiChang.masterSecret, DaMaiChang.appKey,3);
	}
	public JPush(String masterSecret,String appKey){
		masterSecret = masterSecret;
		appKey = appKey ;
		jpush = new JPushClient(masterSecret,appKey,3);
	}
	
	private static void test(){
		PushPayload payload = PushPayload.alertAll("这是一条通知消息2！");
		try {
			PushResult result = jpush.sendPush(payload);
//			System.out.println("result === " + result);
		} catch (APIConnectionException e) {
			//e.printStackTrace();
			logger.error("", e);
		} catch (APIRequestException e) {
			//e.printStackTrace();
			logger.error("", e);
		}
	}
	
	/**
	 * 发送给所有客户端
	 * @param msg 发送信息
	 */
	public static int send_all_alert(String msg){
		int code = JPushCode.C_0000;
		PushPayload payload = PushPayload.alertAll(msg);
		
		try {
			PushResult result = jpush.sendPush(payload);
			log(1,"send_all_alert",result.toString());
		} catch (APIConnectionException e) {
			code = JPushCode.C_999;
//			e.printStackTrace();
			logger.error("", e);
			log(2,"send_all_alert",e.getMessage());
		} catch (APIRequestException e) {
			code = e.getErrorCode();
//			e.printStackTrace();
			logger.error("", e);
			log(2,"send_all_alert",e.getErrorMessage());
		}
		return code ;
	}
	
	/**
	 * 发送给指定别名的客户端
	 * @param msg 消息
	 * @param alias 别名
	 */
	public static int send_all_alias(String msg,String alias){
		int code = JPushCode.C_0000;
		PushPayload payload = PushPayload.newBuilder().setPlatform(Platform.all())
		.setAudience(Audience.alias(alias))
		.setNotification(Notification.alert(msg))
		.build();
		try {
			PushResult result = jpush.sendPush(payload);
			log(1,"send_all_alias",result.toString());
		} catch (APIConnectionException e) {
			code = JPushCode.C_999;
//			e.printStackTrace();
			logger.error("", e);
			log(2,"send_all_alias",e.getMessage());
		} catch (APIRequestException e) {
			code = e.getErrorCode();
//			e.printStackTrace();
			logger.error("", e);
			log(2,"send_all_alias",e.getErrorMessage());
		}
		return code ;
	}
	
	/**
	 * 向所有的客户端推送自定义消息 ，需要指定别名
	 * @param alias 要发送给的设备别名
	 * @param title 标题
	 * @param content 内容
	 * @param contentType 内容类型
	 */
	public static int send_all_alias_message(String alias ,String title,String content,String contentType){
		int code = JPushCode.C_0000;
		Message jmsg = Message.newBuilder()
		.setTitle(title).setContentType(contentType).setMsgContent(content).addExtras(new HashMap<String, String>()).build();
		PushPayload payload = PushPayload.newBuilder()
		.setPlatform(Platform.android_ios())
		.setAudience(Audience.alias(alias))
		.setMessage(jmsg).build();
		try {
			PushResult result = jpush.sendPush(payload);
			log(1,"send_all_alias_message",result.toString());
		} catch (APIConnectionException e) {
			code = JPushCode.C_999 ;
//			e.printStackTrace();
			logger.error("", e);
			log(2,"send_all_alias_message",e.getMessage());
		} catch (APIRequestException e) {
			code = e.getErrorCode();
//			e.printStackTrace();
			logger.error("", e);
			log(2,"send_all_alias_message",e.getErrorMessage());
		}
		return code ;
	}
	
	
	/**
	 * 向所有的客户端推送自定义消息（默认文本类型）需要制定别名
	 * @param alias 要发送给的设备别名
	 * @param title 标题
	 * @param content 内容
	 * @param param 参数
	 */
	public static int send_all_alias_message(String alias ,String title,String content){
		String contentType = "text" ;
		return send_all_alias_message(alias,title, content, contentType);
	}
	
	/**
	 * 向指定注册id用户推送自定义消息
	 * @param id 要发送给的注册id
	 * @param title 标题
	 * @param content 内容
	 * @param contentType 内容类型
	 * @param param 参数
	 */
	public static int send_all_id_message(String id ,String title,String content,String contentType,Map<String,String> param){
		Message jmsg = Message.newBuilder()
		.setTitle(title).setContentType(contentType).setMsgContent(content).addExtras(param).build();
		PushPayload payload = PushPayload.newBuilder()
		.setPlatform(Platform.android_ios())
		.setAudience(Audience.registrationId(id))
		.setMessage(jmsg).build();
		int code = JPushCode.C_0000 ;
		try {
			PushResult result = jpush.sendPush(payload);
			log(1,"send_all_alias_message",result.toString());
		} catch (APIConnectionException e) {
//			e.printStackTrace();
			logger.error("", e);
			log(2,"send_all_alias_message",e.getMessage());
		} catch (APIRequestException e) {
//			e.printStackTrace();
			logger.error("", e);
			code = e.getErrorCode() ;
			log(2,"send_all_alias_message",e.getMessage());
		}
		return code ;
	}
	
	/**
	 * 向指定注册id用户推送自定义消息（默认文本类型）
	 * @param id 要发送给的注册id
	 * @param title 标题
	 * @param content 内容
	 * @param param 参数
	 */
	public static int send_all_id_message(String id ,String title,String content,Map<String,String> param){
		String contentType = "text" ;
		return send_all_id_message(id,title, content, contentType, param);
	}
	
	
	
	/**
	 * 
	 * @param level 日志级别：0：debug，1：info，2：error
	 */
	private static void log(int level,String method,String msg){
		if(level == 0 ){
			logger.debug(DateUtil.convertDateToStr(DateUtil.getCurDate(),DateUtil.GENERAL_FORMHMS)+":"+method+":debug=" + msg);
//			System.out.println(DateUtil.convertDateToStr(DateUtil.getCurDate(),DateUtil.GENERAL_FORMHMS)+":"+method+":debug=" + msg);
		}else if(level == 1 ){
			logger.info(DateUtil.convertDateToStr(DateUtil.getCurDate(),DateUtil.GENERAL_FORMHMS)+":"+method+":info=" + msg);
//			System.err.println(DateUtil.convertDateToStr(DateUtil.getCurDate(),DateUtil.GENERAL_FORMHMS)+":"+method+":info=" + msg);
		}else if(level == 2 ){
//			System.out.println(DateUtil.convertDateToStr(DateUtil.getCurDate(),DateUtil.GENERAL_FORMHMS)+":"+method+":error=" + msg);
			logger.error(DateUtil.convertDateToStr(DateUtil.getCurDate(),DateUtil.GENERAL_FORMHMS)+":"+method+":error=" + msg);
		}
	}
	
	
	
	
	/**==========================由于IOS端无法离线获取自定义,故将原始方法修改为如下方法=============================****/
	/**
	 * 向Android_ios的客户端推送自定义消息 ，需要指定别名   直接发送文本类型
	 * @param alias 要发送给的设备别名
	 * @param title 标题
	 * @param content 内容
	 */
	public static int send_android_alias_message(String alias ,String title,String content){
		int code = JPushCode.C_0000;
		Message jmsg = Message.newBuilder()
		.setTitle(title).setContentType("text").setMsgContent(content).addExtras(new HashMap<String, String>()).build();
		PushPayload payload = PushPayload.newBuilder()
		.setPlatform(Platform.android_ios())
		.setAudience(Audience.alias(alias))
		.setMessage(jmsg).build();
		try {
			PushResult result = jpush.sendPush(payload);
			log(1,"send_all_alias_message",result.toString());
		} catch (APIConnectionException e) {
			code = JPushCode.C_999 ;
//			e.printStackTrace();
			log(2,"send_all_alias_message",e.getMessage());
		} catch (APIRequestException e) {
			code = e.getErrorCode();
//			e.printStackTrace();
			log(2,"send_all_alias_message",e.getErrorMessage());
		}
		return code ;
	}
	
	/**
	 * 向android_ios客户端推送自定义消息  ,直接发送文本类型
	 * @param title 标题
	 * @param content 内容
	 */
	public static int send_android_alias_message(String title,String content){
		int code = JPushCode.C_0000;
		Message jmsg = Message.newBuilder()
		.setTitle(title).setContentType("text").setMsgContent(content).build();
		PushPayload payload = PushPayload.newBuilder()
		.setPlatform(Platform.android_ios())
		.setAudience(Audience.all())
		.setMessage(jmsg).build();
		try {
			PushResult result = jpush.sendPush(payload);
			log(1,"send_all_alias_message",result.toString());
		} catch (APIConnectionException e) {
			code = JPushCode.C_999 ;
//			e.printStackTrace();
			log(2,"send_all_alias_message",e.getMessage());
		} catch (APIRequestException e) {
			code = e.getErrorCode();
//			e.printStackTrace();
			log(2,"send_all_alias_message",e.getErrorMessage());
		}
		return code ;
	}
	
	/**
	 * 发送给指定别名的IOS的客户端的通知
	 * @param msg 消息
	 * @param alias 别名
	 */
	public static int send_ios_alias(String msg,String alias){
		int code = JPushCode.C_0000;
		PushPayload payload = PushPayload.newBuilder().setPlatform(Platform.ios())
		.setAudience(Audience.alias(alias))
		.setNotification(Notification.alert(msg))
		.build();
		try {
			PushResult result = jpush.sendPush(payload);
			log(1,"send_all_alias",result.toString());
		} catch (APIConnectionException e) {
			code = JPushCode.C_999;
//			e.printStackTrace();
			log(2,"send_all_alias",e.getMessage());
		} catch (APIRequestException e) {
			code = e.getErrorCode();
//			e.printStackTrace();
			log(2,"send_all_alias",e.getErrorMessage());
		}
		return code ;
	}
	/**
	 * 发送给ios客户端
	 * @param msg 消息
	 */
	public static int send_ios_alert(String msg){
		int code = JPushCode.C_0000;
		PushPayload payload = PushPayload.newBuilder().setPlatform(Platform.ios())
		.setAudience(Audience.all())
		.setNotification(Notification.alert(msg))
		.build();
		try {
			PushResult result = jpush.sendPush(payload);
			log(1,"send_all_alias",result.toString());
		} catch (APIConnectionException e) {
			code = JPushCode.C_999;
//			e.printStackTrace();
			log(2,"send_all_alias",e.getMessage());
		} catch (APIRequestException e) {
			code = e.getErrorCode();
//			e.printStackTrace();
			log(2,"send_all_alias",e.getErrorMessage());
		}
		return code ;
	}
	
	/**================通过标签发送==================*/
	/**
	 *  向指定标签的android发送自定义消息
	 * @param msg 消息
	 * @param tag 标签
	 * @param title 标题
	 */
	public static int send_android_tagWithTitle(String msg,String tag,String title){
		//.setTitle(title).setContentType("text").setMsgContent(content).addExtras(new HashMap<String, String>()).build();
		int code = JPushCode.C_0000;
		PushPayload payload =  PushPayload.newBuilder()
        .setPlatform(Platform.android())
        .setAudience(Audience.newBuilder()
                .addAudienceTarget(AudienceTarget.tag(tag))
                .build())
        .setMessage(Message.newBuilder()
        		.setTitle(title)
                .setMsgContent(msg)
                .addExtras(new HashMap<String, String>())
                .build())
        .build();
		try {
			PushResult result = jpush.sendPush(payload);
			log(1,"send_android_tagWithTitle",result.toString());
		} catch (APIConnectionException e) {
			code = JPushCode.C_999;
//			e.printStackTrace();
			logger.error("", e);
			log(2,"send_android_tagWithTitle",e.getMessage());
		} catch (APIRequestException e) {
			code = e.getErrorCode();
//			e.printStackTrace();
			logger.error("", e);
			log(2,"send_android_tagWithTitle",e.getErrorMessage());
		}
		return code ;
	}
	
	/**
	 *  向指定标签的ios发送自定义消息
	 * @param msg 消息
	 * @param tag 标签
	 * @param title 标题
	 */
	public static int send_ios_tagWithTitle(String msg,String tag,String title){
		int code = JPushCode.C_0000;
		PushPayload payload = PushPayload.newBuilder()
        .setPlatform(Platform.ios())
        .setAudience(Audience.tag(tag))
        .setNotification(Notification.newBuilder().addPlatformNotification(
        		IosNotification.newBuilder().setAlert(title).setBadge(5).build()
        	).build())
        	.setMessage(Message.content(msg))
            	.setOptions(Options.newBuilder()
            	.setApnsProduction(true)
            	.build())
            .build();
		try {
			PushResult result = jpush.sendPush(payload);
			log(1,"send_ios_tagWithTitle",result.toString());
		} catch (APIConnectionException e) {
			code = JPushCode.C_999;
//			e.printStackTrace();
			logger.error("", e);
			log(2,"send_ios_tagWithTitle",e.getMessage());
		} catch (APIRequestException e) {
			code = e.getErrorCode();
//			e.printStackTrace();
			logger.error("", e);
			log(2,"send_ios_tagWithTitle",e.getErrorMessage());
		}
		return code ;
	}
	
	/***
	 * 向指定标签的android以及ios发送自定义消息
	 * @param tags 标签集合
	 * @param msg 要推送的消息内容
	 * @return
	 */
    public static int send_android_ios_tagWithTitle(String [] tags,String title,String msg) {
    	int code = JPushCode.C_0000;
    	PushPayload payload =  PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.newBuilder()
                        .addAudienceTarget(AudienceTarget.tag(tags))
//                      .addAudienceTarget(AudienceTarget.alias("别名1", "别名2"))
                        .build())
                .setMessage(Message.newBuilder()
                		.setTitle(title)
                        .setMsgContent(msg)
                        .addExtras(new HashMap<String, String>())
                        .build())
                .build();
    	try {
			PushResult result = jpush.sendPush(payload);
			log(1,"send_android_ios_tagWithTitle",result.toString());
		} catch (APIConnectionException e) {
			code = JPushCode.C_999;
//			e.printStackTrace();
			logger.error("", e);
			log(2,"send_android_ios_tagWithTitle",e.getMessage());
		} catch (APIRequestException e) {
			code = e.getErrorCode();
//			e.printStackTrace();
			logger.error("", e);
			log(2,"send_android_ios_tagWithTitle",e.getErrorMessage());
		}
		return code ;
    }
	//由于ios平台限制, 无法接受自定义消息推送,故而需要一并发送通过标签推送的通知
    public static int buildPushObject_android_tag_alertWithTitle(String [] tags,String title,String msg) {
        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(Audience.tag(tags))
                .setNotification(Notification.android(msg, title, null))
                .build();
        return 1;
    }
    
	public static void main(String[] args) {
//		Map<String,String> param = new HashMap<String,String>();
//		param.put("uid", "1233");
//		JPush jpush = new JPush(DaMaiChang.masterSecret, DaMaiChang.appKey);
//		//jpush.send_all_alias_message("xxxx", "test", "ni hao shuai!帅啊！", param);
//		jpush.send_all_alert("test!");
		//{"msg_id":4214099254,"sendno":479397439}z
		//
//		JPush jpush = new JPush(DaMaiChang.masterSecret, DaMaiChang.appKey);
//		send_all_alias("成杰，还是推给你的2！","13147050878");
//		send_all_alias_message("woshibiaoti","neirong");
//		send_android_alias_message("ok","");
		send_android_alias_message("13612873429","活动通知", "{\"title\":\"活动通知\",\"content\":\"我是标题aaa\",\"value\":{\"id\":\"9fc96ab3-8616-4a7a-9947-d732ff6a3cff\",\"activityId\":\"9fc96ab3-8616-4a7a-9947-d732ff6a3cff\"\"type\":\"ACTIVITY\",\"url\":\"\"}}");
	}
}
