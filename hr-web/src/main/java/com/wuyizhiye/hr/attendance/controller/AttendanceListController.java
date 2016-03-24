package com.wuyizhiye.hr.attendance.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.annotation.Dependence;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.util.OrgUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.ListController;
import com.wuyizhiye.hr.attendance.model.Attendance;
import com.wuyizhiye.hr.attendance.model.AttendanceDetail;
import com.wuyizhiye.hr.attendance.model.AttendanceOrg;
import com.wuyizhiye.hr.attendance.service.AttendanceDetailService;
import com.wuyizhiye.hr.attendance.service.AttendanceService;
import com.wuyizhiye.hr.enums.AttendanceStatusEnum;
import com.wuyizhiye.hr.enums.AttendanceTypeEnum;

/**
 * 考勤
 * @author ouyangyi
 * @since 2013-5-20 下午04:23:51
 */
@Controller
@RequestMapping(value="hr/attendance/*")
public class AttendanceListController extends ListController{
	 
	@Resource
	private AttendanceService attendanceService ;
	
	@Resource
	private AttendanceDetailService attendanceDetailService ;

	@Override
	protected CoreEntity createNewEntity() {
		AttendanceDetail detail = new AttendanceDetail();
		getRequest().setAttribute("data", detail);
		return null;
	}
	
