package com.wuyizhiye.cmct.phone.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import com.wuyizhiye.base.dao.QueryExecutor;
import com.wuyizhiye.base.spring.ApplicationContextAware;
import com.wuyizhiye.base.util.HttpClientUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.param.model.ParamLines;
import com.wuyizhiye.basedata.util.BaseConfigUtil;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.cmct.phone.enums.DialResultEnum;
import com.wuyizhiye.cmct.phone.enums.DialTypeEnum;
import com.wuyizhiye.cmct.phone.enums.PhoneMemberEnum;
import com.wuyizhiye.cmct.phone.model.PhoneDialRecord;
import com.wuyizhiye.cmct.phone.model.PhoneMember;

public class PhoneCmctGlUtil {
	public static final String TIP_KEY_STATE = "STATE";
	public static final String TIP_KEY_MSG = "MSG";
	public static final String TIP_KEY_SUCC = "SUCCESS";
	public static final String TIP_KEY_FAIL = "FAIL";
	public static final String TIP_VALUE_EXCEPTION = "EXCEPTION";
	private static QueryExecutor queryExecutor = (QueryExecutor) ApplicationContextAware
			.getApplicationContext().getBean("queryExecutor");

	private static String getRemoteServerUrl() {
		String remoteServerUrl = BaseConfigUtil.getCurrRemoteHttpUrl();
		if (StringUtils.isEmpty(remoteServerUrl)) {
			remoteServerUrl = "http://120.25.236.193:9980";
		}
		//return "http://www.dingjianyun.com/dingjianerp";
		return remoteServerUrl + "/wuyiyun";
	}

	public static Map<String, Object> registGlMember(PhoneMember member,
			String operate_type) {
		return registGlMember(member.getShowPhone(), member.getPassWd(),
				operate_type);
	}

	public static Map<String, Object> registGlMember(String phone,
			String password, String operate_type) {
		Map resMap = new HashMap();
		Map param = new HashMap();
		param.put("phoneType", "GL");
		param.put("phone", phone);
		Integer memberCount = (Integer) queryExecutor
				.execOneEntity(
						"com.wuyizhiye.cmct.phone.dao.PhoneMemberDao.selectCountGlMember",
						param, Integer.class);
		if (memberCount.intValue() > 0) {
			resMap.put("STATE", "FAIL");
			resMap.put("MSG", "当前成员存在线路...");
			return resMap;
		}

		StringBuffer sb = new StringBuffer();
		sb.append("/api?").append("apiNumber=").append("701_registGlMember");
		sb.append("&params=")
				.append("{customerNumber:")
				.append("'" + ParamUtils.getParamValue("CMCT_CUSOMETID") + "',");
		sb.append("phone:").append("'" + phone + "',").append("password:")
				.append("'" + password + "',").append("operateType:")
				.append("'" + operate_type + "'}");
		resMap = setResMap(getRemoteServerUrl() + sb.toString());
		if ("SUCCESS".equals(resMap.get("STATE").toString())) {
			PhoneMember member = new PhoneMember();
			member.setUserId(resMap.get("userId").toString());
			member.setPassword(password);
			member.setShowPhone(phone);
			member.setPhoneType("GL");
			member.setLoginNumber("1");
			member.setId(resMap.get("memberId").toString());
			member.setSetType(PhoneMemberEnum.SHAR);
			member.setLoginNumber("1");
			queryExecutor
					.executeInsert(
							"com.wuyizhiye.cmct.phone.dao.PhoneMemberDao.insert",
							member);
		}
		return resMap;
	}

	public static Map<String, Object> dial_phone(String uid, String phone,
			String called) {
		StringBuffer sb = new StringBuffer();
		sb.append("/api?").append("apiNumber=").append("701_dialGlphone");
		sb.append("&params=")
				.append("{customerNumber:")
				.append("'" + ParamUtils.getParamValue("CMCT_CUSOMETID") + "',")
				.append("userId:").append("'" + uid + "',").append("phone:")
				.append("'" + phone + "',").append("called:")
				.append("'" + called + "'}");
		
		
/*        StringBuffer sb = new StringBuffer();
        sb.append("/api?").append("apiNumber=").append("701_dialGlphone");
        sb.append("&params=").append("{customerNumber:").append((new StringBuilder("'")).append(ParamUtils.getParamValue("CMCT_CUSOMETID")).append("',").toString()).append("userId:").append((new StringBuilder("'")).append(uid).append("',").toString()).append("phone:").append((new StringBuilder("'")).append(phone).append("',").toString()).append("called:").append((new StringBuilder("'")).append(called).append("'}").toString());
        String  lingStr =  new StringBuilder(String.valueOf(getRemoteServerUrl())).append(sb.toString()).toString();
        return setResMap((new StringBuilder(String.valueOf(getRemoteServerUrl()))).append(sb.toString()).toString());*/
        
		return setResMap(getRemoteServerUrl() + sb.toString());
	}

