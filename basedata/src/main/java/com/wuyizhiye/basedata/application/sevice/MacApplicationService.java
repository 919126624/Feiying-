package com.wuyizhiye.basedata.application.sevice;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.access.model.MacAddress;
import com.wuyizhiye.basedata.application.model.MacApplication;

/**
 * @ClassName MacApplicationService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface MacApplicationService extends BaseService<MacApplication>{
	/**
	 * 保存Mac地址的同时更新审批状态
	 * @param mad
	 * @param mal
	 */
	void saveMacAddressAndApplication(MacAddress mad,MacApplication mal);

}
