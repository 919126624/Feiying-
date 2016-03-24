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

import com.wuyizhiye.base.common.enums.DataTypeEnum;
import com.wuyizhiye.base.exceptions.BusinessException;
import com.wuyizhiye.base.exceptions.DownSignException;
import com.wuyizhiye.base.exceptions.PreBuyGoodsException;
import com.wuyizhiye.base.spring.ApplicationContextAware;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.apiCenter.model.APIItem;
import com.wuyizhiye.basedata.apiCenter.model.APIParameter;
import com.wuyizhiye.basedata.apiCenter.model.APIVisitLog;
import com.wuyizhiye.basedata.apiCenter.service.APIVisitLogService;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.framework.base.controller.BaseController;

/**
 * @ClassName APIController
 * @Description 接口中心列表controller
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
public class APIController extends BaseController {
	private static Logger logger=Logger.getLogger(APIController.class);
	@Autowired
	private APIVisitLogService aPIVisitLogService;
	
	
	@RequestMapping(value="api")
	public void api(HttpServletResponse response,@RequestParam(required=true,value="apiNumber")String apiNumber,
			@RequestParam(required = false ,value="getDataIdent",defaultValue="")String getDataIdent){
		Map<String,Object> rest=new HashMap<String, Object>();
		
		//如果allowGetData参数不传， 那么APP端将无法获取数据，这是为了让存在bug的老版本的用户无法使用
		if(!(ParamUtils.getStringValue("000000000000000000000000000000F","GETDATAIDENT")).equals(getDataIdent)){
			outPrint(response, JSONObject.fromObject(rest));
			return;
		}
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
					//判断参数设置中是否有非必填字段
					for(APIParameter p:paramList){
						if(p.getIsNotNull()==1){
							bl=true;
							break;
						}
					}
					//开始验证必填字段
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
							types[i]= DataTypeEnum.getClassByEnum( p.getType());
							//后台设置了非必传，但还是需要写上参数名，不太合理和方便，故做如下修改
							if(pVal==null){
								String typeName = types[i].getSimpleName(); 
								if(typeName.equals("String")){
									pVal = "";
								}else if(typeName.equals("int") || typeName.equals("Long") || typeName.equals("long") || typeName.equals("Integer") 
										|| typeName.equals("Short") || typeName.equals("short")){
									pVal = 0;
								}else if(typeName.equals("Double") || typeName.equals("double")){
									pVal = 0.0D;
								}else if(typeName.equals("Date")){
									pVal = new Date();
								}
							}
							values[i]=pVal;
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
					Method	method = c.getMethod(functionName, types);
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
			logger.error(ex);
		}catch(InvocationTargetException ex){
			ex.printStackTrace();
			//普通订单异常
			if(ex.getTargetException() instanceof DownSignException){
				String msg = ((DownSignException)ex.getTargetException()).getMessage();
				try{
					rest.put("resultStatu", Integer.parseInt(msg.substring(msg.length()-3, msg.length())));
					rest.put("resultMsg",msg.substring(0, msg.length()-3));
				}catch(Exception e){
					rest.put("resultStatu", "500");
					rest.put("resultMsg", "网络异常,请稍后重试");
				}
				rest.put("resultType", 0);
			//预售批量订单异常,注意, 代码中抛出的异常信息必须是符合json规则的字符串
			}else if(ex.getTargetException() instanceof PreBuyGoodsException){
				String msg = ((PreBuyGoodsException)ex.getTargetException()).getMessage();
				try{
					String errorMsg =  msg.substring(msg.indexOf("#PLACE#")+7, msg.lastIndexOf("#PLACE#"));
					rest.put("resultMsg", errorMsg);
				}catch(Exception e){
					rest.put("resultMsg", "您的商品列表不符合限购规则,系统已自动帮你调整,请继续操作");
				}
				rest.put("resultStatu", 100);
				rest.put("resultType", 0);
				rest.put("errorGoods", JSONArray.fromObject(msg));
			//业务异常
			}else if (ex.getTargetException() instanceof BusinessException){
				String msg = ((BusinessException)ex.getTargetException()).getMessage();
				rest.put("resultType", 0);
				rest.put("resultMsg", msg);
				logger.error("APiController:Exception:", ex);
			}else{
				rest.put("resultType", 0);
//				rest.put("resultMsg", ex.getTargetException() == null ? ex.getMessage() : ex.getTargetException().getMessage());
				rest.put("resultMsg", "网络异常,请稍后重试");
			}
			logger.error(ex);
		}catch(IllegalArgumentException ex){
			rest.put("resultType", 0);
			rest.put("resultMsg", ex.getMessage());
			logger.error("APiController:Exception:", ex);
		}catch(Exception ex){
			rest.put("resultType", 0);
//			rest.put("resultMsg", "接口调用失败，接口程序异常！"+ex.getMessage());
			rest.put("resultMsg", "网络异常,请稍后重试");
			logger.error("APiController:Exception:", ex);
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
					//打印日志
					logger.info("编码:"+apiNumber +"参数["+(paramBuilder.length() >= 2000 ? paramBuilder.substring(0, 2000).toString() : paramBuilder.toString())+"]"
							+"出现异常, 信息:" +errorMsg);
				}else{
					log.setSuccess(1);
				}
				log.setVisitTime(stime);
				log.setIp(getIpAddr(getRequest()));
				aPIVisitLogService.addEntity(log);
			}catch (Exception e) {
				logger.error("ApiController:", e);
			}
			outPrint(response, JSONObject.fromObject(rest));
		}
	}
	 
}
