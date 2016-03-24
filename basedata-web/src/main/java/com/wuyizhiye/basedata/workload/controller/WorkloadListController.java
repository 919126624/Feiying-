package com.wuyizhiye.basedata.workload.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.spring.ApplicationContextAware;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.basedata.permission.model.Menu;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.basedata.workload.model.Workload;
import com.wuyizhiye.basedata.workload.model.WorkloadTypeEnum;
import com.wuyizhiye.basedata.workload.service.WorkloadService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName WorkloadListController
 * @Description 工作量
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/workload/*")
public class WorkloadListController extends ListController{
	
	@Autowired
	private WorkloadService workloadService;

	@Override
	protected CoreEntity createNewEntity() {
		return new Workload();
	}

	@Override
	protected String getListView() {
		List<Menu> menuList = queryExecutor.execQuery("com.wuyizhiye.basedata.permission.dao.MenuDao.getParentMenu", new HashMap<String,Object>(), Menu.class);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("menuList", menuList);
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("longNumber", menuList.get(0).getLongNumber());
		List<Menu> subMenuList = queryExecutor.execQuery("com.wuyizhiye.basedata.permission.dao.MenuDao.getSimpleTreeData", param, Menu.class);
		param.clear();
		WorkloadTypeEnum[] workloadTypes = WorkloadTypeEnum.values();
//		List<WorkloadSort> workloadSortList = new ArrayList<WorkloadSort>();
//		for(WorkloadTypeEnum temp : workloadTypes){
//			param.put("typeValue", temp.getValue());
//			WorkloadSort workloadSort = queryExecutor.execOneEntity("com.wuyizhiye.basedata.workload.dao.WorkloadDao.selectSort", param, WorkloadSort.class);
//			if(!StringUtils.isNotNull(workloadSort)){
//				workloadSort = new WorkloadSort();
//				workloadSort.setTypeValue(temp.getValue());
//				workloadSort.setName(temp.getLabel());
//				workloadService.initSort(workloadSort);
//			}
//			workloadSortList.add(workloadSort);
//		}
//		getRequest().setAttribute("workloadSortList", workloadSortList);
		getRequest().setAttribute("workloadTypeList", workloadTypes);
		getRequest().setAttribute("menuList", menuList);
		getRequest().setAttribute("subMenuList", subMenuList);
		return "basedata/workload/workloadList";
	}

	@Override
	protected String getEditView() {
		return "basedata/workload/workloadEdit";
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.workload.dao.WorkloadDao.select";
	}

	@Override
	protected BaseService<Workload> getService() {
		return workloadService;
	}
	
	@RequestMapping(value="getWorkloadById")
	public void getWorkloadById(HttpServletResponse response) {
		Workload workload = workloadService.getEntityById(getString("id"));
		getOutputMsg().put("workload", workload);
		getOutputMsg().put("STATE", "SUCCESS");
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	@RequestMapping(value="getMenuByLongNumber")
	public void getMenuByLongNumber(HttpServletResponse response) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("longNumber", getString("longNumber"));
		List<Menu> subMenuList = queryExecutor.execQuery("com.wuyizhiye.basedata.permission.dao.MenuDao.getSimpleTreeData", param, Menu.class);
		getOutputMsg().put("subMenuList", subMenuList);
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	@RequestMapping(value="toIndex")
	public String toIndex(HttpServletResponse response) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("job", SystemUtil.getCurrentPosition().getJob().getId());
		List<Workload> workloadList = queryExecutor.execQuery("com.wuyizhiye.basedata.workload.dao.WorkloadDao.selectByJob", param,Workload.class);
		param.clear();
		param.put("personId", getCurrentUser().getId());
		param.put("orgId", getCurrentUser().getOrg().getId());
		Date queryStartDate = null;
		Date queryEndDate = null;
		queryStartDate = DateUtil.getTimesmorning();
		queryEndDate = DateUtil.getNextDay(DateUtil.getTimesmorning());
		param.put("queryStartDate", queryStartDate);
		param.put("queryEndDate", queryEndDate);
		for(Workload workload : workloadList){
			String url = workload.getRemark();
			int lastIndex = url.lastIndexOf(".");
			String clazzStr = url.substring(0,lastIndex);
			String methodStr = url.substring(lastIndex+1);
			Object intentionCustomerService = ApplicationContextAware.getApplicationContext().getBean(clazzStr);
			Method method = intentionCustomerService.getClass().getMethod(methodStr, Map.class);
			workload.setRemark(method.invoke(intentionCustomerService,param).toString());
		}
		
		getRequest().setAttribute("workloadList", workloadList);
		return "basedata/workload/workload";
	}
	
	@RequestMapping(value="queryTime")
	public String queryTime(HttpServletResponse response) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("job", SystemUtil.getCurrentPosition().getJob().getId());
		List<Workload> workloadList = queryExecutor.execQuery("com.wuyizhiye.basedata.workload.dao.WorkloadDao.selectByJob", param,Workload.class);
		param.clear();
		param.put("personId", getCurrentUser().getId());
		param.put("orgId", getCurrentUser().getOrg().getId());
		Date queryStartDate = null;
		Date queryEndDate = null;
		/**
		 * 关于日期条件
		 * 例： 查询昨天
		 * 条件为：     queryStartDate：昨天，queryEndDate：今天
		 * date >= queryStartDate and  date < queryEndDate
		 */
		if("yesterday".equals(getString("dateType"))){//昨天
			queryStartDate = DateUtil.getPrevDay(DateUtil.getTimesmorning());
			queryEndDate = DateUtil.getTimesmorning();
		}else if("today".equals(getString("dateType"))){//今天
			queryStartDate = DateUtil.getTimesmorning();
			queryEndDate = DateUtil.getNextDay(DateUtil.getTimesmorning());
		}else if("lastWeek".equals(getString("dateType"))){//上周
			queryStartDate = DateUtil.getCurrWeekMonday(DateUtil.getLastWeek(DateUtil.getTimesmorning()));
			queryEndDate = DateUtil.getCurrWeekSunday(DateUtil.getLastWeek(DateUtil.getTimesmorning()));
		}else if("currentWeek".equals(getString("dateType"))){//本周
			queryStartDate = DateUtil.getCurrWeekMonday(DateUtil.getTimesmorning());
			queryEndDate = DateUtil.getCurrWeekSunday(DateUtil.getTimesmorning());
		}else if("lastMonth".equals(getString("dateType"))){//上月
			queryStartDate = DateUtil.getFirstDay(DateUtil.getLastMonth(DateUtil.getTimesmorning()));
			queryEndDate = DateUtil.getNextDay(DateUtil.getLastDay(DateUtil.getLastMonth(DateUtil.getTimesmorning())));
		}else{//本月
			queryStartDate = DateUtil.getFirstDay(DateUtil.getTimesmorning());
			queryEndDate = DateUtil.getNextDay(DateUtil.getLastDay(DateUtil.getTimesmorning()));
		}
		param.put("dateType", getString("dateType"));
		param.put("queryStartDate", queryStartDate);
		param.put("queryEndDate", queryEndDate);
		for(Workload workload : workloadList){
			String url = workload.getRemark();
			int lastIndex = url.lastIndexOf(".");
			String clazzStr = url.substring(0,lastIndex);
			String methodStr = url.substring(lastIndex+1);
			Object intentionCustomerService = ApplicationContextAware.getApplicationContext().getBean(clazzStr);
			Method method = intentionCustomerService.getClass().getMethod(methodStr, Map.class);
			workload.setRemark(method.invoke(intentionCustomerService,param).toString());
		}
		
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("workloadList", workloadList);
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
		getRequest().setAttribute("workloadList", workloadList);
		return "basedata/workload/workloadDiv";
	}

}
