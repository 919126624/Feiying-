package com.wuyizhiye.basedata.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.wuyizhiye.base.datasource.EncryptionTools;
import com.wuyizhiye.base.exceptions.LicenseCodeEnum;
import com.wuyizhiye.base.exceptions.LicenseExceedException;
import com.wuyizhiye.base.exceptions.LicenseException;
import com.wuyizhiye.base.module.enums.BusinessTypeEnum;
import com.wuyizhiye.base.module.enums.ModuleEnum;
import com.wuyizhiye.base.module.model.ModuleLic;
import com.wuyizhiye.base.spring.DataSourceHolder;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.Encrypt;
import com.wuyizhiye.base.util.HttpClientUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.base.util.SystemConfig;
import com.wuyizhiye.basedata.basic.model.BaseConfigConstants;
import com.wuyizhiye.basedata.sync.model.DataSyncContants;

/**
 * @author FengMy
 * 
 * @since 2012-10-15
 */
@Component(value="validate")
@Aspect
@Lazy(value=false)
public class Validate {
	//private static final String SIGNATURE = getSignature();
	private static  String SIGNATURE = null;//update by li.biao since 2014-3-7
	private static  String NETWORKMAC = null;//update by li.biao since 2014-3-7 兼容之前license
	private static  String SERVERIP =null;
	private static final long PROP =100*24*60*60*1000L;
	private static Logger log =Logger.getLogger(Validate.class); 
	
	private static Map<String, Map<ModuleEnum, ModuleLic>> PermMap = new HashMap<String, Map<ModuleEnum, ModuleLic>>();
	//private static Map<String,Integer> pconLineSizeMap = new ConcurrentHashMap<String,Integer>();//系统在线人数，在framework里面监听修改
	private static Map<String,LicenseInfo> licenseInfoMap = new HashMap<String,LicenseInfo>();//系统许可信息
	//private static Map<String,Integer> mobileonLineSizeMap = new ConcurrentHashMap<String,Integer>();
	
	@Autowired
	private LoginHolder loginHolder;
	
	
	public static Map<ModuleEnum,ModuleLic> getCurrPerms(){
		if(StringUtils.isEmpty(DataSourceHolder.getDataSource())){
			Set<String> dset = PermMap.keySet();
			for(String e : dset){
				if(!PermMap.get(e).isEmpty())
				return PermMap.get(e);
			}
		}
		return PermMap.get(DataSourceHolder.getDataSource());
		
	}
	
	public static List<BusinessTypeEnum> getCurrBuinessType(){
		Map<ModuleEnum,ModuleLic> cperm = getCurrPerms();
		Set<ModuleEnum> moduleset = cperm.keySet();
		Set<BusinessTypeEnum> businessSet = new HashSet<BusinessTypeEnum>();
		List<BusinessTypeEnum> businessList = new ArrayList<BusinessTypeEnum>();
		Iterator<ModuleEnum> ite = moduleset.iterator();
		while(ite.hasNext()){
			ModuleEnum m = ite.next();
			businessSet.add(m.getParent());
		}	
		BusinessTypeEnum[] btary = BusinessTypeEnum.values();
		for(BusinessTypeEnum e : btary){
			if(businessSet.contains(e)) businessList.add(e);
		}
		return businessList;
		
	}
	
	public static String getCurrBuinessTypeStr(){
		Map<ModuleEnum,ModuleLic> cperm = getCurrPerms();
		Set<ModuleEnum> moduleset = cperm.keySet();
		Set<BusinessTypeEnum> businessSet = new HashSet<BusinessTypeEnum>();
		Iterator<ModuleEnum> ite = moduleset.iterator();
		while(ite.hasNext()){
			ModuleEnum m = ite.next();
			businessSet.add(m.getParent());
		}
		String bustr = "";
		Iterator<BusinessTypeEnum> btiterator = businessSet.iterator();
		while(btiterator.hasNext()){
			BusinessTypeEnum b = btiterator.next();
			bustr += "'"+b.getValue()+"',";
		}
		if(bustr.length()>0)bustr = bustr.substring(0, bustr.length()-1);
		return bustr;
		
	}
	
	public static LicenseInfo getCurrLicenseInfo(){
		if(StringUtils.isEmpty(DataSourceHolder.getDataSource())){
			Set<String> dset = licenseInfoMap.keySet();
			for(String e : dset){
				if(null!=licenseInfoMap.get(e))
				return licenseInfoMap.get(e);
			}
		}
		return licenseInfoMap.get(DataSourceHolder.getDataSource());
	}
	
	public static int getCurrPermNum(){
		return null==getCurrPerms()?0:getCurrPerms().keySet().size();
	}
	
