package com.wuyizhiye.basedata.info.service;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.info.model.InfoCenterUnread;

/**
 * @ClassName InfoCenterUnreadService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface InfoCenterUnreadService extends BaseService<InfoCenterUnread>{

	 /**
		 * 跟新未读消息表
		 * @param personId
		 * @param lastContent
		 * @param lastTitle
		 */
	//  public void saveNotRead1(String personId,String lastContent,String lastTitle);
	  
	  /**
		 * 跟新未读消息表
		 * @param personId
		 * @param lastContent
		 * @param lastTitle
		 */
	  public void saveNotRead(InfoCenterUnread data);
}
