
package com.wuyizhiye.hr.attendance.service.impl;

import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.ObjectCopyUtils;
import com.wuyizhiye.basedata.org.dao.OrgDao;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.person.dao.PersonDao;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.model.PersonPositionHistory;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.hr.attendance.dao.AttendanceDao;
import com.wuyizhiye.hr.attendance.dao.AttendanceDetailDao;
import com.wuyizhiye.hr.attendance.model.Attendance;
import com.wuyizhiye.hr.attendance.model.AttendanceDetail;
import com.wuyizhiye.hr.attendance.model.AttendanceRule;
import com.wuyizhiye.hr.attendance.service.AttendanceService;
import com.wuyizhiye.hr.enums.AttendanceRuleTypeEnum;
import com.wuyizhiye.hr.enums.AttendanceStatusEnum;
import com.wuyizhiye.hr.enums.PositionChangeTypeEnum;
import com.wuyizhiye.hr.utils.CommonExcelExportUtils;

/**
 * 考勤
 * @author ouyangyi
 * @since 2013-5-17 下午03:17:13
 */
@Component(value="attendanceService")
@Transactional
public class AttendanceServiceImpl extends BaseServiceImpl<Attendance> implements AttendanceService {

	@Resource
	private AttendanceDao attendanceDao;
	
	@Resource
	private OrgDao orgDao;
	
	@Autowired
	private PersonDao personDao;
	
	@Resource
	private AttendanceDetailDao attendanceDetailDao;
	
	@Override
	protected BaseDao getDao() {
		return attendanceDao;
	}
	
	/**
	 * 根据考勤日期或调职类型获取任职历史记录
	 * @param allPositionHistorys XX人所有主要职位的任职历史记录
	 * @param recordDate 考勤日期
	 * @param type 调职类型
	 * @param minBeginDate 考勤月第一天
	 * @param maxBeginDate 考勤月最后一天
	 * @return
	 */
	private PersonPositionHistory getPositionHistory(List<PersonPositionHistory> allPositionHistorys,Date recordDate,PositionChangeTypeEnum type,
			Date minBeginDate,Date maxBeginDate){
		PersonPositionHistory pstHis = null;
		if(allPositionHistorys==null || allPositionHistorys.size()<1){
			return pstHis;
		}
		for(PersonPositionHistory his:allPositionHistorys){
			if(type==null){
				if(recordDate!=null && his.getEffectdate()!=null && recordDate.compareTo(his.getEffectdate())>=0
						&& (his.getExpirydate()==null || recordDate.compareTo(his.getExpirydate())<=0)){
					//录考勤日期 所在的任职记录
					pstHis  = his;
					break;
				}
			}else if(PositionChangeTypeEnum.ENROLL.equals(type) && type.toString().equals(his.getChangeType()) 
					&& his.getEffectdate()!=null && minBeginDate!=null && maxBeginDate!=null
					&& his.getEffectdate().compareTo(minBeginDate)>=0
					&& his.getEffectdate().compareTo(maxBeginDate)<=0 ){
				//录考勤日期为跑盘员 当月最后一次入职 记录
				pstHis  = his;
				break;
			}else if(PositionChangeTypeEnum.LEAVE.equals(type) && type.toString().equals(his.getChangeType()) 
					&& his.getEffectdate()!=null && minBeginDate!=null && maxBeginDate!=null
					&& his.getEffectdate().compareTo(minBeginDate)>=0
					&& his.getEffectdate().compareTo(maxBeginDate)<=0 ){
				//录考勤日期为跑盘员 当月最后一次离职 记录
				pstHis  = his;
				break;
			}else if(type.toString().equals(his.getChangeType())){
				//根据类型 和最大/最小时间 取最后近一条符合条件的任职记录 
				if((maxBeginDate==null || (his.getEffectdate()!=null && his.getEffectdate().compareTo(maxBeginDate)<=0))
						&&(minBeginDate==null || (his.getEffectdate()!=null && his.getEffectdate().compareTo(minBeginDate)>=0))){
				 pstHis  = his;
				 break;
				}
			}
		}
		
		return pstHis;
	}
	
	public List<Person> getAttendancePerson(){
		//查询登录人组织下 所有员工（包含离职人员）
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("includeChild", "Y");//是否包含下级组织
		params.put("orgLongNumber", SystemUtil.getCurrentOrg().getLongNumber());
		List<Person> listp = this.queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonDao.getDismissionPersonByOrg",params, Person.class);
		if(listp==null || listp.size()<1){
			return listp;
		}
		for(Person p:listp){
			
		}
		return listp;
	}
	
	/**
	 * 根据员工ID从所有任职历史中过滤
	 * @param allphis
	 * @param personid
	 * @return
	 */
	public List<PersonPositionHistory> getPositionHistoryByPersonId(List<PersonPositionHistory> allphis ,String personid){
		List<PersonPositionHistory> personHis = new ArrayList<PersonPositionHistory>();
		if(allphis == null || StringUtils.isEmpty(personid)){
			return personHis;
		}
		for(PersonPositionHistory  his:allphis){
			if(personid.equals(his.getPersonId())){
				personHis.add(his);
			}
		}
		return personHis;
	}
	
	
	/**
	 * 考勤录入 查询考勤人员(登录人组织下的人员)
	 * @param cond
	 * @return
	 */
	public Map<String, Object> query(Map<String, Object> cond){
		Map<String,Object> result = new HashMap<String,Object>();
		List<AttendanceDetail> detailList = new ArrayList<AttendanceDetail>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String recordDateStr = cond.get("recordDate").toString();
		String orgId = cond.get("orgId").toString();
		String keyConditions = cond.get("keyConditions").toString();
		Date recordDate=null;
		String errMsg = "";
		try {
			recordDate = sdf.parse(recordDateStr);
		} catch (ParseException e) {
			result.put("MSG", "考勤日期格式不对");
			return result;
		}
		//查询登录人组织下 所有员工（包含离职人员）
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtils.isNotEmpty(orgId)){
			params.put("orgId", orgId);
		}else{
		 params.put("includeChild", "Y");//是否包含下级组织
		 params.put("orgLongNumber", SystemUtil.getCurrentOrg().getLongNumber());
		}
		if(StringUtils.isNotEmpty(keyConditions)){
		 params.put("key", keyConditions);
		}
		params.put("orderByClause", "O.FLONGNUMBER");
		List<Person> listp = this.queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonDao.getDismissionPersonByOrg",params, Person.class);
		if(listp==null || listp.size()<1){
			result.put("MSG", "登录人组织下没有人员");
			return result;
		}
		String personIdsIn = "";
		for(Person p:listp){
			if("".equals(personIdsIn)){
				personIdsIn = "'"+p.getId()+"'";
			}else{
				personIdsIn += ",'"+p.getId()+"'";
			}
		}
		
