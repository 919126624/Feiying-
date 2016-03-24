package com.wuyizhiye.basedata.code.controller;

import java.io.StringWriter;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.annotation.Dependence;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.module.enums.ModuleEnum;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.spring.ApplicationContextAware;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.code.enums.FetchTypeEnum;
import com.wuyizhiye.basedata.code.enums.PriorityEnum;
import com.wuyizhiye.basedata.code.model.PrintConfig;
import com.wuyizhiye.basedata.code.service.PrintConfigService;
import com.wuyizhiye.framework.base.controller.ListController;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * @ClassName PrintConfigListController
 * @Description 打印配置
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value = "basedata/printConfig/*")
public class PrintConfigListController extends ListController{
	private static Logger logger=Logger.getLogger(PrintConfigListController.class);
	@Autowired
	private PrintConfigService printConfigService;
	
	@Override
	protected CoreEntity createNewEntity() {
		PrintConfig bt=new PrintConfig();
		bt.setModule(getString("moduleType"));
		return bt;
	}

	@Override
	protected String getListView() {
		getRequest().setAttribute("moduleType",getString("moduleType"));
		getRequest().setAttribute("moduleEnumList", ModuleEnum.values());
		return "basedata/code/printConfigList";
	}

	@Override
	protected String getEditView() {
		getRequest().setAttribute("priorityTypes", PriorityEnum.values());
		getRequest().setAttribute("moduleType", getString("moduleType"));
		getRequest().setAttribute("fetchTypeList", FetchTypeEnum.values());
		return "basedata/code/printConfigEdit";
	}
	 
	
	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.code.PrintConfigDao.select";
	}

	@Override
	protected BaseService getService() {
		return printConfigService;
	}
	
	
	/**
	 * 列表数据
	 * @param pagination
	 * @param response
	 */
	@RequestMapping(value="listData")
	@Dependence(method="list")
	public void listData(Pagination<?> pagination,HttpServletResponse response){
		if(StringUtils.isNotNull(getListDataParam().get("moduleType"))){
			pagination = queryExecutor.execQuery(getListMapper(), pagination, getListDataParam());
			afterFetchListData(pagination);
		}
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	
	/**
	 * json转换config
	 * @return
	 */
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
		 
		config.registerJsonValueProcessor(FetchTypeEnum.class, new JsonValueProcessor() {
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof FetchTypeEnum){
					JSONObject json = new JSONObject();
					json.put("name", ((FetchTypeEnum)value).getLabel());
					json.put("value", ((FetchTypeEnum)value).getValue());
					return json;
				}
				return null;
			}
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof FetchTypeEnum){
					return ((FetchTypeEnum)value).getLabel();
				}
				return null;
			}
		});
		return config;
	}
	
	@RequestMapping(value="commPrint")
	protected String commPrint(ModelMap model,@RequestParam(required=true,value="id")String id,@RequestParam(required=true,value="number")String number) {
			try {
			    Map<String, Object> param=new HashMap<String, Object>();
			    param.put("number", number);
			    PrintConfig pc=this.queryExecutor.execOneEntity("com.wuyizhiye.basedata.code.PrintConfigDao.select", param, PrintConfig.class);
			    if(pc!=null && !StringUtils.isEmpty(pc.getFetchConfig())){ 
			    	 Object rootMap=new Object();
			    	if(pc.getFetchType().equals(FetchTypeEnum.SQL_PATH)){ //根据SQL路径取数据
			    		param.clear();
			    		param.put("id", id);
			    		rootMap=this.queryExecutor.execOneEntity(pc.getFetchConfig(), param, Map.class);
			    	}else if(pc.getFetchType().equals(FetchTypeEnum.SERVICES)){//通过服务接口取数据
			    		 String serviceName=pc.getFetchConfig().substring(0, pc.getFetchConfig().indexOf("."));
						 String functionName=pc.getFetchConfig().substring(pc.getFetchConfig().indexOf(".")+1, pc.getFetchConfig().indexOf("(")==-1?pc.getFetchConfig().length(): pc.getFetchConfig().indexOf("("));
			    		 Object bean = ApplicationContextAware.getApplicationContext().getBean(serviceName);
			    		 Class[] types =new Class[]{String.class};
			    		 Class c = bean.getClass();
			    		 Method method = c.getMethod(functionName, types); 
			    		 Object[] values=new Object[]{id};
			    		 rootMap= method.invoke(bean,values);
			    	}
					String templateContent=pc.getPrintModel();
					Configuration cfg=new Configuration();
					StringTemplateLoader stringLoader=new StringTemplateLoader();
					stringLoader.putTemplate("template_"+pc.getId(), templateContent==null?"":templateContent);
					cfg.setTemplateLoader(stringLoader);
					cfg.setDefaultEncoding("UTF-8");
					Template template = cfg.getTemplate("template_"+pc.getId());
					StringWriter sw=new StringWriter();
					template.process(rootMap, sw);
					pc.setPrintModel(sw.toString());
					getRequest().setAttribute("data", pc);
			    }
		    } catch (Exception e) {
		    	logger.error("", e);
				throw new RuntimeException(e);
			} 
		    getRequest().setAttribute("currentDate", new Date());
		return "basedata/code/commPrint";
	}
}
