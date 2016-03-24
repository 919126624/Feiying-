package com.wuyizhiye.basedata.org.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.HttpClientUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.org.model.WeixinOrg;
import com.wuyizhiye.basedata.org.service.WeixinOrgService;
import com.wuyizhiye.framework.base.controller.TreeListController;

/**
 * @ClassName WeixinOrgListController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/weixinorg/*")
public class WeixinOrgListController extends TreeListController {
	@Autowired
	private WeixinOrgService weixinorgService;	
	@Override
	protected CoreEntity createNewEntity() {
		return new WeixinOrg();
	}
	
	@Override
	protected String getListView() {
		return "basedata/weixinorg/weixinOrgList";
	}

	@Override
	protected String getEditView() {
		return "basedata/weixinorg/weixinOrgEdit";
	}
	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.org.dao.WeixinOrgDao.select";	
	}

	@Override
	protected BaseService<WeixinOrg> getService() {
		return weixinorgService;
	}

	@Override
	protected String getTreeDataMapper() {
		return "com.wuyizhiye.basedata.org.dao.WeixinOrgDao.getChild";
	}
	
	/**
	 * ${base}/basedata/org/orgDataPicker?multi=true为多选
	 * @param model
	 * @return
	 */
	@RequestMapping(value="orgDataPicker")
	public String orgDataPicker(ModelMap model){
		Map<String,String> param = getParamMap();
		Set<String> keys = param.keySet();
		for(String s : keys){
			model.put(s, param.get(s));
		}
		return "basedata/weixinorg/wx_orgDataPicker";
	}
	

	/**
	 * dataPicker取组织数据
	 */
	@RequestMapping(value="orgData")
	public void orgData(Pagination<?> pagination,HttpServletResponse response){
		Map<String,Object> param = getListDataParam();
		pagination = queryExecutor.execQuery(getListMapper(), pagination, param);
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}

	@Override
	protected String getSimpleTreeDataMapper() {
		return "com.wuyizhiye.basedata.org.dao.WeixinOrgDao.getSimpleTreeData";
	}
	

	@Override
	protected boolean isAllowDelete(CoreEntity entity) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("parent", entity.getId());
		if(queryExecutor.execCount("com.wuyizhiye.basedata.org.dao.WeixinOrgDao.select", param)>0){
			setOutputMsg("MSG", "存在下级组织,不能删除");
			return false;
		}
		return super.isAllowDelete(entity);
	}

	@Override
	public void delete(String id, HttpServletResponse response) {
		WeixinOrg weixinOrg=this.weixinorgService.getEntityById(id);
		String syncweixin = this.getString("syncweixin");
		try{
			if("1".equals(syncweixin)){
			String strRes=HttpClientUtil.callHttpUrl(getBasePath()+"/weixinapi/weixinorg?t=del&orgid="+weixinOrg.getNumber(), "");
			JSONObject jsonObj=JSONObject.fromObject(strRes);
			if("FAIL".equals(jsonObj.getString("STATE"))) throw new Exception(jsonObj.getString("MSG"));
			}
			
			weixinorgService.deleteEntity(weixinOrg);
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("MSG", "删除成功");
			
		}catch(Exception e){
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", e.getMessage());
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	@RequestMapping(value="importData")
	public void importData(HttpServletResponse response){
		try{
		int orgcount = 
		this.queryExecutor.execCount("com.wuyizhiye.basedata.org.dao.WeixinOrgDao.select", null);
		if(orgcount>0){
			throw new Exception("该功能只用于第一次初始导入");
		}
		String strRes=
			HttpClientUtil.callHttpUrl(getBasePath()+"/weixinapi/weixinorg?t=list", "");
		JSONObject jsonObj=JSONObject.fromObject(strRes);
		if("SUCCESS".equals(jsonObj.getString("STATE"))){
			List<WeixinOrg> wxorglist = new ArrayList<WeixinOrg>();			
			JSONArray jary = jsonObj.getJSONArray("DATA");
			Iterator itr = jary.iterator();
			Map<String,Object> longmap = new HashMap<String,Object>();
			//按组织编号过来
			while(itr.hasNext()){
				JSONObject jobj = (JSONObject)itr.next();
				String id = jobj.getString("id");
				String name = jobj.getString("name");
				String parentid = jobj.getString("parentid");
				WeixinOrg wxorg = new WeixinOrg();
				wxorg.setNumber(id);
				wxorg.setName(name);
				wxorg.setId(id);
					
				if(StringUtils.isNotNull(parentid)){
					WeixinOrg porg = new WeixinOrg();
					porg.setId(parentid);
					wxorg.setParent(porg);	
					if(null==longmap.get(parentid)) wxorg.setLongNumber(parentid+"!"+id);
					else wxorg.setLongNumber(longmap.get(parentid)+"!"+id);
					longmap.put(id, wxorg.getLongNumber());
				}else{
					wxorg.setLongNumber(wxorg.getNumber());
					longmap.put(id, wxorg.getNumber());
				}
				wxorglist.add(wxorg);
			}
			this.weixinorgService.addBatch(wxorglist);
			
			
			
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("MSG", "操作成功");
		}else{
			throw new Exception(jsonObj.getString("MSG"));
		}
		}catch(Exception e){
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", e.getMessage());
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
}
