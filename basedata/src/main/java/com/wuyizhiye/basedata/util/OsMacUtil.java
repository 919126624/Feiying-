package com.wuyizhiye.basedata.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.basic.model.BaseConfigConstants;

/**
 * @ClassName OsMacUtil
 * @Description 获取各操作系统的服务器MAC
 * @author li.biao
 * @date 2015-4-2
 */
public class OsMacUtil {
	private static final String DEFAULT_MAC = "FF-FF-FF-FF-FF-FF" ;//默认MAC，试用
	private static final String DEFAULT_OS = "linux" ;//默认操作系统
	private static Logger log =Logger.getLogger(OsMacUtil.class); 
	/**
	 * 获取配置的操作系统
	 * @return 操作系统名字，小写
	 */
	public static String getOsName(){
		String osname = BaseConfigUtil.getCurrBaseConfig(BaseConfigConstants.OS_NAME);
		if(StringUtils.isEmpty(osname)){
			osname = DEFAULT_OS ;
		}
		osname = DEFAULT_OS ;
		
		return osname.toLowerCase() ;
	}
	
	public static String getOsNetWorkCard(){
		String osnetworkcard = BaseConfigUtil.getCurrBaseConfig(BaseConfigConstants.OS_NETWORK_CARD);
		if(StringUtils.isEmpty(osnetworkcard)){
			osnetworkcard = "eth0" ;
		}
		return osnetworkcard ;
	}
	
	/**
	 * 获取网卡MAC
	 * @return
	 */
	public static String getNetworkMac(){
		String mac = DEFAULT_MAC ;//默认临时模式
		
		//配置开发模式
		/*
		String devMac = SystemConfig.getParameter("devMac");
		if(!StringUtils.isEmpty(devMac)){
			mac = devMac ;
			return mac ;
		}
		*/
		//生产模式
		String osname = getOsName() ;
		if("linux".equals(osname) || "unix".equals(osname)){
			mac = getLinuxMac() ;
		}else if("windows7".equals(osname) || "win7".equals(osname)){
			mac = getWin7MAC() ;
		}else if("windows".equals(osname) || "win".equals(osname) || "xp".equals(osname) || "windows xp".equals(osname)){
			mac = getWindowsXpMAC() ;
		}else if("local".equals(osname) || "localhost".equals(osname)){//配置本地启用临时模式
			mac = DEFAULT_MAC ;
		}
		if(StringUtils.isEmpty(mac)){
			mac = DEFAULT_MAC ;
		}
		return mac ;
	}
	
	/**
	 * 获取linux、unix下面的主网卡MAC地址，只取eth0的
	 * @return 大写的MAC 连接符为“：”
	 */
	public static String getLinuxMac(){
		String mac = null;  
        BufferedReader bufferedReader = null;  
        Process process = null;  
        try {  
            // linux下的命令，我们只取eth0作为本地主网卡  
            process = Runtime.getRuntime().exec("ifconfig "+getOsNetWorkCard());  
            // 显示信息中包含有mac地址信息  
            bufferedReader = new BufferedReader(new InputStreamReader(  
                    process.getInputStream()));  
            String line = null;  
            int index = -1;  
            while ((line = bufferedReader.readLine()) != null) {  
                // 寻找标示字符串[hwaddr]  
                index = line.toLowerCase().indexOf("hwaddr");  
                if (index >= 0) {// 找到了  
                    // 取出mac地址并去除2边空格  
                    mac = line.substring(index + "hwaddr".length() + 1).trim();  
                    break;  
                }  
            }  
        } catch (IOException e) {  
            log.error("", e);  
        } finally {  
            try {  
                if (bufferedReader != null) {  
                    bufferedReader.close();  
                }  
            } catch (IOException e1) {  
                e1.printStackTrace();  
            }  
            bufferedReader = null;  
            process = null;  
        }  
		return mac == null ? null : mac.toUpperCase() ;
	}
	
