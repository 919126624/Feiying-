package com.wuyizhiye.basedata.info.model;

import com.wuyizhiye.base.CoreEntity;

/**
 * @ClassName RedisChatInfoSender
 * @Description 用于将鼎尖聊聊里面的相关信息存入到Redis的主体信息的实体
 * @author li.biao
 * @date 2015-4-2
 */
public class RedisChatInfoSender extends CoreEntity {

	private String receivePersonId;	//接收消息的人
	
	private String sendPersonId;	//发消息人的Id
	private String sendPersonName;	//发消息人的名字
	private String sendPersonPhotoUrl;			//头像地址
	
	private String lastMsgContent;		//最后消息内容
	
	private int notReadCount;			//未读数量
	

	public String getSendPersonPhotoUrl() {
		return sendPersonPhotoUrl;
	}

	public void setSendPersonPhotoUrl(String sendPersonPhotoUrl) {
		this.sendPersonPhotoUrl = sendPersonPhotoUrl;
	}

	public String getReceivePersonId() {
		return receivePersonId;
	}

	public void setReceivePersonId(String receivePersonId) {
		this.receivePersonId = receivePersonId;
	}
	

	public String getSendPersonId() {
		return sendPersonId;
	}

	public void setSendPersonId(String sendPersonId) {
		this.sendPersonId = sendPersonId;
	}

	public String getSendPersonName() {
		return sendPersonName;
	}

	public void setSendPersonName(String sendPersonName) {
		this.sendPersonName = sendPersonName;
	}

	public String getLastMsgContent() {
		return lastMsgContent;
	}

	public void setLastMsgContent(String lastMsgContent) {
		this.lastMsgContent = lastMsgContent;
	}

	public int getNotReadCount() {
		return notReadCount;
	}

	public void setNotReadCount(int notReadCount) {
		this.notReadCount = notReadCount;
	}
	
	
}
