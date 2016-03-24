package com.wuyizhiye.basedata.person.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;




import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.base.util.SystemConfig;
import com.wuyizhiye.basedata.images.model.Photo;
import com.wuyizhiye.basedata.images.model.PhotoAlbum;
import com.wuyizhiye.basedata.images.service.PhotoAlbumService;
import com.wuyizhiye.basedata.images.service.PhotoService;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.person.enums.CardTypeEnum;
import com.wuyizhiye.basedata.person.enums.OperateTypeEnum;
import com.wuyizhiye.basedata.person.enums.PersonStatusEnum;
import com.wuyizhiye.basedata.person.enums.PositionChangeTypeEnum;
import com.wuyizhiye.basedata.person.enums.UserStatusEnum;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.model.PersonPosition;
import com.wuyizhiye.basedata.person.model.PersonPositionHistory;
import com.wuyizhiye.basedata.person.model.PersonPositionHistoryLog;
import com.wuyizhiye.basedata.person.service.PersonPositionHistoryLogService;
import com.wuyizhiye.basedata.person.service.PersonPositionHistoryService;
import com.wuyizhiye.basedata.person.service.PersonService;
import com.wuyizhiye.basedata.util.OperationLogUtil;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName PersonEditController
 * @Description 职员编辑controller
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/person/*")
public class PersonEditController extends EditController {
	
	@Resource
	private PersonPositionHistoryService personPositionHistoryService;
	
	@Resource
	private PersonPositionHistoryLogService personPositionHistoryLogService;
	
	@Autowired
	private PersonService personService;
	@Autowired
	private PhotoAlbumService photoAlbumService;
	@Autowired
	private PhotoService photoService;
	@Override
	protected Class<Person> getSubmitClass() {
		return Person.class;
	}

	@Override
	protected BaseService<Person> getService() {
		return personService;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void save(HttpServletResponse response)
			throws InstantiationException, IllegalAccessException {
		Person data = (Person) getSubmitEntity();
		if(!StringUtils.isEmpty(data.getNumber())){
			data.setNumber(data.getNumber().replaceAll(" ", ""));
		}
		String position = getString("positionJson");	//任职信息
		String rewardPunishment = getString("rewardPunishmentJson");	//奖惩记录
		String agentCertificate = getString("agentCertificateJson");	//经纪人证
		String workExperience = getString("workExperienceJson");		//工作经历
		String education = getString("educationJson");						//教育经历
		List<PersonPosition> pps = new ArrayList<PersonPosition>();
		
		JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] {"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"})); 
		if(!StringUtils.isEmpty(position)){
			pps = new ArrayList<PersonPosition>(JSONArray.toCollection(JSONArray.fromObject(position), PersonPosition.class));
		}
		
		
		if(validate(data)){
			if(data instanceof CoreEntity){
				if(StringUtils.isEmpty(((CoreEntity)data).getId())){
					data.setUserName(data.getNumber());
					data.setStatus(UserStatusEnum.ENABLE);
					if(pps!=null && pps.size() > 0){
						for(PersonPosition pp : pps){
							if(pp.isPrimary()){
								//主要职位写成 入职日期
								data.setInnerDate(pp.getEffectDate()); 
							}
						}
					}
					data.setPersonStatus(PersonStatusEnum.REGULAR);//默认正式员工
					personService.addPerson(data, pps);
				}else{
					if(data.getInnerDate()==null && pps!=null && pps.size() > 0){
						for(PersonPosition pp : pps){
							if(pp.isPrimary()){
								//主要职位写成 入职日期
								data.setInnerDate(pp.getEffectDate()); 
							}
						}
					}
					personService.updatePerson(data, pps);
				}
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "保存成功");
			}
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	/**
	 * 调整人员任职历史
	 * @return
	 */
	@RequestMapping(value="savePersonPositionHis")
	protected void savePersonPositionHis(HttpServletResponse response) {
		String positionHistoryJson = getString("positionHistoryJson");	//任职历史信息
		List<PersonPositionHistory> positionHistoryList = new ArrayList<PersonPositionHistory>();
		
		JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] {"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"})); 
		if(!StringUtils.isEmpty(positionHistoryJson)){
			positionHistoryList = new ArrayList<PersonPositionHistory>(JSONArray.toCollection(JSONArray.fromObject(positionHistoryJson), PersonPositionHistory.class));
			Date now = new Date();
			String phIds = "";
			String personId = getString("personId");
			String mapper = "com.wuyizhiye.basedata.person.dao.PersonDao.getPerson4PositionHisEdit";
			Map<String,Object> param = new HashMap<String, Object>();
			param.put("id", personId);
			Person  person = queryExecutor.execOneEntity(mapper, param, Person.class);
			//调整前 任职历史
			List<PersonPositionHistory> positionHistoryDbList = personPositionHistoryService.getAllPersonPositionHistory(getString("personId"));
			List<PersonPositionHistory> positionHistoryDelList = positionHistoryDbList;
			List<PersonPositionHistoryLog> positionHistoryLogList = new ArrayList<PersonPositionHistoryLog>(); 
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			for(PersonPositionHistory ph:positionHistoryList){
				ph.setLastupdateTime(now);
				ph.setUpdator(SystemUtil.getCurrentUser().getId());
				
				OperateTypeEnum operateType = null;
				String description = "";
				String descriptionId = "";
				
				if(StringUtils.isEmpty(ph.getId())){
					ph.setCreatorId(SystemUtil.getCurrentUser().getId());
					ph.setCreateTime(now);
					ph.setUUID();
					personPositionHistoryService.insertPositionHistory(ph);
					//调整日志
					PersonPositionHistoryLog pstHisLog = initPstHisLog(now,person);
					operateType = OperateTypeEnum.ADD;//新增
					description =   PositionChangeTypeEnum.getEnumByValue(ph.getChangeType()).getLabel()+"-";
					description += (ph.getPrimary()?"主要职位":"兼职") +"-"+ph.getJobStatus().getName()+"-";
					description += "-异动前组织："+ph.getOldOrgName();
					description += "-异动前职位："+ph.getOldPositionName();
					description += "-异动前职级："+ph.getOldJobLevelName();
					description += "-异动后组织："+ph.getChangeOrgName();
					description += "-异动后职位："+ph.getChangePositionName();
					description += "-异动后职级："+ph.getChangeJobLevelName();
					description += "-生效日期："+ (ph.getEffectdate()==null?"null":df.format(ph.getEffectdate()));
					description += "-失效日期："+ (ph.getExpirydate()==null?"null":df.format(ph.getExpirydate()));
					pstHisLog.setDescription(description);
					pstHisLog.setOperateType(operateType);
					positionHistoryLogList.add(pstHisLog);
					
				}else{
					personPositionHistoryService.updateByPrimaryKey(ph);
					PersonPositionHistory pstHisDb = getPersonPositionHistoryById(positionHistoryDbList,ph.getId());
					if(pstHisDb!=null){
						operateType = OperateTypeEnum.EDIT;//修改
						if(pstHisDb.getChangeType() !=null && !pstHisDb.getChangeType().equals(ph.getChangeType())){
							description = "异动类型："+ (pstHisDb.getChangeType()==null?"null":(PositionChangeTypeEnum.getEnumByValue(pstHisDb.getChangeType()).getLabel()))+"修改为"+ (ph.getChangeType()==null?"null":(PositionChangeTypeEnum.getEnumByValue(ph.getChangeType()).getLabel()))+";";
						} 
						if(ph.getPrimary()!=pstHisDb.getPrimary()){ 
						  description += (pstHisDb.getPrimary()?"主要职位":"兼职")+"修改为"+(ph.getPrimary()?"主要职位":"兼职") +";";
						}
						if(StringUtils.isEmpty(pstHisDb.getChangeOrgName())){
							pstHisDb.setChangeOrgName(null);
						}
						if(StringUtils.isEmpty(pstHisDb.getChangePositionName())){
							pstHisDb.setChangePositionName(null);
						}
						if(StringUtils.isEmpty(pstHisDb.getChangeJobLevelName())){
							pstHisDb.setChangeJobLevelName(null);
						}
						
						if(StringUtils.isEmpty(pstHisDb.getOldOrgName())){
							pstHisDb.setOldOrgName(null);
						}
						if(StringUtils.isEmpty(pstHisDb.getOldPositionName())){
							pstHisDb.setOldPositionName(null);
						}
						if(StringUtils.isEmpty(pstHisDb.getOldJobLevelName())){
							pstHisDb.setOldJobLevelName(null);
						}
						
						if(StringUtils.isEmpty(ph.getChangeOrgName())){
							ph.setChangeOrgName(null);
						}
						if(StringUtils.isEmpty(ph.getChangePositionName())){
							ph.setChangePositionName(null);
						}
						if(StringUtils.isEmpty(ph.getChangeJobLevelName())){
							ph.setChangeJobLevelName(null);
						}
						
						if(StringUtils.isEmpty(ph.getOldOrgName())){
							ph.setOldOrgName(null);
						}
						if(StringUtils.isEmpty(ph.getOldPositionName())){
							ph.setOldPositionName(null);
						}
						if(StringUtils.isEmpty(ph.getOldJobLevelName())){
							ph.setOldJobLevelName(null);
						}
						if((pstHisDb.getJobStatus()==null && ph.getJobStatus()!=null) || 
						  (pstHisDb.getJobStatus()!=null && !pstHisDb.getJobStatus().getId().equals(ph.getJobStatus()==null?null:ph.getJobStatus().getId())) ){
						   description += "岗位状态:"+(pstHisDb.getJobStatus()==null?null:pstHisDb.getJobStatus().getName())+"修改为"+(ph.getJobStatus()==null?null:ph.getJobStatus().getName())+";";
						}
						if((pstHisDb.getOldOrgName()==null && ph.getOldOrgName()!=null) || 
								  (pstHisDb.getOldOrgName()!=null && !pstHisDb.getOldOrgName().equals(ph.getOldOrgName())) ){
								  description += "异动前组织:"+pstHisDb.getOldOrgName()+"修改为"+ph.getOldOrgName()+";";
						}
						if((pstHisDb.getOldPositionName()==null && ph.getOldPositionName()!=null) || 
								  (pstHisDb.getOldPositionName()!=null && !pstHisDb.getOldPositionName().equals(ph.getOldPositionName())) ){
								  description += "异动前职位:"+pstHisDb.getOldPositionName()+"修改为"+ph.getOldPositionName()+";";
						}
						if((pstHisDb.getOldJobLevelName()==null && ph.getOldJobLevelName()!=null) || 
								  (pstHisDb.getOldJobLevelName()!=null && !pstHisDb.getOldJobLevelName().equals(ph.getOldJobLevelName())) ){
								  description += "异动前职级:"+pstHisDb.getOldJobLevelName()+"修改为"+ph.getOldJobLevelName()+";";
						}
						if((pstHisDb.getChangeOrgName()==null && ph.getChangeOrgName()!=null) || 
								  (pstHisDb.getChangeOrgName()!=null && !pstHisDb.getChangeOrgName().equals(ph.getChangeOrgName())) ){
								  description += "异动后组织:"+pstHisDb.getChangeOrgName()+"修改为"+ph.getChangeOrgName()+";";
						}
						if((pstHisDb.getChangePositionName()==null && ph.getChangePositionName()!=null) || 
								  (pstHisDb.getChangePositionName()!=null && !pstHisDb.getChangePositionName().equals(ph.getChangePositionName())) ){
								  description += "异动后职位:"+pstHisDb.getChangePositionName()+"修改为"+ph.getChangePositionName()+";";
						}
						if((pstHisDb.getChangeJobLevelName()==null && ph.getChangeJobLevelName()!=null) || 
								  (pstHisDb.getChangeJobLevelName()!=null && !pstHisDb.getChangeJobLevelName().equals(ph.getChangeJobLevelName())) ){
								  description += "异动后职级:"+pstHisDb.getChangeJobLevelName()+"修改为"+ph.getChangeJobLevelName()+";";
						}
						if((pstHisDb.getEffectdate()==null && ph.getEffectdate()!=null) || 
								  (pstHisDb.getEffectdate()!=null && !pstHisDb.getEffectdate().equals(ph.getEffectdate())) ){
								  description += "生效日期:"+(pstHisDb.getEffectdate()==null?"null":df.format(pstHisDb.getEffectdate()))+"修改为"+(ph.getEffectdate()==null?"null":df.format(ph.getEffectdate()))+";";
						}
						if((pstHisDb.getExpirydate()==null && ph.getExpirydate()!=null) || 
								  (pstHisDb.getExpirydate()!=null && !pstHisDb.getExpirydate().equals(ph.getExpirydate())) ){
								  description += "失效日期:"+(pstHisDb.getExpirydate()==null?"null":df.format(pstHisDb.getExpirydate()))+"修改为"+(ph.getExpirydate()==null?"null":df.format(ph.getExpirydate()))+";";
						}
						if(StringUtils.isNotNull(description)){
							description = "异动类型："+PositionChangeTypeEnum.getEnumByValue(pstHisDb.getChangeType()).getLabel()+";" +description;
							//调整日志
							PersonPositionHistoryLog pstHisLog = initPstHisLog(now,person);
							pstHisLog.setDescription(description);
							pstHisLog.setOperateType(operateType);
							positionHistoryLogList.add(pstHisLog);
						}
						 
						}
				}
				if(StringUtils.isEmpty(phIds)){
					phIds = "'"+ph.getId()+"'";
				}else{
					phIds += ",'"+ph.getId()+"'";
				}
			    if(positionHistoryDelList!=null){
			    	for(int i=0;i<positionHistoryDelList.size();i++){
			    		PersonPositionHistory phDel = positionHistoryDelList.get(i);
			    		if(phDel.getId().equals(ph.getId())){
			    			positionHistoryDelList.remove(i);
			    			break;
			    		}
			    	}
			    }
				 
			}
			if(StringUtils.isNotNull(personId)){
				Map<String,Object> params = new HashMap<String, Object>();
				params.put("idNotIn", phIds);
				params.put("personId",personId);
				queryExecutor.executeDelete(PersonPositionHistory.Mapper+".deleteByCond", params);
			}
			if(positionHistoryDelList!=null){
				//删除的任职历史 记录操作日志
				OperateTypeEnum operateType = null;
				String description = "";
				for(int i=0;i<positionHistoryDelList.size();i++){
		    		PersonPositionHistory phDel = positionHistoryDelList.get(i);
		    		//调整日志
					PersonPositionHistoryLog pstHisLog = initPstHisLog(now,person);
					operateType = OperateTypeEnum.DELETE;//删除
					description =   PositionChangeTypeEnum.getEnumByValue(phDel.getChangeType()).getLabel()+"-";
					description += (phDel.getPrimary()?"主要职位":"兼职") +"-"+(phDel.getJobStatus()==null?null:phDel.getJobStatus().getName())+"-";
					description += "-异动前组织："+phDel.getOldOrgName();
					description += "-异动前职位："+phDel.getOldPositionName();
					description += "-异动前职级："+phDel.getOldJobLevelName();
					description += "-异动后组织："+phDel.getChangeOrgName();
					description += "-异动后职位："+phDel.getChangePositionName();
					description += "-异动后职级："+phDel.getChangeJobLevelName();
					description += "-生效日期："+ (phDel.getEffectdate()==null?"null":df.format(phDel.getEffectdate()));
					description += "-失效日期："+ (phDel.getExpirydate()==null?"null":df.format(phDel.getExpirydate()));
					pstHisLog.setDescription(description);
					pstHisLog.setOperateType(operateType);
					positionHistoryLogList.add(pstHisLog);
		    	}
				
			}
			if(positionHistoryLogList.size()>0){
				personPositionHistoryLogService.addBatch(positionHistoryLogList);//保存操作日志
			}
			//OperationLogUtil.saveOperationLog("basedata/person/savePersonPositionHis", SystemUtil.getCurrentUser().getName()+"调整人员id为["+personId+"]任职历史", "", "");
		}
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "保存成功");
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	/**
	 * 初始任职历史日志对象 
	 * @param now
	 * @param person
	 * @return
	 */
	private PersonPositionHistoryLog initPstHisLog(Date now,Person person){
		PersonPositionHistoryLog pstHisLog = new PersonPositionHistoryLog();
		pstHisLog.setCreateTime(now);
		//操作人
		pstHisLog.setCreatorId(SystemUtil.getCurrentUser().getId());
		pstHisLog.setCreatorName(SystemUtil.getCurrentUser().getName());
		pstHisLog.setOrgId(SystemUtil.getCurrentOrg().getId());
		pstHisLog.setOrgIdName(SystemUtil.getCurrentOrg().getName());
		//任职历史人员
		pstHisLog.setPersonId(person.getId());
		pstHisLog.setPersonName(person.getName());
		pstHisLog.setPersonNumber(person.getNumber());
		if(person.getOrg() !=null){
			pstHisLog.setPersonOrgId(person.getOrg().getId());
			pstHisLog.setPersonOrgIdName(person.getOrg().getName());
		}
		return pstHisLog;
	}
	
	/**
	 * 根据id 筛选任职历史
	 * @param allPersonPositionHistory
	 * @param id
	 * @return
	 */
	private PersonPositionHistory getPersonPositionHistoryById(List<PersonPositionHistory> allPersonPositionHistory,String id){
		PersonPositionHistory pstHis = null ;
		if(allPersonPositionHistory==null || allPersonPositionHistory.size()<1){
			return pstHis;
		}
		for(PersonPositionHistory ph:allPersonPositionHistory){
			if(ph.getId().equals(id)){
				return ph;
			}
		}
		
		return pstHis;
	}
	
	/**
	 * 设置生日
	 * @param response
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@RequestMapping(value="saveBirth")
	public void saveBirth(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		 String personId = getString("personId");
		 String calendarType = getString("calendarType");
		 String birthdayEn = getString("birthdayEn");
		 String birthdayCn = getString("birthdayCn");
		 Person person = personService.getEntityById(personId);
		 person.setCalendarType(calendarType);
		 person.setBirthdayCn(birthdayCn);
		 person.setBirthdayEn(birthdayEn);
		 personService.updateEntity(person);
			 
		getOutputMsg().put("STATE", "SUCCESS");
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	/**
	 * 根据人员id得到部门
	 * added by taking.wang
	 * @since 2013-01-19
	 * @param response
	 */
	@RequestMapping(value="getDept")
	public void getOrgInfo(HttpServletResponse response){
		String personId = this.getString("personId");
		Org org = this.personService.getPersonOrg(personId);
		this.getOutputMsg().put("org", org);
		this.outPrint(response, JSONObject.fromObject(this.getOutputMsg()));
		
	}
	
	@RequestMapping(value="saveInfo")
	public void saveInfo(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		Person data = (Person) getSubmitEntity();
		personService.updateEntity(data);
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "保存成功");
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected boolean validate(Object data) {
		Person person = (Person) data;
		//验证工号
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("number", person.getNumber());
		List<Person> persons= queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonDao.select", param, Person.class);
		if(persons.size() > 0){
			for(Person p : persons){
				if(!p.getId().equals(person.getId())){
					getOutputMsg().put("MSG", "该工号己存在");
					return false;
				}
			}
		}
		String position = getString("positionJson");
		List<PersonPosition> pps = new ArrayList<PersonPosition>();
		if(!StringUtils.isEmpty(position)){
			pps = new ArrayList<PersonPosition>(JSONArray.toCollection(JSONArray.fromObject(position), PersonPosition.class));
		}
		if(pps.size()==0){
			getOutputMsg().put("MSG", "必须包含任职信息");
			return false;
		}
		if(pps.size()>0){
			int pc = 0;
			for(PersonPosition pp : pps){
				if(pp.isPrimary()){
					pc++;
				}
			}
			if(pc!=1){
				getOutputMsg().put("MSG", "必须且仅能有一个主要任职信息");
				return false;
			}
		}
		
		//TODO 此处为身份证验证部分---编辑的时候,可以不用验证的.但是要看是否修改过
		//1.判断是否是身份证类型,如果不是,目前则不验证,前台只验证为不为空
		//String cardType=person.getCardType().getValue();
		//if(CardTypeEnum.IDCARD.getValue().equals(cardType)){//此处为身份证进行校验
			String idCard=person.getIdCard();
			String personId=person.getId();
			//给person的idcard去除空格
			person.setIdCard(idCard.trim());
			Map<String, Object> checkParam=new HashMap<String, Object>();
			checkParam.put("idcard",idCard.trim().toUpperCase());
			checkParam.put("personId", personId);
			int result = this.queryExecutor.execCount("com.wuyizhiye.basedata.person.dao.PersonDao.judgeIdCard", checkParam);
			if(result>0){
				getOutputMsg().put("MSG", "证件号码已经在系统中存在");
				return false;
			}
		//}
		return super.validate(data);
	}
	
	/**
	 * 上传头像
	 * @param file
	 * @param dir
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping(value="upload",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> upload(@RequestParam(value="image")MultipartFile file,
			@RequestParam(value="fileName",required=false)String ordingName,
			@RequestParam(value="direct",required=true)String dir,
			@RequestParam(value="belong",required=false)String belong,
			HttpServletResponse response) throws IOException{
		if(!file.isEmpty()){
			File path = new File(SystemConfig.getParameter("image_path") + dir);
			if(!path.exists()){
				path.mkdirs();
			}
			
			String fileName = file.getOriginalFilename();
			
			fileName = (StringUtils.isEmpty(ordingName)?UUID.randomUUID().toString():ordingName) + fileName.substring(fileName.lastIndexOf("."));
			File img = new File(path.getPath() + "/" + fileName);
			if(img.exists()){
				getOutputMsg().put("STATE", "FAIL");
				getOutputMsg().put("MSG", "该文件己存在");
			}else{
				img.createNewFile();
				FileOutputStream fos = new FileOutputStream(img);
				InputStream is = file.getInputStream();
				byte[] buff = new byte[4096];//缓存4k
				int len = 0;
				while((len=is.read(buff))>0){
					fos.write(buff, 0, len);
				}
				is.close();
				fos.flush();
				fos.close();
				if(!StringUtils.isEmpty(belong)){
					addPhotoToAlbum(belong, file.getOriginalFilename(), dir + "/" + fileName);
				}
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "上传文件成功");
				getOutputMsg().put("PATH", dir + "/" + fileName);
				
			}
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "上传文件为空");
		}
//		outPrint(response, JSONObject.fromObject(getOutputMsg()));
		return getOutputMsg(); 
	}
	
	private void addPhotoToAlbum(String personId,String photoName,String path){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("belong", personId);
		param.put("name", "头像相册");
		List<PhotoAlbum> albums = queryExecutor.execQuery("com.wuyizhiye.basedata.images.dao.PhotoAlbumDao.select", param , PhotoAlbum.class);
		PhotoAlbum album;
		if(albums.size()>0){
			album = albums.get(0);
		}else{
			album = new PhotoAlbum();
			album.setId(UUID.randomUUID().toString());
			album.setName("头像相册");
			album.setBelong(personId);
			photoAlbumService.addEntity(album);
		}
		Photo photo = new Photo();
		photo.setAlbum(album);
		photo.setName(photoName);
		photo.setPath(path);
		photoService.addEntity(photo);
	}

	@Override
	protected String getImagePath() {
		// TODO Auto-generated method stub
		return "initSystem/systemImg/headDefault";
	}
}