		//取得 人员 所有任职历史记录 (按 生效日期和失效日期排序)
		params.clear();
		params.put("personIdsIn", personIdsIn);
		params.put("primary",1);
		params.put("orderByClause","HIS.FKPERSON_ID, HIS.FEFFECTDATE desc ,(case when HIS.FEXPIRYDATE is null then 1 else 0 end) desc ,HIS.FEXPIRYDATE desc ");
		List<PersonPositionHistory> allPersonphis = this.queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonPositionHistoryDao.selectByExample",params, PersonPositionHistory.class);
		
		for(Person p:listp){
			
			AttendanceDetail detail = new AttendanceDetail();
			params.clear();
			params.put("personId", p.getId());
			params.put("recordDate",recordDate);
			//查询当人当天的考勤记录
			List<AttendanceDetail> details = this.queryExecutor.execQuery("com.wuyizhiye.hr.attendance.dao.AttendanceDetailDao.selectListByCond",params, AttendanceDetail.class);
			if(details!=null && details.size()>0){
				detail = details.get(0);
			}else{
				//取得 人员 所有任职历史记录 (按 生效日期和失效日期排序)
				List<PersonPositionHistory> allphis = getPositionHistoryByPersonId(allPersonphis,p.getId());
				/*params.clear();
				params.put("personId", p.getId());
				params.put("primary",1);
				params.put("orderByClause"," HIS.FEFFECTDATE desc ,HIS.FEXPIRYDATE desc ");*/
					//this.queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonPositionHistoryDao.selectByExample",params, PersonPositionHistory.class);
				if(allphis==null || allphis.size()<1){
					//任职历史记录 异常 不能做考勤
					continue;
				}
				
				//录考勤日期 所在的任职记录
				PersonPositionHistory recordPstHis = getPositionHistory(allphis,recordDate,null,null,null);
				if(recordPstHis==null){
					// 根据录考勤日期找不到任职历史  不符合实际 
					continue;
				}
				if(PositionChangeTypeEnum.DELRUNDISK.toString().equals(recordPstHis.getChangeType()) ||
						PositionChangeTypeEnum.LEAVE.toString().equals(recordPstHis.getChangeType())){
					//如果是考勤当月  删除跑盘或离职 才能录考勤
					Date firstDay = DateUtil.getFirstDay(recordDate);
					Date lastDay = DateUtil.getLastDay(recordDate);
					Date effectDate = recordPstHis.getEffectdate();//离职 或删除跑盘 生效日期
					if(effectDate!=null && firstDay.compareTo(effectDate)<=0
							&& lastDay.compareTo(effectDate)>=0){
						detail.setOrgId(recordPstHis.getOldOrgId());
						detail.setOrgName(recordPstHis.getOldOrgName());
						detail.setPositionId(recordPstHis.getOldPositionId());
						detail.setPositionName(recordPstHis.getOldPositionName());
						detail.setJobLevelId(recordPstHis.getOldJobLevel());
						detail.setJobLevelName(recordPstHis.getOldJobLevelName());
						detail.setNoEntry(1);//离职 人员默认 未入职
					}else{
						//不能录考勤
						continue;
					}
				}else{
					detail.setOrgId(recordPstHis.getChangeOrgId());
					detail.setOrgName(recordPstHis.getChangeOrgName());
					detail.setPositionId(recordPstHis.getChangePositionId());
					detail.setPositionName(recordPstHis.getChangePositionName());
					detail.setJobLevelId(recordPstHis.getChangeJobLevel());
					detail.setJobLevelName(recordPstHis.getChangeJobLevelName());
					detail.setActualDay(1);//非离职 人员默认  出勤
				}
				if(StringUtils.isEmpty(detail.getOrgId()) || StringUtils.isEmpty(detail.getPositionId())
						|| StringUtils.isEmpty(detail.getJobLevelId())){
					errMsg += "  "+ p.getName()+ " 组织或职位或职级为空";
				}
				detail.setRecordDate(recordDate);
				detail.setPersonId(p.getId());
				detail.setPersonName(p.getName());
				detail.setPersonNumber(p.getNumber());
			}
			detailList.add(detail);
		}
		result.put("errMsg", errMsg);//用于调试时 页面提示
		result.put("MSG", "SUCC");
		result.put("detailList",detailList);
		return result;
	}
	
	/**
	 * 保存录入信息
	 * @throws ParseException 
	 */
	@Transactional
	public Map<String, Object> saveEntry(Map<String, Object> data) throws Exception {
		
		Map<String,Object> result = new HashMap<String,Object>();
		String recordDateStr = (String) data.get("recordDate");
		List<AttendanceDetail> list =  (List<AttendanceDetail>) data.get("data");
		Date nowTime = new Date();
		Map<String,Object> params = new HashMap<String,Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat periodDf = new SimpleDateFormat("yyyy-MM");
		Date recordDate=null;
		try {
			recordDate = sdf.parse(recordDateStr);
		}catch (ParseException e) {
			result.put("MSG", "考勤日期格式不对");
			return result;
		}
		String period = periodDf.format(recordDate);
		Date firstDay = DateUtil.getFirstDay(recordDate);//考勤月 第一天
		Date lastDay = DateUtil.getLastDay(recordDate);//考勤月 最后一天
		 
		for(AttendanceDetail detail : list){
			Attendance attendance = new Attendance();
			
			//查找是否有月统计记录
			params.clear();
			params.put("personId", detail.getPersonId());
			params.put("period", period);
			params.put("orgId", detail.getOrgId());
			List<Attendance> attendances = this.queryExecutor.execQuery("com.wuyizhiye.hr.attendance.dao.AttendanceDao.getAttendanceByCond",params, Attendance.class);
			if(attendances == null || attendances.size() == 0 ){
				//基本属性
				attendance.setUUID();
				attendance.setCreator(SystemUtil.getCurrentUser());
				attendance.setCreateTime(nowTime);
				attendance.setUpdator(SystemUtil.getCurrentUser());
				attendance.setLastUpdateTime(nowTime);
				//考勤期间
				attendance.setPeriod(period);		  //考勤期间(年-月份)
				attendance.setMonthFirstDay(firstDay);//考勤月 第一天
				attendance.setMonthLastDay(lastDay);  //考勤月 最后一天
				//考勤人员
				attendance.setPersonId(detail.getPersonId());
				attendance.setPersonNumber(detail.getPersonNumber());
				attendance.setPersonName(detail.getPersonName());
				attendance.setOrgId(detail.getOrgId());
				attendance.setOrgName(detail.getOrgName());
				attendance.setPositionId(detail.getPositionId());
				attendance.setPositionName(detail.getPositionName());
				attendance.setJobLevelId(detail.getJobLevelId());
				attendance.setJobLevelName(detail.getJobLevelName());
				//考勤项
 				attendance.setActualDay(detail.getActualDay());			//实际出勤天数
				attendance.setLate(detail.getLate());					//迟到次数
				attendance.setLeaveEarly(detail.getLeaveEarly());		//早退次数
				attendance.setSkipWork(detail.getSkipWork());			//旷工次数
				attendance.setCompassionateLeave(detail.getCompassionateLeave());//事假（天）
				attendance.setSickLeave(detail.getSickLeave());			//病假（天）
				attendance.setRestDays(detail.getRestDays());			//休息（天）
				attendance.setAnnualLeave(detail.getAnnualLeave());		//年假（天）
				attendance.setMarriageLeave(detail.getMarriageLeave()); //婚假（天）
				attendance.setBereavementLeave(detail.getBereavementLeave());//丧假（天）
				attendance.setIPPFLeave(detail.getIPPFLeave());			//计生假（天）
				attendance.setSkipWorkLeave(detail.getSkipWorkLeave()); //旷工（天）
				attendance.setNoEntry(detail.getNoEntry());				//未入职天数
				attendance.setOtherHolidays(detail.getOtherHolidays()); //其它假期(天)
				//备用 考勤项
				attendance.setOtherHolidays1(detail.getOtherHolidays1());
				attendance.setOtherHolidays2(detail.getOtherHolidays2());
				attendance.setOtherHolidays3(detail.getOtherHolidays3());
				attendance.setOtherHolidays4(detail.getOtherHolidays4());
				attendance.setOtherHolidays5(detail.getOtherHolidays5());
				
				attendance.setLastEnterDate(recordDate);//录入的最大日期
				attendance.setApprovalState(AttendanceStatusEnum.SAVE.toString());//审核状态未提交
				attendance.setRuleType(getAttendanceType(attendance.getOrgId())); //考勤制
				if(!StringUtils.isEmpty(attendance.getPersonId())){	
					Person person=personDao.getEntityById(attendance.getPersonId());
					attendance.setControlUnit(person.getControlUnit());
				}
				attendanceDao.addEntity(attendance);
			}else{
				attendance = attendances.get(0);
				//已提交(已提交 、审核)的不让修改   跳过保存
				if(AttendanceStatusEnum.SUBMIT.toString().equals(attendance.getApprovalState())
						||AttendanceStatusEnum.APPROVED.toString().equals(attendance.getApprovalState())){
					continue;
				}
				 
				//先删除明细数据，再保存
				params.clear();
				params.put("personId", detail.getPersonId());
				params.put("recordDate", recordDate);
				List<AttendanceDetail> attendanceDetails = this.queryExecutor.execQuery("com.wuyizhiye.hr.attendance.dao.AttendanceDetailDao.selectListByCond",params, AttendanceDetail.class);
				AttendanceDetail oldDetail = new AttendanceDetail();
				if(attendanceDetails!=null && attendanceDetails.size()>0){
					oldDetail = attendanceDetails.get(0);
					for(AttendanceDetail d :attendanceDetails){
				    	attendanceDetailDao.deleteById(d.getId());
					}
				} 
				
				attendance.setUpdator(SystemUtil.getCurrentUser());
				attendance.setLastUpdateTime(nowTime);
				DecimalFormat decf = new DecimalFormat("0.00");
				//考勤项
 				attendance.setActualDay(Double.valueOf(decf.format(attendance.getActualDay()-oldDetail.getActualDay()+detail.getActualDay())));			//实际出勤天数
				attendance.setLate(attendance.getLate()-oldDetail.getLate()+detail.getLate());					//迟到次数
				attendance.setLeaveEarly(attendance.getLeaveEarly()-oldDetail.getLeaveEarly()+detail.getLeaveEarly());		//早退次数
				attendance.setSkipWork(attendance.getSkipWork()-oldDetail.getSkipWork()+detail.getSkipWork());			//旷工次数
				attendance.setCompassionateLeave(Double.valueOf(decf.format(attendance.getCompassionateLeave()-oldDetail.getCompassionateLeave()+detail.getCompassionateLeave())));//事假（天）
				attendance.setSickLeave(Double.valueOf(decf.format(attendance.getSickLeave()-oldDetail.getSickLeave()+detail.getSickLeave())));			//病假（天）
				attendance.setRestDays(Double.valueOf(decf.format(attendance.getRestDays()-oldDetail.getRestDays()+detail.getRestDays())));			//休息（天）
				attendance.setAnnualLeave(Double.valueOf(decf.format(attendance.getAnnualLeave()-oldDetail.getAnnualLeave()+detail.getAnnualLeave())));		//年假（天）
				attendance.setMarriageLeave(Double.valueOf(decf.format(attendance.getMarriageLeave()-oldDetail.getMarriageLeave()+detail.getMarriageLeave()))); //婚假（天）
				attendance.setBereavementLeave(Double.valueOf(decf.format(attendance.getBereavementLeave()-oldDetail.getBereavementLeave()+detail.getBereavementLeave())));//丧假（天）
				attendance.setIPPFLeave(Double.valueOf(decf.format(attendance.getIPPFLeave()-oldDetail.getIPPFLeave()+detail.getIPPFLeave())));			//计生假（天）
				attendance.setSkipWorkLeave(Double.valueOf(decf.format(attendance.getSkipWorkLeave()-oldDetail.getSkipWorkLeave()+detail.getSkipWorkLeave()))); //旷工（天）
				attendance.setNoEntry(Double.valueOf(decf.format(attendance.getNoEntry()-oldDetail.getNoEntry()+detail.getNoEntry())));				//未入职天数
				attendance.setOtherHolidays(Double.valueOf(decf.format(attendance.getOtherHolidays()-oldDetail.getOtherHolidays()+detail.getOtherHolidays()))); //其它假期(天)
				//备用 考勤项
				attendance.setOtherHolidays1(Double.valueOf(decf.format(attendance.getOtherHolidays1()-oldDetail.getOtherHolidays1()+detail.getOtherHolidays1())));
				attendance.setOtherHolidays2(Double.valueOf(decf.format(attendance.getOtherHolidays2()-oldDetail.getOtherHolidays2()+detail.getOtherHolidays2())));
				attendance.setOtherHolidays3(Double.valueOf(decf.format(attendance.getOtherHolidays3()-oldDetail.getOtherHolidays3()+detail.getOtherHolidays3())));
				attendance.setOtherHolidays4(Double.valueOf(decf.format(attendance.getOtherHolidays4()-oldDetail.getOtherHolidays4()+detail.getOtherHolidays4())));
				attendance.setOtherHolidays5(Double.valueOf(decf.format(attendance.getOtherHolidays5()-oldDetail.getOtherHolidays5()+detail.getOtherHolidays5())));
				
				if(attendance.getLastEnterDate()!=null && 
						attendance.getLastEnterDate().compareTo(recordDate)<0){
					attendance.setLastEnterDate(recordDate);//录入的最大日期
					//一个人 一个有月 在一下组织下 只存一条记录
					//月考勤里的 职位 、职级只存组织下 这个月最后一天的职位职级
					attendance.setPositionId(detail.getPositionId());
					attendance.setPositionName(detail.getPositionName());
					attendance.setJobLevelId(detail.getJobLevelId());
					attendance.setJobLevelName(detail.getJobLevelName());
				}
				attendance.setRuleType(getAttendanceType(attendance.getOrgId())); //考勤制
				if(!StringUtils.isEmpty(attendance.getPersonId())){	
					Person person=personDao.getEntityById(attendance.getPersonId());
					attendance.setControlUnit(person.getControlUnit());
				}
				attendanceDao.updateEntity(attendance);
			}
			
			//保存考勤明细 
			//基本属性
			detail.setUUID();
			detail.setCreator(SystemUtil.getCurrentUser());
			detail.setCreateTime(nowTime);
			detail.setUpdator(SystemUtil.getCurrentUser());
			detail.setLastUpdateTime(nowTime);
			//考勤期间
			detail.setPeriod(period);	  //考勤期间(年-月份)
			detail.setRecordDate(recordDate);//考勤日期 
			detail.setParent(attendance); //月考勤
			detail.setApprovalState(AttendanceStatusEnum.SAVE.toString());//审核状态未提交
			
			attendanceDetailDao.addEntity(detail);
		}	
			
		result.put("MSG", "SUCC");
		 
		return result;
	}
	
	/**
	 * 月考勤查询（查询指定期间内的统计情况） -- 分页
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryMonthByCondPerPage(Pagination<Attendance> pagination,Map<String, Object> cond) {
		Map<String,Object> result = new HashMap<String,Object>();
		
		String period = (String) cond.get("period");
		String keyConditions = (String) cond.get("keyConditions");
		String orgLongNum = (String) cond.get("orgLongNum");
		String approvalState = (String) cond.get("approvalState");
		String fromPage = (String) cond.get("fromPage");
		if(StringUtils.isEmpty(orgLongNum)){
			//orgLongNum = SystemUtil.getCurrentOrg().getLongNumber();
			Date recordDate = DateUtil.convertStrToDate(period, "yyyy-MM");
			recordDate = DateUtil.getLastDay(recordDate);//考勤月 最后一天
			Map<String,Object> p = new HashMap<String, Object>();
			  p.put("id",SystemUtil.getCurrentOrg().getId());
			  p.put("begDate", recordDate);
			  p.put("endDate", recordDate);
			  List<Map> orgHis = queryExecutor.execQuery("com.wuyizhiye.basedata.org.dao.OrgHistoryDao.getHistoryOrgTree", p, Map.class);
			  if(orgHis!=null && orgHis.size()>0){
				  orgLongNum =  orgHis.get(0).get("longNumber")==null?"NO_DATA":orgHis.get(0).get("longNumber").toString();
			  }else{
				  orgLongNum = "NO_DATA";
			  }
		}
		List<Attendance> attendanceList = new ArrayList<Attendance>();
		//根据组织和考勤期间 查询月考勤(只显示员工 考勤月最后一天 所在组织的月考勤)
		Map<String,Object> params = new HashMap<String,Object>();
		if("queryPortlet".equals(fromPage)){
			//首页查看考勤  如果登录人不是领导则只能查看自己的考勤
			Position position = SystemUtil.getCurrentPosition();
			if(position==null || !position.getLeading()){
				params.put("personId", SystemUtil.getCurrentUser().getId());
			}
		}
		params.put("key", keyConditions);
		params.put("period", period);
		params.put("orgLongNum", orgLongNum);
		params.put("approvalState", approvalState);
		params.put("orderByClause", "D.FORGNAME ");
		List<Attendance> attendances = null;//this.queryExecutor.execQuery("com.wuyizhiye.hr.attendance.dao.AttendanceDao.getMonthAttendanceByCond",params, Attendance.class);
		
		//如果是集团的id的话，则不控制CU数据隔离
		if(!"000000000000000000000000000000F".equalsIgnoreCase(SystemUtil.getCurrentControlUnit().getId())){
			params.put("controlUnitId", SystemUtil.getCurrentControlUnit().getId());	//得到控制单元的Id
		}
		pagination = queryExecutor.execQuery("com.wuyizhiye.hr.attendance.dao.AttendanceDao.getMonthAttendanceByCond", pagination, params);
		result.put("pagination", pagination);
		if(pagination==null || pagination.getItems()==null || pagination.getItems().size()<1){
			result.put("MSG", period+"期间 还没录考勤信息");
			result.put("attendanceList", attendanceList);
			return result;
		}
		attendances = (List<Attendance>)pagination.getItems();
		String personIdsIn = "";
		for(Attendance a :attendances){
			if("".equals(personIdsIn)){
				personIdsIn = "'"+a.getPersonId()+"'";
			}else{
				personIdsIn += ",'"+a.getPersonId()+"'";
			}
		}
		params.clear();
		params.put("personIdsIn", personIdsIn);
		params.put("primary",1);
		params.put("orderByClause","HIS.FKPERSON_ID, HIS.FEFFECTDATE desc ,(case when HIS.FEXPIRYDATE is null then 1 else 0 end) desc ,HIS.FEXPIRYDATE desc");
		List<PersonPositionHistory> allPersonphis = this.queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonPositionHistoryDao.selectByExample",params, PersonPositionHistory.class);
		
		for(Attendance a :attendances){
			
			//员工当月最后所在组织录入的考勤
			List<PersonPositionHistory> allphis = getPositionHistoryByPersonId(allPersonphis, a.getPersonId());
			/*params.clear();
			params.put("personId", a.getPersonId());
			params.put("primary",1);
			params.put("orderByClause"," HIS.FEFFECTDATE desc ,HIS.FEXPIRYDATE desc ");
			List<PersonPositionHistory> allphis = this.queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonPositionHistoryDao.selectByExample",params, PersonPositionHistory.class);
			*/
			if(allphis==null || allphis.size()<1){
				//任职历史记录 异常 不能做考勤
				continue;
			}
			//考勤月最后一天的任职记录
			PersonPositionHistory phis = getPositionHistory(allphis,a.getMonthLastDay(),null,null,null);
			if(phis==null){
				continue;
			}
			if(PositionChangeTypeEnum.LEAVE.toString().equals(phis.getChangeType())){
				//当月离职 离职前的组织 
				if(phis.getOldOrgId() ==null || !phis.getOldOrgId().equals(a.getOrgId())){
						continue;
					}
			}else{
				if(phis.getChangeOrgId() ==null || !phis.getChangeOrgId().equals(a.getOrgId())){
					continue;
				}
			}
			
			//查月考勤明细  然后汇总
			Attendance attendance = setAttendanceByDetails(a);
			if(attendance != null){
				attendanceList.add(attendance);
			}
		}
		if(attendanceList==null || attendanceList.size()<1){
			result.put("MSG", period+"期间  "+SystemUtil.getCurrentOrg().getName()+" 组织下 还没录考勤信息");
			result.put("attendanceList", attendanceList);
			return result;
		}
		pagination.setItems(attendanceList);
		result.put("attendanceList", attendanceList);
		result.put("MSG","SUCC");
		return  result;
	}
	
	/**
	 * 考勤查询（查询指定期间内的统计情况） --全部  不分页
	 */
	public Map<String, Object> queryMonthByCond(Map<String, Object> cond){
		Map<String,Object> result = new HashMap<String,Object>();
		
		String period = (String) cond.get("period");
		String keyConditions = (String) cond.get("keyConditions");
		String orgLongNum = (String) cond.get("orgLongNum");
		String approvalState = (String) cond.get("approvalState");
		if(StringUtils.isEmpty(orgLongNum)){
			Date recordDate = DateUtil.convertStrToDate(period, "yyyy-MM");
			recordDate = DateUtil.getLastDay(recordDate);//考勤月 最后一天
			Map<String,Object> p = new HashMap<String, Object>();
			  p.put("id",SystemUtil.getCurrentOrg().getId());
			  p.put("begDate", recordDate);
			  p.put("endDate", recordDate);
			  List<Map> orgHis = queryExecutor.execQuery("com.wuyizhiye.basedata.org.dao.OrgHistoryDao.getHistoryOrgTree", p, Map.class);
			  if(orgHis!=null && orgHis.size()>0){
				  orgLongNum =  orgHis.get(0).get("longNumber")==null?"NO_DATA":orgHis.get(0).get("longNumber").toString();
			  }else{
				  orgLongNum = "NO_DATA";
			  }
		}
		List<Attendance> attendanceList = new ArrayList<Attendance>();
		//根据组织和考勤期间 查询月考勤(只显示员工 考勤月最后一天 所在组织的月考勤)
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("key", keyConditions);
		params.put("period", period);
		params.put("orgLongNum", orgLongNum);
		params.put("approvalState", approvalState);
		params.put("orderByClause", "D.FORGNAME ");
		List<Attendance> attendances = this.queryExecutor.execQuery("com.wuyizhiye.hr.attendance.dao.AttendanceDao.getMonthAttendanceByCond",params, Attendance.class);
		if(attendances==null || attendances.size()<1){
			result.put("MSG", period+"期间 还没录考勤信息");
			result.put("attendanceList", attendanceList);
			return result;
		}
		String personIdsIn = "";
		for(Attendance a :attendances){
			if("".equals(personIdsIn)){
				personIdsIn = "'"+a.getPersonId()+"'";
			}else{
				personIdsIn += ",'"+a.getPersonId()+"'";
			}
		}
		params.clear();
		params.put("personIdsIn", personIdsIn);
		params.put("primary",1);
		params.put("orderByClause","HIS.FKPERSON_ID, HIS.FEFFECTDATE desc ,(case when HIS.FEXPIRYDATE is null then 1 else 0 end) desc ,HIS.FEXPIRYDATE desc");
		List<PersonPositionHistory> allPersonphis = this.queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonPositionHistoryDao.selectByExample",params, PersonPositionHistory.class);
		
		for(Attendance a :attendances){
			
			//员工当月最后所在组织录入的考勤
			List<PersonPositionHistory> allphis = getPositionHistoryByPersonId(allPersonphis, a.getPersonId());
			if(allphis==null || allphis.size()<1){
				//任职历史记录 异常 不能做考勤
				continue;
			}
			//考勤月最后一天的任职记录
			PersonPositionHistory phis = getPositionHistory(allphis,a.getMonthLastDay(),null,null,null);
			if(phis==null){
				continue;
			}
			if(PositionChangeTypeEnum.LEAVE.toString().equals(phis.getChangeType())){
				//当月离职 离职前的组织 
				if(phis.getOldOrgId() ==null || !phis.getOldOrgId().equals(a.getOrgId())){
						continue;
					}
			}else{
				if(phis.getChangeOrgId() ==null || !phis.getChangeOrgId().equals(a.getOrgId())){
					continue;
				}
			}
			
			//查月考勤明细  然后汇总
			Attendance attendance = setAttendanceByDetails(a);
			if(attendance != null){
				attendanceList.add(attendance);
			}
		}
		if(attendanceList==null || attendanceList.size()<1){
			result.put("MSG", period+"期间  "+SystemUtil.getCurrentOrg().getName()+" 组织下 还没录考勤信息");
			result.put("attendanceList", attendanceList);
			return result;
		}
		result.put("attendanceList", attendanceList);
		result.put("MSG","SUCC");
		return  result;
	}
	
	/**
	 * 根据月考勤明细 汇总考勤数据
	 * @param a
	 */
	private Attendance setAttendanceByDetails(Attendance a){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("personId", a.getPersonId());
		params.put("period", a.getPeriod());
		params.put("orderByClause", "D.FRECORDDATE ");
		List<AttendanceDetail> attendanceDetails = this.queryExecutor.execQuery("com.wuyizhiye.hr.attendance.dao.AttendanceDetailDao.selectListByCond",params, AttendanceDetail.class);
		if(attendanceDetails  ==null || attendanceDetails.size()<1){
			return null;
		}
		 double actualDay =0D;				//实际出勤天数
		 int late =0;						//迟到次数
		 int leaveEarly =0;				//早退次数
		 int skipWork =0;					//旷工次数
		 double compassionateLeave =0D;		//事假（天）
		 double sickLeave =0D;				//病假（天）
		 double restDays =0D;				//休息（天）
		 double annualLeave =0D;			//年假（天）
		 double marriageLeave =0D;			//婚假（天）
		 double bereavementLeave =0D;		//丧假（天）
		 double IPPFLeave =0D;				//计生假（天）
		 double skipWorkLeave =0D;			//旷工（天）
		 double noEntry =0D;				//未入职天数
		 double otherHolidays =0D;			//其它假期(天)
		 double otherHolidays1 =0D;			//其它假期(天) --假期类型备用字段
		 double otherHolidays2 =0D;			//其它假期(天) --假期类型备用字段
		 double otherHolidays3 =0D;			//其它假期(天) --假期类型备用字段
		 double otherHolidays4 =0D;			//其它假期(天) --假期类型备用字段
		 double otherHolidays5 =0D;			//其它假期(天) --假期类型备用字段
		 String remark = "";
		 
		for(AttendanceDetail d:attendanceDetails){
			actualDay += d.getActualDay();
			late += d.getLate();
			leaveEarly += d.getLeaveEarly();
			skipWork += d.getSkipWork();
			compassionateLeave += d.getCompassionateLeave();
			sickLeave += d.getSickLeave();
			restDays += d.getRestDays();
			annualLeave += d.getAnnualLeave();
			marriageLeave += d.getMarriageLeave();
			bereavementLeave += d.getBereavementLeave();
			IPPFLeave += d.getIPPFLeave();
			skipWorkLeave += d.getSkipWorkLeave() ;
			noEntry += d.getNoEntry();
			otherHolidays += d.getOtherHolidays();
			otherHolidays1 += d.getOtherHolidays1();
			otherHolidays2 += d.getOtherHolidays2();
			otherHolidays3 += d.getOtherHolidays3();
			otherHolidays4 += d.getOtherHolidays4();
			otherHolidays5 += d.getOtherHolidays5();
			remark += d.getRemark();
		}
		//应出勤天数
		a.setAttendanceDays(getAttendanceDays(a.getMonthFirstDay(), a.getMonthLastDay(),a.getRuleType()));
		//未录入考勤的天数 默认为未入职
		//未入职 = 应出勤 - （实际出勤+休息+所有假期）
		noEntry = a.getAttendanceDays() -(actualDay+restDays+compassionateLeave+sickLeave+annualLeave+
				marriageLeave+bereavementLeave+IPPFLeave+skipWorkLeave+otherHolidays+otherHolidays1+
				otherHolidays2+otherHolidays3+otherHolidays4+otherHolidays5);
		DecimalFormat decf = new DecimalFormat("0.00");
		
		a.setActualDay(Double.valueOf(decf.format(actualDay)));
		a.setLate(late);
		a.setLeaveEarly(leaveEarly);
		a.setSkipWork(skipWork);
		a.setCompassionateLeave(Double.valueOf(decf.format(compassionateLeave)));
		a.setSickLeave(Double.valueOf(decf.format(sickLeave)));
		a.setRestDays(Double.valueOf(decf.format(restDays)));
		a.setAnnualLeave(Double.valueOf(decf.format(annualLeave)));
		a.setMarriageLeave(Double.valueOf(decf.format(marriageLeave)));
		a.setBereavementLeave(Double.valueOf(decf.format(bereavementLeave)));
		a.setIPPFLeave(Double.valueOf(decf.format(IPPFLeave)));
		a.setSkipWorkLeave(Double.valueOf(decf.format(skipWorkLeave)));
		a.setNoEntry(Double.valueOf(decf.format(noEntry)));
		a.setOtherHolidays(Double.valueOf(decf.format(otherHolidays)));
		a.setOtherHolidays1(Double.valueOf(decf.format(otherHolidays1)));
		a.setOtherHolidays2(Double.valueOf(decf.format(otherHolidays2)));
		a.setOtherHolidays3(Double.valueOf(decf.format(otherHolidays3)));
		a.setOtherHolidays4(Double.valueOf(decf.format(otherHolidays4)));
		a.setOtherHolidays5(Double.valueOf(decf.format(otherHolidays5)));
		a.setRemark(remark);
		return a;
	}
	
	/**
	 * 设置考勤制 （五天制、六天制、七天制)
	 * @param a
	 */
	private int  getAttendanceType(String orgId){
		int attendanceType = 5 ;
		Org org =  orgDao.getEntityById(orgId);
		if(org!=null){
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("orgLongNum", org.getLongNumber());
			params.put("orderByClause", " org.FLONGNUMBER DESC ");
			List<AttendanceRule> attendanceRules = this.queryExecutor.execQuery("com.wuyizhiye.hr.attendance.dao.AttendanceRuleDao.getAttendanceRuleByCond",params, AttendanceRule.class);
		    if(attendanceRules !=null && attendanceRules.size()>0){
		    	attendanceType = Integer.valueOf(attendanceRules.get(0).getRuleType().getValue());
		    }
		}
		return attendanceType;
	}
	
	/**
	 * 应出勤天数 
	 * @param firstDate 考勤月第一天 
	 * @param lastDate 考勤月最后一天
	 * @param attendanceType 考勤规则 （五天制、六天制、七天制)  默认 五天制
	 * @return
	 */
	private int getAttendanceDays(Date firstDate,Date lastDate,int attendanceType){
		int attendanceDays = 0;//应出勤天数
		Calendar firstCal = Calendar.getInstance();
		 firstCal.setTime(firstDate);
		 Calendar lastCal = Calendar.getInstance();
		 lastCal.setTime(lastDate);
		 if(attendanceType==0){
			 attendanceType = 5;
		 }
		 
		 while(firstCal.compareTo(lastCal)<=0){
			 int week = firstCal.get(Calendar.DAY_OF_WEEK);
			 if(AttendanceRuleTypeEnum.FIVE.getValue().equals(attendanceType+"")){
				 if(week>=2 && week<=6){
					 attendanceDays++;
				 }
			 }else if(AttendanceRuleTypeEnum.SIX.getValue().equals(attendanceType+"")){
				 if(week>=2 && week<=7){
					 attendanceDays ++;
				 }
			 }else if(AttendanceRuleTypeEnum.SEVEN.getValue().equals(attendanceType+"")){
				 
					 attendanceDays ++;
			 }
			 firstCal.add(Calendar.DAY_OF_MONTH, 1);//加一天
		 }
		return attendanceDays;
	}
	
	/**
	 * 查询月考勤明细 及汇总 
	 * @param personId
	 * @param period
	 */
	public List<AttendanceDetail> getAttendanceDetails(Attendance a){
		//根据员工ID和考勤期间 查询月考勤
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("personId", a.getPersonId());
		params.put("period", a.getPeriod());
		params.put("orderByClause", "D.FLASTENTERDATE ");
		List<Attendance> attendances = this.queryExecutor.execQuery("com.wuyizhiye.hr.attendance.dao.AttendanceDao.getMonthAttendanceByCond",params, Attendance.class);
		if(attendances!=null && attendances.size()>0){
			ObjectCopyUtils.copy(attendances.get(0),a);
		}
		
		params.clear();
		params.put("personId", a.getPersonId());
		params.put("period", a.getPeriod());
		params.put("orderByClause", "D.FRECORDDATE ");
		List<AttendanceDetail> attendanceDetails = this.queryExecutor.execQuery("com.wuyizhiye.hr.attendance.dao.AttendanceDetailDao.selectListByCond",params, AttendanceDetail.class);
		if(attendanceDetails  ==null){
			attendanceDetails = new ArrayList<AttendanceDetail>();
		}
		 double actualDay =0D;				//实际出勤天数
		 int late =0;						//迟到次数
		 int leaveEarly =0;				//早退次数
		 int skipWork =0;					//旷工次数
		 double compassionateLeave =0D;		//事假（天）
		 double sickLeave =0D;				//病假（天）
		 double restDays =0D;				//休息（天）
		 double annualLeave =0D;			//年假（天）
		 double marriageLeave =0D;			//婚假（天）
		 double bereavementLeave =0D;		//丧假（天）
		 double IPPFLeave =0D;				//计生假（天）
		 double skipWorkLeave =0D;			//旷工（天）
		 double noEntry =0D;				//未入职天数
		 double otherHolidays =0D;			//其它假期(天)
		 double otherHolidays1 =0D;			//其它假期(天) --假期类型备用字段
		 double otherHolidays2 =0D;			//其它假期(天) --假期类型备用字段
		 double otherHolidays3 =0D;			//其它假期(天) --假期类型备用字段
		 double otherHolidays4 =0D;			//其它假期(天) --假期类型备用字段
		 double otherHolidays5 =0D;			//其它假期(天) --假期类型备用字段
		 String remark = "";
		 
		 Date periodDate = DateUtil.convertStrToDate(a.getPeriod(), "yyyy-MM");
		 Date firstDay = DateUtil.getFirstDay(periodDate);
		 Date lastDay = DateUtil.getLastDay(periodDate);
		 Calendar firstCal = Calendar.getInstance();
		 firstCal.setTime(firstDay);
		 otherHolidays5 = firstCal.get(Calendar.DAY_OF_WEEK);//考勤月第一天星期数
		 Calendar lastCal = Calendar.getInstance();
		 lastCal.setTime(lastDay);
		 List<AttendanceDetail> monthDetails = new ArrayList<AttendanceDetail>();//整月 明细
		 
		 while(firstCal.compareTo(lastCal)<=0){
			 boolean existsFlag = false;//已录考勤
			 
			 for(AttendanceDetail d:attendanceDetails){
				 if(firstCal.getTime().compareTo(d.getRecordDate())==0){
					actualDay += d.getActualDay();
					late += d.getLate();
					leaveEarly += d.getLeaveEarly();
					skipWork += d.getSkipWork();
					compassionateLeave += d.getCompassionateLeave();
					sickLeave += d.getSickLeave();
					restDays += d.getRestDays();
					annualLeave += d.getAnnualLeave();
					marriageLeave += d.getMarriageLeave();
					bereavementLeave += d.getBereavementLeave();
					IPPFLeave += d.getIPPFLeave();
					skipWorkLeave += d.getSkipWorkLeave() ;
					noEntry += d.getNoEntry();
					otherHolidays += d.getOtherHolidays();
					otherHolidays1 += d.getOtherHolidays1();
					otherHolidays2 += d.getOtherHolidays2();
					otherHolidays3 += d.getOtherHolidays3();
					//otherHolidays4 += d.getOtherHolidays4();
					//otherHolidays5 += d.getOtherHolidays5();
					String detailRemark = "";
					if(d.getCompassionateLeave()>0 || d.getSickLeave()>0 || d.getAnnualLeave()>0 || d.getMarriageLeave()>0 
							|| d.getBereavementLeave()>0 || d.getIPPFLeave()>0 || d.getSkipWorkLeave()>0 || d.getOtherHolidays()>0 ){
						detailRemark += "请假";
					}
					if(d.getRestDays()>0){
						detailRemark += "休息";
					}
					if(d.getNoEntry()>0){
						detailRemark += "未入职";
					}
					if(d.getLeaveEarly()>0){
						detailRemark += "迟到/早退";
					}
					d.setRemark(detailRemark);
					d.setOtherHolidays4(getScheduleCount(a.getPersonId(),firstCal.getTime()));//待办事项
					existsFlag = true;
					monthDetails.add(d);
					break;
				 }
				}
			 if(!existsFlag){
				 AttendanceDetail d = new AttendanceDetail();
				 d.setRecordDate(firstCal.getTime());
				 d.setNoEntry(1);
				 d.setRemark("");
				 d.setOtherHolidays4(getScheduleCount(a.getPersonId(),firstCal.getTime()));//待办事项
				 monthDetails.add(d);
			 }
			 
			 firstCal.add(Calendar.DAY_OF_MONTH, 1);//加一天
		 }
		 
		
		//应出勤天数
		// a.setAttendanceDays(getAttendanceDays(a.getMonthFirstDay(), a.getMonthLastDay(),a.getRuleType()));
		//未录入考勤的天数 默认为未入职
		//未入职 = 应出勤 - （实际出勤+休息+所有假期）
		/*noEntry = a.getAttendanceDays() -(actualDay+restDays+compassionateLeave+sickLeave+annualLeave+
				marriageLeave+bereavementLeave+IPPFLeave+skipWorkLeave+otherHolidays+otherHolidays1+
				otherHolidays2+otherHolidays3+otherHolidays4);*/
		DecimalFormat decf = new DecimalFormat("0.00");
		
		a.setActualDay(Double.valueOf(decf.format(actualDay)));
		a.setLate(late);
		a.setLeaveEarly(leaveEarly);
		a.setSkipWork(skipWork);
		a.setCompassionateLeave(Double.valueOf(decf.format(compassionateLeave)));
		a.setSickLeave(Double.valueOf(decf.format(sickLeave)));
		a.setRestDays(Double.valueOf(decf.format(restDays)));
		a.setAnnualLeave(Double.valueOf(decf.format(annualLeave)));
		a.setMarriageLeave(Double.valueOf(decf.format(marriageLeave)));
		a.setBereavementLeave(Double.valueOf(decf.format(bereavementLeave)));
		a.setIPPFLeave(Double.valueOf(decf.format(IPPFLeave)));
		a.setSkipWorkLeave(Double.valueOf(decf.format(skipWorkLeave)));
		a.setNoEntry(Double.valueOf(decf.format(noEntry)));
		a.setOtherHolidays(Double.valueOf(decf.format(otherHolidays)));
		a.setOtherHolidays1(Double.valueOf(decf.format(otherHolidays1)));
		a.setOtherHolidays2(Double.valueOf(decf.format(otherHolidays2)));
		a.setOtherHolidays3(Double.valueOf(decf.format(otherHolidays3)));
		a.setOtherHolidays4(Double.valueOf(decf.format(otherHolidays4)));
		a.setOtherHolidays5(Double.valueOf(decf.format(otherHolidays5)));
		remark += "";
		if(a.getRestDays()>0){
			remark += "休息"+a.getRestDays()+"天  ";
		}
		if(a.getNoEntry()>0){
			remark += "未入职" +a.getNoEntry()+"天  ";
		}
		if(a.getLeaveEarly()>0){
			remark += "迟到/早退"+a.getLeaveEarly()+"次  ";
		}
		if(a.getCompassionateLeave()>0){
			remark += "事假"+a.getCompassionateLeave()+"天  ";
		}
		if(a.getSickLeave()>0){
			remark += "病假"+a.getSickLeave()+"天  ";
		}
		if(a.getAnnualLeave()>0){
			remark += "年假"+a.getAnnualLeave()+"天  ";
		}
		if(a.getMarriageLeave()>0){
			remark += "婚假"+a.getMarriageLeave()+"天  ";
		}
		if(a.getBereavementLeave()>0){
			remark += "丧假"+a.getBereavementLeave()+"天  ";
		}
		if(a.getIPPFLeave()>0){
			remark += "计生假"+a.getIPPFLeave()+"天  ";
		}
		if(a.getSkipWorkLeave()>0){
			remark += "旷工"+a.getSkipWorkLeave()+"天  ";
		}
		if(a.getOtherHolidays()>0){
			remark += "其它假"+a.getOtherHolidays()+"天  ";
		}
		if(StringUtils.isNotEmpty(remark)){
			remark = a.getPersonName()+" 该月"+remark;
		}
		a.setRemark(remark);
		return monthDetails;
	}
	
	/**
	 * 查询待办事项数量  
	 * @param personId
	 * @param recordDate
	 * @return
	 */
	private int  getScheduleCount(String personId,Date recordDate){
		//待办事项  
		 Map<String,Object> params = new HashMap<String,Object>();
		 params.put("personId",  personId);
		 params.put("todaySchedule",  "1");
		 params.put("schedate", DateUtil.convertDateToStr(recordDate,"yyyy-MM-dd"));
		 int scheduleCount = queryExecutor.execCount("com.wuyizhiye.interflow.schedule.dao.ScheduleDao.select", params);
		 return scheduleCount ;
	}
	
	/**
	 * 修改考勤信息 状态（提交、驳回、审核）
	 * @param attendanceIds
	 * @param approveType
	 * @return
	 */
	@Transactional
	public String updateAttendanceState(List<Attendance> attendances,String approveType,String approvalRemark){
		String resultMsg = "SUCC";
		if(attendances==null || attendances.size()<1){
			return resultMsg ;
		}
		Date now = new Date();
		for(Attendance a:attendances){
			if(StringUtils.isEmpty(a.getPeriod()) || StringUtils.isEmpty(a.getPersonId())){
				//人员id和 期间必须做修改考勤信息状态的 条件
				continue;
			}
			a.setUpdator(SystemUtil.getCurrentUser());
			a.setLastUpdateTime(now);
			String approvalState = "";
			if("SUBMIT".equals(approveType)){
				approvalState = AttendanceStatusEnum.SUBMIT.toString();
			}else if("REJECT".equals(approveType)){
				a.setApprovalPersonId(SystemUtil.getCurrentUser().getId());
				a.setApprovalPersonName(SystemUtil.getCurrentUser().getName());
				approvalState = AttendanceStatusEnum.REJECT.toString();
				a.setApprovalRemark(approvalRemark);//驳回理由
			}else if("APPROVED".equals(approveType)){
				a.setApprovalPersonId(SystemUtil.getCurrentUser().getId());
				a.setApprovalPersonName(SystemUtil.getCurrentUser().getName());
				approvalState = AttendanceStatusEnum.APPROVED.toString();
			}else if("BACKAPPROVED".equals(approveType)){
				//反审核 改状态为待提交
				a.setApprovalPersonId(SystemUtil.getCurrentUser().getId());
				a.setApprovalPersonName(SystemUtil.getCurrentUser().getName());
				approvalState = AttendanceStatusEnum.SAVE.toString();
			}
			a.setApprovalState(approvalState); 
			a.setApprovalDate(now);
			//修改 月考勤状态
			this.queryExecutor.executeUpdate("com.wuyizhiye.hr.attendance.dao.AttendanceDao.updateStateByCon",a);
			AttendanceDetail detail = new AttendanceDetail();
			detail.setPeriod(a.getPeriod());
			detail.setPersonId(a.getPersonId());
			detail.setUpdator(SystemUtil.getCurrentUser());
			detail.setLastUpdateTime(now);
			detail.setApprovalState(approvalState);
			//修改 考勤明细状态
			this.queryExecutor.executeUpdate("com.wuyizhiye.hr.attendance.dao.AttendanceDetailDao.updateStateByCon",detail);
		}
		
		return resultMsg;
	}
	
	/**
	 * 月考勤导出Excel
	 * @param cond
	 * @param os
	 * @throws Exception
	 */
	public void exportMonthByCond(Map<String, Object> cond,OutputStream os) throws Exception{
		String[] excelHeader = new String[]{"部门","名称","员工编号","应出勤","未出勤","实出勤","休息",
				"事假","病假","年假","婚假","丧假","计生假","旷工","其它假期","迟到次数","早退次数","旷工次数","状态","驳回理由"}; 
		String[] excelField = new String[]{"orgName","personName","personNumber","attendanceDays","noEntry","actualDay","restDays",
				"compassionateLeave","sickLeave","annualLeave","marriageLeave","bereavementLeave","IPPFLeave","skipWorkLeave",
				"otherHolidays","late","leaveEarly","skipWork","approvalStateName","approvalRemark"};
		String period = (String) cond.get("period");
		Map<String, Object> resultMap = queryMonthByCond(cond);
		List<Attendance> attendanceList = new ArrayList<Attendance>();
		if("SUCC".equals(resultMap.get("MSG"))){
			attendanceList = (List<Attendance>)resultMap.get("attendanceList");
		}else{
			os.write("没有可导出的考勤信息".getBytes());
		}
		CommonExcelExportUtils.exportExcelByDataList(attendanceList, excelHeader, excelField, os, period+"月考勤报表", true);
		/*SXSSFWorkbook  workBook = new SXSSFWorkbook(); 
		Sheet sheet = workBook.createSheet(period+"月考勤报表");
		int rowIndex = 0;
		Row row = sheet.createRow(rowIndex);
		boolean isUseIndex = true;//是否要序号
		Cell cell;
		String fieldName;
		createHead(row,excelHeader,isUseIndex);
		Object value;
		os.flush();
		for(Attendance attendance :attendanceList){
			rowIndex++;
			row = sheet.createRow(rowIndex);
			int count = 0;
			if(isUseIndex){
				cell = row.createCell(0);
				cell.setCellValue(rowIndex);
				count++;
			}
			for(int i = 0+count; i < excelField.length+count; i++){
				fieldName = excelField[i-count];
				cell = row.createCell(i);
				String methodName = "get"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);
				value = (String)attendance.getClass().getMethod(methodName).invoke(attendance);
				 
				setCellValue(cell, value);
			}
			
		} 
		workBook.write(os);
		os.flush();*/
	}
	
	private void setCellValue(Cell cell,Object value){
		if(value == null){
			return;
		}
		cell.setCellType(Cell.CELL_TYPE_STRING);
		cell.setCellValue(value.toString());
	}
	
	/**
	 * 将指定行创建为表头
	 * @param row
	 */
	private void createHead(Row row,String[] excelHeader,boolean isUseIndex){
		Cell cell;
		String headerName;
		int count = 0;
		if(isUseIndex){
			cell = row.createCell(0);
			cell.setCellValue("序号");
			count++;
		}
		for(int i = 0+count; i < excelHeader.length+count; i++){
			headerName = excelHeader[i-count];
			cell = row.createCell(i);
			 
			cell.setCellValue(headerName);
			cell.getCellStyle().setAlignment(CellStyle.ALIGN_CENTER_SELECTION);
		}
	}
	
	public static void main(String[] args) throws Exception {
		Attendance a = new Attendance();
		//System.out.println(a.getLate());
		//System.out.println(a.getActualDay());
		//Date now = new Date();
		//System.out.println(now.compareTo(null));
		//SimpleDateFormat df = new SimpleDateFormat("yyyy-MM--dd");
		//System.out.println(DateUtil.convertStrToDate("2013-05", "yyyy-MM"));
		//System.out.println(df.format(now));
		/*double d = 3.5;
		double c = 4.3533;
		DecimalFormat decf = new DecimalFormat("0.00");
		System.out.println(decf.format(c));
		System.out.println(decf.format(d));*/
		/*a.getClass().getMethod("setOrgName",String.class).invoke(a,"test22");
		String r = (String)a.getClass().getMethod("getOrgName").invoke(a);*/
		String str = "aa";
		String str2 = "汉字";
//		System.out.println(str.length());
//		System.out.println(str2.length());
	}
	
}
