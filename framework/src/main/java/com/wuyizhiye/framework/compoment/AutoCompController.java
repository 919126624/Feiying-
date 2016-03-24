package com.wuyizhiye.framework.compoment;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.atuocomp.AutoComp;
import com.wuyizhiye.base.atuocomp.config.AutoCompConfig;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName AutoCompController
 * @Description 数据补全选择器
 * @author li.biao
 * @date 2015-8-6
 */
@Controller
@RequestMapping(value="/framework/autocomp/*")
public class AutoCompController extends ListController {
	
	@Resource(name="autoCompConfig")
	private AutoCompConfig autoConfig;
	
	@Override
	protected CoreEntity createNewEntity() {
		return null;
	}

	@Override
	protected String getListView() {
		String id = getString("autoId");
		int page = Integer.valueOf(getString("page","1"));
		int pageSize = Integer.valueOf(getString("pageSize","8"));
		String keyword = getString("keyword");
		if(StringUtils.isEmpty(id)){
			throw new RuntimeException("auto id cann't be null.");
		}
		AutoComp<?> auto = autoConfig.getAuto(id);
		if(auto == null){
			throw new RuntimeException("auto is not exists.");
		}
		Map<String,Object> param = getListDataParam();
		param.remove("autoId");
		param.put("key", keyword);
		//查询mapper
		Pagination<T> pagData = new Pagination<T>(pageSize, page);
		pagData = queryExecutor.execQuery(auto.getMapper(), pagData, param);
		getRequest().setAttribute("param", param);
		getRequest().setAttribute("auto", auto);
		getRequest().setAttribute("data", pagData.getItems());
		getRequest().setAttribute("page", page);
		getRequest().setAttribute("pageSize", pageSize);
		getRequest().setAttribute("pageCount", pagData.getPageCount());
		getRequest().setAttribute("keyword", keyword);
		return "framework/compoment/autocomplete";
	}

	@RequestMapping(value="listAllname")
	public String listAllname(){
		String id = getString("autoId");
		int page = Integer.valueOf(getString("page","1"));
		int pageSize = Integer.valueOf(getString("pageSize","8"));
		String keyword = getString("keyword");
		if(StringUtils.isEmpty(id)){
			throw new RuntimeException("auto id cann't be null.");
		}
		AutoComp<?> auto = autoConfig.getAuto(id);
		if(auto == null){
			throw new RuntimeException("auto is not exists.");
		}
		Map<String,Object> param = getListDataParam();
		param.remove("autoId");
		param.put("key", keyword);
		//查询mapper
		Pagination<T> pagData = new Pagination<T>(pageSize, page);
		pagData = queryExecutor.execQuery(auto.getMapper(), pagData, param);
		getRequest().setAttribute("param", param);
		getRequest().setAttribute("auto", auto);
		getRequest().setAttribute("data", pagData.getItems());
		getRequest().setAttribute("page", page);
		getRequest().setAttribute("pageSize", pageSize);
		getRequest().setAttribute("pageCount", pagData.getPageCount());
		getRequest().setAttribute("keyword", keyword);
		return "framework/compoment/autocompleteAllname";
	}
	@Override
	protected String getEditView() {
		return null;
	}
	
	@RequestMapping(value="demo")
	protected String demo() {
		return "framework/compoment/autodemo";
	}

	@Override
	protected String getListMapper() {
		String id = getString("autoId");
		AutoComp<?> query = autoConfig.getAuto(id);
		if(query==null){
			throw new RuntimeException("auto cann't be null.");
		}
		return query.getMapper();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected BaseService getService() {
		return null;
	}
}
