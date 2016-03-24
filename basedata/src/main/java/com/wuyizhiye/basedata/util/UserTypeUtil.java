package com.wuyizhiye.basedata.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.dao.QueryExecutor;
import com.wuyizhiye.base.spring.ApplicationContextAware;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.org.enums.OrgTypeEnum;
import com.wuyizhiye.basedata.org.model.BusinessType;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.person.enums.UserTypeEnum;
import com.wuyizhiye.basedata.person.model.Person;

/**
 * @ClassName UserTypeUtil
 * @Description 用户工具类
 * @author li.biao
 * @date 2015-4-2
 */
public class UserTypeUtil {
	
	private static final Map<UserTypeEnum, UserTypeJudger> JUDGER = new HashMap<UserTypeEnum, UserTypeJudger>();
	private static ThreadLocal<List<BusinessType>> BTYPES = new ThreadLocal<List<BusinessType>>();
	static{
		//管理员
		JUDGER.put(UserTypeEnum.T01, new UserTypeJudger() {
			@Override
			public boolean judge(Person person, Position position) {
				return "admin".equals(person.getUserName());
			}
		});
		//财务管理层
		JUDGER.put(UserTypeEnum.T02, new UserTypeJudger() {
			@Override
			public boolean judge(Person person, Position position) {
				String btypeid = getBusynessTypeId("T01");//标准财务编码T01
				if(StringUtils.isEmpty(btypeid)){
					return false;
				}
				if(StringUtils.isEmpty(position.getBelongOrg().getBusinessTypes())){
					return false;
				}
				if(position.getBelongOrg().getBusinessTypes().contains(btypeid)){
					//有财务业务,并且是主要职位
					return position.getLeading();
				}
				return false;
			}
		});
		//财务人员
		JUDGER.put(UserTypeEnum.T03, new UserTypeJudger() {
			@Override
			public boolean judge(Person person, Position position) {
				String btypeid = getBusynessTypeId("T01");//标准财务编码T01
				if(StringUtils.isEmpty(btypeid)){
					return false;
				}
				if(StringUtils.isEmpty(position.getBelongOrg().getBusinessTypes())){
					return false;
				}
				if(position.getBelongOrg().getBusinessTypes().contains(btypeid)){
					//有财务业务,非主要职位
					return !position.getLeading();
				}
				return false;
			}
		});
		
		//HR管理层
		JUDGER.put(UserTypeEnum.T04, new UserTypeJudger() {
			@Override
			public boolean judge(Person person, Position position) {
				String btypeid = getBusynessTypeId("T02");//人力资源编码T02
				if(StringUtils.isEmpty(btypeid)){
					return false;
				}
				if(StringUtils.isEmpty(position.getBelongOrg().getBusinessTypes())){
					return false;
				}
				if(position.getBelongOrg().getBusinessTypes().contains(btypeid)){
					//有HR业务,非主要职位
					return position.getLeading();
				}
				return false;
			}
		});
		
		//HR人员
		JUDGER.put(UserTypeEnum.T05, new UserTypeJudger() {
			@Override
			public boolean judge(Person person, Position position) {
				String btypeid = getBusynessTypeId("T02");//人力资源编码T02
				if(StringUtils.isEmpty(btypeid)){
					return false;
				}
				if(StringUtils.isEmpty(position.getBelongOrg().getBusinessTypes())){
					return false;
				}
				if(position.getBelongOrg().getBusinessTypes().contains(btypeid)){
					//有HR业务,非主要职位
					return !position.getLeading();
				}
				return false;
			}
		});
		
		//业务员
		JUDGER.put(UserTypeEnum.P01, new UserTypeJudger() {
			@Override
			public boolean judge(Person person, Position position) {
				String btypeid = getBusynessTypeId("F02");//盘源管理编码F02
				if(StringUtils.isEmpty(btypeid)){
					return false;
				}
				if(StringUtils.isEmpty(position.getBelongOrg().getBusinessTypes())){
					return false;
				}
				if(position.getBelongOrg().getBusinessTypes().contains(btypeid)){
					if(position.getBelongOrg().isLeaf()){//必须是叶子节点
						return !position.getLeading();
					}
				}
				return false;
			}
		});
		//经理
		JUDGER.put(UserTypeEnum.P02, new UserTypeJudger() {
			@Override
			public boolean judge(Person person, Position position) {
				String btypeid = getBusynessTypeId("F02");//盘源管理编码F02
				if(StringUtils.isEmpty(btypeid)){
					return false;
				}
				if(StringUtils.isEmpty(position.getBelongOrg().getBusinessTypes())){
					return false;
				}
				if(position.getBelongOrg().getBusinessTypes().contains(btypeid)){
					if(position.getBelongOrg().isLeaf()){//必须是叶子节点的负责人
						return position.getLeading();
					}
				}
				return false;
			}
		});
		
		//秘书
		JUDGER.put(UserTypeEnum.P03, new UserTypeJudger() {
			@Override
			public boolean judge(Person person, Position position) {
				String btypeid = getBusynessTypeId("F02");//盘源管理编码F02
				if(StringUtils.isEmpty(btypeid)){
					return false;
				}
				if(StringUtils.isEmpty(position.getBelongOrg().getBusinessTypes())){
					return false;
				}
				if(position.getBelongOrg().getBusinessTypes().contains(btypeid)){
					//分店非负责人
					if(OrgTypeEnum.STORE.equals(position.getBelongOrg().getOrgType())){
						return !position.getLeading();
					}
				}
				return false;
			}
		});
		
		//业务线管理层
		JUDGER.put(UserTypeEnum.P04, new UserTypeJudger() {
			@Override
			public boolean judge(Person person, Position position) {
				String btypeid = getBusynessTypeId("F02");//盘源管理编码F02
				if(StringUtils.isEmpty(btypeid)){
					return false;
				}
				if(StringUtils.isEmpty(position.getBelongOrg().getBusinessTypes())){
					return false;
				}
				if(position.getBelongOrg().getBusinessTypes().contains(btypeid)){
					if(!position.getBelongOrg().isLeaf()){
						return position.getLeading();
					}
				}
				return false;
			}
		});
		
		//业务线管理层助理
		JUDGER.put(UserTypeEnum.P05, new UserTypeJudger() {
			@Override
			public boolean judge(Person person, Position position) {
				String btypeid = getBusynessTypeId("F02");//盘源管理编码F02
				if(StringUtils.isEmpty(btypeid)){
					return false;
				}
				if(StringUtils.isEmpty(position.getBelongOrg().getBusinessTypes())){
					return false;
				}
				if(position.getBelongOrg().getBusinessTypes().contains(btypeid)){
					if(!position.getBelongOrg().isLeaf()){
						return !position.getLeading();
					}
				}
				return false;
			}
		});
		
		
		//销售员
		JUDGER.put(UserTypeEnum.F01, new UserTypeJudger() {
			@Override
			public boolean judge(Person person, Position position) {
				String btypeid = getBusynessTypeId("F08");//快消业务F08
				if(StringUtils.isEmpty(btypeid)){
					return false;
				}
				if(StringUtils.isEmpty(position.getBelongOrg().getBusinessTypes())){
					return false;
				}
				if(position.getBelongOrg().getBusinessTypes().contains(btypeid)){
					if(position.getBelongOrg().isLeaf()){//必须是叶子节点
						return !position.getLeading();
					}
				}
				return false;
			}
		});
		//销售经理
		JUDGER.put(UserTypeEnum.F02, new UserTypeJudger() {
			@Override
			public boolean judge(Person person, Position position) {
				String btypeid = getBusynessTypeId("F08");//快消业务F08
				if(StringUtils.isEmpty(btypeid)){
					return false;
				}
				if(StringUtils.isEmpty(position.getBelongOrg().getBusinessTypes())){
					return false;
				}
				if(position.getBelongOrg().getBusinessTypes().contains(btypeid)){
					if(position.getBelongOrg().isLeaf()){//必须是叶子节点的负责人
						return position.getLeading();
					}
				}
				return false;
			}
		});
		
		//销售助理
		JUDGER.put(UserTypeEnum.F03, new UserTypeJudger() {
			@Override
			public boolean judge(Person person, Position position) {
				String btypeid = getBusynessTypeId("F08");//快消业务F08
				if(StringUtils.isEmpty(btypeid)){
					return false;
				}
				if(StringUtils.isEmpty(position.getBelongOrg().getBusinessTypes())){
					return false;
				}
				if(position.getBelongOrg().getBusinessTypes().contains(btypeid)){
					//分店非负责人
					if(OrgTypeEnum.STORE.equals(position.getBelongOrg().getOrgType())){
						return !position.getLeading();
					}
				}
				return false;
			}
		});
		
		//销售管理层
		JUDGER.put(UserTypeEnum.F04, new UserTypeJudger() {
			@Override
			public boolean judge(Person person, Position position) {
				String btypeid = getBusynessTypeId("F08");//快消业务F08
				if(StringUtils.isEmpty(btypeid)){
					return false;
				}
				if(StringUtils.isEmpty(position.getBelongOrg().getBusinessTypes())){
					return false;
				}
				if(position.getBelongOrg().getBusinessTypes().contains(btypeid)){
					if(!position.getBelongOrg().isLeaf()){
						return position.getLeading();
					}
				}
				return false;
			}
		});
		
		//新房销售管理层助理
		JUDGER.put(UserTypeEnum.F05, new UserTypeJudger() {
			@Override
			public boolean judge(Person person, Position position) {
				String btypeid = getBusynessTypeId("F08");//快消业务F08
				if(StringUtils.isEmpty(btypeid)){
					return false;
				}
				if(StringUtils.isEmpty(position.getBelongOrg().getBusinessTypes())){
					return false;
				}
				if(position.getBelongOrg().getBusinessTypes().contains(btypeid)){
					if(!position.getBelongOrg().isLeaf()){
						return !position.getLeading();
					}
				}
				return false;
			}
		});
	}
	
