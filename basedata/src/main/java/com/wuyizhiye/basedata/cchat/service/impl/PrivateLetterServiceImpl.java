package com.wuyizhiye.basedata.cchat.service.impl;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.cchat.dao.PrivateLetterDao;
import com.wuyizhiye.basedata.cchat.model.CChat;
import com.wuyizhiye.basedata.cchat.service.PrivateLetterService;
import com.wuyizhiye.basedata.util.SystemUtil;

/**
 * @ClassName PrivateLetterServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="basedataprivateLetterService")
public class PrivateLetterServiceImpl extends BaseServiceImpl<CChat> implements
		PrivateLetterService {
	
	@Autowired
	private PrivateLetterDao privateLetterDao;

	
	@Override
	protected BaseDao getDao() {
		return privateLetterDao;
	}
	
	@Override
	public void addEntity(CChat entity) {
		//加入默认组织
		if(null==entity.getOrg()){
			entity.setOrg(SystemUtil.getCurrentOrg());
		}
		super.addEntity(entity);
	}

	@Override
	public int selectLetterCount(Map<String, Object> map) {
		return privateLetterDao.selectLetterCount(map);
	}
	
	
}
