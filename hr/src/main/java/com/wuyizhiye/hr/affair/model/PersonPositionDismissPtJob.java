package com.wuyizhiye.hr.affair.model;

import com.wuyizhiye.hr.enums.PositionChangeTypeEnum;

/**
 * 撤职model
 * @author ouyangyi
 * @since 2013-4-12 上午09:38:58
 */
public class PersonPositionDismissPtJob extends PositionHistoryBill {

	@Override
	public String getChangeType() {
		 
		return PositionChangeTypeEnum.DISMISS_PARTTIMEJOB.getValue();
	}

}
