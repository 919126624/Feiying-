package com.wuyizhiye.hr.affair.model;

import com.wuyizhiye.hr.enums.PositionChangeTypeEnum;

/**
 * 兼职model
 * @author ouyangyi
 * @since 2013-4-12 上午09:38:58
 */
public class PersonPositionIncreasePtJob extends PositionHistoryBill {

	@Override
	public String getChangeType() {
		 
		return PositionChangeTypeEnum.INCREASE_PARTTIMEJOB.getValue();
	}

}