	/** 
     * 获取widnows xp网卡的mac地址. 
     * @return 大写的MAC 连接符为“-”
     */  
    public static String getWindowsXpMAC() {  
        String mac = null;  
        BufferedReader bufferedReader = null;  
        Process process = null;  
        try {  
            // windows下的命令，显示信息中包含有mac地址信息  
            process = Runtime.getRuntime().exec("ipconfig /all");  
            bufferedReader = new BufferedReader(new InputStreamReader(  
                    process.getInputStream()));  
            String line = null;  
            int index = -1;  
            while ((line = bufferedReader.readLine()) != null) {  
//                System.out.println(line);  
                // 寻找标示字符串[physical  
                index = line.toLowerCase().indexOf("physical address");  
                  
                if (index >= 0) {// 找到了  
                    index = line.indexOf(":");// 寻找":"的位置  
                    if (index >= 0) {  
                        // 取出mac地址并去除2边空格  
                        mac = line.substring(index + 1).trim();  
                    }  
                    break;  
                }  
            }  
        } catch (IOException e) {  
            log.error("", e);  
        } finally {  
            try {  
                if (bufferedReader != null) {  
                    bufferedReader.close();  
                }  
            } catch (IOException e1) {  
                e1.printStackTrace();  
            }  
            bufferedReader = null;  
            process = null;  
        }  
        return mac == null ? null : mac.toUpperCase() ;  
    } 
    
    /** 
     * win7 获取网卡MAC地址 
     * @return 大写的MAC 连接符为“-”
     */  
    public static String getWin7MAC() {  
    	String mac = null ;
		StringBuffer sb = new StringBuffer();
		try {
			//从本地IP对象，获得网卡对象并得到mac地址，mac地址存在于一个byte数组中
			NetworkInterface ni = NetworkInterface.getByInetAddress(InetAddress
					.getLocalHost());
			if(ni != null){
				byte[] signatures = ni.getHardwareAddress();
				if(signatures!=null && signatures.length > 0){
					for (int i = 0; i < signatures.length; i++) {
						//转十六进制
						mac = Integer.toHexString(signatures[i] & 0xFF);
						if (mac.length() == 1) {
							mac = '0' + mac;//补0
						}
						sb.append(mac + "-");
					}
					mac = sb.toString();
					mac = mac.substring(0, mac.length() - 1);
					return mac.toUpperCase();
				}
			}
		} catch (SocketException e) {
			log.error("", e);
		} catch (UnknownHostException e) {
			log.error("", e);
		}
		return mac ;
    } 
    
    public static String getLinuxOsId(){
		String mac = null;  
        BufferedReader bufferedReader = null;  
        Process process = null;  
        try {  
            // linux下的命令，我们只取eth0作为本地主网卡  
            process = Runtime.getRuntime().exec("dmidecode -t 1");  
            // 显示信息中包含有mac地址信息  
            bufferedReader = new BufferedReader(new InputStreamReader(  
                    process.getInputStream()));  
            String line = null;  
            int index = -1;  
            while ((line = bufferedReader.readLine()) != null) {  
                // 寻找标示字符串[hwaddr]  
                index = line.toLowerCase().indexOf("serial number:");  
                if (index >= 0) {// 找到了  
                    // 取出mac地址并去除2边空格  
                    mac = line.substring(index + "serial number:".length()).trim();  
                    break;  
                }  
            }  
        } catch (IOException e) {  
            log.error("", e);  
        } finally {  
            try {  
                if (bufferedReader != null) {  
                    bufferedReader.close();  
                }  
            } catch (IOException e1) {  
                e1.printStackTrace();  
            }  
            bufferedReader = null;  
            process = null;  
        }  
		return mac == null ? null : mac.toUpperCase() ;
	}
	
