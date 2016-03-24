package com.wuyizhiye.basedata.util;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * @ClassName FreeMarkUtil
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public class FreeMarkUtil {
	private static Logger logger=Logger.getLogger(FreeMarkUtil.class);
	/**存放模板文件夹的路径***/  
    private static String baseDir = "F://template/";  
    private static Configuration cfg = new Configuration();  
      
    /**编码格式***/  
    private static String charset = "UTF-8";  
  
  
    public Configuration getConfiguration(){
    	return cfg;
    }
    /** 
     * 根据路径获取模板 
     * @param path 
     * @return 
     */  
    public static Template TemplateFactory(String path) {  
        try {  
            //ResourcePatternResolver r = new PathMatchingResourcePatternResolver();  
            //Resource resource = r.getResource(baseDir);  
            File file = new File(baseDir);  
           // FileTemplateLoader templateLoader = new FileTemplateLoader(file);  
            //cfg.setTemplateLoader(templateLoader);  
            cfg.setDirectoryForTemplateLoading(file);
            Template t = cfg.getTemplate(path, charset);  
            return t;  
        } catch (IOException e) {  
            logger.error("", e);  
        }  
        return null;  
    }  
  
    public static void main(String[] args){
    	FreeMarkUtil.TemplateFactory("login.ftl");
    }
  
}  
