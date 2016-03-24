package com.wuyizhiye.base.util;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.wuyizhiye.base.spring.ApplicationContextAware;

/**
 * @ClassName SystemConfig
 * @Description 系统参数类
 * @author li.biao
 * @date 2015-4-1
 */
@Component(value="systemConfig")
public class SystemConfig {
	
	private Resource res;
	private Properties pros = new Properties();
	static private String filePath;

	public void setResource(Resource resource) throws IOException{
		this.res = resource;
		pros.load(res.getInputStream());
		filePath = res.getURL().getPath().substring(1);
	}
	/**
	 * 获取系统参数
	 * @param key
	 * @return
	 */
	public static String getParameter(String key){
		SystemConfig cfg = ApplicationContextAware.getApplicationContext().getBean("systemConfig", SystemConfig.class);
		return cfg.pros.getProperty(key);
	}
	/**
	 * 修改系统参数
	 * @param key
	 * @param value
	 * @return
	 */
	public static void setParameter(String key, String value){
		SystemConfig cfg = ApplicationContextAware.getApplicationContext().getBean("systemConfig", SystemConfig.class);
		cfg.pros.setProperty(key, value);
		//数据更新到本地文件
		//writeProperties(key, value);
	}
	/**
	 * 获取系统参数
	 * @param key
	 * @return
	 */
	public static String getParameter(String key , String fileName){
		SystemConfig cfg = ApplicationContextAware.getApplicationContext().getBean(fileName, SystemConfig.class);
		return cfg.pros.getProperty(key);
	}
	
	
	
	/**
	 * 修改系统参数
	 * @param key
	 * @param value
	 * @return
	 */
	public static void setParameter(String key, String value,String fileName){

		writeProperties(key, value,fileName);
	}
	
	/**
	 * 更新属性文件的value,如果存在则更新，不存在则插入
	 * @param keyname
	 * @param keyvalue
	 */
	public static void writeProperties(String keyname,String keyvalue) {        
        try {
        	SystemConfig cfg = ApplicationContextAware.getApplicationContext().getBean("systemConfig", SystemConfig.class);
        	//cfg.pros.load(new FileInputStream(filePath));
        	// 调用 Hashtable 的方法 put，使用 getProperty 方法提供并行性。
            // 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
            OutputStream fos = new FileOutputStream(cfg.res.getFile()); 
            cfg.pros.setProperty(keyname, keyvalue);
            cfg.pros.store(fos, "Update '" + keyname + "' value");
        } catch (IOException e) {
            System.err.println("属性文件更新错误");
        }
    }
	
	/**
	 * 更新属性文件的value,如果存在则更新，不存在则插入
	 * @param keyname
	 * @param keyvalue
	 * @param fileName
	 */
	public static void writeProperties(String keyname,String keyvalue, String fileName) {        
        try {
        	SystemConfig cfg = ApplicationContextAware.getApplicationContext().getBean(fileName, SystemConfig.class);
        	// 调用 Hashtable 的方法 put，使用 getProperty 方法提供并行性。
            // 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
			OutputStream fos = new FileOutputStream(cfg.res.getFile());
			cfg.pros.setProperty(keyname, keyvalue);
            cfg.pros.store(fos, "Update '" + keyname + "' value");
        } catch (IOException e) {
            System.err.println("属性文件更新错误");
        }
    }
	
	public static void main(String[] args) {
//		System.out.println(System.getProperty("user.dir"));
	}
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
