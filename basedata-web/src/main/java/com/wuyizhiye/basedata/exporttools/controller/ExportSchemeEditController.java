package com.wuyizhiye.basedata.exporttools.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.exporttools.model.ExportColumn;
import com.wuyizhiye.basedata.exporttools.model.ExportFilter;
import com.wuyizhiye.basedata.exporttools.model.ExportScheme;
import com.wuyizhiye.basedata.exporttools.service.ExportSchemeService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName ExportSchemeEditController
 * @Description 数据导出方案编辑
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="exporttools/exportScheme/*")
public class ExportSchemeEditController extends EditController {
	@Autowired
	private ExportSchemeService exportSchemeService;
	@Override
	protected Class<ExportScheme> getSubmitClass() {
		return ExportScheme.class;
	}

	@Override
	protected ExportSchemeService getService() {
		return exportSchemeService;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected ExportScheme getSubmitEntity() {
		ExportScheme sc = (ExportScheme) super.getSubmitEntity();
		String userIndex = getString("useIndex");
		if(StringUtils.isEmpty(userIndex)){
			sc.setUseIndex(false);
		}else{
			sc.setUseIndex(true);
		}
		String filter = getString("filterJson");
		String column = getString("columnJson");
		List<ExportFilter> filterList = new ArrayList<ExportFilter>();
		List<ExportColumn> columnList = new ArrayList<ExportColumn>();
		if(!StringUtils.isEmpty(filter)){
			JSONArray ja = JSONArray.fromObject(filter);
			Collection<ExportFilter> filters = JSONArray.toCollection(ja, ExportFilter.class);
			if(filters != null){
				for(ExportFilter f : filters){
					filterList.add(f);
				}
			}
		}
		if(!StringUtils.isEmpty(column)){
			JSONArray ja = JSONArray.fromObject(column);
			Collection<ExportColumn> filters = JSONArray.toCollection(ja, ExportColumn.class);
			if(filters != null){
				for(ExportColumn c : filters){
					columnList.add(c);
				}
			}
		}
		sc.setFilters(filterList);
		sc.setColumns(columnList);
		return sc;
	}
	
	@Override
	protected boolean validate(Object data) {
		ExportScheme scheme = (ExportScheme)data;
		Map<String,Object> param = new HashMap<String, Object>();
		if(StringUtils.isEmpty(scheme.getName()) || StringUtils.isEmpty(scheme.getNumber())){
			getOutputMsg().put("MSG", "名称和编码不能为空");
			return false;
		}
		List<ExportScheme> list = null;
		param.put("name", scheme.getName());
		list = queryExecutor.execQuery("com.wuyizhiye.basedata.exporttools.dao.ExportSchemeDao.select", param, ExportScheme.class);
		if(list != null && list.size() > 0){
			for(ExportScheme s : list){
				if(!s.getId().equals(scheme.getId())){
					getOutputMsg().put("MSG", "名称己存在");
					return false;
				}
			}
		}
		param.clear();
		param.put("number", scheme.getNumber());
		list = queryExecutor.execQuery("com.wuyizhiye.basedata.exporttools.dao.ExportSchemeDao.select", param, ExportScheme.class);
		if(list != null && list.size() > 0){
			for(ExportScheme s : list){
				if(!s.getId().equals(scheme.getId())){
					getOutputMsg().put("MSG", "编码己存在");
					return false;
				}
			}
		}
		return super.validate(data);
	}
}
