package com.wuyizhiye.cmct.sms.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.wuyizhiye.base.dao.QueryExecutor;
import com.wuyizhiye.base.spring.ApplicationContextAware;
import com.wuyizhiye.cmct.sms.enums.SMSTypeEnum;
import com.wuyizhiye.cmct.sms.model.SMSConfig;

/**
 * 麦讯通简易短信发送服务，主要用于验证码发送和，简易短信发送
 * @author li.biao
 * @since 2015-9-9 11:26:24
 */
public class MxtSmsUtil {
	
	//默认账户信息
	private static String userId = "968236";
	private static String account = "damaichang";
	private static String password = "51zhiye@";
	
	static{
		QueryExecutor queryExecutor = (QueryExecutor) ApplicationContextAware.getApplicationContext().getBean("queryExecutor");
		//根据 控制类型 和 短信类型 取短信接口配置信息
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("type",SMSTypeEnum.BUSINESS_TYPE);
		param.put("enableFlag",1);
		SMSConfig  smsConfig = queryExecutor.execOneEntity("com.wuyizhiye.cmct.sms.dao.SMSConfigDao.selectConfigOne",param, SMSConfig.class);
		if(smsConfig!=null){
			userId = smsConfig.getGroupId();
			account = smsConfig.getAccountName();
			password = smsConfig.getPassword();
		}
	}

	private static String readXMLString4TagName(String xmlStr, String tagName)
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

	private static class UTF8PostMethod extends PostMethod {
		public UTF8PostMethod(String url) {
			super(url);
		}
		@Override
		public String getRequestCharSet() {
			return "UTF-8";
		}
	}

	private static String directSend(String receivers, String content, Date sendTime) {
		// String response = "";
		String url = "http://61.143.63.169:8080/GateWay/Services.asmx/DirectSend?";
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setBooleanParameter(
				"http.protocol.expect-continue", false);
		// String responseValue;
		PostMethod getMethod = new UTF8PostMethod(url);
		String time = "";
		if (null != sendTime) {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			time = df.format(sendTime);
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
			if (str.contains("GBK")) {
				str = str.replaceAll("GBK", "UTF-8");
			}
			String retCodeValue = "";
			try {
				retCodeValue = readXMLString4TagName(str, "RetCode");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return retCodeValue;
			// int beginPoint = str.indexOf("<RetCode>");
			// int endPoint = str.indexOf("</RetCode>");
			// String result = "RetCode=";
			// return result + str.substring(beginPoint + 9, endPoint);
			// return getMethod.getResponseBodyAsString();
		} catch (HttpException e) {
			return "1";
		} catch (IOException e) {
			return "2";
		} finally {
			getMethod.releaseConnection();
		}
	}

	public static Map<String, String> sendMsg(String receivers, String content,
			Date sendTime) {
		String result = "";
		int codeType = 0;// 1为群发，0为单发
		if (codeType == 1) {
			result = directSend(receivers.replace(",", ";"), content, sendTime);
		} else {
			String s = "";
			for (String phone : receivers.split(",")) {
				result = directSend(phone, content, sendTime);
				if ("Sucess".equals(result)) {
					s = result;
				}
			}
			if (!"".equals(s)) {
				s = "Sucess";
			}
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("result", "Sucess".equals(result) ? "发送成功" : "发送失败");
//		System.out.println("account:" + account);
		return map;
	}
}
