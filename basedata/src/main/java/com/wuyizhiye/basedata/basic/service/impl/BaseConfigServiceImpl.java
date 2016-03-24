package com.wuyizhiye.basedata.basic.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.basic.dao.BaseConfigDao;
import com.wuyizhiye.basedata.basic.model.BaseConfig;
import com.wuyizhiye.basedata.basic.service.BaseConfigService;

/**
 * @ClassName BaseConfigServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="baseConfigService")
@Transactional
public class BaseConfigServiceImpl extends BaseServiceImpl<BaseConfig> implements
		BaseConfigService {

	@Autowired
	private BaseConfigDao baseConfigDao;
	
	@Override
	protected BaseDao getDao() {
		return baseConfigDao;
	}

	@Override
	public void updateList(List<BaseConfig> list) {
		
		for(int i=0;i<list.size();i++){
			BaseConfig cf = list.get(i);
			BaseConfig cftemp = 
					this.baseConfigDao.getBaseConfigByNum(cf.getNumber());
			if(null!=cftemp){
				cf.setId(cftemp.getId());
				this.baseConfigDao.updateEntity(cf);
			}else{
				this.baseConfigDao.addEntity(cf);
			}
		}
	}

	

}
