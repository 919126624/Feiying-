package com.wuyizhiye.basedata.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.dao.impl.QueryExecutorImpl;
import com.wuyizhiye.base.spring.ApplicationContextAware;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.org.enums.OrgTypeEnum;
import com.wuyizhiye.basedata.org.model.BusinessType;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.service.BusinessTypeService;
import com.wuyizhiye.basedata.org.service.OrgService;

/**
 * @ClassName OrgUtils
 * @Description 组织工具类
 * @author li.biao
 * @date 2015-4-2
 */
public class OrgUtils {
	
	/**
	 * 判断组织是否包含指定业务类型
	 * @param org 传组织，必须包含businessTypes字段
	 * @param type  业务类型编码,如F01
	 * @return
	 */
	public static boolean isType(Org org,String type){
		BusinessTypeService service = ApplicationContextAware.getApplicationContext().getBean(BusinessTypeService.class);
		List<BusinessType> businessTypes = service.getAllBusinessTypes();
		String existsType = null;
		for(BusinessType t : businessTypes){
			if(!StringUtils.isEmpty(t.getNumber()) && t.getNumber().equals(type)){
				existsType = t.getId();
			}
		}
		if(StringUtils.isEmpty(existsType)){
			throw new RuntimeException("该业务类型不存在:[" + type + ']');
		}
		if(!StringUtils.isEmpty(org.getBusinessTypes()) && org.getBusinessTypes().indexOf(existsType) > -1){
			return true;
		}
		return false;
	}
	
	/**
	 * 根据业务类型编码获取组织
	 * @param type   业务类型编码,如F01
	 * @param param 其它参数,详细见mapper:com.wuyizhiye.basedata.org.dao.OrgDao.select
	 * @return
	 */
	public static List<Org> getOrgsByBusinessType(String type,Map<String,Object> param){
		BusinessTypeService service = ApplicationContextAware.getApplicationContext().getBean(BusinessTypeService.class);
		List<BusinessType> businessTypes = service.getAllBusinessTypes();
		String existsType = null;
		for(BusinessType t : businessTypes){
			if(!StringUtils.isEmpty(t.getNumber()) && t.getNumber().equals(type)){
				existsType = t.getId();
			}
		}
		if(StringUtils.isEmpty(existsType)){
			throw new RuntimeException("该业务类型不存在:[" + type + ']');
		}
		if(param == null){
			param = new HashMap<String, Object>();
		}
		param.put("businessType", existsType);
		return QueryExecutorImpl.getInstance().execQuery("com.wuyizhiye.basedata.org.dao.OrgDao.select", param, Org.class);
	}
	
	
	/**
	 * 获取历史组织架构树
	 * @param param 参数,详细见mapper:com.wuyizhiye.basedata.org.dao.OrgHistoryDao.getHistoryOrg 中的定义
	 * @param isAllOrg  是否返回当前组织的所有下级组织 , 
	 * @return
	 */
	public static List<Org> getHistoryOrgList(Map<String,Object> param,boolean isAllOrg){
		if(param==null || param.get("begDate")==null || param.get("endDate")==null){
			return null;
		}
		List<Org>  orgList = new ArrayList<Org>();
		if(isAllOrg && StringUtils.isNotNull(param.get("id"))){
			 
			Map<String,Object> p = new HashMap<String, Object>();
			  p.put("id",param.get("id"));
			  p.put("begDate", param.get("begDate"));
			  p.put("endDate", param.get("endDate"));
			  List<Org> orgHis = QueryExecutorImpl.getInstance().execQuery("com.wuyizhiye.basedata.org.dao.OrgHistoryDao.getHistoryOrg", p, Org.class);
			  if(orgHis==null){
				  orgHis  = new ArrayList<Org>();
			  }
			  for(Org o : orgHis){
				  param.put("longnumber", o.getLongNumber());
				  List<Org> childOrg = QueryExecutorImpl.getInstance().execQuery("com.wuyizhiye.basedata.org.dao.OrgHistoryDao.getHistoryOrg", param, Org.class);
				  if(childOrg!=null && childOrg.size()>0){
					  orgList.addAll(childOrg);
				  }
			  }
		}else{
			orgList = QueryExecutorImpl.getInstance().execQuery("com.wuyizhiye.basedata.org.dao.OrgHistoryDao.getHistoryOrg", param, Org.class);
		}
		return orgList;
	}
	
	/**
	 * 获得指定部门所在的CU
	 * @param id
	 * @param isExistsCadre 是否包含虚拟本部
	 * @return
	 */
	public static Org getCUByOrg(String id,boolean isExistsCadre){
		OrgService orgService = ApplicationContextAware.getApplicationContext().getBean(OrgService.class);
		Org org=orgService.getEntityById(id);
		Map<String,Object> pam=new HashMap<String, Object>();
		pam.put("childLongNumber", org.getLongNumber());
		String orgTyps="'"+OrgTypeEnum.GROUP+"','"+OrgTypeEnum.SON_GROUP+"','"+OrgTypeEnum.COMPANY+"','"+OrgTypeEnum.NDEPENDENT_COMPANY+"','"+OrgTypeEnum.BRANCH_COMPANY+"'";
		if(isExistsCadre){
			orgTyps+=",'"+OrgTypeEnum.VIRTUAL_CADRE+"'"; 
		}
		
		pam.put("orgTypes", orgTyps);
		
		List<Org> orgList=QueryExecutorImpl.getInstance().execQuery("com.wuyizhiye.basedata.org.dao.OrgDao.select", pam, Org.class);
		if(orgList!=null && orgList.size()>0){
			return orgList.get(orgList.size()-1);
		}
		return null;
	}
	
	/**
	 * 获得指定部门所在的CU  不包括虚拟本部 组织类型
	 * @param id
	 * @return
	 */
	public static Org getCUByOrg(String id){
		return getCUByOrg(id, false);
	}
	
	/**
	 * 判断该部门是否属于CU（根据部门类型判断）
	 * @param o
	 * @return
	 */
	public static boolean isCU(Org o){
		if(o!=null && o.getOrgType()!=null && (o.getOrgType().equals(OrgTypeEnum.GROUP) || o.getOrgType().equals(OrgTypeEnum.SON_GROUP)  
				|| o.getOrgType().equals(OrgTypeEnum.COMPANY) || o.getOrgType().equals(OrgTypeEnum.NDEPENDENT_COMPANY) || o.getOrgType().equals(OrgTypeEnum.BRANCH_COMPANY)) ){
			
			return true;
			
		}
		return false;
	}
}
