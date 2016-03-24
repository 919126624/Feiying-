package com.wuyizhiye.cmct.phone.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.cmct.phone.dao.PhoneCallInDao;
import com.wuyizhiye.cmct.phone.model.PhoneCallIn;
import com.wuyizhiye.cmct.phone.model.PhoneDeputyNum;
import com.wuyizhiye.cmct.phone.service.PhoneCallInService;
import com.wuyizhiye.cmct.phone.util.PhoneMemberUtil;
import com.wuyizhiye.cmct.phone.util.ProjectMApiRemoteServer;

/**
 * @ClassName PhoneCallInServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="phoneCallInService")
@Transactional
public class PhoneCallInServiceImpl extends BaseServiceImpl<PhoneCallIn> implements PhoneCallInService {
	@Autowired
	private PhoneCallInDao phoneCallInDao;
	@Override
	protected BaseDao getDao() {
		return phoneCallInDao;
	}
	
	@Override
	public void autoTaskSyncBill() {
		Map<String, String>param=new HashMap<String, String>();
		param.put("customerId", ParamUtils.getParamValue(PhoneMemberUtil.CMCT_CUSOMETID));
		/**
		 * 先查找以前是否同步过,找出最后同步的日期
		 */
		List<PhoneCallIn>syncPcs=queryExecutor.execQuery(PhoneCallIn.MAPPER+".select", null, PhoneCallIn.class);
		if(null!=syncPcs && syncPcs.size()>0){
			param.put("startTime", DateUtil.convertDateToStr(syncPcs.get(0).getCreateTime()));
		}
		param.put("endTime", DateUtil.convertDateToStr(DateUtil.getNextDay(new Date())));
		param.put("busType", "('1040','1041')");
		Map<String, Object>result = ProjectMApiRemoteServer.getDeputyNumBill(param);
		if(null!=result && null!=result.get("STATE") && "SUCCESS".equals(result.get("STATE").toString())){
			String billListStr=result.get("billList").toString();
			JSONObject jsonObj=JSONObject.fromObject(billListStr);
			JSONArray jsonArr=jsonObj.getJSONArray("billList");
			Map<String, Object>cotaisKey=new HashMap<String, Object>();
			Map<String, Object>cotaisObj=new HashMap<String, Object>();
			List<PhoneCallIn>pcs=new ArrayList<PhoneCallIn>();
			List<String>sessions=new ArrayList<String>();
			for(int i=0;i<jsonArr.size();i++){
				JSONObject jObjO=jsonArr.getJSONObject(i);
				if(!cotaisKey.containsKey(jObjO.get("SESSIONID"))){
					cotaisKey.put(jObjO.getString("SESSIONID"), jObjO);
					cotaisObj.put(jObjO.getString("SESSIONID"), "FAIL");
				}else{//存在两个相同的sessionId,
					cotaisObj.put(jObjO.getString("SESSIONID"), "SUCCESS");//存在.转接成功
					JSONObject jobjT=(JSONObject) cotaisKey.get(jObjO.getString("SESSIONID"));
					PhoneCallIn phoneCallIn=new PhoneCallIn();
					phoneCallIn.setCreateTime(new Date());
					phoneCallIn.setStatus("1");//成功的
					boolean flag=false;
					if("1040".equals(jobjT.get("BUSTYPE"))){
						flag=true;
						phoneCallIn.setCallerNbr(jobjT.getString("CALLERNBR"));
						phoneCallIn.setCalledNbr(jObjO.getString("CALLEDNBR"));
					}else{
						phoneCallIn.setCallerNbr(jObjO.getString("CALLERNBR"));
						phoneCallIn.setCalledNbr(jobjT.getString("CALLEDNBR"));
					}
					if(flag){
						setCallIn(phoneCallIn,jObjO);
					}else{
						setCallIn(phoneCallIn,jobjT);
					}
					setPerson(phoneCallIn);
					sessions.add(phoneCallIn.getSessionId());
					pcs.add(phoneCallIn);
				}
			}
			for(String key:cotaisObj.keySet()){
				if("FAIL".equals(cotaisObj.get(key))){
					JSONObject jobj=(JSONObject) cotaisKey.get(key);//转接不成功的..只存在一条数据的
					PhoneCallIn phoneCallIn=new PhoneCallIn();
					phoneCallIn.setCreateTime(new Date());
					phoneCallIn.setStatus("0");
					phoneCallIn.setCallerNbr(jobj.getString("CALLERNBR"));
					phoneCallIn.setCalledNbr(jobj.getString("CALLEDNBR"));
					setCallIn(phoneCallIn, jobj);
					setPerson(phoneCallIn);
					pcs.add(phoneCallIn);
					sessions.add(phoneCallIn.getSessionId());
				}
			}
			if(pcs.size()>0){
				this.phoneCallInDao.deleteBatch(sessions);
				this.phoneCallInDao.addBatch(pcs);
			}
		}
	}	
	
	public void setPerson(PhoneCallIn phoneCallIn){
		phoneCallIn.setCreateTime(new Date());
		String calledNbr=phoneCallIn.getCalledNbr();
		Map<String, Object>paramNbr=new HashMap<String, Object>();
		paramNbr.put("billNumber", calledNbr);
		List<PhoneDeputyNum> pdns = queryExecutor.execQuery(PhoneDeputyNum.MAPPER+".select", paramNbr, PhoneDeputyNum.class);
		if(null!=pdns && pdns.size()>0){
			phoneCallIn.setDeputyNum(pdns.get(0));
			phoneCallIn.setUsePerson(pdns.get(0).getBindPerson());
		}
	}
	
	public void setCallIn(PhoneCallIn phoneCallIn,JSONObject jObj){
		phoneCallIn.setSessionId(jObj.getString("SESSIONID"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			phoneCallIn.setStartTime(jObj.getString("STARTTIME")==null || "".equals(jObj.getString("STARTTIME"))?null:sdf.parse(jObj.getString("STARTTIME")));
			phoneCallIn.setEndTime(jObj.getString("ENDTIME")==null || "".equals(jObj.getString("ENDTIME"))?null:sdf.parse(jObj.getString("ENDTIME")));
			phoneCallIn.setCallDuration(jObj.getString("CALLDURATION")==null || "".equals(jObj.getString("CALLDURATION"))?0:Integer.parseInt(jObj.getString("CALLDURATION")));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}