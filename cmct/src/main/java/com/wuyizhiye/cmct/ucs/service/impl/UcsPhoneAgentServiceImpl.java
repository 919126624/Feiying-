package com.wuyizhiye.cmct.ucs.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.cmct.ucs.dao.UcsPhoneAgentDao;
import com.wuyizhiye.cmct.ucs.dao.UcsPhoneMemberDao;
import com.wuyizhiye.cmct.ucs.model.UcsPhoneAgent;
import com.wuyizhiye.cmct.ucs.model.UcsPhoneMember;
import com.wuyizhiye.cmct.ucs.service.UcsPhoneAgentService;

/**
 * @ClassName UcsPhoneAgentServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="ucsPhoneAgentService")
@Transactional
public class UcsPhoneAgentServiceImpl extends BaseServiceImpl<UcsPhoneAgent> implements UcsPhoneAgentService {
	@Autowired
	private UcsPhoneAgentDao ucsPhoneAgentDao;
	
	@Autowired
	private UcsPhoneMemberDao ucsPhoneMemberDao;
	@Override
	protected BaseDao getDao() {
		return ucsPhoneAgentDao;
	}
	@Override
	public UcsPhoneAgent getDealerAgent(Map<String, Object> param) {
		List<UcsPhoneAgent>uas=queryExecutor.execQuery("com.wuyizhiye.cmct.ucs.dao.UcsPhoneAgentDao.getDealerAgent", param, UcsPhoneAgent.class);
		if(null!=uas){
			return uas.get(0);
		}
		return null;
	}
	@Override
	public void deleteById(String id) {
		/**
		 * 删除一个企业用户的时候,改企业下的所有坐席归属于上级管理
		 */
		
		/**
		 * 得到该企业的上级的agentId
		 */
		UcsPhoneAgent agent=getEntityById(id);
		String parentAgentId=agent.getDealerId();
		Map<String, Object>param=new HashMap<String, Object>();
		param.put("dealerId", parentAgentId);
		UcsPhoneAgent parentAg=getDealerAgent(param);
		/**
		 * 得到该企业下的所有坐席
		 */
		List<UcsPhoneMember>mems=queryExecutor.execQuery("com.wuyizhiye.cmct.ucs.dao.UcsPhoneMemberDao.select", param, UcsPhoneMember.class);
		
		for(UcsPhoneMember pm:mems){
			/**
			 * 循环设置agent.id
			 */
			pm.setUcsPhoneAgent(parentAg);
		}
		ucsPhoneMemberDao.updateBatch(mems);
		super.deleteById(id);
	}
}