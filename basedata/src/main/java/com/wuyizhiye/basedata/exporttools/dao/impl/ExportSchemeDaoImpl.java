package com.wuyizhiye.basedata.exporttools.dao.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.base.export.ExportDataSource;
import com.wuyizhiye.base.mybatis.dialect.Dialect;
import com.wuyizhiye.base.mybatis.dialect.MySqlDialect;
import com.wuyizhiye.base.mybatis.dialect.OracleDialect;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.exporttools.dao.ExportSchemeDao;
import com.wuyizhiye.basedata.exporttools.model.ExportFilter;
import com.wuyizhiye.basedata.exporttools.model.ExportScheme;
import com.wuyizhiye.basedata.exporttools.model.FilterTypeEnum;

/**
 * @ClassName ExportSchemeDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="exportSchemeDao")
public class ExportSchemeDaoImpl extends BaseDaoImpl implements ExportSchemeDao {
	private static final String PARAMREG = ":[1-9a-zA-Z]+";
	private static final Pattern PATTERN = Pattern.compile(PARAMREG);
	
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.exporttools.dao.ExportSchemeDao";
	}
	
	@Override
	public ExportDataSource<Map<String,Object>> getExportDataSource(ExportScheme scheme,final Map<String,String> param) throws SQLException {
		final List<ExportFilter> filters = scheme.getFilters();
		String sql = scheme.getSql();
		Matcher matcher = PATTERN.matcher(sql);
		int index = 0;
		final Map<Integer,String> paramMap = new HashMap<Integer, String>();
		while(matcher.find()){
			paramMap.put(++index,matcher.group().substring(1));
		}
		sql = sql.replaceAll(PARAMREG, "?");
		final String finalSql = sql;
		final Connection conn = jdbcTemplate.getDataSource().getConnection();
		DatabaseMetaData dbmd = conn.getMetaData();
		String dataBaseType = dbmd.getDatabaseProductName();
		final Set<Integer> indexs = paramMap.keySet();
		final Dialect dialect = "oracle".equalsIgnoreCase(dataBaseType)?new OracleDialect():new MySqlDialect();//当前仅支持oracle和mysql
		final Object[] params = new Object[indexs.size()];
		for(Integer idx : indexs){
			params[idx-1] = getParamValue(paramMap.get(idx), param, filters);
		}
		final int count = jdbcTemplate.queryForInt("SELECT COUNT(1) FROM (" + sql + ") TMP", params);
		return
		new ExportDataSource<Map<String,Object>>() {
			@Override
			public Pagination<Map<String,Object>> getData(Pagination<Map<String,Object>> pagination) {
					pagination.setRecordCount(count);
					int start = (pagination.getCurrentPage() - 1) * pagination.getPageSize();
					List<Map<String,Object>> items = jdbcTemplate.queryForList(dialect.getLimitString(finalSql, start, pagination.getPageSize()), params);
					pagination.setItems(items);
					return pagination;
			}
		};
	}
	
	/**
	 * 设置参数
	 * @param ps
	 * @param index
	 * @param name
	 * @param param
	 * @param filters
	 * @throws SQLException
	 */
	@SuppressWarnings("unused")
	private void setParam(PreparedStatement ps,Integer index, String name, Map<String,String> param, List<ExportFilter> filters) throws SQLException{
		for(ExportFilter filter :filters){
			if(name.equals(filter.getNumber())){
				String value = param.get(name);
				int sqlType = Types.VARCHAR;
				FilterTypeEnum type = filter.getFilterType();
				if(!StringUtils.isEmpty(value)){
					if(type == FilterTypeEnum.INT){
						sqlType = Types.INTEGER;
						ps.setInt(index, Integer.parseInt(value));
					}else if(type == FilterTypeEnum.FLOAT){
						sqlType = Types.FLOAT;
						ps.setFloat(index,Float.parseFloat(value));
					}else if(type == FilterTypeEnum.DATE){
						sqlType = Types.DATE;
						SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
						try {
							ps.setDate(index,new Date(date.parse(value).getTime()));
						} catch (ParseException e) {
							throw new RuntimeException(value+" to Date yyyy-MM-dd");
						}
					}else if(type == FilterTypeEnum.DATETIME){
						sqlType = Types.TIMESTAMP;
						SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						try {
							ps.setTimestamp(index, new Timestamp(time.parse(value).getTime()));
						} catch (ParseException e) {
							throw new RuntimeException(value+" to DateTime yyyy-MM-dd HH:mm:ss");
						}
					}else{
						sqlType = Types.VARCHAR;
						ps.setString(index, value);
					}
				}else{
					ps.setNull(index, sqlType);
					return;
				}
			}
		}
	}
	
	private Object getParamValue(String name, Map<String,String> param, List<ExportFilter> filters){
		for(ExportFilter filter :filters){
			if(name.equals(filter.getNumber())){
				String value = param.get(name);
				FilterTypeEnum type = filter.getFilterType();
				if(!StringUtils.isEmpty(value)){
					if(type == FilterTypeEnum.INT){
						return Integer.parseInt(value);
					}else if(type == FilterTypeEnum.FLOAT){
						return Float.parseFloat(value);
					}else if(type == FilterTypeEnum.DATE){
						SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
						try {
							return new Date(date.parse(value).getTime());
						} catch (ParseException e) {
							throw new RuntimeException(value+" to Date yyyy-MM-dd");
						}
					}else if(type == FilterTypeEnum.DATETIME){
						SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						try {
							return new Timestamp(time.parse(value).getTime());
						} catch (ParseException e) {
							throw new RuntimeException(value+" to DateTime yyyy-MM-dd HH:mm:ss");
						}
					}else{
						return value;
					}
				}else{
					return null;
				}
			}
		}
		return null;
	}
}
