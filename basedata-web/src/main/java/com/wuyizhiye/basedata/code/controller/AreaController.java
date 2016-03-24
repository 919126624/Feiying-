package com.wuyizhiye.basedata.code.controller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.dao.QueryExecutor;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.basedata.bank.model.City;
import com.wuyizhiye.basedata.bank.service.CityService;
import com.wuyizhiye.basedata.code.enums.AreaTypeEnum;
import com.wuyizhiye.basedata.code.model.Area;
import com.wuyizhiye.basedata.code.service.AreaService;
import com.wuyizhiye.basedata.param.model.ParamConstants;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.framework.base.controller.BaseController;

/**
 * @ClassName AreaController
 * @Description 片区控制类
 * @author li.biao
 * @date 2015-4-7
 */
@Controller(value="basedataAreaController")
@RequestMapping(value="basedata/area/*")
public class AreaController extends  BaseController {
	@Autowired
	private AreaService areaService;
	
	@Autowired
	private CityService cityService;
	
	@Resource(name="queryExecutor")
	protected QueryExecutor queryExecutor;
	
	/**
	 * 
	 * 得到片区列表
	 * @author Cai.xing
	 */
	
	@RequestMapping(value="getList")
	public String getList(HttpServletResponse response){
		return "basedata/area/areaList";
	}
	
	@RequestMapping(value="getListData")
	public Map<String,Object> getListData(ModelMap model,HttpServletResponse response){
		String type = getString("areaType");
		String parentId = getString("parentId");
		String cityId=getString("cityId");
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("parentId", parentId);
		result.put("areaType", type);
		result.put("cityId", cityId);
//		List<Area> areaList =areaService.getAreaList(type,parentId);
		List<Area>areaList=queryExecutor.execQuery("com.wuyizhiye.basedata.code.dao.AreaDao.select", result, Area.class);
		result.clear();
		result.put("items", areaList);
		outPrint(response, JSONObject.fromObject(result));
		return null;
	}
	
	@RequestMapping(value="getListData2")
	public void getListData2(Pagination<?> pagination,HttpServletResponse response){
		//TODO key
		pagination = queryExecutor.execQuery("com.wuyizhiye.basedata.code.dao.AreaDao.select", pagination, getParaMap());
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	
	
	@RequestMapping(value="getListDataByKey")
	public void getListDataByKey(ModelMap model,HttpServletResponse response){
		String key  = getString("term");
		String type  = getString("type");
		String parentId  = getString("parentId");
		Map<String,Object> params = 	new HashMap<String,Object>(); 
		params.put("areaType", type);
		params.put("parentId", parentId);
		params.put("term", key);
		List<Area> areaList =areaService.getListByCollection(params);
		if(StringUtils.isBlank(key)){
			return ;
		}
		Iterator<Area> its = areaList.iterator();
		StringBuilder buf = new StringBuilder();
		buf.append("[");
		while(its.hasNext()){
			Area area = its.next();
			buf.append("{id:\'"+area.getId()+"\',");
			buf.append("open:\'"+"true"+"\',");
			buf.append("name:\'"+area.getName()+"\'}");
			if(its.hasNext()){
				buf.append(",");
			}
		}
		buf.append("]");
		getOutputMsg().put("treeData",buf.toString());
		//getOutputMsg().put("propertyTypeName", PropertyTypeEnum.getJsonStr());
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	/**
	 * 
	 * 根据片区id查询片区信息
	 * @author Cai.xing
	 */
	@RequestMapping(value="getAreaInfoById")
	@ResponseBody
	public Map<String,Object> getAreaInfoById(){
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("result", areaService.getEntityById(getString("fid")));
		return result;
	}
	/**
	 * 
	 * 新增修改片区
	 * @author Cai.xing
	 */
	@RequestMapping(value="addArea")
	public Map<String,Object> addArea(HttpServletResponse response){
		if(getString("areaId")!=null){
			Area area = areaService.getEntityById(getString("areaId"));
			String number = area.getNumber();
			initParams(area);
			//获得新编码
			String newNumber = area.getNumber();
			if(!number.equals(newNumber)){
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("number", newNumber);
				if(queryExecutor.execCount("com.wuyizhiye.basedata.code.dao.AreaDao.getListByCollection", params )>0){
					getOutputMsg().put("STATE", "FAIL");
					getOutputMsg().put("MSG", "编码重复，请修改编码。");
				}else{
					area.setId(getString("areaId"));
					areaService.updateEntity(area);
					getOutputMsg().put("STATE", "SUCCESS");
					getOutputMsg().put("MSG", "修改成功");
				}
			}else{
				area.setId(getString("areaId"));
				if(null!=area.getParent()){
					Area parea = this.areaService.getEntityById(area.getParent().getId());
					area.setLongNumber(parea.getLongNumber()+"!"+area.getNumber());
					area.setDisplayName(parea.getDisplayName()+"-"+area.getName());
				}
				areaService.updateEntity(area);
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "修改成功");
			}
			
		}else{
			Map<String, Object> params = new HashMap<String, Object>();
			Area area = new Area();
			initParams(area);
			params.put("number", area.getNumber());
			if(queryExecutor.execCount("com.wuyizhiye.basedata.code.dao.AreaDao.getListByCollection", params )>0){
				getOutputMsg().put("STATE", "FAIL");
				getOutputMsg().put("MSG", "编码重复，请修改编码。");
			}else{
				area.setId(UUID.randomUUID().toString());
				if(null!=area.getParent()){
					Area parea = this.areaService.getEntityById(area.getParent().getId());
					area.setLongNumber(parea.getLongNumber()+"!"+area.getNumber());
					area.setDisplayName(parea.getDisplayName()+"-"+area.getName());
				}
				areaService.addEntity(area);
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "添加成功！");
			}
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
		return null;
	}
	@RequestMapping(value="addView")
	public String add(ModelMap model){
		String parentId = getString("parentAreaId");
		String cityId = getString("cityId");
		if(!StringUtils.isEmpty(parentId)){
			Area area=this.areaService.getEntityById(parentId);
			model.put("area", area);
		}
		model.put("cityId", cityId);
		return "basedata/area/areaAdd";
	}
	
	/**
	 * 
	 * 修改片区信息
	 * @author Cai.xing
	 */
	@RequestMapping(value="updateArea")
	public Map<String,Object> updateArea(HttpServletResponse response){
		Map<String,Object> result = new HashMap<String, Object>();
		Area area = new Area();
		initParams(area);
		String areaId = getString("areaId");
		area.setId(areaId);
		areaService.updateEntity(area);
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "保存成功");
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
		return null;
	}
	
