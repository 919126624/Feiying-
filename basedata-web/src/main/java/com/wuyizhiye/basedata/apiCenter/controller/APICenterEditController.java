package com.wuyizhiye.basedata.apiCenter.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.apiCenter.model.APIItem;
import com.wuyizhiye.basedata.apiCenter.service.APIItemService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName APICenterEditController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/apiCenter/*")
public class APICenterEditController extends EditController {
	@Autowired
	private APIItemService apiItemService;
	@Override
	protected Class<APIItem> getSubmitClass() {
		return APIItem.class;
	}
	 

	@Override
	protected BaseService<APIItem> getService() {
		return apiItemService;
	}
	
	
	@RequestMapping(value="save")
	public void save(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		APIItem data = (APIItem) getSubmitEntity();
		if(validate(data)){
			if(data instanceof APIItem){
				Map<String, Object> param=new HashMap<String, Object>();
				param.put("number", data.getNumber());
				APIItem db =queryExecutor.execOneEntity("com.wuyizhiye.basedata.apiCenter.dao.APIItemDao.select", param, APIItem.class);
				if(db!=null && db.getNumber().equals(data.getNumber()) && !db.getId().equals(data.getId())){ 
					getOutputMsg().put("STATE", "FAIL");
					getOutputMsg().put("MSG", data.getNumber()+"编号接口已经存在！");
				}else{
					if(StringUtils.isEmpty(data.getId())){
						getService().addEntity(data);
					}else{
						getService().updateEntity(data);
					}
					getOutputMsg().put("id", (data).getId());
					getOutputMsg().put("STATE", "SUCCESS");
					getOutputMsg().put("MSG", "保存成功");
				}
			}
		}else{
			getOutputMsg().put("STATE", "FAIL");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	 
}
