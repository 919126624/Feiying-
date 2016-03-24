package com.wuyizhiye.cmct.phone.util;

import com.wuyizhiye.base.util.StringUtils;

/**
 * @ClassName PhoneMarketUtil
 * @Description 营销工具类
 * @author li.biao
 * @date 2015-5-26
 */
public class PhoneMarketUtil {
	
	/**
	 * 错误代码返回信息
	 */
	public static String getMsgByErrorKey(String errorKey){
		String msg = "" ;
		if(StringUtils.isEmpty(errorKey)){
			return "错误信息为空";
		}
		if("WorkID error!".equals(errorKey)){
			msg="WorkID已经失效";
		}else if("VoiceName and TTSContent is not valid!".equals(errorKey)){
			msg="语音名称或则通知内容是无效的";
		}else if("Points is not Enough!".equals(errorKey)){
			msg="余额不足...";
		}else{
			msg = errorKey ;
		}
		return msg;
	}
}
