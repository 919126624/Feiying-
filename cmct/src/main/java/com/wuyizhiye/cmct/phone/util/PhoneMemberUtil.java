package com.wuyizhiye.cmct.phone.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.wuyizhiye.base.common.HttpsVerifyManager;
import com.wuyizhiye.base.dao.impl.QueryExecutorImpl;
import com.wuyizhiye.base.exceptions.BusinessException;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.enums.CommonFlagEnum;
import com.wuyizhiye.cmct.phone.model.PhoneConfig;
import com.wuyizhiye.cmct.phone.model.PhoneMember;

/**
 * @ClassName PhoneMemberUtil
 * @Description 话务成员工具类
 * @author li.biao
 * @date 2015-5-26
 */
public class PhoneMemberUtil {
	
	/**
	 * 系统参数：话伴分配的orgId
	 */
	//7593
	public static final String CALL_ORGID = "CALL_ORGID" ;
	
	/**
	 * 系统参数：话伴分配的orgName
	 */
	//测试组织账号
	public static final String CALL_ORGNAME = "CALL_ORGNAME" ;
	
	/**
	 * 系统参数：话伴分配的orgKey
	 */
	//8989ee590264d1aad920fe2169032c37
	public static final String CALL_ORGKEY = "CALL_ORGKEY" ;
	
	/**
	 * 系统参数：新增话伴成员接口URL
	 */
	//public static final String CALL_URL_ADDMENBER = "http://api.33e9.net/client/addMember.do" ;
	//http://183.62.152.139:8093/cp-website/client/addMember.do
	public static final String CALL_URL_ADDMENBER = "CALL_URL_ADD" ;
	
	/**
	 * 系统参数：修改话伴成员接口URL
	 */
	//public static final String CALL_URL_EDITMENBER = "http://api.33e9.net/client/editMember.do" ;
	//http://183.62.152.139:8093/cp-website/client/editMember.do
	public static final String CALL_URL_EDITMENBER = "CALL_URL_EDIT" ;
	
	/**
	 * 系统参数：删除话伴成员接口URL
	 */
	//public static final String CALL_URL_DELETEMENBER = "http://api.33e9.net/client/deleteMember.do" ;
	//http://183.62.152.139:8093/cp-website/client/deleteMember.do
	public static final String CALL_URL_DELETEMENBER = "CALL_URL_DELETE" ;
	
	/**
	 * 系统参数：获取渠道商固话列表
	 */
	//public static final String CALL_URL_GETNUMBERLIST = "https://api.33e9.net/client/getnumber.do" ;
	public static final String CALL_URL_GETNUMBERLIST = "CALL_URL_GETNUMBER" ;
	
	/**
	 * 系统参数：查询账户余额
	 */
	public static final String CALL_URL_QUERYACCOUNT = "CALL_URL_QUERYACCOUNT" ;
	
	/**
	 * 系统参数：查询组织话单
	 */
	public static final String CALL_URL_GETCALLLIST = "CALL_URL_GETCALLLIST" ;
	
	/**
	 * 操作类别：添加
	 */
	private static final String OP_TYPE_ADD = "add" ;
	
	/**
	 * 操作类别：编辑修改
	 */
	private static final String OP_TYPE_EDIT = "edit" ;
	
	/**
	 * 操作类别：删除
	 */
	private static final String OP_TYPE_DELETE = "delete" ;
	
	/**
	 * 分配:
	 */
	public  static final String OP_TYPE_ALLOT = "allot" ;
	
	
	/**
	 * 请求路径协议：HTTP
	 */
	private static final String URL_PROTOCOL_HTTP = "HTTP" ;
	
	/**
	 * 请求路径协议：HTTPS
	 */
	private static final String URL_PROTOCOL_HTTPS = "HTTPS" ;
	
	/**
	 * 系统参数 ：是否显示话单明细里面的对方号码
	 */
	public static final String CMCT_DIALDETAIL = "CMCT_DIALDETAIL" ;
	
	/**
	 *系统参数: 未分配线路时候传过去的customerId;
	 */
	public static final String CMCT_CUSOMETID = "CMCT_CUSOMETID" ;
	
	/**
	 * 系统参数,是否录音,默认不录音
	 */
	public static final String CMCT_PHONERECORD = "CMCT_PHONERECORD" ;
	
	/**
	 * 系统参数:是否显示话单明细及同步功能
	 */
	public static final String CMCT_DISPLAY_DCB = "CMCT_DISPLAY_DCB" ;
	
	/**
	 * 号码的使用方式
	 */
	public static final String CMCT_USE_PHONE = "CMCT_USE_PHONE" ;
	
	
	/**
	 * 系统参数,51置业的客服接听号码 
	 */
	public static final String CMCT_WYZY_PHONENUM = "CMCT_WYZY_PHONENUM" ;
	
	public static final String TIP_KEY_STATE = "STATE" ;
	public static final String TIP_KEY_MSG = "MSG" ;
	public static final String TIP_KEY_SUCC = "SUCCESS" ;
	public static final String TIP_KEY_FAIL= "FAIL" ;
	
