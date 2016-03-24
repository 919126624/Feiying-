package com.wuyizhiye.basedata.apiCenter.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuyizhiye.base.common.enums.DataTypeEnum;
import com.wuyizhiye.base.exceptions.BusinessException;
import com.wuyizhiye.base.spring.ApplicationContextAware;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.apiCenter.model.APIItem;
import com.wuyizhiye.basedata.apiCenter.model.APIParameter;
import com.wuyizhiye.basedata.apiCenter.model.APIVisitLog;
import com.wuyizhiye.basedata.apiCenter.service.APIVisitLogService;
import com.wuyizhiye.framework.base.controller.BaseController;

/**
 * @ClassName APIController
 * @Description 接口中心列表controller ;作为ajax跨域请求的控制器
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
public class ApiAjaxController extends BaseController {
	private static Logger logger=Logger.getLogger(ApiAjaxController.class);
	
	@Autowired
	private APIVisitLogService aPIVisitLogService;
	
	@SuppressWarnings("finally")
	@RequestMapping(value="apiAjax",produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String api(HttpServletResponse response,
			@RequestParam(required=true,value="apiNumber")String apiNumber,
			@RequestParam(required=true,value="callback")String callback){
		Map<String,Object> rest=new HashMap<String, Object>();
		StringBuilder paramBuilder = new StringBuilder("");
		Date stime = new Date();
		try{
			Map<String,Object> param=new HashMap<String, Object>();
			param.put("number", apiNumber);
			APIItem apiItem = queryExecutor.execOneEntity("com.wuyizhiye.basedata.apiCenter.dao.APIItemDao.select", param,APIItem.class);
			if(apiItem!=null){
				String drive=apiItem.getApiUrl();
				String serviceName=drive.substring(0, drive.indexOf("."));
				String functionName=drive.substring(drive.indexOf(".")+1, drive.indexOf("("));
				Object bean = ApplicationContextAware.getApplicationContext().getBean(serviceName);
				Class c = bean.getClass();
				param.clear();
				param.put("itemId", apiItem.getId());
				List<APIParameter> paramList=queryExecutor.execQuery("com.wuyizhiye.basedata.apiCenter.dao.APIParameterDao.select", param, APIParameter.class);
				Class[] types = null;
				Object[] values = null;
				if(paramList!=null && paramList.size()>0){
					String paramStr=getString("params"); 
					boolean bl=false;
					for(APIParameter p:paramList){
						if(p.getIsNotNull()==1){
							bl=true;
							break;
						}
					}
					if(bl && StringUtils.isEmpty(paramStr)){
						rest.put("resultType", 2);
						rest.put("resultMsg", "接口调用失败，接口参数不能为空！");
						rest.put("resultData", "");
					}else{
						types=new Class[paramList.size()];
						values=new Object[paramList.size()];
						JSONObject json=JSONObject.fromObject(paramStr);
						String nullParam="";
						for(int i=0;i<paramList.size();i++){
							APIParameter p=paramList.get(i);
							Object pVal=json.get(p.getName());
							if((pVal == null || StringUtils.isEmpty(pVal.toString())) && p.getIsNotNull()==1){
								if(!StringUtils.isEmpty(nullParam)){  
									nullParam+=",";
								}
								nullParam+=p.getName();
							}
							if((pVal instanceof JSONArray) || (pVal instanceof JSONObject) ){
								pVal = pVal.toString();
							}
							values[i]=pVal;
							types[i]= DataTypeEnum.getClassByEnum( p.getType());
							paramBuilder.append(p.getName()).append("=").append(pVal).append("&");
						}
						if(!StringUtils.isEmpty(nullParam)){
							rest.put("resultData", "");
							rest.put("resultType", 2);
							rest.put("resultMsg", "接口调用失败，["+nullParam+"]参数不能为空！");
						}else{//调用接口
							 @SuppressWarnings("unchecked")
							Method method = c.getMethod(functionName, types); 
							Object resultData=method.invoke(bean,values);
						    
							rest.put("resultData", resultData);
						    rest.put("resultType", 1);
							rest.put("resultMsg", "接口调用成功！");
						}
					}
				}else{
					Method method = c.getMethod(functionName, types); 
					Object resultData=method.invoke(bean,values);
					rest.put("resultData", resultData);
				    rest.put("resultType", 1);
					rest.put("resultMsg", "接口调用成功！");
				}
				
			}else{
				rest.put("resultType", -1);
				rest.put("resultMsg", "接口调用失败，未找到对应编码接口！");
			}
			
		}catch(BusinessException ex){
			rest.put("resultType", 0);
			rest.put("resultMsg", ex.getMessage());
			ex.printStackTrace();
		}catch(InvocationTargetException ex){
			rest.put("resultType", 0);
			rest.put("resultMsg", ex.getTargetException() == null ? ex.getMessage() : ex.getTargetException().getMessage());
			ex.printStackTrace();
		}catch(IllegalArgumentException ex){
			rest.put("resultType", 0);
			rest.put("resultMsg", ex.getMessage());
			ex.printStackTrace();
		}catch(Exception ex){
			rest.put("resultType", 0);
			rest.put("resultMsg", "接口调用失败，接口程序异常！"+ex.getMessage());
			ex.printStackTrace();
		}finally{
			//记录访问日志
			try{
				if(paramBuilder.length() > 0){
					paramBuilder.delete(paramBuilder.length() - 1, paramBuilder.length());
				}
				Date etime = new Date();
				APIVisitLog log = new APIVisitLog();
				log.setNumber(apiNumber);
				log.setParams(paramBuilder.length() >= 2000 ? paramBuilder.substring(0, 2000).toString() : paramBuilder.toString());
				log.setPort(getRequest().getServerPort());
				log.setCostTime(etime.getTime() - stime.getTime());
				log.setSuccess((Integer)rest.get("resultType"));
				if(log.getSuccess() != 1){
					log.setSuccess(0);
					String errorMsg = rest.get("resultMsg") == null ? "" : rest.get("resultMsg").toString();
					log.setErrorMsg(errorMsg.length() >=500 ? errorMsg.substring(0, 500) : errorMsg);
				}else{
					log.setSuccess(1);
				}
				log.setVisitTime(stime);
				log.setIp(getIpAddr(getRequest()));
				aPIVisitLogService.addEntity(log);
			}catch (Exception e) {
				logger.error("", e);
			}
			return new StringBuilder(callback).append("(")
				.append(JSONObject.fromObject(rest).toString())
				.append(")").toString();
		}
	}
	 
}

