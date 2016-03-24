package com.wuyizhiye.cmct.phone.service;

import java.util.Map;

import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.exceptions.BusinessException;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.cmct.phone.model.PhoneDialRecord;

/**
 * @ClassName CmctMobileService
 * @Description 通讯手机接口实现类
 * @author li.biao
 * @date 2015-5-26
 */
public interface CmctMobileService extends BaseService {
	
	/**
	 * @param currPage 当前页
	 * @param pageSize 每页数量
	 * @param personId 登录人id
	 * @param keyword 模糊匹配
	 * @param dateStr 时间字符
	 * @return
	 * @throws BusinessException
	 */
	public Pagination<PhoneDialRecord> getMobileDialRecordList(int currPage , int pageSize ,String personId,String keyword,String dateStr) throws BusinessException;
	
	/**
	 * 
	 * @param dialRecordId 通话记录id
	 * @return
	 * @throws BusinessException
	 */
	public Map<String,Object> getMobileDialRecordView(String dialRecordId,String dateStr) throws BusinessException;
	
	/**
	 * 获取来电归属地
	 */
	public Map<String, Object>getMobileHcode(String phone,String getType);
	
	/**
	 * 拨打电话
	 */
	public Map<String, Object>dialPhoneByMobile(String personId,String calleeNbr,String calleeName);
}
