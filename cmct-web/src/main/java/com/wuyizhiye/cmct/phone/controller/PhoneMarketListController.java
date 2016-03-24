package com.wuyizhiye.cmct.phone.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.util.ExcelReader;
import com.wuyizhiye.cmct.phone.model.PhoneMarket;
import com.wuyizhiye.cmct.phone.model.PhoneMarketBindPerson;
import com.wuyizhiye.cmct.phone.service.PhoneMarketBindPersonService;
import com.wuyizhiye.cmct.phone.service.PhoneMarketService;
import com.wuyizhiye.cmct.phone.util.FjCtCmctMemberUtil;
import com.wuyizhiye.cmct.phone.util.PhoneMarketUtil;
import com.wuyizhiye.cmct.phone.util.ProjectMApiRemoteServer;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName PhoneMarketListController
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/phoneMarket/*")
public class PhoneMarketListController extends ListController {

	@Autowired
	private PhoneMarketService phoneMarketService;
	
	@Autowired
	private PhoneMarketBindPersonService phoneMarketBindPersonService;
	
	@Override
	protected CoreEntity createNewEntity() {
		//设置默认值
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		PhoneMarket phoneMarket=new PhoneMarket();
		phoneMarket.setName(getCurrentUser().getUserName()+sdf.format(new Date()));
		phoneMarket.setAmBeginTime("09:00");
		phoneMarket.setAmEndTime("12:00");
		phoneMarket.setPmBeginTime("13:00");
		phoneMarket.setPmEndTime("22:00");
		String marketPersonId=getString("marketPersonId");
		if(StringUtils.isEmpty(marketPersonId)){
			Map<String, Object>param=new HashMap<String, Object>();
			param.put("personId", getCurrentUser().getId());
			List<PhoneMarketBindPerson> pms=queryExecutor.execQuery(PhoneMarketBindPerson.MAPPER+".select", param, PhoneMarketBindPerson.class);
			if(null!=pms && pms.size()>0){
				phoneMarket.setPhoneMbp(pms.get(0));
			}
		}else{			
			PhoneMarketBindPerson pmb=this.phoneMarketBindPersonService.getEntityById(marketPersonId);
			phoneMarket.setPhoneMbp(pmb);
		}
		return phoneMarket;
	}

	@Override
	protected String getListView() {
		return "cmct/phone/phoneMarketList" ;
	}

	@RequestMapping(value="manage")
	public String manage(){
		return "cmct/phone/phoneMarketManage" ;
	}
	
	@Override
	protected String getEditView() {
		return "cmct/phone/phoneMarketEdit" ;
	}

	@Override
	protected String getListMapper() {
		return PhoneMarket.MAPPER+".select";
	}

	@Override
	protected BaseService getService() {
		return phoneMarketService;
	}

	@RequestMapping(value="voiceList")
	public String voiceList(){
		return "cmct/phone/phoneVoiceList" ;
	}
	
	@Override
	protected Map<String, Object> getListDataParam() {
		DateFormat df=new SimpleDateFormat("yyyy/MM/dd");
		Map<String,Object> param=super.getListDataParam();
		try{
			if(null!=param.get("startDate")){
				param.put("startDate", df.parse(param.get("startDate").toString()));
			}
			if(null!=param.get("endDate")){
				param.put("endDate", DateUtil.getNextDay(df.parse(param.get("endDate").toString())));
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return param;
	}
	
	/**
	 * 获取鼎尖yun上面传的语音文件
	 */
	@RequestMapping(value="getDingjianVoice")
	public void getDingjianVoice(HttpServletResponse response){
		Map<String, String> map = new HashMap<String, String>();
		String configId=getString("configId");
		if(!StringUtils.isEmpty(configId)){
			map.put("configId", configId);
			map.put("start", String.valueOf(1));
			map.put("rows", String.valueOf(Integer.MAX_VALUE));
			Map<String, String>result=ProjectMApiRemoteServer.getVoiceByCustomerId(map);
			if(null!=result && "SUCCESS".equals(result.get("STATE"))){
				outPrint(response, result.get("voiceList"));
			}else{
				outPrint(response, "{}");
			}
		}
	}
	
	/**
	 * 转换电话
	 */
	@RequestMapping(value="conversionPhone",method=RequestMethod.POST)
	public void conversionPhone(@RequestParam(value="phone")MultipartFile file,
	HttpServletRequest request,HttpServletResponse response){
		if(!file.isEmpty()){
			String originalFileName  = file.getOriginalFilename();
			try {
				List<String[]> excelData = ExcelReader.getExcelData(file.getInputStream(), 1,originalFileName.endsWith(".xls"));
				StringBuffer sb=new StringBuffer();
				for(int i = 0 ; i < excelData.size() ; i ++ ){
					String[] data =  excelData.get(i);
					
					if(data==null || StringUtils.isEmpty(data[0]) ){
						continue;
					}
					if("电话号码".equals(data[0])){
						continue;
					}
					sb.append(data[0]+",");
				}
				String phoneStr="";
				if(sb.length()>0){
					phoneStr=sb.toString().substring(0, sb.length()-1);
				}
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", phoneStr);
			} catch (IOException e) {
				getOutputMsg().put("STATE","FAILE");
				getOutputMsg().put("MSG", e.getMessage());
			}
			
		}else{
			getOutputMsg().put("STATE","FAILE");
			getOutputMsg().put("MSG", "文件不存在");
		}
		response.setContentType("text/html");
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	/**
	 * 批量发送营销任务暂停接口
	 */
	@RequestMapping(value="updateStauts")
	public void stop(@RequestParam(required=true,value="id")String id,HttpServletResponse response){
		String status=getString("status");
		String statusValue=getString("statusValue");
		PhoneMarket phoneMarket=this.phoneMarketService.getEntityById(id);
		if(null==phoneMarket.getPhoneMbp()){
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "没有找到关联的计费号码");
		}else{			
			Map<String, Object>param=new HashMap<String, Object>();
			param.put("id", phoneMarket.getPhoneMbp().getId());
			param.put("personId", getCurrentUser().getId());
			List<PhoneMarketBindPerson> pms=queryExecutor.execQuery(PhoneMarketBindPerson.MAPPER+".select", param, PhoneMarketBindPerson.class);
			if(null!=pms && pms.size()>0){
				phoneMarket.setPhoneMbp(pms.get(0));
				Map<String, Object>res=FjCtCmctMemberUtil.commonMarketMethond(phoneMarket, status);
				if(null!=res && "SUCCESS".equals(res.get("STATE"))){
					phoneMarket.setStatus(statusValue);
					this.phoneMarketService.updateEntity(phoneMarket);
					getOutputMsg().put("STATE", "SUCCESS");
					getOutputMsg().put("MSG", "更新成功");
				}else{
					getOutputMsg().put("STATE", "FAIL");
					getOutputMsg().put("MSG", PhoneMarketUtil.getMsgByErrorKey(res.get("MSG").toString()));
				}
			}else{
				getOutputMsg().put("STATE", "FAIL");
				getOutputMsg().put("MSG", "没有找到关联的计费号码");
			}
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	@RequestMapping(value="loadVoiceName")
	public void loadVoiceName(HttpServletResponse response){
		String configId=getString("configId");
		if(!StringUtils.isEmpty(configId)){
			
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "appid为空");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
}
