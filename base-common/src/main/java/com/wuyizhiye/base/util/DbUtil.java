package com.wuyizhiye.base.util;

import java.util.ArrayList;
import java.util.Date;


/**
 * @ClassName DbUtil
 * @Description 数据库转换函数类
 * @author li.biao
 * @date 2015-4-1
 */
public class DbUtil {

	final public static int DB_SQL = 0;

	final public static int DB_ORA = 1;	

	final public static int DB_DB2 = 2;
	
	final public static int DB_MYSQL = 3;
	
	final static  String DJ_DATEFORMAT = "yyyy-MM-dd";
	
	private static int dbType = 3;
	
	

	public void setDbTypeString(String dbtype) {
		DbUtil.setDbType(dbtype);
	}

	public static void  setDbType(String dbType){	
		if(dbType.equalsIgnoreCase("ora")){
			DbUtil.dbType = DB_ORA;
		}
		if(dbType.equalsIgnoreCase("sql")){
			DbUtil.dbType = DB_SQL;
		}
		if(dbType.equalsIgnoreCase("db2")){
			DbUtil.dbType = DB_DB2;
		}
		
		if(dbType.equalsIgnoreCase("mysql")){
			DbUtil.dbType = DB_MYSQL;
		}
	}

	public static int getDbType() {
		return dbType;
	}
	/**
	 * instr方法函数
	 * @param str1
	 * @param str2
	 * @param iStart
	 * @return
	 */
	public static String sqlInstr(String str1, String str2, int iStart){
		switch(dbType){
		case DB_ORA: 
			return "instr(" + str1 + "," + str2 + (iStart == 0 ? "" : ("," + iStart)) + ")";
		case DB_SQL: 
			return "charindex(" + str1 + "," + str2 + (iStart == 0 ? "" : ("," + iStart)) + ")";
		case DB_DB2: 
			return "locate(" + str1 + "," + str2 + (iStart == 0 ? "" : ("," + iStart)) + ")";
		default: 
			return "";
		}
	}
	public static String sqlInstr(String sStr1, String sStr2){
		return sqlInstr(sStr1, sStr2, 0);
	}
	/**
	 *反向查找字符串出现的位置
	 * @param str1
	 * @param str2
	 * @param iStart
	 * @return
	 */
	public static String sqlInstrRev(String str1,String str2,int iStart){
		switch(dbType){
		case DB_ORA: 
			return "instr(" + str1 + "," + str2 + (iStart == 0 ? "" : ("," + iStart)) + ",-1,1)";
		case DB_SQL: 
			return "len("+str1+") - charindex(Reserve(" + str1 + ")," + str2 + (iStart == 0 ? "" : ("," + iStart)) + ")";
		case DB_DB2: 
			//DB2的反向查扄1�7 尚未实现
			//return "locate(" + str1 + "," + str2 + (iStart == 0 ? "" : ("," + iStart)) + ")";
		default: 
			return "";
		}
	}
	public static String sqlInstrRev(String str1,String str2){
		return sqlInstrRev(str1,str2,0);
	}
	/**
	 * substring	方法函数
	 * @param str
	 * @param iStart
	 * @param iLen
	 * @return
	 */
	public static String sqlSubStr(String str, String iStart, String iLen){
		switch(dbType){
		case DB_SQL: 
			return "substring("+ str +","+ iStart +","+ iLen +")";
		default: 
			return "substr("+ str +","+ iStart +","+ iLen +")";
		}
	}

