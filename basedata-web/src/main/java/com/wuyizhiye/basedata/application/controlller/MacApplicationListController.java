package com.wuyizhiye.basedata.application.controlller;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.access.enums.AccessTypeEnum;
import com.wuyizhiye.basedata.access.enums.TerminalTypeEnum;
import com.wuyizhiye.basedata.access.model.MacAddress;
import com.wuyizhiye.basedata.application.enums.ApplicationStatusEnum;
import com.wuyizhiye.basedata.application.model.MacApplication;
import com.wuyizhiye.basedata.application.sevice.MacApplicationService;
import com.wuyizhiye.basedata.enums.CommonFlagEnum;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName MacApplicationListController
 * @Description 终端(Mac地址)申请列表
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/application/address/*")
public class MacApplicationListController extends ListController {
	@Autowired
	private MacApplicationService macApplicationService;
	
	@Override
	protected CoreEntity createNewEntity() {
		//得到前台(macApplicationList.js)数据id,并设给前台(macApplicationEdit.jsp)
		this.getRequest().setAttribute("id", this.getString("id"));
		return null;
	}
	
	// 添加终端许可(Mac地址)
	@RequestMapping(value="addMac")
	public void addMac(@RequestParam(value="id")String id,HttpServletResponse response){
		
		MacApplication data = macApplicationService.getEntityById(id);
		MacAddress macData = new MacAddress();
		if(data==null){
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "数据不存在");
		}else{
			//数据库随机码与输入随机码验证
			if(!StringUtils.isEmpty(getString("randomNumber"))&&getString("randomNumber").equals(data.getRandomNumber())){ 
				macData.setAccessType(AccessTypeEnum.SHARE);
				macData.setTerminalType(TerminalTypeEnum.MOBILEPHONE);
				macData.setEnable(true);
				macData.setMac(data.getMac());
				Org o = new Org();
				o.setId(getString("orgid"));
				macData.setOrg(o);		
				Person p = new Person();
				p.setId(getString("personid"));
				macData.setPerson(p);
				if(StringUtils.isEmpty(getString("isLoginSelf"))){
					macData.setIsLoginSelf(CommonFlagEnum.NO);
				}else{
					macData.setIsLoginSelf(CommonFlagEnum.valueOf(getString("isLoginSelf")));
				}
				/*macAddressService.addEntity(macData);
				data.setStatus(ApplicationStatusEnum.APPROVED);
				macApplicationService.updateEntity(data);*/
				data.setStatus(ApplicationStatusEnum.APPROVED);
				//保存Mac地址的同时更新审批状态
				macApplicationService.saveMacAddressAndApplication(macData, data);
					
				getOutputMsg().put("MSG", "操作成功！");
				getOutputMsg().put("STATE", "SUCCESS");
			}else{
				getOutputMsg().put("MSG", "输入随机码不正确！");
			}

		}
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	
	//请求拒绝的处理
	@RequestMapping(value="reject")
	public void reject(@RequestParam(value="id")String id,HttpServletResponse response){
		MacApplication data = macApplicationService.getEntityById(id);
		if(data==null){
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "数据不存在");
		}else{
			data.setStatus(ApplicationStatusEnum.REJECT);
			macApplicationService.updateEntity(data);
					
			getOutputMsg().put("MSG", "操作成功！");
			getOutputMsg().put("STATE", "SUCCESS");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	
	//添加用户申请唯一标识码
	@RequestMapping(value="addMacApplication")
	public String addMacApplication(ModelMap model,@RequestParam(value="mac",required=true)String mac,HttpServletResponse response){
			
			MacApplication macApplication = new MacApplication();
			model.put("mac", mac);
			model.put("status",ApplicationStatusEnum.SAVE);
			
			List<MacApplication> macApplication1=queryExecutor.execQuery(getListMapper(), model, MacApplication.class);
			if(macApplication1.size()>0){
				macApplication = macApplication1.get(0);

				model.put("randomNumber", macApplication.getRandomNumber());
				model.put("MSG", "您的申请正在审批中````");

			}else{	
				macApplication.setMac(mac);
				macApplication.setRandomNumber(String.valueOf(Math.random()*9000+1000).substring(0, 4));
				macApplication.setAplTime(new Date());
				macApplication.setStatus(ApplicationStatusEnum.SAVE);
						
				macApplicationService.addEntity(macApplication);
				model.put("randomNumber", macApplication.getRandomNumber());
				model.put("MSG", "申请成功!");
			}
			
			this.putCommonPath();
			return "mobile/system/postSuccess";
	}
	
	@Override
	protected String getEditView() {
		this.getRequest().setAttribute("accessType", AccessTypeEnum.values());//权限类型
		this.getRequest().setAttribute("terminalType", TerminalTypeEnum.values());//终端设备类型
		return "basedata/application/macApplicationEdit";
	}
	
	@Override
	protected String getListView() {
		return "basedata/application/macApplicationList";
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.application.dao.MacApplicationDao.select";
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	protected BaseService getService() {
		return macApplicationService;
	}
}
