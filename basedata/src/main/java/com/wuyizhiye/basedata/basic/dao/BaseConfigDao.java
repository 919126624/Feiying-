package com.wuyizhiye.basedata.basic.dao;

import java.util.List;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.basic.model.BaseConfig;

/**
 * @ClassName BaseConfigDao
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface BaseConfigDao extends BaseDao {
	
	 public BaseConfig getBaseConfigByNum(String number);
	 
	 public void addOrupdate(List<BaseConfig> cflist);
}
