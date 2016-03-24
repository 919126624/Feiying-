package com.wuyizhiye.cmct.phone.util;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.tree.DefaultText;

import Decoder.BASE64Encoder;

import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.cmct.phone.constant.FjCtCmctConstant;
import com.wuyizhiye.cmct.phone.model.PhoneMarket;
import com.wuyizhiye.cmct.phone.model.PhoneMarketBindPerson;
import com.wuyizhiye.cmct.phone.model.PhoneMember;

/**
 * @ClassName FjCtCmctTool
 * @Description 福建电信话务成员工具类
 * @author li.biao
 * @date 2015-5-26
 */
public class FjCtCmctTool {
	
	/**
	 * 构建请求参数
	 * @param ppm
	 * @param type 方法名
	 * @param iptStr 输入参数
	 * @return
	 */
	public static String buildRequestParam(PhoneMember pm , String method,String iptStr){
		String url = "" ;
		if("dial".equals(method)){
			url = buildDialUrl(pm);
		}else if("dialStop".equals(method)){
			url = buildDialStopUrl(pm);
		}else if("hcodeSearch".equals(method)){
			url = buildHcodeSearchUrl(iptStr);
		}else if("imsBill".equals(method)){
			url = buildTelecomDetail(pm);
		}else if("ImsAccount".equals(method)){
			url = buildImsAccount(pm);
		}
		return url ;
	}
	
	public static String buildMarketRequestParam(PhoneMarket pm, String method){
		String url = "" ;
		if("Marketing1".equals(method)){
			url = buildMarketing1(pm);
		}else if("MARKET_STOP".equals(method)){
			url = buildMarketStop(pm);
		}else if("MARKET_BEGIN".equals(method)){
			url = buildMarketBegin(pm);
		}else if("MARKET_CLOSE".equals(method)){
			url = buildMarketClose(pm);
		}
		return url ;
	}
	
	/** 构建拨号的请求参数
	 * @param ppm
	 * @return
	 */
	private static String buildDialUrl(PhoneMember ppm){
		StringBuilder url =  new StringBuilder("");
		String currTimestamp = getTimestampNow() ;
		url.append("<Request>");
			url.append("<Head>");
				url.append("<MethodName>").append(FjCtCmctConstant.Method.Dial).append("</MethodName>");
				url.append("<Spid>").append(ppm.getSpid()).append("</Spid>");
				url.append("<Appid>").append(ppm.getOrgInterfaceId()).append("</Appid>");
				url.append("<Passwd>").append(sha1Encrypt(ppm.getPassWd())).append("</Passwd>");
				url.append("<Timestamp>").append(currTimestamp).append("</Timestamp>");
				url.append("<Authenticator>").append(sha1Encrypt(currTimestamp+FjCtCmctConstant.Method.Dial+ppm.getSpid()+ppm.getPassWd())).append("</Authenticator>");
			url.append("</Head>");
			url.append("<Body>");
				url.append("<ChargeNbr>").append(ppm.getUserId()).append("</ChargeNbr>");
				url.append("<Key>").append(ppm.getPassword()).append("</Key>");
				url.append("<DisplayNbr>").append(StringUtils.isEmpty(ppm.getDefaultShowPhone())?ppm.getUserId():ppm.getDefaultShowPhone().equals(ppm.getShowPhone())?ppm.getUserId():ppm.getDefaultShowPhone()).append("</DisplayNbr>");
				url.append("<CallerNbr>").append(ppm.getShowPhone()).append("</CallerNbr>");
				url.append("<CalleeNbr>").append(ppm.getAnswerPhone()).append("</CalleeNbr>");
				//url.append("<WaitTime>").append("5").append("</WaitTime>");//新版拨号加入此参数,该参数是表示呼叫主叫后要等待多久再呼叫被叫。单位是秒
				//url.append("<Record>").append(ParamUtils.getParamValue(PhoneMemberUtil.CMCT_PHONERECORD)).append("</Record>");//默认先录音
				url.append("<AnswerOnMedia>").append("1").append("</AnswerOnMedia>");
				url.append("<DisplayNbrMode>").append(StringUtils.isEmpty(ppm.getDefaultShowPhone())?"1":ppm.getDefaultShowPhone().equals(ppm.getShowPhone())?"1":"0").append("</DisplayNbrMode>");
			url.append("</Body>");
		url.append("</Request>");
		return url.toString() ;
	}
	
