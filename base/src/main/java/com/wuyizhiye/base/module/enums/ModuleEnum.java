package com.wuyizhiye.base.module.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ModuleEnum
 * @Description 系统模块
 * @author li.biao
 * @date 2015-4-1
 */
public enum ModuleEnum {
	
	//平台类
	BASEDATA(BusinessTypeEnum.TYHY,"001","basedata","基础平台"),
	INTERFLOW(BusinessTypeEnum.TYHY,"002","interflow","办公平台"),
	//BLOG(BusinessTypeEnum.TYHY,"003","blog","企业微博"),
	CMCT(BusinessTypeEnum.TYHY,"004","cmct","通讯平台"),
	MOBILE(BusinessTypeEnum.TYHY,"005","mobile.oa","移动办公"),
	
	//人力资源类
	HR_AFFAIR(BusinessTypeEnum.TYHY,"006","hr.affair","人事流程"),
	
	HR_ATTENDANCE(BusinessTypeEnum.TYHY,"007","hr.attendance","考勤管理"),
	//HR_SALARY(BusinessTypeEnum.TYHY,"008","hr.salary","薪酬管理"),
	//HR_CONTRACT(BusinessTypeEnum.TYHY,"009","hr.contract","合同社保"),
	//HR_PERF(BusinessTypeEnum.TYHY,"010","hr.perf","绩效管理"),
	//HR_TRAIN(BusinessTypeEnum.TYHY,"011","hr.train","培训管理"),
	//HR_RECRUIT(BusinessTypeEnum.TYHY,"012","hr.recruit","招聘管理"),
	
	//财务类
	/*
	COST(BusinessTypeEnum.TYHY,"013","cost","费用管理"),	
	FIXEDASSETS(BusinessTypeEnum.TYHY,"014","fixedassets","资产管理"),	
	FINA_FUNDS(BusinessTypeEnum.TYHY,"015","fina.funds","资金管理"),	
	FINA_BUDGET(BusinessTypeEnum.TYHY,"016","fina.budget","预算管理"),
	*/
	WEBSITE(BusinessTypeEnum.TYHY,"017","website","网站平台"),
	
	//中介业务类XXXX(BusinessTypeEnum.INTERM,"代号","包名","项目名") 
	/*
	BROKER(BusinessTypeEnum.FDCZJ,"101","broker","盘客管理"), 
	AGENCY(BusinessTypeEnum.FDCZJ,"102","agency","项目代理"),
	FASTSALE(BusinessTypeEnum.FDCZJ,"103","fastsale","新房快销"),
	TRADE(BusinessTypeEnum.FDCZJ,"104","trade","交易管理"),
	TRANFER(BusinessTypeEnum.FDCZJ,"105","transfer","过户管理"),
	NOTE(BusinessTypeEnum.FDCZJ,"106","note","票据资料"),
	SETTLEMENT(BusinessTypeEnum.FDCZJ,"107","settlement","结算提成"),
	PROFITSHARING(BusinessTypeEnum.FDCZJ,"108","profitsharing","利润分红"),
	BUSICENTER(BusinessTypeEnum.FDCZJ,"109","busicenter","业绩中心"),
	BUNK(BusinessTypeEnum.FDCZJ,"110","bunk","铺位管理"),
	RELATION(BusinessTypeEnum.FDCZJ,"111","relation","客户关系"),
	MARKETANALYSIS(BusinessTypeEnum.FDCZJ,"112","marketanalysis","市场分析"),
	BI(BusinessTypeEnum.FDCZJ,"113","bi","数据分析"),
	DISTRIBUTIONSALE(BusinessTypeEnum.FDCZJ,"114","distributionsale","分销管理"),
	MOBILEFASTSALE(BusinessTypeEnum.FDCZJ,"115","mobile.fastsale","移动快销"),
	M_PROPERTYCUSTOMER(BusinessTypeEnum.FDCZJ,"116","mobile.broker","移动盘客"),
	M_BI(BusinessTypeEnum.FDCZJ,"117","mobile.bi","移动BI"),
	*/
	//房产电商
	EBHOUSE(BusinessTypeEnum.FDCZJ,"118","ebhouse","房产电商"),
	
	//DATADEPOT(BusinessTypeEnum.FDCZJ,"119","datadepot","数据仓库"),
	
