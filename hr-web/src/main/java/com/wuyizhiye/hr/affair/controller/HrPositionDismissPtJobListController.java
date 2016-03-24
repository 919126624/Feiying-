package com.wuyizhiye.hr.affair.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.util.OrgUtils;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.ListController;
import com.wuyizhiye.hr.affair.model.PersonPositionChange;
import com.wuyizhiye.hr.affair.service.PositionHistoryBillService;
import com.wuyizhiye.hr.enums.BillStatusEnum;
import com.wuyizhiye.hr.utils.BaseUtils;

/**
 * 员工组织 职 级异动   -- 撤职
 * @author ouyangyi
 * @since 2013-4-10 上午09:28:08
 */
@Controller
@RequestMapping(value="hr/positiondismissptjob/*")
public class HrPositionDismissPtJobListController extends ListController{
	
	static final String ALLOW_BACK_APPROVE = "ALLOW_BACK_APPROVE";//是否允许反审核 
	
	@Resource
	private PositionHistoryBillService positionHistoryBillService ;

	@Override
	protected CoreEntity createNewEntity() {
		PersonPositionChange pc = new PersonPositionChange();
		BaseUtils.setWho(pc, false);
		return pc;
	}

	@Override
	protected String getListView() {
		String allBackApprove = ParamUtils.getParamValue(SystemUtil.getCurrentOrg().getId(), ALLOW_BACK_APPROVE);
		if(StringUtils.isEmpty(allBackApprove)){
			allBackApprove ="N";//默认不允许反审核N
		}
		getRequest().setAttribute("allBackApprove", allBackApprove);
		getRequest().setAttribute("billStatusList", BillStatusEnum.toList());
		return "hr/positiondismissptjob/positionDismissPtJobList";
	}

	@Override
	protected String getEditView() {
		getRequest().setAttribute("isFilterByOrg", isFilterByOrg());
		return "hr/positiondismissptjob/positionDismissPtJobEdit";
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
