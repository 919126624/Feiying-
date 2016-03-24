package com.wuyizhiye.hr.affair.controller;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.annotation.Dependence;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.util.OrgUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.ListController;
import com.wuyizhiye.workflow.service.WorkFlowService;
import com.wuyizhiye.hr.affair.model.PersonPositionChange;
import com.wuyizhiye.hr.affair.model.PositionHistoryBill;
import com.wuyizhiye.hr.affair.service.PositionHistoryBillService;
import com.wuyizhiye.hr.enums.BillStatusEnum;
import com.wuyizhiye.hr.utils.BaseUtils;

/**
 * 员工组织 职 级异动   -- 跑盘员删除
 * @author ouyangyi
 * @since 2013-4-25 下午02:01:05
 */
@Controller
@RequestMapping(value="hr/employeedelrundisk/*")
public class EmployeeDelRunDiskListController extends ListController{
	
	@Resource
	private PositionHistoryBillService positionHistoryBillService ;
	
	@Resource
	private WorkFlowService workFlowService;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="delete")
	@Dependence(method="list")
	public void delete(@RequestParam(required=true,value="id")String id,HttpServletResponse response){
		PositionHistoryBill entity = positionHistoryBillService.getEntityById(id);
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
		PersonPositionChange pc = new PersonPositionChange();
		pc.setEffectdate(new Date());
		BaseUtils.setWho(pc, false);
		return pc;
	}

	@Override
	protected String getListView() {
		getRequest().setAttribute("billStatusList", BillStatusEnum.toList());
		getRequest().setAttribute("isFilterByOrg", isFilterByOrg());
		return "hr/affair/employeedelrundisk/employeeDelRunDiskList";
	}

	@Override
	protected String getEditView() {
		
		getRequest().setAttribute("isFilterByOrg", isFilterByOrg());
		return "hr/affair/employeedelrundisk/employeeDelRunDiskEdit";
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
		 
		return positionHistoryBillService;
	}
	
}
