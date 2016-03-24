package com.wuyizhiye.basedata.exporttools.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.exporttools.model.ExportColumn;
import com.wuyizhiye.basedata.exporttools.model.ExportFilter;
import com.wuyizhiye.basedata.exporttools.model.ExportScheme;
import com.wuyizhiye.basedata.exporttools.service.ExportSchemeService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName ExportSchemeListController
 * @Description 数据导出方案列表
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="exporttools/exportScheme/*")
public class ExportSchemeListController extends ListController {
	@Autowired
	private ExportSchemeService exportSchemeService;
	@Override
	protected ExportScheme createNewEntity() {
		return new ExportScheme();
	}

	@Override
	protected String getListView() {
		return "basedata/exporttools/exportSchemeList";
	}

	@Override
	protected String getEditView() {
		return "basedata/exporttools/exportSchemeEdit";
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.exporttools.dao.ExportSchemeDao.select";
	}

	@Override
	protected ExportSchemeService getService() {
		return exportSchemeService;
	}
	
	@RequestMapping(value="export")
	public String export(@RequestParam(value="id")String id,ModelMap model){
		ExportScheme scheme = getService().getEntityById(id);
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("scheme", scheme.getId());
		scheme.setColumns(queryExecutor.execQuery("com.wuyizhiye.basedata.exporttools.dao.ExportColumnDao.select", param, ExportColumn.class));
		scheme.setFilters(queryExecutor.execQuery("com.wuyizhiye.basedata.exporttools.dao.ExportFilterDao.select", param, ExportFilter.class));
		model.put("scheme", scheme);
//		model.put("longNumber", getString("longNumber"));
		return "basedata/exporttools/export";
	}

	@RequestMapping(value="executeExport")
	public void executeExport(@RequestParam(value="id")String id,HttpServletResponse response) throws IOException{
		ExportScheme scheme = getService().getEntityById(id);
		String type = getString("exportType");
		if(StringUtils.isEmpty(type)){
			type = "csv";
		}
		String fileName = scheme.getName()+"."+("excel".equals(type)?"xlsx":type);
		response.setContentType("application/octet-stream");
        response.addHeader("content-disposition", "attachment; filename=" + URLEncoder.encode(fileName, "utf-8") + "");
        OutputStream os = response.getOutputStream();
        try{
        	getService().executeExport(id, getParamMap(), os,type);
        }catch(Exception e){
        	os.write(e.getMessage().getBytes());
        }
		os.close();
	}
}
