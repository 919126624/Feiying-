package com.wuyizhiye.cmct.phone.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.exceptions.BusinessException;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.service.impl.DataEntityService;
import com.wuyizhiye.cmct.phone.dao.PhoneDialRecordDao;
import com.wuyizhiye.cmct.phone.model.PhoneDialRecord;
import com.wuyizhiye.cmct.phone.service.CmctMobileService;
import com.wuyizhiye.cmct.phone.util.FjCtCmctMemberUtil;

/**
 * @ClassName CmctMobileServiceImpl
 * @Description 通讯收集接口业务员实现类
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="cmctMobileService")
@Transactional
public class CmctMobileServiceImpl extends DataEntityService implements
		CmctMobileService {

	@Autowired
	private PhoneDialRecordDao phoneDialRecordDao; 
	
	@Override
	protected BaseDao getDao() {
		return phoneDialRecordDao;
	}
	
	/**
	 * dateStr 2014-1 
	 */
	@Override
	public Pagination<PhoneDialRecord> getMobileDialRecordList(int currPage,
			int pageSize, String personId, String keyword, String dateStr)
			throws BusinessException {
		Pagination<PhoneDialRecord> data = new Pagination<PhoneDialRecord>(pageSize, currPage);
		if(!StringUtils.isEmpty(personId)){
			//查找登录人
			Map<String,Object> qparam = new HashMap<String,Object>();
			qparam.put("callPersonId", personId);
			qparam.put("orderByClause", "ic.fcalltime desc ");
			qparam.put("searchStr", keyword);
			List<PhoneDialRecord>records=new ArrayList<PhoneDialRecord>();
			for(int i=0;i<12;i++){//拉取一年的通话记录数据
				if(records.size()<pageSize*currPage){
					setSuffixParam(qparam, dateStr,i);
					try{
						List<PhoneDialRecord>recordsTemp=queryExecutor.execQuery(PhoneDialRecord.MAPPER+".select", qparam, PhoneDialRecord.class);
						records.addAll(recordsTemp);
					}catch(Exception e){
						e.printStackTrace();
						break;
					}
				}else{
					break;
				}
			}
			if(records.size()<currPage*pageSize){				
				records=records.subList((currPage-1)*pageSize, records.size());
			}else{
				records=records.subList((currPage-1)*pageSize, currPage*pageSize);
			}
			data.setItems(records);
		}
		return data;
	}

	@Override
	public Map<String, Object> getMobileDialRecordView(String dialRecordId,String dateStr)
			throws BusinessException {
		Map<String,Object> res = new HashMap<String,Object>();
		Map<String,Object> param = new HashMap<String,Object>();
		if(!StringUtils.isEmpty(dialRecordId) && !StringUtils.isEmpty(dateStr)){
			try{				
				param.put("id", dialRecordId);
				param.put("suffix", "_"+dateStr.replace("-", "_"));
				PhoneDialRecord dialRecord=queryExecutor.execOneEntity(PhoneDialRecord.MAPPER+".select", param, PhoneDialRecord.class);
				res.put("STATE", "SUCCESS");
				res.put("dialData", dialRecord);
			}catch (Exception e) {
				res.put("STATE", "FAIL");
				res.put("MSG", e.getMessage());
			}
		}else{
			res.put("STATE", "FAIL");
			res.put("MSG", "nodata");
		}
		return res;
	}
	
	/**
	 * 
	 * @param param
	 * @param dataStr 时间,可以不传,格式为2015-01
	 * @param i 变量
	 */
	private void setSuffixParam(Map<String, Object>param,String dateStr,int i){
		Date monthTemp=null;
		if(!StringUtils.isEmpty(dateStr)){
			monthTemp=DateUtil.convertStrToDate(dateStr, DateUtil.FORMAT_YYYY_MM);
		}else{
			monthTemp=new Date();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(monthTemp);
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)-i);
		String dateStrTemp=DateUtil.convertDateToStr(cal.getTime(),"yyyy_MM");
		param.put("suffix", "_"+dateStrTemp);
	}

	/**
	 * phone 号码
	 * getType 获取方式 local:本地数据库获取,不填电信接口获取
	 */
	@Override
	public Map<String, Object> getMobileHcode(String phone, String getType) {
		Map<String, Object> result = FjCtCmctMemberUtil.hcodeSearch(phone,"local");
		return result;
	}

	/**
	 * 拨打电话
	 * personId 人员id
	 * calleeNbr 被叫号码
	 * calleeName 被叫姓名
	 */
	@Override
	public Map<String, Object> dialPhoneByMobile(String personId,String calleeNbr,String calleeName) {
		Map<String, Object>resultp=FjCtCmctMemberUtil.dialPhoneByMobile(personId, calleeNbr, calleeName);
		return resultp;
	}
}

