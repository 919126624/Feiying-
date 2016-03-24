package com.wuyizhiye.cmct.phone.model;

import com.wuyizhiye.basedata.DataEntity;
import com.wuyizhiye.basedata.enums.CommonFlagEnum;

/**
 * @ClassName PhoneMarket
 * @Description 营销实体
 * @author li.biao
 * @date 2015-5-26
 */
public class PhoneMarket extends DataEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String MAPPER = "com.wuyizhiye.cmct.phone.dao.PhoneMarketDao";
	
	/**
	 * 计费号码（该号码必须为您已经申请的号码）
	 */
	private String chargeNbr;
	
	/**
	 * 计费号码Id by lxl 14.8.26 add 
	 */
	private String chargeNbrId;
	
	
	/**
	 * 需填写选号注册时产生的Key
	 */
	private String key;
	
	/**
	 * 来电显示号码（默认为计费号码，如果想用其他号码做为来电显示号码，必须先调用'主显号码申请'接口，一个计费号码可以对应多个主显号码）
	 */
	private String displayNbr;
	
	/**
	 * 被叫号码。可以填写带区号的固话号码或11位手机号码（如059587654321,18987654321）
	        若号码是企业总机中的某个分机号，格式为固话号码+a+分机号（如059587654321a001）多个被叫用“,”分割， 最多可以5000个被叫号码。
	 */
	private String calleeNbr;
	
	/**
	 * 语音文件名称(填入 在'语音文件管理'页面'新增'并处于'审核通过'状态的 语音文件名称)
	 */
	private String voiceName;
	
	/**
	 *  语音通知的内容与语音文件名称至少二者选一，或者二者都选,二者都传值的情况下只播放媒体语音文件。
		1.能发出读音有：汉字、数字、英文;
		2.能发出读音的特殊符号有： @ $ % &;
		3.不会发出读音的特殊符号： ~ !#^* ( ) -_+ = / ? \ ;,.’:`;
		4.特殊符号如果出现在首部会被当做代码的结束符，后面的所有代码就会无效了。比如“, hello”;代码执行成功，但是没有hello的读音，如果出现在字符串中部，则同总结情况3;
		5.TTS对数字播报为数值模式，如需数字模式需中间加空格。如"123"念的是"一百二十三“ ， ”1 2 3"念的是"一二三"
	 */
	private String tTSContent;
	
	/**
	 * 输入字符“1”，“2”或“3”来判断营销类型。1：自定义按键流程 2：自定义转人工流程 3：普通流程(默认普通流程)
	 */
	private String callMode;
	
	/**
	 * 转人工的时间间隔，如果不设置的话，默认是在听完语音之后再转人工，如果设置的话就按设置的时间点来转人工（当CallMode值为2时传递）
	 */
	private String waitTime;
	
	/**
	 * 转接号码的URL地址,返回结果如：0;XXXX 其中0：成功 XXXX：要转接的电话号码（当CallMode值为2时传递）
	 * 地址需进行BASE64加密，
	 */
	private String transferUrl;
	
	/**
	 * 动态转接是否录音（"0"不录音，"1"录音）
	 */
	private String isRecord;
	
	/**
	 * 上午开始发送时间（以09:00 格式）
	 */
	private String amBeginTime;
	
	/**
	 * 上午结束发送时间（以12:00 格式）
	 */
	private String amEndTime;
	
	/**
	 * 下午开始发送时间（以14:00 格式）
	 */
	private String pmBeginTime;
	
	/**
	 * 下午结束发送时间（以18:00 格式）
	 */
	private String pmEndTime;
	
	/**
	 * 是否直接发送 1 不直接发送 2 直接发送 （默认：1）
	 */
	private String isSend;
	
	/**
	 * 有效期（以:2013-05-20 08:00:00 格式）,在这个时间前发送。超过时间点不发送
	 */
	private String effectDate;
	
	/**
	 * 流量类型（"1"秒，"2"分钟）
	 */
	private String flowrateType;
	
	/**
	 * 最大可群发的数量（最多发送20条/秒）,当FlowrateType为1时，为每秒最大的发送量，当FlowrateType为2时，为每分钟最大的发送量。
	 */
	private String maxFlowrate;
	
	/**
	 * 任务号ID（在自定义按键设置中返回的任务号，只在CallMode=1时生效）
	 */
	private String modeID;
	
	/**
	 * 转坐席的坐席号码，多个坐席用","分开。如（18911111111,1891111112）
	 */
	private String seatNbr;
	
	/**
	 *状态 1:暂停,2:启动,3:关闭
	 * @return
	 */
	private String status;
	
	/**
	 * 发送数量
	 */
	private Integer sendCount;
	
	/**
	 * 执行状态
	 */
	private CommonFlagEnum executeStatus;
	
	/**
	 * lxl 14.9.4 添加当前营销所对应的绑定营销实体
	 */
	private PhoneMarketBindPerson phoneMbp;
	
	public PhoneMarketBindPerson getPhoneMbp() {
		return phoneMbp;
	}

	public void setPhoneMbp(PhoneMarketBindPerson phoneMbp) {
		this.phoneMbp = phoneMbp;
	}

	/**
	 * 不对应数据库 ,做显示
	 * @return
	 */
	private Integer bjTotalCall;
	
	private Integer zjTotalCall;
	
	private Integer jtTotalCall;
	
	private Double zjRate;
	
	private Double jtRate;
	
	private Double zjAvgdr;
	
	public Integer getBjTotalCall() {
		return bjTotalCall;
	}

	public void setBjTotalCall(Integer bjTotalCall) {
		this.bjTotalCall = bjTotalCall;
	}

	public Integer getZjTotalCall() {
		return zjTotalCall;
	}

	public void setZjTotalCall(Integer zjTotalCall) {
		this.zjTotalCall = zjTotalCall;
	}

	public Integer getJtTotalCall() {
		return jtTotalCall;
	}

	public void setJtTotalCall(Integer jtTotalCall) {
		this.jtTotalCall = jtTotalCall;
	}

	public Double getZjRate() {
		return zjRate;
	}

	public void setZjRate(Double zjRate) {
		this.zjRate = zjRate;
	}

	public Double getJtRate() {
		return jtRate;
	}

	public void setJtRate(Double jtRate) {
		this.jtRate = jtRate;
	}

	public Double getZjAvgdr() {
		return zjAvgdr;
	}

	public void setZjAvgdr(Double zjAvgdr) {
		this.zjAvgdr = zjAvgdr;
	}

	public CommonFlagEnum getExecuteStatus() {
		return executeStatus;
	}

	public void setExecuteStatus(CommonFlagEnum executeStatus) {
		this.executeStatus = executeStatus;
	}

	public Integer getSendCount() {
		return sendCount;
	}

	public void setSendCount(Integer sendCount) {
		this.sendCount = sendCount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getChargeNbr() {
		return chargeNbr;
	}

	public void setChargeNbr(String chargeNbr) {
		this.chargeNbr = chargeNbr;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDisplayNbr() {
		return displayNbr;
	}

	public void setDisplayNbr(String displayNbr) {
		this.displayNbr = displayNbr;
	}

	public String getCalleeNbr() {
		return calleeNbr;
	}

	public void setCalleeNbr(String calleeNbr) {
		this.calleeNbr = calleeNbr;
	}

	public String getVoiceName() {
		return voiceName;
	}

	public void setVoiceName(String voiceName) {
		this.voiceName = voiceName;
	}

	public String gettTSContent() {
		return tTSContent;
	}

	public void settTSContent(String tTSContent) {
		this.tTSContent = tTSContent;
	}

	public String getCallMode() {
		return callMode;
	}

	public void setCallMode(String callMode) {
		this.callMode = callMode;
	}

	public String getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(String waitTime) {
		this.waitTime = waitTime;
	}

	public String getTransferUrl() {
		return transferUrl;
	}

	public void setTransferUrl(String transferUrl) {
		this.transferUrl = transferUrl;
	}

	public String getIsRecord() {
		return isRecord;
	}

	public void setIsRecord(String isRecord) {
		this.isRecord = isRecord;
	}

	public String getAmBeginTime() {
		return amBeginTime;
	}

	public void setAmBeginTime(String amBeginTime) {
		this.amBeginTime = amBeginTime;
	}

	public String getAmEndTime() {
		return amEndTime;
	}

	public void setAmEndTime(String amEndTime) {
		this.amEndTime = amEndTime;
	}

	public String getPmBeginTime() {
		return pmBeginTime;
	}

	public void setPmBeginTime(String pmBeginTime) {
		this.pmBeginTime = pmBeginTime;
	}

	public String getPmEndTime() {
		return pmEndTime;
	}

	public void setPmEndTime(String pmEndTime) {
		this.pmEndTime = pmEndTime;
	}

	public String getIsSend() {
		return isSend;
	}

	public void setIsSend(String isSend) {
		this.isSend = isSend;
	}

	public String getEffectDate() {
		return effectDate;
	}

	public void setEffectDate(String effectDate) {
		this.effectDate = effectDate;
	}

	public String getFlowrateType() {
		return flowrateType;
	}

	public void setFlowrateType(String flowrateType) {
		this.flowrateType = flowrateType;
	}

	public String getMaxFlowrate() {
		return maxFlowrate;
	}

	public void setMaxFlowrate(String maxFlowrate) {
		this.maxFlowrate = maxFlowrate;
	}

	public String getModeID() {
		return modeID;
	}

	public void setModeID(String modeID) {
		this.modeID = modeID;
	}

	public String getSeatNbr() {
		return seatNbr;
	}

	public void setSeatNbr(String seatNbr) {
		this.seatNbr = seatNbr;
	}

	public String getChargeNbrId() {
		return chargeNbrId;
	}

	public void setChargeNbrId(String chargeNbrId) {
		this.chargeNbrId = chargeNbrId;
	}
}
