package com.wuyizhiye.basedata.info.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.util.HttpClientUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.info.dao.InfocenterDao;
import com.wuyizhiye.basedata.info.model.InfoCenterUnread;
import com.wuyizhiye.basedata.info.model.InfoTypeEnum;
import com.wuyizhiye.basedata.info.model.Infocenter;
import com.wuyizhiye.basedata.info.model.Remind;
import com.wuyizhiye.basedata.info.model.RemindTypeEnum;
import com.wuyizhiye.basedata.info.service.InfoCenterUnreadService;
import com.wuyizhiye.basedata.info.service.InfocenterService;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.service.PersonService;
import com.wuyizhiye.basedata.service.impl.DataEntityService;
import com.wuyizhiye.basedata.util.SystemUtil;

/**
 * @ClassName InfocenterServiceImpl
 * @Description 消息中心
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value = "infocenterService")
@Transactional
public class InfocenterServiceImpl extends DataEntityService<Infocenter>
		implements InfocenterService {
	private Logger logger= Logger.getLogger(InfocenterServiceImpl.class);
	@Autowired
	private InfocenterDao infocenterDao;
	
	@Autowired
	private PersonService personService;
	@Autowired
	private InfoCenterUnreadService infoCenterUnreadService;
	
	@Override
	protected BaseDao getDao() {
		return infocenterDao;
	}
	
	@Override
	public void sendInfo(Map<String, Object> map) {
		if(map.get("type").equals("free")){
			Set<String> personIdSet=new HashSet<String>();
			if(StringUtils.isNotNull(map.get("jobs"))){
				String[] jobIdArr = map.get("jobs").toString().split(";");
				for(String jobId:jobIdArr){
					Map<String,Object> param = new HashMap<String,Object>();
					param.put("jobId", jobId);
					List<Person> personList = queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonDao.getPersonOfJob", param, Person.class);//通过岗位得到下面人员的id
					for(Person person:personList){
						personIdSet.add(person.getId());
					}
				}
			}
			
			if(StringUtils.isNotNull(map.get("orgIds"))){
				String[] orgIdArr = map.get("orgIds").toString().split(";");
				for(String orgId:orgIdArr){
					Map<String,Object> param = new HashMap<String,Object>();
					param.put("orgId", orgId);
					List<Person> personList = queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonDao.getDismissionPersonByOrg", param, Person.class);//通过组织得到下面人员的id
					for(Person person:personList){
						personIdSet.add(person.getId());
					}
				}
			}
			
			if(StringUtils.isNotNull(map.get("personIds"))){
				String[] personIdArr = map.get("personIds").toString().split(";");
				for(String id:personIdArr){
					personIdSet.add(id);
				}
			}
		
			for(String personId:personIdSet){
				insertInfocenter("系统通知", InfoTypeEnum.SYSTEM, map.get("content").toString(), RemindTypeEnum.FLOAT_BOX, personId, SystemUtil.getCurrentUser().getId(), "", "url");
			}
			
			if(map.get("receive").toString().indexOf("weixin")>0){
				for(String personId:personIdSet){
					Person person = personService.getEntityById(personId);
					try {
						HttpClientUtil.callHttpsUrl(SystemUtil.getBase()+"/weixinapi/sendMessage?m_teyp=text&m_opiden="+person.getNumber()+"&m_module=zhushou&m_contten="+URLEncoder.encode(
								"系统消息："+map.get("content").toString(),"utf-8"), "");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						logger.error("", e);
					} 
				}
			}
		}else{
			
			if(StringUtils.isNotNull(map.get("ids").toString())){
				String[] personIdArr = map.get("ids").toString().split(",");
				for(String id:personIdArr){
					insertInfocenter("系统通知", InfoTypeEnum.SYSTEM, map.get("content").toString(), RemindTypeEnum.FLOAT_BOX, id, SystemUtil.getCurrentUser().getId(), "", "url");
				}
				
				if(map.get("receive").toString().indexOf("weixin")>0){
					for(String id:personIdArr){
						Person person = personService.getEntityById(id);
						try {
							HttpClientUtil.callHttpsUrl(SystemUtil.getBase()+"/weixinapi/sendMessage?m_teyp=text&m_opiden="+person.getNumber()+"&m_module=zhushou&m_contten="+URLEncoder.encode(
									"系统消息："+map.get("content").toString(),"utf-8"), "");
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							logger.error("", e);
						} 
					}
					
				}
			}
			
		}
		 
	}

	@Override
	public void insertInfocenter(String title, InfoTypeEnum type,
			String content, RemindTypeEnum remindType, String personId,
			String creatorId, String objId, String url) {
		Infocenter infocenter = new Infocenter();
		infocenter.setTitle(title);
		infocenter.setType(type);
		infocenter.setContent(content);
		infocenter.setRemindType(remindType);
		Person person = new Person();
		person.setId(personId);
		infocenter.setPerson(person);
		Person creator = new Person();
		creator.setId(creatorId);
		infocenter.setCreator(creator);
		infocenter.setObjId(objId);
		infocenter.setUrl(url);
		infocenter.setUUID();
		infocenter.setCreateDate(new Date());
		this.addEntity(infocenter);
	}
	
	@Override
	public void addEntity(Infocenter infocenter) {
		this.infocenterDao.addEntity(infocenter);
		InfoCenterUnread unread=new InfoCenterUnread();
		unread.setId(infocenter.getPerson().getId());
		unread.setLastContent(infocenter.getContent());
		unread.setLastTitle(infocenter.getTitle());
		unread.setNeedOpenDialog(1);
		infoCenterUnreadService.saveNotRead(unread);
	}
	
	@Override
	public void updateEntity(Infocenter infocenter) {
	 
		this.infocenterDao.updateEntity(infocenter);
		
		InfoCenterUnread unread=new InfoCenterUnread();
		unread.setId(infocenter.getPerson().getId());
		unread.setLastContent(infocenter.getContent());
		unread.setLastTitle(infocenter.getTitle());
		infoCenterUnreadService.saveNotRead(unread);
	}

	@Override
	public void saveRemind(Remind remind) {
		infocenterDao.saveRemind(remind);
	}

	@Override
	public int deleteRemind(String id) {
		return infocenterDao.deleteRemindById(id);
	}

}
