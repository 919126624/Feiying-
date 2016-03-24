package com.wuyizhiye.framework.controller;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.wuyizhiye.base.datasource.EncryptionTools;
import com.wuyizhiye.base.exceptions.LicenseExceedException;
import com.wuyizhiye.base.spring.DataSourceHolder;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.base.util.SystemConfig;
import com.wuyizhiye.basedata.basic.model.BaseConfig;
import com.wuyizhiye.basedata.basic.service.BaseConfigService;
import com.wuyizhiye.basedata.module.service.ModuleService;
import com.wuyizhiye.basedata.util.LoginHolder;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.basedata.util.Validate;
import com.wuyizhiye.framework.base.controller.BaseController;

/**
 * @ClassName BaseConfigController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller 
@RequestMapping(value="manager/*")
public class BaseConfigController extends BaseController {
	@Autowired
	private BaseConfigService baseConfigService;
	@Autowired
	private ModuleService moduleService;
	
	private static Logger log=Logger.getLogger(BaseConfigController.class); 
	@RequestMapping(value="admin-console")
	public String show(ModelMap model){
		putPath();
		model.put("dataSourceList", SystemUtil.getDataSourceList());
		model.put("staticsignature",Validate.getStaticSignature());
		
		return "framework/admin-console";
	}
	
	@RequestMapping(value="admin-save")
	public void save(HttpServletResponse response){
		
		String dataCenter = this.getString("dataCenter");
		if(StringUtils.isNotNull(dataCenter)){
		
			DataSourceHolder.setDataSource(dataCenter);
		
		String curpanel = this.getString("curpanel");
		try{

			//String licensepath = this.getString("licensepath");
			//String houseindex = this.getString("houseindex");
			//String resourceindex = this.getString("resourceindex");
			//String indexurl = this.getString("indexurl");
			String controlmode = this.getString("controlmode");
		//	String homeurl = this.getString("homeurl");
			//String housepowerindex = this.getString("housepowerindex");
			String mailid = this.getString("mailclientid");
			String mailsecret = this.getString("mailclientsecret");
			String browerrestrict = this.getString("browerrestrict");
		//	BaseConfig lic = new BaseConfig("licensepath","license路径",licensepath);
			//BaseConfig hsbc = new BaseConfig("houseindex","盘源索引",houseindex);
		//	BaseConfig ribc = new BaseConfig("resourceindex","资源客索引",resourceindex);
		//	BaseConfig indexurlbc = new BaseConfig("indexurl","索引地址",indexurl);
		//	BaseConfig homeurlbc = new BaseConfig("homeurl","首页地址",homeurl);
			BaseConfig controlmodebc = new BaseConfig("controlmode","控制模式",controlmode);
		//	BaseConfig hpibc = new BaseConfig("housepowerindex","权限房源索引",housepowerindex);
			BaseConfig mailidbc = new BaseConfig("mailclientid","邮件client_id",mailid);
			BaseConfig mailsecretbc = new BaseConfig("mailclientsecret","邮件client_secret",mailsecret);
			BaseConfig browerrestrictbc = new BaseConfig("browerrestrict","浏览器限制",browerrestrict);
			List<BaseConfig>  bclist = new ArrayList<BaseConfig>();
			bclist.add(controlmodebc);
		//	bclist.add(indexurlbc);
		//	bclist.add(ribc);
		//	bclist.add(hsbc);
		//	bclist.add(lic);
		//	bclist.add(homeurlbc);
		//	bclist.add(hpibc);
			bclist.add(mailidbc);
			bclist.add(mailsecretbc);
			bclist.add(browerrestrictbc);
			this.baseConfigService.updateList(bclist);
	
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("MSG", "保存成功");
		}catch(Exception e){
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", e.getMessage());
		}
			
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "数据中心参数为空,请重新打开页面");
		}
		
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
		
	}
	
	@RequestMapping(value="loadConfig")
	public void loadConfig(HttpServletResponse response) throws Exception{
		String dataCenter = this.getString("dataCenter");
		if(StringUtils.isEmpty(dataCenter)) throw new Exception("没有选择数据中心");
		DataSourceHolder.setDataSource(dataCenter);
		try{
		List<BaseConfig> datalist = queryExecutor.execQuery(getListMapper(), this.getParaMap(),BaseConfig.class);
		outPrint(response, JSONArray.fromObject(datalist, getDefaultJsonConfig()));
		}catch(Exception e){
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", e.getMessage());
			outPrint(response, JSONObject.fromObject(getOutputMsg()));
		}
		
	}
	
	@RequestMapping(value="upload",method=RequestMethod.POST)
	public void upload(@RequestParam(value="license")MultipartFile file,
			@RequestParam(value="fileName",required=false)String ordingName,
			@RequestParam(value="direct",required=true)String dir,
			@RequestParam(value="dataCenter",required=true)String dataCenter,
			HttpServletResponse response) throws IOException{
		if(!file.isEmpty()){
			dir = dataCenter+"/" + dir;
			File path = new File(SystemConfig.getParameter("image_path") + dir);
			if(!path.exists()){
				path.mkdirs();
			}
			String fileName = file.getOriginalFilename();
			//以当前时间命名license
			fileName = (StringUtils.isEmpty(ordingName)?StringUtils.getCurrdate("yyyyMMddHHmmss"):ordingName) + fileName.substring(fileName.lastIndexOf("."));
			File img = new File(path.getPath() + "/" + fileName);
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
			
			Map<String,String> map = Validate.readLicense(path.getPath() + "/" + fileName);
			
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("MSG", "上传文件成功");
			getOutputMsg().put("PATH", path.getPath() + "/" + fileName);
			getOutputMsg().put("fileName", fileName);
			getOutputMsg().putAll(map);
			
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "上传文件为空");
		}
		response.setContentType("text/html");
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	
	@RequestMapping(value="loadLicense")
	public void loadLicense(HttpServletResponse response) throws Exception{
		String licensepath = this.getString("licensepath");
		Map<String,String> map = Validate.readLicense(licensepath);
		getOutputMsg().putAll(map);
		response.setContentType("text/html");
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	@RequestMapping(value="refreshBaseConfig")
	public void refreshBaseConfig(HttpServletResponse response) throws Exception{
		
		String dataCenter = this.getString("dataCenter");
		if(StringUtils.isEmpty(dataCenter)) throw new Exception("没有选择数据中心");
		//设置数据中心
		DataSourceHolder.setDataSource(dataCenter);
		Map<String,String> configtemp = new HashMap<String,String>();
		List<BaseConfig> cflist = null;
		try{
		cflist = (List<BaseConfig>)queryExecutor.execQuery("com.wuyizhiye.basedata.basic.dao.BaseConfigDao.select",null, BaseConfig.class);
		for(int j=0;j<cflist.size();j++){
			BaseConfig temp = cflist.get(j);			
			configtemp.put(temp.getNumber(), temp.getParamValue());					
		}
			
		LoginHolder.getAllBaseConfig().put(dataCenter, configtemp);		
		Validate.refreshOneData(dataCenter);
		moduleService.refreshLicense();
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "操作成功");
		}catch(Exception e){
			log.error(e.getMessage());
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", e.getMessage());
		}	
		
		response.setContentType("text/html");
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	private String getListMapper(){
		return "com.wuyizhiye.basedata.basic.dao.BaseConfigDao.select";
	}
	
	private void putPath(){
		int port = getRequest().getServerPort();
		String scheme = getRequest().getScheme();
		String path = getRequest().getScheme()+"://"+getRequest().getServerName() + ((("http".equals(scheme) && port == 80) ||("https".equals(scheme)  && port == 443)) ? "" : ":" + port) + getRequest().getContextPath();
		getSession().setAttribute("base",path);
		getSession().setAttribute("imgBase",path+"/images");
		getSession().setAttribute("imgPrePath",path+"/default/style/images");
		getSession().setAttribute("initImagePath",path+"/default");
	}
	
	@RequestMapping(value="getlicense",method=RequestMethod.POST)
	public void getlicense(HttpServletResponse response) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("FF-FF-FF-FF-FF-FF").append("\n");
		sb.append("0000|开发环境|50|50").append("\n");
		sb.append("BASEDATA|1414339200000|29820844800000|80").append("\n");
		sb.append("INTERFLOW|1414339200000|29820844800000|80").append("\n");
		sb.append("BLOG|1396108800000|1404057600000|80").append("\n");
		sb.append("CMCT|1396108800000|1404057600000|80").append("\n");
		sb.append("MOBILE|1396108800000|1404057600000|80").append("\n");
		//Encrypt enc = new Encrypt();
		//byte[] saveData = enc.encrypt(sb.toString());
		/*if(saveData.length > 0){
			for(int i = 1; i < saveData.length; i++){
				saveData[i] = (byte)(saveData[i] + saveData[0]);
			}
		}*/
		
		this.outPrint(response, EncryptionTools.encry(sb.toString()));										
	}
	
	@RequestMapping(value="licenseExceedTest")
	public String licenseExceedTest(HttpServletResponse response){		
		throw new LicenseExceedException("");			
	}
}
