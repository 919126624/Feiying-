package com.wuyizhiye.base.timing.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.timing.model.Task;
import com.wuyizhiye.base.timing.service.TaskService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.framework.base.controller.EditController;
import com.wuyizhiye.framework.util.BeanUtils;

/**
 * @ClassName TaskEditController
 * @Description 定时任务列表
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="/base/task/*")
public class TaskEditController extends EditController {
	private static Logger logger=Logger.getLogger(TaskEditController.class);
	@Autowired
	private TaskService taskService;
	@Override
	protected Class<Task> getSubmitClass() {
		return Task.class;
	}

	@Override
	protected TaskService getService() {
		return taskService;
	}
	
	
	@Override
	public void save(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		String timeStr = getString("runTime");
		if(!StringUtils.isNotNull(timeStr)){
			getOutputMsg().put("STATE", "FAILURE");
			getOutputMsg().put("MSG", "未选择时间");
			outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
			return;
		}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");; 
		if(getString("type").equals("CYCLE") && timeStr.length()==5){
			timeStr = "1971-01-01 "+timeStr;
		}
		try {
			timeStr = sdf1.format(sdf.parse(timeStr));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		}
		Task task = null;
		Map<String, String> paramMap = getParamMap();
		paramMap.put("runTime", timeStr);
		task = BeanUtils.fillentity(paramMap,task, Task.class);
		if(validate(task)){
			if(task instanceof CoreEntity){
				if(StringUtils.isEmpty(((CoreEntity)task).getId())){
					task.setIsValid(1);
					getService().addEntity(task);
				}else{
					task.setIsValid(getService().getEntityById(task.getId()).getIsValid());
					getService().updateEntity(task);
				}
				getOutputMsg().put("id", (task).getId());
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "保存成功");
			}
		}else{
			getOutputMsg().put("STATE", "FAIL");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
}