	/**
	 * 添加单个话伴成员
	 * @param cp
	 * @return 状态标识STATE=SUCCESS（成功）=FAIL（失败），错误信息MSG
	 */
	public static Map<String,Object> addMenber(PhoneMember cp){
		Map<String,Object> msgRst = new HashMap<String,Object>();
		try {
			//构建请求URL
			String reqUrl = buildRequestUrl(cp, OP_TYPE_ADD);
			
			//调用
			Map<String,Object> result = null ;
			URL url = new URL(reqUrl);
			if(URL_PROTOCOL_HTTP.equals(url.getProtocol().toUpperCase())){//http方式请求
				result = callUrlAndGetResult(reqUrl);
			}else if(URL_PROTOCOL_HTTPS.equals(url.getProtocol().toUpperCase())){//https方式请求
				result = callHttpsUrlAndGetResult(reqUrl);
			}else{
				msgRst.put("STATE", "FAIL");
				msgRst.put("MSG","所配置的请求协议方式不正确");
			}
			if(result.get("STATE")!=null && "SUCCESS".equals(result.get("STATE"))){
				JSONArray jArray = JSONArray.fromObject(result.get("MSG"));
				JSONObject jObject = jArray.getJSONObject(0);
				String errorKey = jObject.getString("responseKey");
				if("00".equals(errorKey)){
					msgRst.put("STATE", "SUCCESS");
					msgRst.put("userId", jObject.get("userId"));
					msgRst.put("loginNumber", jObject.get("mobileno"));
					msgRst.put("password", jObject.get("clientKey"));
				}else {
					msgRst.put("STATE", "FAIL");
					msgRst.put("MSG", getMsgByErrorKey(OP_TYPE_ADD, errorKey));
				}
			}else{
				msgRst = result ;
			}
		} catch (Exception e) {
			e.printStackTrace();
			msgRst.put("STATE", "EXCEPTION");
			msgRst.put("MSG",e.getMessage());
		}
		return msgRst ;
	}
	
	/**
	 * 修改话伴成员
	 * @param cp
	 * @return 状态标识STATE=SUCCESS（成功）=FAIL（失败），错误信息MSG
	 */
	public static Map<String,Object> editMenber(PhoneMember cp){
		Map<String,Object> msgRst = new HashMap<String,Object>();
		try{
			//构建请求URL
			String reqUrl = buildRequestUrl(cp, OP_TYPE_EDIT);
			//调用
			Map<String,Object> result = null ;
			URL url = new URL(reqUrl);
			if(URL_PROTOCOL_HTTP.equals(url.getProtocol().toUpperCase())){//http方式请求
				result = callUrlAndGetResult(reqUrl);
			}else if(URL_PROTOCOL_HTTPS.equals(url.getProtocol().toUpperCase())){//https方式请求
				result = callHttpsUrlAndGetResult(reqUrl);
			}else{
				msgRst.put("STATE", "FAIL");
				msgRst.put("MSG","所配置的请求协议方式不正确");
			}
			if(result.get("STATE")!=null && "SUCCESS".equals(result.get("STATE"))){
				JSONObject jObject = JSONObject.fromObject(result.get("MSG"));
				String errorKey = jObject.getString("responseKey");
				if("00".equals(errorKey)){
					msgRst.put("STATE", "SUCCESS");
					msgRst.put("userId", jObject.get("userId"));
					msgRst.put("loginNumber", jObject.get("mobileno"));
					msgRst.put("password", jObject.get("clientKey"));
				}else {
					msgRst.put("STATE", "FAIL");
					msgRst.put("MSG", getMsgByErrorKey(OP_TYPE_EDIT, errorKey));
				}
			}else{
				msgRst = result ;
			}
		} catch (Exception e) {
			e.printStackTrace();
			msgRst.put("STATE", "EXCEPTION");
			msgRst.put("MSG",e.getMessage());
		}
		return msgRst ;
	}
	
	/**
	 * 删除话伴成员
	 * @param cp
	 * @return 状态标识STATE=SUCCESS（成功）=FAIL（失败），错误信息MSG
	 */
	public static Map<String,Object> deleteMenber(PhoneMember cp){
		Map<String,Object> msgRst = new HashMap<String,Object>();
		try{
			//构建请求URL
			String reqUrl = buildRequestUrl(cp, OP_TYPE_DELETE);
			//调用
			Map<String,Object> result = null ;
			URL url = new URL(reqUrl);
			if(URL_PROTOCOL_HTTP.equals(url.getProtocol().toUpperCase())){//http方式请求
				result = callUrlAndGetResult(reqUrl);
			}else if(URL_PROTOCOL_HTTPS.equals(url.getProtocol().toUpperCase())){//https方式请求
				result = callHttpsUrlAndGetResult(reqUrl);
			}else{
				msgRst.put("STATE", "FAIL");
				msgRst.put("MSG","所配置的请求协议方式不正确");
			}
			if(result.get("STATE")!=null && "SUCCESS".equals(result.get("STATE"))){
				JSONObject jObject = JSONObject.fromObject(result.get("MSG"));
				String errorKey = jObject.getString("responseKey");
				if("00".equals(errorKey)){
					msgRst.put("STATE", "SUCCESS");
					msgRst.put("userId", jObject.get("userId"));
					msgRst.put("loginNumber", jObject.get("mobileno"));
					msgRst.put("password", jObject.get("clientKey"));
				}else {
					msgRst.put("STATE", "FAIL");
					msgRst.put("MSG", getMsgByErrorKey(OP_TYPE_DELETE, errorKey));
				}
			}else{
				msgRst = result ;
			}
		} catch (Exception e) {
			e.printStackTrace();
			msgRst.put("STATE", "EXCEPTION");
			msgRst.put("MSG",e.getMessage());
		}
		return msgRst ;
	}
	
