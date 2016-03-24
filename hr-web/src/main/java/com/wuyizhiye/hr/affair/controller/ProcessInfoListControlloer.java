package com.wuyizhiye.hr.affair.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.framework.base.controller.ListController;
import com.wuyizhiye.hr.affair.model.ProcessInfo;
import com.wuyizhiye.hr.enums.BillEnum;

/**
 * 单据信息(公司动态)
 * @author hlz
 *
 */
@Controller
@RequestMapping(value="hr/processInfo/*")
public class ProcessInfoListControlloer extends ListController {

	@Override
	protected CoreEntity createNewEntity() {
		return null;
	}

	@Override
	protected String getListView() {
		return null;
	}

	@Override
	protected String getEditView() {
		return null;
	}

	@Override
	protected String getListMapper() {
		return null;
	}

	@Override
	protected BaseService getService() {
		return null;
	}
	
	@RequestMapping(value="getCount")
	public void getAllCount(HttpServletResponse response){
		Map<String, Object> param = new HashMap<String, Object>();
		
//		getOutputMsg().put("all", getProcessCount(param));
//		param.put("billType", BillEnum.ENROLL);//入职
//		getOutputMsg().put("enrollCount", getProcessCount(param));
//		param.put("billType", BillEnum.POSITIVE);//转正
//		getOutputMsg().put("positiveCount", getProcessCount(param));
//		param.put("billType", BillEnum.REINSTATEMENT);//复职
//		getOutputMsg().put("reinstatementCount", getProcessCount(param));
//		param.put("billType", BillEnum.LEAVE);//离职
//		getOutputMsg().put("leaveCount", getProcessCount(param));
//		param.put("billType", BillEnum.TRANSFER);//调职
//		getOutputMsg().put("transferCount", getProcessCount(param));
//		param.put("billType", BillEnum.PROMOTION);//晋升
//		getOutputMsg().put("promotionCount", getProcessCount(param));
//		param.put("billType", BillEnum.DEMOTION);//降职
//		getOutputMsg().put("demotionCount", getProcessCount(param));
//		param.put("billType", BillEnum.DISMISS_PARTTIMEJOB);//撤职
//		getOutputMsg().put("missjobCount", getProcessCount(param));
//		param.put("billType", BillEnum.INCREASE_PARTTIMEJOB);//兼职
//		getOutputMsg().put("increasejobCount", getProcessCount(param));
		param.put("queryStartDate", StringUtils.isNotNull(getString("queryStartDate"))?DateUtil.convertStrToDateHms(getString("queryStartDate")+" 00:00:00"):null);
		param.put("queryEndDate", StringUtils.isNotNull(getString("queryEndDate"))?DateUtil.convertStrToDateHms(getString("queryEndDate")+" 23:59:59"):null);
		param.put("key", getString("key"));
		outPrint(response, JSONObject.fromObject(getProcessCount(param)));
	}
	
