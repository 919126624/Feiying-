package com.wuyizhiye.base.util;

import java.util.ArrayList;
import java.util.List;

import com.wuyizhiye.base.query.Selector;
import com.wuyizhiye.base.query.Sorter;


/**
 * @ClassName SQLUtil
 * @Description SQL工具类
 * @author li.biao
 * @date 2015-4-1
 */
public class SQLUtil {
	
	/**
	 * 匹配SQL中的Select关键字
	 */
	public static final String SELECT = "(s|S)(e|E)(l|L)(e|E)(c|C)(t|T)\\s+";
	
	/**
	 * 匹配SQL中的from关键字
	 */
	public static final String FROM  = "\\s+(f|F)(r|R)(o|O)(m|M)\\s+";
	
	/**
	 * 匹配SQL中的as关键字
	 */
	public static final String AS  = "\\s+(a|A)(s|S)\\s+";
	
	/**
	 * 匹配SQL中的distinct关键字
	 */
	public static final String DISTINCT  = "\\s+(d|D)(i|I)(s|S)(t|T)(i|I)(n|N)(c|C)(t|T)\\s+";
	
	/**
	 * 匹配SQL中的where关键字
	 */
	public static final String WHERE  = "\\s+(w|W)(h|H)(e|E)(r|R)(e|E)\\s+";
	
	/**
	 * 匹配SQL中的order by 关键字
	 */
	public static final String ORDER_BY  = "\\s+(o|O)(r|R)(d|D)(e|E)\\s+(b|B)(y|Y)\\s+";
	
	/**
	 * 从SQL中解析出selector
	 * @param sql
	 * @return
	 */
	public static List<Selector> getSelectors(String sql){
		if(StringUtils.isEmpty(sql)){
			return null;
		}
		sql = sql.replaceAll(DISTINCT, "");
		sql = sql.replaceAll(SELECT, "SELECT ");
		sql = sql.replaceAll(FROM, " FROM ");
		List<Selector> selectors = new ArrayList<Selector>();
		String stors = sql.substring(sql.indexOf("SELECT ")+7, sql.indexOf(" FROM "));
		String[] stor = stors.split(",");
		for(String s : stor){
			s = s.trim();
			s = s.replaceAll(AS, ",");
			String[] tmp = s.split(",");
			Selector st = new Selector();
			st.setColumn(tmp[0]);
			if(tmp.length > 1){
				st.setField(tmp[1].replaceAll("\"", ""));
			}else{
				st.setField(tmp[0]);
			}
			selectors.add(st);
		}
		return selectors;
	}
	
	public static List<Sorter> getSorters(String sql){
		List<Sorter> sorters = new ArrayList<Sorter>();
		
		return sorters;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String sql = "seLect DISTINCT POSITIONTYPE.FID AS \"positionType.id\",POSITIONTYPE.FNAME AS \"positionType.name\","
		+"POSITIONTYPELEVEL.FID AS \"positionTypeLevel.id\","
		+"POSITIONTYPELEVEL.FNAME AS \"positionTypeLevel.name\","
		+"STAND.FID AS \"standard\""
		+" fRom T_HR_POSITIONTYPELEVEL POSITIONTYPELEVEL"
		+" INNER JOIN T_HR_POSITIONTYPE POSITIONTYPE"
		+" ON POSITIONTYPELEVEL.FKPOSITIONTYPEID = POSITIONTYPE.FID"
		+" INNER JOIN T_HR_POSITION POSITION"
		+" ON POSITION.FKPOSITIONTYPEID = POSITIONTYPE.FID"
		+" INNER JOIN T_HR_ORG ORG"
		+" ON ORG.FID = POSITION.FKORGID"
		+" LEFT JOIN T_HR_SALARYSTANDARD STAND"
		+" ON STAND.FKPOSITIONTYPEID = POSITIONTYPE.FID AND STAND.FKPOSITIONTYPELEVELID = POSITIONTYPELEVEL.FID"
		+" WHERE ORG.FLONGNUMBER LIKE '${orgLongNumber}!%'"
		+" ORDER BY POSITIONTYPE.FID,POSITIONTYPELEVEL.FID";
		List<Selector> selectors = getSelectors(sql);
		for(Selector s : selectors){
//			System.out.println(s);
		}
	}
}