	/**
	 * 获取渠道商固话列表
	 * @param orgInterfaceId 核算渠道ID
	 * @param pageNo 当前页
	 * @param numberStatus 状态   先查2，如果得到空，则查 5，1：在使用（已购买已分配） 2：在使用（已购买未分配） 
	 * 3．欠费停用（已购买） 4：组织所有号码（已购买的所有号码） 5：在售（所属渠道商在售的号码）
	 * ************************************修改历史===
	 * @update by li.biao since 2014-2-19 修改只查找在售（5）状态
	 * @return STATE：查询状态；numberList：号码列表； pageNo: 返回结果的当前页 ；pageTotal:总页数
	 */
	public static Map<String,Object> getNumberList(Map<String,Object> param) throws Exception{
		Map<String,Object> msgRst = new HashMap<String,Object>();
		String orgInterfaceId = (String) param.get("orgInterfaceId");
		int pageNo = param.get("pageNo") == null ? 1 : (Integer)param.get("pageNo") ;
		int numberstatus = param.get("numberStatus") == null ? 5 : (Integer)param.get("numberStatus") ;
		
		//构建URL
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(getPhoneConfigParam(orgInterfaceId,CALL_URL_GETNUMBERLIST));
		urlBuilder.append("?");
		urlBuilder.append("orgId=").append(getPhoneConfigParam(orgInterfaceId,CALL_ORGID));
		urlBuilder.append("&").append("orgKey=").append(getPhoneConfigParam(orgInterfaceId,CALL_ORGKEY));
		urlBuilder.append("&").append("Numberstatus=").append(numberstatus);
		urlBuilder.append("&").append("pageNo=").append(pageNo);
		
		String reqUrl = urlBuilder.toString();
		
		//调用
		Map<String,Object> result = null ;
		URL url = new URL(reqUrl);
		if(URL_PROTOCOL_HTTP.equals(url.getProtocol().toUpperCase())){//http方式请求
			result = callUrlAndGetResult(reqUrl);
		}else if(URL_PROTOCOL_HTTPS.equals(url.getProtocol().toUpperCase())){//https方式请求
			result = callHttpsUrlAndGetResult(reqUrl);
		}else{
			msgRst.put("STATE", "FAIL");
			msgRst.put("MSG","所配置的请求协议方式不正确");
			return msgRst ;
		}
		if(result.get("STATE")!=null && "SUCCESS".equals(result.get("STATE"))){
			JSONArray jArray = JSONArray.fromObject(result.get("MSG"));
			JSONObject jObject = jArray.getJSONObject(0);
			JSONArray numberArr = JSONArray.fromObject(jObject.get("numberList"));
			if((numberArr == null || numberArr.size() == 0) && numberstatus == 2){
				param.put("numberStatus", 5);
				//msgRst = getNumberList(param);
			}else{
				msgRst.put("numberList",  numberArr.toString());
				msgRst.put("pageNo", jObject.get("pageNo"));
				int pageTotal = jObject.get("pageTotal") == null ? 1 : Integer.valueOf(jObject.get("pageTotal").toString());
				int totalNum = jObject.get("totalNum") == null ? 30 : Integer.valueOf(jObject.get("totalNum").toString());
				msgRst.put("recordTotal", pageTotal * totalNum);
				msgRst.put("STATE", "SUCCESS");
			}
		}else{
			msgRst = result ;
		}
		return msgRst ;
	}
	
	/**
	 * 查询账户余额
	 * @param param orgId 核算组织ID
	 * @return STATE 查询标识（SUCCESS成功，FAIL失败）；accountMoney 余额（单位为厘）
	 * @throws Exception
	 */
	public static Map<String,Object> queryAccount(Map<String,Object> param) throws Exception{
		Map<String,Object> msgRst = new HashMap<String,Object>();
		String orgId = (String) param.get("orgInterfaceId");

		//构建URL
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(param.get("accountUrl"));
		urlBuilder.append("?");
		urlBuilder.append("orgId=").append(orgId);
		urlBuilder.append("&").append("orgKey=").append(param.get("orgKey"));
		
		String reqUrl = urlBuilder.toString();
		
		//调用
		Map<String,Object> result = null ;
		URL url = new URL(reqUrl);
		if(URL_PROTOCOL_HTTP.equals(url.getProtocol().toUpperCase())){//http方式请求
			result = callUrlAndGetResult(reqUrl);
		}else if(URL_PROTOCOL_HTTPS.equals(url.getProtocol().toUpperCase())){//https方式请求
			result = callHttpsUrlAndGetResult(reqUrl);
		}else{
			msgRst.put("STATE", "FAIL");
			msgRst.put("MSG","所配置的请求协议方式不正确");
			return msgRst ;
		}
		if(result.get("STATE")!=null && "SUCCESS".equals(result.get("STATE"))){
			JSONArray jArray = JSONArray.fromObject(result.get("MSG"));
			JSONObject jObject = jArray.getJSONObject(0);
			msgRst.put("accountMoney",jObject.get("accountMoney"));
			msgRst.put("STATE", "SUCCESS");
		}else{
			msgRst = result ;
		}
		return msgRst ;
	}
	

