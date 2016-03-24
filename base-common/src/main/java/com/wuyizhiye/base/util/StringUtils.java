package com.wuyizhiye.base.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.apache.log4j.Logger;

/**
 * @ClassName StringUtils
 * @Description 字符串工具类
 * @author li.biao
 * @date 2015-4-1
 */
public class StringUtils {
	private final static Logger logger = Logger.getLogger(StringUtils.class);
	/**
	 * @Description 判断字符串是否为空
	 * @param str
	 * @return
	 */
	public static boolean isNotNull(String str) {
		if (str != null && !"".equals(str.trim())) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * @Description 判断对象是否为空
	 * @param obj
	 * @return
	 */
	public static boolean isNotNull(Object obj) {
		if (obj != null && obj.toString() != null && !"".equals(obj.toString().trim())) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * @Description 判断字符串是否为空(自动截取首尾空白)
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str){
		return isEmpty(str,true);
	}
	
	/**
	 * @Description 判断字符串是否为空
	 * @param str 源字符串
	 * @param trim 是否截取首尾空白
	 * @return
	 */
	public static boolean isEmpty(String str,boolean trim){
		return str==null?true:"".equals(str.trim());
	}
	
	/**
	 * @Description 判断字符是否匹配
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean isEquals(String str1,String str2){
		if(isEmpty(str1) || isEmpty(str2)){
			return false;
		}
		return str1.equals(str2);
	}
	
	/**
	 * @Description 将一个字符串转成字符数组
	 * @param str
	 * @param delim
	 * @return
	 */
	public static String[] parseToArray(String str, String delim) {
		ArrayList arr = new ArrayList();
		StringTokenizer st = new StringTokenizer(str, delim);
		while (st.hasMoreTokens()) {
			arr.add(st.nextToken());
		}
		String[] ret = new String[arr.size()];
		for (int i = 0; i < arr.size(); i++) {
			ret[i] = (String) arr.get(i);
		}
		return ret;
	}
	
	/**
	 * 
	 * @param source
	 *            源字符列表
	 * @param denotation
	 *            连接符号
	 * @return 例如：['abc','ccc','efg']将返回"abc,ccc,efg"格式
	 * @description 根据一组字符串将其拼接成一个带有指定分隔符的字符串
	 */
	public static String arrayToString(List<String> source, String denotation) throws Exception {
		if (source == null) {
			throw new Exception("source cannot pass null");
		}
		StringBuffer sb = new StringBuffer();
		for (String str : source) {
			sb.append(str + denotation);
		}
		if (sb.length() > 0 && sb.indexOf(denotation) > 0) {
			sb = sb.delete(sb.length() - 1, sb.length());
		}
		return sb.toString();
	}
	
	/**
	 * @author li。jinwen
	 * @param source
	 *            源字符串
	 * @param denotation
	 *            拆分字符
	 * @param type
	 *            返回类型
	 * @return
	 * @return 例如："abc,ccc,efg"将返回['abc','ccc','efg']格式
	 * @description 根据一个字符串将其按照指定的分隔符组合成一个字符串列表
	 */
	public static Object stringToArray(String source, String denotation, Class<?> type) throws Exception {
		ArrayList<String> array = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(source, denotation);
		while (st.hasMoreTokens()) {
			array.add(st.nextToken());
		}
		if (type.isAssignableFrom(ArrayList.class)) {
			return array;
		} else if (type.isAssignableFrom(String[].class)) {
			String[] ret = new String[array.size()];
			for (int i = 0; i < array.size(); i++) {
				ret[i] = (String) array.get(i);
			}
			return ret;
		} else {// 抛出异常
			throw new Exception("The type of the type is unknown");
		}
	}
    
	/**
	 * @Description 字符替换
	 * @param str
	 * @param old
	 * @param rep
	 * @return
	 */
	public static String replace(String str, String old, String rep) {
		if ((str == null) || (old == null) || (rep == null)) {
			return "";
		}
		int index = str.indexOf(old);
		if ((index < 0) || old.equals("")) { 
			return str;
		}
		StringBuffer strBuf = new StringBuffer(str);
		while (index >= 0) { // found old part
			strBuf.delete(index, index + old.length());
			strBuf.insert(index, rep);
			index = strBuf.toString().indexOf(old);
		}
		return strBuf.toString();
	}

	/**
	 * @Description 带逗号分隔的数字转换为NUMBER类型
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Number stringToNumber(String str) throws ParseException {
		if(str==null || str.equals("")){
			return null;
		}
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		dfs.setGroupingSeparator(',');
		dfs.setMonetaryDecimalSeparator('.');
		DecimalFormat df = new DecimalFormat("###,###,###,###.##", dfs);
		return df.parse(str);
	}
	
	/**
	 * @Description 获取扩展名
	 * @param filename
	 * @return
	 */
    public static String getExtensionName(String filename) {   
        if ((filename != null) && (filename.length() > 0)) {   
            int dot = filename.lastIndexOf('.');   
            if ((dot >-1) && (dot < (filename.length() - 1))) {   
                return filename.substring(dot + 1);   
            }   
        }   
        return filename;   
    }  
    
    /**
     * 用于字符串替换
     * @param target       目标对象 需要替换的字符串
     * @param replacement  要替换的字符串
     * @param value        替换的值
     * @return
     */
    public static String replacement(String target,String replacement,String value){
    	if(target!=null)
    	return target.replace(replacement,value);
    	return null;
    }

    /**
     * @Description 判断字符串是否为数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
    	if(str==null || str.length()==0){
    		return false;
    	}
    	for (int i = 0; i < str.length(); i++){
    	   if (!Character.isDigit(str.charAt(i))){
    	    return false;
    	   }
    	}
    	return true;
    }
    
    /**
     * @Description 计算指定时间与当前时间的差
     * @param date
     * @return
     */
    public static String convDateToString(Date date){
    	Long time=new Date().getTime()-date.getTime();
    	Long min=time/1000/60;
    	if(min<5){
    		return "刚刚";
    	}else if(min>=5 && min<60){
    		return min+"分钟之前";
    	}else if(min>=60 && min<1440){
    		return min/60+"小时之前";
    	}else if(min>=1440 && min<10080){
    		return min/60/24+"天之前";
    	}else if(min>=10080 && min<40320){
    		return min/60/24/7+"周之前";
    	}else if(min>=40320 && min<525600){
    		return min/60/24/7/4+"月之前";
    	}else if(min>=525600 ){
    		return min/60/24/365+"年之前";
    	}
		return null;
    }
    
    /**
     * @Description 秒数转换成中文表示
     * @param secondTime
     * @return
     */
    public static String convSecondTimeToString(int secondTime){
    	if(secondTime < 60){
    		return secondTime + "秒" ;
    	}else if(secondTime >= 60 && secondTime < 60*60){
    		int m = (int) Math.floor(secondTime/60);
    		int s = secondTime % 60 ;
    		return m + "分" + s + "秒" ;
    	}else if(secondTime >= 60*60){
    		int h = (int) Math.floor(secondTime/(60*60)) ;
    		int m = (int) Math.floor((secondTime - h*(60*60))/60);
    		int s = (secondTime - h*(60*60))%60;
    		return h+"小时"+m+ "分"+ s + "秒";
    	}
		return "0";
    }
    
    /**
     * @Description 获取当前服务器日期
     * @param formatStr
     * @return
     */
	public static String getCurrdate(String formatStr) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
		String mDateTime = formatter.format(cal.getTime());
		return mDateTime;
	}
	
