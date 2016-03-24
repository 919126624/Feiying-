package com.wuyizhiye.basedata.info.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.info.dao.InfomationDao;
import com.wuyizhiye.basedata.info.enums.InfomationStatusEnum;
import com.wuyizhiye.basedata.info.model.Infomation;
import com.wuyizhiye.basedata.info.service.InfomationService;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.service.impl.DataEntityService;

/**
 * @ClassName InfomationServiceImpl
 * @Description 消息提醒
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value = "infomationService")
@Transactional
public class InfomationServiceImpl extends DataEntityService<Infomation>
		implements InfomationService {

	@Autowired
	private InfomationDao infomationDao;

	@Override
	protected BaseDao getDao() {
		return infomationDao;
	}

	@Override
	public void sendInfo(Map<String, Object> map) {
		String ids = map.get("ids").toString();
		String content = map.get("content").toString();
		String personId = map.get("personId").toString();
		String orgId = map.get("orgId").toString();
		String[] receiveIds = ids.split(",");
		List<Infomation> infolist = new ArrayList<Infomation>();
		for(String receiveId : receiveIds){
			Infomation info = new Infomation();
			Person p = new Person();
			info.setContent(content);
			p.setId(receiveId);
			info.setPerson(p);
			info.setModuleType("BASEDATA");
			info.setCreateDate(new Date());
			info.setStatus(InfomationStatusEnum.NO_WARN);
			infolist.add(info);
		}
		this.addBatch(infolist);
		
	}
	
	

}
