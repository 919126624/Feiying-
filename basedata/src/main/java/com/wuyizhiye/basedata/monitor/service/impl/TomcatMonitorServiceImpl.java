package com.wuyizhiye.basedata.monitor.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.monitor.dao.TomcatMonitorDao;
import com.wuyizhiye.basedata.monitor.model.TomcatMonitor;
import com.wuyizhiye.basedata.monitor.service.TomcatMonitorService;

/**
 * @ClassName TomcatMonitorServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="tomcatMonitorService")
@Transactional
public class TomcatMonitorServiceImpl extends BaseServiceImpl<TomcatMonitor> implements TomcatMonitorService {
	
	private static Logger logger = Logger.getLogger(TomcatMonitorServiceImpl.class); 
	
	@Autowired
	private TomcatMonitorDao tomcatMonitorDao;
	@Override
	protected BaseDao getDao() {
		return tomcatMonitorDao;
	}
	@Override
	public void getMonitorInfo(TomcatMonitor tomcat) {
		if(tomcat == null || StringUtils.isEmpty(tomcat.getTomcatName())){
			return ;
		}
		BufferedReader bufferedReader = null;  
        Process process = null;  
        try { 
            //执行命令
        	String[] cmds = {"/bin/sh","-c","ps -ef | grep " + tomcat.getTomcatName()};
            process = Runtime.getRuntime().exec(cmds);  
            //从执行结果中读取信息
            bufferedReader = new BufferedReader(new InputStreamReader(  
                    process.getInputStream()));  
            String line = null;  
            String pid = null ;
            while ((line = bufferedReader.readLine()) != null) { 
                //寻找标示字符串
            	line = line.replaceAll("\\s+", " ");
            	String[] lineStr = line.split(" ");
            	if(lineStr!=null && lineStr.length > 1){
            		pid = lineStr[1];
            		break ;
            	}
            } 
            tomcat.setTomcatPath(tomcat.getTomcatPath() + " pid="+pid+" ");
            if(pid != null && pid != "" ){
            	process = Runtime.getRuntime().exec("cat /proc/"+pid+"/status"); 
            	 //从执行结果中读取信息
                bufferedReader = new BufferedReader(new InputStreamReader(  
                        process.getInputStream()));  
                String pline = null;  
                int index = -1 ;
                String vmsize = "" ;
                String vmrss = "" ;
                while ((pline = bufferedReader.readLine()) != null) {  
                    index = pline.toLowerCase().indexOf("vmsize:");
                    if(index >= 0){
                    	//最大分配内存
                    	vmsize = pline.toLowerCase().substring(index + "vmsize:".length() + 1).trim();
                    	tomcat.setMaxMemory(Long.valueOf(vmsize.replace("kb", "").trim()));
                    	continue ;
                    }
                    index = pline.toLowerCase().indexOf("vmrss:");
                    if(index >= 0){
                    	//使用内存
                    	vmrss = pline.toLowerCase().substring(index + "vmrss:".length() + 1).trim();  
                    	tomcat.setTotalMemory(Long.valueOf(vmrss.replace("kb", "").trim()));
                    	continue ;
                    }
                }  
            }
        } catch (IOException e) {  
            logger.error("", e); 
            logger.error("getMonitorInfo:"+e.getMessage());
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
	}	
}