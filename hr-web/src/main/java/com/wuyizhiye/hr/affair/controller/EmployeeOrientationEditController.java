/**
 * com.wuyizhiye.basedata.person.controller.EmployeeOrientationEditController.java
 */
package com.wuyizhiye.hr.affair.controller;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.base.util.SystemConfig;
import com.wuyizhiye.basedata.basic.model.BasicData;
import com.wuyizhiye.basedata.basic.service.BasicDataService;
import com.wuyizhiye.basedata.images.model.Photo;
import com.wuyizhiye.basedata.images.model.PhotoAlbum;
import com.wuyizhiye.basedata.images.service.PhotoAlbumService;
import com.wuyizhiye.basedata.images.service.PhotoService;
import com.wuyizhiye.basedata.person.enums.CardTypeEnum;
import com.wuyizhiye.basedata.person.enums.JobStatusEnum;
import com.wuyizhiye.basedata.person.enums.UserStatusEnum;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.model.PersonPosition;
import com.wuyizhiye.basedata.person.model.PersonPositionHistory;
import com.wuyizhiye.basedata.person.service.PersonPositionHistoryService;
import com.wuyizhiye.basedata.person.service.PersonPositionService;
import com.wuyizhiye.basedata.person.service.PersonService;
import com.wuyizhiye.basedata.util.GenerateKey;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.EditController;
import com.wuyizhiye.hr.affair.model.Education;
import com.wuyizhiye.hr.affair.model.EmployeeOrientation;
import com.wuyizhiye.hr.affair.model.EmployeeRunDiskBill;
import com.wuyizhiye.hr.affair.model.WorkExperience;
import com.wuyizhiye.hr.affair.service.EmployeeOrientationService;
import com.wuyizhiye.hr.affair.service.PositionHistoryBillService;
import com.wuyizhiye.hr.enums.BillStatusEnum;
import com.wuyizhiye.hr.enums.PositionChangeTypeEnum;
import com.wuyizhiye.hr.model.AgentCertificate;
import com.wuyizhiye.hr.model.RewardPunishment;
import com.wuyizhiye.hr.service.EducationService;
import com.wuyizhiye.hr.service.WorkExperienceService;
import com.wuyizhiye.workflow.enums.ProcessTypeEnum;
import com.wuyizhiye.workflow.service.WorkFlowService;

/**
 *入职申请编辑页面
 * @author Cai.xing
 * @since 2013-04-03
 */
@Controller
@RequestMapping(value="hr/employeeOrientation/*")
public class EmployeeOrientationEditController extends EditController {
	@Autowired
	private EmployeeOrientationService employeeOrientationService;
	@Autowired
	private PhotoAlbumService photoAlbumService;
	@Autowired 
	private BasicDataService basicDataService;
	@Autowired
	private PhotoService photoService;
	@Autowired
	private PersonPositionService personPositionService;
	@Autowired
	private PersonService personService;
	@Autowired
	private PositionHistoryBillService positionHistoryBillService;
	@Resource
	private PersonPositionHistoryService personPositionHistoryService ;
	@Autowired
	EducationService educationService;
	@Autowired
	WorkExperienceService workExperienceService;
	private final static String BILLNUMBER = "YGBH";
	private final static String CREATE_BY_NUM = "CREATE_BY_NUM";//是否根据员工编码生成登录账号
	private final static String CREATE_BY_NUM_NEW = "CREATE_BY_NUM_NEW";//是否生成新的员工编码
	
	@Override
	protected Class<EmployeeOrientation> getSubmitClass() {
		return EmployeeOrientation.class;
	}

