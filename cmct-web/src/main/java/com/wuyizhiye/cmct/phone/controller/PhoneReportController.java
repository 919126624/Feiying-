package com.wuyizhiye.cmct.phone.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.service.OrgService;
import com.wuyizhiye.basedata.person.enums.PositionChangeTypeEnum;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.util.OrgUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.cmct.phone.enums.PhoneMemberUseEnum;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName PhoneReportController
 * @Description 话务统计
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping("cmct/phoneReport/*")
public class PhoneReportController extends ListController{
	
	@Autowired
	private OrgService orgService;

	@Override
	protected CoreEntity createNewEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getListView() {
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		put("showMonth",df.format(new Date()).substring(0, 7));
		put("showDay",df.format(new Date()));
		put("startDay",(df.format(new Date()).substring(0, 7)+"-01").replaceAll("-", "/"));
		put("endDay",df.format(new Date()).replaceAll("-", "/"));
		return "cmct/phone/phoneReport";
	}
	
	public void put(String key,Object o){
		this.getRequest().setAttribute(key, o);
	}
	
	protected JsonConfig getDefaultJsonConfig(){
		JsonConfig config = new JsonConfig();
		//日期
		config.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof Date){
					return sdf.format(value);
				}
				return null;
			}
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof Date){
					return sdf.format(value);
				}
				return null;
			}
		});
		//话务类型
		config.registerJsonValueProcessor(PhoneMemberUseEnum.class, new JsonValueProcessor() {
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof PhoneMemberUseEnum){
					JSONObject json = new JSONObject();
					json.put("name", ((PhoneMemberUseEnum)value).getName());
					json.put("value", ((PhoneMemberUseEnum)value).getValue());
					return json;
				}
				return null;
			}
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof PhoneMemberUseEnum){
					return ((PhoneMemberUseEnum)value).getName();
				}
				return null;
			}
		});
		return config;
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("getData")
	@ResponseBody
	public void getData(HttpServletResponse response){
		Map<String,Object> json=new HashMap<String, Object>();
		Map<String,Object> param=new HashMap<String, Object>();//参数
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		String orgId=getString("orgId");//组织ID
		boolean flag=false;//标示 是否 页面第一次 初始数据
		if(null == orgId || "".equals(orgId)){
			orgId=SystemUtil.getCurrentOrg().getId();
			flag=true;
		}
		String dateType=getString("dateType");//类型
    	String showMonth=getString("showMonth");//月份
    	String showDay=getString("showDay");//天
		String startTime=getString("startTime");
		String endTime=getString("endTime");
		String start="";
        String end="";
        if("month".equals(dateType)){//按月查
        	if(null != showMonth && !"".equals(showMonth)){
        		start=showMonth+"-01";
            	end=getMonthEnd(showMonth);
            	param.put("suffix", "_"+showMonth.replace("-", "_"));
        	}
        	
        }else if("day".equals(dateType)){//按天查
        	if(null != showDay && !"".equals(showDay)){
        		start=showDay;
            	end=showDay;
            	param.put("suffix", "_"+showDay.substring(0,7).replace("-", "_"));
        	}
        }else if("period".equals(dateType)){//按期间查
        	if(null != startTime && !"".equals(startTime)){
        		start=startTime.replaceAll("/", "-");
        	}
        	if(null != endTime && !"".equals(endTime)){
        		end=endTime.replaceAll("/", "-");
        	}
        }
        try {
        	if(!"".equals(start)){
        		param.put("startTime", df.parse(start));
        	}
			if(!"".equals(end)){
				param.put("endTime", DateUtil.getNextDay(df.parse(end)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Org org=this.orgService.getEntityById(orgId);
		List<Map> result=new ArrayList<Map>();
		if(!flag){//第一次加载数据  求和 不需要读人员数据
			List<Map> plist=this.getPersonData(org, param);
			if(null != plist && plist.size()>0){
				result.addAll(plist);
			}
		}
		List<Map> olist=this.getOrgData(org, param,flag);
		if(null != olist && olist.size()>0){
			result.addAll(olist);
		}
		/**
		 * 将 通话时长 单位由秒转变成分钟
		 */
		for(Map map:result){
			/**
			 * 添加每行  平均通话时长
			 */
			setVal(map);
			setPercent(map,"PERSON_FOUR");
			setPercent(map,"PERSON_FIVE");
			setPercent(map,"PERSON_SIX");
			setPercent(map,"PERSON_SEVEN");
			setMinute(map,"PERSON_THREE");//总通话时长
			setMinute(map,"PERSON_EIGHT");//总通话时长
		}
		json.put("showList", result);
		outPrint(response,JSONObject.fromObject(json, getDefaultJsonConfig()));
	}
	/**
	 * 设置各个列占百分比
	 * @param map
	 * @param key
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setPercent(Map map,String key){
		Double total=Double.parseDouble(map.get("PERSON_TWO").toString());//总成功通数
		Double num=Double.parseDouble(map.get(key).toString());
		if(total != 0){
			String percent=getNumberFormat((num/total)*100)+"%";
			map.put(key, map.get(key).toString()+"("+percent+")");
		}
	}
	
	public static String getNumberFormat(Double obj){
		  DecimalFormat df=new DecimalFormat("###,##0.00");
		  return df.format(obj);
	  }
	/**
	 * 设置平均通话时长
	 * @param map
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setVal(Map map){
		int total=Integer.parseInt(map.get("PERSON_THREE").toString());//总时长
		int num=Integer.parseInt(map.get("PERSON_TWO").toString());//总通数
		if(num != 0){
			map.put("PERSON_EIGHT", total/num);//平均时长
		}else{
			map.put("PERSON_EIGHT", 0);//平均时长
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setMinute(Map map,String key){
		int count=Integer.parseInt(map.get(key).toString());
		String result="";
		if(count>=60){
			result=count/60+"分"+count%60+"秒";
		}else{
			result=count+"秒";
		}
		map.put(key, result);
	}
	
	public String getMinute(Integer count){
		String result="";
		if(count>=60){
			result=count/60+"分"+count%60+"秒";
		}else{
			result=count+"秒";
		}
		return result;
	}
	
	public String getMonthEnd(String str){
		 Calendar c = Calendar.getInstance();
		  SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM");
		  try {
			c.setTime(format.parse(str));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		  c.add(Calendar.MONTH, -1);//用于 获取上/ 下一个月
		//得到一个月最后一天日期(31/30/29/28)
		  int MaxDay=c.getActualMaximum(Calendar.DAY_OF_MONTH);
		  //按你的要求设置时间
		  c.set( c.get(Calendar.YEAR), c.get(Calendar.MONTH), MaxDay, 23, 59, 59);
		  //按格式输出
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		   return sdf.format(c.getTime()); //该月最后一天
	}
	
	public static void main(String[] args) {
		float a=112;
//		System.out.println(a/60);
	}
	
	/**
	 * 查找人员数据
	 * @param orgId
	 * @param start
	 * @param end
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> getPersonData(Org org,Map<String,Object> param){
		/**
		 * 先取出该组织下 所有人员
		 */
		param.put("orgId", org.getId());
		param.put("isPrimary", "yes");//查询主要职位
		param.put("changeTypeNotIn", "'"+PositionChangeTypeEnum.LEAVE.getValue()+"','"+PositionChangeTypeEnum.DELRUNDISK.getValue()+"'");
		List<Person> plist=queryExecutor.execQuery("com.wuyizhiye.fastsale.dao.AchieveTargetDao.getHistoryPersonByOrg", param, Person.class);
		param.put("personIds", getPersonIds(plist));
		List<Map> mlist=queryExecutor.execQuery("com.wuyizhiye.cmct.phone.dao.PhoneDialRecordDao.getCallPersonData", param, Map.class);
		for(Map map:mlist){
			map.put("URL", "no");
			map.put("ORG_NUMBER", map.get("ORG_LONGNUMBER"));
			map.put("ORG_LONGNUMBER", org.getLongNumber()+"_"+map.get("ORG_LONGNUMBER"));
			map.put("ORG_LEVEL", org.getLevel()+3);
		}
		return mlist;
	}
	
	/**
	 * 查找下级组织数据
	 * @param orgId
	 * @param param
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public List<Map> getOrgData(Org org,Map<String,Object> param,boolean flag){
		Map<String,Object> oparam=new HashMap<String, Object>();
		oparam.put("parentid", org.getId());
		oparam.put("begDate", param.get("startTime"));
		oparam.put("endDate", param.get("endTime"));
		/**
		 * 取下级组织
		 */
		List<Map> result=new ArrayList<Map>();
		List<Org> olist=new ArrayList<Org>();
		if(!flag){
			olist=OrgUtils.getHistoryOrgList(oparam, false);
		}else{
			olist.add(org);
		}
		if(null != olist && olist.size()>0){//有下级组织
			oparam.remove("parentid");//移除父节点
			oparam.put("longnumber", org.getLongNumber());//根据该组织的长编码 找到所有的下级组织
			List<Org> child=OrgUtils.getHistoryOrgList(oparam, false);
			param.put("orgIds", getOrgIds(child));
			List<Map> temp=queryExecutor.execQuery("com.wuyizhiye.cmct.phone.dao.PhoneDialRecordDao.getCallOrgData", param, Map.class);
			for(Org o:olist){
				Map<String,Object> one=new HashMap<String, Object>();
				one.put("ORG_NAME", o.getName());
				one.put("ORG_NUMBER", o.getNumber());
				one.put("ORG_ID", o.getId());
				one.put("ORG_LONGNUMBER", o.getLongNumber());
				one.put("ORG_LEVEL", o.getLevel());
				one.put("URL", "yes");//有点击查看下级 图标  
				int first=0;
				int two=0;
				int three=0;
				int four=0;
				int five=0;
				int six=0;
				int seven=0;
				for(Map m:temp){
					if(m.get("ORG_LONGNUMBER").toString().contains(o.getLongNumber())){//是该组织的下级
						if(null != m.get("PERSON_ONE")){
							first+=Integer.parseInt(m.get("PERSON_ONE").toString());
						}
						if(null != m.get("PERSON_TWO")){
							two+=Integer.parseInt(m.get("PERSON_TWO").toString());
						}
						if(null != m.get("PERSON_THREE")){
							three+=Integer.parseInt(m.get("PERSON_THREE").toString());
						}
						if(null != m.get("PERSON_FOUR")){
							four+=Integer.parseInt(m.get("PERSON_FOUR").toString());
						}
						if(null != m.get("PERSON_FIVE")){
							five+=Integer.parseInt(m.get("PERSON_FIVE").toString());
						}
						if(null != m.get("PERSON_SIX")){
							six+=Integer.parseInt(m.get("PERSON_SIX").toString());
						}
						if(null != m.get("PERSON_SEVEN")){
							seven+=Integer.parseInt(m.get("PERSON_SEVEN").toString());
						}
					}
				}
				one.put("PERSON_ONE", first);
				one.put("PERSON_TWO", two);
				one.put("PERSON_THREE", three);
				one.put("PERSON_FOUR", four);
				one.put("PERSON_FIVE", five);
				one.put("PERSON_SIX", six);
				one.put("PERSON_SEVEN", seven);
				result.add(one);
			}
		}
		return result;
	}
	
	/**
	 * 拼组织ID
	 * @param olist
	 * @return
	 */
	public String getOrgIds(List<Org> olist){
		String str="(";
		for(Org o:olist){
			str+="'"+o.getId()+"',";
		}
		if(!"(".equals(str)){
			str=str.substring(0, str.length()-1)+")";
		}else{
			str="('')";
		}
		return str;
	}
	
	public String getPersonIds(List<Person> olist){
		String str="(";
		for(Person o:olist){
			str+="'"+o.getId()+"',";
		}
		if(!"(".equals(str)){
			str=str.substring(0, str.length()-1)+")";
		}else{
			str="('')";
		}
		return str;
	}
	
	
	@Override
	protected String getEditView() {
		return null;
	}

	@Override
	protected String getListMapper() {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected BaseService getService() {
		return null;
	}
	
	@RequestMapping(value="exportPhoneReport")
	public void exportPhoneReport(HttpServletResponse response)throws IOException {
		Map<String,Object> param=new HashMap<String, Object>();//参数
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		String dateType=getString("dateType");//类型
    	String showMonth=getString("showMonth");//月份
    	String showDay=getString("showDay");//天
		String startTime=getString("startTime");
		String endTime=getString("endTime");
		String start="";
        String end="";
        if("month".equals(dateType)){//按月查
        	if(null != showMonth && !"".equals(showMonth)){
        		start=showMonth+"-01";
            	end=getMonthEnd(showMonth);
            	param.put("suffix", "_"+showMonth.replace("-", "_"));
        	}
        }else if("day".equals(dateType)){//按天查
        	if(null != showDay && !"".equals(showDay)){
        		start=showDay;
            	end=showDay;
            	param.put("suffix", "_"+showDay.substring(0,7).replace("-", "_"));
        	}
        }else if("period".equals(dateType)){//按期间查
        	if(null != startTime && !"".equals(startTime)){
        		start=startTime.replaceAll("/", "-");
        	}
        	if(null != endTime && !"".equals(endTime)){
        		end=endTime.replaceAll("/", "-");
        	}
        }
        try {
        	if(!"".equals(start)){
        		param.put("startTime", df.parse(start));
        	}
			if(!"".equals(end)){
				param.put("endTime", DateUtil.getNextDay(df.parse(end)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		response.setContentType("application/octet-stream");
		String fileName = "话务量报表.xlsx";
        response.addHeader("content-disposition", "attachment; filename=" +URLEncoder.encode(fileName, "utf-8") + "");
        OutputStream os = response.getOutputStream();
        try{
        	SXSSFWorkbook  workBook = new SXSSFWorkbook(); 
        	 
    		Sheet sheet = workBook.createSheet("话务量报表");
    		Row hd = sheet.createRow(0);
    		Cell cell=hd.createCell(0);
    		
    		XSSFCellStyle alignStyle = (XSSFCellStyle)workBook.createCellStyle();   
    		alignStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER); 
    		
    		Font ztFont = workBook.createFont();  
        	ztFont.setFontHeightInPoints((short)18);
        	ztFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
    		alignStyle.setFont(ztFont);
    		
    		cell.setCellValue(start+"至"+end);
    		
    		cell.setCellStyle(alignStyle);   
    		 
    		sheet.addMergedRegion(new CellRangeAddress(0,0,(short)0,(short)7)); //项目名称跨列
    		sheet.autoSizeColumn(1, true);
    		
    		String[] title=new String[]{"分公司","部门","组别","带客业务员","总拨打通数","成功通数","总时长","0-20秒通数","20秒-1分钟通数","1分钟-3分钟通数","3分钟以上通数","平均通话时长","小计(成功通数)","合计(成功通数)","总计(成功通数)"};
    		Row tr = sheet.createRow(1);
    		
    		XSSFCellStyle ts = (XSSFCellStyle)workBook.createCellStyle();   
    		ts.setAlignment(XSSFCellStyle.ALIGN_CENTER); 
    		Font tf = workBook.createFont();  
    		
    		tf.setFontHeightInPoints((short)11);
    		tf.setBoldweight(Font.BOLDWEIGHT_BOLD);
    		ts.setFont(tf);
    		for(int i=0;i<title.length;i++){
    			Cell tc=tr.createCell(i);
    			tc.setCellValue(title[i]);
    			tc.setCellStyle(ts);   
    		}
		
    		List<Map> list=this.queryExecutor.execQuery("com.wuyizhiye.cmct.phone.dao.PhoneDialRecordDao.exportPhoneReport", param, Map.class);
    		if(list!=null && list.size()>0){
    			String[] values=new String[]{"forgName","porgName","orgName","personName","COUNT_ONE","COUNT_TWO","COUNT_THREE","COUNT_FOUR","COUNT_FIVE","COUNT_SIX","COUNT_SEVEN","COUNT_EIGHT"};
    			String orgName="";//组别
    			String porgName="";//部门
    			String forgName="";//分公司
    			int org_row=2;
    			int porg_row=2;
    			int forg_row=2;
    			
    			double org_count=0;//小计
    			double porg_count=0;//合计
    			double forg_count=0;//总计
    			
    			for(int i=0;i<list.size();i++){
    				Map m=list.get(i);
    				Row row = sheet.createRow(i+2);
    				for(int n=0;n<values.length;n++){
    					Cell tc=row.createCell(n);
    					if(values[n].indexOf("COUNT_")>=0){
    						if(values[n].equals("COUNT_THREE")){
    							tc.setCellValue(getMinute(m.get(values[n])==null?0:new Integer(m.get(values[n]).toString())));
    						}else{
    							tc.setCellValue(m.get(values[n])==null?0:new Double(m.get(values[n]).toString()));
    						}
    					}else{
    						tc.setCellValue(m.get(values[n])==null?"":m.get(values[n]).toString());
    					}
    				}
    				 
    				Cell a=row.createCell(12);
    				Cell b=row.createCell(13);
    				Cell c=row.createCell(14);
    				
    				String c_forgName=i!=list.size()-1?(list.get(i+1).get("forgName")==null?"":list.get(i+1).get("forgName").toString()):"";
    				String c_porgName=i!=list.size()-1?(list.get(i+1).get("porgName")==null?"":c_forgName+list.get(i+1).get("porgName").toString()):"";
    				String c_orgName=i!=list.size()-1?(list.get(i+1).get("orgName")==null?"":c_porgName+list.get(i+1).get("orgName").toString()):"";
    			 
    					
					forgName=m.get("forgName")==null?"":m.get("forgName").toString();
					porgName=m.get("porgName")==null?"":forgName+m.get("porgName").toString();
					orgName=m.get("orgName")==null?"":porgName+m.get("orgName").toString();
    					
					org_count=org_count+(m.get("COUNT_TWO")==null?0:new Double(m.get("COUNT_TWO").toString()));
					porg_count=porg_count+(m.get("COUNT_TWO")==null?0:new Double(m.get("COUNT_TWO").toString()));
					forg_count=forg_count+(m.get("COUNT_TWO")==null?0:new Double(m.get("COUNT_TWO").toString()));
					 
				 if(!orgName.equals(c_orgName)){
					 if(null!=sheet.getRow(org_row)){
						 sheet.getRow(org_row).getCell(12).setCellValue(org_count); 
					 }
					if(org_row!=i+2){
						sheet.addMergedRegion(new CellRangeAddress(org_row,i+2,(short)2,(short)2)); //组别
						sheet.addMergedRegion(new CellRangeAddress(org_row,i+2,(short)12,(short)12)); //小计
					}
					org_row=i+3;
					org_count=0;
				 } 
					
				 if(!porgName.equals(c_porgName)){
					 if(null!=sheet.getRow(porg_row)){
						 sheet.getRow(porg_row).getCell(13).setCellValue(porg_count); 
					 }
					
					if(porg_row!=i+2){
						sheet.addMergedRegion(new CellRangeAddress(porg_row,i+2,(short)1,(short)1)); //部门
						sheet.addMergedRegion(new CellRangeAddress(porg_row,i+2,(short)13,(short)13)); //合计
					 }
					porg_row=i+3;
					porg_count=0;
				 }
				 if(!forgName.equals(c_forgName)){
					 if(null!=sheet.getRow(forg_row)){
						 sheet.getRow(forg_row).getCell(14).setCellValue(forg_count); 
					 }
					 
					 if(forg_row!=i+2){
						 sheet.addMergedRegion(new CellRangeAddress(forg_row,i+2,(short)0,(short)0)); //分公司
						 sheet.addMergedRegion(new CellRangeAddress(forg_row,i+2,(short)14,(short)14)); //总计
					 }
					 forg_row=i+3;
					 forg_count=0;
				}
    				 
    			}
    			
    		}
    	 sheet.setColumnWidth(0, 5100);
    	 sheet.setColumnWidth(1, 3500);
    	 sheet.setColumnWidth(2, 4600);
    	 sheet.setColumnWidth(3, 3200);
    	 for(int j=4;j<=14;j++){//设置宽度
    		 sheet.setColumnWidth(j, 3500);
    	 }
    	 workBook.write(os);
        }catch(Exception e){
        	e.printStackTrace();
        	os.write(e.getMessage().getBytes());
        }
		os.close();
	
	}
}
