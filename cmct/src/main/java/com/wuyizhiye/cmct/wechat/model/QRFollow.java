package com.wuyizhiye.cmct.wechat.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.cmct.wechat.enums.QRFollowEnum;

/**
 * @ClassName QRFollow
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
public class QRFollow extends CoreEntity{
	/**
	 * 对应的二维码
	 */
	private QRRecord qr ;
	/**
	 * 对应关注人的微信id 
	 */
	private String weChatId;
	/**
	 * 创建时间
	 */
	private Date creatTime;
	/**
	 * 关注/取消关注
	 */
	private QRFollowEnum type;
	/**
	 * QR的action_info 标识字段
	 */
	private String QRKey;
	
	
	public QRFollowEnum getType() {
		return type;
	}
	public void setType(QRFollowEnum type) {
		this.type = type;
	}
	public String getQRKey() {
		return QRKey;
	}
	public void setQRKey(String qRKey) {
		QRKey = qRKey;
	}
	public QRRecord getQr() {
		return qr;
	}
	public void setQr(QRRecord qr) {
		this.qr = qr;
	}
	public String getWeChatId() {
		return weChatId;
	}
	public void setWeChatId(String weChatId) {
		this.weChatId = weChatId;
	}
	public Date getCreatTime() {
		return creatTime;
	}
	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}
	
}
