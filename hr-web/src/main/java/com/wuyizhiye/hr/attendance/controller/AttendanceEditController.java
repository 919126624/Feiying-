package com.wuyizhiye.hr.attendance.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.service.OrgService;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.BaseController;
import com.wuyizhiye.hr.attendance.model.Attendance;
import com.wuyizhiye.hr.attendance.model.AttendanceDetail;
import com.wuyizhiye.hr.attendance.model.AttendanceOrg;
import com.wuyizhiye.hr.attendance.service.AttendanceOrgService;
import com.wuyizhiye.hr.attendance.service.AttendanceService;

/**
 * 考勤
 * @author ouyangyi
 * @since 2013-5-20 下午04:37:32
 */
@Controller
@RequestMapping(value="hr/attendance/*")
public class AttendanceEditController extends BaseController{
	

	@Resource
	private AttendanceService attendanceService ;
	
	@Resource
	private AttendanceOrgService attendanceOrgService ;
	
	@Resource
	private OrgService orgService ;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="saveAttendanceDetails")
	public void saveAttendanceDetails(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		String recordDate = getString("recordDate");
		String detailsJSON = getString("detailsJSON");
		JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] {"yyyy-MM-dd", "yyyy-MM-dd"}));
		List<AttendanceDetail> detailList = null;
		if(!StringUtils.isEmpty(detailsJSON)){
			detailList = new ArrayList<AttendanceDetail>(JSONArray.toCollection(JSONArray.fromObject(detailsJSON), AttendanceDetail.class));
		}
		if(detailList==null || detailList.size()<1){
			getOutputMsg().put("MSG", "没有可操作的数据");
			outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
			return;
		}
		
		Map<String, Object> cond = new HashMap<String, Object>();
		cond.put("recordDate", recordDate);
		cond.put("data", detailList);
		try{
		  Map<String, Object> resultMap = attendanceService.saveEntry(cond);
		 
		  getOutputMsg().put("MSG", resultMap.get("MSG"));
		}catch(Exception e){
			getOutputMsg().put("MSG","保存失败"+e.getMessage());
			getOutputMsg().put("errMsg",e.getStackTrace());
			e.printStackTrace();
		}
		
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	@RequestMapping(value="saveAttendanceDetailOne")
	public void saveAttendanceDetailOne(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		String recordDate = getString("recordDate");
		String detailJSON = getString("detailJSON");
		List<AttendanceDetail> detailList = new ArrayList<AttendanceDetail>();
		AttendanceDetail detail = new AttendanceDetail();
		if(!StringUtils.isEmpty(detailJSON)){
			JSONObject json=JSONObject.fromObject(detailJSON);
			detail = (AttendanceDetail)JSONObject.toBean(json, AttendanceDetail.class);
			detailList.add(detail);
		}
		if(detailList==null || detailList.size()<1){
			getOutputMsg().put("MSG", "没有可操作的数据");
			outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
			return;
		}
		
		Map<String, Object> cond = new HashMap<String, Object>();
		cond.put("recordDate", recordDate);
		cond.put("data", detailList);
		try{
		  Map<String, Object> resultMap = attendanceService.saveEntry(cond);
		 
		  getOutputMsg().put("MSG", resultMap.get("MSG"));
		}catch(Exception e){
			getOutputMsg().put("MSG","保存失败"+e.getMessage());
			getOutputMsg().put("errMsg",e.getStackTrace());
			e.printStackTrace();
		}
		
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	@RequestMapping(value="saveAttendanceOrg")
	public void saveAttendanceOrg(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		String orgIds = getString("orgIds","");
		String[] orgIdArr = orgIds.split(","); 
			 
		//先删除 后新增
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("creatorId", SystemUtil.getCurrentUser().getId());
		this.queryExecutor.executeUpdate("com.wuyizhiye.hr.attendance.dao.AttendanceOrgDao.deleteByCond",params);
		Date now = new Date();
		if(StringUtils.isNotNull(orgIds)){
			for(String orgId:orgIdArr){
				AttendanceOrg attOrg = new AttendanceOrg();
				attOrg.setCreator(SystemUtil.getCurrentUser());
				Org org = orgService.getEntityById(orgId);
				attOrg.setOrgId(org.getId());
				attOrg.setOrgName(org.getName());
				attOrg.setCreateTime(now);
				attOrg.setUUID();
				attendanceOrgService.addEntity(attOrg);
			}
		}
		getOutputMsg().put("MSG","SUCC");
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}

	/**
	 * 审核
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="updateAttendanceState")
	public void updateAttendanceState(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		String attendanceArray = getString("attendanceArray");
		String approveType = getString("approveType");
		String approvalRemark = getString("approvalRemark");
		List<Attendance> attendanceList = null;
		if(!StringUtils.isEmpty(attendanceArray)){
			attendanceList = new ArrayList<Attendance>(JSONArray.toCollection(JSONArray.fromObject(attendanceArray), Attendance.class));
		}
		if(attendanceList==null || attendanceList.size()<1){
			getOutputMsg().put("counter", "0");
			outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
			return ;
		} 
		getOutputMsg().put("counter", attendanceList.size());
		//审核
		String approveFlag = "";
		try{
			approveFlag = attendanceService.updateAttendanceState(attendanceList,approveType,approvalRemark);
		}catch(Exception e){
			approveFlag = "FAIL";
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", e.getStackTrace());
		}
		
		if("SUCC".equals(approveFlag)){
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("MSG", "操作成功");
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "操作失败");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
}
