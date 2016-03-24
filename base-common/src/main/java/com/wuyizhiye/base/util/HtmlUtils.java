package com.wuyizhiye.base.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 * @ClassName HtmlUtils
 * @Description 静态化HTML工具 
 * @author li.biao
 * @date 2015-4-1
 */
public class HtmlUtils {
    // 获取img标签正则  
    private static final String IMGURL_REG = "<img.*src=(.*?)[^>]*?>";  
    // 获取src路径的正则  
    private static final String IMGSRC_REG = "http:\"?(.*?)(\"|>|\\s+)";  
    
    // 获取hidden 图片链接 标签
    private static final String HIDIMGURL_REG = "<input.*hiddenimg\"\\s+value=(.*?)[^>]*?>";  
   
    // 获取JS标签正则  
    private static final String JS_REG = "<script.*src=(.*?)[^>]*?>";  
    // 获取JS路径的正则  
    private static final String JSSRC_REG = "http:\"?(.*?)(\"|>|\\s+)";  
    
    // 获取CSS标签正则  
    private static final String CSS_REG = "<link.*href=(.*?)[^>]*?>";  
    // 获取CSS路径的正则  
    private static final String CSSLINK_REG = "http:\"?(.*?)(\"|>|\\s+)"; 
    
    private static Logger log=Logger.getLogger(HtmlUtils.class); 
    
	public static void staticHtml(String address,String file) throws Exception{
		File f = new File(file);
		if(!f.getParentFile().exists()){
			f.getParentFile().mkdirs();
		}
		staticHtml(address, f);
	
	}
	public static void staticHtml(String address,String file,List<Map> lisp) throws Exception{
		File f = new File(file);
		if(!f.getParentFile().exists()){
			f.getParentFile().mkdirs();
		}
		staticHtml(address, f,lisp);
	
	}
	/**
	 * 静态化页面
	 * @param address
	 * @param file
	 * @throws IOException
	 */
	public static void staticHtml(String address,File file) throws IOException{
		
		URL url = new URL(address);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		BufferedReader bufReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		FileWriter fos = new FileWriter(file);
		BufferedWriter buffer = new BufferedWriter(fos);
		String tempString = null; 
		while((tempString = bufReader.readLine()) != null){		
			buffer.write(tempString);
			buffer.newLine();
			buffer.flush();
		}	
        bufReader.close();  
		conn.disconnect();
        fos.flush();
        buffer.close();
		fos.close();
	}
	
	public static void staticHtml(String address,File file,List<Map> lisp) throws IOException{
		URL url = new URL(address);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		BufferedReader bufReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		FileWriter fos = new FileWriter(file); 
		
		String tempString = null;  
        int line = 1;  
        // 一次读入一行，直到读入null为文件结束  
        StringBuffer chunk = new  StringBuffer();
        while ((tempString = bufReader.readLine()) != null) {  
            // 显示行号  
            chunk.append(tempString);
            chunk.append("\r\n");
        }  
        bufReader.close();  
		String html = chunk.toString();
		Iterator<Map> its = lisp.iterator();
		while(its.hasNext()){
			Map it = its.next();
			if(html.indexOf(it.get("OLDURL").toString())!=-1){
				html = html.replace(it.get("OLDURL").toString(), it.get("NEWURL").toString());
			}
		}
		fos.write(html);
		fos.flush(); 
		conn.disconnect();
		fos.close();
	}
	public static void staticHtmlPad(String domainName,String address,String dir,String fileUrl,String todomainName) throws IOException{
		if(StringUtils.isEmpty(fileUrl)) fileUrl = dir+address+(address.indexOf(".html")>-1?"":".html");
		else fileUrl = dir + fileUrl;
		File f = new File(fileUrl);
		if(!f.getParentFile().exists()){
			f.getParentFile().mkdirs();
		}
		URL url = new URL(domainName+address);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		BufferedReader bufReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		
		BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(f));   
		StringBuffer chunk = new  StringBuffer();
		String tempString = null; 
		while((tempString = bufReader.readLine()) != null){		
			chunk.append(tempString);
			chunk.append("\r\n");
		}
		bufReader.close();
		
		String html = chunk.toString();
		
		//获取图片标签  
        List<String> imgUrl = getImageUrl(html,IMGURL_REG);  
        //获取图片src地址  
        List<String> imgSrc = getImageSrc(imgUrl,IMGSRC_REG);  
        
        List<String> hidimglist = HtmlUtils.getImageUrl(html, HIDIMGURL_REG);
		List<String> hidimgsrc = HtmlUtils.getImageSrc(hidimglist, IMGSRC_REG);
		
		//获取JS
        List<String> js = getImageUrl(html,JS_REG);  
        //获取JS地址
        List<String> jssrc = getImageSrc(js,JSSRC_REG);  
        //获取css
        List<String> css = getImageUrl(html,CSS_REG);  
        //获取ss地址
        List<String> csssrc = getImageSrc(css,CSSLINK_REG);  
        Download(hidimgsrc,domainName,dir);
        Download(jssrc,domainName,dir);
        Download(imgSrc,domainName,dir);
        Download(csssrc,domainName,dir);
        html = html.replace(domainName+"/images", todomainName+"/images");
        html = html.replace(domainName+"/default/style", todomainName+"/style");
        html = html.replace(domainName+"/default/js", todomainName+"/js");
		fos.write(html.getBytes());
		fos.flush(); 
		