	/**
	 * 获取指定组织的具体话单信息（获取全部）
	 * @param orgInterfaceId 指定组织
	 * @param sTime 同步开始时间
	 * @param eTime 同步结束时间
	 * @param pageData 用于分页递归存放数据，传null
	 * @return STATE:SUCCESS/FAIL状态：成功/失败 ；MSG：失败信息；list：数据集，用Map存放的对象，key是当前页，值是当前页的json数据
	 *  pageNo：当前页；pageCount：总页数；
	 *  list中json数据对象：billcallid 计费ID，call_id 呼叫id，call_times 呼叫耗时，start_time 呼叫开始时间，
	 *  end_time 呼叫结束时间，fees 费用，price 费率，service_Name 呼叫类型 ，show_Phone 去电号码，user_Phone 对方号码
	 * @throws Exception
	 */
	public static Map<String,Object> getCallList(Map<String,Object> param ,Map<String,Object> pageData) throws Exception{
		Map<String,Object> msgRst = new HashMap<String,Object>();
		String orgInterfaceId = (String) param.get("syncOrgId");
		int pageNo = param.get("pageNo") == null ? 1 : (Integer)param.get("pageNo") ;
		int pageSize = 1000 ;
		int resulttype = 0 ;
		String sTime =  param.get("sTime") == null ? null : (String)param.get("sTime");
		String eTime =  param.get("eTime") == null ? null : (String)param.get("eTime");
		
		//构建URL
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(param.get("getCallUrl").toString());
		urlBuilder.append("?");
		urlBuilder.append("orgId=").append(orgInterfaceId);
		urlBuilder.append("&").append("orgKey=").append(param.get("syncOrgKey").toString());
		urlBuilder.append("&").append("sTime=").append(sTime);
		urlBuilder.append("&").append("eTime=").append(eTime);
		urlBuilder.append("&").append("pageNo=").append(pageNo);
		urlBuilder.append("&").append("pageSize=").append(pageSize);
		urlBuilder.append("&").append("resulttype=").append(resulttype);
		
		String reqUrl = urlBuilder.toString();
		
		//调用
		Map<String,Object> result = null ;
		URL url = new URL(reqUrl);
		if(URL_PROTOCOL_HTTP.equals(url.getProtocol().toUpperCase())){//http方式请求
			result = callUrlAndGetResult(reqUrl);
		}else if(URL_PROTOCOL_HTTPS.equals(url.getProtocol().toUpperCase())){//https方式请求
			result = callHttpsUrlAndGetResult(reqUrl);
		}else{
			msgRst.put("STATE", "FAIL");
			msgRst.put("MSG","所配置的请求协议方式不正确");
			return msgRst ;
		}
		if(result.get("STATE")!=null && "SUCCESS".equals(result.get("STATE"))){
			JSONObject jObject = JSONObject.fromObject(result.get("MSG"));
			String reponseKey = jObject.getString("responseKey");
			if("00".equals(reponseKey)){
				if(pageData == null){
					pageData = new HashMap<String,Object>();
				}
				String listObj = jObject.getString("list");
				String curPage = jObject.getString("curPage");
				String countPage = jObject.getString("countPage");
				int pageIndex = Integer.valueOf(curPage);
				int pageCount = Integer.valueOf(countPage);
				pageData.put(curPage, listObj);
				if(pageCount > pageIndex){
					param.put("pageNo", pageIndex +1 );//查找下一页
					getCallList(param,pageData);
				}
				msgRst.put("list", pageData);
				msgRst.put("pageNo", curPage);
				msgRst.put("pageCount", countPage);
				msgRst.put("STATE", "SUCCESS");
			}else{
				msgRst.put("list", "[]");
				msgRst.put("pageNo", pageNo);
				msgRst.put("pageCount", 0);
				msgRst.put("STATE", "FAIL");
				msgRst.put("MSG", getMsgByErrorKey(CALL_URL_GETCALLLIST, reponseKey));
			}
		}else{
			msgRst = result ;
		}
		return msgRst ;
	}
	
