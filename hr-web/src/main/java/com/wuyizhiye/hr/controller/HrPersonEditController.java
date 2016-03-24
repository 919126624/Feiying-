/**
 * com.wuyizhiye.basedata.person.controller.PersonEditController.java
 */
package com.wuyizhiye.hr.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
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
import com.wuyizhiye.base.common.ImageAttribute;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.ImageUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.base.util.SystemConfig;
import com.wuyizhiye.basedata.basic.service.BasicDataService;
import com.wuyizhiye.basedata.images.model.Photo;
import com.wuyizhiye.basedata.images.model.PhotoAlbum;
import com.wuyizhiye.basedata.images.service.PhotoAlbumService;
import com.wuyizhiye.basedata.images.service.PhotoService;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.org.service.PositionService;
import com.wuyizhiye.basedata.person.enums.JobStatusEnum;
import com.wuyizhiye.basedata.person.enums.UserStatusEnum;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.model.PersonPosition;
import com.wuyizhiye.basedata.util.GenerateKey;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.EditController;
import com.wuyizhiye.hr.model.AgentCertificate;
import com.wuyizhiye.hr.model.Education;
import com.wuyizhiye.hr.model.RewardPunishment;
import com.wuyizhiye.hr.model.WorkExperience;
import com.wuyizhiye.hr.service.HrPersonService;
import com.wuyizhiye.hr.utils.ImageCutUtils;

/**
 * hr职员编辑controller
 * @author takingwang
 *
 * @since 2012-10-9
 */
@Controller
@RequestMapping(value="hr/person/*")
public class HrPersonEditController extends EditController {
	@Autowired
	private HrPersonService hrPersonService;
	@Autowired
	private PhotoAlbumService photoAlbumService;
	@Autowired
	private PhotoService photoService;
	
	@Resource
	private PositionService positionService;
	@Autowired 
	private BasicDataService basicDataService;
	private final static String BILLNUMBER = "YGBH";
	
	@Override
	protected Class<Person> getSubmitClass() {
		return Person.class;
	}

	@Override
	protected BaseService<Person> getService() {
		return hrPersonService;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void save(HttpServletResponse response)
			throws InstantiationException, IllegalAccessException {
		try {
			Person data = (Person) getSubmitEntity();
			
			
			
			String position = getString("positionJson");	//任职信息
			String rewardPunishment = getString("rewardPunishmentJson");	//奖惩记录
			String agentCertificate = getString("agentCertificateJson");	//经纪人证
			String workExperience = getString("workExperienceJson");		//工作经历
			String education = getString("educationJson");						//教育经历
			List<PersonPosition> pps = new ArrayList<PersonPosition>();
			List<RewardPunishment> rewardPunishmentList = new ArrayList<RewardPunishment>();
			ArrayList<AgentCertificate> agentCertificateList = new ArrayList<AgentCertificate>();
			List<WorkExperience> workExperienceList = new ArrayList<WorkExperience>();
			List<Education> educationList = new ArrayList<Education>();
			JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] {"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"})); 
			if(!StringUtils.isEmpty(position)){
				pps = new ArrayList<PersonPosition>(JSONArray.toCollection(JSONArray.fromObject(position), PersonPosition.class));
				//设置员工组织 为主要职位所属组织
				if(pps !=null && pps.size()>0){
					for(PersonPosition pp:pps){
						if(pp.isPrimary()){
							if(pp.getPosition()!=null){
							 Position p = positionService.getEntityById(pp.getPosition().getId());
							 data.setOrg(p.getBelongOrg());
							}
						}
					}
				}
			}
			
			//奖惩记录
			if(!StringUtils.isEmpty(rewardPunishment)){
				rewardPunishmentList = new ArrayList<RewardPunishment>(JSONArray.toCollection(JSONArray.fromObject(rewardPunishment), RewardPunishment.class));
			}
			//经纪人证
			if(!StringUtils.isEmpty(agentCertificate)){
				agentCertificateList = new ArrayList<AgentCertificate>(JSONArray.toCollection(JSONArray.fromObject(agentCertificate), AgentCertificate.class));
			}
			//工作经历
			if(!StringUtils.isEmpty(workExperience)){
				workExperienceList = new ArrayList<WorkExperience>(JSONArray.toCollection(JSONArray.fromObject(workExperience), WorkExperience.class));
			}
			//教育经历
			if(!StringUtils.isEmpty(education)){
				educationList = new ArrayList<Education>(JSONArray.toCollection(JSONArray.fromObject(education), Education.class));
			}
			
