package com.wuyizhiye.basedata.system.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuyizhiye.basedata.param.model.ParamConstants;
import com.wuyizhiye.basedata.param.model.ParamLines;
import com.wuyizhiye.basedata.param.service.ParamLinesService;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.framework.base.controller.BaseController;

/**
 * @ClassName SystemConfigController
 * @Description 系统配置controller
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/system/*")
public class SystemConfigController extends BaseController {
	private static Logger logger=Logger.getLogger(SystemConfigController.class);
	@Autowired
	private ParamLinesService paramLinesService;
	
	@RequestMapping(value="systemConfig")
	public String systemConfig(HttpServletResponse response){
		return "system/systemConfig";
	}
	
	//设置地图中心点
	
	@RequestMapping(value="initMapData")
	public String initMapData(ModelMap model,HttpServletResponse response){
		//地图默认经度
		String mapx = ParamUtils.getParamValue(ParamConstants.DEFAULT_MAPX_KEY);
		//地图默认维度
		String mapy = ParamUtils.getParamValue(ParamConstants.DEFAULT_MAPY_KEY);
		//地图默认缩略级别
		String zoom = ParamUtils.getParamValue(ParamConstants.DEFAULT_ZOOM_KEY);
		
		model.put("mapx", mapx);
		model.put("mapy", mapy);
		model.put("zoom", zoom);
		
		return "system/initMapConfig";
	}
	
	
	@RequestMapping(value="saveInitMap")
	@ResponseBody
	public Map<String,Object> saveInitMap(){
		Map<String,Object> result = new HashMap<String, Object>();
		String mapx = getString("mapx");
		String mapy = getString("mapy");
		String zoom = getString("zoom");
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			List<ParamLines> uplist = new ArrayList<ParamLines>();
			map.put("paramNumber", ParamConstants.DEFAULT_MAPX_KEY);
			ParamLines pmapx = this.paramLinesService.getOneParamLines(map);
			if(null!=pmapx){
				pmapx.setParamValue(mapx);
				uplist.add(pmapx);
			}else{
				throw new Exception("无系统参数-地图中心点X坐标,请联系管理员设置");
			}
			map.put("paramNumber", ParamConstants.DEFAULT_MAPY_KEY);
			ParamLines pmapy = this.paramLinesService.getOneParamLines(map);
			if(null!=pmapy){
				pmapy.setParamValue(mapy);
				uplist.add(pmapy);
			}else{
				throw new Exception("无系统参数-地图中心点Y坐标,请联系管理员设置");
			}
			map.put("paramNumber", ParamConstants.DEFAULT_ZOOM_KEY);
			ParamLines pzoom = this.paramLinesService.getOneParamLines(map);
			if(null!=pzoom){
				pzoom.setParamValue(zoom);
				uplist.add(pzoom);
			}else{
				throw new Exception("无系统参数-地图中心点显示级别,请联系管理员设置");
			}
			this.paramLinesService.updateBatch(uplist);
		} catch (Exception e) {
			result.put("result", false);
			logger.error("", e);
		}
		result.put("result", true);
		return result;
	}
	
}