	public Set<ModuleEnum> getCurrPermModule(){
		return null==getCurrPerms()?null:getCurrPerms().keySet();
	}
	
	/**
	 * 传正数或负数表示增加或减少
	 * @param num
	 */
	public static void addOnLineSize(int num,HttpSession sess){
		if(!StringUtils.isEmpty(DataSourceHolder.getDataSource())){

		LicenseInfo li = getCurrLicenseInfo();
		int pcallow = li.getPcallowsize();
		if(num>pcallow) {
			sess.invalidate();
			throw new LicenseExceedException("");	
		}
		}
	}
	
	public static void addMobileOnLineSize(int num,HttpSession sess){
		if(!StringUtils.isEmpty(DataSourceHolder.getDataSource())){
			LicenseInfo li = getCurrLicenseInfo();
			int mobileallow = li.getMobileallowsize();
			if(num>mobileallow) {
				sess.invalidate();
				throw new LicenseExceedException("");
			}
		}
	}
	
	
	@Before("execution(* com.wuyizhiye..*Controller.*(..))")
	public void execute(JoinPoint point) {
		String className = point.getTarget().getClass().getName();
		if(className.length() > 12){
			className = className.substring(13);
			ModuleEnum[] ms = ModuleEnum.values();
			boolean isValidate = false;
			for(ModuleEnum m:ms){
				if(className.startsWith(m.getPkg())){
					isValidate = true;
					break;
				}
			}
			if(!isValidate){
				return;
			}
			Map<ModuleEnum,ModuleLic> perms = this.getCurrPerms();
			if(null==perms) throw new LicenseException("无有效license");
			Set<ModuleEnum> modules = perms.keySet();
			ModuleEnum module = null;
			for(ModuleEnum e : modules){
				if(className.startsWith(e.getPkg())){
					module = e;
					break;
				}
			}
			if(module==null){
				for(ModuleEnum m:ms){
					if(className.startsWith(m.getPkg())){
						module = m;
						break;
					}
				}
				throw new LicenseException("LICENSE-CODE:" + LicenseCodeEnum.NOPERMS + "[模块["+(module==null?"":module.getName())+"]未获软件许可]");
			}else{
				if(perms.get(module).getStart().compareTo(new Date()) > 0){
					throw new LicenseException("LICENSE-CODE:" + LicenseCodeEnum.NOTOPERMS + "[模块["+(module==null?"":module.getName())+"]未到软件许可期]");
				}
				if(perms.get(module).getEnd().compareTo(new Date()) < 0){
					throw new LicenseException("LICENSE-CODE:" + LicenseCodeEnum.OUTOFPERMS + "[模块["+(module==null?"":module.getName())+"]己超出软件许可期限]");
				}
				/*
				if(perms.get(module).getCount()!=0 && perms.get(module).getCount() < getCurrOnLineSize()){
					throw new LicenseException("LICENSE-CODE:" + LicenseCodeEnum.OUTUSER + "[模块["+(module==null?"":module.getName())+"]己超出软件许可在线人数]");
				}
				*/
			}
		}
	}
	
	@PostConstruct
	public void init(){
		
//		SIGNATURE = OsMacUtil.getProcessId();//update by li.biao since 2014-3-7
//		NETWORKMAC = OsMacUtil.getNetworkMac();//update by li.biao since 2014-3-7 兼容之前license
//		SERVERIP = OsMacUtil.getNetWorkIp();
		
		List<String> dslist = SystemUtil.getDataSourceSingleList();
		for(int j=0;j<dslist.size();j++){		
			
			refreshOneData(dslist.get(j));		
		}		
		log.info("==License初始化完成==");
	}
	
	public static void refreshData(){
		String curDataSource = DataSourceHolder.getDataSource();	
		refreshOneData(curDataSource);
	}
	