	/**
	 * substring	方法函数
	 * @param str
	 * @param iStart
	 * @param iLen
	 * @return
	 */
	public static String sqlSubStr(String str, int iStart, int iLen){
		return sqlSubStr( str, String.valueOf(iStart),String.valueOf(iLen));
	}
	  /**left，第二个参数也可用整数*/
	  public static String sqlLeft(String sStr, String sLen){
	      if (dbType == DJCons.DB_ORA)
	         return "SubStr(" + sStr + ",1," + sLen + ")";
	      return "LEFT(" + sStr + "," + sLen + ")";
	   }
	/**
	 * left	方法函数
	 * @param str
	 * @param iLen
	 * @return
	 */
	public static String sqlLeft(String str, int iLen){
		switch(dbType){
		case DB_ORA: 
			return "substr("+ str +",1,"+ iLen +")";
		default: 
			return "left("+ str +","+ iLen +")";
		}
	}
	
	
	/**
	 * right 方法函数
	 * @param str
	 * @param iLen
	 * @return
	 */
	public static String sqlRight(String str, int iLen){
		switch(dbType){
		case DB_ORA: 
			return "DECODE(" + iLen + ",0,'',SUBSTR(" + str + ",-(" + iLen + ")))";
		default: 
			return "right("+ str +","+ iLen +")";
		}
	}
	
	/**right，第二个参数也可用整数*/
   public static String sqlRight(String sStr, String sLen){
   
      if (dbType == DJCons.DB_ORA)
         return "decode(" + sLen + ",0,'',substr(" + sStr + ",-(" + sLen + ")))";
      return "right(" + sStr + "," + sLen + ")";
   }
	/**
	 * len 方法函数
	 * @param str
	 * @return
	 */
	public static String sqlLen(String str){
		switch(dbType){
		case DB_SQL: 
			return "len("+ str +")";
		default: 
			return "length("+ str +")";
		}
	}
	/**
	 *  返回sql语句中的有效日期
	 * @param sDate
	 * @return
	 */
	public static String sqlDate(String sDate){
		return sqlDate(sDate,dbType,false);
	}
	/**
	 * 返回sql语句中的有效日期
	 * @param dDate
	 * @return
	 */
	public static String sqlDate(java.util.Date dDate){		
		return sqlDate(dDate,dbType,false);
	}
	
	/**
	 * 返回适用于sql语句的指定格式的日期字符串，用于字符串类型的日期字段
	 * @param sDate String：日期字段，字符串类型
	 * @param sFormat String：格式/默认为yyyy/MM/dd
	 * @return String
	 */
	public static String sqlDateS(String sDate,String sFormat){
		if(dbType==DJCons.DB_ORA){ return "to_date("+sDate+",'"+sFormat+"')"; }
		if(dbType==DJCons.DB_SQL) return "cast("+sDate+" as datetime)";
		if(dbType==DJCons.DB_DB2) return (sFormat.indexOf(':')>0?"TIMESTAMP":"DATE")+"("+(sFormat.indexOf('/')>0?"replace("+sDate+",'/','-')":sDate)+")";
		return "";
	}

	public static String sqlDateS(String sDate){
		return sqlDateS(sDate,"yyyy-MM-dd");
	}
	
	/**
	 * 返回sql语句中的有效日期 btime=true也保存时闄1�7
	 * @param sDate
	 * @param sFormat
	 * @return
	 */
	public static String sqlToDate(String sDate, boolean bTime){
		return sqlDate( sDate,dbType, bTime);
	}
	
	public static String sqlToDate(java.util.Date dDate, boolean bTime){
		return sqlDate( dDate,dbType, bTime);
	}
	
	private static String sqlDate(String sDate,int dbtype, boolean bTime){
		if (dbtype == DJCons.DB_ORA)
			return ("to_date('" + sDate + "','" +
					DateUtil.GENERAL_FORMATTER + (bTime ? "hh24:mi:ss" : "") + "')");
		if (dbtype == DJCons.DB_SQL)
			return "'" + sDate + "'";
		if (dbtype == DJCons.DB_DB2)
			return (bTime ? "TIMESTAMP" : "DATE") + "('" + sDate + "')";
		return "";		
	}
	/**日期类型参数的sqlDate...btime=true也保存时*/
	private static String sqlDate(java.util.Date dDate, int dbtype, boolean bTime){

		String dformat = DateUtil.GENERAL_FORMATTER + (bTime ? " HH:mm:ss" : "");
		String sDate = DateUtil.dateConvertStr(dDate, dformat);
		return sqlDate( sDate, dbtype,  bTime);

	}
	
