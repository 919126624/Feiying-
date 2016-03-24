package com.wuyizhiye.base.wechat;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.wuyizhiye.base.common.XmlResp;



public class WeChatResp implements XmlResp {
	
	private WeChatInfo weChatInfo;

	@Override
	public XmlResp praseResp(Map map) {
		weChatInfo = new WeChatInfo();
		Set<String> set = map.keySet();
		Iterator<String> it = set.iterator();
		while(it.hasNext()){
			String s = it.next();
			String v = null==map.get(s)?"":map.get(s).toString();
			if(v.indexOf("CDATA:")>-1 || v.indexOf("Text:")>-1) v = v.substring(7,v.lastIndexOf("]"));
			v = v.trim();
			if(s.equalsIgnoreCase("ToUserName")) weChatInfo.setToUserName(v);
			else if(s.equalsIgnoreCase("FromUserName")) weChatInfo.setFromUserName(v);
			else if(s.equalsIgnoreCase("CreateTime")) weChatInfo.setCreateTime(v);
			else if(s.equalsIgnoreCase("MsgType")) weChatInfo.setMsgType(v);
			else if(s.equalsIgnoreCase("Event")) weChatInfo.setEvent(v);
			else if(s.equalsIgnoreCase("EventKey")) weChatInfo.setEventKey(v);
			else if(s.equalsIgnoreCase("Ticket")) weChatInfo.setTicket(v);
			else if(s.equalsIgnoreCase("Latitude")) weChatInfo.setLatitude(v);
			else if(s.equalsIgnoreCase("Longitude")) weChatInfo.setLongitude(v);
			else if(s.equalsIgnoreCase("Content")) weChatInfo.setContent(v);
			else if(s.equalsIgnoreCase("MsgId")) weChatInfo.setMsgId(v);
			else continue;
		}
		return this;
	}

	public WeChatInfo getWeChatInfo() {
		return weChatInfo;
	}

	public void setWeChatInfo(WeChatInfo weChatInfo) {
		this.weChatInfo = weChatInfo;
	}

	

	
}
