package com.wuyizhiye.cmct.wechat.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.service.PersonService;
import com.wuyizhiye.cmct.wechat.model.RecruitInfo;
import com.wuyizhiye.cmct.wechat.service.RecruitInfoService;
import com.wuyizhiye.framework.base.controller.BaseController;

/**
 * @ClassName MobileRecruitInfoController
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping("cmct/ri/*")
public class MobileRecruitInfoController extends BaseController{
	
	private final String Mapper = "com.wuyizhiye.cmct.wechat.dao.RecruitInfoDao";
	
	@Autowired
	PersonService personService;
	@Autowired
	RecruitInfoService recruitInfoService;
	
	
	@RequestMapping("list")
	public String list(ModelMap model){
		return "cmct/wechat/RecruitInfoList";
	}
	
	@RequestMapping("add")
	public String add(ModelMap model){
		String type = getString("VIEWSTATE");
		String id = getString("id");
		try {
			if(StringUtils.isNotNull(id)){
				RecruitInfo ri = recruitInfoService.getEntityById(id);
				model.put("personId", ri.getPerson().getId());
				Map map = this.queryExecutor.execOneEntity(Mapper+".selectPersonDescById", model, Map.class);
				ri.setPersonDesc(map.get("personName")+"("+map.get("orgName")+")");
				model.put("data", ri);
			}
		} catch (Exception e) {
		}
		return "cmct/wechat/RecruitInfoAdd";
	}
	
	@RequestMapping("ajaxSave")
	public void ajaxSave(ModelMap model,HttpServletResponse response){
		String id = getString("id");
		String personId = getString("personId");
		String recruitCount = getString("recruitCount");
		String recruitRemark = getString("recruitRemark");
		try {
			if(StringUtils.isNotNull(id)){
				//修改
				RecruitInfo ri = recruitInfoService.getEntityById(id);
				Person p = new Person();
				p.setId(personId);
				ri.setPerson(p);
				ri.setRecruitCount(Integer.valueOf(recruitCount));
				ri.setRecruitRemark(recruitRemark);
				
				recruitInfoService.updateEntity(ri);
			}else{
				//新增
				RecruitInfo ri = new RecruitInfo();
				
				ri.setUUID();
				Person p = new Person();
				
				if(checkPersonisExist(personId)>0){
					model.put("MSG", "用户已存在");
					outPrint(response, JSONObject.fromObject(model));
					return ;
				};
				
				p.setId(personId);
				ri.setPerson(p);
				ri.setRecruitCount(Integer.valueOf(recruitCount));
				ri.setRecruitRemark(recruitRemark);
				
				recruitInfoService.addEntity(ri);
			}
		} catch (Exception e) {
			model.put("MSG", "未知错误");
		}
		outPrint(response, JSONObject.fromObject(model));
	}
	
	private int checkPersonisExist(String personId) {
		HashMap param = new HashMap();
		param.put("personId", personId);
		int i = this.queryExecutor.execCount(Mapper+".select", param);
		return i;
	}

	@RequestMapping("listData")
	public void listData(ModelMap model,HttpServletResponse response,Pagination<RecruitInfo> pagination){
		String keyWord = getString("key");
		model.put("keyWord", keyWord);
		
		Pagination<RecruitInfo> list = this.queryExecutor.execQuery(Mapper+".select", pagination, model);
		
		
		outPrint(response, JSONObject.fromObject(list));
	}
	@RequestMapping("delete")
	public void delete(HttpServletResponse response){
		try {
			String id = getString("id");
			if(StringUtils.isNotNull(id)){
				recruitInfoService.deleteById(id);
			}
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("MSG", "删除成功");
		} catch (Exception e) {
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "未知错误");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
}