	public static Map<String, Object> getRemainMin(String userId) {
		StringBuffer sb = new StringBuffer();
		sb.append("/api?").append("apiNumber=").append("701_getRemainMin");
		sb.append("&params=")
				.append("{userId:")
				.append("'" + userId + "',")
				.append("customerNumber:")
				.append("'" + ParamUtils.getParamValue("CMCT_CUSOMETID") + "'}");
		return setResMap(getRemoteServerUrl() + sb.toString());
	}

	public static Map<String, Object> getAmountMin(String userId,
			String tradAmount) {
		StringBuffer sb = new StringBuffer();
		sb.append("/api?").append("apiNumber=").append("701_getAmountMin");
		sb.append("&params=")
				.append("{userId:")
				.append("'" + userId + "',")
				.append("customerNumber:")
				.append("'" + ParamUtils.getParamValue("CMCT_CUSOMETID") + "',")
				.append("tradAmount:").append("'" + tradAmount + "'}");
		return setResMap(getRemoteServerUrl() + sb.toString());
	}

	public static Map<String, Object> takeCost(String userId, String costType,
			String tradAmount, int tradMin, String description) {
		StringBuffer sb = new StringBuffer();
		sb.append("/api?").append("apiNumber=").append("701_takeCost");
		sb.append("&params=").append("{userId:").append("'" + userId + "',")
				.append("costType:").append("'" + costType + "',")
				.append("tradAmount:").append("'" + tradAmount + "',");
		sb.append("tradMin:")
				.append(tradMin + ",")
				.append("customerNumber:")
				.append("'" + ParamUtils.getParamValue("CMCT_CUSOMETID") + "',");
		sb.append("description:").append("'" + description + "'}");
		return setResMap(getRemoteServerUrl() + sb.toString());
	}

	public static Map<String, Object> saveShow(String userId, String showType) {
		StringBuffer sb = new StringBuffer();
		sb.append("/api?").append("apiNumber=").append("701_saveShow");
		sb.append("&params=").append("{userId:").append("'" + userId + "',");
		sb.append("customerNumber:")
				.append("'" + ParamUtils.getParamValue("CMCT_CUSOMETID") + "',")
				.append("showType:").append("'" + showType + "'}");
		return setResMap(getRemoteServerUrl() + sb.toString());
	}

	public static Map<String, Object> saveBind(String userId, String showPhone) {
		StringBuffer sb = new StringBuffer();
		sb.append("/api?").append("apiNumber=").append("701_saveBind");
		sb.append("&params=").append("{userId:").append("'" + userId + "',");
		sb.append("customerNumber:")
				.append("'" + ParamUtils.getParamValue("CMCT_CUSOMETID") + "',")
				.append("showPhone:").append("'" + showPhone + "'}");
		return setResMap(getRemoteServerUrl() + sb.toString());
	}

	public static Map<String, Object> showBill(String key, int currPage,
			int pageSize, String startTime, String endTime) {
		StringBuffer sb = new StringBuffer();
		sb.append("/api?").append("apiNumber=").append("701_showBill");
		sb.append("&params=")
				.append("{customerNumber:")
				.append("'" + ParamUtils.getParamValue("CMCT_CUSOMETID") + "',");
		sb.append("key:").append("'" + key + "',").append("currPage:")
				.append(currPage);
		sb.append(",pageSize:").append(pageSize).append(",startTime:")
				.append("'" + startTime + "',").append("endTime:")
				.append("'" + endTime + "'}");
		return setResMap(getRemoteServerUrl() + sb.toString());
	}

	public static Map<String, Object> getCumulative(String uids) {
		StringBuffer sb = new StringBuffer();
		sb.append("/api?").append("apiNumber=").append("701_getCumulative");
		sb.append("&params=")
				.append("{customerNumber:")
				.append("'" + ParamUtils.getParamValue("CMCT_CUSOMETID") + "',");
		sb.append("userIds:").append("'" + uids + "'}");
		return setResMap(getRemoteServerUrl() + sb.toString());
	}

	public static Map<String, Object> getCostRecord(String uid, int currPage,
			int pageSize) {
		StringBuffer sb = new StringBuffer();
		sb.append("/api?").append("apiNumber=").append("701_getCostRecord");
		sb.append("&params=")
				.append("{customerNumber:")
				.append("'" + ParamUtils.getParamValue("CMCT_CUSOMETID") + "',");
		sb.append("userId:").append("'" + uid + "',").append("currPage:")
				.append(currPage);
		sb.append(",pageSize:").append(pageSize).append("}");
		return setResMap(getRemoteServerUrl() + sb.toString());
	}

	public static Map<String, Object> deleteGlMember(String uid) {
		StringBuffer sb = new StringBuffer();
		sb.append("/api?").append("apiNumber=").append("701_deleteGlMember");
		sb.append("&params=")
				.append("{customerNumber:")
				.append("'" + ParamUtils.getParamValue("CMCT_CUSOMETID") + "',");
		sb.append("userId:").append("'" + uid + "'}");
		Map resMap = new HashMap();
		resMap = setResMap(getRemoteServerUrl() + sb.toString());
		if ("SUCCESS".equals(resMap.get("STATE").toString())) {
			Map param = new HashMap();
			param.put("userId", uid);
			PhoneMember member = (PhoneMember) queryExecutor.execOneEntity(
					"com.wuyizhiye.cmct.phone.dao.PhoneMemberDao.select", param,
					PhoneMember.class);
			param.clear();
			if (member != null) {
				param.put("id", member.getId());
				queryExecutor
						.executeDelete(
								"com.wuyizhiye.cmct.phone.dao.PhoneMemberDao.deleteById",
								param);
			}
		}
		return resMap;
	}