	/**
	 * 转换日期为字符串
	 * @param str
	 * @param sFormat
	 * @return
	 */
	public static String sqlDateToChar(String str,String sFormat){
		switch(dbType){
		case DB_ORA: 
			return "to_char(" + str + ",'" + sFormat + "')";
		case DB_SQL: 
			return "case(" + str + " as datetime)";
		case DB_DB2: 
			return (sFormat.indexOf(':') > 0 ? "TIMESTAMP" : "DATE") + "(" +(sFormat.indexOf('/') > 0 ? "replace(" + str + ",'/','-')" : str) + ")";
		default: 
			return str;
		}
	}
	
	public static String sqlDateToChar(String str){
		return sqlDateToChar(str,DJ_DATEFORMAT);
	}
	
	/**
	 * 日期计算
	 * @param sDate
	 * @param iDay
	 * @param dateType
	 * @return
	 */
	public static String sqlAddDate(String sDate,int iDay,String dateType){
		switch(dbType){
		case DB_DB2:
			if(dateType.equalsIgnoreCase("year")){
				return " "+sDate+" + "+iDay+" years ";
			}
			else if(dateType.equalsIgnoreCase("month")){
				return " "+sDate+" + "+iDay+" months ";
			}
			else if(dateType.equalsIgnoreCase("day")){
				return " "+sDate+" + "+iDay+" days ";
			}
		case DB_SQL:
			return " dateadd("+dateType+","+iDay+","+sDate+" ) ";
		case DB_ORA:
			if(dateType.equalsIgnoreCase("day")){
				return " "+sDate+ " + "+iDay+" ";
			}
			else if(dateType.equalsIgnoreCase("month")){
				return "add_months("+sDate+","+iDay+")";
			}
			else if(dateType.equalsIgnoreCase("year")){
				return "add_months("+sDate+","+iDay+"*12)";
			}
		default:return "";
		}
	}
	
	/**
	 * 日期相减
	 * @param sDate1
	 * @param sDate2
	 * @param dateType
	 * @return
	 */
	public static String sqlDateDiff(String sDate1,String sDate2,String dateType){
		switch(dbType){
		case DB_DB2:
			if(dateType.equalsIgnoreCase("day")){
				return "days(" + sDate2 + ")-days(" + sDate1 + ")";
			}
			else if(dateType.equalsIgnoreCase("month")){
				return "months(" + sDate2 + ")-months(" + sDate1 + ")";
			}
			else if(dateType.equalsIgnoreCase("year")){
				return "years(" + sDate2 + ")-years(" + sDate1 + ")";
			}
		case DB_SQL:
			if(dateType.equalsIgnoreCase("day")){
				return "datediff(day," + sDate1 + "," + sDate2 + ")";
			}
			else if(dateType.equalsIgnoreCase("month")){
				return "datediff(month," + sDate1 + "," + sDate2 + ")";
			}
			else if(dateType.equalsIgnoreCase("year")){
				return "datediff(year," + sDate1 + "," + sDate2 + ")";
			}
		case DB_ORA:
			return "to_date('"+ sDate2 +"','yyyy-mm-dd') - to_date('"+ sDate1 +"','yyyy-mm-dd')";
		default:
			return sDate2 + "-" + sDate1;	
		}
	}
	/**
	 * @param sDate
	 * @return
	 */
	public static String sqlYear(String sDate){
		switch(dbType){
		case DB_ORA:
			return "to_number(to_char(" + sDate + "," + "'yyyy'))";
		default:
			return "YEAR(" + sDate + ")";
		}
	}
	/**
	 * year 函数
	 * @param sDate
	 * @return
	 */
	public static String sqlMonth(String sDate){
		switch(dbType){
		case DB_ORA:
			return "to_number(to_char(" + sDate + "," + "'mm'))";
		default:
			return "month(" + sDate + ")";
		}
	}
	/**
	 * day	函数
	 * @param sDate
	 * @return
	 */
	public static String sqlDay(String sDate){
		switch(dbType){
		case DB_ORA:
			return "to_number(to_char(" + sDate + "," + "'dd'))";
		default:
			return "day(" + sDate + ")";
		}
	}
	

