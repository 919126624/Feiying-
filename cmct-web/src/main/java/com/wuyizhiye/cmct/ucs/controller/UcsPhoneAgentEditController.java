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
import com.wuyizhiye.basedata.permission.util.SecurityUtil;
import com.wuyizhiye.cmct.ucs.model.UcsPhoneAgent;
import com.wuyizhiye.cmct.ucs.model.UcsPhoneMember;
import com.wuyizhiye.cmct.ucs.service.UcsPhoneAgentService;
import com.wuyizhiye.cmct.ucs.util.UcsPhoneMemberUtil;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName UcsPhoneAgentEditController
 * @Description 企业开户
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/ucsPhoneAgent/*")
public class UcsPhoneAgentEditController extends EditController {

	@Autowired
	private UcsPhoneAgentService ucsPhoneAgentService;

	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return ucsPhoneAgentService;
	}

	@Override
	protected Class getSubmitClass() {
		// TODO Auto-generated method stub
		return UcsPhoneMember.class;
	}
	@Override
	public void save(HttpServletResponse response)
			throws InstantiationException, IllegalAccessException {
		Object data = getSubmitEntity();
		if(validate(data)){
			UcsPhoneMember member = (UcsPhoneMember) data ;
 			UcsPhoneAgent agent=member.getUcsPhoneAgent();
			Map<String,Object> result = new HashMap<String,Object>();
			//新增数据
			if(StringUtils.isEmpty(member.getFlagId())){
				//调用接口，新增企业用户
				//设置新增时加密的key,特殊字符+创建人:为agentId+添加机构的名称
				//找到当前选择的经销商,得到agentId
				UcsPhoneAgent ag=this.ucsPhoneAgentService.getEntityById(agent.getId());
				String key="";
				try {
					key = SecurityUtil.encryptPassword((UcsPhoneMemberUtil.getPhoneConfigParam(UcsPhoneMemberUtil.UCSPHONE_KEY)+ag.getAgentId()).trim()).toLowerCase();
				} catch (Exception e) {
					e.printStackTrace();
				}
				agent.setKey(key);
				agent.setDealerId(ag.getAgentId());
				agent.setId(null);//新增时清空因f7带过来的id,重新生成
				result=UcsPhoneMemberUtil.ucsPhoneUrl(member, UcsPhoneMemberUtil.UCSAGENTADD_URL);
			}else{
				String key="";
				try {
					key = SecurityUtil.encryptPassword((UcsPhoneMemberUtil.getPhoneConfigParam(UcsPhoneMemberUtil.UCSPHONE_KEY)+agent.getAgentId()).trim()).toLowerCase();
				} catch (Exception e) {
					e.printStackTrace();
				}
				agent.setKey(key);
				member.setUcsPhoneAgent(agent);
				result=UcsPhoneMemberUtil.ucsPhoneUrl(member, UcsPhoneMemberUtil.UCSAGENTUPDATE_URL);
			}
			if("SUCCESS".equals(result.get("STATE").toString())){
				if(StringUtils.isEmpty(member.getFlagId())){
					/**
					 * 如果添加用户成功,特定字符+密码,进行,md5加密,然后得到agentId
					 */
					String key="";
					try {
						key = SecurityUtil.encryptPassword((UcsPhoneMemberUtil.getPhoneConfigParam(UcsPhoneMemberUtil.UCSPHONE_KEY)+agent.getPasswd()).trim()).toLowerCase();
					} catch (Exception e) {
						e.printStackTrace();
					}
					agent.setKey(key);
					result=UcsPhoneMemberUtil.ucsPhoneUrl(member, UcsPhoneMemberUtil.UCSAGENTSELECT_URL);//agentId
					if("SUCCESS".equals(result.get("STATE").toString())){
						agent.setAgentId(result.get("MSG").toString());
					}
					
					agent.setCreateTime(new Date());
					agent.setCreator(getCurrentUser());
					getService().addEntity(agent);
				}else{
					getService().updateEntity(agent);
				}
				getOutputMsg().put("id", ((CoreEntity)data).getId());
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "保存成功");
			}else{
				getOutputMsg().put("STATE", "FAIL");
				getOutputMsg().put("MSG", UcsPhoneMemberUtil.getMsgByErrorKey(UcsPhoneMemberUtil.UCSAGENTADD_URL,((String)result.get("MSG")).trim()));
			}
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "验证数据实体出现错误");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
}
