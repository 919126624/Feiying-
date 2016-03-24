package com.wuyizhiye.cmct.sms.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.cmct.sms.model.ShortMessage;
import com.wuyizhiye.cmct.sms.service.ShortMessageService;

/**
 * @ClassName MxtShortMessageServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="mxtShortMessageService")
public class MxtShortMessageServiceImpl extends BaseServiceImpl<ShortMessage> implements ShortMessageService  {

	/*private static final String userId = "961636";
	private static final String account = "aidaibang";
	private static final String password = "adb_123";	*/
	public String readXMLString4TagName(String xmlStr, String tagName)
			throws Exception {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		DocumentBuilder builder = dbf.newDocumentBuilder();

		InputStream inputStream = new ByteArrayInputStream(xmlStr.getBytes());

		Document doc = builder.parse(inputStream); //

		// 下面开始读取

		Element root = doc.getDocumentElement(); // 获取根元素

		NodeList nodes = root.getElementsByTagName(tagName);
		Element e = (Element) nodes.item(0);

		Node t = e.getFirstChild();

		return t.getNodeValue();
	}

	public static class UTF8PostMethod extends PostMethod {
		public UTF8PostMethod(String url) {
			super(url);
		}

		@Override
		public String getRequestCharSet() {
			// return Charset.defaultCharset().name();
			// return super.getRequestCharSet();
			return "UTF-8";
		}
	}

	public Map<String, Object> directSend(String userId ,String account,String password,String url,String receivers, String content, Date sendTime) {
		// String response = "";
		//String url = "http://61.143.63.169:8080/GateWay/Services.asmx/DirectSend?";
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setBooleanParameter(
				"http.protocol.expect-continue", false);
		// String responseValue;
		PostMethod getMethod = new UTF8PostMethod(url);
		String time = "";
		if(null!=sendTime){
			time = DateUtil.convertDateToStr(sendTime,"yyyy-MM-dd HH:mm:ss");
		} 
		
		NameValuePair[] data = { new NameValuePair("UserID", userId),
				new NameValuePair("Account", account),
				new NameValuePair("Password", password),
				new NameValuePair("Phones", receivers),
				new NameValuePair("SendType", "1"),
				new NameValuePair("SendTime", time),
				new NameValuePair("PostFixNumber", ""),
				new NameValuePair("Content", content) };
		getMethod.setRequestBody(data);
		getMethod.addRequestHeader("Connection", "close");
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: "
						+ getMethod.getStatusLine());
			}
			byte[] responseBody = getMethod.getResponseBody();
			String str = new String(responseBody, "UTF-8");
			/*if (str.contains("UTF-8")) {
				str = str.replaceAll("GBK", "utf-8");
			}*/
			String retCodeValue = "";
			String taskId = "";
			String message = "";
			try {
				retCodeValue = readXMLString4TagName(str, "RetCode");
				taskId = readXMLString4TagName(str, "JobID");
				if("Faild".equals(retCodeValue)){
				 message = readXMLString4TagName(str, "Message");
				 resultMap.put("message", message);
				}
				resultMap.put("retCodeValue", retCodeValue);
				resultMap.put("taskId", taskId);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return resultMap;
			 
		} catch (Exception e) {
			resultMap.put("retCodeValue", "Faild");
			resultMap.put("message", "调用接口异常");
		} finally {
			getMethod.releaseConnection();
		}
		return resultMap;
	}
	// 调用接口DirectGetStockDetails
	public String directGetStockDetails(String url) {
		return excute(url);
	}
	// 调用接口DirectSend,没有参数为中文的url时可调用如下方法.
	public String excute(String url) {
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod(url);
		try {
			getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
					new DefaultHttpMethodRetryHandler());
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: "
						+ getMethod.getStatusLine());
			}
			byte[] responseBody = getMethod.getResponseBody();
			String str = new String(responseBody, "GBK");
			if (str.contains("GBK")) {
				str = str.replaceAll("GBK", "utf-8");
			}
			int beginPoint = str.indexOf("<StockRemain>");
			int endPoint = str.indexOf("</StockRemain>");
			return str.substring(beginPoint+13, endPoint);
		} catch (HttpException e) {
			return "1";
		} catch (IOException e) {
			return "2";
		}

		finally {
			getMethod.releaseConnection();
		}
	}
	 
	/*public Map<String, String> sendMsg(String receivers, String content,
			Date sendTime) {
		String result = "";
		int codeType = 0;//1为群发，0为单发
		if(codeType==1){
			result = directSend(receivers.replace(",", ";"), content, sendTime);
		}else{
			String s = "";
			for(String phone : receivers.split(",")){
				result = directSend(phone, content, sendTime);
				if("Sucess".equals(result)){
					s = result;
				}
			}
			if(!"".equals(s)){
				s = "Sucess";
			}
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("result", "Sucess".equals(result)?"发送成功":"发送失败");
		return map;
	}*/

	 
	public String getOverage(String userId ,String account,String password,String url) {
		String result = directGetStockDetails("http://61.143.63.169:8080/GateWay/Services.asmx/DirectGetStockDetails?UserID="
				+ userId
				+ "&Account="
				+ account
				+ "&Password=" + password);
		return result;
	}
	
	@Override
	public Map<String, Object> sendMessage(String groupId, String accountName,
			String password, String url, String from, String to,
			String content, Date sendTime) throws Exception {
		Map<String,Object> map=new HashMap<String, Object>();
		 
		if(StringUtils.isEmpty(to)){
			map.put("state", "接收号码不能为空");
			return map;
		}
		if(StringUtils.isEmpty(content)){
			map.put("state", "短信内容不能为空");
			return map;
		}
		 
		String result = "";
		int codeType = 1;//1为群发，0为单发
		//if(codeType==1){
			map = directSend(groupId,accountName,password,url,to.replace(",", ";"), content, sendTime);
		/*}else{
			String s = "";
			for(String phone : to.split(",")){
				map = directSend(groupId,accountName,password,url,phone, content, sendTime);
				if("Sucess".equals(map.get("retCodeValue"))){
					s = result;
				}
			}
			 
		}*/ 
	 
		map.put("state", ("Sucess".equals(map.get("retCodeValue"))?"ok":map.get("message")));
		return map;
	}
	@Override
	public String getSmsBalance(String groupId, String accountName,
			String password, String url) throws Exception {
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setBooleanParameter(
				"http.protocol.expect-continue", false);
		PostMethod getMethod = new UTF8PostMethod(url);
		//http://www.mxtong.net.cn:8080/GateWay/Services.asmx/DirectFetchSMS?UserID=899332&Account=admin&Password=333222
		NameValuePair[] data = { new NameValuePair("UserID", groupId),
				new NameValuePair("Account", accountName),
				new NameValuePair("Password", password)};
		getMethod.setRequestBody(data);
		getMethod.addRequestHeader("Connection", "close");
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: "
						+ getMethod.getStatusLine());
			}
			byte[] responseBody = getMethod.getResponseBody();
			String str = new String(responseBody, "UTF-8");
			 
			try {
				 
				//创建一个新的字符串
				StringReader read = new StringReader(str);
				//创建新的输入源 DocumentBuilder 解析器将使用 InputSource 对象来确定如何读取 XML 输入
				InputSource source = new InputSource(read);
				//创建Document解析工厂
				DocumentBuilderFactory dbf =DocumentBuilderFactory.newInstance() ;
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(source);
				
				//读取Document元素
				Element el = doc.getDocumentElement() ;
				String retCodeValue = readXMLString4TagName(str, "RetCode");
				if("Sucess".equals(retCodeValue)){
					return  readXMLString4TagName(str, "StockRemain");
				} 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
			 
		} catch (HttpException e) {
			 
		} catch (IOException e) {
			 
		} finally {
			getMethod.releaseConnection();
		}
		return null;
	}
	@Override
	public List<Map<String, Object>> getReplyData(String groupId,
			String accountName, String password, String url) throws Exception {
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setBooleanParameter(
				"http.protocol.expect-continue", false);
		List<Map<String, Object>>  resultList = new ArrayList<Map<String,Object>>();
		PostMethod getMethod = new UTF8PostMethod(url);
		//http://www.mxtong.net.cn:8080/GateWay/Services.asmx/DirectFetchSMS?UserID=899332&Account=admin&Password=333222
		NameValuePair[] data = { new NameValuePair("UserID", groupId),
				new NameValuePair("Account", accountName),
				new NameValuePair("Password", password)};
		getMethod.setRequestBody(data);
		getMethod.addRequestHeader("Connection", "close");
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: "
						+ getMethod.getStatusLine());
			}
			byte[] responseBody = getMethod.getResponseBody();
			String str = new String(responseBody, "UTF-8");
			 
			try {
				 
				//创建一个新的字符串
				StringReader read = new StringReader(str);
				//创建新的输入源 DocumentBuilder 解析器将使用 InputSource 对象来确定如何读取 XML 输入
				InputSource source = new InputSource(read);
				//创建Document解析工厂
				DocumentBuilderFactory dbf =DocumentBuilderFactory.newInstance() ;
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(source);
				
				//读取Document元素
				Element el = doc.getDocumentElement() ;
				String retCodeValue = readXMLString4TagName(str, "RetCode");
				if("Sucess".equals(retCodeValue)){
					NodeList nodeList = el.getElementsByTagName("SMSGroup");
					 //<SMSGroup Phone="string" RecDateTime="string" PostFixNumber="string" />
					if(nodeList!=null && nodeList.getLength()>0){
					  for(int i=0;i<nodeList.getLength();i++ ){	
						Map<String, Object> replyData = new HashMap<String, Object>();
						Node replyNode = nodeList.item(i);
						replyData.put("content", replyNode.getTextContent());
						NamedNodeMap nmap = replyNode.getAttributes();
						replyData.put("mobile", nmap.getNamedItem("Phone").getTextContent());
						replyData.put("taskid", nmap.getNamedItem("PostFixNumber").getTextContent());
						replyData.put("receivetime", nmap.getNamedItem("RecDateTime").getTextContent());
						resultList.add(replyData);
					  }
	 		        }
				} 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return resultList;
			 
		} catch (HttpException e) {
			 
		} catch (IOException e) {
			 
		} finally {
			getMethod.releaseConnection();
		}
		return resultList;
	}
	@Override
	public List<Map<String, Object>> getMessageStatus(String groupId,
			String accountName, String password, String url) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	protected BaseDao getDao() {
		 
		return null;
	}

	public static void main(String[] args) {
		 String userId = "961636";
		 String account = "dingjian";
		 String password = "djsoft";
		String url = "http://61.143.63.169:8080/GateWay/Services.asmx/DirectSend";
		MxtShortMessageServiceImpl ms = new MxtShortMessageServiceImpl();
		try {
		 //System.out.println(ms.sendMessage(userId,account,password,url,null,"15219481921","用系统发不了短信呢【深圳大学】",null));	
		// System.out.println(ms.getReplyData(userId, account, password, "http://www.mxtong.net.cn:8080/GateWay/Services.asmx/DirectFetchSMS?"));
//			System.out.println(ms.getSmsBalance(userId, account, password, "http://www.mxtong.net.cn:8080/Services.asmx/DirectGetStockDetails"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
