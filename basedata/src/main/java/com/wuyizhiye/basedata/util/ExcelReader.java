package com.wuyizhiye.basedata.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import java.util.Random;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.wuyizhiye.base.util.StringUtils;

/**
 * @ClassName ExcelReader
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public class ExcelReader {
	private static Logger logger=Logger.getLogger(ExcelReader.class);
	/**
	 * 根据绝对路径获取excel数据
	 * @param pathName excel 绝对路径
	 * @return.0
	
	 */
	public static List<String[]> getExcelData(String pathName , int startRowNum){
		
		File f = new File(pathName);
		List<String[]> listCell = new ArrayList<String[]>();
		try {
			FileInputStream is = new FileInputStream(f);
			HSSFWorkbook wbs = new HSSFWorkbook(is);
			HSSFSheet childSheet = wbs.getSheetAt(0);
			for (int j = startRowNum; j < childSheet.getLastRowNum(); j++) {
				childSheet.getNumMergedRegions();
				HSSFRow row = childSheet.getRow(j);
				String [] arr = new String[50];
				if (null != row) {
					for (int k = 0; k < row.getLastCellNum(); k++) {
						HSSFCell cell = row.getCell(k);
						//单元格数据
						if(k<50){
							arr[k] = getCellValue(cell);
						}
					}
				}
				listCell.add(arr);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return listCell;
	}
	
	
	public static void main(String[] args) throws Exception {
		
//		readErrorInventoryRecord("E:/库存更正数据1.xls");
//		outPutExcel("2015-12","E:/12月考勤_.xls","E:/12月份考勤_res.xls");
//		getPersonCount("E:/非凡之星加班餐补统计表-9月份1111 .xls");
//		printUpdateStorageSql("E:/storage1.xls");
		
//		printUpdateGoodsCategorySql("C:/Users/Administrator/Desktop/修正商品类目/today.xls");
	}
	
	
	public static void printUpdateGoodsCategorySql(String filePath){
		List<String[]> rlist=getExcelData(filePath,1);
//		String sql="";
//		String check="GD011857,GD012804,GD013678,GD011564,GD006909,GD000797,GD011815,GD011194,GD011192,GD011671,GD011851,GD011622,GD011165,GD012678,GD011776,GD011560,GD011878,GD011868,GD013415,GD011809,GD011189";
		 int i=0;
		 FileWriter fileWriter=null;
		 try {
			 fileWriter=new FileWriter("E:\\sql.txt", true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(String[] list:rlist){
			i++;
//			System.out.println(list[0]);
//			boolean flag=true;
//			for(String c:check.split(",")){
//				if(c.equals(list[0])){
//					flag=false;
//				}
//			}
//			if(flag){
//				System.out.println(list[0]);
//			}
//			sql+="'"+list[0]+"',";
			String sql="update t_ebbase_goods set FKGOODSCATEGORYID=(select fid from t_ebbase_goodscategory where FNUMBER='"+list[1]+"') where FBARCODE='"+list[2]+"';";
//			try {
//				fileWriter.write(sql+"\r\n");
//			} catch (IOException e) {
//			}
			System.out.println(sql);
//			System.out.println(list[0]+","+list[1]+","+list[2]);
		}
		
	}
	
	/**
	 * 打印 更新库存 表sql
	 *  随机 生成  库存可用数
	 * @param filePath
	 */
	public static void printUpdateStorageSql(String filePath){
		List<String[]> rlist=getExcelData(filePath,1);
		Random ran=new Random();
		for(String[] list:rlist){
			int tmp=ran.nextInt(100);
			String str="update t_storage_inventory set FALLCOUNT="+tmp+",FABLECOUNT="+tmp+",FOCCUPYCOUNT=0 where FKSTORAGEID='"+list[0]+"';";
			System.out.println(str);
		}
	}
	
	/**
	 * 列出 excel表中 人 出现次数
	 * @param filePath
	 */
	public static void getPersonCount(String filePath){
		List<String[]> rlist=getExcelData(filePath,2);
		Map<String,Object> res=new HashMap<String, Object>();
		for(String[] s:rlist){
			String name=s[3].trim();
			if(!res.containsKey(name)){
				res.put(name, 1);
			}else{
				res.put(name, (Integer)res.get(name)+1);
			}
		}
//		System.out.println(res);
	}
	
	public static void readErrorInventoryRecord(String filePath){
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<String[]> rlist=getExcelData(filePath,0);
		String refno="CORRECTINVENTORYREC";
		//备注: e8948c60-071f-48ea-b442-5a39badd24df 是管理员id
		for(String[] addr:rlist){
			int curAbel=Integer.parseInt(addr[0]);//库存表 可用数
			int recAbel=Integer.parseInt(addr[1]);//库存记录表 累计可用数
			int curOccu=Integer.parseInt(addr[2]);//库存表 占用数
			int recOccu=Integer.parseInt(addr[3]);//库存记录表 占用数
			int curAll=Integer.parseInt(addr[4]);//库存表 总库存数
			int recAll=Integer.parseInt(addr[5]);//库存记录表 总库存数
			String sql = "INSERT INTO T_EBBASE_INVENTORYRECORD VALUES ('"
					+ UUID.randomUUID().toString() + "', '" + addr[7]
					+ "', '"+refno+"', " + (curAbel - recAbel) + ", "
					+ (curOccu - recOccu) + ", " + (curAll - recAll) + ", '"
					+ df.format(new Date())
					+ "', 'e8948c60-071f-48ea-b442-5a39badd24df', '" + addr[6]
					+ "', 'IN_STORAGE');";
			System.out.println(sql);
		}
	}
	
	/**
	 * 考勤信息统计
	 * @param month
	 * @param filePath
	 * @param outPath
	 * @throws Exception
	 */
	public static void outPutExcel(String month,String filePath,String outPath) throws Exception{
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<String[]> rlist=getExcelData(filePath,1);
		List<Map<String,Object>> mlist=new ArrayList<Map<String,Object>>();
		Map<String,Object> nameMap=new HashMap<String, Object>();

		for(String[] sl:rlist){
			String number=sl[2];
			String time=sl[3].replaceAll("/", "-");
			time=df.format(df.parseObject(time));
			String today=time.substring(0, 10);
			boolean flag=false;
			for(Map<String,Object> m:mlist){
				if(number.equals(m.get("number")) && today.equals(m.get("today"))){//当天 有存记录
					flag=true;
					String firstTime=m.get("firstTime").toString();//第一打卡时间
					String lastTime=m.get("lastTime").toString();//最后打卡时间
					if(df.parse(time).compareTo(df.parse(firstTime))<0){//比最早打卡时间 小  
						m.put("firstTime", time);
					}
					if(df.parse(time).compareTo(df.parse(lastTime))>0){//比最晚打卡时间大
						m.put("lastTime", time);
					}
				}
			}
			
			//记录所有打卡人员
			
			if(!nameMap.containsKey(number)){
				nameMap.put(number, sl[1]);
			}
			if(!flag){
				Map<String,Object> obj=new HashMap<String, Object>();
				obj.put("company", sl[0]);//公司
				obj.put("name", sl[1]);//姓名
				obj.put("number", sl[2]);//工号
				obj.put("firstTime", time);//打卡时间
				obj.put("lastTime", time);//打卡时间
				obj.put("today", today);
				obj.put("xingqi", getStrByInt(dayForWeek(today)));
				obj.put("jiqihao", sl[4]);
				obj.put("bianhao", sl[5]);
				obj.put("bidui", sl[6]);
				obj.put("leibie", sl[7]);
				mlist.add(obj);
			}
		}
		//验证数据
		validateOneDayTwoDifRecords(mlist,df);
		List<Map<String,Object>> reslist=new ArrayList<Map<String,Object>>();
		int maxDay=getOneMonth(Integer.parseInt(month.split("-")[1]));//获取九月份最大天数
		for(String number:nameMap.keySet()){//循环所有人员
			for(int i=1;i<=maxDay;i++){//循环一个月所有天数
				String today=month+"-"+i;
				if(i<10){
					today=month+"-0"+i;
				}
				int dayWeek=dayForWeek(today);
				Map<String,Object> res1=new HashMap<String, Object>();//上班打卡记录
				Map<String,Object> res2=new HashMap<String, Object>();//下班打卡记录
				res1.put("todayStr", today+"("+getStrByInt(dayWeek)+")");//今天日期
				res2.put("todayStr", today+"("+getStrByInt(dayWeek)+")");//今天日期
				
				boolean flag=false;//标示当天是否有打卡记录
				Map<String,Object> todayMap=null;
				for(Map<String,Object> mo:mlist){
					if(number.equals(mo.get("number"))){
						if(today.equals(mo.get("today"))){//当天有打卡记录
							flag=true;
							todayMap=mo;
						}
					}
				}
				boolean weekend=true;//周末未打卡不记录
				if(flag){//当天有打卡记录
					res1.putAll(todayMap);
					res2.putAll(todayMap);
					//一天只有一次打卡记录的  显示出来
					if(todayMap.get("firstTime").equals(todayMap.get("lastTime"))){
						oneToFiveSameRecords(df, todayMap, today, res1, res2, dayWeek);
					}else{//两次打卡记录不一样
						oneToFiveDifRecords(df, todayMap, today, res1, res2, dayWeek);
					}
				}else{//当天没有打卡记录
					if(dayWeek>5){//周末没有打卡记录 不 显示
						weekend=false;
					}
					//今天没有打卡记录
					res1.put("name", nameMap.get(number));
					res1.put("number", number);
					res2.put("name", nameMap.get(number));
					res2.put("number", number);
					res1.put("time_one", "");//当天首次打卡记录
					res2.put("time_one", "");//当天第二次打卡记录
					res1.put("remark", "没有打卡记录!");//当天首次打卡记录
					res2.put("remark", "没有打卡记录!");//当天第二次打卡记录
				}
				if(weekend){
					reslist.add(res1);
					reslist.add(res2);
				}
			}
		}
		//计算迟到罚款金额
		calcLateMoney(reslist);
		
		String[] headers={"日期","部门","姓名","登记号码","打卡时间","机器号","编号","对比方式","备注","打卡时间-小时","迟到罚款"};
		String[] properties={"todayStr","company","name","number","time_one","jiqihao","bianhao","bidui","remark","time_two","late_price"};
		OutputStream os = new FileOutputStream(outPath); 
		ExcelExportUtil.exportMap(headers, properties, reslist, os);
	}
	
	/**
	 * 验证 一天 有两条不同时间打卡记录
	 *    去除 早上打两次卡 下午未打卡
	 *     以及 早上未打卡 晚上打两次卡 情况
	 * @param mlist
	 * @throws ParseException 
	 */
	public static void validateOneDayTwoDifRecords(List<Map<String,Object>> mlist,DateFormat df) throws ParseException{
		for(Map<String,Object> map:mlist){
			if(!map.get("firstTime").equals(map.get("lastTime"))){//两次打卡记录 时间不一样
				//第一次打卡记录在12点之后
				if(df.parse(map.get("firstTime").toString()).compareTo(df.parse(map.get("today").toString()+" 12:00:00"))>0){
					map.put("firstTime", map.get("lastTime"));
				}
				//第二次打卡记录在12点之前
                if(df.parse(map.get("lastTime").toString()).compareTo(df.parse(map.get("today").toString()+" 12:00:00"))<0){
                	map.put("lastTime", map.get("firstTime"));
				}
				
			}
		}
	}
	
	
	/**
	 * 周一至周五 两次打卡记录不一致
	 * @param df
	 * @param todayMap
	 * @param today
	 * @param res1
	 * @param res2
	 * @param dayWeek
	 * @throws ParseException
	 */
	public static void oneToFiveDifRecords(DateFormat df,
			Map<String, Object> todayMap, String today,
			Map<String, Object> res1, Map<String, Object> res2, int dayWeek) throws ParseException{
		res1.put("time_one", todayMap.get("firstTime"));//当天首次打卡记录
		res2.put("time_one", todayMap.get("lastTime"));//当天第二次打卡记录
		if(dayWeek>5){//周末
			res1.put("remark", getStrByInt(dayWeek)+"加班");
			res2.put("remark", getStrByInt(dayWeek)+"加班");
		}else{
			if(df.parse(todayMap.get("firstTime").toString()).compareTo(df.parse(today+" 09:00:00"))>0){
				res1.put("remark", "早上迟到!");
			}
			
			if(df.parse(todayMap.get("lastTime").toString()).compareTo(df.parse(today+" 18:00:00"))<0){
				if(df.parse(todayMap.get("lastTime").toString()).compareTo(df.parse(today+" 09:00:00"))<0){
					res2.put("remark", "下班忘打卡!");
				}else{
					res2.put("remark", "下班早退!");
				}
			}
			
			if(df.parse(todayMap.get("lastTime").toString()).compareTo(df.parse(today+" 21:00:00"))>0){
				res2.put("remark", "晚上加班");
			}
		}
	}
	
	/**
	 * 计算周一到周五 只有一次打卡记录情况
	 * @param df
	 * @param todayMap
	 * @param today
	 * @param res1
	 * @param res2
	 * @param dayWeek
	 * @throws ParseException
	 */
	public static void oneToFiveSameRecords(DateFormat df,
			Map<String, Object> todayMap, String today,
			Map<String, Object> res1, Map<String, Object> res2, int dayWeek)
			throws ParseException {
		//只有一次打卡记录  以中午12点为间隔点  12点之前 记录到第一次打卡记录
		if(df.parse(todayMap.get("firstTime").toString()).compareTo(df.parse(today+" 12:00:00"))<0){
			res1.put("time_one", todayMap.get("firstTime"));//当天首次打卡记录
			res2.put("time_one", "");
		}else{
			res2.put("time_one", todayMap.get("firstTime"));//当天首次打卡记录
			res1.put("time_one", "");
		}
		
		if(dayWeek>5){
			res1.put("remark", getStrByInt(dayWeek)+"加班");
			//只有一次打卡记录,且在12点之前
			if(df.parse(todayMap.get("firstTime").toString()).compareTo(df.parse(today+" 12:00:00"))<0){
				res1.put("remark", getStrByInt(dayWeek)+"加班,下班忘打卡!");
			}else{
				res2.put("remark", getStrByInt(dayWeek)+"加班,上班忘打卡!");
			}
			
		}else{
			//一天只有一条打卡记录,且打卡时间是 12点之前
			if(df.parse(todayMap.get("firstTime").toString()).compareTo(df.parse(today+" 12:00:00"))<0){
				if(df.parse(todayMap.get("firstTime").toString()).compareTo(df.parse(today+" 09:00:00"))<0){//如果时间早于上班时间
					res2.put("remark", "下班忘打卡!");
				}else{
					res1.put("remark", "早上迟到!");
					res2.put("remark", "下班忘打卡!");
				}
			}else{
				//一天只有一次打卡记录,且打卡时间 在 12点到18点之前
				if(df.parse(todayMap.get("firstTime").toString()).compareTo(df.parse(today+" 18:00:00"))<0){//
					res1.put("remark", "上班忘打卡!");
					res2.put("remark", "下班早退!");
				}else{
					res1.put("remark", "上班忘打卡!");
					if(df.parse(todayMap.get("firstTime").toString()).compareTo(df.parse(today+" 21:00:00"))>0){//晚上九点之后 算加班
						res2.put("remark","晚上加班");
					}
				}
			}
			
		}
	}
	
    /**
     * 周末打卡记录统计
     * @param df
     * @param todayMap
     * @param today
     */
	public static void weekendDataCalc(DateFormat df, Map<String, Object> todayMap, String today){
		
	}
	
	//计算迟到金额
	public static void calcLateMoney(List<Map<String,Object>> reslist) throws ParseException{
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(Map<String,Object> map:reslist){
        	String time_one=map.get("time_one").toString();
        	
        	if(!StringUtils.isEmpty(time_one)){
        		map.put("time_two", time_one.substring(0, 13));
        	}else{
        		map.put("time_two", "");
        	}
        	String todayStr=map.get("todayStr").toString();
        	if(!todayStr.contains("星期六") && !todayStr.contains("星期日")){
        		if(!StringUtils.isEmpty(time_one)){
        			String today=time_one.substring(0, 10);
        			//九点到九点10分之间
            		if(df.parse(time_one).compareTo(df.parse(today+ " 09:00:00"))>0 && 
            				df.parse(time_one).compareTo(df.parse(today+ " 09:10:00"))<0){
            			map.put("late_price", 10);
            		}else if(df.parse(time_one).compareTo(df.parse(today+ " 09:10:00"))>0 && 
            				df.parse(time_one).compareTo(df.parse(today+ " 09:30:00"))<0){
            			map.put("late_price", 20);
            		}else if(df.parse(time_one).compareTo(df.parse(today+ " 09:30:00"))>0 && 
            				df.parse(time_one).compareTo(df.parse(today+ " 10:00:00"))<0){
            			map.put("late_price", 50);
            		}else if(df.parse(time_one).compareTo(df.parse(today+ " 10:00:00"))>0 && 
            				df.parse(time_one).compareTo(df.parse(today+ " 12:00:00"))<0){
            			map.put("late_price", "请假半天");
            		}
        		}
        	}else{
        		map.put("late_price", "");
        	}
        	
        }
        
        
	}
	
	//获取某一个月最大天数
	public static int getOneMonth(int m){
		Calendar cal = Calendar.getInstance();  
        //下面可以设置月份，注：月份设置要减1，所以设置1月就是1-1，设置2月就是2-1，如此类推  
        cal.set(Calendar.MONTH, m-1);  
        int MaxDay=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return MaxDay;
	}
	
	public static int dayForWeek(String pTime) throws Exception {  
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
		 Calendar c = Calendar.getInstance();  
		 c.setTime(format.parse(pTime));  
		 int dayForWeek = 0;  
		 if(c.get(Calendar.DAY_OF_WEEK) == 1){  
		  dayForWeek = 7;  
		 }else{  
		  dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;  
		 }  
		 return dayForWeek;  
	}
	
	public static String getStrByInt(int i){
		String res="";
		switch (i) {
		case 1:
			res="星期一";
			break;
		case 2:
			res="星期二";
			break;
		case 3:
			res="星期三";
			break;
		case 4:
			res="星期四";
			break;
		case 5:
			res="星期五";
			break;
		case 6:
			res="星期六";
			break;
		case 7:
			res="星期日";
			break;
		}
		return res;
	}
	

	
	
	/**
	 * 根据文件流获取excel数据
	 * @param is
	 * @param startRowNum
	 * @return
	 */
	public static List<String[]> getExcelData(InputStream is , int startRowNum){
			
			List<String[]> listCell = new ArrayList<String[]>();
			try {
				HSSFWorkbook wbs = new HSSFWorkbook(is);
				HSSFSheet childSheet = wbs.getSheetAt(0);
				for (int j = startRowNum; j <= childSheet.getLastRowNum(); j++) {
					childSheet.getNumMergedRegions();
					HSSFRow row = childSheet.getRow(j);
					String [] arr = new String[50];
					// System.out.println("有列数" + row.getLastCellNum());
					if (null != row) {
						for (int k = 0; k < row.getLastCellNum(); k++) {
							HSSFCell cell = row.getCell(k);
							//单元格数据
							if(k <= 49){
								arr[k] = getCellValue(cell);
							}
						}
					}
					listCell.add(arr);
				}
			} catch (Exception e) {
				logger.error("", e);
			}
			return listCell;
		}

	/**
	 * 根据文件流获取excel数据
	 * @param is
	 * @param startRowNum
	 * @param istrim是否去除前后空格
	 * @return
	 */
	public static List<String[]> getExcelData(InputStream is , int startRowNum,boolean is2003,int istrim){
			
			List<String[]> listCell = new ArrayList<String[]>();
			try {
				Workbook wbs = null ;
				if(is2003){
					wbs = new HSSFWorkbook(is);
				}else{
					wbs = new XSSFWorkbook(is);
				}
				Sheet childSheet = wbs.getSheetAt(0);
				for (int j = startRowNum; j <= childSheet.getLastRowNum(); j++) {
					childSheet.getNumMergedRegions();
					Row row = childSheet.getRow(j);
					String [] arr = new String[50];
					if (null != row) {
						for (int k = 0; k < row.getLastCellNum(); k++) {
							Cell cell = row.getCell(k);
							//单元格数据
							arr[k] = getCellValue(cell,istrim);
							if(k==49){
								break;
							}
						}
					}
					listCell.add(arr);
				}
			} catch (Exception e) {
				logger.error("", e);
			}
			return listCell;
		}
	
	/**
	 * 根据文件流获取excel数据
	 * @param is
	 * @param startRowNum
	 * @return
	 */
	public static List<String[]> getExcelData(InputStream is , int startRowNum,boolean is2003){
			
			List<String[]> listCell = new ArrayList<String[]>();
			try {
				Workbook wbs = null ;
				if(is2003){
					wbs = new HSSFWorkbook(is);
				}else{
					wbs = new XSSFWorkbook(is);
				}
				Sheet childSheet = wbs.getSheetAt(0);
				for (int j = startRowNum; j <= childSheet.getLastRowNum(); j++) {
					childSheet.getNumMergedRegions();
					Row row = childSheet.getRow(j);
					String [] arr = new String[50];
					if (null != row) {
						for (int k = 0; k < row.getLastCellNum(); k++) {
							Cell cell = row.getCell(k);
							//单元格数据
							if(k <= 49){
								arr[k] = getCellValue(cell);
							}
						}
					}
					listCell.add(arr);
				}
			} catch (Exception e) {
				logger.error("", e);
			}
			return listCell;
		}
	
	public static Workbook getBook(FileInputStream is) throws IOException{
		Workbook book = null;
		try {
			book = new XSSFWorkbook(is);
		}catch (Exception e) {
			POIFSFileSystem fs = new POIFSFileSystem(is);
			book = new HSSFWorkbook(fs);
		}
		return book;
	}
	
	/**
	 * 根据绝对路径获取excel数据
	 * @param pathName excel 绝对路径
	 * @return
	 */
	public static List<String[]> getExcelData(FileInputStream is , int startRowNum,Boolean is2003excle){
		if(is2003excle){
			return getExcelData2003(is, startRowNum);
		}
		List<String[]> listCell = new ArrayList<String[]>();
		Workbook book = null;
		try {
			book = getBook(is);
			Sheet childSheet = book.getSheetAt(0);
			for (int j = startRowNum; j <= childSheet.getLastRowNum(); j++) {
				Row row = childSheet.getRow(j);
				String [] arr = new String[50];
				if (null != row) {
					for (int k = 0; k < row.getLastCellNum(); k++) {
						if(k>=50){
							break;
						}
						Cell cell = row.getCell(k);
						//单元格数据
						arr[k] = getCellValue2Pio(cell);
					}
				}
				listCell.add(arr);
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
		return listCell;
	}
	
	/**
	 * 根据绝对路径获取excel数据
	 * @param startRowNum 从哪一行开始
	 * @return
	 */
	public static List<String[]> getExcelData2003(FileInputStream is , int startRowNum){
		List<String[]> listCell = new ArrayList<String[]>();
		try {
			HSSFWorkbook wbs = new HSSFWorkbook(is);
			HSSFSheet childSheet = wbs.getSheetAt(0);
//			System.out.println("有行数" + childSheet.getLastRowNum());
			for (int j = startRowNum; j <= childSheet.getLastRowNum(); j++) {
				childSheet.getNumMergedRegions();
				HSSFRow row = childSheet.getRow(j);
				String [] arr = new String[50];
				// System.out.println("有列数" + row.getLastCellNum());
				if (null != row) {
					for (int k = 0; k < row.getLastCellNum(); k++) {
						if(k>=50){
							break;
						}
						HSSFCell cell = row.getCell(k);
						//单元格数据
						if(k <= 49){
							arr[k] = getCellValue(cell);
						}
					}
				}
				listCell.add(arr);
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
		return listCell;
	}
	
	public static List<String[]> getExcelData2007(FileInputStream is , int startRowNum){
		List<String[]> listCell = new ArrayList<String[]>();
		try {
			XSSFWorkbook wbs = new XSSFWorkbook(is); 
			XSSFSheet childSheet = wbs.getSheetAt(0);
			for (int j = startRowNum; j <= childSheet.getLastRowNum(); j++) {
				childSheet.getNumMergedRegions();
				XSSFRow row = childSheet.getRow(j);
				String [] arr = new String[50];
				if (null != row) {
					for (int k = 0; k < row.getLastCellNum(); k++) {
						if(k>=50){
							break;
						}
						XSSFCell cell = row.getCell(k);
						//单元格数据
						arr[k] = getCellValue2007(cell);
					}
				}
				listCell.add(arr);
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
		return listCell;
	}

	/**
	 * 获取单元格的值
	 * @param cell 单元
	 * @param istrim 选择是否去除空格
	 * @return 字符串
	 */
	public static String getCellValue(Cell cell,int istrim ){
		String value = null;
		if (null != cell) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC: // 数字
				if (HSSFDateUtil.isCellDateFormatted(cell)) {   
			        //  如果是date类型则 ，获取该cell的date值   
					value = HSSFDateUtil.getJavaDate(cell.getNumericCellValue()).toString();   
			    } else { // 纯数字   
			    	DecimalFormat df = new DecimalFormat("0.##"); 
			    	value = df.format(cell.getNumericCellValue()); 
			    }
				break;
			case Cell.CELL_TYPE_STRING: // 字符串
				value = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_BOOLEAN: // Boolean
				value = cell.getBooleanCellValue()+"";
				break;
			case Cell.CELL_TYPE_FORMULA: // 公式
				value = cell.getCellFormula();
				break;
			case Cell.CELL_TYPE_BLANK: // 空值
				break;
			case Cell.CELL_TYPE_ERROR: // 故障
				break;
			default:
				break;
			}
		} else {
			
		}
		if(StringUtils.isEmpty(value)){
			return null;
		}
		if(istrim==1){
			return value.trim();
		}else{
			return value;
		}
	}
	/**
	 * 获取单元格的值
	 * @param cell 单元
	 * @return 字符串
	 */
	public static String getCellValue(Cell cell ){
		String value = null;
		if (null != cell) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC: // 数字
				if (HSSFDateUtil.isCellDateFormatted(cell)) {   
			        //  如果是date类型则 ，获取该cell的date值   
					value = HSSFDateUtil.getJavaDate(cell.getNumericCellValue()).toString();   
			    } else { // 纯数字   
			    	DecimalFormat df = new DecimalFormat("0.##"); 
			    	value = df.format(cell.getNumericCellValue());
			    }
				break;
			case Cell.CELL_TYPE_STRING: // 字符串
				value = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_BOOLEAN: // Boolean
				value = cell.getBooleanCellValue()+"";
				break;
			case Cell.CELL_TYPE_FORMULA: // 公式
				value = cell.getCellFormula();
				break;
			case Cell.CELL_TYPE_BLANK: // 空值
				break;
			case Cell.CELL_TYPE_ERROR: // 故障
				break;
			default:
				break;
			}
		} else {
			
		}
		if(StringUtils.isEmpty(value)){
			return null;
		}
		return value.trim();
	}
	
	/**
	 * 获取单元格的值
	 * @param cell 单元
	 * @return 字符串
	 */
	public static String getCellValue2007(XSSFCell cell ){
		String value = null;
		if (null != cell) {
			// System.out.print(cell.getColumnIndex());
			switch (cell.getCellType()) {
			case HSSFCell.CELL_TYPE_NUMERIC: // 数字
				
				if (HSSFDateUtil.isCellDateFormatted(cell)) {   
			        //  如果是date类型则 ，获取该cell的date值   
					value = HSSFDateUtil.getJavaDate(cell.getNumericCellValue()).toString();   
			    } else { // 纯数字   
			    	value = String.valueOf(cell.getNumericCellValue());   
			    }
				break;
			case HSSFCell.CELL_TYPE_STRING: // 字符串
				value = cell.getStringCellValue();
				break;
			case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
				value = cell.getBooleanCellValue()+"";
				break;
			case HSSFCell.CELL_TYPE_FORMULA: // 公式
				value = cell.getCellFormula();
				break;
			case HSSFCell.CELL_TYPE_BLANK: // 空值
//				System.out.println("   ");
				break;
			case HSSFCell.CELL_TYPE_ERROR: // 故障
//				System.out.println(" 故障 ");
				break;
			default:
//				System.out.print("未知类型   ");
				break;
			}
		} else {
//			System.out.print("   ");
		}
		return value;
	}
	
	/**
	 * 获取单元格的值
	 * @param cell 单元
	 * @return 字符串
	 */
	public static String getCellValue2Pio(Cell cell){
		String value = null;
		if (null != cell) {
			// System.out.print(cell.getColumnIndex());
			switch (cell.getCellType()) {
			case HSSFCell.CELL_TYPE_NUMERIC: // 数字
				
				if (HSSFDateUtil.isCellDateFormatted(cell)) {   
			        //  如果是date类型则 ，获取该cell的date值   
					value = HSSFDateUtil.getJavaDate(cell.getNumericCellValue()).toString();   
			    } else { // 纯数字   
			    	value = String.valueOf(cell.getNumericCellValue());   
			    }
				break;
			case HSSFCell.CELL_TYPE_STRING: // 字符串
				value = cell.getStringCellValue();
				break;
			case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
				value = cell.getBooleanCellValue()+"";
				break;
			case HSSFCell.CELL_TYPE_FORMULA: // 公式
				value = cell.getCellFormula();
				break;
			case HSSFCell.CELL_TYPE_BLANK: // 空值
//				System.out.println("   ");
				break;
			case HSSFCell.CELL_TYPE_ERROR: // 故障
//				System.out.println(" 故障 ");
				break;
			default:
//				System.out.print("未知类型   ");
				break;
			}
		} else {
//			System.out.print("   ");
		}
		return value;
	}
}
