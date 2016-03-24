package com.wuyizhiye.hr.affair.controller;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.annotation.Dependence;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.util.OrgUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.ListController;
import com.wuyizhiye.workflow.service.WorkFlowService;
import com.wuyizhiye.hr.affair.model.LeaveOffice;
import com.wuyizhiye.hr.affair.service.LeaveOfficeService;
import com.wuyizhiye.hr.enums.BillStatusEnum;
/**
 * @author Cai.xing
 * @since 2013-04-09
 * */
@Controller
@RequestMapping(value="hr/affair/leaveOffice/*")
public class LeaveOfficeListController extends ListController{
	@Autowired
	private LeaveOfficeService leaveOfficeService ;
	
	@Resource
	private WorkFlowService workFlowService;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="delete")
	@Dependence(method="list")
	public void delete(@RequestParam(required=true,value="id")String id,HttpServletResponse response){
		LeaveOffice entity = leaveOfficeService.getEntityById(id);
		if(entity!=null){
			if(isAllowDelete(entity)){
				getService().deleteEntity(entity);
				if(StringUtils.isNotNull(entity.getProcessInstance())){
				  workFlowService.deleteProcessInstance(entity.getProcessInstance(), "", true);
				}else{
					workFlowService.deleteProcessView(entity.getId());
				}
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "删除成功");
			}else{
				getOutputMsg().put("STATE", "FAIl");
			}
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "记录不存在");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	@Override
	protected CoreEntity createNewEntity() {
		// TODO Auto-generated method stub
		LeaveOffice lo=new LeaveOffice() ;
		lo.setValidateTime(new Date());
		return lo;
	}

	@Override
	protected String getListView() {
		getRequest().setAttribute("billStatusList", BillStatusEnum.toList());
		return "hr/affair/leaveOfficeList";
	}

	@Override
	protected String getEditView() {
		getRequest().setAttribute("isFilterByOrg", isFilterByOrg());
		return "hr/affair/leaveOfficeEdit";
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
		return "com.wuyizhiye.hr.affair.dao.LeaveOfficeDao.select";
	}

	@Override
	protected BaseService<LeaveOffice> getService() {
		return leaveOfficeService;
	}
	public void listData(Pagination<?> pagination,HttpServletResponse response){
		Map<String,Object> map = getListDataParam();
		//map.put("orgLongNumber", this.getCurrentUser().getOrg().getLongNumber());
		if(map.get("beginDate")!=null && !map.get("beginDate").toString().equals("")){
			map.put("beginDate",DateUtil.convertStrToDate(map.get("beginDate").toString(),"yyyy/MM/dd"));
		}else{
			map.put("beginDate",null);
		}
		
		if(map.get("endDate")!=null && !map.get("endDate").toString().equals("")){
			map.put("endDate",DateUtil.convertStrToDate(map.get("endDate").toString(),"yyyy/MM/dd"));
		}else{
			map.put("endDate",null);
		}
		pagination = this.queryExecutor.execQuery(getListMapper(), pagination,map);   
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}

}
