package com.wuyizhiye.hr.salary.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.EditController;
import com.wuyizhiye.framework.util.BeanUtils;
import com.wuyizhiye.hr.enums.SchemeObjectTypeEnum;
import com.wuyizhiye.hr.salary.model.SalaryItem;
import com.wuyizhiye.hr.salary.model.SalaryScheme;
import com.wuyizhiye.hr.salary.model.SalarySchemeItem;
import com.wuyizhiye.hr.salary.model.SalarySchemeObject;
import com.wuyizhiye.hr.salary.service.SalaryItemService;
import com.wuyizhiye.hr.salary.service.SalarySchemeService;
import com.wuyizhiye.hr.utils.SalaryFormulaUtil;
@Controller
@RequestMapping(value="hr/salaryScheme/*")
public class SalarySchemeEditController extends EditController {

	@Autowired
	private SalarySchemeService salarySchemeService;
	
	@Autowired
	private SalaryItemService salaryItemService;
	
	@Override
	protected Class getSubmitClass() {
		return SalaryScheme.class;
	}

	@Override
	protected BaseService getService() {
		return salarySchemeService;
	}

	@Override
	protected Object getSubmitEntity(){
		Class c = getSubmitClass();
		if(c==null){
			return null;
		}
		String id = getString("id");
		SalaryScheme entity = null;
		if(!StringUtils.isEmpty(id)){
			entity = (SalaryScheme) getService().getEntityById(id);
		}
		entity = BeanUtils.fillentity(getParamMap(),entity, c);
		entity.setCreateTime(DateUtil.getCurDate());
		entity.setCreator(SystemUtil.getCurrentUser());
		entity.setSalarySchemeItem(getSchemeItems());
		entity.setSalarySchemeObjectList(getSchemeObject());
		return entity;
	}
	
	private List<SalarySchemeItem> getSchemeItems(){
		List<SalarySchemeItem> itemList = new ArrayList<SalarySchemeItem>();
		String itemJson = getString("itemJson");
		List<SalaryItem> salaryItemList = queryExecutor.execQuery("com.wuyizhiye.hr.salary.dao.SalaryItemDao.select", null, SalaryItem.class);
		if(!StringUtils.isEmpty(itemJson)){
			JSONArray array = JSONArray.fromObject(itemJson.replaceAll("\r", "#!#").replaceAll("\n", "~!~"));
			for(int i = 0 ; i< array.size();i++){
				JSONObject obj = array.getJSONObject(i);
				SalarySchemeItem item = new SalarySchemeItem();
				item.setId(UUID.randomUUID().toString());
				item.setFormula(obj.getString("formula").replaceAll("#!#", "").replaceAll("~!~", "\n"));
				item.setFormulaState(1);
				item.setOrder(i);
				item.setSalaryItem(salaryItemService.getEntityById(obj.getString("itemId")));
				if(null!=obj.getString("formula")){
					item.setScript(SalaryFormulaUtil.formula2script(obj.getString("formula").replaceAll("#!#", "").replaceAll("~!~", "\n"), salaryItemList));
				}else{
					item.setScript("");
				}
				itemList.add(item);
			}
		}
		return itemList;
	}
	
	private List<SalarySchemeObject> getSchemeObject(){
		List<SalarySchemeObject> objectList = new ArrayList<SalarySchemeObject>();
		if(getString("objectType").equals(SchemeObjectTypeEnum.COMPANY.toString())){
			SalarySchemeObject object = new SalarySchemeObject();
			object.setId(UUID.randomUUID().toString());
			Org org = queryExecutor.execOneEntity("com.wuyizhiye.basedata.org.dao.OrgDao.getChild", null, Org.class);
			object.setObjId(org.getId());
			objectList.add(object);
		}else if(getString("objectType").equals(SchemeObjectTypeEnum.ORG.toString())){
			String orgId = getString("objectId");
			if(!StringUtils.isEmpty(orgId)){
				String[] orgIds = orgId.split(";");
				for(String id : orgIds){
					SalarySchemeObject object = new SalarySchemeObject();
					object.setId(UUID.randomUUID().toString());
					object.setObjId(id);
					objectList.add(object);
				}
			}
		}else if(getString("objectType").equals(SchemeObjectTypeEnum.JOB.toString())){
			String jobId = getString("objectId");
			if(!StringUtils.isEmpty(jobId)){
				String[] jobIds = jobId.split(";");
				for(String id : jobIds){
					SalarySchemeObject object = new SalarySchemeObject();
					object.setId(UUID.randomUUID().toString());
					object.setObjId(id);
					objectList.add(object);
				}
			}
		}
		
		return objectList;
	}
}
