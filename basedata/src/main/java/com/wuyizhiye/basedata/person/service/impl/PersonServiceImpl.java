package com.wuyizhiye.basedata.person.service.impl;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.util.HttpClientUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.org.dao.JobLevelDao;
import com.wuyizhiye.basedata.org.dao.PositionDao;
import com.wuyizhiye.basedata.org.model.JobLevel;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.org.model.WeixinOrg;
import com.wuyizhiye.basedata.person.dao.PersonDao;
import com.wuyizhiye.basedata.person.dao.PersonPositionDao;
import com.wuyizhiye.basedata.person.dao.PersonPositionHistoryDao;
import com.wuyizhiye.basedata.person.enums.SexEnum;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.model.PersonPosition;
import com.wuyizhiye.basedata.person.model.PersonPositionHistory;
import com.wuyizhiye.basedata.person.service.PersonService;
import com.wuyizhiye.basedata.service.impl.DataEntityService;
import com.wuyizhiye.basedata.util.OrgUtils;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.basedata.util.SystemUtil;

/**
 * @ClassName PersonServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="personService")
@Transactional
public class PersonServiceImpl extends DataEntityService<Person> implements
		PersonService {
	static final String ADD_PERSON = "ADD_PERSON";//是否允许新增
	static final String JOBNUMBER = "ZYGW";//置业顾问
	static final String JOBNUMBERXS = "LDXSDB";//LDXSDB 销售代表 
	static final String KXXSDB = "KXXSDB";//KXXSDB 快销销售代表 
	static final String JOBLEVEL = "PPY";//跑盘员
	@Autowired
	private PersonDao personDao;
	@Autowired
	private PersonPositionDao personPositionDao;
	
	@Autowired
	private PositionDao positionDao;
	
	@Autowired
	private JobLevelDao jobLevelDao;
	
	@Resource
	 private PersonPositionHistoryDao personPositionHistoryDao;
	@Resource
	 private PersonService personService;
	
	@Override
	protected BaseDao getDao() {
		return personDao;
	}
	@Override
	public void addPerson(Person person, List<PersonPosition> personPosition) {
		//设置生日 用于生日墙默认值
		if(person.getBirthday()!=null){
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			person.setCalendarType("solar");
			person.setBirthdayEn(df.format(person.getBirthday()));
		}
		if(null!=person.getOrg() && !StringUtils.isEmpty(person.getOrg().getId())){			
			person.setControlUnit(OrgUtils.getCUByOrg(person.getOrg().getId()));
		}
		super.addEntity(person);
		this.addPersonPosition(person, personPosition);
		
		synWeiXinPerson( person, personPosition,"add");
	}
	
	/**
	 * 新增或修改人员信息   同步到微信企业号
	 * @param person
	 * @param personPosition
	 * @param function  新增修改   add/update
	 */
	public void synWeiXinPerson(Person person, List<PersonPosition> personPosition,String function){
		
		/**
		 * 将用户同步到微信企业号 begin
		 */
	    try{
	    	String personpositionId=null;
	    	for(PersonPosition p:personPosition){
	    		if(p.isPrimary()){
	    			personpositionId=p.getId();
	    		}
	    	}
	    	Map<String,Object> param=new HashMap<String, Object>();
	    	param.put("personpositionId", personpositionId);
	    	WeixinOrg worg=this.queryExecutor.execOneEntity("com.wuyizhiye.basedata.org.dao.WeixinOrgDao.selectByPosition", param, WeixinOrg.class); 
	    	int sex=person.getSex().toString().equals(SexEnum.WOMAN.toString())?1:0;
	    	
	    	String strJson = "{";
	        strJson += "\"userid\":\""+person.getNumber()+"\",";
	        strJson += "\"name\":\""+person.getName()+"\",";
	        strJson += "\"mobile\":\""+person.getPhone()+"\",";
	        if(StringUtils.isNotNull(person.getWorkPhone())) {
	        	strJson += "\"tel\":\""+person.getWorkPhone()+"\",";
	        }
	        if(StringUtils.isNotNull(person.getEmail())) {
	        	strJson += "\"email\":\""+person.getEmail()+"\",";
	        }
	        if(worg!=null){
	        	  strJson += "\"department\":\""+worg.getNumber()+"\",";
	        	  if(worg.getPosition()!=null && worg.getPosition().getName()!=null){
	        		  strJson += "\"position\":\""+worg.getPosition().getName()+"\",";
	        	  }
	        }
	      
	        strJson += "\"gender\":"+sex;
	        strJson += "}";
	        
		    String str=HttpClientUtil.callHttpUrl(SystemUtil.getBase()+"/weixinapi/weixinperson?t="+function, URLEncoder.encode(strJson,"utf-8"));
	    }catch(Exception ex){
	    	ex.printStackTrace();
	    }
	    /**
		 * 将用户同步到微信企业号 end
		 */	
		
	}
	/**
	 * 增加职位
	 * added by taking.wang
	 * @param personPosition
	 */
	private void addPersonPosition(Person person, List<PersonPosition> personPosition){
		if(personPosition!=null && personPosition.size() > 0){
			for(PersonPosition pp : personPosition){
				pp.setPerson(person);
				//是否允许新增员工   控制是否写任职历史
				//String addPersonFlag = ParamUtils.getParamValue(SystemUtil.getCurrentOrg().getId(), ADD_PERSON);
				if(pp.isPrimary()){
					//写任职历史记录
					addPositionHistoryByBill(pp);
				}
			}
			personPositionDao.addBatch(personPosition);
		}
	}
	
	/**
	 * 修改职位
	 * added by taking.wang
	 * @param personPosition
	 */
	private void updatePersonPosition(Person person, List<PersonPosition> personPosition){
		//先删除职位，工作经历，教育经历，奖惩记录，经纪人证，那些，然后再插入值
		this.personPositionDao.deleteByPerson(person.getId());
		
		addPersonPosition(person,personPosition);
	}
	
	
	@Override
	public void updatePerson(Person person, List<PersonPosition> personPosition) {
		//设置生日 用于生日墙默认值
		if(person.getBirthday()!=null && StringUtils.isEmpty(person.getCalendarType())){
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			person.setCalendarType("solar");
			person.setBirthdayEn(df.format(person.getBirthday()));
		}
		if(null!=person.getOrg() && !StringUtils.isEmpty(person.getOrg().getId())){			
			person.setControlUnit(OrgUtils.getCUByOrg(person.getOrg().getId()));
		}
		super.updateEntity(person);
		
		this.updatePersonPosition(person, personPosition);
		
		 personService.synWeiXinPerson(person, personPosition, "update"); //微信同步
		
//		List<PersonPosition> old = personPositionDao.getByPerson(person.getId());
//		List<PersonPosition> newPP = new ArrayList<PersonPosition>();
//		List<PersonPosition> update = new ArrayList<PersonPosition>();
//		Iterator<PersonPosition> nIter = personPosition.iterator();
//		while(nIter.hasNext()){
//			PersonPosition pp = nIter.next();
//			if(StringUtils.isEmpty(pp.getId())){
//				pp.setPerson(person);
//				newPP.add(pp);
//				nIter.remove();
//			}
//		}
//		personPositionDao.addBatch(newPP);
//		Iterator<PersonPosition> iterator = old.iterator();
//		while(iterator.hasNext()){
//			PersonPosition pp = iterator.next();
//			for(PersonPosition p : personPosition){
//				if(pp.getId().equals(p.getId())){
//					p.setPerson(person);
//					p.setEffectDate(pp.getEffectDate());
//					update.add(p);
//					iterator.remove();
//				}
//			}
//		}
//		personPositionDao.updateBatch(update);
//		List<String> ids = new ArrayList<String>();
//		for(PersonPosition p : old){
//			ids.add(p.getId());
//		}
//		personPositionDao.deleteBatch(ids);
	}
	
	/**
	 * 入职新增 写任职历史记录
	 * @param (PositionHistoryBill) bill
	 * @return
	 */
	public String addPositionHistoryByBill(PersonPosition pp){
		String resultMsg = "SUCC";
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("personId", pp.getPerson().getId());
			params.put("primary",1);
			List<PersonPositionHistory> allPersonphis = this.queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonPositionHistoryDao.selectByExample",params, PersonPositionHistory.class);
			if(allPersonphis!=null && allPersonphis.size()>1){
				return resultMsg;
			}
			//先删除任职历史 再新增
			PersonPositionHistory pstHisParam = new PersonPositionHistory();
			pstHisParam.setPersonId(pp.getPerson().getId());
			pstHisParam.setPrimary(true);
			personPositionHistoryDao.deletePositionHistorySelective(pstHisParam);
			PersonPositionHistory phis = new PersonPositionHistory();
			Date now = new Date();
			phis.setCreateTime(now);
			phis.setCreatorId(SystemUtil.getCurrentUser().getId());
			phis.setUpdator(SystemUtil.getCurrentUser().getId());
			phis.setOrgId(SystemUtil.getCurrentOrg().getId());
			phis.setLastupdateTime(now);
			phis.setIsdisable("N");
			phis.setPersonId(pp.getPerson().getId());
			phis.setJobStatus(pp.getPerson().getJobStatus());
			phis.setPrimary(pp.isPrimary());
			phis.setChangePositionId(pp.getPosition().getId());
			phis.setChangeJobLevel(pp.getJobLevel().getId());
			Position pst = positionDao.getEntityById(pp.getPosition().getId());
			phis.setChangeOrgId(pst.getBelongOrg().getId());
			phis.setEffectdate(pp.getEffectDate());
			//置业顾问 编码
			String jobNumber = ParamUtils.getParamValue(SystemUtil.getCurrentOrg().getId(), JOBNUMBER);
			if(StringUtils.isEmpty(jobNumber)){
				jobNumber ="8016";//默认8016
			}
			//销售代表 编码
			String jobNumberXs = ParamUtils.getParamValue(SystemUtil.getCurrentOrg().getId(), JOBNUMBERXS);
			if(StringUtils.isEmpty(jobNumberXs)){
				jobNumberXs ="8020";//默认8020
			}
			String kxxsdb = ParamUtils.getParamValue(SystemUtil.getCurrentOrg().getId(), KXXSDB);
			
			//跑盘员职级 level
			String jobLevel = ParamUtils.getParamValue(SystemUtil.getCurrentOrg().getId(), JOBLEVEL);
			if(StringUtils.isEmpty(jobLevel)){
				jobLevel ="1";//默认1
			}
			JobLevel jl = jobLevelDao.getEntityById(pp.getJobLevel().getId());
			if(jl!=null && jl.getJob()!=null &&(jobNumber.equals(jl.getJob().getNumber()) || jobNumberXs.equals(jl.getJob().getNumber())
					|| (kxxsdb!=null && kxxsdb.equals(jl.getJob().getNumber())))
				&& Integer.valueOf(jobLevel).equals(jl.getLevel())){
				//如果职级为 跑盘员 则类型为 新增跑盘
				phis.setChangeType("RUNDISK");
			}else{
				//入职
		    	phis.setChangeType("ENROLL");
			}
			 
			personPositionHistoryDao.addEntity(phis);
		}catch(Exception e){
			 
		}
		return resultMsg ;
	}
	
	/**
	 * 根据人员id得到部门
	 * added by taking.wang
	 * @since 2013-01-19
	 */
	@Override
	public Org getPersonOrg(String personId) {
		// TODO Auto-generated method stub
		return this.personDao.getPersonOrg(personId);
	}
	
	@Override
	public Person selectPersonLead(Map<String, Object> param) {
		return this.personDao.selectPersonLead(param);
	}
}
