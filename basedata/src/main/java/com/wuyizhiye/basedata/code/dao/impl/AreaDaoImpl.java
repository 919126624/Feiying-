package com.wuyizhiye.basedata.code.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.code.dao.AreaDao;
import com.wuyizhiye.basedata.code.model.Area;


/**
 * @ClassName AreaDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="basedataareaDao")
public class AreaDaoImpl extends BaseDaoImpl implements
AreaDao {
	@Override
	protected String getNameSpace() {
		// TODO Auto-generated method stub
		return "com.wuyizhiye.basedata.code.dao.AreaDao";
	}
	
	@Override
	public List<Area> getListByCollection(Map<String,Object> params) {
		// TODO Auto-generated method stub
		List<Area> areaList=(List<Area>) getSqlSession().selectList(getNameSpace() + ".getListByCollection", params);
		return areaList;
	}

}
