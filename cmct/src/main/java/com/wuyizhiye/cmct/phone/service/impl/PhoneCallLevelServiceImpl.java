package com.wuyizhiye.cmct.phone.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.org.dao.OrgDao;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.person.dao.PersonDao;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.cmct.phone.dao.PhoneCallLevelDao;
import com.wuyizhiye.cmct.phone.model.PhoneCallLevel;
import com.wuyizhiye.cmct.phone.model.PhoneDialRecord;
import com.wuyizhiye.cmct.phone.service.PhoneCallLevelService;
import com.wuyizhiye.cmct.phone.util.DialRecordUtil;

/**
 * @ClassName PhoneCallLevelServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="phoneCallLevelService")
@Transactional
public class PhoneCallLevelServiceImpl extends BaseServiceImpl<PhoneCallLevel> implements PhoneCallLevelService {
	@Autowired
	private PhoneCallLevelDao phoneCallLevelDao;
	
	@Autowired
	private PersonDao PersonDao;
	
	@Autowired
	private OrgDao orgDao;
	
	@Override
	protected BaseDao getDao() {
		return phoneCallLevelDao;
	}
	
	/**
	 * 定时同步通话记录数据,
	 */
	@Override
	public void taskAutoSyncPhoneCall() {
		//1,先查出当前表里数据是否有同步过,有取出同步的时间,作为开始时间,结束时间为当前时间
		Map<String, Object>orderParam=new HashMap<String, Object>();
		orderParam.put("orderBy", "lastTimeDesc");
		List<PhoneCallLevel>cls=queryExecutor.execQuery(PhoneCallLevel.MAPPER+".select", orderParam, PhoneCallLevel.class);
		PhoneCallLevel cl=new PhoneCallLevel();
		Map<String, PhoneCallLevel>contaisCl=new HashMap<String, PhoneCallLevel>();
		Map<String, Object>param=new HashMap<String, Object>();
		if(null!=cls && cls.size()>0){
			cl=cls.get(0);//取出第一条的时间,作为开始时间,因为当前表里数据都一样
			param.put("startTime", null==cl.getLastUpdateTime()?DateUtil.getPrevDay(new Date()):cl.getLastUpdateTime());
			for(PhoneCallLevel pcl:cls){
				if(null!=pcl.getPerson() && !StringUtils.isEmpty(pcl.getPerson().getId())){					
					contaisCl.put(pcl.getPerson().getId(), pcl);
				}
			}
		}
		param.put("endTime", new Date());
		param.put("suffix", DialRecordUtil.getSuffix());
		List<Map>maps=queryExecutor.execQuery(PhoneDialRecord.MAPPER+".getHomeReportNew", param, Map.class);
		List<PhoneCallLevel>addpcl=new ArrayList<PhoneCallLevel>();
		List<PhoneCallLevel>updatepcl=new ArrayList<PhoneCallLevel>();
		if(null!=maps){
			for(Map map:maps){
				if(contaisCl.containsKey(map.get("PERSON_ID"))){
					PhoneCallLevel upCl=contaisCl.get(map.get("PERSON_ID"));
					upCl.setCallSuccCumulative(upCl.getCallSuccCumulative()+Integer.parseInt(map.get("SUCCESSCALL").toString()));
					upCl.setCallTotalCumulative(upCl.getCallTotalCumulative()+Integer.parseInt(map.get("TOTALCALL").toString()));
					upCl.setDurationCumulative(upCl.getDurationCumulative()+Integer.parseInt(map.get("SECONDSUCC").toString()));
					upCl.setLastUpdateTime(new Date());
					updatepcl.add(upCl);
				}else{
					PhoneCallLevel addCl=new PhoneCallLevel();
					Person person=this.PersonDao.getEntityById(map.get("PERSON_ID").toString());
					addCl.setPerson(person);
					addCl.setCallSuccCumulative(Integer.parseInt(map.get("SUCCESSCALL").toString()));
					addCl.setCallTotalCumulative(Integer.parseInt(map.get("TOTALCALL").toString()));
					addCl.setDurationCumulative(Integer.parseInt(map.get("SECONDSUCC").toString()));
					Org org=null;
					if(map.containsKey("ORG_ID") && null!=map.get("ORG_ID") && !StringUtils.isEmpty(map.get("ORG_ID").toString())){
						org=this.orgDao.getEntityById(map.get("ORG_ID").toString());
					}
					addCl.setOrg(org);
					addCl.setLastUpdateTime(new Date());
					addpcl.add(addCl);
				}
			}
		}
		if(addpcl.size()>0){
			this.phoneCallLevelDao.addBatch(addpcl);
		}
		if(updatepcl.size()>0){
			this.phoneCallLevelDao.updateBatch(updatepcl);
		}
	}	
}