	/**
	 * 构建拨号中断的请求参数
	 * @param ppm
	 * @return
	 */
	private static String buildDialStopUrl(PhoneMember pm){
		StringBuilder url =  new StringBuilder("");
		String currTimestamp = getTimestampNow() ;
		url.append("<Request>");
			url.append("<Head>");
				url.append("<MethodName>").append(FjCtCmctConstant.Method.DialStop).append("</MethodName>");
				url.append("<Spid>").append(pm.getSpid()).append("</Spid>");
				url.append("<Appid>").append(pm.getOrgInterfaceId()).append("</Appid>");
				url.append("<Passwd>").append(sha1Encrypt(pm.getPassWd())).append("</Passwd>");
				url.append("<Timestamp>").append(currTimestamp).append("</Timestamp>");
				url.append("<Authenticator>").append(sha1Encrypt(currTimestamp+FjCtCmctConstant.Method.DialStop+pm.getSpid()+pm.getPassWd())).append("</Authenticator>");
			url.append("</Head>");
			url.append("<Body>");
				url.append("<Sessionid>").append(pm.getSessionId()).append("</Sessionid>");//sessionid要传拨号时的记录
			url.append("</Body>");
		url.append("</Request>");
		return url.toString() ;
	}
	
	/**
	 * 构建请求号码归属地查询
	 * @param ppm
	 * @return
	 */
	private static String buildHcodeSearchUrl(String phone){
		StringBuilder url =  new StringBuilder("");
		String currTimestamp = getTimestampNow() ;
		url.append("<Request>");
			url.append("<Head>");
				url.append("<MethodName>").append(FjCtCmctConstant.Method.HcodeSearch).append("</MethodName>");
				url.append("<Spid>").append(FjCtCmctConstant.SPID).append("</Spid>");
				url.append("<Passwd>").append(sha1Encrypt(FjCtCmctConstant.SP_PWD)).append("</Passwd>");
				url.append("<Timestamp>").append(currTimestamp).append("</Timestamp>");
				url.append("<Authenticator>").append(sha1Encrypt(currTimestamp+FjCtCmctConstant.Method.HcodeSearch+FjCtCmctConstant.SPID+FjCtCmctConstant.SP_PWD)).append("</Authenticator>");
			url.append("</Head>");
			url.append("<Body>");
				url.append("<Mobile>").append(phone).append("</Mobile>");
			url.append("</Body>");
		url.append("</Request>");
		return url.toString() ;
	}
	
	/**
	 * 构建话务余额请求参数
	 * 
	 * @param pm
	 * @return
	 */
	public static String buildImsAccount(PhoneMember pm) {
		StringBuilder url = new StringBuilder("");
		String currTimestamp = getTimestampNow();
		url.append("<Request>");
		url.append("<Head>");
		url.append("<MethodName>").append(FjCtCmctConstant.Method.IMS_ACCOUNT).append("</MethodName>");
		url.append("<Spid>").append(pm.getSpid()).append("</Spid>");
		url.append("<Appid>").append(pm.getOrgInterfaceId()).append("</Appid>");
		url.append("<Passwd>").append(sha1Encrypt(pm.getPassWd())).append("</Passwd>");
		url.append("<Timestamp>").append(currTimestamp).append("</Timestamp>");
		url.append("<Authenticator>").append(sha1Encrypt(currTimestamp + FjCtCmctConstant.Method.IMS_ACCOUNT + pm.getSpid()+pm.getOrgKey()))
				.append("</Authenticator>");
		url.append("</Head>");
		url.append("<Body>");
		url.append("<Ims>").append(pm.getUserId()).append("</Ims>");
		url.append("<Key>").append(pm.getPassword()).append("</Key>");
		url.append("<Type>").append("1").append("</Type>");//类型 1：套餐类，2：IT下账类
		url.append("</Body>");
		url.append("</Request>");

		return url.toString();
	}
	