	/**
	 * @Description 解析字符串 ---> 去掉字符串中回车、换行、空格
	 * @param str
	 * @return
	 */
	public static String parse(String str) {
		return str.replaceAll("\n", "").replaceAll("chr(13)", "").replaceAll(
				" ", "");
	}
	
	/**
	 * 获取UUID
	 * @return UUID
	 */
	public static String getUUID(){
		return (UUID.randomUUID()+"").replaceAll("-","");
	}
	
	/**
	 * @Description 得到全拼
	 * @param src
	 * @return
	 */
    public static String getPingYin(String src) {
        char[] t1 = null;
        t1 = src.toCharArray();
        String[] t2 = new String[t1.length];
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);
        String t4 = "";
        int t0 = t1.length;
        try {
            for (int i = 0; i < t0; i++) {
                // 判断是否为汉字字符
                if (java.lang.Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
                    t4 += t2[0];
                } else {
                    t4 += java.lang.Character.toString(t1[i]);
                }
            }
            return t4;
        } catch (BadHanyuPinyinOutputFormatCombination e1) {
            e1.printStackTrace();
        }
        return t4;
    }
    
    
    /**
     * @Description 得到简拼
     * @param target
     * @param isSimpleSpell
     * @return
     */
    public static String getSpell(String target, boolean isSimpleSpell) {
        char[] chars = target.toCharArray();
        StringBuffer spell = new StringBuffer();
        String[] pinyinStr;
        int endIndex;
        for (char ch : chars) {
            // 如果不是汉字
            if (ch <= 128) {
                spell.append(ch);
                continue;
            }
            pinyinStr = PinyinHelper.toHanyuPinyinStringArray(ch);
            // 过滤中文符号
            if (pinyinStr == null) {
                spell.append(ch);
                continue;
            }
            endIndex = isSimpleSpell ? 1 : pinyinStr[0].length() - 1;
            spell.append(pinyinStr[0].substring(0, endIndex));
        }
        return spell.toString();
    }
    

    /**
     * @Description 将字符串转移为ASCII码
     * @param cnStr
     * @return
     */
    public static String getCnASCII(String cnStr) {
        StringBuffer strBuf = new StringBuffer();
        byte[] bGBK = cnStr.getBytes();
        for (int i = 0; i < bGBK.length; i++) {
            strBuf.append(Integer.toHexString(bGBK[i] & 0xff));
        }
        return strBuf.toString();
    }
    
    public static int switchNumber(String str){
    	char c = str.charAt(0);
    	int temp = 0;
    	switch (c) {
			// 数值
			case '〇':
			case '零':
				temp = 0;
				break;
			case '一':
				temp = 1;
				break;
			case '二':
				temp = 2;
				break;
			case '三':
				temp = 3;
				break;
			case '四':
				temp = 4;
				break;
			case '五':
				temp = 5 ;
				break;
			case '六':
				temp = 6 ;
				break;
			case '七':
				temp = 7 ;
				break;
			case '八':
				temp = 8 ;
				break;
			case '九':
				temp = 9 ;
				break;
			// 单位，前缀是单数字
			case '十':
				temp = 10;
				break;
    	}
    	return temp;
    }    
    
    /**
     * @Description 中文数字转换为阿拉伯数
     * @param s
     * @return
     */
    public static int cnNumToInt(String s) {
		int result = 0;
		int yi = 1;// 记录高级单位
		int wan = 1;// 记录高级单位
		int ge = 1;// 记录单位
		char c = s.charAt(0);
		int temp = 0;// 记录数值
		switch (c) {
			// 数值
			case '〇':
				case '零':
				temp = 0;
				break;
			case '一':
				temp = 1 * ge * wan * yi;
				ge = 1;
				break;
			case '二':
				temp = 2 * ge * wan * yi;
				ge = 1;
				break;
			case '三':
				temp = 3 * ge * wan * yi;
				ge = 1;
				break;
			case '四':
				temp = 4 * ge * wan * yi;
				ge = 1;
				break;
			case '五':
				temp = 5 * ge * wan * yi;
				ge = 1;
				break;
			case '六':
				temp = 6 * ge * wan * yi;
				ge = 1;
				break;
			case '七':
				temp = 7 * ge * wan * yi;
				ge = 1;
				break;
			case '八':
				temp = 8 * ge * wan * yi;
				ge = 1;
				break;
			case '九':
				temp = 9 * ge * wan * yi;
				ge = 1;
				break;
			// 单位，前缀是单数字
			case '十':
				ge = 10;
				break;
			case '百':
				ge = 100;
				break;
			case '千':
				ge = 1000;
				break;
			// 高级单位，前缀可以是多个数字
			case '万':
				wan = 10000;
				ge = 1;
				break;
			case '亿':
				yi = 100000000;
				wan = 1;
				ge = 1;
				break;
			default:
			return -1;
		}
		result += temp;
		if (ge > 1) {
			result += 1 * ge * wan * yi;
		}
		return result;
	}

    /**
     * @Description 字符相等比较
     * @param str1
     * @param str2
     * @return
     */
    public static boolean equals(String str1, String str2) {
      return str1 == null ? false : str2 == null ? true : str1.equals(str2);
    }

    /**
     * @Description 字符相等比较（不顾大小写）
     * @param str1
     * @param str2
     * @return
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
      return str1 == null ? false : str2 == null ? true : str1.equalsIgnoreCase(str2);
    }
    
    /**
     * @Description 根据属性构建get/set方法名
     * @param type
     * @param fildeName
     * @return
     */
    public static String getMethodName(String type,String fildeName) {
    	//把一个字符串的第一个字母大写、效率是最高的、
        byte[] items = fildeName.getBytes();  
        if(items.length > 2 && Character.isUpperCase(items[1])){
        	return type + fildeName ;
        }
        items[0] = (byte) ((char) items[0] - 'a' + 'A');  
        return type + (new String(items));  
    } 
    
    /**
     * @Description 合并多个空格成一个空格
     * @param str
     * @return
     */
    public static String mergeBlankSpace(String str){
	       String s = "";
	       for (int i = 0; i < str.length() - 1; i++) {
	           //空格转成int型代表数字是32
	           if ((int) str.charAt(i) == 32 && (int) str.charAt(i + 1) == 32) {
	              continue;
	           }
	           s += str.charAt(i);
	       }
	       if ((int) str.charAt(str.length() - 1) != 32){
	    	   s += str.charAt(str.length() - 1);
	       }
	       return s ;
	}
    
    /**
     * 
     * @Description 在某个日期上加加小时 返回新的日期  孙海涛 2014-05-12
     * @param dt
     * @param addHour 正数为加负数为减
     * @return
     */
    public static Date getAddHour(Date dt,double addHour){
    	try {
    		Calendar cal = Calendar.getInstance();
    		cal.setTime(dt);
    		Double m=new Double(addHour*60);
    		cal.add(Calendar.MINUTE, m.intValue());
			return cal.getTime();
		} catch (Exception e) {
//			e.printStackTrace();
			logger.error("", e);
			
		}
		return null;
    }
    
    
    
    /********************* 移除样式 start ********************/
    private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
    private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
    private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
    private static final String regEx_escape = "&[^;]+;"; // 定义HTML中的转义字符
    
    //去掉html格式
	public static String trimStyle(String htmlStr) {
		 Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); // 过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); // 过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签

        return htmlStr.trim(); // 返回文本字符串
	}
	/********************** 移除样式 end ***********************/
	
	public static String trimTag(String htmlStr) {
		 
       Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
       Matcher m_html = p_html.matcher(htmlStr);
       htmlStr = m_html.replaceAll(""); // 过滤html标签
       
       p_html = Pattern.compile(regEx_escape, Pattern.CASE_INSENSITIVE);
       m_html = p_html.matcher(htmlStr);
       htmlStr = m_html.replaceAll(""); // 过滤html标签
       
       return htmlStr.trim(); // 返回文本字符串
	}
	
	 /**
	 * 得到表名后缀
	 * @param date
	 * @return
	 */
	public static String getSuffixStr(Date date){
		String year = (DateUtil.getYear(date) + "").substring(2, 4);
		String monthStr = DateUtil.getMonth(date) + "";
		int month = Integer.parseInt(monthStr);
		if(month<=3){
			monthStr = "01";
		}else if(month<=6){
			monthStr = "02";
		}else if(month<=9){
			monthStr = "03";
		}else if(month<=12){
			monthStr = "04";
		}else{
			return "";
		}
		
		return year + monthStr;		
	}
	
	/**
	 * @Description 传入开始时间和结束时间的，得到这段时间内的所有分表
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static List<String> getSuffixStrList(Date startDate,Date endDate){
		List<String> suffixStrList = new ArrayList<String>();
		String startSuffix = getSuffixStr(startDate);
		String endSuffix = getSuffixStr(endDate);
		if(startSuffix.equalsIgnoreCase(endSuffix)){
			suffixStrList.add(startSuffix);
		} else {
			outFlag:	//跳出循环标识
			for(int i=Integer.parseInt(startSuffix.substring(0, 2));i<=Integer.parseInt(endSuffix.substring(0, 2));i++){
				int j=1;
				//如果是开始时间的话，则取开始的季度前缀
				if(i==Integer.parseInt(startSuffix.substring(0, 2))){
					j = Integer.parseInt(startSuffix.substring(2,4));
				}
				for(;j<=4;j++){
					//如果是小于10的数，则需要前面加上0
					suffixStrList.add((i>=10?String.valueOf(i):"0"+String.valueOf(i))+'0'+j);
					//如果是结束时间所在的分表前缀的话，则跳出循环
					if(i == Integer.parseInt(endSuffix.substring(0, 2)) && j==Integer.parseInt(endSuffix.substring(2,4))){
						break outFlag;
					}
				}
			}
			
		}
		return suffixStrList;
	}

	/**
	 * @Description 获取指定时间的上一季度后缀
	 * @param date
	 * @return
	 */
	public static String getPreQuarterSuffix(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -3);    //得到前3个月
		return getSuffixStr(calendar.getTime());
	}
	
	/**
	 * @Description 获取指定时间的下一季度后缀
	 * @param date
	 * @return
	 */
	public static String getNextQuarterSuffix(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 3);    //得到后3个月
		return getSuffixStr(calendar.getTime());
	}
	
	public static String cntIntToNum(int n){
    	String temp = "";
    	switch(n){
    	case 1:
    		temp = "状元";
    		break;
    	case 2:
    		temp = "榜眼";
    		break;
    	case 3:
    		temp = "探花";
    		break;
    	case 4:
    		temp = "第四";
    		break;
    	case 5:
    		temp = "第五";
    		break;
    	case 6:
    		temp = "第六";
    		break;
    	case 7:
    		temp = "第七";
    		break;
    	case 8:
    		temp = "第八";
    		break;
    	case 9:
    		temp = "第九";
    		break;
    	case 0:
    		temp = "零";
    		break;
    	default:
    		temp = "无";
    	}
    	return temp;
    }
}
