package com.wuyizhiye.basedata.topic.controller;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.topic.enums.TopicStatusEnum;
import com.wuyizhiye.basedata.topic.model.DealHistory;
import com.wuyizhiye.basedata.topic.model.Topic;
import com.wuyizhiye.basedata.topic.service.DealHistoryService;
import com.wuyizhiye.basedata.topic.service.TopicService;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName DealHistoryEditController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/dealHistory/*")//new
public class DealHistoryEditController extends EditController {
	@Autowired
	private DealHistoryService dealHistoryService;
	@Autowired
	private TopicService topicService;
	@Override
	protected Class<DealHistory> getSubmitClass() {
		return DealHistory.class;
	}

	@Override
	public void save(HttpServletResponse response)
			throws InstantiationException, IllegalAccessException {
		DealHistory data = (DealHistory) getSubmitEntity();
		if(validate(data)){
			//只保存不允许更新
			if(StringUtils.isEmpty(data.getId())){
				Topic topic = topicService.getEntityById(data.getTopic().getId());
				data.setDealPerson(SystemUtil.getCurrentUser());
				data.setDealTime(new Date());
				String newStatus = getString("status");
				TopicStatusEnum topicStatus = Enum.valueOf(TopicStatusEnum.class, newStatus);
				if(topic.getStatus().equals(topicStatus) && !(TopicStatusEnum.STATE_T.toString().equalsIgnoreCase(newStatus)) ){
					//只备注
					data.setRecord("补充描述");
				}else{
					//状态转换
					data.setStatus(topicStatus);
					if(TopicStatusEnum.STATE_T.toString().equalsIgnoreCase(newStatus)){
						if(!topic.getStatus().getLabel().equalsIgnoreCase(topicStatus.getLabel())){
							data.setRecord("将状态由 \""+topic.getStatus().getLabel() + "\" 转为 \"" +topicStatus.getLabel()+ "\" ,问题处理者：由 "+topic.getDealPerson().getName()+" 变成 "+this.getString("dealPerson.name"));
						} else {
							data.setRecord("问题再次被转交 ,问题处理者：由 "+topic.getDealPerson().getName()+" 变成 "+this.getString("dealPerson.name"));
						}
						topic.setDealPerson(new Person(this.getString("dealPerson.id")));
					} else {
						data.setRecord("将状态由 \""+topic.getStatus().getLabel() + "\" 转为 \"" +topicStatus.getLabel()+ "\"");
					}
					topic.setStatus(topicStatus);
					topicService.updateEntity(topic);
				}
				data.setStatus(topic.getStatus());
				getService().addEntity(data);
			}
			getOutputMsg().put("id", data.getId());
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("MSG", "处理成功");
		}else{
			getOutputMsg().put("STATE", "FAIL");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	@Override
	protected BaseService<DealHistory> getService() {
		return dealHistoryService;
	}

}