	/**
	 * 构建营销请求参数
	 * 
	 * @param pm
	 * @return
	 */
	public static String buildMarketing1(PhoneMarket phoneMarket) {
		PhoneMarketBindPerson pbp=phoneMarket.getPhoneMbp();
		StringBuilder url = new StringBuilder("");
		String currTimestamp = getTimestampNow();
		url.append("<Request>");
		url.append("<Head>");
		url.append("<MethodName>").append(FjCtCmctConstant.Method.Marketing2).append("</MethodName>");
		url.append("<Spid>").append(pbp.getSpid()).append("</Spid>");
		url.append("<Appid>").append(pbp.getAppid()).append("</Appid>");
		url.append("<Passwd>").append(sha1Encrypt(pbp.getAppKey())).append("</Passwd>");
		url.append("<Timestamp>").append(currTimestamp).append("</Timestamp>");
		url.append("<Authenticator>").append(sha1Encrypt(currTimestamp + FjCtCmctConstant.Method.Marketing1 + pbp.getSpid()+pbp.getAppKey()))
				.append("</Authenticator>");
		url.append("</Head>");
		url.append("<Body>");
		url.append("<DisplayNbr>").append(pbp.getMarketNumber()).append("</DisplayNbr>");
		url.append("<Key>").append(pbp.getMarketPassWord()).append("</Key>");
		url.append("<ChargeNbr>").append(pbp.getMarketNumber()).append("</ChargeNbr>");//来电显示号码（默认为计费号码，如果想用其他号码做为来电显示号码，必须先调用'主显号码申请'接口，一个计费号码可以对应多个主显号码）
		url.append("<CalleeNbr>").append(phoneMarket.getCalleeNbr()).append("</CalleeNbr>");//被叫号码。最多可以5000个被叫号码。
		url.append("<CallMode>").append(phoneMarket.getCallMode()).append("</CallMode>");//输入字符“1”，“2”或“3”来判断营销类型。1：自定义按键流程 2：自定义转人工流程 3：普通流程(默认普通流程)
		if(!StringUtils.isEmpty(phoneMarket.getWaitTime())){		
			url.append("<WaitTime>").append(phoneMarket.getWaitTime()).append("</WaitTime>");//转人工的时间间隔，如果不设置的话，默认是在听完语音之后再转人工，如果设置的话就按设置的时间点来转人工（当CallMode值为2时传递）
		}
		if(!StringUtils.isEmpty(phoneMarket.getTransferUrl())){
			url.append("<TransferUrl>").append(FjCtCmctTool.urlEncoder(phoneMarket.getTransferUrl())).append("</TransferUrl>");
		}
		if(!StringUtils.isEmpty(phoneMarket.getIsRecord())){
			url.append("<IsRecord>").append(phoneMarket.getIsRecord()).append("</IsRecord>");
		}
		if(!StringUtils.isEmpty(phoneMarket.getAmBeginTime())){			
			url.append("<AmBeginTime>").append(phoneMarket.getAmBeginTime()).append("</AmBeginTime>");
		}
		if(!StringUtils.isEmpty(phoneMarket.getAmEndTime())){
			url.append("<AmEndTime>").append(phoneMarket.getAmEndTime()).append("</AmEndTime>");
		}
		if(!StringUtils.isEmpty(phoneMarket.getPmBeginTime())){
			url.append("<PmBeginTime>").append(phoneMarket.getPmBeginTime()).append("</PmBeginTime>");
		}
		if(!StringUtils.isEmpty(phoneMarket.getPmEndTime())){
			url.append("<PmEndTime>").append(phoneMarket.getPmEndTime()).append("</PmEndTime>");
		}
		if(!StringUtils.isEmpty(phoneMarket.getVoiceName())){
			url.append("<VoiceName>").append(phoneMarket.getVoiceName()).append("</VoiceName>");//语音文件名称
		}
		if(!StringUtils.isEmpty(phoneMarket.gettTSContent())){
			url.append("<TTSContent>").append(phoneMarket.gettTSContent()).append("</TTSContent>");
		}
		url.append("<IsSend>").append("1").append("</IsSend>");//是否直接发送 1 不直接发送 2 直接发送 （默认：1）
		if(!StringUtils.isEmpty(phoneMarket.getEffectDate())){		
			url.append("<EffectDate>").append(phoneMarket.getEffectDate()).append("</EffectDate>");//有效期（以:2013-05-20 08:00:00 格式）,在这个时间前发送。超过时间点不发送
		}
		url.append("<FlowrateType>").append("1").append("</FlowrateType>");//流量类型（"1"秒，"2"分钟）
		if(!StringUtils.isEmpty(phoneMarket.getMaxFlowrate())){
			url.append("<MaxFlowrate>").append(phoneMarket.getMaxFlowrate()).append("</MaxFlowrate>");//最大可群发的数量（最多发送20条/秒）,当FlowrateType为1时，为每秒最大的发送量，当FlowrateType为2时，为每分钟最大的发送量。
		}
		url.append("<SeatNbr>").append(phoneMarket.getSeatNbr()).append("</SeatNbr>");//转坐席的坐席号码，多个坐席用","分开。如（18911111111,1891111112）
		url.append("</Body>");
		url.append("</Request>");

		return url.toString();
	}
	
