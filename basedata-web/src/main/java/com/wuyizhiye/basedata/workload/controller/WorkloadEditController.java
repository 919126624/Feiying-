package com.wuyizhiye.basedata.workload.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.workload.model.Workload;
import com.wuyizhiye.basedata.workload.service.WorkloadService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName WorkloadEditController
 * @Description 工作量
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/workload/*")
public class WorkloadEditController extends EditController{
	
	@Autowired
	private WorkloadService workloadService;

	@Override
	protected Class<Workload> getSubmitClass() {
		return Workload.class;
	}

	@Override
	protected BaseService<Workload> getService() {
		return workloadService;
	}
	
	@RequestMapping(value="setAccreditRow")
	public void setAccreditRow(HttpServletResponse response) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("id", getString("jobId"));
		int count = queryExecutor.execCount("com.wuyizhiye.basedata.org.dao.JobDao.select", param);
		if(count>=1){
			workloadService.setAccredit(getString("workloadId"),getString("jobId"));
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("MSG", "设置成功");
			outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
		}else{
			getOutputMsg().put("MSG", "请选中正确的岗位授权");
			outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
		}
	}
	
	@RequestMapping(value="delAccreditRow")
	public void delAccreditRow(HttpServletResponse response) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("id", getString("jobId"));
		int count = queryExecutor.execCount("com.wuyizhiye.basedata.org.dao.JobDao.select", param);
		if(count>=1){
			workloadService.delAccredit(getString("workloadId"),getString("jobId"));
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("MSG", "设置成功");
			outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
		}else{
			getOutputMsg().put("MSG", "请选中正确的岗位授权");
			outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
		}
	}
	
//	@RequestMapping(value="saveSort")
//	public void saveSort(HttpServletResponse response) {
//		String paramStr = getString("paramStr");
//		String[] param = paramStr.split(";");
//		for(int i=0;i<param.length;i++){
//			String typeValue = param[i].split(":")[0];
//			String number = param[i].split(":")[1];
//			String name = param[i].split(":")[2];
//			WorkloadSort workloadSort = new WorkloadSort();
//			workloadSort.setTypeValue(typeValue);
//			workloadSort.setNumber(number);
//			workloadSort.setName(name);
//			workloadService.saveSort(workloadSort);
//		}
//		getOutputMsg().put("STATE", "SUCCESS");
//		getOutputMsg().put("MSG", "设置成功");
//		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
//	}

}
