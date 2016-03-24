package com.wuyizhiye.basedata.mark.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.basic.model.Mark;
import com.wuyizhiye.basedata.basic.service.MarkService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName MarkEidtController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping("basedata/mark/*")
public class MarkEidtController extends EditController {

	@Autowired
	private MarkService markBasicService;

	@Override
	protected Class getSubmitClass() {
		return Mark.class;
	}

	@Override
	protected BaseService getService() {
		return markBasicService;
	}

	/**
	 * 如果更改了归属；则需要改变其孩子的长编码
	 */
	private List<Mark> setChildLongNumber(String oldLongNumber,String newLongNumber,String oldLongName,String newLongName){
		//根据长编码找出其孩子
		Map<String, Object> param=new HashMap<String, Object>();
		param.put("loadChild", oldLongNumber==null?"loadChild":oldLongNumber);
		//更新其孩子的长编码
		List<Mark> marks=queryExecutor.execQuery(Mark.MAPPER+".select", param, Mark.class);
		for(Mark mark:marks){
			//更新长编码
			mark.setLongNumber(mark.getLongNumber().replaceFirst(oldLongNumber, newLongNumber));
			mark.setLongName(mark.getLongName().replaceFirst(oldLongName, newLongName));
		}
		return marks;
	}
	
	private boolean validate(Mark data) {
		//判断数据库中是否存在该名字
		Map<String, Object> param=new HashMap<String, Object>();
		param.put("currentRcoderId", data.getId());
		param.put("existName", data.getName());
		int count=queryExecutor.execCount(Mark.MAPPER+".isExistName", param);
		if(count>0){
			getOutputMsg().put("MSG", "标签名称已在数据库中存在！");
			return false;
		}
		return true;
	}
	
	@Override
	public void save(HttpServletResponse response)
			throws InstantiationException, IllegalAccessException {
		Mark data = (Mark) getSubmitEntity();
		if (validate(data)) {
			if (data instanceof CoreEntity) {
				if (StringUtils.isEmpty(data.getId())) {
					data.setModule("FASTSALE");
					String number=markBasicService.getMarkCode(Mark.NUMBER_S);
					data.setNumber(number);
					if(StringUtils.isNotNull(data.getPid())){//如果有归属
						Mark parent=markBasicService.getEntityById(data.getPid());
						data.setParent(parent);
						data.setLongNumber(data.getParent().getLongNumber()+"!"+number);
						data.setLongName(data.getParent().getLongName()+"_"+data.getName());
					}else{
						data.setLongNumber(number);
						data.setLongName(data.getName());
					}
					markBasicService.addEntity(data);
				} else {
					List<Mark> marks=new ArrayList<Mark>();
					if(StringUtils.isNotNull(data.getPid())){//如果有归属
						Mark parent=markBasicService.getEntityById(data.getPid());
						data.setParent(parent);
						String oldLongNumber=data.getLongNumber();
						data.setLongNumber(data.getParent().getLongNumber()+"!"+data.getNumber());
						String oldLongName=data.getLongName();
						data.setLongName(data.getParent().getLongName()+"_"+data.getName());
						marks=setChildLongNumber(oldLongNumber, data.getLongNumber(), oldLongName, data.getLongName());
					}else{
						//判断级别
						if(data.getSystem()==0){//按照系统级别恢复
							String oldLongNumber=data.getLongNumber();
							String oldLongName=data.getLongName();
							data.setParent(null);
							data.setLongNumber(data.getNumber());
							data.setLongName(data.getName());
							marks=setChildLongNumber(oldLongNumber, data.getLongNumber(), oldLongName, data.getLongName());
						}else{//按照非系统级别恢复[置空]
							data.setParent(null);
							data.setLongNumber(null);
							data.setLongName(null);
							//不会有叶子节点
						}
					}
					markBasicService.updateEntity2(data,marks);
				}
				getOutputMsg().put("id", ((CoreEntity) data).getId());
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "保存成功");
			}
		} else {
			getOutputMsg().put("STATE", "FAIL");
		}
		outPrint(response,
				JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
}
