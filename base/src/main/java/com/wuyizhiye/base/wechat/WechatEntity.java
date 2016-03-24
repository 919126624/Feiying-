package com.wuyizhiye.base.wechat;

/**
 * @ClassName WechatEntity
 * @Description 微信消息参数实体
 * @author li.biao
 * @date 2015-4-1
 */
public class WechatEntity {

	//用户id
	private String userId ;
	
	//微信id
	private String wechatId ;
	
	//微信消息类型
	private WeChatMsgTypeEnum type ;
	
	//微信内容
	private String content ;
	
	//标题
	private String title ;
	
	//查看连接
	private String url ;
	
	//图片连接
	private String picUrl ;

	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getWechatId() {
		return wechatId;
	}

	public void setWechatId(String wechatId) {
		this.wechatId = wechatId;
	}

	public WeChatMsgTypeEnum getType() {
		return type;
	}

	public void setType(WeChatMsgTypeEnum type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
}
