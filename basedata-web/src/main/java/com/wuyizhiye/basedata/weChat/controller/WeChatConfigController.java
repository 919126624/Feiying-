package com.wuyizhiye.basedata.weChat.controller;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.weChat.model.WeChatConfig;
import com.wuyizhiye.basedata.weChat.service.WeChatConfigService;
import com.wuyizhiye.framework.base.controller.BaseController;

/**
 * @ClassName WeChatConfigController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/weChatConfig/*")//new
public class WeChatConfigController extends BaseController {
	private static final String mapper = "com.wuyizhiye.basedata.weChat.dao.WeChatConfigDao";
	
	@Autowired
	WeChatConfigService weChatConfigService;
	
	
	@RequestMapping("list")
	public String list(){
		return "basedata/weChat/weChatConfigList";
	}
	
	@RequestMapping("edit")
	public String editView(ModelMap model){
		String state = getString("VIEWSTATE");
		String id = getString("id");
		if("EDIT".equals(state) || "VIEW".equals(state)){
			WeChatConfig we = weChatConfigService.getEntityById(id);
			model.put("data",we);
		}
		return "basedata/weChat/weChatConfigEdit";
	}
	
	@RequestMapping("ajaxSave")
	public void save(ModelMap model,HttpServletResponse response){
		String id = getString("id");
		String url = getString("url");
		String name = getString("name");
		String appId = getString("appId");
		String number = getString("number");
		
		WeChatConfig w = null;
		try {
			if(StringUtils.isEmpty(id)){
				//新增
				w=new WeChatConfig();
				w.setAppId(appId);
				w.setName(name);
				w.setNumber(number);
				w.setUrl(url);
				w.setUUID();
				weChatConfigService.addEntity(w);
			}else{
				//修改
				w = weChatConfigService.getEntityById(id);
				w.setAppId(appId);
				w.setName(name);
				w.setNumber(number);
				w.setUrl(url);
				weChatConfigService.updateEntity(w);
			}
			model.put("MSG", "SUCCESS");
		} catch (Exception e) {
			model.put("MSG", "FAIL");
		}
		outPrint(response, JSONObject.fromObject(model));
	}
	
	@RequestMapping("listData")
	public void listData(ModelMap model,HttpServletResponse response,Pagination pagination){
		pagination = queryExecutor.execQuery(mapper+".select", pagination, null);
		
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	
	@RequestMapping("delete")
	public void delete(ModelMap model,HttpServletResponse response){
		String id = getString("id");
		try {
			weChatConfigService.deleteById(id);
			model.put("STATE", "SUCCESS");
		} catch (Exception e) {
			model.put("STATE", "FAIL");
		}
		outPrint(response, JSONObject.fromObject(model));
	}
	
}