	public static void refreshOneData(String dataCenter){
		//首先判断是否启用本地license,默认本地license
		String locallicense = SystemConfig.getParameter("localLicense");	
		if(StringUtils.isEmpty(locallicense)){
		locallicense = 
			BaseConfigUtil.getBaseConfigProp(dataCenter, BaseConfigConstants.LOCAL_LICENSE);
		}
		String licSrc = "";
		boolean isupdate = true;
		if(StringUtils.isEmpty(locallicense) || "yes".equals(locallicense)){
			//以配置文件为第一判断,其次判断数据中心
			String licensePath = SystemConfig.getParameter("licensePath");	
			
			if(StringUtils.isEmpty(licensePath)){
				licensePath = BaseConfigUtil.getBaseConfigProp(dataCenter, BaseConfigConstants.LICENSE_KEY);
			}
			if(!StringUtils.isEmpty(licensePath)){		
				try {
					licSrc = readFromFile(licensePath);
				} catch (IOException e) {
					log.error("", e);
				}
			}
		}else{
			String remotelicense = 
				BaseConfigUtil.getBaseConfigProp(dataCenter, BaseConfigConstants.REMOTE_LICENSE);
			String cpuid = SIGNATURE;		
			String timestamp = String.valueOf((new Date()).getTime());
			String serverip = SERVERIP;
			try{
			String encodelic = 
			HttpClientUtil.callHttpUrlThrowEx(
					(StringUtils.isEmpty(remotelicense)?DataSyncContants.DEFLICENSE:remotelicense)+"&type=validate&dc="+dataCenter+"&cpuid="+cpuid+"&start="+timestamp+"&ip="+serverip, "");			
			licSrc = EncryptionTools.decry(encodelic);	
			 log.error("===="+cpuid+"==="+serverip+"==="+licSrc);
			}catch(Exception e){
				isupdate = false;
				 log.error("====远程取license报错===="+e.getMessage());
			}
		   
		}
		
		String configcustno = BaseConfigUtil.getCurrCustomerNo();
		
		List<ModuleLic> licList = new ArrayList<ModuleLic>();
		Map<ModuleEnum,ModuleLic> permTemp = new HashMap<ModuleEnum,ModuleLic>();
		LicenseInfo licenseinfo = new LicenseInfo();
		licenseinfo.setMobileallowsize(-1);
		licenseinfo.setPcallowsize(-1);	
		
		String infostr = "";
			try {
				if(!StringUtils.isEmpty(licSrc)){
					String[] licInfo = licSrc.split("\n");
					infostr = licInfo[1];
					String[] infoary = infostr.split("\\|");
					if(infoary.length!=4) return;
					String customerno = infoary[0];
					licenseinfo.setCustomerno(infoary[0]);
					licenseinfo.setCustomername(infoary[1]);
					licenseinfo.setPcallowsize(Integer.parseInt(infoary[2]));
					licenseinfo.setMobileallowsize(Integer.parseInt(infoary[3]));
					
					//如果未设置客户编号或者客户编号和license编号一致,则继续读取license
					if(StringUtils.isEmpty(configcustno) || customerno.equals(configcustno)){			
					
						if(licInfo.length > 0){
							if(isDev(licInfo)){//临时模式 百天限制
								for(int i = 2; i < licInfo.length; i++){
									ModuleLic module = readModule(licInfo[i],dataCenter);
									if(module.getStart().compareTo(module.getEnd())<0 && (module.getEnd().getTime() - module.getStart().getTime()) <= PROP){
										licList.add(module);
										//allowcount = module.getCount();
									}
								}
							}else if(isSignature(licInfo)){//生产模式 没有百天限制
								for(int i = 2; i < licInfo.length; i++){
									ModuleLic module = readModule(licInfo[i],dataCenter);
									if(module.getStart().compareTo(module.getEnd())<0){
										licList.add(module);
										//allowcount = module.getCount();
									}
								}
							}
						}
					}
				}
			} catch (Exception e) {
				log.error("", e);
			}
		
		if(isupdate){
			for(ModuleLic lic : licList){
				permTemp.put(lic.getType(), lic);
			}
			PermMap.put(dataCenter, permTemp);
			//allowUserMap.put(dataCenter, allowcount);
			licenseInfoMap.put(dataCenter, licenseinfo);
		}
	}
	
	public static Map<String,String> readLicense(String licensePath){
		Map<String,String> map = new HashMap<String,String>();
		try {
			String locallicense = 
				BaseConfigUtil.getCurrBaseConfig(BaseConfigConstants.LOCAL_LICENSE);
			String licSrc = "";
			if(StringUtils.isEmpty(locallicense) || "yes".equals(locallicense)){			
				licSrc = readFromFile(licensePath);
			}else{
				String remotelicense = 
					BaseConfigUtil.getCurrBaseConfig(BaseConfigConstants.REMOTE_LICENSE);
				String cpuid = SIGNATURE;		
				String encodelic = 
				HttpClientUtil.callHttpUrl(
						(StringUtils.isEmpty(remotelicense)?DataSyncContants.DEFLICENSE:remotelicense)+"&type=validate&dc="+DataSourceHolder.getDataSource()+"&cpuid="+cpuid, "");	
				licSrc = EncryptionTools.decry(encodelic);	
			}
			String infostr = "";
			if(!StringUtils.isEmpty(licSrc)){
				log.debug("------LICENSE-SIGNATURE:"+SIGNATURE);
				String[] licInfo = licSrc.split("\n");		
				if(licInfo.length > 0){
					
					infostr = licInfo[1];
					String[] infoary = infostr.split("\\|");
					if(infoary.length!=4) return map;
					map.put("allowcount", infoary[2]);
					if(isDev(licInfo)){
						for(int i = 2; i < licInfo.length; i++){
							ModuleLic module = readModule(licInfo[i],null);
							if(module.getStart().compareTo(module.getEnd())<0 && (module.getEnd().getTime() - module.getStart().getTime()) <= PROP){							
								map.put("enddate", DateUtil.convertDateToStr(module.getEnd()));
								return map;
							}
						}
					}else if(isSignature(licInfo)){
						for(int i = 2; i < licInfo.length; i++){
							ModuleLic module = readModule(licInfo[i],null);
							if(module.getStart().compareTo(module.getEnd())<0){
								map.put("enddate", DateUtil.convertDateToStr(module.getEnd()));
								return map;
							}
						}
					}
				}
			}
		} catch (IOException e) {
			log.error("", e);
		} catch (ParseException e) {
			log.error("", e);
		}
		return map;
	}
	
