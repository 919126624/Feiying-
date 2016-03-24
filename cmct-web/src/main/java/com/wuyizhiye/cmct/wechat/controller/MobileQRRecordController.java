package com.wuyizhiye.cmct.wechat.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.util.HttpClientUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.basic.model.BaseConfig;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.service.PersonService;
import com.wuyizhiye.cmct.wechat.enums.QRUserEnum;
import com.wuyizhiye.cmct.wechat.model.QRRecord;
import com.wuyizhiye.framework.base.controller.BaseController;

/**
 * @ClassName MobileQRRecordController
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping("cmct/qr/*")
public class MobileQRRecordController extends BaseController{
	
	private final String Mapper = "com.wuyizhiye.cmct.wechat.dao.QRRecordDao";
	
	@Autowired
	PersonService personService;
	
	
	@RequestMapping("list")
	public String list(ModelMap model){
		return "cmct/wechat/qrList";
	}
	
	@RequestMapping("listData")
	public void listPagData(ModelMap model,HttpServletResponse response){
		Integer currentPage = getString("currentPage")==null? 1 : Integer.valueOf(getString("currentPage"));
		Integer pageSize = getString("pageSize")==null? 10 : Integer.valueOf(getString("pageSize")); 
		String keyWord = getString("key");
		model.put("keyWord", keyWord);
		
		Pagination<QRRecord> list = this.queryExecutor.execQuery(Mapper+".select", new Pagination<QRRecord>(pageSize, currentPage), model);
		//填充粉丝数
		outPrint(response, JSONObject.fromObject(list));
	}
	
	@RequestMapping("add")
	public String add(ModelMap model){
		model.put("targetList", QRUserEnum.values());
		return "cmct/wechat/qrAdd";
	}
	@RequestMapping("edit")
	public String edit(ModelMap model){
		model.put("targetList", QRUserEnum.values());
		String id = getString("id");
		model.put("id", id);
		QRRecord qrRecord = this.queryExecutor.execOneEntity(Mapper+".getById",model , QRRecord.class);
		model.put("data", qrRecord);
		return "cmct/wechat/qrAdd";
	}
	@RequestMapping("delete")
	public void delete(ModelMap model,HttpServletResponse response){
		try {
			String id = getString("id");
			model.put("id", id);
			this.queryExecutor.executeDelete(Mapper+".delete", model);
			
			model.put("STATE", "SUCCESS");
			model.put("MSG", "删除成功");
		} catch (Exception e) {
			model.put("MSG", e.getStackTrace());
		}
		
		outPrint(response, JSONObject.fromObject(model));
	}
	
	
	@RequestMapping("ajaxSave")
	public void ajaxSave(ModelMap model,HttpServletResponse response){
		String id = getString("id");
		
		String type = getString("type");
		String target = getString("target");
		String personId = getString("personId");
		String remark = getString("remark");
		String orgLongNumber = getString("orgLongNumber");
		try {
			//如果选择组织
			if(StringUtils.isNotNull(orgLongNumber)){
				model.put("includeChild", 1);
				model.put("orgLongNumber", orgLongNumber);
				try {
//					List<Person> personList = this.queryExecutor.execQuery(Person.MAPPER+".getPersonInfoByOrg", model, Person.class);
					Pagination<Person> persons = new Pagination<Person>(20,1);
					int curnum = 0;
					do{
					//Pagination<Person> p1 = this.queryExecutor.execQuery(Person.MAPPER+".getPersonInfoByOrg", new Pagination<Person>(20,1), model);
						persons.setCurrentPage(curnum+1);
					//for(int i =1;i<p1.getPageCount();i++){
						persons = this.queryExecutor.execQuery(Person.MAPPER+".getPersonInfoByOrg",persons, model);
						for(Person p : persons.getItems()){
							//新增
							if(checkPersonHasQRRecord(p.getId())>0){
								continue;
							}
							QRRecord qr = new QRRecord();
							
							qr.setCreateTime(new Date());
							qr.setUUID();
							qr.setPerson(p);
							qr.setRemark(remark);
							
							target = p.getName()+"("+p.getPersonPosition().getPosition().getBelongOrg().getName()+")";
							qr.setTarget(target);
							qr.setType(QRUserEnum.PERSON);
							
							createQRUrl(qr);
							
							this.queryExecutor.executeInsert(Mapper+".insert", qr);
						}
						curnum++;
					//}
					}while(persons.getCurrentPage()<persons.getPageCount());
					model.clear();
					model.put("STATE", "SUCCESS");
					model.put("MSG", "批量新增成功");
				} catch (Exception e) {
					model.put("MSG", e.getMessage());
				}
				outPrint(response, JSONObject.fromObject(model));
				return ;
			}
			
			
			if(StringUtils.isEmpty(id)){
				//新增
				QRRecord qr = new QRRecord();
				
				
				
				qr.setCreateTime(new Date());
				qr.setUUID();
				if(StringUtils.isNotNull(personId)&&type.equals(QRUserEnum.PERSON)){
					if(checkPersonHasQRRecord(personId)>0){
						model.put("STATE", "FAIL");
						model.put("MSG", "该用户已存在");
						outPrint(response, JSONObject.fromObject(model));
						return;
					}
					Person person = new Person();
					person.setId(personId);
					qr.setPerson(person);
				}
				qr.setRemark(remark);
				qr.setTarget(target);
				qr.setType(QRUserEnum.valueOf(type));
				
				createQRUrl(qr);
				
				this.queryExecutor.executeInsert(Mapper+".insert", qr);
				model.put("MSG", "新增成功");
			}else{
				//修改
				model.put("id", id);
				QRRecord qr = this.queryExecutor.execOneEntity(Mapper+".getById", model, QRRecord.class);
				if(StringUtils.isNotNull(personId)){
					Person person = new Person();
					person.setId(personId);
					qr.setPerson(person);
				}
				qr.setRemark(remark);
				qr.setTarget(target);
				qr.setType(QRUserEnum.valueOf(type));
				
				createQRUrl(qr);
				
				this.queryExecutor.executeUpdate(Mapper+".update", qr);
				
				model.put("MSG", "修改成功");
			}
			model.put("STATE", "SUCCESS");
		} catch (Exception e) {
			model.put("STATE", "FAIL");
			model.put("MSG", e.getMessage());
		}
		outPrint(response, JSONObject.fromObject(model));
	}
	
	@RequestMapping("addQRCode")
	public void addQRCode(HttpServletResponse response ,ModelMap model){
		try {
			String id = getString("id");
			model.put("id", id);
			QRRecord qr = this.queryExecutor.execOneEntity(Mapper+".getById", model, QRRecord.class);
			
			String qrKey = qr.getQRKey();
			Map param = new HashMap();
			param.put("number", "qrUrl");
			BaseConfig bsQr = this.queryExecutor.execOneEntity("com.wuyizhiye.basedata.basic.dao.BaseConfigDao.getByNumber", param, BaseConfig.class);
			String qrUrl = bsQr.getParamValue();
			if(StringUtils.isEmpty(qrUrl)){
				model.put("MSG", "请先去设置系统参数 二维码请求路径");
			}
//			String result = HttpClientUtil.callHttpUrlGet("http://mobile.51zhiye.com/weixinapi/getQRcode?qrkey="+qrKey);
			String result = HttpClientUtil.callHttpUrlGet(qrUrl+"?qrkey="+qrKey);
			if(result.indexOf("STATE")>0){
				String url = result.substring(result.indexOf("\"url\":")+7,result.length()-3);
				if(StringUtils.isNotNull(url)){
					qr.setUrl(url);
					this.queryExecutor.executeUpdate(Mapper+".update", qr);
				}
			}
			if(qr.getUrl()!=null){
				model.put("MSG", "生成成功");
			}else{
				model.put("MSG", "生成失败");
			}
		} catch (Exception e) {
			model.put("MSG", "生成失败");
		}
		outPrint(response, JSONObject.fromObject(model));
	}
	
	private String createQRUrl(QRRecord qr) throws Exception{
		qr.setQRKey(getQRCodeNumber());
		String qrKey = qr.getQRKey();
		Map param = new HashMap();
		param.put("number", "qrUrl");
		BaseConfig bsQr = this.queryExecutor.execOneEntity("com.wuyizhiye.basedata.basic.dao.BaseConfigDao.getByNumber", param, BaseConfig.class);
		String qrUrl = bsQr.getParamValue();
		
		if(StringUtils.isEmpty(qrUrl)){
			throw new Exception("请先去设置系统参数 二维码请求路径");
		}
		
		
//		String result = HttpClientUtil.callHttpUrlGet("http://mobile.51zhiye.com/weixinapi/getQRcode?qrkey="+qrKey);
		String result = HttpClientUtil.callHttpUrlGet(qrUrl+"?qrkey="+qrKey);
		if(result.indexOf("STATE")>0){
			String url = result.substring(result.indexOf("\"url\":")+7,result.length()-3);
			if(StringUtils.isNotNull(url)){
				qr.setUrl(url);
				this.queryExecutor.executeUpdate(Mapper+".update", qr);
			}
		}
		return result;
	}
	
	
	private synchronized String getQRCodeNumber(){
		String qrKey = this.queryExecutor.execOneEntity(Mapper+".getMaxQRKey", null, String.class);
		return qrKey==null? "1" : ((Integer.valueOf(qrKey)+1)+"");  
	}
	
	private int checkPersonHasQRRecord(String personId){
		HashMap param = new HashMap();
		param.put("personId", personId);
		
		Integer i = this.queryExecutor.execOneEntity(Mapper+".checkPersonHashQRRecord", param, Integer.class);
		return i;
	}
	
	@RequestMapping("demo1")
	public String demo1(){
		return "mobile/demo/demo4";
	}
	
	@RequestMapping("demo2")
	public String demo2(){
		return "mobile/demo/demo5";
	}
}
