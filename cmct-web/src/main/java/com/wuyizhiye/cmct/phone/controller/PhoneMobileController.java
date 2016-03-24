package com.wuyizhiye.cmct.phone.controller;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.cmct.phone.enums.CostTypeEnum;
import com.wuyizhiye.cmct.phone.model.PhoneMember;
import com.wuyizhiye.cmct.phone.service.PhoneMemberService;
import com.wuyizhiye.cmct.phone.util.PhoneCmctGlUtil;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName PhoneQueryController
 * @Description 呼叫中心HTTP接口 话务查询 控制器
 * @author li.biao
 * @date 2015-5-26
 */

@Controller
@RequestMapping({"cmct/phoneMobile/*"})
public class PhoneMobileController extends ListController{

	@Autowired
	private PhoneMemberService phoneMemberService;

	protected CoreEntity createNewEntity() {
		return new PhoneMember();
	}

	protected String getListView() {
		return "cmct/phone/phoneMobileList";
	}

	protected String getEditView() {
		return "cmct/phone/phoneMobileEdit";
	}

	protected String getListMapper() {
		return "com.wuyizhiye.cmct.phone.dao.PhoneMemberDao.select";
	}

	protected BaseService getService() {
		return this.phoneMemberService;
	}

	@RequestMapping(value="takeCost")
	public String takeCost() {
		if (!StringUtils.isEmpty(getString("id"))) {
			getRequest().setAttribute("data",
					this.phoneMemberService.getEntityById(getString("id")));
		}
		getRequest().setAttribute("costTypes", CostTypeEnum.values());
		return "cmct/phone/phoneMobileCost";
	}

	@RequestMapping(value="saveTakeCost")
	public void saveTakeCost(HttpServletResponse response) {
		String costType = getString("costType");
		String tradAmount = getString("tradAmount");
		int tradMin = getInt("tradMin", 0);
		String description = getString("description");
		String userId = getString("userId");
		Map resMap = PhoneCmctGlUtil.takeCost(userId, costType, tradAmount,
				tradMin, description);
		getOutputMsg().putAll(resMap);
		outPrint(response,
				JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}

	@RequestMapping(value="getRemainMin")
	public void getRemainMin(HttpServletResponse response) {
		String userId = getString("userId");
		Map resMap = PhoneCmctGlUtil.getRemainMin(userId);
		outPrint(response,
				JSONObject.fromObject(resMap, getDefaultJsonConfig()));
	}

	@RequestMapping(value="getAmountMin")
	public void getAmountMin(HttpServletResponse response) {
		String userId = getString("userId");
		String tradAmount = getString("tradAmount", "0");
		Map resMap = PhoneCmctGlUtil.getAmountMin(userId, tradAmount);
		outPrint(response,
				JSONObject.fromObject(resMap, getDefaultJsonConfig()));
	}

	@RequestMapping(value="setShow")
	public String setShow() {
		if (!StringUtils.isEmpty(getString("id"))) {
			getRequest().setAttribute("data",
					this.phoneMemberService.getEntityById(getString("id")));
		}
		return "cmct/phone/phoneMobileShow";
	}

	@RequestMapping(value="saveShow")
	public void saveShow(HttpServletResponse response) {
		String showType = getString("showType");
		String userId = getString("userId");
		String id = getString("id");
		Map resMap = PhoneCmctGlUtil.saveShow(userId, showType);
		if (("SUCCESS".equals(resMap.get("STATE").toString()))
				&& (!StringUtils.isEmpty(id))) {
			PhoneMember phoneMember = (PhoneMember) this.phoneMemberService
					.getEntityById(id);
			if (phoneMember != null) {
				phoneMember.setLoginNumber(showType);
				this.phoneMemberService.updateEntity(phoneMember);
			}
		}

		getOutputMsg().putAll(resMap);
		outPrint(response,
				JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}

	@RequestMapping(value = "toBind")
	public String toBind() {
		if (!StringUtils.isEmpty(getString("id"))) {
			getRequest().setAttribute("data",
					this.phoneMemberService.getEntityById(getString("id")));
		}
		return "cmct/phone/phoneMobileBind";
	}

	@RequestMapping(value="saveBind")
	public void saveBind(HttpServletResponse response) {
		String id = getString("id");
		String showPhone = getString("showPhone");
		String userId = getString("userId");
		Map resMap = PhoneCmctGlUtil.saveBind(userId, showPhone);
		if ("SUCCESS".equals(resMap.get("STATE"))) {
			try {
				PhoneMember member = (PhoneMember) this.phoneMemberService
						.getEntityById(id);
				if (member != null) {
					member.setShowPhone(showPhone);
					this.phoneMemberService.updateEntity(member);
				}
			} catch (Exception e) {
				resMap.put("STATE", "FAIL");
				resMap.put("MSG", e.getMessage());
			}
		}
		getOutputMsg().putAll(resMap);
		outPrint(response,
				JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}

	@RequestMapping(value="demo")
	public String demo() {
		if (!StringUtils.isEmpty(getString("id"))) {
			getRequest().setAttribute("data",
					this.phoneMemberService.getEntityById(getString("id")));
		}
		return "cmct/phone/phoneMobileDemo";
	}

	@RequestMapping(value="dialDemo")
	public void dialDemo(HttpServletResponse response) {
		Map resMap = PhoneCmctGlUtil.dial_phone(getString("userId"),
				getString("showPhone"), getString("called"));
		getOutputMsg().putAll(resMap);
		outPrint(response,
				JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}

	@RequestMapping(value="getCumulative")
	public void getCumulative(HttpServletResponse response) {
		Map resMap = PhoneCmctGlUtil.getCumulative(getString("userIds"));
		if (resMap.get("STATE").toString().equals("SUCCESS")) {
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("details",
					JSONArray.fromObject(resMap.get("details")));
		} else {
			getOutputMsg().putAll(resMap);
		}
		outPrint(response,
				JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}

	@RequestMapping(value="showRecord")
	public String showRecord() {
		String memberId = getString("memberId");
		getRequest().setAttribute("data",
				this.phoneMemberService.getEntityById(memberId));
		return "cmct/phone/phoneShowRecord";
	}

	@RequestMapping(value="showRecordData")
	public void showRecordData(Pagination<?> pagination,
			HttpServletResponse response) {
		Map resMap = PhoneCmctGlUtil.getCostRecord(getString("userId"),
				pagination.getCurrentPage(), pagination.getPageSize());
		if ("SUCCESS".equals(resMap.get("STATE").toString()))
			outPrint(response, resMap.get("pagData"));
		else
			outPrint(response,
					JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}

	@RequestMapping(value="deleteData")
	public void deleteData(HttpServletResponse response) {
		String id = getString("id");
		String userId = getString("userId");
		if (!StringUtils.isEmpty(id)) {
			Map resMap = PhoneCmctGlUtil.deleteGlMember(userId);
			if (resMap.get("STATE").toString().equals("SUCCESS")) {
				getService().deleteById(id);
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "删除成功");
			} else {
				getOutputMsg().putAll(resMap);
			}
		} else {
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "记录不存在");
		}
		outPrint(response,
				JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
}