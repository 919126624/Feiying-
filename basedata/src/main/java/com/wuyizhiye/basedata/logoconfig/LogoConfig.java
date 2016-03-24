package com.wuyizhiye.basedata.logoconfig;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.enums.LoginPageModelEnum;

/**
 * @ClassName LogoConfig
 * @Description logo配置
 * @author li.biao
 * @date 2015-4-2
 */
public class LogoConfig extends CoreEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2404431440042779399L;
	 
	
	private String logoUrl;  //首页Logo
	
	private String logoUrl4Login;//登录页面Logo
	
	
	private String logoConfigphotoids;// 冗余
	
	private String waiWangUrl;  //外网路径
	
	private String jiangHuUrl;  //外网路径
	
	private String toolCheck;//首页工具箱 显示内容
	
	private String downLoadUrl;  //浏览器下载地址
	
	private String attribute1;  //备用字段1
	
	private String attribute2;  //备用字段2
	
	private String attribute3;  //备用字段2
	
	private LoginPageModelEnum  loginPageModel;//首页模板
	
	private String quickMarkUrl;  //二维码路径
	
	private String quickMarkDescribe;  //二维码说明
	

	
	public String getQuickMarkUrl() {
		return quickMarkUrl;
	}

	public void setQuickMarkUrl(String quickMarkUrl) {
		this.quickMarkUrl = quickMarkUrl;
	}

	public String getQuickMarkDescribe() {
		return quickMarkDescribe;
	}

	public void setQuickMarkDescribe(String quickMarkDescribe) {
		this.quickMarkDescribe = quickMarkDescribe;
	}


	public LoginPageModelEnum getLoginPageModel() {
		return loginPageModel;
	}

	public void setLoginPageModel(LoginPageModelEnum loginPageModel) {
		this.loginPageModel = loginPageModel;
	}

	public String getToolCheck() {
		return toolCheck;
	}

	public void setToolCheck(String toolCheck) {
		this.toolCheck = toolCheck;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getLogoUrl4Login() {
		return logoUrl4Login;
	}

	public void setLogoUrl4Login(String logoUrl4Login) {
		this.logoUrl4Login = logoUrl4Login;
	}

	public String getLogoConfigphotoids() {
		return logoConfigphotoids;
	}

	public void setLogoConfigphotoids(String logoConfigphotoids) {
		this.logoConfigphotoids = logoConfigphotoids;
	}

	public String getWaiWangUrl() {
		return waiWangUrl;
	}

	public void setWaiWangUrl(String waiWangUrl) {
		this.waiWangUrl = waiWangUrl;
	}

	public String getJiangHuUrl() {
		return jiangHuUrl;
	}

	public void setJiangHuUrl(String jiangHuUrl) {
		this.jiangHuUrl = jiangHuUrl;
	}

	public String getDownLoadUrl() {
		return downLoadUrl;
	}

	public void setDownLoadUrl(String downLoadUrl) {
		this.downLoadUrl = downLoadUrl;
	}

	public String getAttribute1() {
		return attribute1;
	}

	public void setAttribute1(String attribute1) {
		this.attribute1 = attribute1;
	}

	public String getAttribute2() {
		return attribute2;
	}

	public void setAttribute2(String attribute2) {
		this.attribute2 = attribute2;
	}

	public String getAttribute3() {
		return attribute3;
	}

	public void setAttribute3(String attribute3) {
		this.attribute3 = attribute3;
	}
}
