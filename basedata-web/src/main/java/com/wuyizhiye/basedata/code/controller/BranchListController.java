package com.wuyizhiye.basedata.code.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.TreeEntity;
import com.wuyizhiye.basedata.bank.model.Branch;
import com.wuyizhiye.basedata.bank.service.BranchService;
import com.wuyizhiye.framework.base.controller.TreeListController;

/**
 * @ClassName BranchListController
 * @Description 网点银行ListController
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="trade/branch/*")
public class BranchListController extends TreeListController {
	@Autowired
	private BranchService branchService;

	@Override
	protected String getTreeDataMapper() {
		return "com.wuyizhiye.basedata.bank.BranchDao.getChild";
	}

	@Override
	protected String getSimpleTreeDataMapper() {
		return "com.wuyizhiye.basedata.bank.BranchDao.getSimpleTreeData";
	}

	@Override
	protected CoreEntity createNewEntity() {
		String parentId = getString("parent");
		Branch branch = new Branch();
		if(!StringUtils.isEmpty(parentId)){
			Branch parent = branchService.getEntityById(parentId);
			branch.setParent(parent);
		}
		return branch;
	}

	@Override
	protected String getListView() {
		return "trade/bank/branchList";
	}

	@Override
	protected String getEditView() {
		return "trade/bank/branchEdit";
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.bank.BranchDao.select";
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected BaseService getService() {
		return branchService;
	}
	
	/**
	 * ${base}/trade/bank/branchDataPicker?multi=true为多选
	 * @param model
	 * @return
	 */
	@RequestMapping(value="branchDataPicker")
	public String branchDataPicker(ModelMap model){
		Map<String,String> param = getParamMap();
		Set<String> keys = param.keySet();
		for(String s : keys){
			model.put(s, param.get(s));
		}
		return "trade/bank/branchDataPicker";
	}
	
	/**
	 * 延迟加载方式获取树数据
	 * @param response
	 */
	@RequestMapping(value="branchDataTree")
	public void treeData(HttpServletResponse response){
		String root = getString("root");
		String parent = getString("parent");
		String includeChild = getString("includeChild");
		String bankId = getString("bankId","");
		Map<String,Object> param = getTreeFilterParam();
		CoreEntity pare = null;
		
		if(!StringUtils.isEmpty(bankId)){
			param.put("bankId", bankId);
		}
		if(!StringUtils.isEmpty(parent)){
			pare = getTreeService().getEntityById(parent);
			if(pare!=null){
				if(!StringUtils.isEmpty(includeChild) && Boolean.getBoolean(includeChild)==true){
					param.put("includeChild", includeChild);
					param.put("longNumber", ((TreeEntity)pare).getLongNumber());
				}else{
					param.put("parent", pare.getId());
				}
			}
			List<Branch> Branchs = queryExecutor.execQuery(getTreeDataMapper(), param,Branch.class);
			String result = JSONArray.fromObject(Branchs, getDefaultJsonConfig()).toString();
			outPrint(response, result);
		}else{
			if(!StringUtils.isEmpty(root)){
				List<Branch> Branchs = new ArrayList<Branch>();
				Branch Branch = branchService.getEntityById(root);
				Branchs.add(Branch);
				outPrint(response, JSONArray.fromObject(Branchs, getDefaultJsonConfig()).toString());
			}else{
				List<Branch> branchs = queryExecutor.execQuery(getSimpleTreeDataMapper(), param,Branch.class);
				String result = JSONArray.fromObject(branchs, getDefaultJsonConfig()).toString();
				outPrint(response, result);
			}
		}
		
	}
	
	/**
	 * dataPicker取数据
	 */
	@RequestMapping(value="branchData")
	public void branchData(Pagination<?> pagination,HttpServletResponse response){
		Map<String,Object> param = getListDataParam();
		if(param.get("parent")==null && param.get("longNumber")==null){
			param.put("parent", "nodata");
		}
		String bankId = getString("id");
		param.put("bankId", bankId);
		pagination = queryExecutor.execQuery(getListMapper(), pagination, param);
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	
	

}