	/**
	 * 考勤录入 
	 * @param model
	 * @return
	 */
	@RequestMapping(value="toEntry")
	@Dependence(method="toQuery")
	public String toEntry(ModelMap model){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);//默认录当前日期 前一天的考勤
		getRequest().setAttribute("recordDate", DateUtil.convertDateToStr(cal.getTime()));
		return "hr/attendance/toEntry";
	}
	
	/**
	 * 考勤录入   新版
	 * @param model
	 * @return
	 */
	@RequestMapping(value="toEntry2")
	public String toEntry2(ModelMap model){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);//默认录当前日期 前一天的考勤
		getRequest().setAttribute("recordDate", DateUtil.convertDateToStr(cal.getTime()));
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("creatorId",SystemUtil.getCurrentUser().getId());
		params.put("orderByClause"," D.FORGNAME asc ");
		List<AttendanceOrg> attendanceOrgs = this.queryExecutor.execQuery("com.wuyizhiye.hr.attendance.dao.AttendanceOrgDao.getAttendanceOrgByCond",params, AttendanceOrg.class);
		model.put("attendanceOrgs", attendanceOrgs);
		return "hr/attendance/toEntry2";
	}
	
	/**
	 * 考勤查询 
	 * @param model
	 * @return
	 */
	@RequestMapping(value="toQuery")
	public String toQuery(ModelMap model){
		//默认录当前月的考勤
		getRequest().setAttribute("period", DateUtil.convertDateToStr(new Date(), "yyyy-MM"));
		return "hr/attendance/query";
	}
	
	/**
	 * 考勤审核 
	 * @param model
	 * @return
	 */
	@RequestMapping(value="toApprove")
	public String toApprove(ModelMap model){
		//默认录当前月的考勤
		List approvalStateList = AttendanceStatusEnum.toList();
		getRequest().setAttribute("approvalStateList", approvalStateList);
		getRequest().setAttribute("period", DateUtil.convertDateToStr(new Date(), "yyyy-MM"));
		return "hr/attendance/approve";
	}
	
	/**
	 * 查询月考勤
	 * @param response
	 */
	@RequestMapping(value="queryMonthByCond")
	public void queryMonthByCond(Pagination<Attendance> pagination,HttpServletResponse response){
		String period = getString("period");
		String keyConditions = getString("keyConditions");
		String orgLongNum = getString("orgLongNum");
		String approvalState = getString("approvalState");
		String fromPage = getString("fromPage");
		Map<String, Object> cond = new HashMap<String, Object>();
		cond.put("period", period);
		cond.put("keyConditions", keyConditions);
		cond.put("approvalState", approvalState);
		cond.put("orgLongNum", orgLongNum);
		cond.put("fromPage", fromPage);
		Map<String, Object> resultMap = attendanceService.queryMonthByCondPerPage(pagination,cond);
		//Pagination<Attendance> attendancePage = new Pagination<Attendance>();
		//List<Attendance> attendanceList = new ArrayList<Attendance>();
		if("SUCC".equals(resultMap.get("MSG"))){
			//attendanceList = (List<Attendance>)resultMap.get("attendanceList");
			pagination = (Pagination<Attendance>)resultMap.get("pagination");
		} 
		//attendancePage.setItems(attendanceList);
		//attendancePage.setRecordCount(attendanceList.size());
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	
	/**
	 * 查询月考勤页面  导出Excel
	 * @param response
	 */
	@RequestMapping(value="exportMonthByCond")
	public void exportMonthByCond(HttpServletResponse response)throws IOException {
		String period = getString("period");
		String keyConditions = getString("keyConditions");
		String orgLongNum = getString("orgLongNum");
		String approvalState = getString("approvalState");
		Map<String, Object> cond = new HashMap<String, Object>();
		cond.put("period", period);
		cond.put("keyConditions", keyConditions);
		cond.put("approvalState", approvalState);
		cond.put("orgLongNum", orgLongNum);
		response.setContentType("application/octet-stream");
		String fileName = period+"考勤报表.xlsx";
        response.addHeader("content-disposition", "attachment; filename=" +URLEncoder.encode(fileName, "utf-8") + "");
        OutputStream os = response.getOutputStream();
        try{
        	attendanceService.exportMonthByCond(cond,os);
        }catch(Exception e){
        	os.write(e.getMessage().getBytes());
        }
		os.close();
	}
	
	/**
	 * 查询 月考勤明细
	 * @param response
	 */
	@RequestMapping(value="queryAttendanceDetails")
	public void queryAttendanceDetails(HttpServletResponse response){
		String period = getString("period");
		String personId = getString("personId");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("personId",personId);
		params.put("period", period);
		params.put("orderByClause", "D.FRECORDDATE ");
		List<AttendanceDetail> attendanceDetails = this.queryExecutor.execQuery("com.wuyizhiye.hr.attendance.dao.AttendanceDetailDao.selectListByCond",params, AttendanceDetail.class);
		Pagination<AttendanceDetail> attendanceDetailPage = new Pagination<AttendanceDetail>();
		 
		attendanceDetailPage.setItems(attendanceDetails);
		attendanceDetailPage.setRecordCount(attendanceDetails.size());
		outPrint(response, JSONObject.fromObject(attendanceDetailPage, getDefaultJsonConfig()));
	}
	
	/**
	 * 考勤查询 Portlet
	 * @param model
	 * @return
	 */
	@RequestMapping(value="toQueryPortlet")
	public String toQueryPortlet(ModelMap model){
		//默认录当前月的考勤
		getRequest().setAttribute("period", DateUtil.convertDateToStr(new Date(), "yyyy-MM"));
		getRequest().setAttribute("periodDis", DateUtil.convertDateToStr(new Date(), "yyyy年M月"));
		return "hr/attendance/queryPortlet";
	}
	
	/**
	 * 考勤查询 
	 * @param model
	 * @return
	 */
	@RequestMapping(value="toQueryPortletMore")
	public String toQueryPortletMore(ModelMap model){
		//默认录当前月的考勤
		getRequest().setAttribute("period", DateUtil.convertDateToStr(new Date(), "yyyy-MM"));
		return "hr/attendance/queryPortletMore";
	}
	
	
	/**
	 * 查询 月考勤明细  用于首页
	 * @param response
	 */
	@RequestMapping(value="getAttendanceDetails")
	public void getAttendanceDetails(HttpServletResponse response){
		String period = getString("period");
		String personId = getString("personId");
		Attendance attendance = new Attendance();
		attendance.setPeriod(period);
		attendance.setPersonId(personId);
		List<AttendanceDetail> attendanceDetails = attendanceService.getAttendanceDetails(attendance);
		getOutputMsg().put("attendanceDetails", attendanceDetails);
		getOutputMsg().put("attendance", attendance);
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDetailJsonConfig()));
	}
	
	/**
	 * json转换config
	 * @return
	 */
	protected JsonConfig getDetailJsonConfig(){
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-d");
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
		return config;
	}
	
	@RequestMapping(value="getPersonByKey") 
	public void getPersonByKey(ModelMap model,HttpServletResponse response){
		String key  = getString("term");
		if(StringUtils.isEmpty(key)){
			return ;
		}
		
		Org org = SystemUtil.getCurrentOrg();
		Position position = SystemUtil.getCurrentPosition();
		Pagination<Person> pagination = null ;
		if(position!=null && position.getLeading()){
			//岗位负责人 可查询下级组织人员考勤信息
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("key", key);
			map.put("orgLongNumber", org.getLongNumber());
			map.put("includeChild", "Y");
			pagination = this.queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonDao.getDismissionPersonByOrg", new Pagination<Person>(20,0), map);
		}else{
			Person person = SystemUtil.getCurrentUser();
			pagination = new Pagination<Person>();
			List<Person> pl = new ArrayList<Person>();
			pl.add(person);
			pagination.setItems(pl);
			pagination.setRecordCount(1);
		}
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	
	/**
	 * 考勤录入 
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="entry")
	@Dependence(method="list")
	public String entry(ModelMap model){
		try{
			String recordDate = getString("recordDate");
			String orgId = getString("orgId","");
			String keyConditions = getString("keyConditions","");
			if(StringUtils.isEmpty(recordDate)){
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DAY_OF_MONTH, -1);//默认录当前日期 前一天的考勤
				recordDate = DateUtil.convertDateToStr(cal.getTime());
			}
			List<AttendanceDetail> detailList = null;
			Map<String, Object> cond = new HashMap<String, Object>();
			cond.put("recordDate", recordDate);
			cond.put("orgId", orgId);
			cond.put("keyConditions", keyConditions);
			Map<String, Object> resultMap = attendanceService.query(cond);
			if("SUCC".equals(resultMap.get("MSG"))){
				detailList = (List<AttendanceDetail>)resultMap.get("detailList");
				if(detailList==null || detailList.size()<1){
				  getRequest().setAttribute("MSG","该组织下没有可录考勤的人员");
				}else{
				  getRequest().setAttribute("attendanceDetails", detailList);
				  getRequest().setAttribute("errMsg", resultMap.get("errMsg").toString());
				}
			}else{
				getRequest().setAttribute("MSG", resultMap.get("MSG").toString());
			}
			getRequest().setAttribute("recordDate",recordDate);
			
		}catch(Exception e){
			getRequest().setAttribute("MSG","系统异常");
			getRequest().setAttribute("errMsg",e.getStackTrace());
		}
		
		return "hr/attendance/entry";
	}
	
	/**
	 * 考勤录入 
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="entry2")
	@Dependence(method="list")
	public String entry2(ModelMap model){
		try{
			String recordDate = getString("recordDate");
			String orgId = getString("orgId","");
			String keyConditions = getString("keyConditions","");
			if(StringUtils.isEmpty(recordDate)){
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DAY_OF_MONTH, -1);//默认录当前日期 前一天的考勤
				recordDate = DateUtil.convertDateToStr(cal.getTime());
			}
			List<AttendanceDetail> detailList = null;
			Map<String, Object> cond = new HashMap<String, Object>();
			cond.put("recordDate", recordDate);
			cond.put("orgId", orgId);
			cond.put("keyConditions", keyConditions);
			Map<String, Object> resultMap = attendanceService.query(cond);
			if("SUCC".equals(resultMap.get("MSG"))){
				detailList = (List<AttendanceDetail>)resultMap.get("detailList");
				if(detailList==null || detailList.size()<1){
				  getRequest().setAttribute("MSG","该组织下没有可录考勤的人员");
				}else{
				  getRequest().setAttribute("attendanceDetails", detailList);
				  getRequest().setAttribute("errMsg", resultMap.get("errMsg").toString());
				}
			}else{
				getRequest().setAttribute("MSG", resultMap.get("MSG").toString());
			}
			getRequest().setAttribute("recordDate",recordDate);
			
		}catch(Exception e){
			getRequest().setAttribute("MSG","系统异常");
			getRequest().setAttribute("errMsg",e.getStackTrace());
		}
		getRequest().setAttribute("attendanceTypes",AttendanceTypeEnum.toList());
		return "hr/attendance/entry2";
	}
	
	/**
	 * 考勤录入 
	 * @param model
	 * @return
	 */
	@RequestMapping(value="entryOne")
	@Dependence(method="list")
	public String entryOne(ModelMap model){
		String detailId = getString("id");
		AttendanceDetail detail = attendanceDetailService.getEntityById(detailId);
		getRequest().setAttribute("detail",detail);
		return "hr/attendance/entryOne";
	}
	

	@Override
	protected String getListView() {
		 
		return "hr/attendance/attendanceList";
	}

	@Override
	protected String getEditView() {
		
		//getRequest().setAttribute("isFilterByOrg", isFilterByOrg());
		return "hr/attendance/attendanceDetailEdit";
	}
	/**
	 * 公司或集团下 或者 组织类型为 人力资源(T02)下的人 不需要根据登录人组织过滤
	 * @return
	 */
	private String isFilterByOrg(){
		Org org = SystemUtil.getCurrentOrg();
		if(org ==null || StringUtils.isEmpty(org.getBusinessTypes())){
			return  "N";//公司 和集团
		}
		//业务类型为 人力资源
		if(OrgUtils.isType(org, "T02")){
			return "N";
		}
		return "Y";
	}

	@Override
	protected String getListMapper() {
		 
		return "com.wuyizhiye.hr.dao.PositionHistoryBillDao.selectByExample";
	}

	@Override
	protected BaseService getService() {
		 
		return attendanceService;
	}

}
