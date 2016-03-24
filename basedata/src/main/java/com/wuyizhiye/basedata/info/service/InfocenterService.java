package com.wuyizhiye.basedata.info.service;

import java.util.Map;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.info.model.InfoTypeEnum;
import com.wuyizhiye.basedata.info.model.Infocenter;
import com.wuyizhiye.basedata.info.model.Remind;
import com.wuyizhiye.basedata.info.model.RemindTypeEnum;

/**
 * @ClassName InfocenterService
 * @Description 消息中心
 * @author li.biao
 * @date 2015-4-2
 */
public interface InfocenterService extends BaseService<Infocenter> {

	void sendInfo(Map<String, Object> map);

	/**
	 * 
	 * @param title 标题
	 * @param type 消息类型
	 * @param content  内容
	 * @param remindType  提醒方式
	 * @param personId  接收人
	 * @param creatorId  创建人
	 * @param objId  对象id
	 * @param url  点击打开url地址
	 */
	void insertInfocenter(String title,InfoTypeEnum type,String content,RemindTypeEnum remindType,String personId,String creatorId,String objId,String url);

	void saveRemind(Remind remind);

	int deleteRemind(String id);

}
