package com.wuyizhiye.cmct.phone.dao;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.org.model.Org;

/**
 * @ClassName PhoneDialDetailDao
 * @Description 话单明细实体，（存放接口同步数据）
 * @author li.biao
 * @date 2015-5-26
 */
public interface PhoneDialDetailDao extends BaseDao {
	
	List<Org> getOrgByType(Map<String,Object> param);
	
	int deleteBySyncId(String syncid) ;
}
