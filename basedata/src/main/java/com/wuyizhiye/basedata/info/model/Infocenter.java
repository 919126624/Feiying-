package com.wuyizhiye.basedata.info.model;

import java.util.Date;

import com.wuyizhiye.basedata.DataEntity;
import com.wuyizhiye.basedata.person.model.Person;

/**
 * @ClassName Infocenter
 * @Description 消息中心
 * @author li.biao
 * @date 2015-4-2
 */
public class Infocenter extends DataEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String NAME_SPACE = "com.wuyizhiye.basedata.info.dao.InfocenterDao";
	
	private String id;
	/**
	 * 消息类型
	 */
	private InfoTypeEnum type;
	/**
	 * 消息标题
	 */
	private String title;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 提醒方式
	 */
	private RemindTypeEnum remindType;
	/**
	 * 是否已读
	 */
	private int isRead;
	/**
	 * 读取时间
	 */
	private Date readTime;
	/**
	 * 接收人
	 */
	private Person person;
	/**
	 * 创建人
	 */
	private Person creator;
	/**
	 * 内容中包涵对象ID
	 */
	private String objId;
	/**
	 * 点击打开的url
	 */
	private String url;
	

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public InfoTypeEnum getType() {
		return type;
	}
	public void setType(InfoTypeEnum type) {
		this.type = type;
	}
	public String getTypeStr(){
		return type==null?"":type.getLabel();
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public RemindTypeEnum getRemindType() {
		return remindType;
	}
	public void setRemindType(RemindTypeEnum remindType) {
		this.remindType = remindType;
	}
	public int getIsRead() {
		return isRead;
	}
	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}
	public Date getReadTime() {
		return readTime;
	}
	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public Person getCreator() {
		return creator;
	}
	public void setCreator(Person creator) {
		this.creator = creator;
	}
	public String getObjId() {
		return objId;
	}
	public void setObjId(String objId) {
		this.objId = objId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	private String createDateStr;
	public String getCreateDateStr() {
		return createDateStr;
	}
	public void setCreateDateStr(String createTimeStr) {
		this.createDateStr = createTimeStr;
	}
	
}
