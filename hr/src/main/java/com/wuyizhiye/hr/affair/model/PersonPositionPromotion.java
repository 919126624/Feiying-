package com.wuyizhiye.hr.affair.model;

import com.wuyizhiye.hr.enums.PositionChangeTypeEnum;

/**
 * 晋升model
 * @author ouyangyi
 * @since 2013-4-12 上午09:39:18
 */
public class PersonPositionPromotion extends PositionHistoryBill {

	@Override
	public String getChangeType() {
		 
		return PositionChangeTypeEnum.PROMOTION.getValue();
	}

}
