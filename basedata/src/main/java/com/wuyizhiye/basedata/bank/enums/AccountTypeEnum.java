package com.wuyizhiye.basedata.bank.enums;

/**
 * @ClassName AccountTypeEnum
 * @Description 资金账户类型
 * @author li.biao
 * @date 2015-4-2
 */
public enum AccountTypeEnum {
	BANK("银行"),
	CASH("现金"),
	CASH_FOREIGN("现金(外币)"),
    BANK_PUBLIC_COST("银行对公(费用类)"),
    BANK_PRIVATE_COST("银行对私(费用类)"),
    BANK_PRIVATE_REFUND("银行对私(费用还款)"),
    BANK_PUBLIC_BUSINESS("银行对公(业务类)"),
    BANK_PRIVATE_BUSINESS("银行对私(业务类)"),
    BANK_PRIVATE("银行对私(备用金)"),
    BANK_PUBLIC_CHECK("银行对公(支票进账)"),
	POS("银行对公(POS机)");
    private AccountTypeEnum(String desc) {
    	this.desc = desc;
    }

    private String desc;

    public String getDesc() {
	return desc;
    }
   
    public void setDesc(String desc){
    	this.desc=desc;
    }
}
