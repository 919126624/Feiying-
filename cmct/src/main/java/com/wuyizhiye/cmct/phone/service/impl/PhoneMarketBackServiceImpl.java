package com.wuyizhiye.cmct.phone.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.enums.CommonFlagEnum;
import com.wuyizhiye.cmct.phone.dao.PhoneDxCallLogDao;
import com.wuyizhiye.cmct.phone.dao.PhoneMarketBackDao;
import com.wuyizhiye.cmct.phone.dao.PhoneMarketDao;
import com.wuyizhiye.cmct.phone.dao.PhoneMarketDetailDao;
import com.wuyizhiye.cmct.phone.enums.PhoneDxLogEnum;
import com.wuyizhiye.cmct.phone.model.PhoneDxCallLog;
import com.wuyizhiye.cmct.phone.model.PhoneMarket;
import com.wuyizhiye.cmct.phone.model.PhoneMarketBack;
import com.wuyizhiye.cmct.phone.model.PhoneMarketDetail;
import com.wuyizhiye.cmct.phone.service.PhoneMarketBackService;

/**
 * @ClassName PhoneMarketBackServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="phoneMarketBackService")
@Transactional
public class PhoneMarketBackServiceImpl extends BaseServiceImpl<PhoneMarketBack> implements PhoneMarketBackService {
	@Autowired
	private PhoneMarketBackDao phoneMarketBackDao;

	@Autowired
	private PhoneMarketDetailDao phoneMarketDetailDao;
	
	@Autowired
	private PhoneDxCallLogDao phoneDxCallLogDao;
	
	@Autowired
	private PhoneMarketDao phoneMarketDao;
	
	@Override
	protected BaseDao getDao() {
		return phoneMarketBackDao;
	}
	@Override
	public void addEntity(PhoneMarketBack entity) {
		PhoneMarket phoneMarket=entity.getPhoneMarket();
		if(null!=phoneMarket && "NO".equals(phoneMarket.getExecuteStatus().getValue())){
			phoneMarket.setExecuteStatus(CommonFlagEnum.YES);//已执行
			phoneMarketDao.updateEntity(phoneMarket);
		}
		if("17".equals(entity.getType())){//批量发送营销任务发送状态推送
			String marketState=entity.getCallState();
			if(!StringUtils.isEmpty(marketState)){
				conversionState(entity);
			}
		}
		super.addEntity(entity);
	}	
	
	public void conversionState(PhoneMarketBack entity){
		String stateLongArr[]=entity.getCallState().split(",");
		List<PhoneMarketDetail>pmds=new ArrayList<PhoneMarketDetail>();
		SimpleDateFormat simp=new SimpleDateFormat("yyyyMMddhhmmss");
		try{
			for(int i=0;i<stateLongArr.length;i++){
				if(!StringUtils.isEmpty(stateLongArr[i])){
					String stateShortArr[]=stateLongArr[i].split("\\$");

					PhoneMarketDetail pmd=new PhoneMarketDetail();
					pmd.setPhoneMarket(entity.getPhoneMarket());
					pmd.setPhoneMarketBack(entity);
					pmd.setWorkID(entity.getWorkID());
					pmd.setCreateTime(new Date());
					pmd.setCalleeNbr(stateShortArr[0]);
					pmd.setSessionId(stateShortArr[1]);
					pmd.setStatus(stateShortArr[2]);
					pmd.setCallDuration(stateShortArr[3]);
					pmd.setTransferDuration(stateShortArr[4]);
					pmd.setTransferNumber(stateShortArr[5]);
					if(!StringUtils.isEmpty(stateShortArr[6])){
						pmd.setStartTimeO(simp.parse(stateShortArr[6]));
					}else{
						pmd.setStartTimeO(null);
					}
					if(!StringUtils.isEmpty(stateShortArr[7])){
						pmd.setStartTimeT(simp.parse(stateShortArr[7]));
					}else{
						pmd.setStartTimeT(null);
					}
					if(!StringUtils.isEmpty(stateShortArr[8])){						
						pmd.setEndTime(simp.parse(stateShortArr[8]));
					}else{
						pmd.setEndTime(null);
					}
					pmd.setKeyValue(stateShortArr[9]);
					pmds.add(pmd);
					
					PhoneDxCallLog dxCallLog=new PhoneDxCallLog();
					dxCallLog.setSessionId(pmd.getSessionId());
					dxCallLog.setStatus(PhoneDxLogEnum.MARKETSTATUS);
					dxCallLog.setLogDetail(pmd.getStatus());
					dxCallLog.setCurrentDate(new Date());
					phoneDxCallLogDao.addEntity(dxCallLog);
				}
			}
			if(pmds.size()>0){
				this.phoneMarketDetailDao.addBatch(pmds);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}