package com.wuyizhiye.cmct.sms.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.cmct.sms.dao.ShortMessageDao;
import com.wuyizhiye.cmct.sms.model.ShortMessage;
import com.wuyizhiye.cmct.sms.service.ShortMessageService;

/**
 * @ClassName ZcyShortMessageNewServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="zcyShortMsgNewService")
public class ZcyShortMessageNewServiceImpl extends BaseServiceImpl<ShortMessage> implements ShortMessageService {
	/**
	 * 测试账户
	 */
 
	//短信长度单位，超过70个字多发一条
	private static final int msg_unit = 70 ;
//	@Resource(name="cmctShortMessageDao", type=ShortMessageDao.class)
	@Autowired
	private ShortMessageDao shortMessageDao;
	
	@Override
	protected BaseDao getDao() {
		return shortMessageDao;
	}

	/**
	 * 发送post请求
	 * @param url
	 * @param params
	 * @return
	 */
	public static String httpPost(String url, Map<String, String> params) {

		URL u = null;
		HttpURLConnection con = null;
		//构建请求参数
		StringBuffer sb = new StringBuffer();
	
		if(params!=null){
	
			for (Entry<String, String> e : params.entrySet()) {
				sb.append(e.getKey());
				sb.append("=");
				sb.append(e.getValue());
				sb.append("&");
			}
			sb.substring(0, sb.length() - 1);
		}
	
//		System.out.println("send_url:"+url);
//		System.out.println("send_data:"+sb.toString());
	
		//尝试发送请求
		try {
			u = new URL(url);
			con = (HttpURLConnection) u.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setUseCaches(false);
			//setConnectTimeout：设置连接主机超时（单位：毫秒） 
			//setReadTimeout：设置从主机读取数据超时（单位：毫秒） 
			con.setConnectTimeout(30000);//默认30秒
			con.setReadTimeout(30000);//默认30秒
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
			osw.write(sb.toString());
			osw.flush();
			osw.close();
	
		} catch (Exception e) {
		  e.printStackTrace();
		} finally {
	
			if (con != null) {
			  con.disconnect();
			}
		}
	
		//读取返回内容
		StringBuffer buffer = new StringBuffer();
		try {
	
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			String temp;
			while ((temp = br.readLine()) != null) {
		
			  buffer.append(temp);
			  buffer.append("\n");
			}
		}catch (Exception e) {
	
		  e.printStackTrace();
		}
	
		return buffer.toString();
	}
	
	/**
	 * 查看短信回复
	 */
	@Override
	public List<Map<String, Object>> getReplyData(String groupId,String accountName, String password, String url) throws Exception {
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		Map<String,String> params = new HashMap<String,String>();
		params.put("action", "query");//任务名称
		params.put("userid", groupId);
		params.put("account", accountName);
		params.put("password", password);
		String result = httpPost(url,params);
		list = getReplyCodeList(result);
		return list;
	}
	
	/**
	 * 获取短信发送状态
	 */
	@Override
	public List<Map<String, Object>> getMessageStatus(String groupId,
			String accountName, String password, String url) throws Exception {
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		Map<String,String> params = new HashMap<String,String>();
		params.put("action", "query");//任务名称
		params.put("userid", groupId);
		params.put("account", accountName);
		params.put("password", password);
		String result = httpPost(url,params);
		list = getStatusCodeList(result);
		return list;
	}
	
	/**
	 * 发送短信
	 * @param groupId 企业ID
	 * @param username 账号
	 * @param password 密码
	 * @param from 发送号码
	 * @param to   接收号码  (多个号码之间用半角逗号隔开) 
	 * @param content 短信内容
	 * @return
	 * @throws Exception
	 */
	public  Map<String,Object> sendMessage(String groupId,String accountName,String password,String url,String from,String to ,String content ,Date sendTime)throws Exception{
		Map<String,Object> map=new HashMap<String, Object>();
		String result = "" ;
		/*String sendContent = "";
		String sendContentTemp = content;*/
		if(StringUtils.isEmpty(to)){
			map.put("state", "接收号码不能为空");
			return map;
		}
		Map<String,String> params = new HashMap<String,String>();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timeStr =null;
		if(sendTime!=null){
			timeStr = df.format(sendTime) ;
			params.put("sendTime", timeStr);
		}
		
		if(StringUtils.isEmpty(groupId)){
			map.put("state", "企业ID未配置");
			return map;
		}
		String productid = groupId.split(":")[0];
		if(groupId.split(":").length==2){
			String extno = groupId.split(":")[1];
			params.put("extno", extno);
		}
		//action=send&userid=12&account=账号&password=密码&mobile=15023239810&content=内容&sendTime=&extno=
		params.put("action", "send");
		params.put("userid", productid);
		params.put("account", accountName);
		params.put("password", password);
		params.put("mobile", to);
		 
		/*while(!sendContent.equals(sendContentTemp)){
			//每条短信最多发70个汉字
			if(sendContentTemp.length()<=msg_unit){
				sendContent = sendContentTemp;
			}else{
				sendContent = sendContentTemp.substring(0, msg_unit);
				sendContentTemp = sendContentTemp.substring(msg_unit);
			}
			params.put("content", sendContent);
			result = httpPost(url,params);
			result = getCodeStatus(result);
			if(!"ok".equals(result)){
				break;
			}
		}*/
		params.put("content", content);
		result = httpPost(url,params);
		map = getCodeStatus(result);
		return map;
	}
	
	/**
	 * 根据xml文本获取请求状态(取回复信息)
	 * @param result
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String,Object>> getReplyCodeList(String xmlDoc)
			throws Exception {
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		
		//创建一个新的字符串
		StringReader read = new StringReader(xmlDoc);
		
		//创建新的输入源 DocumentBuilder 解析器将使用 InputSource 对象来确定如何读取 XML 输入
		InputSource source = new InputSource(read);
		
		//创建Document解析工厂
		DocumentBuilderFactory dbf =DocumentBuilderFactory.newInstance() ;
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(source);
		
		//读取Document元素
		Element el = doc.getDocumentElement() ;
		NodeList nodeList = el.getElementsByTagName("callbox");
		if(nodeList!=null && nodeList.getLength()>0){
            for(int i=0;i<nodeList.getLength();i++){
            	Node code = nodeList.item(i);
            	Map<String,Object> map=new HashMap<String, Object>();
    			NodeList childList=code.getChildNodes();
    			for(int j=0;j<childList.getLength();j++){
    				Node node=childList.item(j);
    				if("mobile".equals(node.getNodeName()) || "taskid".equals(node.getNodeName())
    						|| "receivetime".equals(node.getNodeName())|| "content".equals(node.getNodeName())){
    					map.put(node.getNodeName(), node.getFirstChild().getNodeValue());
    				}
    			}
    			list.add(map);
            }
		}
		return list;
	}
	
	/**
	 * 根据xml文本获取请求状态(取回复信息)
	 * @param result
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String,Object>> getStatusCodeList(String xmlDoc)
			throws Exception {
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		
		//创建一个新的字符串
		StringReader read = new StringReader(xmlDoc);
		
		//创建新的输入源 DocumentBuilder 解析器将使用 InputSource 对象来确定如何读取 XML 输入
		InputSource source = new InputSource(read);
		
		//创建Document解析工厂
		DocumentBuilderFactory dbf =DocumentBuilderFactory.newInstance() ;
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(source);
		
		//读取Document元素
		Element el = doc.getDocumentElement() ;
		NodeList nodeList = el.getElementsByTagName("statusbox");
		if(nodeList!=null && nodeList.getLength()>0){
            for(int i=0;i<nodeList.getLength();i++){
            	Node code = nodeList.item(i);
            	Map<String,Object> map=new HashMap<String, Object>();
    			NodeList childList=code.getChildNodes();
    			for(int j=0;j<childList.getLength();j++){
    				Node node=childList.item(j);
    				if("mobile".equals(node.getNodeName()) || "taskid".equals(node.getNodeName())
    						|| "receivetime".equals(node.getNodeName())|| "status".equals(node.getNodeName()) || "errorcode".equals(node.getNodeName())){
    					map.put(node.getNodeName(), node.getFirstChild().getNodeValue());
    				}
    			}
    			list.add(map);
            }
		}
		return list;
	}
	
	
	
	/**
	 * 根据xml文本获取请求状态
	 * @param result
	 * @return
	 * @throws Exception
	 */
	public static Map<String,Object> getCodeStatus(String xmlDoc)
			throws Exception {
		Map<String,Object> result=new HashMap<String, Object>();
		String codeContent = "" ;
		
		//创建一个新的字符串
		StringReader read = new StringReader(xmlDoc);
		
		//创建新的输入源 DocumentBuilder 解析器将使用 InputSource 对象来确定如何读取 XML 输入
		InputSource source = new InputSource(read);
		
		//创建Document解析工厂
		DocumentBuilderFactory dbf =DocumentBuilderFactory.newInstance() ;
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(source);
		
		//读取Document元素
		Element el = doc.getDocumentElement() ;
		NodeList nodeList = el.getElementsByTagName("message");
		NodeList task=el.getElementsByTagName("taskID");//任务Id
		if(nodeList!=null && nodeList.getLength()>0){
			Node code = nodeList.item(0);
			codeContent = code.getTextContent();
			if(StringUtils.isNotNull(codeContent) &&
					(codeContent.indexOf("大于您当前余额")>=0 || codeContent.indexOf("余额不足")>=0)){
				codeContent =  "公司总账号余额不足，请及时充值！";
			}
			result.put("state", codeContent);
		}
		if(task!=null && task.getLength()>0){
			Node code = task.item(0);
			String taskId = code.getTextContent();
			result.put("taskId", taskId);
		}
		return result;
	}
	
	public static String getCodeStatus4Balance(String xmlDoc)throws Exception {

	String codeContent = "" ;
	
	//创建一个新的字符串
	StringReader read = new StringReader(xmlDoc);
	
	//创建新的输入源 DocumentBuilder 解析器将使用 InputSource 对象来确定如何读取 XML 输入
	InputSource source = new InputSource(read);
	
	//创建Document解析工厂
	DocumentBuilderFactory dbf =DocumentBuilderFactory.newInstance() ;
	DocumentBuilder db = dbf.newDocumentBuilder();
	Document doc = db.parse(source);
	
	//读取Document元素
	Element el = doc.getDocumentElement() ;
	NodeList nodeList = el.getElementsByTagName("message");
	if(nodeList!=null && nodeList.getLength()>0){
		Node code = nodeList.item(0);
		codeContent = code.getTextContent();
	}
	if(StringUtils.isEmpty(codeContent)){
		//如果返回成功  返回余额信息
		nodeList = el.getElementsByTagName("overage");
		if(nodeList!=null && nodeList.getLength()>0){
			Node code = nodeList.item(0);
			codeContent = code.getTextContent();
		}
	}
	return codeContent;
	}
	
	/**
	 * 查询短信账户余额
	 * @param groupId 企业ID
	 * @param username 账号
	 * @param password 密码
	 * @return
	 * @throws Exception
	 */
	public  String getSmsBalance(String groupId,String accountName,String password,String url)throws Exception{
		String result = "" ;
		Map<String,String> params = new HashMap<String,String>();
		//String url = "http://121.199.40.163:9001/sms.aspx";
		
		params.put("action", "overage");
		params.put("userid", groupId);
		params.put("account", accountName);
		params.put("password", password);
		result = httpPost(url,params);
		result = getCodeStatus4Balance(result);
		return result ;
	}

	public static void main(String[] args) throws Exception {
		
		
		String abc="<?xml version='1.0' encoding='utf-8' ?><returnsms>"+
		 "<callbox>"+
		 "<mobile>13714260213</mobile>"+
		 "<taskid>355531</taskid>"+
		 "<content>更好很久慷慨解囊不仅仅姐夫看看放假</content>"+
		 "<receivetime>2013-9-17 17:38:08</receivetime>"+
		 "<extno></extno>"+
		 "</callbox>"+
		 "</returnsms>";
		List<Map<String,Object>> list=getReplyCodeList(abc);
		for(Map<String,Object> map:list){
			Set<String> keys = map.keySet();
			for(String key : keys){
//				System.out.println(key+"================="+map.get(key));
			}
		}
		
		
	}

}
