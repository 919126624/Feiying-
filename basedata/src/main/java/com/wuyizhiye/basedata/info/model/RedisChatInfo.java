package com.wuyizhiye.basedata.info.model;

import java.util.List;

import com.wuyizhiye.base.CoreEntity;

/**
 * @ClassName RedisChatInfo
 * @Description 用于将鼎尖聊聊里面的相关信息存入到Redis的主体信息的实体
 * @author li.biao
 * @date 2015-4-2
 */
public class RedisChatInfo extends CoreEntity {

	private String personId;	//消息接受人的Id
	private Integer msgNeedFloatDlg =1;	//消息是否需要弹窗标识 默认是需要弹窗
	private int totalMsgCount;	//所有的消息数量
	private List<RedisChatInfoSender> senderList;	//发送人的实体

	public int getTotalMsgCount() {
		return totalMsgCount;
	}

	public void setTotalMsgCount(int totalMsgCount) {
		this.totalMsgCount = totalMsgCount;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public List<RedisChatInfoSender> getSenderList() {
		return senderList;
	}

	public void setSenderList(List<RedisChatInfoSender> senderList) {
		this.senderList = senderList;
	}

	public Integer getMsgNeedFloatDlg() {
		return msgNeedFloatDlg;
	}

	public void setMsgNeedFloatDlg(Integer msgNeedFloatDlg) {
		this.msgNeedFloatDlg = msgNeedFloatDlg;
	}

	
}
