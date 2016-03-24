package com.wuyizhiye.cmct.fetch.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wuyizhiye.base.util.DateConversionUtils;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.framework.base.controller.BaseController;

/**
 * @ClassName CmctFetchController
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value = "api/*")
public class CmctFetchController extends BaseController {
	/**
	 * 电话系统
	 * 
	 * @param pId
	 *            此项为必填项;表示需要统计哪一个人员电话接通数量
	 * @param dTime
	 *            某一天的时间;(例如：20141111格式有严格的要求；必须是标准的8位数字时间)
	 * @param sTime
	 *            起始时间;(例如：20141111192058格式有严格的要求；必须是标准的14位数字时间)
	 * @param eTime
	 *            结束时间;(例如：20141111192058格式有严格的要求；必须是标准的14位数字时间)
	 * @param response
	 */
	@RequestMapping(value = "cmct/allPass")
	public void getAllPassCount(
			@RequestParam(value = "pId", required = true, defaultValue = "") String pId,
			@RequestParam(value = "dTime", required = true, defaultValue = "") String dTime,
			@RequestParam(value = "sTime", required = true, defaultValue = "") String sTime,
			@RequestParam(value = "eTime", required = true, defaultValue = "") String eTime,
			HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();// 1.返回的数据
		if (StringUtils.isEmpty(pId)) {
			resultMap.put("STATE", "FAIl");// 2.报错---参数异常【pId不可以为空】
			resultMap.put("MSG", "pId不可为空！请检查请求参数");
		} else {
			Map<String, Object> param = new HashMap<String, Object>();// 查询参数
			try {
				param.put("pId", pId);
				if (StringUtils.isNotNull(dTime)) {
					param.put("dTime", "Y");
					param.put("dTimeStart", DateConversionUtils.getStartDate(dTime));
					param.put("dTimeEnd", DateConversionUtils.getEndDate(dTime));
				}
				param.put("sTime", StringUtils.isEmpty(sTime) ? null : DateConversionUtils.conversionStrToDate(sTime));
				param.put("eTime", StringUtils.isEmpty(eTime) ? null : DateConversionUtils.conversionStrToDate(eTime));
				int count = queryExecutor.execCount("com.wuyizhiye.fastsale.dao.CustomerFollowDao.getAllPassCount", param);
				resultMap.put("count", count);
			} catch (Exception e) {
				e.printStackTrace();
				resultMap.put("STATE", "FAIl");// 3.异常
				resultMap.put("MSG", "系统异常请联系管理员稍后重试！");
			}
		}
		outPrint(response, JSONObject.fromObject(resultMap, getDefaultJsonConfig()));
	}

}