	/**
	 *构建话务账单请求参数
	 * 
	 * @param pm
	 * @return
	 */
	public static String buildTelecomDetail(PhoneMember pm) {
		StringBuilder url = new StringBuilder("");
		String currTimestamp = getTimestampNow();
		url.append("<Request>");
		url.append("<Head>");
		url.append("<MethodName>").append(FjCtCmctConstant.Method.IMS_BILL).append("</MethodName>");
		url.append("<Spid>").append(pm.getSpid()).append("</Spid>");
		url.append("<Appid>").append(pm.getOrgInterfaceId()).append("</Appid>");
		url.append("<Passwd>").append(sha1Encrypt(pm.getPassWd())).append("</Passwd>");
		url.append("<Timestamp>").append(currTimestamp).append("</Timestamp>");
		url.append("<Authenticator>").append(sha1Encrypt(currTimestamp + FjCtCmctConstant.Method.IMS_BILL + pm.getSpid()+pm.getOrgKey()))
				.append("</Authenticator>");
		url.append("</Head>");
		url.append("<Body>");
		url.append("<Ims>").append(pm.getUserId()).append("</Ims>");
		url.append("<Key>").append(pm.getPassword()).append("</Key>");
		url.append("<StartTime>").append(getTimestampByDate(pm.getStartTime())).append("</StartTime>");
		url.append("<EndTime>").append(getTimestampByDate(pm.getEndTime())).append("</EndTime>");
		url.append("<DataType>").append("his").append("</DataType>");
		url.append("</Body>");
		url.append("</Request>");

		return url.toString();
	}
	
	/**
	 * 批量发送营销任务暂停接口
	 * @param ppm
	 * @return
	 */
	private static String buildMarketStop(PhoneMarket market){
		StringBuilder url =  new StringBuilder("");
		PhoneMarketBindPerson pbp=market.getPhoneMbp();
		String currTimestamp = getTimestampNow() ;
		url.append("<Request>");
			url.append("<Head>");
				url.append("<MethodName>").append(FjCtCmctConstant.Method.MARKET_STOP).append("</MethodName>");
				url.append("<Spid>").append(pbp.getSpid()).append("</Spid>");
				url.append("<Appid>").append(pbp.getAppid()).append("</Appid>");
				url.append("<Passwd>").append(sha1Encrypt(pbp.getAppKey())).append("</Passwd>");
				url.append("<Timestamp>").append(currTimestamp).append("</Timestamp>");
				url.append("<Authenticator>").append(sha1Encrypt(currTimestamp+FjCtCmctConstant.Method.MARKET_STOP+pbp.getSpid()+pbp.getAppKey())).append("</Authenticator>");
			url.append("</Head>");
			url.append("<Body>");
				url.append("<WorkID>").append(market.getModeID()).append("</WorkID>");//批量发送营销语音返回的WorkID
			url.append("</Body>");
		url.append("</Request>");
		return url.toString() ;
	}
	
