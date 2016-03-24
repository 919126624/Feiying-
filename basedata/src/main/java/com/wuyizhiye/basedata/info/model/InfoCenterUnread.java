package com.wuyizhiye.basedata.info.model;

import com.wuyizhiye.basedata.DataEntity;

/**
 * @ClassName InfoCenterUnread
 * @Description 消息中心未读数据
 * @author li.biao
 * @date 2015-4-2
 */
public class InfoCenterUnread extends DataEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String NAME_SPACE = "com.wuyizhiye.basedata.info.dao.InfoCenterUnreadDao";

	private String id; // 主键ID 对应人员ID
	private int unreadCount;  //未读消息数量
	private String lastContent; // 未读消息内容  最后一条
	private String lastTitle; // 未读消息标题  最后一条
	private int needOpenDialog; // 是否需要弹窗
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getUnreadCount() {
		return unreadCount;
	}
	public void setUnreadCount(int unreadCount) {
		this.unreadCount = unreadCount;
	}
	public String getLastContent() {
		return lastContent;
	}
	public void setLastContent(String lastContent) {
		this.lastContent = lastContent;
	}
	public String getLastTitle() {
		return lastTitle;
	}
	public void setLastTitle(String lastTitle) {
		this.lastTitle = lastTitle;
	}
	public int getNeedOpenDialog() {
		return needOpenDialog;
	}
	public void setNeedOpenDialog(int needOpenDialog) {
		this.needOpenDialog = needOpenDialog;
	}

}
