package com.wuyizhiye.basedata.util;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.dao.QueryExecutor;
import com.wuyizhiye.base.module.enums.ModuleEnum;
import com.wuyizhiye.base.spring.ApplicationContextAware;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.info.enums.InfomationStatusEnum;
import com.wuyizhiye.basedata.info.model.Infomation;

/**
 * @ClassName InfomationUtils
 * @Description 消息提醒通用类
 * @author li.biao
 * @date 2015-4-2
 */
public class InfomationUtils {

	/**
	 * 新增消息提醒
	 * 
	 * @param infomation
	 */
	public static void insertInfoEntity(Infomation infomation) {
		if (StringUtils.isEmpty(infomation.getId())) {
			infomation.setId(StringUtils.getUUID());
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", infomation.getId()==null?StringUtils.getUUID():infomation.getId());
		param.put("content", infomation.getContent());
		param.put("createDate", infomation.getCreateDate());
		param.put("moduleId", infomation.getModuleId());
		param.put("moduleType", infomation.getModuleType());
		param.put("person", infomation.getPerson());
		param.put("status", infomation.getStatus());
		getQueryExecutor().executeInsert(Infomation.NAME_SPACE + ".insert",
				param);
	}
	
	/**
	 * 修改消息提醒
	 * 
	 * @param infomation
	 */
	public static void updateInfoEntity(Infomation infomation) {
		if (StringUtils.isEmpty(infomation.getId())) {
			return;
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("content", infomation.getContent());
		param.put("createDate", infomation.getCreateDate());
		param.put("moduleId", infomation.getModuleId());
		param.put("moduleType", infomation.getModuleType());
		param.put("person", infomation.getPerson());
		param.put("status", infomation.getStatus());
		getQueryExecutor().executeInsert(Infomation.NAME_SPACE + ".insert",
				param);
	}


	/**
	 * 更新消息提醒
	 * 
	 * @param id
	 *            主键
	 * @param status
	 *            消息状态
	 */
	public static void updateInfoStatus(String ids, String status) {
		Map<String, Object> param = new HashMap<String, Object>();
		String idList[] = null;
		if (ids.indexOf(",") > 0) {
			idList = ids.split(",");
			for (int i = 0; i < idList.length; i++) {
				if (i == 0) {
					ids = "'" + idList[i] + "'";
				} else {
					ids += ",'" + idList[i] + "'";
				}
			}
		} else {
			ids = "'" + ids + "'";
		}
		if (StringUtils.isNotNull(ids)) {
			param.put("ids", ids);
			param.put("status", status);
		}
		getQueryExecutor().executeUpdate(Infomation.NAME_SPACE + ".update",
				param);
	}
	
	/**
	 * 更新消息提醒
	 * 
	 * @param id
	 *            主键
	 * @param status
	 *            消息状态
	 */
	public static void updateNoWarnInfoStatus() {
		Map<String, Object> param = new HashMap<String, Object>();
		 
		param.put("ymdCurDate", new Date());
		param.put("status", InfomationStatusEnum.YES_WARN.getValue());
		param.put("statusCond", "'"+InfomationStatusEnum.NO_WARN.getValue()+"'");
		param.put("person", SystemUtil.getCurrentUser().getId());
		getQueryExecutor().executeUpdate(Infomation.NAME_SPACE + ".updateByCond",
				param);
	}
	 
	/**
	 * 根据关联数据ID修改对应提醒的时间及内容   孙海涛 2014-05-12
	 * @param content 更新内容
	 * @param createDate 更新时间
	 * @param moduleId 关联数据ID
	 */
	public static void updateContentAndDate(String content,Date createDate,String moduleId) {
		Map<String, Object> param = new HashMap<String, Object>();
		 
		param.put("content", content);
		param.put("createDate", createDate);
		param.put("moduleId", moduleId);
		
		Map<String, Object> qparam = new HashMap<String, Object>();
		qparam.put("moduleId", moduleId);
		
		Infomation info=getQueryExecutor().execOneEntity(Infomation.NAME_SPACE + ".select", qparam, Infomation.class);
		if(info!=null){
			 if(info.getCreateDate().getTime()-createDate.getTime()<0){ //如果时间延后  则更新提醒状态
				 param.put("status", InfomationStatusEnum.NO_WARN.toString()); 
			 }
			 getQueryExecutor().executeUpdate(Infomation.NAME_SPACE + ".updateContentAndDate",param);
		}
	}

	/**
	 * 统计消息提醒
	 * 
	 * @param module
	 *            模块
	 * @param personId
	 *            提醒人 (默认查询当前登录人的提醒)
	 * @param status
	 *            状态 (默认查询 ,未查阅 的提醒)
	 * @return
	 */
	public static int getCount(ModuleEnum module, String personId,
			InfomationStatusEnum statusEnum) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("module", module);
		param.put("person", personId == null ? SystemUtil.getCurrentUser()
				.getId() : personId);
		param.put("status", statusEnum == null ? "'"
				+ InfomationStatusEnum.NO_WARN.getValue() + "','"
				+ InfomationStatusEnum.YES_WARN.getValue() + "'" : "'"
				+ statusEnum.getValue() + "'");
		param.put("ymdCurDate", new Date());
		return getQueryExecutor().execQuery(
				
				
				
				Infomation.NAME_SPACE + ".select", param, Infomation.class)
				.size();
	}

	/**
	 * 统计消息提醒,默认查询当前人的所有消息
	 * 
	 * @return
	 */
	public static int getCountByPerson() {
		return getCount(null, null, null);
	}

	/**
	 * 查询提醒信息
	 * 
	 * @param module
	 *            模块
	 * @param personId
	 *            提醒人 (默认查询当前登录人的提醒)
	 * @param status
	 *            状态 (默认查询 ,未查阅 的提醒)
	 * @return
	 */
	public static List<Infomation> getInfoList(Map<String, Object> param) {
		return getQueryExecutor().execQuery(Infomation.NAME_SPACE + ".select",
				param, Infomation.class);
	}

	private static QueryExecutor getQueryExecutor() {
		return ApplicationContextAware.getApplicationContext().getBean(
				"queryExecutor", QueryExecutor.class);
	}

}
