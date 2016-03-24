/**
 * com.wuyizhiye.basedata.person.controller.PersonEditController.java
 */
package com.wuyizhiye.hr.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.base.util.SystemConfig;
import com.wuyizhiye.basedata.basic.service.BasicDataService;
import com.wuyizhiye.basedata.images.model.Photo;
import com.wuyizhiye.basedata.images.model.PhotoAlbum;
import com.wuyizhiye.basedata.images.service.PhotoAlbumService;
import com.wuyizhiye.basedata.images.service.PhotoService;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.org.service.PositionService;
import com.wuyizhiye.basedata.person.enums.CardTypeEnum;
import com.wuyizhiye.basedata.person.enums.JobStatusEnum;
import com.wuyizhiye.basedata.person.enums.UserStatusEnum;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.model.PersonPosition;
import com.wuyizhiye.basedata.person.service.PersonService;
import com.wuyizhiye.basedata.util.GenerateKey;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.BaseController;
import com.wuyizhiye.framework.base.controller.EditController;
import com.wuyizhiye.hr.affair.model.PositionHistoryBill;
import com.wuyizhiye.hr.affair.service.LeaveOfficeService;
import com.wuyizhiye.hr.enums.BillStatusEnum;
import com.wuyizhiye.hr.enums.PositionChangeTypeEnum;
import com.wuyizhiye.hr.model.AgentCertificate;
import com.wuyizhiye.hr.model.Education;
import com.wuyizhiye.hr.model.RewardPunishment;
import com.wuyizhiye.hr.model.WorkExperience;
import com.wuyizhiye.hr.service.HrPersonService;

/**
 * 人事对外接口
 * @author 孙海涛
 *
 * @since 2014-12-6
 */
@Controller
@RequestMapping(value="hr/interface/*")
public class HrInterfaceController extends BaseController {
	@Autowired
	private PersonService personService;
	@Autowired
	private PhotoAlbumService photoAlbumService;
	@Autowired
	private LeaveOfficeService leaveOfficeService;
	
	@Resource
	private PositionService positionService;
	@Autowired 
	private BasicDataService basicDataService;
	
	
	@RequestMapping(value="leaveOffice")
	public void leaveOffice(HttpServletResponse response)
			throws InstantiationException, IllegalAccessException {
		try {
		    String userNumber=getString("userNumber");//工号
		    String idcard=getString("idcard");//身份证
		    String effectiveDateStr=getString("effectiveDate");//生效日期
		    
		    if(StringUtils.isEmpty(userNumber) || StringUtils.isEmpty(idcard) || StringUtils.isEmpty(effectiveDateStr) ){
		    	getOutputMsg().put("STATE", 3);
				getOutputMsg().put("MSG", "员工工号和身份证号不能为空！");
		    }else{
		    	Date effectdate=DateUtil.convertStrToDate(effectiveDateStr);
		    	Date now=new Date();
		    	if(DateUtil.compareDate(now, effectdate)>=1){  
			    	HashMap<String, Object> param=new HashMap<String, Object>();
			    	param.put("primary", 1);
			    	param.put("idcard", idcard);
			    	param.put("userNumber", userNumber);
			    	Person ps =this.queryExecutor.execOneEntity("com.wuyizhiye.basedata.person.dao.PersonDao.getPersonInfoByCondition", param, Person.class);
			    	if(ps==null){
			    		getOutputMsg().put("STATE", 4);
						getOutputMsg().put("MSG", "未找到离职人员数据！idcard:"+idcard+" userNumber:"+userNumber);
			    	}else{
			    		
			    		PositionHistoryBill bill = new PositionHistoryBill();
						bill.setApplyPerson(ps);
			    		if(ps.getPersonPosition()!=null && ps.getPersonPosition().getPosition()!=null){
			    			bill.setApplyOrg(ps.getPersonPosition().getPosition().getBelongOrg());
			    		}
						bill.setApplyChangeOrg(null);
						bill.setApplyPosition(ps.getPersonPosition()!=null?ps.getPersonPosition().getPosition():null);
						bill.setApplyJoblevel(ps.getPersonPosition()!=null?ps.getPersonPosition().getJobLevel():null);
						bill.setChangeType(PositionChangeTypeEnum.LEAVE.getValue());
						bill.setPrimary(true);
						bill.setTakeOfficeDate(ps.getInnerDate());
						bill.setBillStatus(BillStatusEnum.APPROVED.getValue());
						bill.setIsdisable("1");
						
						bill.setEffectdate(effectdate);
						
						bill.setHandOverPerson(null);//离职交接人 by hlz
						bill.setCreator(null);
						bill.setUpdator(null);
						bill.setJobStatus(basicDataService.getEntityByNumber(JobStatusEnum.DIMISSION.getValue()));//设置申请人岗位状态
						
						Person per=personService.getEntityById(ps.getId());
						per.setJobStatus(basicDataService.getEntityByNumber(JobStatusEnum.DIMISSION.getValue()));
						per.setLeaveDate(effectdate);
						
						this.leaveOfficeService.leaveOffice(per, bill);
			    	}
		    	}else{
		    		getOutputMsg().put("STATE", 3);
					getOutputMsg().put("MSG", "该单据申请日期大于当前时间，不能调用！");
		    	}
		    }
		}catch (Exception e){
			e.printStackTrace();
			getOutputMsg().put("STATE", 5);
			getOutputMsg().put("MSG", "离职接口调用失败！");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	 
}
