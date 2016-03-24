package com.wuyizhiye.basedata.showSql;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.framework.base.controller.BaseController;

/**
 * @ClassName ShowSqlController
 * @Description 点击时间时判断是否可以展示sql
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/showSql/*")
public class ShowSqlController extends BaseController {
	
	@RequestMapping(value="showSql")
	public void showSql(HttpServletResponse response){
		String paramValue=ParamUtils.getParamValue("ALLOW_SHOW_SQL");
		getOutputMsg().put("value", paramValue);
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
}
