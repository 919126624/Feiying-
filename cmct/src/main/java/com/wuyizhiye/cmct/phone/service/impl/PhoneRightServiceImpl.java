package com.wuyizhiye.cmct.phone.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.enums.CommonFlagEnum;
import com.wuyizhiye.basedata.service.impl.DataEntityService;
import com.wuyizhiye.cmct.phone.dao.PhoneRightDao;
import com.wuyizhiye.cmct.phone.model.PhoneRight;
import com.wuyizhiye.cmct.phone.service.PhoneRightService;
import com.wuyizhiye.cmct.phone.util.PhoneRightUtil;

/**
 * @ClassName PhoneRightServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="phoneRightService")
@Transactional
public class PhoneRightServiceImpl extends DataEntityService<PhoneRight> implements PhoneRightService {
	@Autowired
	private PhoneRightDao phoneRightDao;
	@Override
	protected BaseDao getDao() {
		return phoneRightDao;
	}
	@Override
	public void saveRight(PhoneRight p) throws Exception {
		
		if(StringUtils.isNotNull(p.getId())){
			//Map<String,Object> map = new HashMap<String,Object>();
			//map.put("STATE", "SUCCESS");
			Map<String,Object> map = null;
			if(StringUtils.isEmpty(p.getCallRightId())){
				map = PhoneRightUtil.addRight(p);				
			}else{
				map = PhoneRightUtil.editRight(p);
			}
			String state = map.get("STATE").toString();
			if("SUCCESS".equals(state)){
				p.setCallRightId(map.get("callRightsID").toString());
				super.updateEntity(p);
			}else{
				String msg = map.get("MSG").toString();
				String errNumber = map.get("errNumber").toString();
				throw new Exception(msg+" "+errNumber);
			}
		}else{		
			//Map<String,Object> map = new HashMap<String,Object>();
			//map.put("STATE", "SUCCESS");
			Map<String,Object> map = PhoneRightUtil.addRight(p);
			String state = map.get("STATE").toString();
			
			if("SUCCESS".equals(state)){
				p.setCallRightId(map.get("callRightsID").toString());
				super.addEntity(p);
			}else{
				String msg = map.get("MSG").toString();
				String errNumber = map.get("errNumber").toString();
				throw new Exception(msg+" "+errNumber);
			}
		}
	}
	
	@Override
	public void delRight(PhoneRight p) throws Exception {
		//Map<String,Object> map = new HashMap<String,Object>();
		//map.put("STATE", "SUCCESS");
		Map<String,Object> map = PhoneRightUtil.deleteRight(p);
		String state = map.get("STATE").toString();
		if("SUCCESS".equals(state)){
			super.deleteEntity(p);
		}else{
			String msg = map.get("MSG").toString();
			throw new Exception(msg);
		}	
	}	
	
	@Override
	public void updateOrgDefaultRight(PhoneRight pr, String type)
			throws Exception {
		Map<String,Object> map = null;
		if("setDefault".equals(type)){
			map = PhoneRightUtil.bindOrgDefaultRight(pr);
		}else if("setDefaultNone".equals(type)){
			map = PhoneRightUtil.unbindOrgDefaultRight(pr);
		}
		String state = map.get("STATE").toString();
		if("SUCCESS".equals(state)){
			if("setDefault".equals(type)){
				//更新本条权限为默认，该组织下的其他权限为非默认
				String orgInterfaceId = pr.getCallOrgId() ;
				Map<String,Object> queryParam = new HashMap<String,Object>();
				queryParam.put("orgId", orgInterfaceId);
				List<PhoneRight> list = queryExecutor.execQuery("com.wuyizhiye.cmct.phone.dao.PhoneRightDao.select", queryParam, PhoneRight.class);
				if(list!=null && list.size() > 0){
					for(PhoneRight phoneRight : list){
						phoneRight.setOrgDefault(null);
					}
					phoneRightDao.updateBatch(list);
				}
				pr.setOrgDefault(CommonFlagEnum.YES);
				phoneRightDao.updateEntity(pr);
			}else if("setDefaultNone".equals(type)){
				//更新本条权限为非默认
				pr.setOrgDefault(null);
				phoneRightDao.updateEntity(pr);
			}
		}else{
			String msg = map.get("MSG").toString();
			String errNumber = map.get("errNumber").toString();
			throw new Exception(msg+" "+errNumber);
		}
	}
}