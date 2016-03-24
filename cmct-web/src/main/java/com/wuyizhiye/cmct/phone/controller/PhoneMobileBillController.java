package com.wuyizhiye.cmct.phone.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.cmct.phone.util.PhoneCmctGlUtil;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName PhoneQueryController
 * @Description 
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping({ "cmct/phoneMobileBill/*" })
public class PhoneMobileBillController extends ListController {
	
	protected CoreEntity createNewEntity() {
		return null;
	}

	protected String getListView() {
		getRequest().setAttribute("userId", getString("userId"));
		SimpleDateFormat day = new SimpleDateFormat("yyyy/MM/dd");
		getRequest().setAttribute("startDay",
				day.format(new Date()).substring(0, 7) + "/01");
		getRequest().setAttribute("endDay", day.format(new Date()));

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		getRequest().setAttribute("showMonth",
				df.format(new Date()).substring(0, 7));
		getRequest().setAttribute("showDay", df.format(new Date()));
		getRequest().setAttribute(
				"startDay",
				(df.format(new Date()).substring(0, 7) + "-01").replaceAll("-",
						"/"));
		getRequest().setAttribute("endDay",
				df.format(new Date()).replaceAll("-", "/"));
		return "cmct/phone/phoneMobileBill";
	}

	protected String getEditView() {
		return null;
	}

	protected String getListMapper() {
		return null;
	}

	protected BaseService getService() {
		return null;
	}

	public void listData(Pagination<?> pagination, HttpServletResponse response) {
		String dateType = getString("dateType");
		String showMonth = getString("showMonth");
		String showDay = getString("showDay");
		String startTime = getString("startTime");
		String endTime = getString("endTime");
		String start = "";
		String end = "";
		if ("month".equals(dateType)) {
			if ((showMonth != null) && (!"".equals(showMonth))) {
				start = showMonth + "-01";
				end = getMonthEnd(showMonth);
			}
		} else if ("day".equals(dateType)) {
			if ((showDay != null) && (!"".equals(showDay))) {
				start = showDay;
				end = showDay;
			}
		} else if ("period".equals(dateType)) {
			if ((startTime != null) && (!"".equals(startTime))) {
				start = startTime.replaceAll("/", "-");
			}
			if ((endTime != null) && (!"".equals(endTime))) {
				end = endTime.replaceAll("/", "-");
			}
		}
		String key = getString("key", "");
		Map resMap = PhoneCmctGlUtil.showBill(key, pagination.getCurrentPage(),
				pagination.getPageSize(), start, end);
		if ("SUCCESS".equals(resMap.get("STATE").toString()))
			outPrint(response, resMap.get("pagData"));
		else
			outPrint(response,
					JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}

	public String getMonthEnd(String str) {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		try {
			c.setTime(format.parse(str));
		} catch (Exception e) {
			e.printStackTrace();
		}
		int MaxDay = c.getActualMaximum(5);

		c.set(c.get(1), c.get(2), MaxDay, 23, 59, 59);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(c.getTime());
	}
}