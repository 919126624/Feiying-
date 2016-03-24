package com.wuyizhiye.cmct.phone.controller;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.annotation.Dependence;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.enums.CommonFlagEnum;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.service.OrgService;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.cmct.phone.enums.PhoneEnableEnum;
import com.wuyizhiye.cmct.phone.enums.PhoneMemberEnum;
import com.wuyizhiye.cmct.phone.enums.PhoneMemberUseEnum;
import com.wuyizhiye.cmct.phone.enums.PhoneStateEnum;
import com.wuyizhiye.cmct.phone.model.PhoneConfig;
import com.wuyizhiye.cmct.phone.model.PhoneMember;
import com.wuyizhiye.cmct.phone.model.PhoneRight;
import com.wuyizhiye.cmct.phone.service.ApiCallBackService;
import com.wuyizhiye.cmct.phone.service.PhoneMemberService;
import com.wuyizhiye.cmct.phone.util.FjCtCmctMemberUtil;
import com.wuyizhiye.cmct.phone.util.PhoneMemberUtil;
import com.wuyizhiye.cmct.phone.util.ProjectMApiRemoteServer;
import com.wuyizhiye.cmct.ucs.enums.UcsPhoneSortEnum;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName PhoneMemberListController
 * @Description 话务设置
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping("cmct/phonemember/*")
public class PhoneMemberListController extends ListController {

	public static Logger log = Logger
			.getLogger(PhoneMemberListController.class);

	@Autowired
	private PhoneMemberService phoneMemberService;

	@Autowired
	private OrgService orgService;

	@Autowired
	private ApiCallBackService apiCallBackService;

	@Override
	protected CoreEntity createNewEntity() {
		// TODO Auto-generated method stub
		return new PhoneMember();
	}

	@RequestMapping(value = "toQueryAccount")
	public String toQueryAccount(ModelMap modelMap) {
		// 核算渠道
		List<PhoneConfig> configList = queryExecutor.execQuery(
				PhoneConfig.MAPPER + ".select", null, PhoneConfig.class);
		put("configList", configList);
		put("configFlag", configList != null && configList.size() == 1);
		return "cmct/phone/callAccountQuery";
	}

