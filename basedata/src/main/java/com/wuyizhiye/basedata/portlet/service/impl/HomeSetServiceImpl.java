package com.wuyizhiye.basedata.portlet.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.portlet.dao.HomeSetDao;
import com.wuyizhiye.basedata.portlet.model.HomeSet;
import com.wuyizhiye.basedata.portlet.model.PortletLayout;
import com.wuyizhiye.basedata.portlet.service.HomeSetService;
import com.wuyizhiye.basedata.portlet.service.PortletLayoutService;
import com.wuyizhiye.basedata.service.impl.DataEntityService;

/**
 * @ClassName HomeSetServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Transactional
@Component(value="homeSetService")
public class HomeSetServiceImpl extends DataEntityService<HomeSet> implements HomeSetService{

	@Autowired
	private HomeSetDao homeSetDao;
	
	@Autowired
	private PortletLayoutService portletLayoutService;

	@Override
	protected BaseDao getDao() {
		return homeSetDao;
	}

	@Override
	public void updateStatus(HomeSet portlet) {
		this.homeSetDao.updateStatus(portlet);
	}

	@Override
	public void updateSelective(HomeSet portlet) {
		this.homeSetDao.updateSelective(portlet);
	}

	@Override
	public void addHomeSet(HomeSet h, List<PortletLayout> plist,boolean add) {
		
		if(add){
			this.addEntity(h);
			
		}else{
			this.portletLayoutService.deleteByParent(h.getId());
			this.updateEntity(h);
		}
		
		this.portletLayoutService.addBatch(plist);
	}

	@Override
	public void updateDefault(HomeSet portlet) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("isdefault", 1);
		List<HomeSet> hslist = this.homeSetDao.querylist(map);
		List<HomeSet> uplist = new ArrayList<HomeSet>();
		for(int i=0;i<hslist.size();i++){		
			HomeSet temp = new HomeSet();
			temp.setId(hslist.get(i).getId());
			temp.setIsdefault(0);
			uplist.add(temp);
		}
		uplist.add(portlet);
		for(int i=0;i<uplist.size();i++){
			this.homeSetDao.updateSelective(uplist.get(i));
		}
	}
	
	
}
