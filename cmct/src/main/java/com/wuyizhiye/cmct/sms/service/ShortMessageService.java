package com.wuyizhiye.cmct.sms.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.cmct.sms.model.ShortMessage;

/**
 * @ClassName ShortMessageService
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
public interface ShortMessageService extends BaseService<ShortMessage>{

	/**
	 * 发送短信
	 * @param groupId 企业ID
	 * @param username 账号
	 * @param password 密码
	 * @param from 发送号码
	 * @param to   接收号码  (多个号码之间用半角逗号隔开) 
	 * @param content 短信内容
	 * @return
	 * @throws Exception
	 */
	public  Map<String,Object> sendMessage(String groupId,String accountName,String password,String url,String from,String to ,String content,Date sendTime)throws Exception;
	
	/**
	 * 查询短信账户余额
	 * @param groupId 企业ID
	 * @param username 账号
	 * @param password 密码
	 * @return
	 * @throws Exception
	 */
	public  String getSmsBalance(String groupId,String accountName,String password ,String url)throws Exception;
	
	
	/**
	 * 查看 短信回复
	  * @param groupId 企业ID
	 * @param username 账号
	 * @param password 密码
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> getReplyData(String groupId,String accountName,String password ,String url) throws Exception;
	
	
	/**
	 * 获取短信发送状态
	  * @param groupId 企业ID
	 * @param username 账号
	 * @param password 密码
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> getMessageStatus(String groupId,String accountName,String password ,String url) throws Exception;
}
