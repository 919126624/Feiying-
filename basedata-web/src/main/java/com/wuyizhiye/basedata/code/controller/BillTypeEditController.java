package com.wuyizhiye.basedata.code.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.code.model.BillType;
import com.wuyizhiye.basedata.code.service.BillTypeService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName BillTypeEditController
 * @Description 单据类型编辑控制器
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value = "basedata/billType/*")
public class BillTypeEditController extends EditController {

	@Autowired
	private BillTypeService billTypeService;
	@Override
	protected Class getSubmitClass() {
		return BillType.class;
	}

	@Override
	protected BaseService getService() {
		return billTypeService;
	}
	@Override
	protected String getImagePath() {
		// TODO Auto-generated method stub
		String flag = this.getString("flag");
		if("defalutIcon".equalsIgnoreCase(flag)){
			return "initSystem/process/default";
		}else if("disabledIcon".equalsIgnoreCase(flag)){
			return "initSystem/process/disabled";
		}else if("checkedIcon".equalsIgnoreCase(flag)){
			return "initSystem/process/checked";
		}
		return "initSystem/process/default";
	}

}
