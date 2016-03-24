package com.wuyizhiye.cmct.sms.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.person.dao.PersonDao;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.cmct.sms.dao.MessageGiveDao;
import com.wuyizhiye.cmct.sms.dao.SMSControlDao;
import com.wuyizhiye.cmct.sms.dao.SMSControlHistoryDao;
import com.wuyizhiye.cmct.sms.enums.SMSControlTypeEnum;
import com.wuyizhiye.cmct.sms.enums.SMSOperationTypeEnum;
import com.wuyizhiye.cmct.sms.model.MessageGive;
import com.wuyizhiye.cmct.sms.model.SMSControl;
import com.wuyizhiye.cmct.sms.model.SMSControlHistory;
import com.wuyizhiye.cmct.sms.service.MessageGiveService;

/**
 * @ClassName MessageGiveServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="cmctMessageGiveService")
@Transactional
public class MessageGiveServiceImpl extends BaseServiceImpl<MessageGive> implements MessageGiveService {
	@Autowired
	private MessageGiveDao messageGiveDao;
	
	@Autowired
	private SMSControlDao smsControlDao;
	
	@Autowired
	private SMSControlHistoryDao smsControlHistoryDao;//短信操作记录
	
	@Autowired
	private PersonDao personDao;
	
	@Override
	protected BaseDao getDao() {
		return messageGiveDao;
	}
	@Override
	public void addEntity(MessageGive entity) {
		Person give=SystemUtil.getCurrentUser();
		Person get=personDao.getEntityById(entity.getGeter().getId());
		if(!give.getId().equals(get.getId())){//赠送人  和 接收人 不相同
			entity.setGiver(give);
			entity.setGiveDate(new Date());
	        SMSControl giver=getPersonSMS(give.getId());//赠送人 短信控制信息
	        SMSControl geter=getPersonSMS(get.getId());//接收人短信控制信息
	        /**
	         * 由于 暂定 只有  余额控制的 才能进行 段性 赠予
	         */
	        int giveNum=giver.getBalanceAmout()-entity.getGiveNum();//赠送人  短信余额减去
	        int getNum=geter.getBalanceAmout()+entity.getGiveNum();//接收人短信余额 加上
	        giver.setBalanceAmout(giveNum);
	        geter.setBalanceAmout(getNum);
	        
	        /**
	         * 各自 修改 其短信控制 信息
	         */
	        smsControlDao.updateEntity(giver);
	        smsControlDao.updateEntity(geter);
	        super.addEntity(entity);
	        
	        /**
	         * 写短信操作记录
	         */
	        SMSControlHistory gic=new SMSControlHistory();//赠送者操作记录
	        gic.setId(UUID.randomUUID().toString());
	        gic.setObjectId(give.getId());
	        gic.setCreateTime(new Date());
	        gic.setCreator(give);
	        gic.setOrg(SystemUtil.getCurrentOrg());
	        gic.setControlId(giver.getId());//对应短信控制ID
	        gic.setOperationtype(SMSOperationTypeEnum.GIVE_MESSAGE);//操作类型  赠送短信
	        gic.setObjectNumber(give.getNumber());
	        gic.setObjectName(give.getName());
	        gic.setBalanceAmout(giveNum);//余额条数
	        gic.setDescription("短信赠送");
	        this.smsControlHistoryDao.addEntity(gic);
	        
	        SMSControlHistory gec=new SMSControlHistory();//获赠者操作记录
	        gec.setId(UUID.randomUUID().toString());
	        gec.setObjectId(get.getId());
	        gec.setCreateTime(new Date());
	        gec.setCreator(give);
	        gec.setOrg(SystemUtil.getCurrentOrg());
	        gec.setControlId(geter.getId());//对应短信控制ID
	        gec.setOperationtype(SMSOperationTypeEnum.RECEIVE_MESSAGE);//操作类型  赠送短信
	        gec.setObjectNumber(get.getNumber());
	        gec.setObjectName(get.getName());
	        gec.setBalanceAmout(getNum);//余额条数
	        gec.setDescription("获取赠送短信");
	        this.smsControlHistoryDao.addEntity(gec);
		}
	}	
	
	/**
	 * 取对应人员的  短信控制信息
	 * @param id
	 * @return
	 */
	public SMSControl getPersonSMS(String id){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("controlType", SMSControlTypeEnum.PERSONAL_TYPE);//控制类型 个人
		params.put("objectId", id);//控制类型 个人
		List<SMSControl> list = queryExecutor.execQuery("com.wuyizhiye.interflow.sms.dao.SMSControlDao.getSMSControlByCond", params, SMSControl.class);
		if(null != list && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	
}