	/**
	 * 获取指定组织的具体话单信息（单页获取）
	 * @param orgInterfaceId 指定组织
	 * @param sTime 同步开始时间
	 * @param eTime 同步结束时间
	 * @return STATE:SUCCESS/FAIL状态：成功/失败 ；MSG：失败信息；list：数据集，用Map存放的对象，key是当前页，值是当前页的json数据
	 *  pageNo：当前页；pageCount：总页数；
	 *  list中json数据对象：billcallid 计费ID，call_id 呼叫id，call_times 呼叫耗时，start_time 呼叫开始时间，
	 *  end_time 呼叫结束时间，fees 费用，price 费率，service_Name 呼叫类型 ，show_Phone 去电号码，user_Phone 对方号码
	 * @throws Exception
	 */
	public static Map<String,Object> getCallListEachPage(Map<String,Object> param) throws Exception{
		Map<String,Object> msgRst = new HashMap<String,Object>();
		String orgInterfaceId = (String) param.get("orgInterfaceId");
		int pageNo = param.get("pageNo") == null ? 1 : (Integer)param.get("pageNo") ;
		int pageSize = 1000 ;
		int resulttype = 0 ;
		String sTime =  param.get("sTime") == null ? null : (String)param.get("sTime");
		String eTime =  param.get("eTime") == null ? null : (String)param.get("eTime");
		
		//构建URL
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(getPhoneConfigParam(orgInterfaceId,CALL_URL_GETCALLLIST));
		urlBuilder.append("?");
		urlBuilder.append("orgId=").append(getPhoneConfigParam(orgInterfaceId,CALL_ORGID));
		urlBuilder.append("&").append("orgKey=").append(getPhoneConfigParam(orgInterfaceId,CALL_ORGKEY));
		urlBuilder.append("&").append("sTime=").append(sTime);
		urlBuilder.append("&").append("eTime=").append(eTime);
		urlBuilder.append("&").append("pageNo=").append(pageNo);
		urlBuilder.append("&").append("pageSize=").append(pageSize);
		urlBuilder.append("&").append("resulttype=").append(resulttype);
		
		String reqUrl = urlBuilder.toString();
		
		//调用
		Map<String,Object> result = null ;
		URL url = new URL(reqUrl);
		if(URL_PROTOCOL_HTTP.equals(url.getProtocol().toUpperCase())){//http方式请求
			result = callUrlAndGetResult(reqUrl);
		}else if(URL_PROTOCOL_HTTPS.equals(url.getProtocol().toUpperCase())){//https方式请求
			result = callHttpsUrlAndGetResult(reqUrl);
		}else{
			msgRst.put("STATE", "FAIL");
			msgRst.put("MSG","所配置的请求协议方式不正确");
			return msgRst ;
		}
		if(result.get("STATE")!=null && "SUCCESS".equals(result.get("STATE"))){
			JSONObject jObject = JSONObject.fromObject(result.get("MSG"));
			String reponseKey = jObject.getString("responseKey");
			if("00".equals(reponseKey)){
				String listObj = jObject.getString("list");
				String curPage = jObject.getString("curPage");
				String countPage = jObject.getString("countPage");
				msgRst.put("list", listObj);
				msgRst.put("pageNo", curPage);
				msgRst.put("pageCount", countPage);
				msgRst.put("STATE", "SUCCESS");
			}else{
				msgRst.put("list", "[]");
				msgRst.put("pageNo", pageNo);
				msgRst.put("pageCount", 0);
				msgRst.put("STATE", "FAIL");
				msgRst.put("MSG", getMsgByErrorKey(CALL_URL_GETCALLLIST, reponseKey));
			}
		}else{
			msgRst = result ;
		}
		return msgRst ;
	}
	
	
	/**
	 * 构建请求URL
	 * @param cp
	 * @param type
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String buildRequestUrl(PhoneMember cp ,String type) throws Exception{
		
		StringBuilder urlBuilder = new StringBuilder();
		
		//读取操作URL；后续要配置到系统参数里面，读取系统参数
		if(OP_TYPE_ADD.equals(type)){
			urlBuilder.append(getPhoneConfigParam(cp.getOrgInterfaceId(),CALL_URL_ADDMENBER));
		}else if(OP_TYPE_EDIT.equals(type)){
			urlBuilder.append(getPhoneConfigParam(cp.getOrgInterfaceId(),CALL_URL_EDITMENBER));
		}else if(OP_TYPE_DELETE.equals(type)){
			urlBuilder.append(getPhoneConfigParam(cp.getOrgInterfaceId(),CALL_URL_DELETEMENBER));
		}
		urlBuilder.append("?");
		urlBuilder.append("orgId=").append(getPhoneConfigParam(cp.getOrgInterfaceId(),CALL_ORGID));
		urlBuilder.append("&").append("orgKey=").append(getPhoneConfigParam(cp.getOrgInterfaceId(),CALL_ORGKEY));
		
		//构建URL参数
		if(OP_TYPE_ADD.equals(type)){
			StringBuilder userBuilder = new StringBuilder();
			//userBuilder.append(cp.getObjectName()).append("+");
			userBuilder.append(cp.getAlias() == null ? "" : cp.getAlias()).append("+");//取别名传送，作为线路名称
			userBuilder.append(phoneNumberConvert(cp.getLoginNumber())) ;
			if(!StringUtils.isEmpty(cp.getAnswerPhone())){
				userBuilder.append("+").append(phoneNumberConvert(cp.getAnswerPhone()));
			}else{
				userBuilder.append("+");
			}
			if(!StringUtils.isEmpty(cp.getShowPhone())){
				userBuilder.append("+").append(phoneNumberConvert(cp.getShowPhone()));
			}else{
				userBuilder.append("+");
			}
			if(CommonFlagEnum.YES.equals(cp.getIsAllot())){
				userBuilder.append("+true");//是否分配话伴：分配
			}else{
				userBuilder.append("+false");//是否分配话伴：不分配
			}
			urlBuilder.append("&").append("user=").append(URLEncoder.encode(userBuilder.toString(),"UTF-8"));
		}else if(OP_TYPE_EDIT.equals(type)){
			StringBuilder editBuilder = new StringBuilder();
			editBuilder.append("&").append("userId=").append(cp.getUserId());
			//editBuilder.append("&").append("name=").append(URLEncoder.encode(cp.getObjectName(),"UTF-8"));
			editBuilder.append("&").append("name=").append(cp.getAlias() == null ? "" : URLEncoder.encode(cp.getAlias(),"UTF-8"));//取别名传送，作为线路名称
			if(cp.getNewPhone()!=null && !cp.getLoginNumber().equals(cp.getNewPhone())){
				editBuilder.append("&").append("newPhone=").append(phoneNumberConvert(cp.getNewPhone()));
			}
			if(!StringUtils.isEmpty(cp.getAnswerPhone())){
				editBuilder.append("&").append("answerPhone=").append(phoneNumberConvert(cp.getAnswerPhone()));
			}
			if(!StringUtils.isEmpty(cp.getShowPhone())){
				editBuilder.append("&").append("showPhone=").append(phoneNumberConvert(cp.getShowPhone()));
			}
			urlBuilder.append(editBuilder.toString());
		}else if(OP_TYPE_DELETE.equals(type)){
			urlBuilder.append("&").append("userId=").append(cp.getUserId());
			urlBuilder.append("&").append("mobileno=");
			if(CommonFlagEnum.YES.equals(cp.getIsCallBank())){//是否回收电话号码
				urlBuilder.append("&").append("isCallBack=true");
			}else{
				urlBuilder.append("&").append("isCallBack=false");
			}
		}
		return urlBuilder.toString() ;
	}
	
	public static Map<String, Object> dialPhone(PhoneMember pm){
		Map<String,Object> msgRst = new HashMap<String,Object>();
		if(StringUtils.isEmpty(pm.getOrgInterfaceId())){
			msgRst.put(TIP_KEY_STATE, TIP_KEY_FAIL);
			msgRst.put(TIP_KEY_MSG, "核算渠道为空");
		}
		if(StringUtils.isEmpty(pm.getUserId())){
			msgRst.put(TIP_KEY_STATE, TIP_KEY_FAIL);
			msgRst.put(TIP_KEY_MSG, "成员为空");
		}
		Map<String,Object> resultMap=callHttpsUrlAndGetResult("https://api.33e9.net/visitor/visitcall.do?orgid="+pm.getOrgInterfaceId()+"&orgkey="+pm.getPassWd()+"&BillCallID="+pm.getSessionId()+"&PhoneNumber=86"+pm.getBillNumber()+"&OutPhoneNumber="+phoneNumberConvert(pm.getShowPhone())+"&userid="+pm.getUserId());
		if(resultMap.get("STATE")!=null && "SUCCESS".equals(resultMap.get("STATE"))){
			JSONObject jsonObj=JSONObject.fromObject(resultMap.get("MSG"));
			if("000000".equals(jsonObj.getString("returnCode"))){
				msgRst.put(TIP_KEY_STATE, TIP_KEY_SUCC);
				msgRst.put("returnData", jsonObj.getString("returnData"));
				msgRst.put("returnDesc", jsonObj.getString("returnDesc"));
			}else{
				msgRst.put(TIP_KEY_STATE, TIP_KEY_FAIL);
				if("000002".equals(jsonObj.getString("returnCode"))){
					msgRst.put(TIP_KEY_MSG, "参数错误");
				}else if("000099".equals(jsonObj.getString("returnCode"))){
					msgRst.put(TIP_KEY_MSG, "拨打失败");
				}else{
					msgRst = resultMap ;
				}
			}
		}else{
			msgRst = resultMap ;
		}
		return msgRst;
	}
	
	/**
	 * 查找话务参数系统配置
	 * @param orgId 核算渠道
	 * @param code 参数代码
	 * @return
	 * @throws Exception
	 */
	public static String getPhoneConfigParam(String orgId,String code) throws Exception{
		String val = "";
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("orgId", orgId);
		param.put("isEnable", CommonFlagEnum.YES);
		List<PhoneConfig> configList = QueryExecutorImpl.getInstance().execQuery(PhoneConfig.MAPPER+".select", param, PhoneConfig.class);
		if(configList !=null && configList.size() > 0 ){
			PhoneConfig config = configList.get(0);
			if(CALL_URL_ADDMENBER.equals(code)){
				val = config.getAddUrl();
			}else if(CALL_URL_EDITMENBER.equals(code)){
				val = config.getModifyUrl() ;
			}else if(CALL_URL_DELETEMENBER.equals(code)){
				val = config.getDeleteUrl();
			}else if(CALL_URL_GETNUMBERLIST.equals(code)){
				val = config.getGetNumberUrl();
			}else if(CALL_URL_QUERYACCOUNT.equals(code)){
				val = config.getQueryAccountUrl() ;
			}else if(CALL_URL_GETCALLLIST.equals(code)){
				val = config.getGetCallUrl() ;
			}else if(CALL_ORGID.equals(code)){
				val = config.getOrgId() ;
			}else if(CALL_ORGNAME.equals(code)){
				val = config.getOrgName() ;
			}else if(CALL_ORGKEY.equals(code)){
				val = config.getOrgKey() ;
			}else {
				throw new BusinessException(code+"--所请求的参数代码未找到接口");
			}
		}else{
			throw new BusinessException(code+"--系统参数未设置值或者未启用");
		}
		return val ;
	}
	
