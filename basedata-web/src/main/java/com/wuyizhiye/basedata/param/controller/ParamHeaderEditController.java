package com.wuyizhiye.basedata.param.controller;

import java.util.ArrayList;
import java.util.Collection;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.code.model.Rules;
import com.wuyizhiye.basedata.param.model.ParamHeader;
import com.wuyizhiye.basedata.param.model.ParamLines;
import com.wuyizhiye.basedata.param.service.ParamHeaderService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName ParamHeaderEditController
 * @Description 参数设置
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value = "basedata/param/*")
public class ParamHeaderEditController extends EditController {
	
	@Autowired
	private ParamHeaderService paramHeaderService;
	
	@Override
	protected Class getSubmitClass() {
		return ParamHeader.class;
	}

	@Override
	protected BaseService getService() {
		return paramHeaderService;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected Object getSubmitEntity() {
		ParamHeader paramHeader = (ParamHeader) super.getSubmitEntity();
		String entityJson = getString("entityJson");
		if(!StringUtils.isEmpty(entityJson)){
			Collection<ParamLines> tmp = JSONArray.toCollection(JSONArray.fromObject(entityJson), ParamLines.class);
			paramHeader.setParamLines(new ArrayList<ParamLines>(tmp));
		}
		return paramHeader;
	}
}
