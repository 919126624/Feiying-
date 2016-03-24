package com.wuyizhiye.hr.affair.model;

import com.wuyizhiye.hr.enums.PositionChangeTypeEnum;

/**
 * 降职model
 * @author ouyangyi
 * @since 2013-4-12 上午09:38:58
 */
public class PersonPositionDemotion extends PositionHistoryBill {

	@Override
	public String getChangeType() {
		 
		return PositionChangeTypeEnum.DEMOTION.getValue();
	}

}
