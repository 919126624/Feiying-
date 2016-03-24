package com.wuyizhiye.cmct.phone.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.cmct.phone.model.PhoneRight;
import com.wuyizhiye.cmct.phone.service.PhoneRightService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName PhoneRightEditController
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping("cmct/phoneRight/*")
public class PhoneRightEditController extends EditController {

	@Autowired
	private PhoneRightService phoneRightService;
	
	@Override
	protected Class getSubmitClass() {
		return PhoneRight.class;
	}

	@Override
	protected BaseService getService() {
		return phoneRightService;
	}
	
	@RequestMapping(value="saveRight")
	public void saveRight(HttpServletResponse response){
		Object data = getSubmitEntity();
		if(validate(data)){
				PhoneRight pr = (PhoneRight)data;
				try {
					phoneRightService.saveRight(pr);				
					getOutputMsg().put("id", ((CoreEntity)data).getId());
					getOutputMsg().put("STATE", "SUCCESS");
					getOutputMsg().put("MSG", "保存成功");
				} catch (Exception e) {
					getOutputMsg().put("STATE", "FAIL");
					getOutputMsg().put("MSG", e.getMessage());
				}
			
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "数据不符合规范");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	@RequestMapping(value="delRight")
	public void delRight(@RequestParam(required=true,value="id")String id,HttpServletResponse response) throws Exception{
		PhoneRight entity = this.phoneRightService.getEntityById(id);
		if(entity!=null){
				Map<String,Object> param = new HashMap<String,Object>();
				param.put("phoneRightId", id);
				int c = 
						this.queryExecutor.execOneEntity("com.wuyizhiye.cmct.phone.dao.PhoneRightDao.isRightBind", param, Integer.class);
				if(c==0){
					this.phoneRightService.delRight(entity);
					getOutputMsg().put("STATE", "SUCCESS");
					getOutputMsg().put("MSG", "删除成功");
				}else{
					getOutputMsg().put("STATE", "FAIL");
					getOutputMsg().put("MSG", "该权限已绑定线路,不可删除");
				}
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "记录不存在");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}

}
