package com.wuyizhiye.basedata.info.model;

import java.util.Date;

import com.wuyizhiye.basedata.DataEntity;
import com.wuyizhiye.basedata.info.enums.InfomationStatusEnum;
import com.wuyizhiye.basedata.person.model.Person;

/**
 * @ClassName Infomation
 * @Description 消息提醒
 * @author li.biao
 * @date 2015-4-2
 */
public class Infomation extends DataEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String NAME_SPACE = "com.wuyizhiye.basedata.info.dao.InfomationDao";

	private String id; // 主键ID
	private Date createDate; // 提醒时间FCREATEDATE
	private Person person; // 提醒人FKPERSONID
	private String content; // 提醒内容FCONTENT
	private String moduleId; // 对象IDFKMODULEID
	private String moduleType; // 对象类型FMODULES
	private InfomationStatusEnum status; // 消息状态
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public InfomationStatusEnum getStatus() {
		return status;
	}

	public void setStatus(InfomationStatusEnum status) {
		this.status = status;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public String getModuleType() {
		return moduleType;
	}


}
