package com.wuyizhiye.cmct.sms.service.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.cmct.sms.model.ShortMessage;
import com.wuyizhiye.cmct.sms.service.ShortMessageService;

/**
 * @ClassName YYShortMessageServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="yyShortMessageService")
public class YYShortMessageServiceImpl extends BaseServiceImpl<ShortMessage> implements ShortMessageService {

	public static class ParamConstants{
		public static String userid="userid";
		public static String account="account";
		public static String password="password";
		public static String action="action";
		public static String content="content";
		public static String sendtime="sendTime";
		public static String mobile="mobile";
		public static String extno="extno";
	}
	
	public static class ResultConstants{
		public static String status="returnstatus";
		public static String message="message";
		public static String remainpoint="remainpoint";
		public static String taskID="taskID";
		public static String successCounts="successCounts";
		public static String payinfo="payinfo";
		public static String overage="overage";
		public static String sendTotal="sendTotal";
	}
	
	@Override
	public Map<String, Object> sendMessage(String groupId, String accountName,
			String password, String url, String from, String to,
			String content, Date sendTime) throws Exception {
		
		Map<String,Object> map=new HashMap<String, Object>();
		Map<String,Object> resultmap=new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer(url+"?1=1") ;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timeStr =null;
		if(sendTime!=null){
			timeStr = df.format(sendTime) ;
			sb.append("&"+ParamConstants.sendtime+"="+timeStr);
		}
		
		//username=机构ID:登录名&password=pass&from=001&to=130xxxxxxxxx&content=hell,world&presendTime=2003-02-03 12:12:03
		//params.put("extno","");
		sb.append("&"+ParamConstants.action+"=send");
		sb.append("&"+ParamConstants.userid+"="+groupId);
		sb.append("&"+ParamConstants.account+"="+accountName);
		sb.append("&"+ParamConstants.password+"="+password);
		sb.append("&"+ParamConstants.mobile+"="+to);
		sb.append("&"+ParamConstants.content+"="+URLEncoder.encode(content));
		
		String result;
		try {
			result = httpPost(sb.toString());
			map = readXMLString4TagName(result);
			
			if("Success".equalsIgnoreCase(map.get(ResultConstants.status).toString())){
				resultmap.put("state", "ok");
				resultmap.put("remainPoint", map.get(ResultConstants.remainpoint));
				resultmap.put("taskId", map.get(ResultConstants.taskID));
				resultmap.put("MSG", map.get(ResultConstants.message));
			}else{
				resultmap.put("state", "FAIL");
				resultmap.put("MSG", map.get(ResultConstants.message));
			}
			
		}catch (Exception e) {
			resultmap.put("STATE", "FAIL");
			resultmap.put("MSG", e.getMessage());
		}
						
		return resultmap;
	}

	@Override
	public String getSmsBalance(String groupId, String accountName,
			String password, String url) throws Exception {
		
		Map<String,Object> map=new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer(url+"?") ;
		sb.append(ParamConstants.action+"=overage");
		sb.append("&"+ParamConstants.userid+"="+groupId);
		sb.append("&"+ParamConstants.account+"="+accountName);
		sb.append("&"+ParamConstants.password+"="+password);
		
		String result = "";
		try {
			String resultxml = httpPost(sb.toString());
			map = readXMLString4TagName(resultxml);
			if("Sucess".equalsIgnoreCase(map.get(ResultConstants.status).toString())){
				result = map.get(ResultConstants.overage).toString();
			}				
		}catch (Exception e) {
			e.printStackTrace();
		}

		
		return result;
	}

	@Override
	public List<Map<String, Object>> getReplyData(String groupId,
			String accountName, String password, String url) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> getMessageStatus(String groupId,
			String accountName, String password, String url) throws Exception {
		return null;
	}

	@Override
	protected BaseDao getDao() {
		// TODO Auto-generated method stub
		return null;
	}

	public static String httpPost(String url) throws IOException{
		StringBuffer result = new StringBuffer("");
		url = url.replaceAll(" ", "%20");
		URL posturl = new URL(url);

		// 打开url连接
		HttpURLConnection connection = (HttpURLConnection) posturl.openConnection();
		// 设置url请求方式 ‘get’ 或者 ‘post’
		connection.setRequestMethod("POST");

		// 发送
		BufferedReader in = new BufferedReader(new InputStreamReader(
				posturl.openStream()));

		// 返回发送结果
		String str = "";
		while((str=in.readLine())!=null){
		// 返回结果为XML格式文档
			result.append(str);
		}
		return result.toString();
	}
	
	/**
	 * 处理返回结果
	 * @param result
	 * @return
	 */
	public static Map<String,Object> readXMLString4TagName(String xmlStr)
			throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		DocumentBuilder builder = dbf.newDocumentBuilder();

		InputStream inputStream = new ByteArrayInputStream(xmlStr.getBytes());

		Document doc = builder.parse(inputStream); //

		Element root = doc.getDocumentElement(); // 获取根元素
		NodeList nodes = root.getChildNodes(); 
		for(int i = 0;i<nodes.getLength();i++){
			if(nodes.item(i).getNodeType()==Node.ELEMENT_NODE){
			Element e = (Element) nodes.item(i);	
			map.put(e.getNodeName(), null==e.getFirstChild()?"":e.getFirstChild().getNodeValue());
			}
			
		}	
		return map;
	}
	
	public static void main(String[] args) throws Exception {
		String userId = "8805";
		 String account = "ggyyx";
		 String password = "123456qwe";
		String url = "http://inter.ueswt.com/sms.aspx";
		YYShortMessageServiceImpl ms = new YYShortMessageServiceImpl();
//		System.out.println(ms.getSmsBalance(userId, account, password, url));  
	}
	
}
