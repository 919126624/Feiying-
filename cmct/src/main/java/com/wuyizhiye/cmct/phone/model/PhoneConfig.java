package com.wuyizhiye.cmct.phone.model;

import com.wuyizhiye.basedata.DataEntity;
import com.wuyizhiye.basedata.enums.CommonFlagEnum;

/**
 * @ClassName PhoneConfig
 * @Description 呼叫中心-话伴参数配置表
 * @author li.biao
 * @date 2015-5-26
 */
public class PhoneConfig  extends DataEntity{

	private static final long serialVersionUID = 2500150758463358734L;
	
	public static final String MAPPER = "com.wuyizhiye.cmct.phone.dao.PhoneConfigDao";
	
	//渠道商id
	private String orgId ;
	
	//渠道商名称
	private String orgName ;
	
	//渠道商密匙
	private String orgKey ; 
	
	//添加成员接口
	private String addUrl ;
	
	//修改成员接口
	private String modifyUrl ;
	
	//删除成员接口
	private String deleteUrl ;
	
	//获取渠道商固话列表接口
	private String getNumberUrl ;
	
	//查询账户余额接口
	private String queryAccountUrl ;
	
	//查询组织话单接口
	private String getCallUrl ;
	
	//是否启用
	private CommonFlagEnum enable ;
	
	//配置名称
	private String configName ;
	
	private String addRightUrl;
	private String delRightUrl;
	private String updRightUrl;
	private String bindRightUrl;
	private String unbindRightUrl;
	
	/*-----以下字段数据库表中不存在------*/
	//余额
	private Double balance ;
	
	//核算渠道的合作商
	private String partners;

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgKey() {
		return orgKey;
	}

	public void setOrgKey(String orgKey) {
		this.orgKey = orgKey;
	}

	public String getAddUrl() {
		return addUrl;
	}

	public void setAddUrl(String addUrl) {
		this.addUrl = addUrl;
	}

	public String getModifyUrl() {
		return modifyUrl;
	}

	public void setModifyUrl(String modifyUrl) {
		this.modifyUrl = modifyUrl;
	}

	public String getDeleteUrl() {
		return deleteUrl;
	}

	public void setDeleteUrl(String deleteUrl) {
		this.deleteUrl = deleteUrl;
	}

	public String getGetNumberUrl() {
		return getNumberUrl;
	}

	public void setGetNumberUrl(String getNumberUrl) {
		this.getNumberUrl = getNumberUrl;
	}

	public CommonFlagEnum getEnable() {
		return enable;
	}

	public void setEnable(CommonFlagEnum enable) {
		this.enable = enable;
	}

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public String getQueryAccountUrl() {
		return queryAccountUrl;
	}

	public void setQueryAccountUrl(String queryAccountUrl) {
		this.queryAccountUrl = queryAccountUrl;
	}

	public String getGetCallUrl() {
		return getCallUrl;
	}

	public void setGetCallUrl(String getCallUrl) {
		this.getCallUrl = getCallUrl;
	}

	public String getAddRightUrl() {
		return addRightUrl;
	}

	public void setAddRightUrl(String addRightUrl) {
		this.addRightUrl = addRightUrl;
	}

	public String getDelRightUrl() {
		return delRightUrl;
	}

	public void setDelRightUrl(String delRightUrl) {
		this.delRightUrl = delRightUrl;
	}

	public String getUpdRightUrl() {
		return updRightUrl;
	}

	public void setUpdRightUrl(String updRightUrl) {
		this.updRightUrl = updRightUrl;
	}

	public String getBindRightUrl() {
		return bindRightUrl;
	}

	public void setBindRightUrl(String bindRightUrl) {
		this.bindRightUrl = bindRightUrl;
	}

	public String getUnbindRightUrl() {
		return unbindRightUrl;
	}

	public void setUnbindRightUrl(String unbindRightUrl) {
		this.unbindRightUrl = unbindRightUrl;
	}

	public String getPartners() {
		return partners;
	}

	public void setPartners(String partners) {
		this.partners = partners;
	}
	
}
