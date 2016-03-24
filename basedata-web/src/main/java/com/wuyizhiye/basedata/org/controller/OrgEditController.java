package com.wuyizhiye.basedata.org.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.Geohash;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.org.enums.OrgTypeEnum;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.service.OrgService;
import com.wuyizhiye.framework.base.controller.EditController;
import com.wuyizhiye.framework.compoment.PinyinController;

/**
 * @ClassName OrgEditController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/org/*")
public class OrgEditController extends EditController {
	private static Logger logger=Logger.getLogger(OrgEditController.class);
	@Autowired
	private OrgService orgService;
	@Override
	protected Class<Org> getSubmitClass() {
		return Org.class;
	}
	
	@Override
	protected Object getSubmitEntity() {
		Org org = (Org) super.getSubmitEntity();
		if(OrgTypeEnum.GROUP.equals(org.getOrgType()) || OrgTypeEnum.COMPANY.equals(org.getOrgType()) || OrgTypeEnum.NDEPENDENT_COMPANY.equals(org.getOrgType())){
			org.setBusinessTypes(null);
		}else{
			String[] btypes = getRequest().getParameterValues("businessType");
			StringBuilder sbuild = new StringBuilder();
			for(String s : btypes){
				sbuild.append(s).append(";");
			}
			org.setBusinessTypes(sbuild.toString());
		}
		return org;
	}

	@Override
	protected BaseService<Org> getService() {
		return orgService;
	}
	
	@Override
	protected boolean validate(Object data) {
		Org entity = (Org) data;
		if(StringUtils.isEmpty(entity.getName())){
			getOutputMsg().put("MSG", "名称不能为空");
			return false;
		}
		if(StringUtils.isEmpty(entity.getNumber())){
			getOutputMsg().put("MSG", "编码不能为空");
			return false;
		}
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("number", entity.getNumber());
		List<Org> values = queryExecutor.execQuery("com.wuyizhiye.basedata.org.dao.OrgDao.select", param, Org.class);
		for(Org g : values){
			if(!g.getId().equals(entity.getId())){
				getOutputMsg().put("MSG", "该编码己存在");
				return false;
			}
		}
		param.clear();
		values = queryExecutor.execQuery("com.wuyizhiye.basedata.org.dao.OrgDao.select", param, Org.class);
		Org org = orgService.getEntityById(entity.getParent().getId());
		for (Org orgNumber : values) {
			if(entity.getNumber().contains(orgNumber.getNumber())){
				if(!entity.getNumber().contains(org.getNumber())) {
					getOutputMsg().put("MSG", "该编码要与系统中直接上级已经存在的编码有包含关系");
					return false;
				} else if(entity.getParent().getId().equals(orgNumber.getId())){
					if(!orgNumber.getNumber().equals(org.getNumber())){
						getOutputMsg().put("MSG", "该编码要与系统中直接上级已经存在的编码有包含关系");
						return false;
					}
				} else {
					
				}
			}
		}
		if(entity.getParent()!=null){
			//同一组织下的组织不能同名
			param.put("parent", entity.getParent().getId());
		}
		param.put("name", entity.getName());
		values = queryExecutor.execQuery("com.wuyizhiye.basedata.org.dao.OrgDao.select", param, Org.class);
		for(Org g : values){
			if(!g.getId().equals(entity.getId())){
				getOutputMsg().put("MSG", "该组织名称冲突");
				return false;
			}
		}
 		return super.validate(data);
	}
	
	@RequestMapping(value="save")
	public void save(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		Org data = (Org) getSubmitEntity();
		if(validate(data)){
			if(data instanceof Org){
				if(StringUtils.isEmpty(data.getId())){
					getService().addEntity(data);
	
				}else{
					getService().updateEntity(data);
					Org dbOrg = getService().getEntityById(data.getId());
		
				} 
				getOutputMsg().put("id", (data).getId());
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "保存成功");
			}
		}else{
			getOutputMsg().put("STATE", "FAIL");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	@RequestMapping(value="updateFullPy")
	public void updatePY(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		PinyinController pc = new PinyinController();
		List<Org> oList = queryExecutor.execQuery("com.wuyizhiye.basedata.org.dao.OrgDao.selectOfFullPy", new HashMap(), Org.class);
		for(Org o : oList){
			Org org = orgService.getEntityById(o.getId());
			String fullPy = pc.getSpell(org.getName(),false);
			org.setFullPinyin(fullPy);
			orgService.updateEntity(org);
		}
	}
	@RequestMapping(value="saveOrgMapMark")
	@ResponseBody
	public Map<String, Object> saveMapMark(ModelMap model, HttpServletResponse response){
		Map<String,Object> result = new HashMap<String, Object>();
		Org org = orgService.getEntityById(getString("id"));
		try {
			org.setMapx(getString("mapx"));
			org.setMapy(getString("mapy"));
			org.setZoom(getInt("zoom"));
			org.setMapoint(1);
			org.setGeoHashStr(Geohash.encode(Double.valueOf(getString("mapy")),Double.valueOf(getString("mapx"))));
			orgService.updateEntity(org);
			result.put("flag", true);
		} catch (Exception e) {
			result.put("flag", false);
		}
		return result;
	}

}
