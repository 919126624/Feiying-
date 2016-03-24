package com.wuyizhiye.cmct.wechat.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.cmct.wechat.enums.QRUserEnum;

/**
 * @ClassName QRRecord
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
public class QRRecord extends CoreEntity{
	/**
	 * 使用对象
	 */
	private String target;
	/**
	 * 使用类型枚举
	 */
	private QRUserEnum type ;
	/**
	 * 使用人
	 */
	private Person person;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 对应微信的QR生成信息 action_info
	 */
	private String QRKey;
	/**
	 * 微信二维码地址
	 */
	private String url;
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	
	/**
	 * 冗余字段
	 * @return
	 */
	private Integer fansCount;
	
	
	
	public Integer getFansCount() {
		return fansCount;
	}
	public void setFansCount(Integer fansCount) {
		this.fansCount = fansCount;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public QRUserEnum getType() {
		return type;
	}
	public void setType(QRUserEnum type) {
		this.type = type;
	}
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getQRKey() {
		return QRKey;
	}
	public void setQRKey(String qRKey) {
		QRKey = qRKey;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
}
