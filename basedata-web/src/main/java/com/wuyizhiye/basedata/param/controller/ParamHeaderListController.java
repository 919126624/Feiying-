package com.wuyizhiye.basedata.param.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.module.enums.BusinessTypeEnum;
import com.wuyizhiye.base.module.enums.ModuleEnum;
import com.wuyizhiye.base.module.model.Module;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.param.enums.ParamLevelEnum;
import com.wuyizhiye.basedata.param.enums.ParamTypeEnum;
import com.wuyizhiye.basedata.param.model.ParamHeader;
import com.wuyizhiye.basedata.param.model.ParamLines;
import com.wuyizhiye.basedata.param.service.ParamHeaderService;
import com.wuyizhiye.basedata.param.service.ParamLinesService;
import com.wuyizhiye.basedata.util.Validate;
import com.wuyizhiye.framework.base.controller.ListController;


/**
 * @ClassName ParamHeaderListController
 * @Description 参数设置
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value = "basedata/param/*")
public class ParamHeaderListController extends ListController {

	@Autowired
	private ParamHeaderService paramHeaderService;
	
	@Autowired
	private ParamLinesService paramLinesService;
	
	@Override
	protected CoreEntity createNewEntity() {
		return new ParamHeader();
	}

	@Override
	protected String getListView() {
		getRequest().setAttribute("isModuleType",getString("isModuleType"));
		getRequest().setAttribute("moduleType",getString("moduleType"));
		getRequest().setAttribute("moduleEnumList", ModuleEnum.values());
		return "basedata/param/paramHeaderList";
	}

	@Override
	protected String getEditView() {
		getRequest().setAttribute("paramLevelEnums", ParamLevelEnum.values());
		getRequest().setAttribute("paramTypeEnums", ParamTypeEnum.values());
		getRequest().setAttribute("moduleType", getString("parent"));
		if(getString("id")!=null&&!"".equals(getString("id"))){
			ParamHeader pd = paramHeaderService.getEntityById(getString("id"));
			getRequest().setAttribute("moduleName", ModuleEnum.valueOf(pd.getModule()).getName());
			getRequest().setAttribute("moduleType", pd.getModule());
		}else{
			getRequest().setAttribute("moduleName", ModuleEnum.valueOf(getString("parent")).getName());	
		}
		
		return "basedata/param/paramHeaderEdit";
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.param.ParamHeaderDao.select";
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected BaseService getService() {
		return paramHeaderService;
	}
	
	
	@RequestMapping(value="toParamIndex")
	public String  toParamIndex(){
		getRequest().setAttribute("flag", getString("flag"));
		return "basedata/param/paramIndex";
	}
	
	
	@RequestMapping(value="simpleTreeData")
	public void simpleTreeData(HttpServletResponse response) {
		List<Module>  moduleList =  queryExecutor.execQuery("com.wuyizhiye.base.module.dao.ModuleDao.select",this.getListDataParam(),Module.class);
		List<Map<String,Object>> treeData = new ArrayList<Map<String,Object>>();
		List<BusinessTypeEnum> busset = Validate.getCurrBuinessType();
		for(BusinessTypeEnum e : busset){
			Map<String,Object> m = new HashMap<String, Object>();
			m.put("id", e);
			m.put("name", e.getName());
			m.put("pid", e.getParent());
			treeData.add(m);
			for (int i = 0; i < moduleList.size(); i++) {
				//找出付模块,并且模块必须启动了.才能设置参数
				if(e.equals(moduleList.get(i).getType().getParent()) && moduleList.get(i).isEnable()){
					Map<String,Object> moMap = new HashMap<String, Object>();
					moMap.put("id", moduleList.get(i).getType());
					moMap.put("name",moduleList.get(i).getType().getName());
					moMap.put("pid",moduleList.get(i).getType().getParent());
					treeData.add(moMap);
				}
			}
			
		}
		outPrint(response, JSONArray.fromObject(treeData));
	}
	
	/**
	 * 获取listData
	 * @param pagination
	 * @param response
	 */
	@RequestMapping(value = "listLinesData")
	@ResponseBody
	public Pagination<ParamLines> listLinesData(Pagination<?> pagination, HttpServletResponse response) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("headerid", getString("headerid"));
		return (Pagination<ParamLines>) queryExecutor.execQuery("com.wuyizhiye.basedata.param.dao.ParamLinesDao.select",pagination, param);
		
	}

	
	/**
	 * 删除参数值Lines
	 */
	@RequestMapping(value="deleteLinesRow")
	@ResponseBody
	public void deleteLinesRow(HttpServletResponse response){
		ParamHeader paramheader = paramHeaderService.getEntityById(getString("id"));
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("headerid",paramheader.getId());
		List<ParamLines> lines = queryExecutor.execQuery("com.wuyizhiye.basedata.param.dao.ParamLinesDao.select",param,ParamLines.class);
		if(lines.size()>0){
			for(int i=0;i<lines.size();i++){
			paramLinesService.deleteById(lines.get(i).getId());
			}
		}
		if(paramheader!=null){
			if(isAllowDelete(paramheader)){
				paramHeaderService.deleteEntity(paramheader);
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "删除成功");
			}else{
				getOutputMsg().put("STATE", "FAIl");
			}
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "记录不存在");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	/**
	 * 修改状态
	 * @param lines
	 * @param response
	 */
	@RequestMapping(value="updateStatus")
	@ResponseBody
	public void updateStatus(ParamHeader lines,HttpServletResponse response){
		ParamHeader header= paramHeaderService.getEntityById(lines.getId());
		header.setStatus(lines.getStatus());
		paramHeaderService.updateStatus(header);
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "操作成功");
		outPrint(response, JSONObject.fromObject(getOutputMsg()).toString());
	}
	
	/**
	 * 根据类型查找组织
	 * @param response
	 * @param modelMap
	 */
	@RequestMapping(value="autoComplateOrg")
	@ResponseBody
	public Pagination<Org> autoComplateOrg(HttpServletResponse response,ModelMap modelMap){
		StringBuilder strBuilder = new StringBuilder("");
		String[] orgArr=getSplitArr(getString("orgTypes"));
		for (String o:orgArr) {
			strBuilder.append("'").append(o).append("',");
		}
		if(strBuilder.length() > 1){
			strBuilder = strBuilder.deleteCharAt(strBuilder.length()-1);
		}else{
			strBuilder.append("'nodata'");
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("orgTypes",strBuilder.toString());
		Pagination<Org> orgs =(Pagination<Org>) queryExecutor.execQuery("com.wuyizhiye.basedata.org.dao.OrgDao.getOrgByType", new Pagination<Org>(100, 0),param);
		return orgs;
	}
	
	
	private String[] getSplitArr(String stringArr) {
		if (stringArr == null)
			return null;
		String[] str = stringArr.split(",");
		return str;
	}
	
	@RequestMapping(value="getParamHeaderByNumber")
	@ResponseBody
	public Pagination<ParamHeader> getParamHeaderByNumber(){
		Map<String,Object> paraMap =new HashMap<String,Object>();
		paraMap.put("number", getString("number"));
		Pagination<ParamHeader> paramHeaderItems = queryExecutor.execQuery("com.wuyizhiye.basedata.param.ParamHeaderDao.getByNumber", new Pagination<ParamHeader>(10, 1), paraMap);
		return paramHeaderItems;
	}
}
