package com.wuyizhiye.basedata.info.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.info.dao.InfoCenterUnreadDao;
import com.wuyizhiye.basedata.info.model.InfoCenterUnread;
import com.wuyizhiye.basedata.info.service.InfoCenterUnreadService;

/**
 * @ClassName InfoCenterUnreadServiceImpl
 * @Description 消息中心未读数据Service
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="infoCenterUnreadService")
public class InfoCenterUnreadServiceImpl extends BaseServiceImpl<InfoCenterUnread> implements InfoCenterUnreadService  {

	private static Logger log =Logger.getLogger(InfoCenterUnreadServiceImpl.class); 
	@Autowired
	private  InfoCenterUnreadDao  infoCenterUnreadDao;
	 
	
	/**
	 * 跟新未读消息表
	 * @param personId
	 * @param lastContent
	 * @param lastTitle
	 */
 /* public void saveNotRead(String personId,String lastContent,String lastTitle){
		Map<String,Object> param=new HashMap<String, Object>();
		param.put("personId", personId);
		int count=queryExecutor.execCount(InfoCenterUnread.NAME_SPACE+".getById", param);
		
		param.put("lastContent",lastContent);//最后消息内容
		param.put("lastTitle", lastTitle);//最后消息标题
		param.put("isOpenDialog", 1);//是否弹窗
		if(count>0){
			queryExecutor.executeInsert(InfoCenterUnread.NAME_SPACE+".updateByPerson", param);
		}else{
			queryExecutor.executeInsert(InfoCenterUnread.NAME_SPACE+".insertByPerson", param);
		}
	}*/
	/**
	 * 跟新未读消息表
	 * @param personId
	 * @param lastContent
	 * @param lastTitle
	 */
public void saveNotRead(InfoCenterUnread data){
		Map<String,Object> param=new HashMap<String, Object>();
		param.put("id", data.getId());
		int count=queryExecutor.execCount(InfoCenterUnread.NAME_SPACE+".getById", param);
		//data.setNeedOpenDialog(1);//是否弹窗
		if(count>0){
			queryExecutor.executeInsert(InfoCenterUnread.NAME_SPACE+".updateByPerson", data);
		}else{
			queryExecutor.executeInsert(InfoCenterUnread.NAME_SPACE+".insertByPerson", data);
		}
	}

	@Override
	protected BaseDao getDao() {
		return infoCenterUnreadDao;
	}
}
