package com.wuyizhiye.cmct.phone.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.cmct.phone.enums.PhoneControlTypeEnum;
import com.wuyizhiye.cmct.phone.enums.PhoneEnableEnum;
import com.wuyizhiye.cmct.phone.enums.PhoneMemberEnum;
import com.wuyizhiye.cmct.phone.enums.PhoneStateEnum;
import com.wuyizhiye.cmct.phone.model.PhoneMember;
import com.wuyizhiye.cmct.phone.service.PhoneMemberService;
import com.wuyizhiye.cmct.phone.util.PhoneMemberUtil;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName PhoneMemberEditController
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping("cmct/phonemember/*")
public class PhoneMemberEditController extends EditController{
	
	@Autowired
	private PhoneMemberService phoneMemberService;

	@SuppressWarnings("rawtypes")
	@Override
	protected Class getSubmitClass() {
		// TODO Auto-generated method stub
		return PhoneMember.class;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected BaseService getService() {
		return phoneMemberService;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void save(HttpServletResponse response)throws InstantiationException, IllegalAccessException {
		Object data = getSubmitEntity();
		if(validate(data)){
			PhoneMember callPerson = (PhoneMember) data ;
			Map<String,Object> result = new HashMap<String,Object>();
			
			if(StringUtils.isEmpty(callPerson.getUserId())){
				//调用接口，新增话伴成员
				result = PhoneMemberUtil.addMenber(callPerson);
				callPerson.setUserId(result.get("userId")!=null?result.get("userId").toString():null);
				callPerson.setLoginNumber(result.get("loginNumber")!=null?result.get("loginNumber").toString():null);
				callPerson.setPassword(result.get("password")!=null?result.get("password").toString():null);
				if(StringUtils.isEmpty(callPerson.getDefaultAnswerPhone())){
					callPerson.setDefaultAnswerPhone(callPerson.getLoginNumber());//新增的成员，接口默认注册手机为接听话机
				}
			}else{
				//先去找到本地记录成员
				Map<String,Object> queryParam = new HashMap<String,Object>();
				queryParam.put("userId", callPerson.getUserId());
				PhoneMember dbPerson = queryExecutor.execOneEntity(PhoneMember.MAPPER+".select", queryParam, PhoneMember.class);
				if(dbPerson!=null){
					PhoneMember  editPerson = new PhoneMember() ; 
					try {
						BeanUtils.copyProperties(editPerson,callPerson)  ;
					} catch (Exception e) {
						e.printStackTrace();
					}
					editPerson.setLoginNumber(callPerson.getLoginNumber());
					if(!StringUtils.isEmpty(callPerson.getShowPhone()) && callPerson.getShowPhone().equals(dbPerson.getShowPhone())){
						editPerson.setShowPhone(null);
					}
					//调用接口，修改话伴成员
					result = PhoneMemberUtil.editMenber(editPerson);
				}
			}
			if("SUCCESS".equals(result.get("STATE").toString())){
				if(PhoneMemberEnum.SHAR.equals(callPerson.getSetType())){//选择 共享模式   设置 专用人为空
					callPerson.setOnlyUser(null);
				}
				callPerson.setLastUpdateTime(new Date());
				callPerson.setUpdator(SystemUtil.getCurrentUser());
				callPerson.setControlType(PhoneControlTypeEnum.PERSONAL);
				if(StringUtils.isEmpty(callPerson.getId())){
					callPerson.setCreator(SystemUtil.getCurrentUser());
					callPerson.setCreateTime(new Date());
					callPerson.setEnable(PhoneEnableEnum.USE);
					callPerson.setState(PhoneStateEnum.FREE);//开通状态
					getService().addEntity(callPerson);
				}else{
					if(!StringUtils.isEmpty(callPerson.getNewPhone())){
						callPerson.setLoginNumber(callPerson.getNewPhone());
						callPerson.setDefaultAnswerPhone(callPerson.getNewPhone());
					}
					getService().updateEntity(callPerson);
				}
				getOutputMsg().put("id", ((CoreEntity)data).getId());
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "保存成功");
			}else{
				getOutputMsg().put("STATE", "FAIL");
				getOutputMsg().put("MSG", result.get("MSG"));
			}
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "验证数据实体出现错误");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	
	/**
	 * 分配设置
	 */
	@RequestMapping(value="batchMatchPhone")
	public void batchMatchPhone(HttpServletResponse response){
		Object data = getSubmitEntity();
		PhoneMember pm=(PhoneMember)data;
		Map<String, Object>result=this.phoneMemberService.batchMatchPhone(pm);
		outPrint(response, JSONObject.fromObject(result, getDefaultJsonConfig()));
	}
	
	
	/**
	 * 已分配线路修改保存,by lxl 14.3.13
	 */
	@RequestMapping(value="updateMatch")
	public void updateMatch(HttpServletResponse response){
		Object data = getSubmitEntity();
		if(validate(data)){
			if(data instanceof CoreEntity){
				if(StringUtils.isEmpty(((CoreEntity)data).getId())){
					getService().addEntity((CoreEntity)data);
				}else{
					getService().updateEntity((CoreEntity)data);
				}
				getOutputMsg().put("id", ((CoreEntity)data).getId());
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "保存成功");
			}
		}else{
			getOutputMsg().put("STATE", "FAIL");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
}
