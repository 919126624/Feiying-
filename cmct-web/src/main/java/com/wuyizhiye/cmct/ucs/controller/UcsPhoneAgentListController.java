package com.wuyizhiye.cmct.ucs.controller;

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
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName UcsPhoneAgentListController
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/ucsPhoneAgent/*")
public class UcsPhoneAgentListController extends ListController {

	@Autowired
	private UcsPhoneAgentService ucsPhoneAgentService;
	@Override
	protected CoreEntity createNewEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getListView() {
		// TODO Auto-generated method stub
		return "cmct/ucs/ucsPhoneAgentList";
	}

	@Override
	protected String getEditView() {
/*		UcsPhoneMember member=getDefaultDealerId();
		Map<String, Object> result=UcsPhoneMemberUtil.ucsPhoneUrl(member, UcsPhoneMemberUtil.UCSAGENTSELECT_URL);//DealerId
		if("SUCCESS".equals(result.get("STATE").toString())){
			this.getRequest().setAttribute("dealerId", result.get("MSG").toString());
		}else{
			this.getRequest().setAttribute("dealerId", "864");
		}
		this.getRequest().setAttribute("dealerName", member.getUcsPhoneAgent().getAgentName());*/
		return "cmct/ucs/ucsPhoneAgentEdit";
	}

	@Override
	protected String getListMapper() {
		// TODO Auto-generated method stub
		return "com.wuyizhiye.cmct.ucs.dao.UcsPhoneAgentDao.select";
	}

	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return ucsPhoneAgentService;
	}
	
	/**
	 * 删除企业用户或则经销商
	 */
	@Override
	public void delete(String id, HttpServletResponse response) {
		UcsPhoneMember member=new UcsPhoneMember();
		UcsPhoneAgent agent=this.ucsPhoneAgentService.getEntityById(id);
		if(agent != null){
			
			if(StringUtils.isEmpty(agent.getDealerId())){
				getOutputMsg().put("STATE", "FAIL");
				getOutputMsg().put("MSG", "顶级经销商不能删除");
			}else{
				
				/**
				 * 根据agent的dealer找到上级经销商的名字
				 */
				Map<String, Object>param=new HashMap<String, Object>();
				param.put("dealerId", agent.getDealerId());
				UcsPhoneAgent ag=this.ucsPhoneAgentService.getDealerAgent(param);
				
				agent.setDealerUserName(ag.getAgentName());
				
				/**
				 * 设置加密的key 特殊字符+要删除的企业用户名
				 */
				String key="";
				try {
					key = SecurityUtil.encryptPassword((UcsPhoneMemberUtil.getPhoneConfigParam(UcsPhoneMemberUtil.UCSPHONE_KEY)+agent.getAgentId()).trim()).toLowerCase();
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				
				agent.setKey(key);
				member.setUcsPhoneAgent(agent);
				//调用接口，删除企业用户或则经销商
				Map<String,Object> result = UcsPhoneMemberUtil.ucsPhoneUrl(member,UcsPhoneMemberUtil.UCSAGENTDELETE_URL);
				if("SUCCESS".equals(result.get("STATE").toString())){
					/**
					 * 删除数据同时修改该企业下的所有坐席
					 */
					ucsPhoneAgentService.deleteById(id);
					getOutputMsg().put("STATE", "SUCCESS");
					getOutputMsg().put("MSG", "删除成功");
				}else{
					getOutputMsg().put("STATE", "FAIL");
					getOutputMsg().put("MSG", UcsPhoneMemberUtil.getMsgByErrorKey(UcsPhoneMemberUtil.UCSAGENTDELETE_URL,(String)result.get("MSG")));
				}
			}
			
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "记录不存在");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	/**
	 * 设置默认的经销商
	 * @return
	 */
	public UcsPhoneMember getDefaultDealerId(){
		UcsPhoneMember member=new UcsPhoneMember();
		UcsPhoneAgent agent=new UcsPhoneAgent();
		agent.setAgentName("孙海亮");
		agent.setPasswd("dingjian618");
		String key="";
		try {
			key = SecurityUtil.encryptPassword((agent.getAgentName()+agent.getPasswd()+UcsPhoneMemberUtil.getPhoneConfigParam(UcsPhoneMemberUtil.UCSPHONE_KEY)).trim()).toLowerCase();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		agent.setKey(key);
		member.setUcsPhoneAgent(agent);
		
		return member;
	}
}