	public static String sqlNumber(String sStr1) {
		switch(dbType){
		case DB_ORA:return "to_number(" + sStr1 + ")";
		default:return "cast(" + sStr1 + " as numeric(18,2))";
		}
		
	}

	/**
	 * sStr1--转换的字符数捄1�7 len--要转换数据Number的长庄1�7 ws--小数后保留位敄1�7
	 */
	public static String sqlNumber(String sStr1, int len, int ws) {
		switch(dbType){
		case DB_ORA:return "to_number(" + sStr1 + ")";
		default:return "cast(" + sStr1 + " as numeric(" + len + "," + ws + "))";
		}
	}
	
	
	
	/**
	 * trim函数
	 * @param str
	 * @return
	 */
	public static String sqlTrim(String str){
		switch(dbType){
		case DB_ORA:
			return "Trim(" + str + ")";
		default:
			return "RTrim(LTrim(" + str + "))";
		}
	}
	/**
	 * QL的Isnull 函数
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static String sqlIsNull(String str1, String str2){
		switch(dbType){
		case DB_ORA:
			return "NVL(" + str1 + "," + str2 + ")";
		case DB_SQL:
			return "IsNull(" + str1 + "," + str2 + ")";
		default:
			return "(case when " + str1 + " is null then " + str2 + " else " + str1 + " end)";
		}
	}
	public static String sqlIsNull(String str1){
		return sqlIsNull(str1, "0");
	}
	/**   字符串连接字符*/
	public static String sqlJN(){
		if(dbType==DJCons.DB_SQL) 
			return " + ";
		return " || ";
	}
	/**
	 * 整除
	 * @param str1
	 * @param str2
	 * @param bAbs
	 * @return
	 */
	public static String sqlDiv(String str1, String str2, boolean bAbs){
		switch(dbType){
		case DB_ORA:
			return "floor(abs(" + str1 + ")/abs(" + str2 + ")) "
			+ (bAbs ? "" : ("* sign(" + str1 + ") * sign(" + str2 + ")"));
		default:
			return (bAbs ? "abs(" : "") + "(" + str1 + ") / (" + str2 + ")" + (bAbs ? ")" : "");
		}
	}
	/**
	 * 取模
	 * @param str1
	 * @param str2
	 * @param bAbs
	 * @return
	 */
	public static String sqlMod(String str1, String str2, boolean bAbs){
		switch(dbType){
		case DB_ORA:
			return (bAbs ? "abs(" : "") + "mod(" + str1 + "," + str2 + ")" + (bAbs ? ")" : "");
		case DB_SQL:
			return (bAbs ? "abs(" : "") + "(" + str1 + ") % (" + str2 + ")" + (bAbs ? ")" : "");
		default:
			return (bAbs ? "abs(" : "") + "mod(" + str1 + ", " + str2 + ")" + (bAbs ? ")" : "");
		}
	}
	/**
	 * 连接字符串符号
	 * @return
	 */
	public static String sqlAdd(){
		switch(dbType){
		case DB_SQL:
			return " + ";
		default :
			return " || ";
		}
	}
	/**
	 * 用于返回不同数据库类型下的数据类型表示，传入参数是sqlserver格式
	 *	主要用于cast表达式，目前sql/oracle的cast语法只有as 后面类型不同
	 *	目前stype支持money和nvarchar，注意不要填nvarchar后面的1�7(20)之类的1�7
	 * @param sType
	 * @return
	 */
	public static String sqlCastType(String sType){
		sType = sType.toLowerCase().trim();
		switch(dbType){
		case DB_ORA:
			if (sType.equalsIgnoreCase("money")){
				return "number(19,4)";
			}
			else if(sType.equalsIgnoreCase("nvarchar")){
				return "varchar2";
			}
			else{
				return sType;
			}
		case DB_SQL:
			return sType;
		case DB_DB2:
			if (sType.equalsIgnoreCase("money")){
				return "decimal(19,4)";
			}
			else if(sType.equalsIgnoreCase("nvarchar")){
				return "varchar";
			}
			else{
				return sType;
			}
		default:
			return "";
		}
	}
	/**
	 * case when/sql,,,,decode/oracle
	 * 因为oracle的case a when b ...语句对于nvarchar不能用，扄1�7以用decode
	 * sAllParam的格式为，第丄1�7个元素是要比较的表达式，以后两个丄1�7组是值和结果＄1�7
	 * 朄1�7后如果有单个的就是else对应的�1�7��1�7�每个元素间用�1�7�号间隔，暂时不支持带�1�7�号的表达式
	 * @param sAllParam
	 * @return
	 */
	public static String sqlCase(String sAllParam){
		String[] sarr = null;
		StringBuffer stmp = new StringBuffer(99);
		int lloop;
		if (sAllParam.length() == 0)
			return "";
		if (dbType == DB_ORA)
			return "decode(" + sAllParam + ")";
		else{
			sarr = splitParam(sAllParam);
			//第一个元素是case的对象，后面成对是条件和值，for处理这些 条件/倄1�7
			//如果有else，最后就是else内容，在for结束后处琄1�7
			for (lloop = 1; lloop <= ((sarr.length - 1) / 2) * 2; lloop++){
				stmp.append(((lloop % 2) == 0 ? " then " : " when ") + sarr[lloop]);
			}
			if (sarr.length - 1 == lloop)
				stmp.append(" else " + sarr[lloop]);
			return " case " + sarr[0] + stmp.toString() + " end";
		}
	}

