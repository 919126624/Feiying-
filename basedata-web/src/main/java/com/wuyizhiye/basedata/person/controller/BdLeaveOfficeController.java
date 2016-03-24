package com.wuyizhiye.basedata.person.controller;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.annotation.Dependence;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.HttpClientUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.basic.service.BasicDataService;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.person.enums.JobStatusEnum;
import com.wuyizhiye.basedata.person.model.BdLeaveOffice;
import com.wuyizhiye.basedata.person.model.BdPositionHistoryBill;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.service.PersonPositionHistoryService;
import com.wuyizhiye.basedata.person.service.PersonPositionService;
import com.wuyizhiye.basedata.person.service.PersonService;
import com.wuyizhiye.basedata.util.GenerateKey;
import com.wuyizhiye.basedata.util.OrgUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName BdLeaveOfficeController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/leaveOffice/*")
public class BdLeaveOfficeController extends EditController{
	private static Logger logger=Logger.getLogger(BdLeaveOfficeController.class);
	@Autowired 
	private BasicDataService basicDataService;
	@Autowired 
	PersonService personService;
	@Resource
	PersonPositionHistoryService personPositionHistoryService;
	@Autowired
	private PersonPositionService personPositionService;
	private final static String BILLNUMBER = "LEAVE";
	
	@RequestMapping(value="add")
	@Dependence(method="list")
	public String add(ModelMap model){
		String personId = getString("personId");
		BdLeaveOffice dbLeaveOffice =  new BdLeaveOffice();
		if(StringUtils.isNotNull(personId)){
			String mapper = "com.wuyizhiye.basedata.person.dao.PersonDao.getPersonInfoByOrg";
			 Map<String,Object> param = new HashMap<String, Object>();
			 param.put("id", personId);
			 Person person = this.queryExecutor.execOneEntity(mapper, param, Person.class);
			 dbLeaveOffice.setApplyPerson(person);
			 if(person.getPersonPosition()!=null){
			  dbLeaveOffice.setApplyPosition(person.getPersonPosition().getPosition());
			  dbLeaveOffice.setApplyJoblevel(person.getPersonPosition().getJobLevel());
			  if(person.getPersonPosition().getPosition()!=null){
				  dbLeaveOffice.setApplyOrg(person.getPersonPosition().getPosition().getBelongOrg());
			  }
			 }
		}
		model.put("data",dbLeaveOffice);
		return getEditView();
	}
	
	protected String getEditView() {
		getRequest().setAttribute("isFilterByOrg", isFilterByOrg());
		return "basedata/person/leaveOffice4BdEdit";
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
	public void save(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		BdLeaveOffice data = (BdLeaveOffice) getSubmitEntity();
		if(validate(data)){
			if(data instanceof CoreEntity){
					
				try {
					String fnumber = GenerateKey.getKeyCode(null, BILLNUMBER);
					data.setNumber(fnumber);
					 
						data.setTitle(data.getApplyOrg().getName()+""+data.getApplyPosition().getName()+""+data.getApplyPerson().getName()+"申请离职");
						//审核
						Map<String,Object> resultMap  = approvalBill(data);
						if(!"SUCCESS".equals(resultMap.get("STATE"))){
							getOutputMsg().put("STATE", "FAIL");
							getOutputMsg().put("MSG", resultMap.get("MSG"));
						}
								
				} catch (Exception e) {
					getOutputMsg().put("STATE", "FAIL");
					getOutputMsg().put("MSG", "系统异常！请联系管理员！");
					logger.error("", e);
				}
					
				getOutputMsg().put("id", ((CoreEntity)data).getId());
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "保存成功");
			}
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "系统异常！请联系管理员！");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	/**
	 * 审批通过调用方法
	 * @author Cai.xing
	 * */
	@RequestMapping(value="approvalBill")
	@ResponseBody
	@Transactional
	public Map<String,Object> approvalBill(BdLeaveOffice leaveOffice){
		String billId = getString("billId");
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("FID",billId);
			//离职
			Person per = personService.getEntityById(leaveOffice.getApplyPerson().getId());
			per.setJobStatus(basicDataService.getEntityByNumber(JobStatusEnum.DIMISSION.getValue()));
			per.setLeaveDate(leaveOffice.getValidateTime());
			
			BdPositionHistoryBill bill = new BdPositionHistoryBill();
			bill.setApplyPerson(leaveOffice.getApplyPerson());
			bill.setApplyOrg(leaveOffice.getApplyOrg());
			bill.setApplyChangeOrg(null);
			bill.setApplyPosition(leaveOffice.getApplyPosition());
			bill.setApplyPosition(leaveOffice.getApplyPosition());
			bill.setApplyJoblevel(leaveOffice.getApplyJoblevel());
			bill.setBillNumber(leaveOffice.getNumber());
			bill.setChangeType("LEAVE");
			bill.setJobStatus(leaveOffice.getApplyPerson().getJobStatus());
			bill.setPrimary(true);
			bill.setTakeOfficeDate(leaveOffice.getApplyPerson().getInnerDate());
			bill.setBillStatus("APPROVED");
			bill.setIsdisable("1");
			bill.setEffectdate(leaveOffice.getValidateTime());
			bill.setHandOverPerson(leaveOffice.getGivePerson());//离职交接人 by hlz
			bill.setCreator(leaveOffice.getCreator());
			bill.setUpdator(leaveOffice.getUpdator());
			bill.setJobStatus(per.getJobStatus());//设置申请人岗位状态
			personPositionHistoryService.updatePositionHistoryByBill(bill);
			personPositionHistoryService.addPositionHistoryByBill(bill);
			personService.updateEntity(per);
			//删除任职信息
			//根据人员删除任职信息
			personPositionService.deleteByPersonId(leaveOffice.getApplyPerson().getId());
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("MSG", "审批通过！");
			
			/**
			 * 将用户同步到微信企业号 begin
			 */
		    try{
		    	
		    	String strJson = "{";
		        strJson += "\"userid\":\""+per.getNumber()+"\"";
		        strJson += "}";
		        
			   String str= HttpClientUtil.callHttpUrl(SystemUtil.getBase()+"/weixinapi/weixinperson?t=del", URLEncoder.encode(strJson,"utf-8"));
//			   System.out.println(str);
		    }catch(Exception ex){
		    	ex.printStackTrace();
		    }
		    /**
			 * 将用户同步到微信企业号 end
			 */	
		} catch (Exception e) {
			logger.error("", e);
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "系统异常！请联系管理员！");
		}
		return getOutputMsg();
	}
	 
	 
	@Override
	protected Class<BdLeaveOffice> getSubmitClass() {
		return BdLeaveOffice.class;
	}
	@Override
	protected BaseService<BdLeaveOffice> getService() {
		return null;
	}
}
