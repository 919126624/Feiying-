package com.wuyizhiye.basedata.code.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.bank.model.Branch;
import com.wuyizhiye.basedata.bank.service.BranchService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName BankBranchEditController
 * @Description 网上银行编辑--新版本
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="trade/bankbranch/*")
public class BankBranchEditController extends EditController {
	@Autowired
	private BranchService branchService;
	@Override
	protected Class<Branch> getSubmitClass() {
		return Branch.class;
	}
	@Override
	protected Object getSubmitEntity() {
		Branch branch = (Branch) super.getSubmitEntity();
		return branch;
	}
	@Override
	protected BaseService<Branch> getService() {
		return branchService;
	}
}