	private static String[] splitParam(String sParam){
		int ltmp = 0, lpair = 0;
		int lloop = 0;
		int llen = 0;
		char ctmp;
		//Vector vect = new Vector(5,2); //其实也可用stringbuffer实现，但试vector：P
		ArrayList<String> alParam = new ArrayList<String>(5); //将Vector替换为ArrayList，默认容量继续保持为5，ArrayList没有增量的定义属性，省去＄1�7
		String[] sarr = null; //特意从ArrayList转成array返回:P
		//分析括号，如果左右括号数目差不等亄1�7�那么即使碰到�1�7�号，也不能隔断
		for (; lloop < sParam.length(); lloop++){
			ctmp = sParam.charAt(lloop);
			if (ctmp == '(')
				lpair++;
			if (ctmp == ')')
				lpair--;
			if (ctmp == ',' && lpair == 0){
				alParam.add(sParam.substring(ltmp, lloop));
				ltmp = lloop + 1; //下一个起始点
				llen++;
			}
		}
		alParam.add(sParam.substring(ltmp, lloop));
		sarr = new String[alParam.size()];
		alParam.toArray(sarr);
		//vect.copyInto(sarr);
		return sarr;
	}
	/**
	 * 用户group by 后面的cube
	 * @param sCube String：应该是"(s1,s2)"这样的形弄1�7
	 * @return String：对ora＄1�7"cube(s1,s2)" 对sqlserver＄1�7"(s1,s2) with cube"
	 */
	public static String sqlCube(String sCube){
		if (dbType == DB_SQL)
			return " " + sCube + " with cube";
		return " cube(" + sCube + ") ";
	}
	/** 
	 * 箄1�7化对于多个like条件SQL语句
	 * @param columnName String   受限的like字段各1�7
	 * @param conditions String   多个like条件的字符串数组
	 * @return String
	 */
	public static String sqlLike(String columnName, String[] conditions){
		StringBuffer buf = new StringBuffer();
		buf.append("(").append(columnName).append(" like '").append(conditions[0]).append("%'");
		for (int i = 1; i < conditions.length; i++)
			buf.append(" or ").append(columnName).append(" like '").append(conditions[i]).append("%'");
		buf.append(")");
		return buf.toString();
	}
	/**
	 * 套账SQL丄1�7 IN（里的字符串＄1�7
	 * @param str
	 * @return String
	 */
	public static String sqlIN(String str){
		String[] arr = str.split(",");
		String tmpStr = "";
		for(int i=0;i<arr.length;i++){
			tmpStr += "'"+arr[i].trim()+"',";
		}
		if(tmpStr.length()>0){
			tmpStr = tmpStr.substring(0,tmpStr.length()-1);
		}else{
			tmpStr = str;
		}
		return tmpStr;
	}
	/**
	 * 支持各数据库下空串的判断
	 * @param sStr String
	 * @return String
	 */
	public static String sqlTrimNull(String sStr) {
		if (dbType == DB_SQL)
			return "len(" + sqlTrim(sStr) + ") = 0";
		else if (dbType == DB_DB2)
			return "length(" + sqlTrim(sStr) + ") = 0";
		else
			return "trim(" + sStr + ") is null";
	}
	/**
	 * 取得数据表中前N条数捄1�7
	 * @param topNum	取得的条敄1�7
	 * @param fields	字段各1�7
	 * @param table	表名
	 * @param whereStr	其他WHERE条件
	 * @param orderStr	排序
	 * @return
	 */
	public static String sqlTop(int topNum,String fields,String table,String whereStr,String orderStr){
		if(dbType == DB_SQL){
			return "SELECT TOP "+ topNum +" "+fields+" FROM "+ table + (whereStr.length()>0?" WHERE "+ whereStr+" " : " ") + orderStr;
		}
		else if(dbType == DB_ORA){
			return "SELECT "+fields+" FROM (SELECT * FROM "+ table +" "+ orderStr +") WHERE ROWNUM<="+ topNum + (whereStr.length()>0?" AND "+whereStr+" " : " ") ;
		}
		else if(dbType == DB_DB2){
			return "SELECT "+fields+" FROM "+ table  + (whereStr.length()>0?" WHERE "+ whereStr+" " : " ") + orderStr+" FETCH FIRST "+ topNum +" ROWS ONLY ";
		}
		return "";
	}
	/**
	 * 拼接分页sql语句
	 * @param sql	查询数据的sql语句
	 * @param pageSize	每页存放数据条数
	 * @param pageIndex 页序号
	 * @return
	 */
	public static String getFySql(String sql,int pageSize,int pageIndex){
		StringBuffer _sql = new StringBuffer();
        int starNum = pageSize * pageIndex + 1;
		_sql.append(" select * from ( ");	
		if(DbUtil.getDbType()==DbUtil.DB_SQL){
			_sql.append("select TOP ").append(String.valueOf(pageSize)).append("st,a.* from ( ").append(sql).append(" ) a )"); 
		}else if(DbUtil.getDbType()==DbUtil.DB_ORA){
            _sql.append("select rownum st,a.* from ( ").append(sql)
    		.append( " ) a)").append(" where st between ").append(starNum).append(" and ").append(starNum + pageSize);
		}else if(DbUtil.getDbType()==DbUtil.DB_DB2){
            _sql.append("select a.* from ( ").append(sql).append(" ) a )").append(" FETCH FIRST ").append(String.valueOf(pageSize)).append(" ROWS ONLY");
		}
		return _sql.toString();
	}
	
