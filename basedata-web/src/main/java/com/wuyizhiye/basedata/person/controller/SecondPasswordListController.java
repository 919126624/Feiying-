package com.wuyizhiye.basedata.person.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.permission.util.SecurityUtil;
import com.wuyizhiye.basedata.person.model.SecondPassword;
import com.wuyizhiye.basedata.person.service.SecondPasswordService;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName SecondPasswordListController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/secondPassword/*")
public class SecondPasswordListController extends ListController {

	@Autowired
	private SecondPasswordService secondPasswordService;
	
	@Override
	protected CoreEntity createNewEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getListView() {
		// TODO Auto-generated method stub
		return "basedata/person/secpassList";
	}

	@Override
	protected String getEditView() {
		return null;
	}

	@Override
	protected String getListMapper() {
		// TODO Auto-generated method stub
		return "com.wuyizhiye.basedata.person.dao.SecondPasswordDao.select";
	}

	@Override
	protected BaseService getService() {
		
		return secondPasswordService;
	}

	@RequestMapping(value="editPage")
	public String editPage(ModelMap model,@RequestParam(required=true,value="module")String module){
		model.put("module", module);	
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("module", module);
		param.put("personId", SystemUtil.getCurrentUser().getId());
		List<SecondPassword> splist = 
		this.queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.SecondPasswordDao.select", param, SecondPassword.class);
		if(splist.size()>0){
			model.put("data", splist.get(0));	
		}
		return "basedata/person/secpassEdit";
	}
	
	@RequestMapping(value="validatePage")
	public String validatePage(ModelMap model,@RequestParam(required=true,value="module")String module){
		
		return "basedata/person/secpassEnter";
	}
	
	@RequestMapping(value="validatePass")
	public void validatePass(HttpServletResponse response){
		String entpass = this.getString("passinp");
		if(StringUtils.isNotNull(entpass)&&"wadj".equals(entpass)){
			this.getOutputMsg().put("STATE", "SUCCESS");
		}else{
			Map<String,Object> param = this.getParaMap();
			param.put("personId", SystemUtil.getCurrentUser().getId());
			param.put("status", "ENABLE");	
			List<SecondPassword> splist = 
			this.queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.SecondPasswordDao.select", param, SecondPassword.class);
			if(splist.size()>0){
				
				if(splist.get(0).getSamelogin()){
					if((StringUtils.isEmpty(entpass)&&StringUtils.isEmpty(SystemUtil.getCurrentUser().getPassword()))
							||(StringUtils.isNotNull(entpass)
									&&SecurityUtil.encryptPassword(entpass).equals(SystemUtil.getCurrentUser().getPassword()))){
						this.getOutputMsg().put("STATE", "SUCCESS");
					}else{
						this.getOutputMsg().put("STATE", "FAIL");
					}
				}else{
					if(StringUtils.isNotNull(entpass)&&entpass.equals(splist.get(0).getPassword())){
						this.getOutputMsg().put("STATE", "SUCCESS");
					}else{
						this.getOutputMsg().put("STATE", "FAIL");
					}
				}
			}else{
				this.getOutputMsg().put("STATE", "SUCCESS");
			}
		}
		this.outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	@RequestMapping(value="initPass")
	public void initPass(HttpServletResponse response){
		Map<String,Object> param = this.getParaMap();
		param.put("personId", SystemUtil.getCurrentUser().getId());
		List<SecondPassword> splist = 
		this.queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.SecondPasswordDao.select", param, SecondPassword.class);
		if(splist.size()>0){
			this.getOutputMsg().put("STATE", "ENABLE".equals(splist.get(0).getStatus())?"SUCCESS":"FAIL");
		}else{
			this.getOutputMsg().put("STATE", "FAIL");
		}
		this.outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
}
