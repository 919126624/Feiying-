package com.wuyizhiye.basedata.service;

import java.util.List;
import java.util.Map;

import javax.naming.NoPermissionException;

import net.sf.json.JSONObject;

import com.wuyizhiye.base.exceptions.BusinessException;
import com.wuyizhiye.base.service.BaseService;

/**
 * @ClassName BasedataMobileService
 * @Description 基础模块手机接口
 * @author li.biao
 * @date 2015-4-2
 */
public interface BasedataMobileService extends BaseService{

	/**
	 * 手机接口--登录
	 * @param userName 登录名
	 * @param password 登录密码
	 * @param dataCenter 数据中心
	 * @param signature 限制标识
	 * @return json对象
	 * id 主键
	 * name 名字
	 * userName 登录名
	 * number 工号 
	 * password 密码
	 * photo 头像地址
	 * orgId 组织id
	 * orgName 组织名称
	 * orgLongNumber 组织长编码
	 * @throws BusinessException
	 */
	public Map<String,Object> mobileLogin(String userName,String password,String dataCenter,String signature) throws BusinessException , NoPermissionException;
	
	/**
	 * 手机接口 -- 获取手机端有权限的菜单
	 * @param personId 登录人Id
	 * @return
	 * 菜单集合
	 * id：主键Id，number：菜单编号，link：菜单链接，miniIcon：菜单小图标，largeIcon：菜单大图标
	 * @throws BusinessException
	 */
	public List<JSONObject> mobileMeum(String personId) throws BusinessException ;
	
	/**
	 * 获取登录人头部简要信息
	 * @param personId
	 * @return
	 * callLevle：级别，callLevleName:级别名称，autograph:心情签名
	 * @throws BusinessException
	 */
	public Map<String,Object> mobileHeadInfo(String personId) throws BusinessException ;
	
}
