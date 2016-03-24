package com.wuyizhiye.base.wechat;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.wuyizhiye.base.util.HttpClientUtil;
import com.wuyizhiye.base.util.SystemConfig;

/**
 * @ClassName WechatServiceApi
 * @Description ERP 微信 API调用工具类
 * @author li.biao
 * @date 2015-4-1
 */
public class WechatServiceApi {

	//日志记录器
	private static Logger logger = Logger.getLogger(WechatServiceApi.class); 
	
	/**
	 * 发送消息给用户
	 * @param wechat 必录参数 字段： type，wechatId
	 * @return -1:
	 */
	public static String sendNewsInfoToUser(WechatEntity wechat){
		if(wechat == null || StringUtils.isEmpty(wechat.getWechatId())){
			logger.error("WechatServiceApi.sendNewsInfoToUser:参数错误或者未找到经纪人微信号");
			return "-1";
		}
		String wechatHost = SystemConfig.getParameter("weixinhost");
		if(StringUtils.isEmpty(wechatHost)){
			logger.error("WechatServiceApi.sendNewsInfoToUser:未配置微信地址");
			return "-2";
		}
		String url = wechatHost + "/weixinapi/sendMessage" ;
		StringBuilder paramBuilder = new StringBuilder() ;
		paramBuilder.append("m_teyp").append("=").append(wechat.getType().getValue()).append("&");
		paramBuilder.append("m_opiden").append("=").append(wechat.getWechatId()).append("&");
		if(!StringUtils.isEmpty(wechat.getContent())){
			paramBuilder.append("m_contten").append("=").append(URLEncoder.encode(wechat.getContent())).append("&");
		}
		if(!StringUtils.isEmpty(wechat.getUrl())){
			paramBuilder.append("m_tourl").append("=").append(wechat.getUrl()).append("&");
		}
		if(!StringUtils.isEmpty(wechat.getPicUrl())){
			paramBuilder.append("m_picurl").append("=").append(wechat.getPicUrl()).append("&");
		}
		if(!StringUtils.isEmpty(wechat.getTitle())){
			paramBuilder.append("m_title").append("=").append(URLEncoder.encode(wechat.getTitle()));
		}
		return HttpClientUtil.callHttpUrlGet(url+"?"+paramBuilder.toString());
	}
	
	/**
	 * 更新用户微信信息
	 * @param wechat 必录参数 ： userId 用户id；wechatId 微信id
	 */
	public static String updateWeChatInfo(WechatEntity wechat){
		if(wechat == null || StringUtils.isEmpty(wechat.getWechatId()) || StringUtils.isEmpty(wechat.getUserId())){
			logger.error("WechatServiceApi.updateWeChatInfo:参数错误或者未找到经纪人微信号");
			return "-1";
		}
		String wechatHost = SystemConfig.getParameter("weixinhost");
		if(StringUtils.isEmpty(wechatHost)){
			logger.error("WechatServiceApi.updateWeChatInfo:未配置微信地址");
			return "-2";
		}
		String url = wechatHost + "/weixinapi/updateWeChatInfo" ;
		Map<String,String> params = new HashMap<String,String>();
		params.put("m_uIdser", wechat.getUserId());
		params.put("m_weIdchat", wechat.getWechatId());
		StringBuilder paramBuilder = new StringBuilder() ;
		paramBuilder.append("m_uIdser").append("=").append(wechat.getUserId()).append("&");
		paramBuilder.append("m_weIdchat").append("=").append(wechat.getWechatId());
		return HttpClientUtil.callHttpUrlGet(url+"?"+paramBuilder.toString());
	}
	
	public static void main(String[] args) {
		String wechatHost = "http://120.25.236.193:9980/ebcar";
		WechatEntity wechat = new WechatEntity();
		wechat.setWechatId("o9LPtjjaqPoD0WhVdagNKTQxDUNo");
		wechat.setTitle("报价测试");
	 	wechat.setContent("111");
	 	wechat.setUrl(wechatHost+"/inquiryhistory/schemeView?scheId=1");
		String picUrl = wechatHost+"/default/style/images/bannerscheme.jpg" ;
		wechat.setPicUrl(picUrl);
		wechat.setType(WeChatMsgTypeEnum.news);		
	
		wechat.setUserId("852d9382-968e-4f10-8f05-c4dfc050686b");
		String res = sendNewsInfoToUser(wechat);
//		System.out.println(res);
	}
	
}