	//地产物业
	/*
	INVITEMERCHANT(BusinessTypeEnum.DCWY,"301","InviteMerchant","招商管理"),
	LEASEMANAGEMENT(BusinessTypeEnum.DCWY,"302","LeaseManagement","租赁管理"),
	PROPERTYSERVICE(BusinessTypeEnum.DCWY,"303","PropertyService","物业服务"),
	*/
	
	//动迁
	/*
	CQZS(BusinessTypeEnum.DONGQ,"401","dongqian.cqzs","动迁管理"),
	CQXP(BusinessTypeEnum.DONGQ,"402","dongqian.cqxp","新盘选房"),
	CQRH(BusinessTypeEnum.DONGQ,"403","dongqian.cqrh","入户管理"),
	CQCW(BusinessTypeEnum.DONGQ,"404","dongqian.cqcw","财务结算"),
	CQBI(BusinessTypeEnum.DONGQ,"405","dongqian.cqbi","统计分析"),
	*/
	
	//借贷管理
	
	WLJD(BusinessTypeEnum.JRZJ,"602","p2p","网络借贷"),
	//LENDCONTRACT(BusinessTypeEnum.JRZJ,"603","lendcontract","合同管理"),
	//CUSTOMERMARKETING(BusinessTypeEnum.JRZJ,"601","customermarketing","客户营销"),
	
	
	//软件行业
	PROJECTM(BusinessTypeEnum.RJHY,"701","projectm","项目管理"),
	BAGE(BusinessTypeEnum.RJHY,"702","bage","白菜电话"),
	LUNCH(BusinessTypeEnum.RJHY,"703","lunch","乐盒饭"),
	
	EBBASE(BusinessTypeEnum.RJHY,"704","ebbase","电商基础"),
	EBSTORAGE(BusinessTypeEnum.RJHY,"705","ebstorage","电商仓储"),
	EBSITE(BusinessTypeEnum.RJHY,"706","ebsite","电商网站"),
	EBFINANCE(BusinessTypeEnum.RJHY,"707","ebfinance","电商财务"),
	EBBI(BusinessTypeEnum.RJHY,"708","ebbi","电商报表"),
	
	//保险中介
	//EBCAR(BusinessTypeEnum.BXZJ,"801","EBCAR","汽车金融") ,
	
	//经纪圈
	ECONOMICCIRCLE(BusinessTypeEnum.RJHY,"901","middlemen","经纪圈"),
	;
	private BusinessTypeEnum parent;
	private String pkg;
	private String name;
	private String number ;
	private ModuleEnum(BusinessTypeEnum parent,String number ,String pkg,String name){
		this.parent = parent;
		this.number = number ;
		this.pkg = pkg;
		this.name = name;
	}
	public String getPkg() {
		return pkg;
	}
	public String getName() {
		return name;
	}
	public BusinessTypeEnum getParent() {
		return parent;
	}
	public String getNumber() {
		return number;
	}
	
	/**
	 * 转LIST
	 * @return
	 */
	public static List<Map<String, String>> toList(){
		ModuleEnum[] ary = ModuleEnum.values();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for(int i=0;i<ary.length;i++){
			Map<String,String> map = new HashMap<String,String>();
			map.put("value", ary[i].toString());
			map.put("name", ary[i].name);
			map.put("parent", ary[i].parent.toString());
			list.add(map);
		}
		return list;
	}
	
	/**
	 * 获得人事行政模块集合  孙海涛 2014-10-11
	 * @return
	 */
	public static List<ModuleEnum> getHrModules(){
		List<ModuleEnum> hrList=new ArrayList<ModuleEnum>();
		hrList.add(ModuleEnum.HR_AFFAIR);
		hrList.add(ModuleEnum.HR_ATTENDANCE);
		//hrList.add(ModuleEnum.HR_SALARY);
		//hrList.add(ModuleEnum.HR_CONTRACT);
		//hrList.add(ModuleEnum.HR_PERF);
		//hrList.add(ModuleEnum.HR_RECRUIT);
		return hrList;
	}
	
	/**
	 * 获得财务模块集合  孙海涛 2014-10-11
	 * @return
	 */
	public static List<ModuleEnum> getCostModules(){
		List<ModuleEnum> costList=new ArrayList<ModuleEnum>();
		//costList.add(ModuleEnum.COST);
		//costList.add(ModuleEnum.FIXEDASSETS);
		//costList.add(ModuleEnum.FINA_FUNDS);
		//costList.add(ModuleEnum.FINA_BUDGET);
		return costList;
	}
}
