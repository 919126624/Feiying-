package com.wuyizhiye.basedata.code.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.annotation.Dependence;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.bank.model.BaseBank;
import com.wuyizhiye.basedata.bank.model.Branch;
import com.wuyizhiye.basedata.bank.model.City;
import com.wuyizhiye.basedata.bank.service.BaseBankService;
import com.wuyizhiye.basedata.bank.service.BranchService;
import com.wuyizhiye.framework.base.controller.TreeListController;

/**
 * @ClassName BankBranchListController
 * @Description 网点银行--新版本
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="trade/bankbranch/*")
public class BankBranchListController extends TreeListController {
	@Autowired
	private BranchService branchService;
	
	@Autowired
	private BaseBankService baseBankService ;

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
		String baseBankId = getString("bankId");
		Branch branch = new Branch();
		if(!StringUtils.isEmpty(parentId)){
			Branch parent = branchService.getEntityById(parentId);
			branch.setParent(parent);
		}
		if(!StringUtils.isEmpty(baseBankId)){
			BaseBank baseBank = baseBankService.getEntityById(baseBankId);
			branch.setBaseBank(baseBank);
		}
		return branch;
	}

	@Override
	protected String getListView() {
		return "";
	}

	@Override
	protected String getEditView() {
		
		List<City> cityList = queryExecutor.execQuery("com.wuyizhiye.basedata.bank.dao.CityDao.getCityList", null, City.class);
		getRequest().setAttribute("cityList", cityList);
		return "trade/bank/bankBranchEdit";
	}

	@Override
	protected String getListMapper() {
		return "";
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected BaseService getService() {
		return branchService;
	}
	
	/**
	 * 重写删除
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="delete")
	@Dependence(method="list")
	public void delete(@RequestParam(required=true,value="id")String id,HttpServletResponse response){
		CoreEntity entity = getService().getEntityById(id);
		
		if(entity!=null){
			if(isAllowDelete(entity)){
				
				//存在子节点，则不允许删除
				Map<String,Object> param = new HashMap<String,Object>();
				param.put("byParent", id);
				List<Branch> list = queryExecutor.execQuery("com.wuyizhiye.basedata.bank.BranchDao.selectByBank", param, Branch.class);
				if(list!=null && list.size()>0){
					getOutputMsg().put("STATE", "FAIl");
					getOutputMsg().put("MSG", "该网点存在引用，不允许删除");
				}else{
					getService().deleteEntity(entity);
					getOutputMsg().put("STATE", "SUCCESS");
					getOutputMsg().put("MSG", "删除成功");
				}
			}else{
				getOutputMsg().put("STATE", "FAIl");
			}
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "记录不存在");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}

}