			if(validate(data)){
				if(data instanceof CoreEntity){
					if(StringUtils.isEmpty(((CoreEntity)data).getId())){
						data.setUserName(data.getNumber());
						data.setStatus(UserStatusEnum.ENABLE);
						String fnumber = GenerateKey.getKeyCode(null, BILLNUMBER);
						data.setNumber(fnumber); //hetengfeng 只有新增时才需要创建编号
						hrPersonService.addPerson(data, pps, rewardPunishmentList, agentCertificateList, workExperienceList, educationList);
					}else{
						if(StringUtils.isEmpty(data.getNumber())){
							String fnumber = GenerateKey.getKeyCode(null, BILLNUMBER);
							data.setNumber(fnumber); //hetengfeng 只有新增时才需要创建编号
						}
						hrPersonService.updatePerson(data, pps, rewardPunishmentList, agentCertificateList, workExperienceList, educationList);
					}
					getOutputMsg().put("STATE", "SUCCESS");
					getOutputMsg().put("MSG", "保存成功");
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "保存失败,失败原因:"+e.getMessage());
//			throw new RuntimeException(e);
		}
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
		Org org = this.hrPersonService.getPersonOrg(personId);
		this.getOutputMsg().put("org", org);
		this.outPrint(response, JSONObject.fromObject(this.getOutputMsg()));
		
	}
	
	@RequestMapping(value="idcard/judge")
	public void idCardHasExists(HttpServletRequest request,HttpServletResponse response){
		//判断证件类型
		//String cardType=this.getString("cardType");
		//if(CardTypeEnum.IDCARD.getValue().equals(cardType)){//身份证
			String idcard = this.getString("idcard");
			String personId = this.getString("id");
	 		Map<String,Object> param = new HashMap<String, Object>();
			param.put("idcard", idcard.trim().toUpperCase());
			param.put("personId", personId);
			int result = this.queryExecutor.execCount("com.wuyizhiye.basedata.person.dao.PersonDao.judgeIdCard", param);
			this.getOutputMsg().clear();
			if(result > 0){
				 List<Person> p1 = this.queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonDao.getPersonInfoByCondition", param, Person.class);
				 if(p1!=null && p1.size()>0){
					String ondutyId =  basicDataService.getEntityByNumber(JobStatusEnum.ONDUTY.getValue()).getId();
					 if(p1.get(0).getJobStatus().getId().equals(ondutyId)){
						 getOutputMsg().put("ret", 1);
					 }else{
						 getOutputMsg().put("ret", 0);
					 }
				 }
				this.getOutputMsg().put("MSG", "证件号码在系统已经存在！");
			}
		//}else{
			
		//}
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	@RequestMapping(value="saveInfo")
	public void saveInfo(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		Person data = (Person) getSubmitEntity();
		hrPersonService.updateEntity(data);
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "保存成功");
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	/**
	 * 上传并裁剪图片
	 * @param file
	 * @param dir
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping(value="uploadAndCut",method=RequestMethod.POST)
	public void uploadAndCut(@RequestParam(value="image")MultipartFile file,
			@RequestParam(value="fileName",required=false)String ordingName,
			@RequestParam(value="direct",required=true)String dir,
			@RequestParam(value="album",required=false)String album,
			@RequestParam(value="belong",required=true)String belong,
			HttpServletResponse response) throws IOException{
		if(!file.isEmpty()){
			dir = SystemUtil.getCurrentDataCenter() + dir;
			final byte[] bytes = file.getBytes();
			final String fileName = file.getOriginalFilename();
			final String path = SystemConfig.getParameter("image_path") + dir;
		    String suffix = fileName.substring(fileName.lastIndexOf("."));
			boolean ispass = true ;
			
			//限制大小  update by li.biao since 2013-12-20
			long fileSize = file.getSize();
			String allowSizeStr = ParamUtils.getParamValue("ALLOW_PHOTO_SIZE") ;
			double allowSize = StringUtils.isEmpty(allowSizeStr) ? 0 : Double.valueOf(allowSizeStr);// 0 无限制 单位M
			if(allowSize != 0d && fileSize > allowSize * 1024 * 1024){
				getOutputMsg().put("STATE", "FAIL");
				getOutputMsg().put("MSG", "上传文件超出限制，最大允许"+allowSize+"M");
				ispass = false ;
			}
			
			//允许通过
			if(ispass){
				
				File folder = new File(SystemConfig.getParameter("image_path") + dir);
				if(!folder.exists()){
					folder.mkdirs();
				}
				
				//String fileName = file.getOriginalFilename();
				boolean isAddSuffix = true;//用于判断返回链接时是否增加_size文本,人员时不增加
				String fileNameBack = fileName.substring(0,fileName.lastIndexOf("."));
					
				List<ImageAttribute> list = new ArrayList<ImageAttribute>();
				//width 或  height 属性为空则原图保存		
				ImageAttribute imgattr = new ImageAttribute(null,null,true);
				list.add(imgattr);
				ImageAttribute imgattr1 = new ImageAttribute(300,300,true);
				list.add(imgattr1);
				String url = ImageUtil.compressSave(bytes,path,suffix,list);
				if(!suffix.equals(".jpg")){
					String photoString =SystemConfig.getParameter("image_path")+dir + "/"+url;
					if(suffix.equals(".png")){
					ImageCutUtils.convert(photoString+"_300X300"+suffix,ImageUtil.IMAGE_TYPE_JPG,photoString+"_300X300"+".jpg");
					ImageCutUtils.convert(photoString+"_origin"+suffix,ImageUtil.IMAGE_TYPE_JPG,photoString+"_origin.jpg");
					}else{
						ImageUtil.convert(photoString+"_300X300"+suffix,ImageUtil.IMAGE_TYPE_JPG,photoString+"_300X300"+".jpg");
						ImageUtil.convert(photoString+"_origin"+suffix,ImageUtil.IMAGE_TYPE_JPG,photoString+"_origin.jpg");	
					}
					new File(photoString+"_300X300"+suffix).delete();
					new File(photoString+"_origin"+suffix).delete();
					suffix =".jpg";
				}
				
				Photo photo = new Photo();
				if(!StringUtils.isEmpty(album)){
					PhotoAlbum pa = new PhotoAlbum();
					pa.setId(album);
					photo.setAlbum(pa);
				}
				photo.setName(file.getOriginalFilename());
				
				photo.setPath(dir + "/"+url+"_size"+suffix);
				
				photoService.addEntity(photo);
//				System.out.print("belong__"+belong);
				Person person = hrPersonService.getEntityById(belong);
				person.setPhoto(dir + "/"+url+suffix);
				hrPersonService.updateEntity(person);
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "上传文件成功");
				//getOutputMsg().put("path", dir + "/"+url+(isAddSuffix?"_size":"")+suffix);
				getOutputMsg().put("path", dir + "/"+url+"_300X300"+suffix);
				getOutputMsg().put("id", photo.getId());
				getOutputMsg().put("fileName", fileNameBack);
			}
		}
		response.setContentType("text/html");
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	
	@RequestMapping(value="savePhotoCut")
	public void personPhotoCut(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		String dataId = getString("dataId");
	    int x1 = getInt("x1");
	    int y1 = getInt("y1");
	    int w =  getInt("w");
	    int h =  getInt("h");
	    Person person = this.hrPersonService.getEntityById(dataId);
	    String picSrc = person.getPhoto();
	    // String picSrc = "demo_ebcar/person/head/9dbb15e7-8942-4467-842d-bee6f21624cd.jpg";
	    String picDir = picSrc.substring(0,picSrc.lastIndexOf("/"));
	    String picName = picSrc.substring(picSrc.lastIndexOf("/"),picSrc.lastIndexOf("."));
	    String picSuffix =picSrc.substring(picSrc.lastIndexOf("."));
	    picDir = SystemConfig.getParameter("image_path")+picDir;
	    //ImageCutUtils.abscut(picDir+picName+picSuffix, picDir+picName+"_hd"+picSuffix, Integer.parseInt(x1), Integer.parseInt(y1), Integer.parseInt(w), Integer.parseInt(h));
	    getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "保存成功");
	    try {
	    	String  cutImagePath = picDir+picName+picSuffix;
	    	ImageCutUtils.cutImage(picDir+picName+"_300X300"+picSuffix,cutImagePath,x1,y1,w,h);
	    	ImageUtil.scale2(cutImagePath, picDir+picName+picSuffix,180,180,true);
	    	ImageUtil.scale2(cutImagePath, picDir+picName+"_50X50"+picSuffix,50,50,true);
	    	ImageUtil.scale2(cutImagePath, picDir+picName+"_30X30"+picSuffix,30,30,true);
	    	new File(picDir+picName+"_300X300"+picSuffix).delete();
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "文件读取出错");
		}
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
//		if(persons.size() > 0){
//			for(Person p : persons){
//				if(!p.getId().equals(person.getId())){
//					getOutputMsg().put("MSG", "该工号己存在");
//					return false;
//				}
//			}
//		}
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