		conn.disconnect();
		fos.close();
	}
	/*** 
     * 获取ImageUrl地址 
     *  
     * @param HTML 
     * @return 
     */  
    private static List<String> getImageUrl(String html,String type) {  
        Matcher matcher = Pattern.compile(type).matcher(html);  
        List<String> listImgUrl = new ArrayList<String>();  
        while (matcher.find()) {  
            listImgUrl.add(matcher.group());  
        }  
        return listImgUrl;  
    }  
  
    /*** 
     * 获取ImageSrc地址 
     *  
     * @param listImageUrl 
     * @return 
     */  
    private static List<String> getImageSrc(List<String> listImageUrl,String type) {  
        List<String> listImgSrc = new ArrayList<String>();  
        for (String image : listImageUrl) {  
            Matcher matcher = Pattern.compile(type).matcher(image);  
            while (matcher.find()) {  
                listImgSrc.add(matcher.group().substring(0, matcher.group().length() - 1).split(";")[0]);  
            }  
        }  
        return listImgSrc;  
    }  
    /*** 
     * 下载链接
     *  
     * @param listImgSrc 
     */  
    private static void Download(List<String> listImgSrc,String domainName,String dir) {  
       
            for (String url : listImgSrc) {
            	String imageName = "";
            	 try {  
                imageName = url.substring(url.lastIndexOf("/") + 1, url.length());  
                if("".equals(imageName))continue;
                url=url.replace("'", "");
                URL uri = new URL(url);  
                InputStream in = uri.openStream();  
                if(imageName.indexOf("?")>-1){
                	imageName = imageName.split("\\?")[0];
                }
                
                String type = "";
                if(url.startsWith(domainName+"/images")) type = "/images/";
                if(url.startsWith(domainName+"/default/style")) type = "/style/";
                if(url.startsWith(domainName+"/default/js")) type = "/js/";
                
                
               // System.out.println(dir+getDir(url,imageName,type)+imageName);
                createDir(url,imageName,type,dir);
                FileOutputStream fo = new FileOutputStream(new File(dir+getDir(url,imageName,type)+imageName));  
                byte[] buf = new byte[1024];  
                int length = 0;  
               
                while ((length = in.read(buf, 0, buf.length)) != -1) {  
                    fo.write(buf, 0, length);  
                }  
                in.close();  
                fo.close();  
                log.info(imageName + "下载完成");  
            	 } catch (Exception e) { 
//                 	e.printStackTrace();
            		log.error("", e);
                 	log.error(imageName + "下载失败");
                 }  
            }  
        
    }  
    
    public static void makeDir(String file){
    	File f = new File(file);
		if(!f.getParentFile().exists()){
			f.getParentFile().mkdirs();
		}
		f.mkdir();
    }
    public static void createDir(String url,String name,String type,String fileUrl){
    	name = name.replace("'", "");
    	url = url.replace("'", "");
    	String s = url.substring(url.indexOf(type),url.indexOf(name));
//    	System.out.println(fileUrl+s);
    	makeDir(fileUrl+s);
    }
    public static String getDir(String url,String name,String type){
    	name = name.replace("'", "");
    	url = url.replace("'", "");
//    	System.out.println(url+"=="+name+"=="+type);
    	String s = url.substring(url.indexOf(type),url.indexOf(name));
    	return s;
    }
    
    public static String replaceHtml(Map<String,String> param,String file) throws IOException{
    	File f = new File(file);
    	FileInputStream fileimp = new FileInputStream(f);
    	BufferedInputStream isr = new BufferedInputStream(fileimp);
		byte[] buff = new byte[2048];
		int len = 0;	
		StringBuffer chunk = new  StringBuffer();
		while((len = isr.read(buff))>0){
			chunk.append(new String(buff, 0, len));				
		}
		isr.close();
		fileimp.close();
		
		FileWriter fos = new FileWriter(f);
		String html = chunk.toString();
		if(null==param) return "FAIL";
		Set<String> keyset = param.keySet();
		for(String s : keyset){
			html = html.replace(s, param.get(s)); 
		}
		fos.write(html);
		fos.close();		
    	return "SUCCESS";
    }
    
	public static void main(String[] args) throws Exception{
		
		HtmlUtils.staticHtmlPad("http://mobile.51zhiye.com","/pad/mediumData?houseProjectId=7944a420-1dae-4682-b570-ce2d1ecef38d","D://My Documents//58ttt//","2.html","file:///android_asset");
		/*String html = "<input type=\"hidden\" key=\"hiddenimg\" value=\"http://mobile.51zhiye.com/default/style/pad/images/promote/ypls/ypls1.jpg\"/>";
		html += "<img src=\"\"/>";
		html += "<input  type=\"hidden\" key=\"hiddenimg\" value=\"http://mobile.51zhiye.com/default/style/pad/images/promote/ypls/ypls3.jpg\"/>";
		List<String> strlist = HtmlUtils.getImageUrl(html, HIDIMGURL_REG);
		List<String> srclist = HtmlUtils.getImageSrc(strlist, IMGSRC_REG);
		for(String s : srclist){
//			System.out.println(s);
		}*/
	}
}
