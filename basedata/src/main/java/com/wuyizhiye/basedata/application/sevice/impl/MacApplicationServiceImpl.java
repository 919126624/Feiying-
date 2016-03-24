package com.wuyizhiye.basedata.application.sevice.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.access.model.MacAddress;
import com.wuyizhiye.basedata.access.service.MacAddressService;
import com.wuyizhiye.basedata.application.dao.MacApplicationDao;
import com.wuyizhiye.basedata.application.model.MacApplication;
import com.wuyizhiye.basedata.application.sevice.MacApplicationService;

/**
 * @ClassName MacApplicationServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="macApplicationService")
@Transactional
public class MacApplicationServiceImpl extends BaseServiceImpl<MacApplication> implements MacApplicationService {
			
		@Autowired
		private MacAddressService macAddressService;
	
		@Autowired
		private MacApplicationDao macApplicationDao;
		
		@Override
		protected BaseDao getDao() {
			return macApplicationDao;
		}

		/**
		 * 保存Mac地址的同时更新审批状态
		 * @param mad
		 * @param mal
		 */
		@Override
		public void saveMacAddressAndApplication(MacAddress mad,
				MacApplication mal) {
			macAddressService.addEntity(mad);
			this.updateEntity(mal);
		}
			
}