	/**
	 * 
	 * 删除片区
	 * @author zqf
	 */
	@RequestMapping(value="deleteArea")
	@ResponseBody
	public Map<String,Object> deleteArea(HttpServletResponse response){
		String areaId = getString("areaId");
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("geographyAreaId", areaId);
		param.put("parentId", areaId);
		if(queryExecutor.execCount("com.wuyizhiye.basedata.code.dao.AreaDao.getListByCollection", param )>1){//包含了自己
			setOutputMsg("MSG", "该区域下含有子区域,请删除子区域再删除");
			setOutputMsg("STATE", "FAIL");
		}else{
			Area area = new Area();
			area.setId(areaId);
			area.setId(areaId);
			areaService.deleteEntity(area);
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("MSG", "删除成功");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
		return null;
	}
	@RequestMapping(value="getNode") 
	public void getNode(ModelMap model,HttpServletResponse response){  
		String result = this.getChildsNode(1, areaService.getAreaList("1", null));
		getOutputMsg().put("treeData",result);
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	
	@RequestMapping(value="toMapMark")
	public String toMapMark(ModelMap model, HttpServletResponse response){
		Area area = areaService.getEntityById(getString("fid"));
		model.put("id", getString("fid"));
		model.put("mapx", area.getMapx());
		model.put("mapy", area.getMapy());
		model.put("zoom", area.getZoom());
		model.put("areaName", area.getName());
		//如果未标过点，则设置默认数据
		if(!StringUtils.isNotEmpty(area.getMapx())){
			//系统默认配置
			model.put("defaultMapx", ParamUtils.getParamValue(ParamConstants.DEFAULT_MAPX_KEY));
			model.put("defaultMapy", ParamUtils.getParamValue(ParamConstants.DEFAULT_MAPY_KEY));
			model.put("defaultZoom", ParamUtils.getParamValue(ParamConstants.DEFAULT_ZOOM_KEY));
		}
		return "basedata/area/areaMapMark";
	}
	
	@RequestMapping(value="saveMapMark")
	@ResponseBody
	public Map<String, Object> saveMapMark(ModelMap model, HttpServletResponse response){
		Map<String,Object> result = new HashMap<String, Object>();
		Area area = areaService.getEntityById(getString("id"));
		try {
			area.setMapx(getString("mapx"));
			area.setMapy(getString("mapy"));
			area.setZoom(getInt("zoom"));
			area.setPointime(DateUtil.getCurDate());
			area.setMapoint(1);
			area.setPointPerson(this.getCurrentUser());
			areaService.updateEntity(area);
			result.put("flag", true);
		} catch (Exception e) {
			result.put("flag", false);
		}
		
		return result;
	}
	
	
	public String getChildsNode(int level,List<Area> areaList){
		StringBuffer buf = new StringBuffer("[");
		for(int i=0;i<areaList.size();i++){
			if(i!=0){
				buf.append(",");
			}
			Area areaTemp = areaList.get(i);
			buf.append("{id:'")
				.append(areaTemp.getId())
				.append("',type:'")
				.append(AreaTypeEnum.REGION)
				.append("',open:'true")
				.append("',name:'")
				.append(areaTemp.getName()) 
				.append("',children:");
			if(areaTemp.getAreaType().equals(AreaTypeEnum.REGION)){
				buf.append("[]");
				/*buf.append("[{id:'"+AreaTypeEnum.GEOGRAPHY+i+"',name:'"+AreaTypeEnum.GEOGRAPHY.getDesc()+"',type:'"+AreaTypeEnum.GEOGRAPHY+"'},");
				buf.append("{id:'"+AreaTypeEnum.BUSINESS+i+"',name:'"+AreaTypeEnum.BUSINESS.getDesc()+"',type:'"+AreaTypeEnum.BUSINESS+"'},");
				buf.append("{id:'"+AreaTypeEnum.ROAD+i+"',name:'"+AreaTypeEnum.ROAD.getDesc()+"',type:'"+AreaTypeEnum.ROAD+"'}]");*/
			}else{
				buf	.append(getChildsNode(level+1,areaService.getAreaList(level+1+"", areaTemp.getId())));
			}
			buf.append("}");
		}
		buf.append("]"); 
		return buf.toString();
	}
	/**
	 * 
	 * 初始化参数
	 * @author Cai.xing
	 */
	private void initParams(Area area){
		String name =  getString("name");// 片区名
		String fullPinyin =  getString("fullPinyin");// 全拼
		String simplePinyin =  getString("simplePinyin");// 简拼
		String number =  getString("number");// 片区编号
		String description =  getString("description"); // 片区描述
		String areaType =  getString("areaType"); //片区类型
		String parentid =  getString("parentid"); // 片区父节点
		String cityID=getString("cityId");
		if(!StringUtils.isEmpty(cityID)){
			City city=cityService.getEntityById(cityID);
			area.setCity(city);
		}
		area.setName(name);
		area.setFullPinyin(fullPinyin);
		area.setSimplePinyin(simplePinyin);
		area.setNumber(number);
		area.setDescription(description);
		if(areaType!=null){
			area.setAreaType(AreaTypeEnum.valueOf(areaType));
		}else{
			if(parentid!=null){
				area.setAreaType(AreaTypeEnum.REGION);
			}else{
				area.setAreaType(AreaTypeEnum.CITY);
			}
		}
		if(parentid!=null){
			Area parent = new Area();
			parent.setId(parentid);
			area.setParent(parent);
		}
	}
	
    public static void main(String[] args) throws IOException{
    	  String strURL = "http://www.szzlb.gov.cn/LEAPV5/LEAP/Service/RPC/RPC.DO?callService=web&method=beanSearch&sid=A8569A40EB7FF945CF9FBBE6496995C8&rup=http://www.szzlb.gov.cn/LEAPV5/&_website_=*&a=H4sIAAAAAAAEC42PTQ+CMAyG/0vPCxElGrly92LihXGYUOYSYGQfEDX8dzvABG92l75Pu75tDm8OvTCiRYfGckhzAh1JSjkIg0JVHBiHQTR+gUmyO9DbxNxQN0JSPT5SLo32/dxsn9ZhGy2DorJR2DkOE/vT5byJH5fTPy4FC8dJvPg2/FnVVb2CRbxnmx0GhWOpK3xob3HE+2wm0WXEbutSzngk6nSmfZif1qKxgSgrlf3qCYoPRDowgFYBAAA=";  
          URL url = new URL(strURL);  
          
          HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();  
          InputStreamReader input = new InputStreamReader(httpConn  
                  .getInputStream(), "utf-8");  
          BufferedReader bufReader = new BufferedReader(input);  
          String line = "";  
          StringBuilder contentBuf = new StringBuilder();  
          while ((line = bufReader.readLine()) != null) {  
              contentBuf.append(line);  
          }  
          String buf = contentBuf.toString();  
//          System.out.println("captureHtml()的结果：\n" + buf);  
    }
    
    @RequestMapping(value="getTreeArea")
	public void getTreeArea(HttpServletResponse response){
		List<City> citys=cityService.getisModelList("Y");
		List<Map>maps=new ArrayList<Map>();
		Map<String, Object>params=new HashMap<String, Object>();
		if(null!=citys && citys.size()>0){
			for(City city:citys){
				Map<String, String> map=new HashMap<String, String>();
				map.put("id", city.getId());
				map.put("name", city.getName());
				map.put("number", city.getNumber());
				map.put("pid", null);
				map.put("cityId", city.getId());
				map.put("parentAreaId", null);
				map.put("cityName", city.getName());
				maps.add(map);
				params.put("cityId", city.getId());
				params.put("areaType", AreaTypeEnum.REGION);
				List<Area>areas=queryExecutor.execQuery("com.wuyizhiye.basedata.code.dao.AreaDao.select", params, Area.class);
				if(null!=areas && areas.size()>0){
					for(Area area:areas){
						Map<String, String> areaMap=new HashMap<String, String>();
						areaMap.put("id", area.getId());
						areaMap.put("name", area.getName());
						areaMap.put("number", area.getNumber());
						areaMap.put("pid", city.getId());
						areaMap.put("cityId", city.getId());
						areaMap.put("parentAreaId", area.getId());
						areaMap.put("cityName", city.getName());
						maps.add(areaMap);
					}
				}
			}
		}
		outPrint(response, JSONArray.fromObject(maps));
	}

}
