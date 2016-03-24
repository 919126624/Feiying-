package com.wuyizhiye.basedata.remind.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.spring.ApplicationContextAware;
import com.wuyizhiye.basedata.remind.model.Remind;
import com.wuyizhiye.basedata.remind.service.RemindService;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName RemindListController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/remind/*")
public class RemindListController extends ListController{
	
	@Autowired
	private RemindService remindService;

	@Override
	protected CoreEntity createNewEntity() {
		return new Remind();
	}

	@Override
	protected String getListView() {
		return "basedata/remind/remindList";
	}

	@Override
	protected String getEditView() {
		return "basedata/remind/remindEdit";
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.remind.dao.RemindDao.select";
	}

	@Override
	protected RemindService getService() {
		return remindService;
	}
	
	@RequestMapping(value="getRemindById")
	public void getRemindById(HttpServletResponse response) {
		Remind remind = remindService.getEntityById(getString("id"));
		getOutputMsg().put("remind", remind);
		getOutputMsg().put("STATE", "SUCCESS");
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="toIndex")
	public String toIndex(HttpServletResponse response) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("job", SystemUtil.getCurrentPosition().getJob().getId());
		List<Remind> remindList = queryExecutor.execQuery("com.wuyizhiye.basedata.remind.dao.RemindDao.selectByJob", param, Remind.class);
		Iterator<Remind> remite = remindList.iterator();
		while(remite.hasNext()){
			Remind remind = remite.next();
			String url = remind.getUrl();
			int lastIndexU = url.lastIndexOf(".");
			String clazzStr = url.substring(0,lastIndexU);
			String methodStr = url.substring(lastIndexU+1);
			Object intentionCustomerService = ApplicationContextAware.getApplicationContext().getBean(clazzStr);
			Method method = intentionCustomerService.getClass().getMethod(methodStr,Map.class);
			Map<String,Object> map = (Map<String,Object>)method.invoke(intentionCustomerService,new HashMap<String,Object>());
			String remark = remind.getRemark();
			
			Set<String> set = map.keySet();
			Iterator<String> iterator = set.iterator();
			boolean isShow = false;
			while(iterator.hasNext()){
				String key = iterator.next();
				String keystr = map.get(key).toString();
				if(key.contains("key")&&!keystr.contains(">0<")&&(!"0".equals(keystr))) isShow = true;
				remark = remark.replace("#"+key+"#", map.get(key).toString());
				
			}
			remind.setShow(isShow?1:0);
			remind.setRemark(remark);
			if(!isShow) remite.remove();
			
		}
		
		getRequest().setAttribute("remindList", remindList);
		return "basedata/remind/remind";
	}
	
	@Override
	public void delete(@RequestParam(required=true,value="id")String id,HttpServletResponse response){
		Remind remind = (Remind)getService().getEntityById(id);
		if(remind!=null){
				Map<String,Object> param = new HashMap<String,Object>();
				param.put("remindId", remind.getId());
				Integer count = queryExecutor.execOneEntity("com.wuyizhiye.basedata.remind.dao.RemindManageDao.selectCount", param, Integer.class);
				if(count>0){
					getOutputMsg().put("STATE", "FAIL");
					getOutputMsg().put("MSG", "该项已经被关联，不允许删除");
					outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
					return;
				}
				getService().deleteEntity( remind);
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "删除成功");
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "记录不存在");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
}