	public static String getWindowProcessId(){
		String mac = null;  
		try{
		Process process = Runtime.getRuntime().exec(
			    new String[] { "wmic", "cpu", "get", "ProcessorId" });
			  process.getOutputStream().close();
			  Scanner sc = new Scanner(process.getInputStream());
			  String property = sc.next();
			  String serial = sc.next();
			  mac = serial;
		}catch(Exception e){
			log.error("", e);
		}
		return mac == null ? null : mac.toUpperCase() ;
	}
	
	public static String getLinuxProcessId(){
		String mac = null;  
        BufferedReader bufferedReader = null;  
        Process process = null;  
        try {  
            // linux下的命令，我们只取eth0作为本地主网卡  
            process = Runtime.getRuntime().exec("dmidecode -t 4");  
            // 显示信息中包含有mac地址信息  
            bufferedReader = new BufferedReader(new InputStreamReader(  
                    process.getInputStream()));  
            String line = null;  
            int index = -1;  
            while ((line = bufferedReader.readLine()) != null) {  
                // 寻找标示字符串[hwaddr]  
                index = line.toLowerCase().indexOf("id:");  
                if (index >= 0) {// 找到了  
                    // 取出mac地址并去除2边空格  
                    mac = line.substring(index + "id:".length()).trim();  
                    break;  
                }  
            }  
        } catch (IOException e) {  
            log.error("", e);  
        } finally {  
            try {  
                if (bufferedReader != null) {  
                    bufferedReader.close();  
                }  
            } catch (IOException e1) {  
                e1.printStackTrace();  
            }  
            bufferedReader = null;  
            process = null;  
        }  
		return mac == null ? null : mac.toUpperCase().replaceAll("\\s", "");
	}
    
	/**
	 * 获取网卡MAC
	 * @return
	 */
	public static String getProcessId(){
		String mac = DEFAULT_MAC ;//默认临时模式
		//生产模式
		String osname = getOsName() ;
		if("linux".equals(osname) || "unix".equals(osname)){
			String lp = getLinuxOsId();
			if(null!=lp) mac = lp;
		}else{
			String lp = getWindowProcessId();
			if(null!=lp) mac = lp;
		}
		log.info("===获取cpuID==="+mac+"======");
		return mac ;
	}
	
	public static String getNetWorkIp(){
		String ip = "";
		String osname = getOsName() ;
		if("linux".equals(osname) || "unix".equals(osname)){
			ip = getLinuxIp();
		}else{
			ip = getWindowIp();
		}
		return ip;
	}
	
	public static String getLinuxIp(){
		String mac = null;  
        BufferedReader bufferedReader = null;  
        Process process = null;  
        try {  
            // linux下的命令，我们只取eth0作为本地主网卡  
            process = Runtime.getRuntime().exec("ifconfig "+getOsNetWorkCard());  
            // 显示信息中包含有mac地址信息  
            bufferedReader = new BufferedReader(new InputStreamReader(  
                    process.getInputStream()));  
            String line = null;  
            int index = -1;  
            while ((line = bufferedReader.readLine()) != null) {  
                // 寻找标示字符串[hwaddr]  
                index = line.toLowerCase().indexOf("inet addr:"); 
//                System.out.println(line);
                if (index >= 0) {// 找到了  
                    // 取出mac地址并去除2边空格  
                    mac = line.substring(index + "inet addr:".length(),line.toLowerCase().indexOf("bcast")).trim();  
                    break;  
                }  
            }  
        } catch (IOException e) {  
            log.error("", e);  
        } finally {  
            try {  
                if (bufferedReader != null) {  
                    bufferedReader.close();  
                }  
            } catch (IOException e1) {  
                e1.printStackTrace();  
            }  
            bufferedReader = null;  
            process = null;  
        }  
		return mac == null ? null : mac.toUpperCase() ;
	}
	
	public static String getWindowIp(){
		String ip = "";
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			log.error("", e);
		}
		ip=addr.getHostAddress().toString();
		return ip;
	}
}
