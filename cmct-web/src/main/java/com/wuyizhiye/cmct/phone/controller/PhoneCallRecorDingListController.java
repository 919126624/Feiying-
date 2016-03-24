package com.wuyizhiye.cmct.phone.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.base.util.SystemConfig;
import com.wuyizhiye.base.voice.VoiceUtil;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.cmct.phone.model.PhoneCallRecorDing;
import com.wuyizhiye.cmct.phone.service.PhoneCallRecorDingService;
import com.wuyizhiye.framework.base.controller.ListController;
import com.wuyizhiye.framework.util.Constant;

/**
 * @ClassName PhoneCallRecorDingListController
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/phoneCrd/*")
public class PhoneCallRecorDingListController extends ListController {

	@Autowired
	private PhoneCallRecorDingService phoneCallRecorDingService;
	
	@Override
	protected CoreEntity createNewEntity() {
		return new PhoneCallRecorDing();
	}

	@Override
	protected String getListView() {
		SimpleDateFormat day=new SimpleDateFormat("yyyy/MM/dd");
		getRequest().setAttribute("startDay",day.format(new Date()).substring(0, 7)+"/01");
		getRequest().setAttribute("endDay",day.format(new Date()));
		return "cmct/phone/phoneCrdList";
	}

	@Override
	protected String getEditView() {
		return "cmct/phone/phoneCrdEdit";
	}

	@Override
	protected String getListMapper() {
		return PhoneCallRecorDing.mapper+".select";
	}

	@Override
	protected BaseService getService() {
		return phoneCallRecorDingService;
	}

	@Override
	protected Map<String, Object> getListDataParam() {
		Map<String,Object> param=super.getListDataParam();
		//跟进数据源判断查询SQL
		String dataBaseType = ParamUtils.getParamValue(Constant.DATABASETYPE);
		if(StringUtils.isEmpty(dataBaseType)){//默认为MYSQL
			dataBaseType = Constant.DATABASETYPE_MYSQL ;
		}else{
			dataBaseType = dataBaseType.toUpperCase() ;//转成大写
		}
		Object queryStartDateObj = param.get("queryStartDate");
		
		Object queryEndDateObj = param.get("queryEndDate");
		if(queryEndDateObj!=null && !StringUtils.isEmpty(queryEndDateObj.toString())){
			String queryEndDate = queryEndDateObj.toString().replace("/", "-");
			queryEndDateObj = DateUtil.convertDateToStr(DateUtil.getDateByDays(DateUtil.convertStrToDate(queryEndDate), -1)) ;
			queryEndDateObj = queryEndDateObj.toString().replace("-", "/");
		}
		
		if(Constant.DATABASETYPE_MYSQL.equals(dataBaseType)){
			if(queryStartDateObj!=null && !StringUtils.isEmpty(queryStartDateObj.toString())){
				param.put("queryStartDateMySql", queryStartDateObj);
			}
			if(queryEndDateObj!=null && !StringUtils.isEmpty(queryEndDateObj.toString())){
				param.put("queryEndDateMySql", queryEndDateObj);
			}
		}else if(Constant.DATABASETYPE_ORACLE.equals(dataBaseType)){
			if(queryStartDateObj!=null && !StringUtils.isEmpty(queryStartDateObj.toString())){
				param.put("queryStartDateOracle", queryStartDateObj);
			}
			if(queryEndDateObj!=null && !StringUtils.isEmpty(queryEndDateObj.toString())){
				param.put("queryEndDateOracle", queryEndDateObj);
			}
		}else if(Constant.DATABASETYPE_SQLSERVER.equals(dataBaseType)){
			
		}
		if(null!=queryStartDateObj && !StringUtils.isEmpty(queryStartDateObj.toString())){
			String dateStr=queryStartDateObj.toString().replace("/", "_");
			param.put("suffix", "_"+dateStr.substring(0, 7));
		}
		return param;
	}
	
	/**
	 * 音频播放
	 */
	@RequestMapping(value="playCrd")
	public void playCrd(HttpServletResponse response){
		String callBillId=getString("callBillId");
		if(!StringUtils.isEmpty(callBillId)){
			Map<String, Object>param=new HashMap<String, Object>();
			param.put("sessionsId", callBillId);
			List<PhoneCallRecorDing> pcrds = queryExecutor.execQuery(PhoneCallRecorDing.mapper+".select", param, PhoneCallRecorDing.class);
			if(null!=pcrds && pcrds.size()>0){
				PhoneCallRecorDing pcr=pcrds.get(0);
				if(!StringUtils.isEmpty(pcr.getCallRdUrl())){//已经转存过了
					getOutputMsg().put("STATE", "SUCCESS");
					getOutputMsg().put("MSG", pcr.getCallRdUrl());
				}else{//没转存的,先转存至本地
					String date=DateUtil.convertDateToStr(new Date(),DateUtil.GENERAL_FORMATTER);
					String year=date.substring(0, 4);
					String month=date.substring(5, 7);
					File voiceFile = new File(SystemConfig.getParameter("voice_path"));
					
					if(!voiceFile.exists() && !voiceFile.isDirectory()){
						voiceFile.mkdirs();
					}
					
					File yearFile = new File(voiceFile.getPath() +voiceFile.separator + year);
					if(!yearFile.exists() && !yearFile.isDirectory()){
						yearFile.mkdirs();
					}
					
					File monthFile = new File(yearFile.getPath() + yearFile.separator + month);
					if(!monthFile.exists() && !monthFile.isDirectory()){
						monthFile.mkdirs();
					}
					if(!StringUtils.isEmpty(pcr.getSessionId())){
						String fileName=pcr.getSessionId()+".wav";
						try {
							VoiceUtil.copy(pcr.getSlowRdUrl(), monthFile.getPath()+monthFile.separator+fileName);
							pcr.setDownStatus("YES");//已下载
							pcr.setCallRdUrl("/voice/"+year+"/"+month+"/"+fileName);	
							this.phoneCallRecorDingService.updateEntity(pcr);
							getOutputMsg().put("STATE", "SUCCESS");
							getOutputMsg().put("MSG", pcr.getCallRdUrl());
						} catch (Exception e) {
							getOutputMsg().put("STATE", "FAIL");
							getOutputMsg().put("MSG", e.getMessage());
							e.printStackTrace();
						}
					}else{
						getOutputMsg().put("STATE", "FAIL");
						getOutputMsg().put("MSG", "录音sessionId为空");
					}
				}
			}else{
				getOutputMsg().put("STATE", "FAIL");
				getOutputMsg().put("MSG", "没找到关联的录音数据");
			}
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "录音sessionId为空");
		}
		
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	/**
	 * 转存
	 */
	@RequestMapping(value="turnSave")
	public void turnSave(HttpServletResponse response){
		String id=getString("id");
		String year=getString("year");
		String month=getString("month");
		PhoneCallRecorDing pcr=this.phoneCallRecorDingService.getEntityById(id);
		
		File voiceFile = new File(SystemConfig.getParameter("voice_path"));
		
		if(!voiceFile.exists() && !voiceFile.isDirectory()){
			voiceFile.mkdirs();
		}
		
		File yearFile = new File(voiceFile.getPath() +voiceFile.separator + year);
		if(!yearFile.exists() && !yearFile.isDirectory()){
			yearFile.mkdirs();
		}
		
		File monthFile = new File(yearFile.getPath() + yearFile.separator + month);
		if(!monthFile.exists() && !monthFile.isDirectory()){
			monthFile.mkdirs();
		}
		if(!StringUtils.isEmpty(pcr.getSessionId())){
			String fileName=pcr.getSessionId()+".wav";
			try {
				VoiceUtil.copy(pcr.getSlowRdUrl(), monthFile.getPath()+monthFile.separator+fileName);
				pcr.setDownStatus("YES");//已下载
				pcr.setCallRdUrl("/voice/"+year+"/"+month+"/"+fileName);	
				this.phoneCallRecorDingService.updateEntity(pcr);
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "转存成功");
			} catch (Exception e) {
				getOutputMsg().put("STATE", "FAIL");
				getOutputMsg().put("MSG", e.getMessage());
				e.printStackTrace();
			}
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "sessionId为空");
		}
		
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	/**
	 *批量转存 
	 */
	@RequestMapping(value="batchTurnSave")
	public void batchTurnSave(HttpServletResponse response){
		String ids=getString("ids");
		if(!StringUtils.isEmpty(ids)){
			String []idsArr=ids.split(",");
			String year=getString("year");
			String month=getString("month");
			Map<String, Object>rest=this.phoneCallRecorDingService.batchTurnSave(idsArr, year, month);
			if(null!=rest && "SUCCESS".equals(rest.get("STATE"))){
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", rest.get("MSG"));
			}else{
				getOutputMsg().put("STATE", "FAIL");
				getOutputMsg().put("MSG", rest.get("MSG"));
			}
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "数据为空");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	
	/**
	 * json转换config
	 * @return
	 */
	
	@Override
	protected JsonConfig getDefaultJsonConfig(){
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof Date){
					return sdf.format(value);
				}
				return null;
			}
			
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof Date){
					return sdf.format(value);
				}
				return null;
			}
		});
		return config;
	}
}
