package com.wuyizhiye.basedata.portlet.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.portlet.dao.PositionPortletDao;
import com.wuyizhiye.basedata.portlet.model.PositionPortlet;
import com.wuyizhiye.basedata.portlet.service.PositionPortletService;

/**
 * @ClassName PositionPortletServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="positionPortletService")
public class PositionPortletServiceImpl extends BaseServiceImpl<PositionPortlet> implements PositionPortletService{

	@Autowired
	private PositionPortletDao positionPortletDao;

	@Override
	protected BaseDao getDao() {
		return this.positionPortletDao;
	}

	@Override
	public void saveAndUpdate(List<PositionPortlet> pplist) {
		for(int i=0;i<pplist.size();i++){
			PositionPortlet pp =pplist.get(i);
			PositionPortlet port = 
			positionPortletDao.getEntityByPositionId(pp.getPosition().getId());
			if(null==port){
				this.positionPortletDao.addEntity(pp);
			}else{
				pp.setId(port.getId());
				this.positionPortletDao.updateEntity(pp);
			}
		}
	}

	@Override
	public PositionPortlet getEntityByPositionId(String position) {
		
		return this.positionPortletDao.getEntityByPositionId(position);
		
	}
	

	
}
