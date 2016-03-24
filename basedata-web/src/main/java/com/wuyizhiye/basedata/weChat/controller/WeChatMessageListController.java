package com.wuyizhiye.basedata.weChat.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.base.wechat.WeChatMsgTypeEnum;
import com.wuyizhiye.base.wechat.WechatEntity;
import com.wuyizhiye.base.wechat.WechatServiceApi;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.basedata.weChat.enums.WeChatMessageTypeEnum;
import com.wuyizhiye.basedata.weChat.model.WeChatMessage;
import com.wuyizhiye.basedata.weChat.service.WeChatMessageService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName WeChatMessageListController
 * @Description 微信消息 控制器
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value = "basedata/weChat/*")
public class WeChatMessageListController extends ListController {

	@Autowired
	private WeChatMessageService weChatMessageService;

	@Override
	protected CoreEntity createNewEntity() {
		return new WeChatMessage();
	}

	@Override
	protected String getListView() {
		this.getRequest().setAttribute("person", SystemUtil.getCurrentUser());
		this.getRequest().setAttribute("position", SystemUtil.getCurrentPosition());
		this.getRequest().setAttribute("wechatId", getString("clientWeChat","-999"));
		this.getRequest().setAttribute("personId", getString("personId","-999"));
		return "basedata/weChat/weChatMessageList";
	}
	
	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "detailList")
	public String detailList() {
		int page = Integer.valueOf(getString("page","1"));
		int pageSize = Integer.valueOf(getString("pageSize","1"));
		String wechatId = getString("wechatId","-999");
		String personId = getString("personId","-999");
		Map<String,Object> queryMap = new HashMap<String,Object>();
		queryMap.put("clientWeChat", wechatId);
		queryMap.put("personId", personId);
		Pagination<WeChatMessage> pagData = new Pagination<WeChatMessage>();
		pagData.setCurrentPage(page);
		pagData.setPageSize(pageSize);
		pagData = queryExecutor.execQuery(getListMapper(), pagData, queryMap);
		boolean showHistory = false ;
		if(pagData.getRecordCount() > page * pageSize){
			showHistory = true ;
		}
		if(pagData!=null && pagData.getItems()!=null && pagData.getItems().size() > 0){
			Collections.sort(pagData.getItems(), new Comparator<WeChatMessage>() {
				@Override
				public int compare(WeChatMessage o1, WeChatMessage o2) {
					return o1.getMsgDate().compareTo(o2.getMsgDate());
				}
				
			});
		}
		this.getRequest().setAttribute("showHistory", showHistory ? "Y" : "N");
		this.getRequest().setAttribute("showList", pagData.getItems());
		this.getRequest().setAttribute("wechatId", wechatId);
		this.getRequest().setAttribute("personId", personId);
		return "basedata/weChat/weChatMessageDetail";
	}
	
	
	/**
	 * 微信发送消息
	 * @param pagination
	 * @param response
	 */
	@RequestMapping(value="sendMessage")
	public void sendMessage(HttpServletResponse response){
		
		String wechatId = getString("wechatId");
		String content = getString("content");
		
		WechatEntity wechat = new WechatEntity() ;
		wechat.setWechatId(wechatId);
		wechat.setType(WeChatMsgTypeEnum.text);
		wechat.setContent(content);
		String msg = WechatServiceApi.sendNewsInfoToUser(wechat);
		if("-1".equals(msg) || "-2".equals(msg)){
			getOutputMsg().put("FLAG", "FAIL");
			getOutputMsg().put("MSG", "参数错误或者没有找到微信号或者没有配置微信地址");
		}else{
			//插入发送消息
			WeChatMessage wechatMsg = new WeChatMessage();
			wechatMsg.setPublicWeChat(null);
			wechatMsg.setClientWeChat(wechatId);
			wechatMsg.setMsgDate(DateUtil.getCurDate());
			wechatMsg.setContent(content);
			wechatMsg.setMessageType(WeChatMessageTypeEnum.SEND);
			wechatMsg.setPerson(SystemUtil.getCurrentUser());
			wechatMsg.setIsRead(0);
			if(!StringUtils.isEmpty(msg)){
				JSONObject jsonObj = JSONObject.fromObject(msg);
				String flag = jsonObj.getString("STATE");
				if("SUCCESS".equals(flag)){
					wechatMsg.setSnedCode("0");
				}else{
					wechatMsg.setSnedCode("-1");
				}
				getOutputMsg().put("FLAG", "SUCC");
				weChatMessageService.addEntity(wechatMsg);
			}else{
				getOutputMsg().put("FLAG", "FAIL");
				getOutputMsg().put("MSG", "微信返回信息为空");
			}
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}

	@Override
	protected String getEditView() {
		return null;
	}

	@Override
	protected String getListMapper() {
		return WeChatMessage.NAME_SPACE + ".select";
	}

	@Override
	protected BaseService getService() {
		return weChatMessageService;
	}
	
	 
}
