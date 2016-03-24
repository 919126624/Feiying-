package com.wuyizhiye.basedata.info.dao;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.info.model.Remind;

/**
 * @ClassName InfocenterDao
 * @Description 消息中心
 * @author li.biao
 * @date 2015-4-2
 */
public interface InfocenterDao extends BaseDao {

	int saveRemind(Remind remind);
	
	int deleteRemindById(String id);
	
}