	/**
	 * 批量发送营销任务启动接口
	 * @param ppm
	 * @return
	 */
	private static String buildMarketBegin(PhoneMarket market){
		StringBuilder url =  new StringBuilder("");
		PhoneMarketBindPerson pbp=market.getPhoneMbp();
		String currTimestamp = getTimestampNow() ;
		url.append("<Request>");
			url.append("<Head>");
				url.append("<MethodName>").append(FjCtCmctConstant.Method.MARKET_BEGIN).append("</MethodName>");
				url.append("<Spid>").append(pbp.getSpid()).append("</Spid>");
				url.append("<Appid>").append(pbp.getAppid()).append("</Appid>");
				url.append("<Passwd>").append(sha1Encrypt(pbp.getAppKey())).append("</Passwd>");
				url.append("<Timestamp>").append(currTimestamp).append("</Timestamp>");
				url.append("<Authenticator>").append(sha1Encrypt(currTimestamp+FjCtCmctConstant.Method.MARKET_BEGIN+pbp.getSpid()+pbp.getAppKey())).append("</Authenticator>");
			url.append("</Head>");
			url.append("<Body>");
				url.append("<WorkID>").append(market.getModeID()).append("</WorkID>");//批量发送营销语音返回的WorkID
				url.append("<MaxFlowrate>").append("10").append("</MaxFlowrate>");//每秒钟最大可群发的数量（最多发送20条/秒）
			url.append("</Body>");
		url.append("</Request>");
		return url.toString() ;
	}
	
	/**
	 * 批量发送营销任务关闭接口
	 * @param ppm
	 * @return
	 */
	private static String buildMarketClose(PhoneMarket market){
		StringBuilder url =  new StringBuilder("");
		PhoneMarketBindPerson pbp=market.getPhoneMbp();
		String currTimestamp = getTimestampNow() ;
		url.append("<Request>");
			url.append("<Head>");
				url.append("<MethodName>").append(FjCtCmctConstant.Method.MARKET_CLOSE).append("</MethodName>");
				url.append("<Spid>").append(pbp.getSpid()).append("</Spid>");
				url.append("<Appid>").append(pbp.getAppid()).append("</Appid>");
				url.append("<Passwd>").append(sha1Encrypt(pbp.getAppKey())).append("</Passwd>");
				url.append("<Timestamp>").append(currTimestamp).append("</Timestamp>");
				url.append("<Authenticator>").append(sha1Encrypt(currTimestamp+FjCtCmctConstant.Method.MARKET_CLOSE+pbp.getSpid()+pbp.getAppKey())).append("</Authenticator>");
			url.append("</Head>");
			url.append("<Body>");
				url.append("<WorkID>").append(market.getModeID()).append("</WorkID>");//批量发送营销语音返回的WorkID
			url.append("</Body>");
		url.append("</Request>");
		return url.toString() ;
	}
	
	/**
	 * 生成SHA-1 加密码
	 * @return 
	 */
	public static String sha1Encrypt(String str){
		String result = "";
		try{
			MessageDigest alg = MessageDigest.getInstance("SHA-1");
		    alg.update(str.getBytes());
		    byte[] bts = alg.digest();
		    String tmp = "" ;
		    for (int i = 0; i < bts.length; i++) {
		        tmp = (Integer.toHexString(bts[i] & 0xFF));
		        if (tmp.length() == 1) result += "0";
		          result += tmp;
		    }
		}catch (Exception e) {
			e.printStackTrace();
		}
	    return result ;
	}
	
	/**
	 * 加密推送URL，加密方式：BASE64Encoder
	 * @param url
	 * @return
	 */
	public static String urlEncoder(String url) { 
		if(StringUtils.isEmpty(url)){
			return "";
		}
	    BASE64Encoder encoder = new BASE64Encoder();
	    String result = encoder.encode(url.getBytes());
	    return result;
	}
	
