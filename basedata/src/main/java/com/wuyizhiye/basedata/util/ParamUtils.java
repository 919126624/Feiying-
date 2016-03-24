package com.wuyizhiye.basedata.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.dao.impl.QueryExecutorImpl;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.param.enums.ParamTypeEnum;
import com.wuyizhiye.basedata.param.model.ParamLines;

/**
 * @ClassName ParamUtils
 * @Description 参数工具类
 * @author li.biao
 * @date 2015-4-2
 */
public class ParamUtils {
	/**
	 * 通过组织 , 参数编号获取参数值
	 * @param orgId 组织ID
	 * @param code  参数编号
	 * @return  参数值
	 */
	public static String getParamValue(String orgId, String paramCode){
		Map<String, Object> param = new HashMap<String, Object>();
		if(null != SystemUtil.getCurrentOrg()){
			if(!StringUtils.isNotNull(orgId)){
				orgId = SystemUtil.getCurrentOrg().getId();
			}
			param.put("id",orgId);
			Org  org =  (Org)QueryExecutorImpl.getInstance().execOneEntity("com.wuyizhiye.basedata.org.dao.OrgDao.getById",param, Org.class);
			if(StringUtils.isNotNull(org)){
				param.put("longnumber", org.getLongNumber()); 
			}
		}
		param.put("paramnumber", paramCode);
		List<ParamLines>  paramItems =  (List<ParamLines>)QueryExecutorImpl.getInstance().execQuery("com.wuyizhiye.basedata.param.dao.ParamLinesDao.selectByOrg",param, ParamLines.class);
		if(paramItems!=null && paramItems.size()>0)
			return paramItems.get(0).getParamValue();
		return null;
	}
	
	public static String getParamValue(String paramCode){
		return getParamValue(null,paramCode);
	}
	
