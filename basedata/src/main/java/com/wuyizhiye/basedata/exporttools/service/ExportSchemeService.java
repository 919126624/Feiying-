package com.wuyizhiye.basedata.exporttools.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.exporttools.model.ExportScheme;

/**
 * @ClassName ExportSchemeService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface ExportSchemeService extends BaseService<ExportScheme> {
	/**
	 * 执行导出
	 * @param scheme
	 * @param param
	 * @throws IOException 
	 */
	void executeExport(String scheme,Map<String,String> param,OutputStream os,String type) throws IOException;
}
