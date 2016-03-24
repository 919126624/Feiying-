package com.wuyizhiye.basedata.code.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.code.dao.AreaDao;
import com.wuyizhiye.basedata.code.model.Area;
import com.wuyizhiye.basedata.code.service.AreaService;

/**
 * @ClassName AreaServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="basedataareaService")
@Transactional
public class AreaServiceImpl extends BaseServiceImpl<Area> implements AreaService{
	@Autowired
	private AreaDao areaDao;
	@Override
	protected BaseDao getDao() {
		// TODO Auto-generated method stub
		return areaDao;
	}
	@Override
	public List<Area> getListByCollection(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return areaDao.getListByCollection(params);
	}
	@Override
	public List<Area> getAreaList(String areaType, String parentId) {
		// TODO Auto-generated method stub
		Map<String,Object> params=new HashMap<String, Object>();
		params.put("areaType", areaType);
		params.put("parentId", parentId);
		return areaDao.getListByCollection(params);
	}

}
