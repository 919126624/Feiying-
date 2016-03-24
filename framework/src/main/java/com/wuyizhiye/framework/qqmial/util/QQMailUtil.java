package com.wuyizhiye.framework.qqmial.util;

import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.wuyizhiye.base.util.HttpClientUtil;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.util.BaseConfigUtil;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.qqmial.model.QQMail;
import com.wuyizhiye.framework.qqmial.model.QQToken;

/**
 * @ClassName QQMailUtil
 * @Description QQ 企业邮箱工具类
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping("qqmail/*")
public class QQMailUtil {
	
	private static Log log = LogFactory.getLog(QQMailUtil.class);
	
	/**
	 * 获取session
	 * @return
	 */
	public static HttpSession getSession(){
		 return ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest().getSession();
	}
	
	/**
	 * 获取token
	 * @return
	 */
	public static QQToken getToken(){
		QQToken qqMailToken = null ;
		Object token = getSession().getAttribute("qqmail_token");
		if(token == null){
			qqMailToken = QQMailUtil.initQQMailToken(); 
			getSession().setAttribute("qqmail_token", qqMailToken);
		}else{
			qqMailToken = (QQToken) token ;
		}
		return qqMailToken;
	}
	
	/**
	 * 获取单点登录路径
	 * @return
	 */
	public static String getSSOLoginUrl(){
		String url = QQMail.URL_SSOLOGIN ;
		QQToken qqMailToken = QQMailUtil.getToken() ;
		String authKey = QQMailUtil.getAuthKey() ;
		if(qqMailToken != null && !StringUtils.isEmpty(qqMailToken.getAccess_token()) && !StringUtils.isEmpty(authKey)){
			url += "&agent="+BaseConfigUtil.getCurrMailClientId()+"&user="+SystemUtil.getCurrentUser().getEmail()+"&ticket=" +authKey+"&access_token="+qqMailToken.getAccess_token() ;
		}
		return url ;
	}
	
	/**
	 * 获取登录的SID
	 * @param ssoLoginUrl
	 * @param paramStr
	 * @return
	 */
	public static String getMailSid(String ssoLoginUrl,String paramStr){
		String sid = "" ;
		String rtnHtml = HttpClientUtil.callHttpsUrl(ssoLoginUrl, paramStr);
		if(!StringUtils.isEmpty(rtnHtml)){
			int i = rtnHtml.indexOf("sid=");
			if(i >= 0){
				String tempStr = rtnHtml.substring(i);
				int y = tempStr.indexOf("\"");
				String sidStr = tempStr.substring(0,y);
				int d = sidStr.indexOf("=");
				sid = sidStr.substring(d+1);
			}
		}
		return sid;
	}
	
	/**
	 * 获取authKey
	 * @return
	 */
	public static String getAuthKey(){
		String authKey = "" ;
		Object authKeyObj = getSession().getAttribute("qqmail_authKey");
		if(authKeyObj == null){
			authKey = QQMailUtil.initQQMailAuthKey(getToken(), SystemUtil.getCurrentUser());
			getSession().setAttribute("qqmail_authKey", authKey);
		}else{
			authKey = authKeyObj.toString() ;
		}
		return authKey ;
	}
	
	/**
	 * 获取qqmail token 证书
	 * @return
	 */
	public static  QQToken initQQMailToken(){
		QQToken qqMailToken = new QQToken() ;
		try{
			if(StringUtils.isEmpty(BaseConfigUtil.getCurrMailClientId()) 
					|| StringUtils.isEmpty(BaseConfigUtil.getCurrMailClientSecret())){
				return null ;
			}
			Map<String,String> params = new HashMap<String,String>();
			params.put("grant_type", QQToken.GRANT_TYPE);
			params.put("client_id", BaseConfigUtil.getCurrMailClientId());
			params.put("client_secret", BaseConfigUtil.getCurrMailClientSecret());
			String msg = HttpClientUtil.callHttpsUrl(QQMail.URL_GETOAUTH, "",true , params);
			JSONObject jsonObj = JSONObject.fromObject(msg);
			qqMailToken.setAccess_token(jsonObj.getString("access_token"));
			qqMailToken.setToken_type(jsonObj.getString("token_type"));
			qqMailToken.setRefresh_token(jsonObj.getString("refresh_token"));
			qqMailToken.setExpires_in(jsonObj.getString("expires_in") == null ? 0 : Integer.valueOf(jsonObj.getString("expires_in")));
		}catch(Exception e){
			log.error("QQMailUtil.initQQMailToken 出错："+e.getMessage());
			e.printStackTrace();
			return null ;
		}
		return qqMailToken ;
	}
	
	/**
	 * 获取登录人的authkey
	 * @param qqMailToken 证书
	 * @param currPerson 登录人
	 * @return
	 */
	public static String initQQMailAuthKey(QQToken qqMailToken,Person currPerson){
		String authKey = "" ;
		try{
			if(qqMailToken == null || StringUtils.isEmpty(qqMailToken.getAccess_token())){
				return "" ;
			}else if(currPerson!=null && StringUtils.isEmpty(currPerson.getEmail())){
				return "" ;
			}
			Map<String,String> params = new HashMap<String,String>();
			params.put("alias", currPerson.getEmail());
			params.put("access_token", qqMailToken.getAccess_token());
			String msg = HttpClientUtil.callHttpUrl(QQMail.URL_GETAUTHKEY, "",true , params);
			JSONObject jsonObj = JSONObject.fromObject(msg);
			authKey = jsonObj.getString("auth_key") ;
		}catch(Exception e){
			log.error("QQMailUtil.initQQMailAuthKey 出错："+e.getMessage());
			e.printStackTrace();
			return "" ;
		}
		return authKey ;
	}
	
	/**
	 * 获取新邮件数量
	 * @param qqMailToken 证书
	 * @param currPerson 人员
	 * @return -1:token证书为空；-2：人员或者邮箱为空；-3：api返回错误；-4：出现异常错误；
	 */
	public static int getNewCount(QQToken qqMailToken,Person currPerson){
		int newcount = 0 ;
		try{
			if(qqMailToken == null || StringUtils.isEmpty(qqMailToken.getAccess_token())){
				return -1 ;
			}else if(currPerson!=null && StringUtils.isEmpty(currPerson.getEmail())){
				return -2 ;
			}
			Map<String,String> params = new HashMap<String,String>();
			params.put("alias", currPerson.getEmail());
			params.put("access_token", qqMailToken.getAccess_token());
			String msg = HttpClientUtil.callHttpUrl(QQMail.URL_NEWCOUNT, "" , true , params);
			//{"errcode":"1200","error":"invalid_token"}
			JSONObject jsonObj = JSONObject.fromObject(msg);
			String errorMsg = jsonObj.has("error") ? jsonObj.getString("error") : "";
			String errorCode = jsonObj.has("errcode") ? jsonObj.getString("errcode") : "";
			if(!StringUtils.isEmpty(errorMsg)){
				return -3 ;
			}else{
				newcount = jsonObj.getString("NewCount") == null ? 0 : Integer.valueOf(jsonObj.getString("NewCount"));
			}
		}catch(Exception e){
			log.error("QQMailUtil.getNewCount 出错："+e.getMessage());
			e.printStackTrace();
			return -4 ;
		}
		return newcount ;
	}
	
	/**
	 * 同步部门
	 * @param qqMailToken 证书
	 * @param org 组织
	 * @param action 更新动作类型，1=DEL, 2=ADD, 3=MOD 
	 * @param oldOrg 如果action=3，更新需要传递原来的部门
	 * @return -1：证书为空； -2：非法部门；-3：非法动作；-4：出现异常；1：成功调用
	 */
	public static int syncParty(QQToken qqMailToken,Org org , int action ,Org oldOrg){
		int flag = 0 ;
		try{
			//证书为空
			if(qqMailToken == null || StringUtils.isEmpty(qqMailToken.getAccess_token())){
				return -1 ;
			}
			//非法部门
			if(org == null){
				return -2 ;
			}
			//非法动作
			if(action != 1 && action !=2 && action !=3){
				return -3 ;
			}
			String srcOrgName = org.getDisplayName() ;
			String dstOrgName = "" ;
			if(!StringUtils.isEmpty(srcOrgName)){
				String[] orgNames = srcOrgName.split("_");
				int level = orgNames.length ;
				if(level == 1){
					dstOrgName = orgNames[0];
				}else{
					//部门最多 5 级
					for(int i = 1 ; i < ( level > 6 ?  6 : level ); i ++){
						dstOrgName += dstOrgName.length() == 0 ? orgNames[i] : ( "/" + orgNames[i] );
					}
				}
			}
			//构建参数
			StringBuilder paramStr = new StringBuilder("") ;
			paramStr.append("action=").append(action);
			if(action == 3){
				String oldOrgName = oldOrg.getDisplayName() ;
				String srcPath = "" ;
				if(!StringUtils.isEmpty(oldOrgName)){
					String[] oldOrgNames = oldOrgName.split("_");
					int level = oldOrgNames.length ;
					if(level == 1){
						srcPath = oldOrgNames[0];
					}else{
						//部门最多 5 级
						for(int i = 1 ; i < ( level > 6 ?  6 : level ) ; i ++){
							srcPath += srcPath.length() == 0 ? oldOrgNames[i] : ( "/" + oldOrgNames[i] );
						}
					}
				}
				paramStr.append("&srcpath=").append(URLEncoder.encode(srcPath,"UTF-8"));
			}
			paramStr.append("&dstpath=").append(URLEncoder.encode(dstOrgName,"UTF-8"));
			paramStr.append("&access_token=").append(qqMailToken.getAccess_token());
			String msg = HttpClientUtil.callHttpUrl(QQMail.URL_PARTYSYNC , paramStr.toString() , false , null);
			if(StringUtils.isEmpty(msg)){
				flag = 1; 
			}else{
				JSONObject jsonObj = JSONObject.fromObject(msg);
			}
		}catch(Exception e){
			log.error("QQMailUtil.syncParty 出错："+e.getMessage());
			e.printStackTrace();
			return -4 ;
		}
		return flag ;
	}
	
	/**
	 * 同步人员
	 * @param qqMailToken 证书
	 * @param org 组织
	 * @param action 更新动作类型，1=DEL, 2=ADD, 3=MOD 
	 * @return -1：证书为空；-2：非法人员参数；-3：非法动作；-4：出现异常；1：成功调用
	 */
	public static int syncUser(QQToken qqMailToken,Person user , int action){
		int flag = 0 ;
		try{
			//证书为空
			if(qqMailToken == null || StringUtils.isEmpty(qqMailToken.getAccess_token())){
				return -1 ;
			}
			//非法人员参数
			if(user == null){
				return -2 ;
			}
			//非法动作
			if(action != 1 && action !=2 && action !=3){
				return -3 ;
			}
			
		}catch(Exception e){
			log.error("QQMailUtil.syncUser 出错："+e.getMessage());
			e.printStackTrace();
			return -4 ;
		}
		return flag ;
	}
	
	public static void main(String[] args) {
		String callurl = "https://exmail.qq.com/cgi-bin/login?fun=bizopenssologin&method=bizauth&agent=dingjiansoft&user=libiao@dingjianerp.com&ticket=42636475295C1F06EA9C50872F8D445962577F83D61B00F5F65D671C849C80E0F1535CCCFDCE08B4614C16BBC7775911297679961EDFABB3&access_token=e5V5T4uZwHrz04HEUZBE1SJ81EQsVknM0nDQtfcwOHaxWdTYXEfvwupnb9x8rbMnp5ITPargCndC4hqOuyigOA" ;
		String paramStr = "" ;//"firstlogin=false&domain=dingjianerp.com&aliastype=other&uin=libiao&errtemplate=dm_loginpage&starttime=1386059380587&ts=1386058804&f=biz&chg=0&dmtype=bizmail&loginentry=3&inputuin=libiao@dingjianerp.com&pp=zGOQib8mNrRviIpiSQem53WluSqo41zW67oYOp1a88pPzQsnqyRAFxlLz41a0GSgAO0rKPPJgHAJ75hDYPlvge7T936b7b5SqFGw4aBOHnemRktpXUPkTecA2TifHzTB4c29Kwe//wjZWnhIs3TKa/gpeDqaBMnYhVS0USAgK1E=";
		//HttpClientUtil.callHttpsUrl(QQMail.url_sid, paramStr);
		// 响应内容
		String returnContent = ""; 
		
		// 创建默认的httpClient实例
		HttpClient httpClient = new DefaultHttpClient();
		
		// 创建TrustManager
		X509TrustManager xtm = new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[] {};
			}
		};
		try {
			SSLContext ctx = SSLContext.getInstance("SSL");

			// 使用TrustManager来初始化该上下文，TrustManager只是被SSL的Socket所使用
			ctx.init(null, new TrustManager[] { xtm }, null);

			// 创建SSLSocketFactory
			SSLSocketFactory socketFactory = new SSLSocketFactory(ctx);

			// 通过SchemeRegistry将SSLSocketFactory注册到HttpClient上 ； 443为HTTPS默认端口
			httpClient.getConnectionManager().getSchemeRegistry()
					.register(new Scheme("https", 443, socketFactory));

			// 创建HttpPost
			HttpPost httpPost = new HttpPost(callurl); 
			
			// 构建POST请求的表单参数
			httpPost.setEntity(new StringEntity(paramStr, "UTF-8"));
			
			// 执行POST请求
			HttpResponse response = httpClient.execute(httpPost); 
			/*
			int status = response.getStatusLine().getStatusCode();
			System.out.println("status=:"+status);
			Header[] headers = response.getHeaders("Location");
			if(headers != null && headers.length > 0){
				String location = headers[0].getValue();
				//var.setPath(location);
				//httpPost.releaseConnection();
				//result = HttpUtil4.doGetAsString(browser, var, tryTimes);
			}
			*/
			// 获取响应实体
			HttpEntity entity = response.getEntity();

			if (null != entity) {
				returnContent = EntityUtils.toString(entity, "UTF-8");
				EntityUtils.consume(entity); // Consume response content
			}
			/*
			log.debug("callHttpsUrl 请求地址: " + httpPost.getURI());
			log.debug("callHttpsUrl 响应状态: " + response.getStatusLine());
			log.debug("callHttpsUrl 响应内容: " + returnContent);
			*/
			System.out.println("callHttpsUrl 请求地址: " + httpPost.getURI());
			System.out.println("callHttpsUrl 响应状态: " + response.getStatusLine());
			//System.out.println("callHttpsUrl 响应内容: " + returnContent);
			
		} catch (Exception e) {
			log.error("HttpClientUtil.callHttpsUrl 出错：" + e.getMessage());
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			httpClient.getConnectionManager().shutdown(); 
		}
		System.out.println(returnContent);
	}
	
}
