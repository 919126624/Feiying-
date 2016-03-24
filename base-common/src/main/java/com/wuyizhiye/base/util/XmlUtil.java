package com.wuyizhiye.base.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import com.wuyizhiye.base.common.XmlResp;


/**
 * @ClassName XmlUtil
 * @Description xml读取工具类
 * @author li.biao
 * @date 2015-4-1
 */
public class XmlUtil {
	
	/**
	 * 解析xml返回解析好的XmlResp类
	 * @param xmlDoc	xml文档路径
	 * @param resp		XmlResp对象
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static XmlResp parseXml(String xmlDoc,XmlResp resp) throws JDOMException, IOException{
		 	StringReader read = new StringReader(xmlDoc);
	        //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
	        InputSource source = new InputSource(read);
	        //创建一个新的SAXBuilder
	        SAXBuilder sb = new SAXBuilder();
            Map<String,Object> map = new HashMap<String,Object>();
	        
            //通过输入源构造一个Document
            Document doc = sb.build(source);
            //取的根元素
            Element root = doc.getRootElement();
           
            //得到根元素所有子元素的集合
            List child = root.getChildren();
            //获得XML中的命名空间（XML中未定义可不写）
            Element et = null;
	        for(int i=0;i<child.size();i++){
	        	et = (Element)child.get(i);
	        	//找到叶子节点,只支持两级
	        	if(et.getContentSize()>1){
	        		List<Element> etlist = et.getChildren();
	        		for(int j=0;j<etlist.size();j++){
	        			Element temp = etlist.get(j);
	        			map.put(temp.getName(), temp.getContent().size()>0?temp.getContent(0):"");
	        		}
	        	}else{
	        		map.put(et.getName(), et.getContent().size()>0?et.getContent(0):"");
	        	}
	        }
	        resp = resp.praseResp(map);
	            
	        
	        return resp;
	    }
	
	public static void main(String[] args){
		String xmldoc = "<xml><ToUserName><![CDATA[toUser]]></ToUserName><FromUserName><![CDATA[FromUser]]></FromUserName>"
					+"<CreateTime>123456789</CreateTime>"
					+"<MsgType><![CDATA[event]]></MsgType>"
					+"<Event><![CDATA[subscribe]]></Event>"
					+"</xml>";
		//parseXml(xmldoc);
	
		 
	}
}
