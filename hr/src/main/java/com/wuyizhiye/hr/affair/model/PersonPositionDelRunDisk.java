package com.wuyizhiye.hr.affair.model;

import com.wuyizhiye.hr.enums.PositionChangeTypeEnum;

/**
 * 跑盘员删除model
 * @author ouyangyi
 * @since 2013-4-12 上午09:38:58
 */
public class PersonPositionDelRunDisk extends PositionHistoryBill {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5651827284915849522L;

	@Override
	public String getChangeType() {
		 
		return PositionChangeTypeEnum.DELRUNDISK.getValue();
	}

}