	@Override
	protected BaseService<EmployeeOrientation> getService() {
		return employeeOrientationService;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void save(HttpServletResponse response)
			throws InstantiationException, IllegalAccessException {
		try {
			EmployeeOrientation data = (EmployeeOrientation) getSubmitEntity();
			if(!StringUtils.isEmpty(data.getNumber())){
				data.setNumber(data.getNumber().replaceAll(" ", ""));
			}
			//设置员工组织 为主要职位所属组织
			data.setOrg(data.getMainPositionOrg());
			String workExperience = getString("workExperienceJson");		//工作经历
			String education = getString("educationJson");						//教育经历
			List<PersonPosition> pps = new ArrayList<PersonPosition>();
			List<RewardPunishment> rewardPunishmentList = new ArrayList<RewardPunishment>();
			ArrayList<AgentCertificate> agentCertificateList = new ArrayList<AgentCertificate>();
			List<WorkExperience> workExperienceList = new ArrayList<WorkExperience>();
			List<Education> educationList = new ArrayList<Education>();
			JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] {"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"})); 
			//工作经历
			if(!StringUtils.isEmpty(workExperience)){
				workExperienceList = new ArrayList<WorkExperience>(JSONArray.toCollection(JSONArray.fromObject(workExperience), WorkExperience.class));
			}
			//教育经历
			if(!StringUtils.isEmpty(education)){
				educationList = new ArrayList<Education>(JSONArray.toCollection(JSONArray.fromObject(education), Education.class));
			}
			if(validate(data)){
				//入职 岗位状态为 在职
				BasicData basicData = basicDataService.getEntityByNumber(JobStatusEnum.ONDUTY.getValue());
				data.setJobStatus(basicData);
				
				if(data instanceof CoreEntity){
					
					if(StringUtils.isEmpty(((CoreEntity)data).getId())){
						String flag = ParamUtils.getParamValue(SystemUtil.getCurrentOrg().getId(), CREATE_BY_NUM_NEW);
						if("Y".equals(flag)){
							data.setNumber(GenerateKey.getKeyCode(null, BILLNUMBER));
						}
						data.setUserName(data.getNumber());
						data.setStatus(UserStatusEnum.ENABLE);
						
						//如果不是带出来的信息
						if(getString("isPersonInfo")==null){
						  String fnumber = GenerateKey.getKeyCode(null, BILLNUMBER);
						  data.setNumber(fnumber);
						}else{
							//是否为跑盘员  add by li.biao since 2014-2-24
							boolean isRunDiskPerson = false ;
							Person dataPerson = personService.getEntityById(data.getApplyPerson().getId());
							if(dataPerson.getJobStatus()!=null && JobStatusEnum.RUNDISK.getValue().equals(dataPerson.getJobStatus().getNumber())){
								isRunDiskPerson = true ;
							}
							if(isRunDiskPerson){//如果是跑盘员 ，生成正式工号
								String ppyCode = ParamUtils.getParamValue("YGBH_PPY");
								if(!StringUtils.isEmpty(ppyCode)){
									String fnumber = GenerateKey.getKeyCode(null, BILLNUMBER);
									data.setNumber(fnumber);
								} 
							}
						}
						  String billNumber = GenerateKey.getKeyCode(null, ProcessTypeEnum.ENROLL.toString()); //入职单据单据编号
						  data.setBillNumber(billNumber);
						
						data.setTitle("新同事"+data.getName()+"申请入职,入职岗位"+data.getMainPositionOrg().getName()+data.getMainPosition().getName()+"("+data.getMainJobLevel().getName()+")");
						employeeOrientationService.addEmployeeOrientation(data, pps, rewardPunishmentList, agentCertificateList, workExperienceList, educationList);
					}else{
						//如果是带出来的信息
						if(getString("isPersonInfo")!=null){
							data.setStatus(UserStatusEnum.ENABLE);
							//先判断是否在表中存在
							Map<String,Object> params = new HashMap<String,Object>();
							params.put("billSta", BillStatusEnum.SUBMIT.toString());
							params.put("applyPersonId", data.getApplyPerson().getId());
							List<EmployeeOrientation> empList = queryExecutor.execQuery("com.wuyizhiye.hr.affair.dao.EmployeeOrientationDao.select",params,EmployeeOrientation.class);
							if(empList!=null && empList.size()>0){
								getOutputMsg().put("STATE", "FAIL");
								getOutputMsg().put("MSG", "保存失败,失败原因:该人员在入职申请中已经存在！");
								outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
								return;
							}else{
								data.setTitle("新同事"+data.getName()+"申请入职,入职岗位"+data.getMainPositionOrg().getName()+data.getMainPosition().getName()+"("+data.getMainJobLevel().getName()+")");
								employeeOrientationService.updateEmployeeOrientation(data, pps, rewardPunishmentList, agentCertificateList, workExperienceList, educationList);
							}
							
						}else{
							data.setTitle("新同事"+data.getName()+"申请入职,入职岗位"+data.getMainPositionOrg().getName()+data.getMainPosition().getName()+"("+data.getMainJobLevel().getName()+")");
							employeeOrientationService.updateEmployeeOrientation(data, pps, rewardPunishmentList, agentCertificateList, workExperienceList, educationList);
						}
					}
					getOutputMsg().put("STATE", "SUCCESS");
					getOutputMsg().put("MSG", "保存成功");
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "保存失败,失败原因:"+e.getMessage());
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	/**
	 * 修改入职单
	 * @param response
	 * @throws Exception
	 */
	public void updateEmployeeOrientation()
		throws Exception {
		 
		EmployeeOrientation data = (EmployeeOrientation) getSubmitEntity();
		//设置员工组织 为主要职位所属组织
		data.setOrg(data.getMainPositionOrg());
		String workExperience = getString("workExperienceJson");		//工作经历
		String education = getString("educationJson");						//教育经历
		List<PersonPosition> pps = new ArrayList<PersonPosition>();
		List<RewardPunishment> rewardPunishmentList = new ArrayList<RewardPunishment>();
		ArrayList<AgentCertificate> agentCertificateList = new ArrayList<AgentCertificate>();
		List<WorkExperience> workExperienceList = new ArrayList<WorkExperience>();
		List<Education> educationList = new ArrayList<Education>();
		JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] {"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"})); 
		//工作经历
		if(!StringUtils.isEmpty(workExperience)){
			workExperienceList = new ArrayList<WorkExperience>(JSONArray.toCollection(JSONArray.fromObject(workExperience), WorkExperience.class));
		}
		//教育经历
		if(!StringUtils.isEmpty(education)){
			educationList = new ArrayList<Education>(JSONArray.toCollection(JSONArray.fromObject(education), Education.class));
		}
		 
		employeeOrientationService.updateEmployeeOrientation(data, pps, rewardPunishmentList, agentCertificateList, workExperienceList, educationList);
		 
	}
	
	protected boolean validate(EmployeeOrientation data) {
		if(CardTypeEnum.IDCARD.equals(data.getCardType()) && StringUtils.isNotNull(data.getIdCard())){
			//身份证校验
			String idCard = data.getIdCard().trim().toUpperCase();
			data.setIdCard(idCard);
			if(BillStatusEnum.SUBMIT.equals(data.getBillStatu())){
				//提交操作时
				Map<String,Object> param = new HashMap<String, Object>();
				param.put("idcard", idCard);
				//判断身份证表是否存在 在岗、跑盘、见习的人员
				List<Person> p1 = this.queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonDao.getPersonInfoByCondition", param, Person.class);
				 if(p1!=null && p1.size()>0){
					BasicData jobStatus = p1.get(0).getJobStatus();
					 if(jobStatus!=null && (JobStatusEnum.ONDUTY.getValue().equals(jobStatus.getNumber()))){
						 getOutputMsg().put("STATE", "FAIL");
						 getOutputMsg().put("MSG", "提交失败,失败原因:系统已经存在身份证为["+idCard+"]的人员！");
						 return false; 
					 } 
				 }
				//判断是否存在 待审核 的入职单 
				param.clear();
				param.put("billSta", BillStatusEnum.SUBMIT.toString());
				param.put("idCard",idCard);
				List<EmployeeOrientation> empList = queryExecutor.execQuery("com.wuyizhiye.hr.affair.dao.EmployeeOrientationDao.select",param,EmployeeOrientation.class);
				if(empList!=null && empList.size()>0){
					getOutputMsg().put("STATE", "FAIL");
					getOutputMsg().put("MSG", "保存失败,失败原因:已经存在身份证为["+idCard+"]待审核的入职单！");
					return false;
				}
				//判断是否存在 待审核 的 跑盘申请单
				param.clear();
				param.put("billStatus", BillStatusEnum.SUBMIT.toString());
				param.put("idCard", idCard);
				List<EmployeeRunDiskBill> empRunDiskList = queryExecutor.execQuery("com.wuyizhiye.hr.affair.dao.EmployeeRunDiskDao.getEmployeeRunDiskBills",param,EmployeeRunDiskBill.class);
				if(empRunDiskList!=null && empRunDiskList.size()>0){
					getOutputMsg().put("STATE", "FAIL");
					getOutputMsg().put("MSG", "已存在身份证为["+idCard+"]待审核状态的跑盘员申请单");
					return false;
				}
			}
		}
		if(BillStatusEnum.SUBMIT.equals(data.getBillStatu()) &&
				(data.getApplyPerson() !=null && StringUtils.isNotNull(data.getApplyPerson().getId()))){
			PersonPositionHistory ph = personPositionHistoryService.getLastPersonPositionHistory(data.getApplyPerson().getId(), true);
			if(ph!=null && data.getInnerDate()!=null && ph.getEffectdate()!=null && data.getInnerDate().compareTo(ph.getEffectdate())<0){
				//如果生效日期小于 人员最后异动日期 则不充许保存
				getOutputMsg().put("STATE", "FAIL");
				getOutputMsg().put("MSG", "入职日期小于人员最后异动["+PositionChangeTypeEnum.getEnumByValue(ph.getChangeType()).getLabel()+"]日期 ,不能提交");
				return false;
			}
		}
		return true;
	}
	
	@RequestMapping(value="saveInfo")
	public void saveInfo(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		EmployeeOrientation data = (EmployeeOrientation) getSubmitEntity();
		employeeOrientationService.updateEntity(data);
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "保存成功");
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
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
			dir = SystemUtil.getCurrentDataCenter() + dir;
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
	
	public void setUserName(String userName,EmployeeOrientation employeeOrientation,String oldName){
		//是否根据员工编码生成登录账号
		String reateByNumFlag = ParamUtils.getParamValue(SystemUtil.getCurrentOrg().getId(), CREATE_BY_NUM);
		if("Y".equals(reateByNumFlag)){
			//根据员工编码生成登录账号
			employeeOrientation.setUserName(employeeOrientation.getNumber());
			return ;
		}
		Map<String,Object> param = new HashMap<String, Object>();
		int a = 0;
		param.put("userName", userName);
		List<Person> p1 = this.queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonDao.getPersonInfoByCondition", param, Person.class);
		if(p1!=null && p1.size()>0){
			if(!userName.equals(oldName)){
			  a = Integer.valueOf(userName.replace(oldName, ""));
			}
			 a++;
			setUserName(oldName+a,employeeOrientation,oldName);
		}else{
			employeeOrientation.setUserName(userName);
		}
	}
	
	/**
	 * 审批通过调用方法
	 * @author Cai.xing
	 * @throws Exception 
	 * */
	@RequestMapping(value="approvalBill")
	@ResponseBody
	public Map<String,Object> approvalBill() throws Exception{
		//审批通过后反写教育经历\工作经历及人员信息
		//String billId = getString("billId");
		return employeeOrientationService.approvalBill(getString("billId"));  
		//return getOutputMsg();
	}
	
	/**
	 * 改变审批状态调用方法
	 * @author Cai.xing
	 * */
	@RequestMapping(value="changeStatu")
	@ResponseBody
	@Transactional
	public Map<String,Object> changeStatu(){
		//审批通过后反写教育经历\工作经历及人员信息
		String billId = getString("billId");
		String billStatu = getString("billStatu");
		
		EmployeeOrientation employeeOrientation = employeeOrientationService.getEntityById(billId);
		if(employeeOrientation!=null){
			try {
				
				employeeOrientation.setBillStatu(BillStatusEnum.valueOf(billStatu));
				employeeOrientationService.updateEntity(employeeOrientation);
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", BillStatusEnum.valueOf(billStatu).getLabel()+"成功！");
			} catch (Exception e) {
				e.printStackTrace();
				getOutputMsg().put("STATE", "FAIL");
				getOutputMsg().put("MSG", "系统异常！请联系管理员！");
			}
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "单据已经不存在，请刷新后重试！");
		}
		return getOutputMsg();
	}
	/**
	 * 批量改变审批状态调用方法
	 * @author Cai.xing
	 * */
	@RequestMapping(value="updateStatu")
	@ResponseBody
	@Transactional
	public Map<String,Object> updateStatu(){
		String powersJson = getString("hpStr");
    	List<EmployeeOrientation> housePowers = null;
    	if(!StringUtils.isEmpty(powersJson)){
    		housePowers = new ArrayList<EmployeeOrientation>(JSONArray.toCollection(JSONArray.fromObject(powersJson), EmployeeOrientation.class));
		}
    	try {
    		Iterator<EmployeeOrientation> its = housePowers.iterator();
    		while(its.hasNext()){
    			EmployeeOrientation houspower = its.next();
    			EmployeeOrientation employeeOrientation = employeeOrientationService.getEntityById(houspower.getId());
    			employeeOrientation.setBillStatu(houspower.getBillStatu());
    			employeeOrientationService.updateEntity(employeeOrientation);
    		}
    		getOutputMsg().put("STATE", "SUCCESS");
    		getOutputMsg().put("MSG", "操作成功！");
		} catch (Exception e) {
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "系统异常！");
			e.printStackTrace();
		}
		return getOutputMsg();
	}
	@Override
	protected String getImagePath() {
		return "initSystem/systemImg/headDefault";
	}
	//以下为示例代码，为了事务控制，应当写到service中
	@Autowired
	private WorkFlowService workFlowService;
	@Autowired
	private RuntimeService runtimeService;
	/**
	 * 修改单据并提交当前属于自己的节点
	 * @param response
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@RequestMapping(value="updateSubmit")
	public void updateSubmit(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		EmployeeOrientation data = (EmployeeOrientation) getSubmitEntity();
		try{
			if(data.getApplyPerson() !=null && StringUtils.isNotNull(data.getApplyPerson().getId())){
				PersonPositionHistory ph = personPositionHistoryService.getLastPersonPositionHistory(data.getApplyPerson().getId(), true);
				if(ph!=null && data.getInnerDate()!=null && ph.getEffectdate()!=null && data.getInnerDate().compareTo(ph.getEffectdate())<0){
					//如果生效日期小于 人员最后异动日期 则不充许保存
					getOutputMsg().put("STATE", "FAIL");
					getOutputMsg().put("MSG", "入职日期小于人员最后异动["+PositionChangeTypeEnum.getEnumByValue(ph.getChangeType()).getLabel()+"]日期 ,不能提交");
					outPrint(response, JSONObject.fromObject(getOutputMsg()));
					return ;
				}
			}
		  updateEmployeeOrientation();
		}catch(Exception e){
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "保存失败："+e.getMessage());
			outPrint(response, JSONObject.fromObject(getOutputMsg()));
			return ;
		}
		if(data!=null && !StringUtils.isEmpty(data.getProcessInstance())){ 
			List<Task> tasks = workFlowService.getCurrentTask(data.getProcessInstance());
			if(tasks!=null){
				for(Task task : tasks){
					if(SystemUtil.getCurrentUser().getId().equals(task.getAssignee())){
						Map<String, Object> variables = new HashMap<String, Object>();
						variables.put(task.getTaskDefinitionKey() + "_status" , "true");
						variables.put(task.getTaskDefinitionKey() + "_description", "保存修改并提交");
						workFlowService.complete(task, variables );
					}
				}
			}
		}
		
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "保存并提交成功");
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	/**
	 * 撤回单据
	 * @param response
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@RequestMapping(value="cancleBill")
	public void cancleBill(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		EmployeeOrientation data = (EmployeeOrientation) getSubmitEntity();
		if(!StringUtils.isEmpty(data.getProcessInstance())){
			workFlowService.deleteProcessInstance(data.getProcessInstance(), "撤销单据", true);
		}
		//取消提交单据,此时需要修改单据状态并清空processInstance字段
		data.setProcessInstance(null);
		data.setBillStatu(BillStatusEnum.REVOKE);
		employeeOrientationService.updateEntity(data);
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "撤销单据成功");
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
}