	public static void addOrreplaceJudger(UserTypeEnum type, UserTypeJudger judger){
		JUDGER.put(type, judger);
	}
	
	public static boolean is(Person person,Position position,UserTypeEnum type){
		
		Map<String, Object> param = new HashMap<String, Object>();
		//取详细组织信息
		param.put("id", position.getBelongOrg().getId());
		List<Org> orgs = getQueryExecutor().execQuery("com.wuyizhiye.basedata.org.dao.OrgDao.select", param , Org.class);
		if(orgs.size() == 1){
			position.setBelongOrg(orgs.get(0));
		}
		//取业务类型列表
		BTYPES.remove();
		param.clear();
		List<BusinessType> btypes = getQueryExecutor().execQuery("com.wuyizhiye.basedata.org.dao.BusinessTypeDao.select", param, BusinessType.class);
		BTYPES.set(btypes);
		if(JUDGER.containsKey(type)){
			return JUDGER.get(type).judge(person, position);
		}
		return false;
	}
	
	public static UserTypeEnum getUserType(Person person,Position position){
		Map<String, Object> param = new HashMap<String, Object>();
		//取详细组织信息
		param.put("id", position.getBelongOrg().getId());
		List<Org> orgs = getQueryExecutor().execQuery("com.wuyizhiye.basedata.org.dao.OrgDao.select", param , Org.class);
		if(orgs.size() == 1){
			position.setBelongOrg(orgs.get(0));
		}
		//取业务类型列表
		BTYPES.remove();
		param.clear();
		List<BusinessType> btypes = getQueryExecutor().execQuery("com.wuyizhiye.basedata.org.dao.BusinessTypeDao.select", param, BusinessType.class);
		BTYPES.set(btypes);
		UserTypeEnum[] types = UserTypeEnum.values();
		for(UserTypeEnum type : types){
			if(JUDGER.containsKey(type) && JUDGER.get(type).judge(person, position)){
				return type;
			}
		}
		return UserTypeEnum.OTHER;
	}
	
	/**
	 * 编码见业务类型
	 * @param number
	 * @return
	 */
	private static String getBusynessTypeId(String number){
		List<BusinessType> btypes = BTYPES.get();
		for(BusinessType t : btypes){
			if(number.equals(t.getNumber())){
				return t.getId();
			}
		}
		return null;
	}
	
	private static QueryExecutor getQueryExecutor(){
		return ApplicationContextAware.getApplicationContext().getBean("queryExecutor", QueryExecutor.class);
	}
}