	private static boolean isDev(String[] licInfo){
		return "FF-FF-FF-FF-FF-FF".equals(licInfo[0]);
	}
	
	private static boolean isSignature(String[] licInfo){
		String tempHard = NETWORKMAC.replace("-", ":");
		return (SIGNATURE.equals(licInfo[0]) || tempHard.equals(licInfo[0])||NETWORKMAC.equals(licInfo[0])) ;
	}
	
	private static ModuleLic readModule(String info,String datacenter) throws ParseException{
		if(StringUtils.isEmpty(info)){
			return null;
		}
		ModuleLic lic = new ModuleLic();
		String[] licInfo = info.split("\\|");
		if(licInfo.length!=4){
			return null;
		}
		lic.setType(Enum.valueOf(ModuleEnum.class, licInfo[0]));
		lic.setStart(new Date(Long.parseLong(licInfo[1])));
		lic.setEnd(new Date(Long.parseLong(licInfo[2])));
		lic.setCount(licInfo[3] == null ? 0 : Integer.valueOf(licInfo[3]));
		/*
		if(allowUserSize == -1){
			allowUserSize = lic.getCount() ;
		}
		if(StringUtils.isNotNull(datacenter)){
				allowUserMap.put(datacenter, lic.getCount());
		}
		*/
		return lic;
	}

	public static String getSignature() {

		String signature = "";
		StringBuffer sb = new StringBuffer();
		try {
			NetworkInterface ni = NetworkInterface.getByInetAddress(InetAddress
					.getLocalHost());
			if(ni != null){
				byte[] signatures = ni.getHardwareAddress();
				if(signatures!=null && signatures.length > 0){
					for (int i = 0; i < signatures.length; i++) {
						signature = Integer.toHexString(signatures[i] & 0xFF);
						if (signature.length() == 1) {
							signature = '0' + signature;
						}
						sb.append(signature + "-");
					}
					signature = sb.toString();
					signature = signature.substring(0, signature.length() - 1);
					return signature.toUpperCase();
				}
			}
		} catch (SocketException e) {
			log.error("", e);
		} catch (UnknownHostException e) {
			log.error("", e);
		}
		return "unknown";
	}
	
	private static String readFromFile(String path) throws IOException{
		File file = new File(path);
		if(file.exists()){
			FileInputStream fis = new FileInputStream(file);
			int length = fis.available();
			byte[] data = new byte[length];
			fis.read(data, 0, length);
			if(data.length > 0){
				for(int i = 1; i < data.length; i++){
					data[i] = (byte)(data[i] - data[0]);
				}
			}
			Encrypt enc = new Encrypt();
			byte[] result = enc.decrypt(data);
			return new String(result,"UTF-8");
		}
		return null;
	}
	
	public static void main(String[] args) throws Exception{
		File file = new File("F://license-yyxdev.lic");
		if(file.exists()){
			FileInputStream fis = new FileInputStream(file);
			int length = fis.available();
			byte[] data = new byte[length];
			fis.read(data, 0, length);
			if(data.length > 0){
				for(int i = 1; i < data.length; i++){
					data[i] = (byte)(data[i] - data[0]);
				}
			}
			String str = new String(data,"ISO-8859-1");
//			System.out.println(str);
			Encrypt enc = new Encrypt();
			byte[] result = enc.decrypt(str.getBytes("ISO-8859-1"));
//			System.out.println(new String(result,"UTF-8"));
			
		}
	}
	
	public static String getStaticSignature() {
		return SIGNATURE;
	}
	
	public static String getServerIp(){
		return SERVERIP;
	}
}