	/**
	 * 将数字�1�7�转化为字符型，并保留相应的位数
	 * @param integer 整数部分位数
	 * @param pointer 小数部分位数
	 */
	public static String sqlNumberToChar(String fieldStr,int integer,int pointer){
		String format = "";
		for(int i=0;i<integer;i++){
			format += "9";
		}
		if(pointer>0){format += "D";}
		for(int i=0;i<pointer;i++){
			format += "9";
		}
		switch(dbType){
		case DB_ORA:
			return "NVL(to_char("+fieldStr+",'"+format+"'),' ')";
		case DB_SQL:
			return "IsNull(case("+fieldStr+" as varchar),' ')";
		default:
			return "(case when case("+fieldStr+" as varchar) is null then ' ' else case("+fieldStr+" as varchar) end)";
		}
	}

	
	/**
	 * 分区查询表
	 * 所有的分区表在分区时遵循一定规则 分区名 ＝ 表名后缀+下划线+P+年份
	 * 所以根据表名和相关年份就可以拼成某一分区
	 * 某年的数据就在某年的分区中
	 * 此处不涉及跨年查询的情况,在页面上会锁定开始日结束日在一年内
	 * @param tabName 表名
	 * @param year 年份
	 * @return 分区表名
	 */
	public static String djPartitionTab(String tabName,String year){
		switch(dbType){
		case DB_ORA:
			return getPTableName(tabName,year);
		default:
			return " " + tabName + " ";
		}

	}
	public static String djPartitionTab(String tabName,Date date){
		switch(dbType){
		case DB_ORA:
			return getPTableName(tabName,date);
		default:
			return " " + tabName + " ";
		}
	}
	
