package com.wuyizhiye.cmct.phone.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.cmct.phone.enums.PhoneEnableEnum;
import com.wuyizhiye.cmct.phone.enums.PhoneMemberEnum;
import com.wuyizhiye.cmct.phone.enums.PhoneMemberUseEnum;
import com.wuyizhiye.cmct.phone.model.PhoneMember;
import com.wuyizhiye.cmct.phone.util.PhoneMemberUtil;
import com.wuyizhiye.cmct.phone.util.ProjectMApiRemoteServer;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName PhoneUnMatchController
 * @Description 未分配线路
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/phoneUnmatch/*")
public class PhoneUnMatchController extends ListController {

	@Override
	protected CoreEntity createNewEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getListView() {
		// TODO Auto-generated method stub
		this.getRequest().setAttribute("typeList",PhoneMemberEnum.values());
		this.getRequest().setAttribute("useList",PhoneMemberUseEnum.values());
		this.getRequest().setAttribute("customerId", ParamUtils.getParamValue(PhoneMemberUtil.CMCT_CUSOMETID));
		this.getRequest().setAttribute("state", "UNUSE");
		this.getRequest().setAttribute("org", SystemUtil.getCurrentOrg());
		return "cmct/phone/phoneUnMatchList";
	}

	
	@Override
	public void listData(Pagination<?> pagination, HttpServletResponse response) {
		/**
		 * 从鼎尖yun上面读取当前客户未分配的数据
		 */
		Map<String, String>map=new HashMap<String, String>();
		//map.put("customerId", ParamUtils.getParamValue(PhoneMemberUtil.CMCT_CUSOMETID));//目前先从系统参数读取客户Id
		map.put("customerId",ParamUtils.getParamValue(PhoneMemberUtil.CMCT_CUSOMETID));//目前先从系统参数读取客户Id
		map.put("status", PhoneEnableEnum.UNUSE.getValue());
		map.put("start", String.valueOf(pagination.getCurrentPage()));
		map.put("rows", String.valueOf(pagination.getPageSize()));
		//pagination=ProjectMApiRemoteServer.getPhoneList(pagination,map);
		long begin = System.currentTimeMillis();
		Map<String, String>result=ProjectMApiRemoteServer.getPhoneListByUnMatch(map);
		Long time = System.currentTimeMillis() - begin;
		BigDecimal second = new BigDecimal(time).divide(new BigDecimal("1000.000")).setScale(3);
		if(null!=result && "SUCCESS".equals(result.get("STATE"))){
			String resStr=result.get("phoneList");
			String dataStr=resStr.replace(resStr.substring(resStr.indexOf("exceTime")+11, resStr.lastIndexOf("exceTime")+17), second+"秒");//把时间替换掉
			outPrint(response, dataStr);
		}else{
			outPrint(response, JSONObject.fromObject("{}"));
		}
	}

	@Override	
	protected String getEditView() {
		this.getRequest().setAttribute("pmJson", getString("data"));
		this.getRequest().setAttribute("type", getString("type"));
		return "cmct/phone/phoneUnMatchEdit";
	}

	@Override
	protected String getListMapper() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 校验分配时填写的mac地址是否存在
	 * 根据mac地址判断是否有过数据
	 */
	@RequestMapping(value="checkMac")
	public void checkMac(HttpServletResponse response){
		Map<String, Object> macParm=new HashMap<String, Object>();
		String mac=getString("mac");
		String phone=getString("phone");
		macParm.put("mac", mac);
		if(!StringUtils.isEmpty(mac)){
			List<PhoneMember>pms=queryExecutor.execQuery("com.wuyizhiye.cmct.phone.dao.PhoneMemberDao.checkMac", macParm, PhoneMember.class);
			if(null!=pms && pms.size()>0){
				String existMac="";
				for(PhoneMember pm:pms){
					existMac +=pm.getShowPhone()+"#";
				}
				
				String existPhone=existMac.substring(0,existMac.length()-1);
				if(!StringUtils.isEmpty(phone) && phone.equals(existPhone)){
					getOutputMsg().put("STATE", "SUCCESS");
					getOutputMsg().put("MSG", "效验成功");
				}else{
					getOutputMsg().put("STATE", "FAIL");
					getOutputMsg().put("MSG", existPhone);
				}
			}else{
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "效验成功");
			}
		}else{
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("MSG", "");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}	
	
	/**
	 * 已分配页签点击分配时.手动输入号码.效验号码是否存在
	 */
	@RequestMapping(value="checkPhone")
	public void checkPhone(HttpServletResponse response,Pagination<?> pagination){
		String phone=getString("phone");
		Map<String, String>map=new HashMap<String, String>();
		map.put("customerId",ParamUtils.getParamValue(PhoneMemberUtil.CMCT_CUSOMETID));//目前先从系统参数读取客户Id
		map.put("status", PhoneEnableEnum.UNUSE.getValue());
		map.put("start", "1");//从第一页开始
		map.put("rows", "2000");//把数据全部查询出来,每页显示的条数写死.
		List<?>pag=ProjectMApiRemoteServer.getPhoneList(pagination, map).getItems();
//		List<?> pag = ProjectMApiRemoteServer.getAllPhoneList(map);
		boolean checkPhone=false;
		if(null!=pag && pag.size()>0){
			if(!StringUtils.isEmpty(phone)){
				JSONArray jsonArray=JSONArray.fromObject(pag);
				for(int i=0;i<jsonArray.size();i++){
					JSONObject jsObj=jsonArray.getJSONObject(i);
					String djyPhone=(String) jsObj.get("showNumber");
					if(phone.equals(djyPhone)){
						checkPhone=true;
						getOutputMsg().put("pmJson", jsObj);
						getOutputMsg().put("STATE", "SUCCESS");
						getOutputMsg().put("MSG", "");
						break;
					}else{
						checkPhone=false;
					}
				}
				if(!checkPhone){
					getOutputMsg().put("STATE", "FAIL");
					getOutputMsg().put("MSG", "该客户没有这个号码");
				}
			}else{
				getOutputMsg().put("STATE", "FAIL");
				getOutputMsg().put("MSG", "不能设置空号码 ");
			}
			
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "当前客户没有未分配的号码了");
		}
		
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
}
