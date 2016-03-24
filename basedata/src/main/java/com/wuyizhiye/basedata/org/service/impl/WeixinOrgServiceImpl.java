package com.wuyizhiye.basedata.org.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.org.dao.WeixinOrgDao;
import com.wuyizhiye.basedata.org.model.WeixinOrg;
import com.wuyizhiye.basedata.org.service.WeixinOrgService;
import com.wuyizhiye.basedata.service.impl.DataEntityService;

/**
 * @ClassName WeixinOrgServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="weixinOrgService")
@Transactional
public class WeixinOrgServiceImpl extends DataEntityService<WeixinOrg> implements WeixinOrgService {
	@Autowired
	private WeixinOrgDao weixinOrgDao;
	@Override
	protected BaseDao getDao() {
		return weixinOrgDao;
	}	
	@Override
	public void addEntity(WeixinOrg entity) {
		entity.setLongNumber(entity.getNumber());
		//entity.setLevel(1);
		entity.setLeaf(true);
		DateFormat  timesTampFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timesTampFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

//		entity.setDisabledDate();
		WeixinOrg parent = null;
		if(entity.getParent()!=null && !StringUtils.isEmpty(entity.getParent().getId())){
			parent = getEntityById(entity.getParent().getId());
			entity.setLongNumber(parent.getLongNumber() + "!" + entity.getNumber());
			//entity.setLevel(parent.getLevel() + 1);
			parent.setLeaf(false);
			super.updateEntity(parent);
		}
		super.addEntity(entity);
	}
	
	@Override
	public void updateEntity(WeixinOrg entity) {
		WeixinOrg oldEntity = getEntityById(entity.getId());
		entity.setLongNumber(entity.getNumber());
		WeixinOrg parent = null;
		if(entity.getParent()!=null){
			parent = getEntityById(entity.getParent().getId());
			entity.setLongNumber(parent.getLongNumber() + "!" + entity.getNumber());
			//entity.setLevel(parent.getLevel() + 1);
			parent.setLeaf(false);
			super.updateEntity(parent);
		}
		super.updateEntity(entity);
	}
}