	/**
	 * 号码格式转换
	 * @param phoneNo
	 * @return
	 */
	public static String phoneNumberConvert(String phoneNo){
		String newNo = "" ;
		String tempNo = "" ;
		String[] phones = phoneNo.split(",");
		for(int i = 0 ; i < phones.length ;i ++){
			tempNo = phones[i] ;
			if("0".equals(phones[i].substring(0,1))){//座机加上国家代号：86-0755-12345678
				tempNo = "86-" + tempNo ; 
			}
			newNo = StringUtils.isEmpty(newNo) ? tempNo : (newNo + "," + tempNo );
		}
		return newNo ;
	}
	
	/**
	 * 调用URL，并且获取返回参数
	 * 适用于HTTP协议
	 * @param urlString
	 * @return
	 */
	public static Map<String,Object> callUrlAndGetResult(String urlString)  {  
		Map<String,Object> result = new HashMap<String,Object>();
		String msg = "";   
        try {
        	//打开连接
            URL url = new URL(urlString);  
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
            conn.setDoOutput(true);  
            conn.setRequestMethod("POST");  
            
            //构建流，获取返回值
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));  
            String line;  
            while ((line = in.readLine()) != null) {  
                msg += line;  
            }  
            in.close();  
            result.put("STATE", "SUCCESS");
            result.put("MSG", msg);
        } catch (Exception e) {  
             e.printStackTrace();
             result.put("STATE", "EXCEPTION");
             result.put("MSG", e.getMessage());
        }  
        return result;  
    }  
	
	/**
	 * 调用URL，并且获取返回参数
	 * 适用于HTTPS协议
	 * @param urlString
	 * @return
	 */
	public static Map<String,Object> callHttpsUrlAndGetResult(String urlString)  { 
		
		class TrustAnyHostnameVerifier implements HostnameVerifier {
			@Override
			public boolean verify(String hostname, SSLSession sslSession) {
				// TODO Auto-generated method stub
				return true;
			}
		}
		
		Map<String,Object> result = new HashMap<String,Object>();
		String msg = "";   
        try {
        	
        	URL url = new URL(urlString);
        	
        	//自定义HTTPS安全认证管理器
        	HttpsVerifyManager hvm = new HttpsVerifyManager();
        	TrustManager tm[] = {hvm} ;
        	//得到上下文
        	SSLContext ctx = SSLContext.getInstance("SSL");
        	//初始化
        	ctx.init(null, tm, null);
        	HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        	conn.setSSLSocketFactory(ctx.getSocketFactory());
        	conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
        	conn.setRequestProperty("Charset", "GBK");
        	conn.connect();        	
            
            //构建流，获取返回值
        	BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"GBK"));  
        	String line;  
            while ((line = in.readLine()) != null) {  
                msg += line;  
            }  
            in.close();  
            result.put("STATE", "SUCCESS");
            result.put("MSG", msg);
        } catch (Exception e) {  
             e.printStackTrace();
             result.put("STATE", "EXCEPTION");
             result.put("MSG", e.getMessage());
        }  
        return result;  
    }
	
	/**
	 * 跟进操作类别和错误代码返回信息
	 * @param type
	 * @param errorKey
	 * @return
	 */
	public static String getMsgByErrorKey(String type,String errorKey){
		String msg = "" ;
		if(OP_TYPE_ADD.equals(type)){
			if("02".equals(errorKey)){
				msg = "添加成员失败"; 
			}else if("03".equals(errorKey)){
				msg = "成员已经存在" ;
			}else if("04".equals(errorKey)){
				msg = "对外号码存在异常" ;
			}else if("05".equals(errorKey)){
				msg = "对外号码不属于该组织的渠道商" ;
			}else if("06".equals(errorKey)){
				msg = "对外号码已被其他组织使用" ;
			}else if("07".equals(errorKey)){
				msg = "对外号码不可为硬座席" ;
			}else if("08".equals(errorKey)){
				msg = "对外号码不可被组织同时间重复使用" ;
			}else if("13".equals(errorKey)){
				msg = "组织ID不存在或者组织Key错误";
			}else if("14".equals(errorKey)){
				msg = "渠道商号码池号码无号码";
			}else if("15".equals(errorKey)){
				msg = "接口系统异常";
			}else if("16".equals(errorKey)){
				msg = "格式错误；注册手机号码已经默认为接听话机，不用再添加" ;
			}else{
				msg = "其他错误" ;
			}
			msg = "addMenber:" + msg;
		}else if(OP_TYPE_EDIT.equals(type)){
			if("02".equals(errorKey)){
				msg = "修改失败"; 
			}else if("03".equals(errorKey)){
				msg = "指定修改的用户不存在" ;
			}else if("04".equals(errorKey)){
				msg = "指定用户接听话机修改失败" ;
			}else if("05".equals(errorKey)){
				msg = "指定用户显示号码修改失败" ;
			}else if("06".equals(errorKey)){
				msg = "对外号码不属于该组织的渠道商" ;
			}else if("07".equals(errorKey)){
				msg = "对外号码已被其他组织使用" ;
			}else if("08".equals(errorKey)){
				msg = "对外号码不可为硬座席" ;
			}else if("09".equals(errorKey)){
				msg = "对外号码不可重复使用" ;
			}else if("10".equals(errorKey)){
				msg = "新指定的注册手机已经存在用户信息" ;
			}else if("13".equals(errorKey)){
				msg = "组织ID不存在";
			}else if("14".equals(errorKey)){
				msg = "组织Key错误";
			}else if("15".equals(errorKey)){
				msg = "接口系统异常";
			}else if("16".equals(errorKey)){
				msg = "发送信息格式错误" ;
			}else{
				msg = "其他错误" ;
			}
			msg = "editMenber:" + msg;
		}else if(OP_TYPE_DELETE.equals(type)){
			if("02".equals(errorKey)){
				msg = "删除失败"; 
			}else if("03".equals(errorKey)){
				msg = "组织的所有者不可被删除" ;
			}else if("04".equals(errorKey)){
				msg = "被删除的对象不存在" ;
			}else if("13".equals(errorKey)){
				msg = "组织ID不存在或组织KEY错误";
			}else if("15".equals(errorKey)){
				msg = "接口系统异常";
			}else if("16".equals(errorKey)){
				msg = "发送信息格式错误" ;
			}else{
				msg = "其他错误" ;
			}
			msg = "deleteMenber:" + msg;
		}else if(CALL_URL_GETCALLLIST.equals(type)){
			if("01".equals(errorKey)){
				msg = "组织id不存在"; 
			}else if("02".equals(errorKey)){
				msg = "组织密匙错误" ;
			}else if("03".equals(errorKey)){
				msg = "时间跨度超过31天" ;
			}else if("04".equals(errorKey)){
				msg = "该时间段超出查询次数限制";
			}else if("05".equals(errorKey)){
				msg = "参数格式不对";
			}else{
				msg = "其他错误" ;
			}
			msg = "getCallList:" + msg;
		}else if(OP_TYPE_ALLOT.equals(type)){
			if("01".equals(errorKey)){
				msg = "参数无效或者为空"; 
			}else if("02".equals(errorKey)){
				msg = "平台不存在此记录" ;
			}else if("03".equals(errorKey)){
				msg = "号码状态未可用" ;
			}else{
				msg = "其他错误" ;
			}
			msg = "matchPhone:" + msg;
		}
		return msg + "["+errorKey+"]" ;
	}
	
	/**
	 * 生成呼叫话单ID
	 * @return 32位固定长度编码
	 * @throws Exception
	 */
	public static String getCallBillId() throws Exception{
		String billId = "" ;
		String orgId = PhoneMemberUtil.getPhoneConfigParam(null,CALL_ORGID);
		String orgPrev = "" ;
		//组织 8位，不够补0
		for(int i = 0 ; i < 8 - orgId.length() ; i ++){
			orgPrev += "0" ;
		}
		billId += (orgPrev + orgId );
		
		//固定，为渠道商
		billId += "_1_" ;
		
		//生成日期前缀
		String nowDateStr = DateUtil.dateConvertStr(DateUtil.getCurDate(),DateUtil.GENERAL_FORMHMS);
		String regEx = "[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(nowDateStr);
		String dateStr = m.replaceAll("").trim();
		billId += dateStr ;
		//00001234_1_2012113009081624 7303074
		billId += PhoneMemberUtil.getRandomNum(7);
//		System.out.println("=====呼叫话单billId:"+billId);
		return billId ;
	}
	
	/**
	 * 生成固定位随机数
	 * @param pwd_len
	 * @return
	 */
	public static String getRandomNum(int pwd_len) {
		// 35是因为数组是从0开始的，26个字母+10个数字
		final int maxNum = 36;
		int i; // 生成的随机数
		int count = 0; // 生成的密码的长度
		char[] str = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

		StringBuffer pwd = new StringBuffer("");
		Random r = new Random();
		while (count < pwd_len) {
			// 生成随机数，取绝对值，防止生成负数，
			i = Math.abs(r.nextInt(maxNum)); // 生成的数最大为36-1
			if (i >= 0 && i < str.length) {
				pwd.append(str[i]);
				count++;
			}
		}
		return pwd.toString();
	}
	
	/**
	 * test
	 * @param args
	 */
	public static void main(String[] args) {
		PhoneMember cp = new PhoneMember();
		//cp.setUUID();
		cp.setLoginNumber("13794469977");
		cp.setObjectName("孙海亮");
		cp.setUserId("6885");
		cp.setAnswerPhone("");
		//deleteMenber(cp);
		String urlString = "https://api.33e9.net/client/addMember.do?orgId=1561&orgKey=d2a30983eead21b46051172d753455cc&user=%E5%BC%A0%E4%B8%80%2B15013609052%2B%2B%2Bfalse" ;
		try {
			/*
			URL url = new URL(urlString);
			System.out.println(url.getHost());
			System.out.println(url.getProtocol());
			System.out.println(url.getPath());
			System.out.println(url.getPort());
			System.out.println(url.getFile());
			*/
			//callHttpsUrlAndGetResult(urlString);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
	}
}