	private Map<String, Object> getProcessCount(Map<String, Object> param){
		List<Map> map = queryExecutor.execQuery("com.wuyizhiye.hr.affair.dao.ProcessInfoDao.selectProcessInfoCount", param, Map.class);
		int totalCount = 0;
		for (Map map2 : map) {
			if(BillEnum.valueOf(map2.get("BILLTYPE").toString())==BillEnum.ENROLL){
				getOutputMsg().put("enrollCount", map2.get("BILLCOUNT"));
			}else if(BillEnum.valueOf(map2.get("BILLTYPE").toString())==BillEnum.POSITIVE){
				getOutputMsg().put("positiveCount",  map2.get("BILLCOUNT"));
			}else if(BillEnum.valueOf(map2.get("BILLTYPE").toString())==BillEnum.REINSTATEMENT){
				getOutputMsg().put("reinstatementCount",  map2.get("BILLCOUNT"));
			}else if(BillEnum.valueOf(map2.get("BILLTYPE").toString())==BillEnum.LEAVE){
				getOutputMsg().put("leaveCount",  map2.get("BILLCOUNT"));
			}else if(BillEnum.valueOf(map2.get("BILLTYPE").toString())==BillEnum.TRANSFER){
				getOutputMsg().put("transferCount",  map2.get("BILLCOUNT"));
			}else if(BillEnum.valueOf(map2.get("BILLTYPE").toString())==BillEnum.PROMOTION){
				getOutputMsg().put("promotionCount",  map2.get("BILLCOUNT"));
			}else if(BillEnum.valueOf(map2.get("BILLTYPE").toString()) ==BillEnum.DEMOTION){
				getOutputMsg().put("demotionCount",  map2.get("BILLCOUNT"));
			}else if(BillEnum.valueOf(map2.get("BILLTYPE").toString())==BillEnum.DISMISS_PARTTIMEJOB){
				getOutputMsg().put("missjobCount",  map2.get("BILLCOUNT"));
			}else if(BillEnum.valueOf(map2.get("BILLTYPE").toString())==BillEnum.INCREASE_PARTTIMEJOB){
				getOutputMsg().put("increasejobCount",  map2.get("BILLCOUNT"));
			}else if(BillEnum.valueOf(map2.get("BILLTYPE").toString())==BillEnum.RUNDISK){
				getOutputMsg().put("rundiskCount",  map2.get("BILLCOUNT"));
			}else if(BillEnum.valueOf(map2.get("BILLTYPE").toString())==BillEnum.DELRUNDISK){
				getOutputMsg().put("delrundiskCount",  map2.get("BILLCOUNT"));
			}
			totalCount += Integer.parseInt(map2.get("BILLCOUNT").toString());//(Integer)(map2.get("BILLCOUNT").toString());
		}
		getOutputMsg().put("all",  totalCount);
		return getOutputMsg();
	}

	
	@RequestMapping(value="getListData")
	public void getListData(HttpServletResponse response){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("billType", getString("billType"));
		if(getString("billType") == null){
			param.put("notInBillType", "true");
		}
		param.put("queryStartDate", StringUtils.isNotNull(getString("queryStartDate"))?DateUtil.convertStrToDateHms(getString("queryStartDate")+" 00:00:00"):null);
		param.put("queryEndDate", StringUtils.isNotNull(getString("queryEndDate"))?DateUtil.convertStrToDateHms(getString("queryEndDate")+" 23:59:59"):null);
		param.put("key", getString("key"));
		String page = getString("page");
		String sizestr = getString("size");
		int currentPage = null==page || "".equals(page)?1:Integer.parseInt(page);
		int pageSize = null==sizestr || "".equals(sizestr)?18:Integer.parseInt(sizestr);
		Pagination<ProcessInfo> pag = queryExecutor.execQuery("com.wuyizhiye.hr.affair.dao.ProcessInfoDao.select", new Pagination<ProcessInfo>(pageSize, currentPage), param);
		if(pag!=null){
			getRequest().setAttribute("processPage", pag);
			getRequest().setAttribute("totalpage", pag.getPageCount());
			getRequest().setAttribute("curpage", pag.getCurrentPage());
		}
		outPrint(response, JSONObject.fromObject(pag,getDefaultJsonConfig()));
	}
	
	@Override
	protected JsonConfig getDefaultJsonConfig(){
		JsonConfig config = new JsonConfig();
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
		config.registerJsonValueProcessor(BillEnum.class, new JsonValueProcessor() {
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof BillEnum){
					JSONObject json = new JSONObject();
					json.put("name", ((BillEnum)value).getLabel());
					json.put("value", ((BillEnum)value).getValue());
					return json;
				}
				return null;
			}
			@Override
			public Object processArrayValue(Object value,JsonConfig cfg) {
				if(value!=null && value instanceof BillEnum){
					return ((BillEnum)value).getLabel();
				}
				return null;
			}
		});
		
		return config;
	}
	
}
