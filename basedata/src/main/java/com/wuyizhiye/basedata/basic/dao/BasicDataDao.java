package com.wuyizhiye.basedata.basic.dao;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.basic.model.BasicData;

/**
 * @ClassName BasicDataDao
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface BasicDataDao extends BaseDao {
	 List<BasicData> getBasicDataList(Map<String,Object> param);
	 BasicData getEntityByNumber(String number);
}
