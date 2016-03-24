package com.wuyizhiye.cmct.wechat.model;

import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.DataEntity;

/**
 * @ClassName WechatPromote
 * @Description 微信推广实体
 * @author li.biao
 * @date 2015-5-26
 */
public class WechatPromote extends DataEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String MAPPER = "com.wuyizhiye.cmct.wechat.dao.WechatPromoteDao";
	/**
	 * 标题
	 */
	private String title;
	
	/**
	 * 封面图片地址
	 */
	private String picUrl;
	
	/**
	 * 上传图片地址
	 */
	private String picUrlT;
	
	/**
	 * 注册地址
	 */
	private String registUrl;
	
	/**
	 *内容
	 */
	private String content;
	
	/**
	 * 状态,是否发布 Y:已经发布
	 */
	private String isPublish;

	
	//以下做显示,不对应数据库
	/**
	 * 阅读量
	 */
	private Integer readAmount;
	
	/**
	 * 注册量
	 */
	private Integer registAmount;
	
	/**
	 * 图片ids
	 */
	private String wechatPhotoids;
	
	private String preview;
	
	public String getPicUrlT() {
		return picUrlT;
	}

	public void setPicUrlT(String picUrlT) {
		this.picUrlT = picUrlT;
	}

	public String getRegistUrl() {
		return registUrl;
	}

	public void setRegistUrl(String registUrl) {
		this.registUrl = registUrl;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getWechatPhotoids() {
		return wechatPhotoids;
	}

	public void setWechatPhotoids(String wechatPhotoids) {
		this.wechatPhotoids = wechatPhotoids;
	}

	public Integer getReadAmount() {
		return readAmount;
	}

	public void setReadAmount(Integer readAmount) {
		this.readAmount = readAmount;
	}

	public Integer getRegistAmount() {
		return registAmount;
	}

	public void setRegistAmount(Integer registAmount) {
		this.registAmount = registAmount;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIsPublish() {
		return isPublish;
	}

	public void setIsPublish(String isPublish) {
		this.isPublish = isPublish;
	}

	public String getPreview() {
		if(StringUtils.isEmpty(content)) return "";
		String ctt = StringUtils.trimTag(content);
		if(StringUtils.isNotNull(ctt)){
			preview = ctt.length()>100?ctt.substring(0, 100):ctt;
		}else{
			preview = "";
		}
		return preview;
	}

	public void setPreview(String preview) {
		this.preview = preview;
	}
}
