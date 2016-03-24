package com.wuyizhiye.basedata.portlet.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.module.enums.BusinessTypeEnum;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.portlet.enums.FormatSizeEnum;
import com.wuyizhiye.basedata.portlet.enums.LayoutEnum;
import com.wuyizhiye.basedata.portlet.model.Portlet;
import com.wuyizhiye.basedata.portlet.model.PortletLayout;
import com.wuyizhiye.basedata.portlet.service.PortletLayoutService;
import com.wuyizhiye.basedata.portlet.service.PortletService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName PortletListController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="interflow/portlet/*")
public class PortletListController extends ListController {

	@Autowired
	private PortletService portletService;
	
	@Autowired
	private PortletLayoutService portletLayoutService;
	
	@Override
	protected CoreEntity createNewEntity() {
		return null;
	}

	@Override
	protected String getListView() {
		this.getRequest().setAttribute("businessTypeList", BusinessTypeEnum.toList());
		return "interflow/portlet/PortletList";
	}

	@Override
	protected String getEditView() {
		this.getRequest().setAttribute("formatlist", FormatSizeEnum.values());
		this.getRequest().setAttribute("businessTypeList", BusinessTypeEnum.toList());
		this.getRequest().setAttribute("layoutlist", LayoutEnum.toList());
		return "interflow/portlet/PortletEdit";
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.portlet.dao.PortletDao.select";
	}

	@Override
	protected BaseService getService() {
		return portletService;
	}

	@RequestMapping(value="updateStatus")
	public void updateStatus(HttpServletResponse response,Portlet portlet) {
		this.portletService.updateStatus(portlet);
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "保存成功");
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));

	}
	
	@Override
	public boolean isAllowDelete(CoreEntity entity) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("portletId", entity.getId());
		List<PortletLayout> list = this.portletLayoutService.getlist(map);
		//如果已绑定不可删除
		if(list.size()>0){
			getOutputMsg().put("MSG", "已绑定的portlet不可删除");
			return false;
		}
		return true;
	}
	/**
	 * json转换config
	 * @return
	 */
	protected JsonConfig getDefaultJsonConfig(){
		JsonConfig config = new JsonConfig();
		//日期
		config.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof Date){
					return sdf.format(value);
				}
				return null;
			}
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof Date){
					return sdf.format(value);
				}
				return null;
			}
		});
		config.registerJsonValueProcessor(BusinessTypeEnum.class, new JsonValueProcessor() {
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof BusinessTypeEnum){
					JSONObject json = new JSONObject();
					json.put("name", ((BusinessTypeEnum)value).getName());
					json.put("value", ((BusinessTypeEnum)value).getValue());
					return json;
				}
				return null;
			}
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof BusinessTypeEnum){
					return ((BusinessTypeEnum)value).getName();
				}
				return null;
			}
		});
		
		config.registerJsonValueProcessor(LayoutEnum.class, new JsonValueProcessor() {
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof LayoutEnum){
					JSONObject json = new JSONObject();
					json.put("name", ((LayoutEnum)value).getTitle());
					json.put("value", ((LayoutEnum)value).getValue());
					return json;
				}
				return null;
			}
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof LayoutEnum){
					return ((LayoutEnum)value).getTitle();
				}
				return null;
			}
		});
		 
		return config;
	}
}
