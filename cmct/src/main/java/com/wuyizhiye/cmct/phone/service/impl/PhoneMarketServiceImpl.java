package com.wuyizhiye.cmct.phone.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.enums.CommonFlagEnum;
import com.wuyizhiye.basedata.service.impl.DataEntityService;
import com.wuyizhiye.cmct.phone.dao.PhoneMarketCalledDao;
import com.wuyizhiye.cmct.phone.dao.PhoneMarketCallingDao;
import com.wuyizhiye.cmct.phone.dao.PhoneMarketDao;
import com.wuyizhiye.cmct.phone.model.PhoneMarket;
import com.wuyizhiye.cmct.phone.model.PhoneMarketCalled;
import com.wuyizhiye.cmct.phone.model.PhoneMarketCalling;
import com.wuyizhiye.cmct.phone.service.PhoneMarketService;

/**
 * @ClassName PhoneMarketServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="phoneMarketService")
@Transactional
public class PhoneMarketServiceImpl extends DataEntityService<PhoneMarket> implements PhoneMarketService {
	@Autowired
	private PhoneMarketDao phoneMarketDao;
	
	@Autowired
	private PhoneMarketCallingDao phoneMarketCallingDao;
	
	@Autowired
	private PhoneMarketCalledDao phoneMarketCalledDao;
	
	@Override
	protected BaseDao getDao() {
		return phoneMarketDao;
	}
	@Override
	public void addEntity(PhoneMarket entity) {
		/**
		 * 把主叫和被叫号码单独存起来,方便于后续的查看操作
		 */
		entity.setUUID();
		entity.setExecuteStatus(CommonFlagEnum.NO);//未执行
		String calleeNbr=entity.getCalleeNbr();
		if(!StringUtils.isEmpty(calleeNbr)){
			String calleeNbrArr[]=calleeNbr.split(",");
			for(int i=0;i<calleeNbrArr.length;i++){
				if(!StringUtils.isEmpty(calleeNbrArr[i])){
					PhoneMarketCalled pmd=new PhoneMarketCalled();
					pmd.setCreateTime(new Date());
					pmd.setModeID(entity.getModeID());
					pmd.setCalleeNbr(calleeNbrArr[i]);
					pmd.setPhoneMarket(entity);
					this.phoneMarketCalledDao.addEntity(pmd);
				}
			}
		}
		String seatNbr=entity.getSeatNbr();
		if(!StringUtils.isEmpty(seatNbr)){
			String seatNbrArr[]=seatNbr.split(",");
			for(int i=0;i<seatNbrArr.length;i++){
				if(!StringUtils.isEmpty(seatNbrArr[i])){
					PhoneMarketCalling pmg=new PhoneMarketCalling();
					pmg.setCreateTime(new Date());
					pmg.setModeID(entity.getModeID());
					pmg.setSeatNbr(seatNbrArr[i]);
					pmg.setPhoneMarket(entity);
					this.phoneMarketCallingDao.addEntity(pmg);
				}
			}
		}
		super.addEntity(entity);
	}	
	
	public static void main(String[] args) {
		String str="13510628424,,13714825616";
		String strArr[]=str.split(",");
		for(int i=0;i<strArr.length;i++){
//			System.out.println(strArr[i]);
		}
	}
}