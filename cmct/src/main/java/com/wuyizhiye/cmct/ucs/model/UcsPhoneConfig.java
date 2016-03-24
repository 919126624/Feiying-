package com.wuyizhiye.cmct.ucs.model;

import com.wuyizhiye.base.CoreEntity;

/**
 * @ClassName UcsPhoneConfig
 * @Description  Ucs通用电话参数实体
 * @author li.biao
 * @date 2015-5-26
 */
public class UcsPhoneConfig extends CoreEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String MAPPER = "com.wuyizhiye.cmct.ucs.dao.UcsPhoneConfigDao";
	/**
	 * 登录url
	 */
	private String ucsLoginUrl;
	 
	/**
	 * 呼叫url
	 */
	private String ucsCallUrl;
	
	/**
	 * 添加坐席
	 */
	private String ucsPhoneAddURL;
	
	/**
	 * 修改坐席 
	 */
	private String ucsPhoneEditUrl;
	
	/**
	 * 删除坐席 
	 */
	private String ucsPhoneDeleteUrl;
	
	/**
	 * 查看坐席
	 */
	private String ucsPhoneSelectUrl;
	
	/**
	 * 记录查看
	 */
	private String ucsCallRecordUrl;
	
	/**
	 * 语音记录查看
	 */
	private String ucsCallRecordingUrl;
	
	/**
	 * 语音记录下载
	 */
	private String ucsCallRecordingDownloadUrl;
	
	/**
	 * 查看机构ID
	 */
	private String ucsAgentSelectUrl;
	
	/**
	 * 企业开户
	 */
	private String ucsAgentAddUrl ;
	
	/**
	 * 删除企业
	 */
	private String ucsAgentDeleteUrl;
	
	/**
	 * 修改企业
	 */
	private String ucsAgentUpdateUrl;
	
	/**
	 * 挂断呼叫
	 */
	private String ucsPhoneHangUrl;
	
	/**
	 * 获取呼叫状态
	 */
	private String ucsPhoneStatusUrl;
	
	/**
	 * 坐席示忙
	 */
	private String ucsPhoneBusyUrl ;
	
	/**
	 * 坐席示闲
	 */
	private String ucsPhoneFreeUrl;
	
	/**
	 * 去电显示号码
	 */
	private String ucsShowTelNoUrl;
	
	/**
	 * 查看通话状态
	 */
	private String ucsCallStatusUrl;
	
	//-------------------------
	/**
	 * 语音播报url
	 */
	private String ucsVoiceDialPhoneUrl;
	
	/**
	 * 多人会话url
	 */
	private String ucsMoreDialPhoneUrl;
	
	/**
	 * 话伴禁用启用url
	 */
	private String ucsPhoneDisableUrl;
	/**
	 * 特殊字符
	 */
	private String ucsSpecialChar;
	
	/**
	 * 上传去电号码
	 */
	private String ucsUploadShownoUrl;
	/**
	 * 默认服务器ip
	 * @return
	 */
	
	
	private String ucsIp;
	
	public String getUcsLoginUrl() {
		return ucsLoginUrl;
	}
	public void setUcsLoginUrl(String ucsLoginUrl) {
		this.ucsLoginUrl = ucsLoginUrl;
	}
	public String getUcsCallUrl() {
		return ucsCallUrl;
	}
	public void setUcsCallUrl(String ucsCallUrl) {
		this.ucsCallUrl = ucsCallUrl;
	}
	public String getUcsPhoneAddURL() {
		return ucsPhoneAddURL;
	}
	public void setUcsPhoneAddURL(String ucsPhoneAddURL) {
		this.ucsPhoneAddURL = ucsPhoneAddURL;
	}
	public String getUcsPhoneEditUrl() {
		return ucsPhoneEditUrl;
	}
	public void setUcsPhoneEditUrl(String ucsPhoneEditUrl) {
		this.ucsPhoneEditUrl = ucsPhoneEditUrl;
	}
	public String getUcsPhoneDeleteUrl() {
		return ucsPhoneDeleteUrl;
	}
	public void setUcsPhoneDeleteUrl(String ucsPhoneDeleteUrl) {
		this.ucsPhoneDeleteUrl = ucsPhoneDeleteUrl;
	}
	public String getUcsPhoneSelectUrl() {
		return ucsPhoneSelectUrl;
	}
	public void setUcsPhoneSelectUrl(String ucsPhoneSelectUrl) {
		this.ucsPhoneSelectUrl = ucsPhoneSelectUrl;
	}
	public String getUcsCallRecordUrl() {
		return ucsCallRecordUrl;
	}
	public void setUcsCallRecordUrl(String ucsCallRecordUrl) {
		this.ucsCallRecordUrl = ucsCallRecordUrl;
	}
	public String getUcsCallRecordingUrl() {
		return ucsCallRecordingUrl;
	}
	public void setUcsCallRecordingUrl(String ucsCallRecordingUrl) {
		this.ucsCallRecordingUrl = ucsCallRecordingUrl;
	}
	public String getUcsCallRecordingDownloadUrl() {
		return ucsCallRecordingDownloadUrl;
	}
	public void setUcsCallRecordingDownloadUrl(String ucsCallRecordingDownloadUrl) {
		this.ucsCallRecordingDownloadUrl = ucsCallRecordingDownloadUrl;
	}
	public String getUcsAgentSelectUrl() {
		return ucsAgentSelectUrl;
	}
	public void setUcsAgentSelectUrl(String ucsAgentSelectUrl) {
		this.ucsAgentSelectUrl = ucsAgentSelectUrl;
	}
	public String getUcsAgentAddUrl() {
		return ucsAgentAddUrl;
	}
	public void setUcsAgentAddUrl(String ucsAgentAddUrl) {
		this.ucsAgentAddUrl = ucsAgentAddUrl;
	}
	public String getUcsPhoneHangUrl() {
		return ucsPhoneHangUrl;
	}
	public void setUcsPhoneHangUrl(String ucsPhoneHangUrl) {
		this.ucsPhoneHangUrl = ucsPhoneHangUrl;
	}
	public String getUcsPhoneStatusUrl() {
		return ucsPhoneStatusUrl;
	}
	public void setUcsPhoneStatusUrl(String ucsPhoneStatusUrl) {
		this.ucsPhoneStatusUrl = ucsPhoneStatusUrl;
	}
	public String getUcsPhoneBusyUrl() {
		return ucsPhoneBusyUrl;
	}
	public void setUcsPhoneBusyUrl(String ucsPhoneBusyUrl) {
		this.ucsPhoneBusyUrl = ucsPhoneBusyUrl;
	}
	public String getUcsPhoneFreeUrl() {
		return ucsPhoneFreeUrl;
	}
	public void setUcsPhoneFreeUrl(String ucsPhoneFreeUrl) {
		this.ucsPhoneFreeUrl = ucsPhoneFreeUrl;
	}
	public String getUcsVoiceDialPhoneUrl() {
		return ucsVoiceDialPhoneUrl;
	}
	public void setUcsVoiceDialPhoneUrl(String ucsVoiceDialPhoneUrl) {
		this.ucsVoiceDialPhoneUrl = ucsVoiceDialPhoneUrl;
	}
	public String getUcsMoreDialPhoneUrl() {
		return ucsMoreDialPhoneUrl;
	}
	public void setUcsMoreDialPhoneUrl(String ucsMoreDialPhoneUrl) {
		this.ucsMoreDialPhoneUrl = ucsMoreDialPhoneUrl;
	}
	public String getUcsSpecialChar() {
		return ucsSpecialChar;
	}
	public void setUcsSpecialChar(String ucsSpecialChar) {
		this.ucsSpecialChar = ucsSpecialChar;
	}
	public String getUcsAgentDeleteUrl() {
		return ucsAgentDeleteUrl;
	}
	public void setUcsAgentDeleteUrl(String ucsAgentDeleteUrl) {
		this.ucsAgentDeleteUrl = ucsAgentDeleteUrl;
	}
	public String getUcsShowTelNoUrl() {
		return ucsShowTelNoUrl;
	}
	public void setUcsShowTelNoUrl(String ucsShowTelNoUrl) {
		this.ucsShowTelNoUrl = ucsShowTelNoUrl;
	}
	public String getUcsCallStatusUrl() {
		return ucsCallStatusUrl;
	}
	public void setUcsCallStatusUrl(String ucsCallStatusUrl) {
		this.ucsCallStatusUrl = ucsCallStatusUrl;
	}
	public String getUcsIp() {
		return ucsIp;
	}
	public void setUcsIp(String ucsIp) {
		this.ucsIp = ucsIp;
	}
	public String getUcsPhoneDisableUrl() {
		return ucsPhoneDisableUrl;
	}
	public void setUcsPhoneDisableUrl(String ucsPhoneDisableUrl) {
		this.ucsPhoneDisableUrl = ucsPhoneDisableUrl;
	}
	public String getUcsUploadShownoUrl() {
		return ucsUploadShownoUrl;
	}
	public void setUcsUploadShownoUrl(String ucsUploadShownoUrl) {
		this.ucsUploadShownoUrl = ucsUploadShownoUrl;
	}
	public String getUcsAgentUpdateUrl() {
		return ucsAgentUpdateUrl;
	}
	public void setUcsAgentUpdateUrl(String ucsAgentUpdateUrl) {
		this.ucsAgentUpdateUrl = ucsAgentUpdateUrl;
	}
	
}
