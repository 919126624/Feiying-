package com.wuyizhiye.cmct.ucs.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.enums.CommonFlagEnum;
import com.wuyizhiye.basedata.permission.util.SecurityUtil;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.cmct.phone.enums.PhoneEnableEnum;
import com.wuyizhiye.cmct.ucs.enums.UcsPhoneFlagEnum;
import com.wuyizhiye.cmct.ucs.model.UcsPhoneMember;
import com.wuyizhiye.cmct.ucs.service.UcsPhoneAgentService;
import com.wuyizhiye.cmct.ucs.service.UcsPhoneMemberService;
import com.wuyizhiye.cmct.ucs.util.UcsPhoneMemberUtil;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName UcsPhoneMemberEditController
 * @Description 电信电话开通,后期调用ucs接口维护数据
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/ucsPhoneMember/*")
public class UcsPhoneMemberEditController extends EditController {

	@Autowired
	private UcsPhoneMemberService ucsPhoneMemberService;
	
	@Autowired
	private UcsPhoneAgentService ucsPhoneAgentService;
	@Override
	protected Class getSubmitClass() {
		// TODO Auto-generated method stub
		return UcsPhoneMember.class;
	}

	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return ucsPhoneMemberService;
	}

	@Override
	public void save(HttpServletResponse response)
			throws InstantiationException, IllegalAccessException {
		Object data = getSubmitEntity();
		if(validate(data)){
			UcsPhoneMember member = (UcsPhoneMember) data ;
			Map<String,Object> result = new HashMap<String,Object>();
			Map<String,Object> showTelResult = new HashMap<String,Object>();
			
			//设置key:为特殊字符+用户电话 
			String key="";
			try {
				key = SecurityUtil.encryptPassword((UcsPhoneMemberUtil.getPhoneConfigParam(UcsPhoneMemberUtil.UCSPHONE_KEY)+member.getTelNo()).trim()).toLowerCase();
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			//默认密码:123456
			String passwd="123456";
			try {
				passwd = SecurityUtil.encryptPassword(passwd);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			member.setPasswd(passwd);
			member.setKey(key);
			if(StringUtils.isEmpty(member.getId())){
				//调用接口，新增话伴成员		
				result=UcsPhoneMemberUtil.ucsPhoneUrl(member, UcsPhoneMemberUtil.UCSPHONEADD_URL);
			}else{
				result=UcsPhoneMemberUtil.ucsPhoneUrl(member, UcsPhoneMemberUtil.UCSPHONEEDIT_URL);
				
			}
			String showTelNoMsg="";//调用修改去电显示号码的msg
			/**
			 * 如果设置了去电显示号码,调用接口设置去电显示号码
			 */
			if(null!=member.getShowTel() && !StringUtils.isEmpty(member.getShowTel().getId())){
				showTelResult=UcsPhoneMemberUtil.ucsPhoneUrl(member, UcsPhoneMemberUtil.UCSSHOWTELNO_URL);//设置去电显示号码
				/**
				 * 如果修改去电显示号码没有成功,把去电显示号码清空
				 */
				
				if(!"SUCCESS".equals(showTelResult.get("STATE").toString())){
					member.setShowTelNo(null);
					showTelNoMsg=":但设置去电显示号码时:"+UcsPhoneMemberUtil.getMsgByErrorKey(UcsPhoneMemberUtil.UCSSHOWTELNO_URL, (String)showTelResult.get("MSG"));
				}
			}else{
				showTelResult.put("STATE", "NOSHOWTELNO");
			}
			if("SUCCESS".equals(result.get("STATE").toString())){
				
				if(StringUtils.isEmpty(member.getId())){
					member.setState(UcsPhoneFlagEnum.FREE);
					member.setEnable(PhoneEnableEnum.USE);
					member.setFlag(CommonFlagEnum.NO);
					member.setCreateTime(new Date());
					member.setCreator(SystemUtil.getCurrentUser());
					getService().addEntity(member);
				}else{
					member.setLastUpdateTime(new Date());
					member.setUpdator(getCurrentUser());
					getService().updateEntity(member);
				}
				
				getOutputMsg().put("id", ((CoreEntity)data).getId());
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "坐席保存成功 "+showTelNoMsg);
				
			}else{
				getOutputMsg().put("STATE", "FAIL");
				getOutputMsg().put("MSG", UcsPhoneMemberUtil.getMsgByErrorKey(UcsPhoneMemberUtil.UCSPHONEADD_URL,(String)result.get("MSG")));
			}
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "验证数据实体出现错误");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
}