	private static String replaceUrl(String url) {
		url = url.replace("{", "%7B");
		url = url.replace("}", "%7D");
		return url;
	}

	public static Map<String, Object> setResMap(String url) {
		url = replaceUrl(url);
		Map msgRst = new HashMap();
		try {
			String requestData = HttpClientUtil.callHttpUrlGet(url);
			if (!StringUtils.isEmpty(requestData)) {
				JSONObject jsonObj = JSONObject.fromObject(requestData);
				if (jsonObj.getInt("resultType") == 1) {
					JSONObject jsonObject = JSONObject.fromObject(jsonObj
							.get("resultData"));
					if ("SUCCESS".equals(jsonObject.getString("STATE"))) {
						Set<String> keySet = jsonObject.keySet();
						for (String key : keySet) {
							msgRst.put(key, jsonObject.getString(key));
						}
						msgRst.put("STATE", "SUCCESS");
					} else {
						msgRst.put("STATE", "FAIL");
						msgRst.put("MSG", jsonObject.getString("MSG"));
					}
				} else {
					msgRst.put("STATE", "FAIL");
					msgRst.put("MSG", jsonObj.get("resultMsg"));
				}
			} else {
				msgRst.put("STATE", "FAIL");
				msgRst.put("MSG", "未请求到任何数据");
			}
		} catch (Exception e) {
			msgRst.put("STATE", "FAIL");
			msgRst.put("MSG", e.getMessage());
			e.printStackTrace();
		}
		return msgRst;
	}

	public static void writeRecordDataBase(String uid, String phone,
			String called) {
		Map param = new HashMap();
		param.put("uid", uid);
		param.put("phone", phone);
		PhoneMember member = (PhoneMember) queryExecutor.execOneEntity(
				"com.wuyizhiye.cmct.phone.dao.PhoneMemberDao.selectGlMember",
				param, PhoneMember.class);
		PhoneDialRecord cr = new PhoneDialRecord();
		cr.setUUID();
		cr.setCallId(uid);
		if (member != null)
			cr.setCallName(member.getObjectName());
		cr.setCallType(DialTypeEnum.WORK);
		cr.setCallTime(new Date());
		cr.setToPhone(called);
		cr.setCallResult(DialResultEnum.C_127);
		cr.setSuffix(DialRecordUtil.getSuffix());
		cr.setCurrShowPhone(phone);
		cr.setRemark("手机客户端呼出");
		try {
			queryExecutor
					.executeInsert(
							"com.wuyizhiye.cmct.phone.dao.PhoneDialRecordDao.insert",
							cr);
		} catch (Exception e) {
			e.printStackTrace();
			param.clear();
			param.put("paramnumber", "DataBaseType");
			ParamLines paramLines = (ParamLines) queryExecutor
					.execOneEntity(
							"com.wuyizhiye.basedata.param.dao.ParamLinesDao.selectByTypeNumber",
							param, ParamLines.class);
			String dataBaseType = paramLines.getParamValue();
			if (dataBaseType.equals("ORACLE")) {
				queryExecutor
						.executeUpdate(
								"com.wuyizhiye.cmct.phone.dao.PhoneDialRecordDao.createTableOfOracle",
								cr);
				queryExecutor
						.executeUpdate(
								"com.wuyizhiye.cmct.phone.dao.PhoneDialRecordDao.createIndexCallIdOfOracle",
								cr);
				queryExecutor
						.executeUpdate(
								"com.wuyizhiye.cmct.phone.dao.PhoneDialRecordDao.createIndexOrgIdOfOracle",
								cr);
				queryExecutor
						.executeUpdate(
								"com.wuyizhiye.cmct.phone.dao.PhoneDialRecordDao.createIndexCallTimeOfOracle",
								cr);
			} else {
				queryExecutor
						.executeUpdate(
								"com.wuyizhiye.cmct.phone.dao.PhoneDialRecordDao.createTableOfMysql",
								cr);
				queryExecutor
						.executeUpdate(
								"com.wuyizhiye.cmct.phone.dao.PhoneDialRecordDao.createIndexCallIdOfMySql",
								cr);
				queryExecutor
						.executeUpdate(
								"com.wuyizhiye.cmct.phone.dao.PhoneDialRecordDao.createIndexOrgIdOfMySql",
								cr);
				queryExecutor
						.executeUpdate(
								"com.wuyizhiye.cmct.phone.dao.PhoneDialRecordDao.createIndexCallTimeOfMySql",
								cr);
			}
			queryExecutor
					.executeInsert(
							"com.wuyizhiye.cmct.phone.dao.PhoneDialRecordDao.insert",
							cr);
		}
	}
}
