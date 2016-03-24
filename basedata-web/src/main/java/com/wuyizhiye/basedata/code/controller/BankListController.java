package com.wuyizhiye.basedata.code.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.bank.model.BaseBank;
import com.wuyizhiye.basedata.bank.model.Branch;
import com.wuyizhiye.basedata.bank.service.BaseBankService;
import com.wuyizhiye.basedata.images.model.Photo;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.TreeListController;

/**
 * @ClassName BankListController
 * @Description 基础银行-银行网点设置（新）
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="trade/bank/*")
public class BankListController extends TreeListController {
	
	@Autowired
	private BaseBankService baseBankService;

	@Override
	protected String getTreeDataMapper() {
		return "";
	}

	@Override
	protected String getSimpleTreeDataMapper() {
		return "com.wuyizhiye.basedata.bank.dao.BaseBankDao.selectTree";
	}

	@Override
	protected CoreEntity createNewEntity() {
		
		return null ;
	}

	@Override
	protected String getListView() {
		return "trade/bank/bankList";
	}

	@Override
	protected String getEditView() {
		return "";
	}

	@Override
	protected String getListMapper() {
		return "";
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected BaseService getService() {
		return baseBankService;
	}
	
	/**
	 * 保存银行
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="saveBank")
	public void saveBank(HttpServletRequest request, HttpServletResponse response){
		String type = getString("type","");
		String bankName = getString("bankName","");
		String bankDesc = getString("bankDesc","");
		String bankId = getString("bankId","");
		String photo = getString("photo","");
		String number = getString("number","");
		
		boolean isError = false ;
		if("ADD".equals(type)){//新增，校验银行名字唯一
			Map<String,Object> bankParam = new HashMap<String,Object>();
			bankParam.put("bankName",bankName);
			BaseBank dbBank = queryExecutor.execOneEntity(BaseBank.MAPPER+".selectAllByCond", bankParam, BaseBank.class);
			if(dbBank!=null && dbBank.getId()!=null && !"".equals(dbBank.getId())){//已经存在该名字的银行
				getOutputMsg().put("STATE", "FAIL");
				getOutputMsg().put("MSG", "已经存在该名字的银行，不能重复增加");
				isError = true ;
			}
		}
		if(!isError){
			BaseBank baseBank = new BaseBank();
			baseBank.setBankDesc(bankDesc);
			baseBank.setBankName(bankName);
			baseBank.setLastUpdateTime(new Date());
			baseBank.setUpdator(SystemUtil.getCurrentUser());
			baseBank.setNumber(number);
			if("EDIT".equals(type)){//编辑
				baseBank.setId(bankId);
				baseBankService.updateEntity(baseBank);
				if(photo!=""){
					Map<String,Object> bankParam = new HashMap<String,Object>();
					bankParam.put("id",bankId);
					Photo p = queryExecutor.execOneEntity(BaseBank.MAPPER+".getLogoById", bankParam, Photo.class);
					Map<String, Object> map = new ModelMap();
					map.put("id",UUID.randomUUID().toString());
					map.put("url",photo);
					map.put("orgid", baseBank.getId());
					if(p==null){
						queryExecutor.executeInsert("com.wuyizhiye.basedata.bank.dao.BaseBankDao"+".insertLogoUrl", map);
					}else{
						queryExecutor.executeUpdate(BaseBank.MAPPER+".updataLogoUrl", map);
					}
				}
			}else{//新增
				baseBank.setUUID();
				baseBank.setCreateTime(new Date());
				baseBank.setCreator(SystemUtil.getCurrentUser());
				baseBankService.addEntity(baseBank);
				if(photo!=""){
					Map<String, Object> map = new ModelMap();
					map.put("id",UUID.randomUUID().toString());
					map.put("url",photo);
					map.put("orgid", baseBank.getId());
					queryExecutor.executeInsert("com.wuyizhiye.basedata.bank.dao.BaseBankDao"+".insertLogoUrl", map);
				}
			}
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("MSG", "操作成功");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	/**
	 * 查找银行
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="selectBankById")
	public void selectBankById(HttpServletRequest request,HttpServletResponse response){
		String bankId = getString("bankId","");
		Map<String,Object> bankParam = new HashMap<String,Object>();
		bankParam.put("id",bankId);
		BaseBank dbBank = queryExecutor.execOneEntity(BaseBank.MAPPER+".selectAllByCond", bankParam, BaseBank.class);
		Photo photo = queryExecutor.execOneEntity(BaseBank.MAPPER+".getLogoById", bankParam, Photo.class);
		if(photo!=null){
			dbBank.setPhotoPath(photo.getPath());
		}
		outPrint(response, JSONObject.fromObject(dbBank, getDefaultJsonConfig()));
	}
	
	/**
	 * 删除
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="delete")
	public void delete(@RequestParam(required=true,value="id")String id,HttpServletResponse response){
		
		CoreEntity entity = getService().getEntityById(id);
		if(entity!=null){
			if(isAllowDelete(entity)){
				
				//校验，存在子节点，不允许删除
				Map<String,Object> param = new HashMap<String,Object>();
				param.put("bankId", id);
				List<Branch> list = queryExecutor.execQuery("com.wuyizhiye.basedata.bank.BranchDao.selectByBank", param, Branch.class);
				if(list!=null && list.size()>0){
					getOutputMsg().put("STATE", "FAIl");
					getOutputMsg().put("MSG", "该银行存在引用，不允许删除");
				}else{
					queryExecutor.executeDelete(BaseBank.MAPPER+".selectAllByCond", param);
					queryExecutor.executeDelete(BaseBank.MAPPER+".deleteLogo", param);
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

	/**
	 * 查找网点数据
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="selectBranchList")
	public void selectBranchList(Pagination<Branch> pagination,HttpServletRequest request,HttpServletResponse response){
		String bankId = getString("bankId");
		String bankName = getString("bankName");
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("bankId", bankId);
		param.put("bankName",bankName);
		this.putControlUnitIdToMap(param);
		pagination = queryExecutor.execQuery("com.wuyizhiye.basedata.bank.BranchDao.selectByBank", pagination, param);
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	
}