	/**
	 * @description 按传递参数获取生成的分区表名(oracle)
	 * @param tN    表名
	 * @param date  日期
	 * @return      表名
	 */
	public static String getPTableName(String tN,Date date){
		StringBuffer tableBuffer = new StringBuffer();
		tableBuffer.append(" ").append(tN).append(" partition (")
				   .append(tN.substring(tN.indexOf("_")+1, tN.length())).append("_p");
		if(date != null) 
			tableBuffer.append(DateUtil.getYear(date)+1);
		else 
			tableBuffer.append(DateUtil.getYear(DateUtil.getCurDate()));
		return tableBuffer.append(") ").toString();
	}
	
	public static String getPTableName(String tN,String year){
		StringBuffer tableBuffer = new StringBuffer();
		tableBuffer.append(" ").append(tN).append(" partition (")
							   .append(tN.substring(tN.indexOf("_")+1, tN.length())).append("_p");	
		if( year.length() > 1 ) 
			tableBuffer.append(Integer.valueOf(year)+1);
		else 
			tableBuffer.append(DateUtil.getYear(DateUtil.getCurDate()));
		return tableBuffer.append(") ").toString();
	}
	
	
	/** 获取不同数据库的时间标示
	 * @desc 
	 * @return
	 */
	public static String getDateFlag(){
		String flag = null;
		if(dbType==2){
			flag = "date";
		}else if(dbType==1){
			flag = "date";
		}else if(dbType==0){
			flag = "smalldatetime";
		}
		return flag;
	}
	/** 获取不同数据库的数字类型标示
	 * @desc 
	 * @return
	 */
	public static String getNumFlag(){
		String flag = null;
		if(dbType==2){
			flag = "decimal";
		}else if(dbType==1){
			flag = "number";
		}else if(dbType==0){
			flag = "numeric";
		}
		return flag;
	}
	/** 获取不同数据库的字符类型标示
	 * @desc 
	 * @return
	 */
	public static String getVarFlag(){
		String flag = null;
		if(dbType==2){
			flag = "varchar";
		}else if(dbType==1){
			flag = "varchar2";
		}else if(dbType==0){
			flag = "varchar";
		}
		return flag;
	}
	
}
