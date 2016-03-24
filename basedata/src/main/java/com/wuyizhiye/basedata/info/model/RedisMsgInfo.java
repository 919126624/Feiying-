package com.wuyizhiye.basedata.info.model;

import com.wuyizhiye.base.CoreEntity;

/**
 * @ClassName RedisMsgInfo
 * @Description 用于存于redis里面的消息相关信息
 * @author li.biao
 * @date 2015-4-2
 */
public class RedisMsgInfo extends CoreEntity {

	private String personId;			//人员Id
	private int msgCount;				//消息数量
	private Integer msgNeedFloatDlg;	//消息是否需要弹窗
	private String msgTitle;				//消息的标题
	private String msgLastContent;		//消息最后内容
	private String lastContentAllData;	//最后的合并，中间用★分开，当期那是;弹窗方式★最后内容
	
	public String getMsgTitle() {
		return msgTitle;
	}
	public void setMsgTitle(String msgTitle) {
		this.msgTitle = msgTitle;
	}
	public String getLastContentAllData() {
		return lastContentAllData;
	}
	public void setLastContentAllData(String lastContentAllData) {
		this.lastContentAllData = lastContentAllData;
	}
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public int getMsgCount() {
		return msgCount;
	}
	public void setMsgCount(int msgCount) {
		this.msgCount = msgCount;
	}
	
	public Integer getMsgNeedFloatDlg() {
		return msgNeedFloatDlg;
	}
	public void setMsgNeedFloatDlg(Integer msgNeedFloatDlg) {
		this.msgNeedFloatDlg = msgNeedFloatDlg;
	}
	public String getMsgLastContent() {
		return msgLastContent;
	}
	public void setMsgLastContent(String msgLastContent) {
		this.msgLastContent = msgLastContent;
	}
}
