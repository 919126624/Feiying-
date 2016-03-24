package com.wuyizhiye.basedata.org.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.HttpClientUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.org.model.WeixinOrg;
import com.wuyizhiye.basedata.org.service.WeixinOrgService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName WeixinOrgEditController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/weixinorg/*")
public class WeixinOrgEditController extends EditController {
	@Autowired
	private WeixinOrgService weixinorgService;	
	@Override
	protected Class<WeixinOrg> getSubmitClass() {
		return WeixinOrg.class;
	}
	
	@Override
	protected Object getSubmitEntity() {
		WeixinOrg org = (WeixinOrg) super.getSubmitEntity();
		return org;
	}

	@Override
	protected BaseService<WeixinOrg> getService() {
		return weixinorgService;
	}
	
	@Override
	protected boolean validate(Object data) {
		WeixinOrg entity = (WeixinOrg) data;
		if(StringUtils.isEmpty(entity.getName())){
			getOutputMsg().put("MSG", "名称不能为空");
			return false;
		}
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("number", entity.getNumber());
		List<WeixinOrg> values = queryExecutor.execQuery("com.wuyizhiye.basedata.org.dao.WeixinOrgDao.select", param,WeixinOrg.class);
		for(WeixinOrg g : values){
			if(!g.getId().equals(entity.getId())){
				getOutputMsg().put("MSG", "该编码己存在");
				return false;
			}
		}
		param.clear();
		if(entity.getParent()!=null){
			//同一组织下的组织不能同名
			param.put("parent", entity.getParent().getId());
		}
		param.put("name", entity.getName());
		values = queryExecutor.execQuery("com.wuyizhiye.basedata.org.dao.WeixinOrgDao.select", param, WeixinOrg.class);
		for(WeixinOrg g : values){
			if(!g.getId().equals(entity.getId())){
				getOutputMsg().put("MSG", "该组织名称冲突");
				return false;
			}
		}
 		return super.validate(data);
	}

	@Override
	public void save(HttpServletResponse response)
			throws InstantiationException, IllegalAccessException {
		try{
			WeixinOrg weixinOrg=(WeixinOrg) getSubmitEntity();
			
			String syncweixin = this.getString("syncweixin");
			if(StringUtils.isEmpty(weixinOrg.getName())){
				throw new Exception("名称不能为空");
			}
			
			String orgname = "";
			try {
				orgname = URLEncoder.encode(weixinOrg.getName(),"utf-8");
			} catch (UnsupportedEncodingException e) {
				orgname = weixinOrg.getName();
			}
			
			if("1".equals(syncweixin)){
				JSONObject jsonObj = null;
				String strRes= "";
				if(!StringUtils.isEmpty(weixinOrg.getId())){
					strRes=
						HttpClientUtil.callHttpUrl(getBasePath()+"/weixinapi/weixinorg?t=update&name="+orgname+"&orgid="+weixinOrg.getNumber(), "");		
				}else{
					if(null!=weixinOrg.getParent()){
						WeixinOrg wxorg = this.weixinorgService.getEntityById(weixinOrg.getParent().getId());
						weixinOrg.setParent(wxorg);
					}
					strRes=
						HttpClientUtil.callHttpUrl(getBasePath()+"/weixinapi/weixinorg?t=add&name="+orgname+(null==weixinOrg.getParent()?"":("&parentid="+weixinOrg.getParent().getNumber())), "");		
				}
				jsonObj=JSONObject.fromObject(strRes);
				if("SUCCESS".equals(jsonObj.getString("STATE"))){
					if(StringUtils.isEmpty(weixinOrg.getId())){	//如果是新增,返回orgid		
						weixinOrg.setNumber(jsonObj.getString("ORGID"));
					}
				}else{
					throw new Exception(jsonObj.getString("MSG"));
				}
			}
			
			if(StringUtils.isEmpty(weixinOrg.getNumber())){
				throw new Exception("编码不能为空");
			}
			
			if(!StringUtils.isEmpty(weixinOrg.getId())){
				this.weixinorgService.updateEntity(weixinOrg);
			}else{
				this.weixinorgService.addEntity(weixinOrg);
			}
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("MSG", "操作成功");
			
		}catch(Exception e){
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", e.getMessage());
		}
				
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
		
		
	}
}