	/**
	 * 查询账户余额
	 * 
	 * @param response
	 */
	@RequestMapping(value = "queryAccount")
	public void queryAccount(HttpServletResponse response) throws Exception {
		Map<String, Object> configParam = new HashMap<String, Object>();
		configParam.put("isEnable", CommonFlagEnum.YES.getValue());
		List<PhoneConfig> configList = queryExecutor.execQuery(PhoneConfig.MAPPER + ".select", configParam, PhoneConfig.class);
		if (configList != null && configList.size() > 0) {
			Map<String, Object> param = new HashMap<String, Object>();
			for (PhoneConfig config : configList) {
				param.put("orgId", config.getOrgId());
				Map<String, Object> result = PhoneMemberUtil
						.queryAccount(param);
				if (result.get("STATE") != null
						&& result.get("STATE").equals("SUCCESS")) {
					// 接口返回单位为厘
					Double money = result.get("accountMoney") == null ? 0
							: Double.valueOf(result.get("accountMoney")
									.toString());
					BigDecimal bg = new BigDecimal(money / 1000);
					config.setBalance(bg.setScale(2, BigDecimal.ROUND_HALF_UP)
							.doubleValue());
				} else {
					config.setBalance(0D);
				}
			}
			getOutputMsg().put("configList", configList);
			getOutputMsg().put("STATE", "SUCCESS");
		} else {
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("configList", null);
		}
		outPrint(response,
				JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}

	/**
	 * 查询账户余额 by lxl 14.5.14
	 * 
	 * @param response
	 */
	@RequestMapping(value = "queryAccountByOrgId")
	public void queryAccountByOrgId(HttpServletResponse response) throws Exception {
		Map<String, Object> configParam = getParaMap();
		Map<String, Object> result=new HashMap<String, Object>();
		PhoneConfig config=new PhoneConfig();
		if(null!=configParam.get("partners") && "TTEN".equals(configParam.get("partners").toString())){
			result= PhoneMemberUtil.queryAccount(configParam);
			if (result.get("STATE") != null && result.get("STATE").equals("SUCCESS")) {
				// 接口返回单位为厘
				Double money = result.get("accountMoney") == null ? 0: Double.valueOf(result.get("accountMoney").toString());
				BigDecimal bg = new BigDecimal(money / 1000);
				config.setBalance(bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			} else {
				config.setBalance(0D);
			}
			
		}else if(null!=configParam.get("partners") && "HW".equals(configParam.get("partners").toString())){
			configParam.put("isAllot", "YES");
			List<PhoneMember>pms=queryExecutor.execQuery(PhoneMember.MAPPER+".select", configParam, PhoneMember.class);
			if(null!=pms && pms.size()>0){
				result=FjCtCmctMemberUtil.ImsAccount(pms.get(0));
				if(result.get("STATE")!=null && result.get("STATE").equals("SUCCESS")){
					Map<String, Object>cost=(Map<String, Object>) result.get("respMap");
					BigDecimal allPoint=new BigDecimal (Double.valueOf(cost.get("AllPoint").toString()));//总点数
					BigDecimal perValue=new BigDecimal (Double.valueOf(cost.get("PerValue").toString()));//点均价(每个结算点数的平均价格即总价格/总结算点数 单位：分/点)
					BigDecimal blance=new BigDecimal(allPoint.multiply(perValue).doubleValue()/100);
					config.setBalance(blance.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				}
			}else{
				config.setBalance(0D);
			}
		}
		getOutputMsg().put("config", config);
		getOutputMsg().put("STATE", "SUCCESS");
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	@RequestMapping(value = "queryAccount2")
	public void queryAccount2(HttpServletResponse response) throws Exception {
		// 保存余额列表
		Map<String, String> param = new HashMap<String, String>();
		String customerId = ParamUtils.getParamValue(PhoneMemberUtil.CMCT_CUSOMETID) ;
		//customerId = "6d8d0cb5-ab3c-40e7-91e3-6ab908b5eba3" ;
		param.put("customerId", customerId);
		List<?> result = ProjectMApiRemoteServer.getHfyeList(param);
		JSONArray jsonArray = JSONArray.fromObject(result);
		outPrint(response, jsonArray.toString());
	}

	@RequestMapping("manager")
	public String manager(ModelMap model) {
		return "cmct/phone/phoneMemberManager";
	}

	@Override
	protected String getListView() {
		put("currentOrg", SystemUtil.getCurrentOrg());
		put("stateList", PhoneStateEnum.values());
		put("today", new Date());

		/**
		 * cmct平台选择电话号码的方式
		 */
		put("cmctUsePhone",
				ParamUtils.getParamValue(PhoneMemberUtil.CMCT_USE_PHONE));
		String isHwConfig="";
		List<PhoneMember>pms=queryExecutor.execQuery(PhoneMember.MAPPER+".select", null, PhoneMember.class);
		if(null!=pms && pms.size()>0){
			for(PhoneMember pm:pms){
				if("HW".equals(pm.getPhoneType())){
					isHwConfig="YES";
					break;
				}
			}
		}
		put("isHwConfig", isHwConfig);
		return "cmct/phone/phoneMemberList";
	}

	@RequestMapping("allotList")
	public String allotList(ModelMap model) {
		put("currentOrg", SystemUtil.getCurrentOrg());
		put("stateList", PhoneStateEnum.values());
		put("today", new Date());

		/**
		 * cmct平台选择电话号码的方式
		 */
		put("cmctUsePhone",
				ParamUtils.getParamValue(PhoneMemberUtil.CMCT_USE_PHONE));
		return "cmct/phone/AllotLineList";
	}

	public void put(String key, Object obj) {
		this.getRequest().setAttribute(key, obj);
	}

	@Override
	protected String getEditView() {

		put("phoneSorts", UcsPhoneSortEnum.values());// 电信号码类别

		put("typeList", PhoneMemberEnum.values());
		put("useList", PhoneMemberUseEnum.values());

		put("state", getString("state", "1"));

		// 核算渠道
		List<PhoneConfig> configList = queryExecutor.execQuery(
				PhoneConfig.MAPPER + ".select", null, PhoneConfig.class);
		put("configList", configList);
		put("configFlag", configList != null && configList.size() == 1);
		// return "cmct/phone/phoneMemberEdit";
		return "cmct/phone/phoneMatchEdit";
	}

	@Override
	protected Map<String, Object> getListDataParam() {
		Map<String, Object> param = super.getListDataParam();
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		String startTime = getString("startTimeStr");
		String endTime = getString("endTimeStr");
		if (!StringUtils.isEmpty(startTime)) {
			try {
				param.put("startTime", df.parse(startTime));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (!StringUtils.isEmpty(endTime)) {
			try {
				param.put("endTime", DateUtil.getNextDay(df.parse(endTime)));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String orgId = getString("orgId");
		if (null != orgId && !"".equals(orgId)) {
			Org org = this.orgService.getEntityById(orgId);
			param.put("longNumber", org.getLongNumber() + "%");
		} else {
			param.put("longNumber", SystemUtil.getCurrentOrg().getLongNumber()
					+ "%");
		}
		String sortname = getString("sortname");
		String sortorder = getString("sortorder");
		if ("mac".equals(sortname)) {
			param.put("orderSql", "order by d.fmac " + sortorder);
		} else {
			param.put("orderSql",
					"order by D.FORGINTERFACEID , D.FCREATETIME desc ");
		}
		return param;
	}

	/**
	 * 删除
	 */
	@Override
	public void delete(String id, HttpServletResponse response) {
		PhoneMember cs = this.phoneMemberService.getEntityById(id);
		if (cs != null) {
			String isCallBack = getString("isCallBack");
			cs.setIsCallBank("YES".equals(isCallBack) ? CommonFlagEnum.YES
					: CommonFlagEnum.NO);
			// 调用接口，删除话伴成员
			Map<String, Object> result = PhoneMemberUtil.deleteMenber(cs);
			if ("SUCCESS".equals(result.get("STATE").toString())) {
				this.phoneMemberService.deleteById(id);
				// 不删除数据，只标识状态
				cs.setEnable(PhoneEnableEnum.DELETE);
				phoneMemberService.updateEntity(cs);
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "删除成功");
			} else {
				getOutputMsg().put("STATE", "FAIL");
				getOutputMsg().put("MSG", result.get("MSG"));
			}
		} else {
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "记录不存在");
		}

		outPrint(response,
				JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}

	/**
	 * 已分配页签点击删除,删除本地.更改鼎尖yun上面状态为未分配
	 */
	@RequestMapping(value = "deleteMatch")
	public void deleteMatch(
			@RequestParam(required = true, value = "id") String id,
			HttpServletResponse response) {
		String enable = getString("enable");
		if (!StringUtils.isEmpty(enable)) {
			if ("STOP".equals(enable) || "DELETE".equals(enable)) {// dingjian状态为停用或则已回收了的,也直接删除本地
				getService().deleteById(id);
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "删除成功");
			} else {
				// 该号码是使用状态.删除之后更改dingjianyun状态为未使用
				Map<String, String> param = new HashMap<String, String>();
				param.put("id", id);
				Map<String, Object> result = ProjectMApiRemoteServer
						.updatePhoneUnUse(param);
				if (null != result && "SUCC".equals(result.get("FLAG"))) {
					getService().deleteById(id);
					getOutputMsg().put("STATE", "SUCCESS");
					getOutputMsg().put("MSG", "删除成功");
				} else {
					if (null != result
							&& "02".equals((String) result.get("MSG"))) {// 平台没有此记录.删除本地
						getService().deleteById(id);
						getOutputMsg().put("STATE", "SUCCESS");
						getOutputMsg().put("MSG", "删除成功");
					} else {
						getOutputMsg().put("STATE", "FAIL");
						getOutputMsg().put(
								"MSG",
								PhoneMemberUtil.getMsgByErrorKey(
										PhoneMemberUtil.OP_TYPE_ALLOT,
										(String) result.get("MSG")));
					}
				}
			}
		} else {
			// 在dingjianyun上取到状态为空..则直接删除本地
			getService().deleteById(id);
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("MSG", "删除成功");
		}
		outPrint(response,
				JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}

	@RequestMapping("dropOne")
	@ResponseBody
	public void dropOne(HttpServletResponse response) {
		String id = getString("id");
		PhoneMember cs = this.phoneMemberService.getEntityById(id);
		cs.setOnlineTime(null);
		cs.setCurrentUser(null);
		cs.setState(PhoneStateEnum.FREE);// 空闲状态
		this.phoneMemberService.updateEntity(cs);
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "操作成功");
		outPrint(response,
				JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}

	// 批量下线
	@RequestMapping("dropBatch")
	@ResponseBody
	public void dropBatch(HttpServletResponse response) {
		String phoneMemberIds = getString("ids");
		String[] ids = phoneMemberIds.split(",");
		PhoneMember cs;
		List<PhoneMember> phoneMemberList = new ArrayList();
		for (String phoneMemberId : ids) {
			cs = this.phoneMemberService.getEntityById(phoneMemberId);
			cs.setOnlineTime(null);
			cs.setCurrentUser(null);
			cs.setState(PhoneStateEnum.FREE);// 空闲状态
			phoneMemberList.add(cs);
		}
		phoneMemberService.updateBatch(phoneMemberList);
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "操作成功");
		outPrint(response,
				JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}

	/**
	 * stopData 停用
	 */
	@RequestMapping("stopData")
	@ResponseBody
	public void stopData(HttpServletResponse response) {
		String id = getString("id");
		String type = getString("type");
		PhoneMember cs = this.phoneMemberService.getEntityById(id);
		if (cs != null) {

			if ("stop".equals(type)) {
				cs.setIsCallBank(CommonFlagEnum.NO);// 不回收删除

				cs.setCancelDate(new Date());
				cs.setEnable(PhoneEnableEnum.STOP);// 停用

				// 调用接口，删除话伴成员
				Map<String, Object> result = PhoneMemberUtil.deleteMenber(cs);
				if ("SUCCESS".equals(result.get("STATE").toString())) {
					this.phoneMemberService.updateEntity(cs);
					getOutputMsg().put("STATE", "SUCCESS");
					getOutputMsg().put("MSG", "操作成功");
				} else {
					getOutputMsg().put("STATE", "FAIL");
					getOutputMsg().put("MSG", result.get("MSG"));
				}

			} else {
				cs.setEnable(PhoneEnableEnum.USE);// 启用
				cs.setState(PhoneStateEnum.FREE);// 开通状态
				cs.setCancelDate(null);

				// 默认接听话机为去电显示号码
				cs.setAnswerPhone(cs.getShowPhone());
				// 调用接口，修改话伴成员
				Map<String, Object> result = PhoneMemberUtil.addMenber(cs);
				if ("SUCCESS".equals(result.get("STATE").toString())) {
					this.phoneMemberService.updateEntity(cs);
					getOutputMsg().put("STATE", "SUCCESS");
					getOutputMsg().put("MSG", "操作成功");
				} else {
					getOutputMsg().put("STATE", "FAIL");
					getOutputMsg().put("MSG", result.get("MSG"));
				}
			}
		} else {
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "记录不存在");
		}

		outPrint(response,
				JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}

	@RequestMapping(value = "bindRight")
	public void bindRight(HttpServletResponse response) {

		String type = this.getString("type");
		String memberid = this.getString("memberid");
		String rightid = this.getString("rightid");
		String callRightId = this.getString("callRightId");
		try {
			PhoneMember pm = this.phoneMemberService.getEntityById(memberid);

			if ("unbind".equals(type)) {
				if (null == pm.getPhoneRight()) {
					throw new Exception("无绑定相关权限");
				}
				callRightId = pm.getPhoneRight().getCallRightId();
				if (StringUtils.isEmpty(callRightId)) {
					throw new Exception("绑定的权限没有在话伴系统注册");
				}
			} else if ("bind".equals(type)) {
				if (StringUtils.isEmpty(rightid)) {
					throw new Exception("请选择需要绑定的权限");
				}
				if (StringUtils.isEmpty(callRightId)) {
					throw new Exception("绑定的权限没有在话伴系统注册");
				}
			}
			String phonenumber = pm.getShowPhone();
			if (StringUtils.isEmpty(phonenumber)) {
				throw new Exception("绑定号码为空,系统异常");
			}
			if (!phonenumber.startsWith("86"))
				phonenumber = "86-" + phonenumber;
			String[] pary = phonenumber.split("-");
			if (pary.length != 3)
				throw new Exception("号码格式不正确,正确格式为 区号-固定电话号码");
			PhoneRight pr = new PhoneRight();
			pr.setId(rightid);
			pr.setPhonenum(phonenumber);
			pr.setCallRightId(callRightId);
			this.phoneMemberService.updatePhoneRight(pm, pr, type);

			this.getOutputMsg().put("STATE", "SUCCESS");
			this.getOutputMsg().put("MSG", "保存成功");
		} catch (Exception e) {
			this.getOutputMsg().put("STATE", "FAIL");
			this.getOutputMsg().put("MSG", e.getMessage());
		}
		outPrint(response,
				JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}

	@RequestMapping(value = "listRightData")
	@Dependence(method = "list")
	public void listRightData(Pagination<PhoneMember> pagination,
			HttpServletResponse response) {
		// pagination =
		// queryExecutor.execQuery("com.wuyizhiye.cmct.phone.dao.PhoneMemberDao.selectRight",
		// pagination, getListDataParam());

		// by lxl 24.1.11 单个号码没有设置权限,显示默认的权限
		Map<String, Object> param = new HashMap<String, Object>();
		pagination = queryExecutor.execQuery(
				"com.wuyizhiye.cmct.phone.dao.PhoneMemberDao.selectRight",
				pagination, getListDataParam());
		List<PhoneMember> pms = pagination.getItems();
		if (null != pms && pms.size() > 0) {
			for (PhoneMember pm : pms) {
				// 如果单个号码没有设置权限,根据组织ID和核算渠道ID查找默认的权限
				if (null == pm.getPhoneRight()) {
					param.put("orgId", pm.getOrg().getId());
					param.put("callOrgId", pm.getOrgInterfaceId());
					List<PhoneRight> prs = queryExecutor
							.execQuery(
									"com.wuyizhiye.cmct.phone.dao.PhoneRightDao.judgeDefaultCallId",
									param, PhoneRight.class);
					if (null != prs && prs.size() > 0) {
						pm.setPhoneRight(prs.get(0));
						pm.setIsDefaultPrivilege("YES");
					}
				}
			}
		}
		pagination.setItems(pms);
		outPrint(response,
				JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	
	@RequestMapping(value = "listRight")
	protected String listRight(ModelMap map) {
		map.put("currentOrg", SystemUtil.getCurrentOrg());
		map.put("stateList", PhoneStateEnum.values());
		map.put("today", new Date());
		return "cmct/phone/phoneMemberRightList";
	}

	@Override
	protected String getListMapper() {
		// TODO Auto-generated method stub
		return PhoneMember.MAPPER + ".select";
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return phoneMemberService;
	}

	
	public void listData1(Pagination<?> pagination, HttpServletResponse response) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("customerId",
				ParamUtils.getParamValue(PhoneMemberUtil.CMCT_CUSOMETID));// 目前先从系统参数读取客户Id
		// map.put("status",
		// PhoneEnableEnum.UNUSE.getValue());//获取当前的状态不要传status
		Pagination<PhoneMember> localPag = (Pagination<PhoneMember>) queryExecutor
				.execQuery(getListMapper(), pagination, getListDataParam());
		List<PhoneMember> localPms = localPag.getItems();// queryExecutor.execQuery(getListMapper()
															// ,
															// getListDataParam(),
															// PhoneMember.class);
		List<?> pag = ProjectMApiRemoteServer.getAllPhoneList(map);

		if (null != pag && pag.size() > 0) {
			JSONArray jsonArray = JSONArray.fromObject(pag);
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsObj = jsonArray.getJSONObject(i);
				String id = (String) jsObj.get("id");

				if (!StringUtils.isEmpty(id)) {
					if (null != localPms && localPms.size() > 0) {
						for (int j = 0; j < localPms.size(); j++) {
							if (!StringUtils.isEmpty(localPms.get(j).getId())
									&& id.equals(localPms.get(j).getId())) {
								// 如果匹配的上.把当前的这一个
								localPms.get(j).setMatch(true);// 已经匹配过
								String state = (String) jsObj.get("state");
								String partners = (String) jsObj
										.getString("partners");// 运营商
								localPms.get(j).setPhoneType(partners);
								PhoneEnableEnum[] enums = PhoneEnableEnum
										.values();
								for (PhoneEnableEnum en : enums) {
									if (en.getValue().equals(state)) {
										localPms.get(j).setEnable(en);
										getService().updateEntity(
												localPms.get(j));
									}
								}
								break;
							} else {
								if (!localPms.get(j).isMatch()) {
									localPms.get(j).setMatch(false);
								}
							}
						}
					}
				}
			}
		}

		/**
		 * 匹配不上的数据状态设置为已回收的
		 */
		if(null!=pag && pag.size()>0){
			for (int i = 0; i < localPms.size(); i++) {
				PhoneMember pm = localPms.get(i);
				if (null != pm && !pm.isMatch()) {
					pm.setEnable(PhoneEnableEnum.DELETE);
					getService().updateEntity(pm);
				}
			}

		}
		
		/*
		 * Pagination pagLocal=new Pagination();
		 * pagLocal.setCurrentPage(pagination.getCurrentPage());
		 * pagLocal.setRecordCount(localPag.size());
		 * pagLocal.setPageSize(pagination.getPageSize()); //
		 * pagLocal.setExceSql(pagination.getExceSql());
		 * pagLocal.setItems(localPms);
		 */
		localPag.setItems(localPms);
		outPrint(response,
				JSONObject.fromObject(localPag, getDefaultJsonConfig()));
	}
	
	/**
	 * 异步获取在鼎尖平台的电话号码
	 * @param response
	 */
	@RequestMapping(value="getDingJianPhone")
	public void getDingJianPhone(HttpServletResponse response){
		Map<String, String> map = new HashMap<String, String>();
		map.put("customerId",ParamUtils.getParamValue(PhoneMemberUtil.CMCT_CUSOMETID));// 目前先从系统参数读取客户Id
		Map<String, String>result=ProjectMApiRemoteServer.getAllPhoneList2(map);
		if(null!=result && "SUCCESS".equals(result.get("STATE"))){
			outPrint(response, result.get("phoneList"));
		}else{
			outPrint(response, JSONObject.fromObject("{}"));
		}
	}
	
	/**
	 * 修改电话状态
	 */
	@RequestMapping(value="changePhoneEnable")
	public void changePhoneEnable(HttpServletResponse response){
		String phoneArr=getString("phoneArr");
		if(!StringUtils.isEmpty(phoneArr)){
			try {
				List<PhoneMember>updatePs=new ArrayList<PhoneMember>();
				JSONArray arry = JSONArray.fromObject(phoneArr);
				for(int i=0;i<arry.size();i++){
					JSONObject json=arry.getJSONObject(i);
					PhoneMember member=this.phoneMemberService.getEntityById(json.getString("id"));
					if(null!=member){
						member.setEnable(PhoneEnableEnum.getEnumByValue(json.getString("state")));
					}
					updatePs.add(member);
				}
				if(updatePs.size()>0){
					getOutputMsg().put("STATE", "SUCCESS");
					this.getOutputMsg().put("MSG", "更新成功");	
					this.phoneMemberService.updateBatch(updatePs);
				}
			}catch (Exception e) {
				this.getOutputMsg().put("STATE", "FAIL");
				this.getOutputMsg().put("MSG", e.getMessage());	
			}
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "数据为空");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	@RequestMapping(value="voiceUpload")
	public String voiceUpload(){
		return "cmct/phone/phoneVoiceEdit";
	}
}
