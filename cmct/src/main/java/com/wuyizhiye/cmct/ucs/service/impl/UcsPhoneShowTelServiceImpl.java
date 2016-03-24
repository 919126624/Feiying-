package com.wuyizhiye.cmct.ucs.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.cmct.phone.enums.StateEnum;
import com.wuyizhiye.cmct.ucs.dao.UcsPhoneShowTelDao;
import com.wuyizhiye.cmct.ucs.model.UcsPhoneShowTel;
import com.wuyizhiye.cmct.ucs.service.UcsPhoneShowTelService;

/**
 * @ClassName UcsPhoneShowTelServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="ucsPhoneShowTelService")
@Transactional
public class UcsPhoneShowTelServiceImpl extends BaseServiceImpl<UcsPhoneShowTel> implements UcsPhoneShowTelService {
	@Autowired
	private UcsPhoneShowTelDao ucsPhoneShowTelDao;
	@Override
	protected BaseDao getDao() {
		return ucsPhoneShowTelDao;
	}
	@Override
	public void addEntity(UcsPhoneShowTel entity) {
		UcsPhoneShowTel st=new UcsPhoneShowTel();
		

			st.setAgent(entity.getAgent());
			st.setCreateTime(new Date());
			st.setCreator(SystemUtil.getCurrentUser());
			st.setState(StateEnum.NO);//刚保存的号码设置成没有选择过的
			st.setAudit(StateEnum.NO);//设置成未审核状态
			
			String showTelMore=entity.getShowTelNoMore();
			if(showTelMore.contains(",")){
				String[]str=showTelMore.split(",");
				for(String showTel:str){
					st.setUUID();
					st.setShowTelNo(showTel);
					super.addEntity(st);
				}
			}else{
				st.setShowTelNo(showTelMore);
				super.addEntity(st);
			}
	}
}