	/**
	 * 通过参数编号获取参数值
	 * @param paramNumber
	 * @return
	 */
	public static String getParamValueByNumber(String paramNumber){
		if(StringUtils.isEmpty(paramNumber)){
			return null ;
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("paramNumber", paramNumber);
		param.put("status", 1);
		List<ParamLines>  paramItems =  (List<ParamLines>)QueryExecutorImpl.getInstance().execQuery("com.wuyizhiye.basedata.param.dao.ParamLinesDao.select",param, ParamLines.class);
		if(paramItems!=null && paramItems.size()>0){
			return paramItems.get(0).getParamValue();
		}
		return null ;
	}
	/**
	 * 
	 * 以上为原获取参数方法
	 * 
	 * 
	 */
	
	
	
	
	/**
	 * 通过组织 , 参数编号获取字符串类型参数值
	 * @param orgId 组织ID
	 * @param paramCode  参数编号
	 * @return  参数值
	 */
	public static String getStringValue(String orgId, String paramCode){
		Map<String, Object> param = new HashMap<String, Object>();
		if(!StringUtils.isNotNull(orgId)){
			orgId = SystemUtil.getCurrentOrg().getId();
		}
		param.put("id",orgId);
		Org  org =  (Org)QueryExecutorImpl.getInstance().execOneEntity("com.wuyizhiye.basedata.org.dao.OrgDao.getById",param, Org.class);
		if(StringUtils.isNotNull(org)){
			param.put("longnumber", org.getLongNumber()); 
		}
		param.put("paramnumber", paramCode);
		param.put("datatype", ParamTypeEnum.STRING);
		List<ParamLines>  paramItems =  (List<ParamLines>)QueryExecutorImpl.getInstance().execQuery("com.wuyizhiye.basedata.param.dao.ParamLinesDao.selectByOrg",param, ParamLines.class);
		if(paramItems!=null && paramItems.size()>0)
			return paramItems.get(0).getParamValue();
		return null;
	}
	/**
	 * 通过组织 , 参数编号获取整数类型参数值
	 * @param orgId 组织ID
	 * @param paramCode  参数编号
	 * @return  参数值
	 */
	public static Integer getIntValue(String orgId, String paramCode) throws NumberFormatException{
		Map<String, Object> param = new HashMap<String, Object>();
		if(!StringUtils.isNotNull(orgId)){
			orgId = SystemUtil.getCurrentOrg().getId();
		}
		param.put("id",orgId);
		Org  org =  (Org)QueryExecutorImpl.getInstance().execOneEntity("com.wuyizhiye.basedata.org.dao.OrgDao.getById",param, Org.class);
		if(StringUtils.isNotNull(org)){
			param.put("longnumber", org.getLongNumber()); 
		}
		param.put("paramnumber", paramCode);
		param.put("datatype", ParamTypeEnum.INT);
		List<ParamLines>  paramItems =  (List<ParamLines>)QueryExecutorImpl.getInstance().execQuery("com.wuyizhiye.basedata.param.dao.ParamLinesDao.selectByOrg",param, ParamLines.class);
		if(paramItems!=null && paramItems.size()>0)
			return Integer.parseInt(paramItems.get(0).getParamValue());
		return null;
	}
	/**
	 * 通过组织 , 参数编号获取布尔类型参数值
	 * @param orgId 组织ID
	 * @param paramCode  参数编号
	 * @return  参数值
	 */
	public static Boolean getBoolValue(String orgId, String paramCode){
		Map<String, Object> param = new HashMap<String, Object>();
		if(!StringUtils.isNotNull(orgId)){
			orgId = SystemUtil.getCurrentOrg().getId();
		}
		param.put("id",orgId);
		Org  org =  (Org)QueryExecutorImpl.getInstance().execOneEntity("com.wuyizhiye.basedata.org.dao.OrgDao.getById",param, Org.class);
		if(StringUtils.isNotNull(org)){
			param.put("longnumber", org.getLongNumber()); 
		}
		param.put("paramnumber", paramCode);
		param.put("datatype", ParamTypeEnum.BOOL);
		List<ParamLines>  paramItems =  (List<ParamLines>)QueryExecutorImpl.getInstance().execQuery("com.wuyizhiye.basedata.param.dao.ParamLinesDao.selectByOrg",param, ParamLines.class);
		if(paramItems!=null && paramItems.size()>0){
			if("TRUE".equals(paramItems.get(0).getParamValue())){
				return true;
			}else if("FALSE".equals(paramItems.get(0).getParamValue())){
				return false;
			}else{
				return null;
			}
		}
			
		return null;
	}
	/**
	 * 通过组织 , 参数编号获取金额类型(小数)参数值
	 * @param orgId 组织ID
	 * @param paramCode  参数编号
	 * @return  参数值
	 */
	public static String getNumValue(String orgId, String paramCode){
		Map<String, Object> param = new HashMap<String, Object>();
		if(!StringUtils.isNotNull(orgId)){
			orgId = SystemUtil.getCurrentOrg().getId();
		}
		param.put("id",orgId);
		Org  org =  (Org)QueryExecutorImpl.getInstance().execOneEntity("com.wuyizhiye.basedata.org.dao.OrgDao.getById",param, Org.class);
		if(StringUtils.isNotNull(org)){
			param.put("longnumber", org.getLongNumber()); 
		}
		param.put("paramnumber", paramCode);
		param.put("datatype", ParamTypeEnum.NUMBER);
		List<ParamLines>  paramItems =  (List<ParamLines>)QueryExecutorImpl.getInstance().execQuery("com.wuyizhiye.basedata.param.dao.ParamLinesDao.selectByOrg",param, ParamLines.class);
		if(paramItems!=null && paramItems.size()>0)
			return paramItems.get(0).getParamValue();
		return null;
	}
	/**
	 * 通过 参数编号获取字符串类型参数值(将自动取当前组织作为查询参数)
	 * @param paramCode  参数编号
	 * @return  参数值
	 */
	public static String getStringValue(String paramCode){
		return getStringValue(null,paramCode);
	}
	/**
	 * 通过 参数编号获取整数类型参数值(将自动取当前组织作为查询参数)
	 * @param paramCode  参数编号
	 * @return  参数值
	 */
	public static Integer getIntValue(String paramCode){
		return getIntValue(null,paramCode);
	}
	/**
	 * 通过 参数编号获取布尔类型参数值(将自动取当前组织作为查询参数)
	 * @param paramCode  参数编号
	 * @return  参数值
	 */
	public static Boolean getBoolValue(String paramCode){
		return getBoolValue(null,paramCode);
	}
	/**
	 * 通过 参数编号获取金额类型参数值(将自动取当前组织作为查询参数)
	 * @param paramCode  参数编号
	 * @return  参数值
	 */
	public static String getNumValue(String paramCode){
		return getNumValue(null,paramCode);
	}
	/**
	 * 通过参数编号获取字符串参数值(若存在多条参数将返回null)
	 * @param paramNumber
	 * @return
	 */
	public static String getStringValueByNumber(String paramNumber){
		if(StringUtils.isEmpty(paramNumber)){
			return null ;
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("paramNumber", paramNumber);
		param.put("status", 1);
		param.put("hstatus", 1);
		param.put("datatype", ParamTypeEnum.STRING);
		List<ParamLines>  paramItems =  (List<ParamLines>)QueryExecutorImpl.getInstance().execQuery("com.wuyizhiye.basedata.param.dao.ParamLinesDao.select",param, ParamLines.class);
		if(paramItems!=null && paramItems.size()==1){
			return paramItems.get(0).getParamValue();
		}
		return null ;
	}
	/**
	 * 通过参数编号获取布尔参数值(若存在多条参数将返回null)
	 * @param paramNumber
	 * @return
	 */
	public static Boolean getBoolValueByNumber(String paramNumber){
		if(StringUtils.isEmpty(paramNumber)){
			return null ;
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("paramNumber", paramNumber);
		param.put("status", 1);
		param.put("hstatus", 1);
		param.put("datatype", ParamTypeEnum.BOOL);
		List<ParamLines>  paramItems =  (List<ParamLines>)QueryExecutorImpl.getInstance().execQuery("com.wuyizhiye.basedata.param.dao.ParamLinesDao.select",param, ParamLines.class);
		if(paramItems!=null && paramItems.size()==1){
			if("TRUE".equals(paramItems.get(0).getParamValue())){
				return true;
			}else if("FALSE".equals(paramItems.get(0).getParamValue())){
				return false;
			}else{
				return null;
			}
		}
		return null ;
	}
	/**
	 * 通过参数编号获取整数参数值(若存在多条参数将返回null)
	 * @param paramNumber
	 * @return
	 */
	public static Integer getIntValueByNumber(String paramNumber) throws NumberFormatException{
		if(StringUtils.isEmpty(paramNumber)){
			return null ;
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("paramNumber", paramNumber);
		param.put("status", 1);//参数子表状态
		param.put("hstatus", 1);//参数主表状态
		param.put("datatype", ParamTypeEnum.INT);
		List<ParamLines>  paramItems =  (List<ParamLines>)QueryExecutorImpl.getInstance().execQuery("com.wuyizhiye.basedata.param.dao.ParamLinesDao.select",param, ParamLines.class);
		if(paramItems!=null && paramItems.size()==1){
			return Integer.parseInt(paramItems.get(0).getParamValue());
		}
		return null ;
	}
	/**
	 * 通过参数编号获取金额类型参数值(若存在多条参数将返回null)
	 * @param paramNumber
	 * @return
	 */
	public static String getNumValueByNumber(String paramNumber){
		if(StringUtils.isEmpty(paramNumber)){
			return null ;
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("paramNumber", paramNumber);
		param.put("status", 1);
		param.put("hstatus", 1);
		param.put("datatype", ParamTypeEnum.NUMBER);
		List<ParamLines>  paramItems =  (List<ParamLines>)QueryExecutorImpl.getInstance().execQuery("com.wuyizhiye.basedata.param.dao.ParamLinesDao.select",param, ParamLines.class);
		if(paramItems!=null && paramItems.size()==1){
			return paramItems.get(0).getParamValue();
		}
		return null ;
	}
}
