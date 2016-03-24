package com.wuyizhiye.framework.qqmial.controller;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.BaseController;
import com.wuyizhiye.framework.qqmial.model.QQToken;
import com.wuyizhiye.framework.qqmial.util.QQMailUtil;

/**
 * @ClassName QQMailController
 * @Description  QQ 企业邮箱控制器
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping("qqmail/*")
public class QQMailController extends BaseController{
	
	/**
	 * 首页
	 * @return
	 */
	@RequestMapping(value = "ssologin")
	public String index(ModelMap modelMap){
		boolean isLogin = Boolean.valueOf(getString("isLogin","false"));
		if(!isLogin){
			String ssologinurl = QQMailUtil.getSSOLoginUrl();
			modelMap.put("qqmail_ssologin", ssologinurl);
			String sid = QQMailUtil.getMailSid(ssologinurl, "");
			getSession().setAttribute("mailsid", sid);
			modelMap.put("mailsid", sid);
		}
		return "framework/qqmail/qqmail" ;
	}
	
	/**
	 * 新消息数量
	 */
	@RequestMapping(value="newcount")
	public void newcount(HttpServletResponse response){
		QQToken qqMailToken = QQMailUtil.getToken() ;
		if(qqMailToken == null){
			getOutputMsg().put("newcount", -1);
		}else{
			int newcount = QQMailUtil.getNewCount(qqMailToken, getCurrentUser());
			if(newcount == -3){
				//重新获取
				qqMailToken = QQMailUtil.initQQMailToken(); 
				getSession().setAttribute("qqmail_token", qqMailToken);
				String authKey = QQMailUtil.initQQMailAuthKey(qqMailToken, SystemUtil.getCurrentUser());
				getSession().setAttribute("qqmail_authKey", authKey);
				newcount = QQMailUtil.getNewCount(qqMailToken, getCurrentUser());
			}
			getOutputMsg().put("newcount", newcount);
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	/**
	 * 新消息数量
	 * @return
	 */
	public int getNewCount(){
		QQToken qqMailToken = QQMailUtil.getToken() ;
		if(qqMailToken == null){
			return -1;
		}else{
			int newcount = QQMailUtil.getNewCount(qqMailToken, getCurrentUser());
			if(newcount == -3){
				//重新获取
				qqMailToken = QQMailUtil.initQQMailToken(); 
				getSession().setAttribute("qqmail_token", qqMailToken);
				String authKey = QQMailUtil.initQQMailAuthKey(qqMailToken, SystemUtil.getCurrentUser());
				getSession().setAttribute("qqmail_authKey", authKey);
				newcount = QQMailUtil.getNewCount(qqMailToken, getCurrentUser());
			}
			return newcount;
		}
	}
	
}
