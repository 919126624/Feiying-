package com.wuyizhiye.basedata.weChat.model;

import java.util.Date;

import com.wuyizhiye.basedata.DataEntity;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.weChat.enums.WeChatMessageTypeEnum;

/**
 * @ClassName WeChatMessage
 * @Description 微信消息
 * @author li.biao
 * @date 2015-4-3
 */
public class WeChatMessage extends DataEntity{

	private static final long serialVersionUID = 1L;
	public static final String NAME_SPACE = "com.wuyizhiye.basedata.weChat.dao.WeChatMessageDao";

	private String id; // 主键ID
	private String publicWeChat;//公司公众微信ID
	private String clientWeChat; //客户微信ID
	private Date msgDate; // 消息发送/接收时间
	private String content; // 内容FCONTENT
	private WeChatMessageTypeEnum messageType; // 消息类型  (发送/接收)
	private Person person; // 发送人FKPERSONID （消息类型为发送时有值）
	private int isRead;//是否已读  1：已读  0：未读 (消息类型为发送的  则全部0)
	private String snedCode;//消息发送状态码 （消息类型为发送时有值,0为成功）
	private String userName;//经纪人名字
	private String carshopName;//车行名字
	
	
	/**作显示字段*/
	private String userPhoto ;
	private String personPhoto ;
	
	
	public String getUserPhoto() {
		return userPhoto;
	}
	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}
	public String getPersonPhoto() {
		return personPhoto;
	}
	public void setPersonPhoto(String personPhoto) {
		this.personPhoto = personPhoto;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCarshopName() {
		return carshopName;
	}
	public void setCarshopName(String carshopName) {
		this.carshopName = carshopName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPublicWeChat() {
		return publicWeChat;
	}
	public void setPublicWeChat(String publicWeChat) {
		this.publicWeChat = publicWeChat;
	}
	 
	public Date getMsgDate() {
		return msgDate;
	}
	public void setMsgDate(Date msgDate) {
		this.msgDate = msgDate;
	}
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public WeChatMessageTypeEnum getMessageType() {
		return messageType;
	}
	public void setMessageType(WeChatMessageTypeEnum messageType) {
		this.messageType = messageType;
	}
	public int getIsRead() {
		return isRead;
	}
	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}
	public String getSnedCode() {
		return snedCode;
	}
	public void setSnedCode(String snedCode) {
		this.snedCode = snedCode;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getClientWeChat() {
		return clientWeChat;
	}
	public void setClientWeChat(String clientWeChat) {
		this.clientWeChat = clientWeChat;
	}
	 
  
}
