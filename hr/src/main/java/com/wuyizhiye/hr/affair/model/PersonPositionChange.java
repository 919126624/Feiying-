package com.wuyizhiye.hr.affair.model;

import com.wuyizhiye.hr.enums.PositionChangeTypeEnum;

public class PersonPositionChange extends PositionHistoryBill {

	@Override
	public String getChangeType() {
		 
		return PositionChangeTypeEnum.TRANSFER.getValue();
	}

}