	/**
	 * 获取当前日期时间戳
	 * @return 20140319135731
	 */
	public static String getTimestampNow(){
		String nowDateStr = DateUtil.convertDateToStrHms(DateUtil.getCurDate());
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(nowDateStr);
        return m.replaceAll("").trim();
	}
	
	/**
	 * 解析请求返回的XML字符串为Map对象
	 * @param xmlStr 
	 * @return Map
	 */
	public static Map<String,Object> parseXmlStr(String xmlStr){
		Map<String,Object> nodeMap = new HashMap<String,Object>();
		try{
			Document doc = DocumentHelper.parseText(xmlStr);
			Element root = doc.getRootElement() ;
			loopXmlElement(root, nodeMap);
		}catch(Exception e){
			e.printStackTrace();
		}
		return nodeMap ;
	}
	
	private static void loopXmlElement(Element bodyEle ,Map<String,Object> nodeMap){
		List<Element> childList = bodyEle.elements();
		for(Element child : childList){
			if(child.elements() == null || child.elements().size() == 0){
				if(child.content()!=null ){
					if(child.content().size() == 0){
						nodeMap.put(child.getName(),"");
					}else{
						DefaultText eleText = (DefaultText) child.content().get(0);
						nodeMap.put(child.getName(),eleText.getText());
					}
				}
			}
			loopXmlElement(child, nodeMap);
		}
	} 

	/**
	 * 解析请求返回的XML字符串为Map对象，Body下面有多个元素的map里面取集合dataList
	 * @param xmlStr 
	 * @return Map
	 */
	public static Map<String,Object> parseXmlData(String xmlStr){
		Map<String,Object> nodeMap = new HashMap <String,Object>();
		try{
			List mapList = new ArrayList();
			Document doc = DocumentHelper.parseText(xmlStr);
			Element root = doc.getRootElement() ;
			loopXmlHead(root, nodeMap);
			loopXmlDataList(root, mapList);
			nodeMap.put("dataList", mapList);
		}catch(Exception e){
			e.printStackTrace();
		}
		return nodeMap ;
	}
	private static void loopXmlDataList(Element bodyEle ,List mapList){
		Iterator<Element> dataIters = bodyEle.elementIterator("Body");
		while(dataIters.hasNext()){
			Element data = (Element) dataIters.next();
			List<Element> childList = data.elements();
			for(Element child : childList){
				Map<String,Object> nodeMap = new HashMap<String,Object>();
				loopXmlElement(child, nodeMap);
				mapList.add(nodeMap);
			}
		}
	}
	private static void loopXmlHead(Element bodyEle ,Map<String,Object> nodeMap){
		Iterator dataIters = bodyEle.elementIterator("Head");
		while(dataIters.hasNext()){
			Element data = (Element) dataIters.next();
			loopXmlElement(data, nodeMap);
		}
	} 
	
	
	/**
	 * 把xml中的标签转换 ，将尖括号 转成&lt和&gt
	 * @param xmlArgs 
	 * @return 
	 */
	private static String xmlToText(String xmlArgs){
		StringBuilder textBuilder = new StringBuilder();
		if(!StringUtils.isEmpty(xmlArgs)){
			xmlArgs = xmlArgs.replaceAll("<", "&lt");
			xmlArgs = xmlArgs.replaceAll(">", "&gt");
			textBuilder.append(xmlArgs);
		}
		return textBuilder.toString() ;
	} 
	
	/**
	 * 获取指定日期时间
	 * 
	 * @param date
	 * @return
	 */
	public static String getTimestampByDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	
	public static void main(String[] args) {
		//String xmlstr = "<Response><Head><Result>-1</Result><ResultDesc>postData is empty!</ResultDesc></Head><Body><Ims>0755-61602340</Ims><Key>123456</Key></Body></Response>";
		String xmlstr = "<Response><Head>  <Result>0</Result>  <ResultDesc></ResultDesc></Head>  <Body><Sessionid>31623261919235979381281951404170</Sessionid></Body></Response>";
		try{
			Map<String,Object> nodeMap = parseXmlStr(xmlstr);
			for(String key : nodeMap.keySet()){
//				System.out.println("key:"+key+" value:"+nodeMap.get(key));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
