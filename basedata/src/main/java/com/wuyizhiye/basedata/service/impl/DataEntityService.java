package com.wuyizhiye.basedata.service.impl;

import java.util.Date;

import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.DataEntity;
import com.wuyizhiye.basedata.util.SystemUtil;

/**
 * @ClassName DataEntityService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 * @param <T>
 */
public abstract class DataEntityService<T extends DataEntity> extends BaseServiceImpl<T> {
	
	@Override
	public void addEntity(T entity) {
		if(entity!=null){
			if(entity.getCreateTime()==null){
				entity.setCreateTime(new Date());
			}
			if(entity.getCreator()==null){
				entity.setCreator(SystemUtil.getCurrentUser());
			}
			if(entity.getControlUnit()==null){
				entity.setControlUnit(SystemUtil.getCurrentControlUnit());
			}
			if(entity.getLastUpdateTime()==null){
				entity.setLastUpdateTime(new Date());
			}
			if(entity.getUpdator()==null){
				entity.setUpdator(SystemUtil.getCurrentUser());
			}
			if(entity.getOrg()==null){
				entity.setOrg(SystemUtil.getCurrentOrg());
			}
		}
		super.addEntity(entity);
	}
	
	@Override
	public void updateEntity(T entity) {
		if(entity!=null){
			entity.setLastUpdateTime(new Date());
			entity.setUpdator(SystemUtil.getCurrentUser());
			if(entity.getControlUnit()==null){
				entity.setControlUnit(SystemUtil.getCurrentControlUnit());
			}
		}
		super.updateEntity(entity);
	}
}
