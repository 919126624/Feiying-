package com.wuyizhiye.cmct.phone.service.impl;

import java.util.Date;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.org.dao.OrgDao;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.service.impl.DataEntityService;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.cmct.phone.dao.PhoneMainShowDao;
import com.wuyizhiye.cmct.phone.model.PhoneMainShow;
import com.wuyizhiye.cmct.phone.service.PhoneMainShowService;
import com.wuyizhiye.cmct.phone.util.PhoneMainShowUtil;

/**
 * @ClassName PhoneMainShowServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="phoneMainShowService")
@Transactional
public class PhoneMainShowServiceImpl extends DataEntityService<PhoneMainShow> implements PhoneMainShowService {
	@Autowired
	private PhoneMainShowDao phoneMainShowDao;
	
	@Autowired
	private OrgDao orgDao;
	
	@Override
	protected BaseDao getDao() {
		return phoneMainShowDao;
	}
	@Override
	@Transactional
	public Map<String, Object> matchDisplayNbr(String ids,String orgId) {
		Map<String, Object>resMap=PhoneMainShowUtil.matchDisplayNbr(ids);
		if("SUCCESS".equals(resMap.get("STATE"))){
			String resStr=resMap.get("data").toString();
			JSONArray jsonArr=JSONArray.fromObject(resStr);
			Org org=new Org();
			if(!StringUtils.isEmpty(orgId)){
				org=this.orgDao.getEntityById(orgId);
			}
			for(int i=0;i<jsonArr.size();i++){
				PhoneMainShow mainShow=new PhoneMainShow();
				JSONObject jsonObj=jsonArr.getJSONObject(i);
				mainShow.setCreateTime(new Date());
				mainShow.setChargeNbr(jsonObj.getString("USERID"));
				mainShow.setDisplayNbr(jsonObj.getString("SHOWNUMBER"));
				mainShow.setCreator(SystemUtil.getCurrentUser());
				mainShow.setOrg(org);
				mainShow.setMatchId(jsonObj.getString("ID"));
				if(jsonObj.containsKey("MARK")){					
					mainShow.setDescription(jsonObj.getString("MARK"));
				}
				this.phoneMainShowDao.addEntity(mainShow);
			}
		}
		return resMap;
	}
	@Override
	public Map<String, Object> deleteDisplayNbr(String id) {
		Map<String, Object>resMap=PhoneMainShowUtil.deleteDisplayNbr(id);
		return resMap;
	}	
}