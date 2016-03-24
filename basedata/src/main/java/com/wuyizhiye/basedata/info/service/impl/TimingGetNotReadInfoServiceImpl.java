package com.wuyizhiye.basedata.info.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import redis.clients.jedis.Jedis;

import com.wuyizhiye.base.dao.QueryExecutor;
import com.wuyizhiye.base.spring.ApplicationContextAware;
import com.wuyizhiye.base.spring.DataSourceHolder;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.info.model.RedisChatInfo;
import com.wuyizhiye.basedata.info.model.RedisChatInfoSender;
import com.wuyizhiye.basedata.info.model.RedisFlowInfo;
import com.wuyizhiye.basedata.info.model.RedisMsgInfo;
import com.wuyizhiye.basedata.info.model.RemindTypeEnum;
import com.wuyizhiye.basedata.info.service.TimingGetNotReadInfoService;
import com.wuyizhiye.basedata.logoconfig.LogoConfig;
import com.wuyizhiye.basedata.redis.dao.RedisDao;
import com.wuyizhiye.basedata.redis.serialize.TransCoderUtil;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.basedata.util.RedisHolder;
import com.wuyizhiye.basedata.util.RedisUtil;
import com.wuyizhiye.basedata.util.SystemUtil;

/**
 * @ClassName TimingGetNotReadInfoServiceImpl
 * @Description 定时得到未读的消息Service
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="timingGetNotReadInfoService")
public class TimingGetNotReadInfoServiceImpl implements TimingGetNotReadInfoService {

	private static Logger log =Logger.getLogger(TimingGetNotReadInfoServiceImpl.class); 
	@Autowired
	private QueryExecutor queryExecutor;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	@Override
	@Transactional
	public void getData() {
		
		try {
			List<String> dclist = SystemUtil.getDataSourceSingleList();
			for(int i=0;i<dclist.size();i++){
				DataSourceHolder.setDataSource(dclist.get(i));
				
				Map<String,HashMap<String,Object>> infomap = new HashMap<String,HashMap<String,Object>>();
				
				Boolean setDataInRedis = false;
				String TIMINGREADMSGMETHOD = ParamUtils.getParamValue("TIMINGREADMSGMETHOD");
				if(!StringUtils.isEmpty(TIMINGREADMSGMETHOD) && "redis".equalsIgnoreCase(TIMINGREADMSGMETHOD)){
					setDataInRedis = true;
				}
				
				if(setDataInRedis){	//如果是从redis读取的话，才启用，否则不启用
					/*************判断是否需要去请求数据 start*************/
					/********************判断上次存放数据的时间是不是为空，或者，是不是本次请求的2秒之后
					 * 如果是为空，或者，和上次请求的时间不超过两秒的话，则直接返回，不用去刷新redis里面的数据
					 * ************************/
					RedisDao redisDao = RedisHolder.getRedisClient();
					//将请求时间加入进去
					Date currentRequestTime = new Date();
					Date lastRequestTime = (Date) redisDao.getByteObject(SystemUtil.getCustomerOnlySign()+"REQUESTTIME", Date.class);
					if(null != lastRequestTime && DateUtil.getDifferSecound(currentRequestTime, lastRequestTime) <2){
						return;
					} 
					redisDao.addByteRedisInfo(SystemUtil.getCustomerOnlySign()+"REQUESTTIME",currentRequestTime,Date.class);
					/******************** 判断是否需要去请求数据 end ***********************/
				}
				Map<String,Object> param = new HashMap<String, Object>();
				String dataBaseType = "";
				try {
					dataBaseType = jdbcTemplate.getDataSource().getConnection().getMetaData().getDatabaseProductName();
				} catch (SQLException e) {
					log.error("", e);
					dataBaseType = "ORACLE";
				}
				param.put("DataBaseType", dataBaseType.toUpperCase());
				
				LogoConfig logoConfig = null;
				List<LogoConfig> cfglist=queryExecutor.execQuery("com.wuyizhiye.basedata.dao.LogoConfigDao.getLogoConfigByCon", null, LogoConfig.class);
				if(null != cfglist && cfglist.size()>0){
					logoConfig = cfglist.get(0);
				}
				
				if(logoConfig!=null && logoConfig.getToolCheck().indexOf("XXZX")!=-1){
					//消息中心中未读的数据
					List<RedisMsgInfo> redisMsgInfoList = this.queryExecutor.execQuery("com.wuyizhiye.basedata.info.dao.TimingGetNotReadInfoDao.getMsgInfo", 
							param, RedisMsgInfo.class);
					this.dealMsgData(redisMsgInfoList);
					
					for(RedisMsgInfo rs : redisMsgInfoList){
					HashMap<String,Object> data = null;
					if(null!=infomap.get(rs.getPersonId())){
						data = infomap.get(rs.getPersonId());
					}else{
						data = new HashMap<String,Object>();
					}		
					data.put("PERSONID", rs.getPersonId());
					data.put("UNREADCOUNT", rs.getMsgCount());
					data.put("UNREADCONTENT", rs.getMsgLastContent());
					data.put("UNREADTITLE", rs.getMsgTitle());	//标题
					data.put("MSGNEEDFLOATDLG", rs.getMsgNeedFloatDlg());	//是否需要弹窗
					infomap.put(rs.getPersonId(), data);
					}
				}
				
				if(logoConfig!=null && logoConfig.getToolCheck().indexOf("QYLC")!=-1){
					//流程中心中未读的数据
					List<RedisFlowInfo> redisFlowInfoList = this.queryExecutor.execQuery("com.wuyizhiye.basedata.info.dao.TimingGetNotReadInfoDao.getFlowInfo", 
							param, RedisFlowInfo.class);
					for(RedisFlowInfo rs : redisFlowInfoList){
						
						HashMap<String,Object> data = null;
						if(null!=infomap.get(rs.getPersonId())){
							data = infomap.get(rs.getPersonId());
						}else{
							data = new HashMap<String,Object>();
						}		
						data.put("PERSONID", rs.getPersonId());
						data.put("WORKFLOWCOUNT", rs.getFlowCount());
						infomap.put(rs.getPersonId(), data);						
					}
				}
				
				if(logoConfig!=null && logoConfig.getToolCheck().indexOf("DJLL")!=-1){
					//得到聊聊中未读的数据
					List<RedisChatInfoSender>  redisChatInfoSenderList = this.queryExecutor.execQuery("com.wuyizhiye.basedata.info.dao.TimingGetNotReadInfoDao.getChatInfo", 
							param, RedisChatInfoSender.class);
					List<RedisChatInfo> redisChatInfoList = this.combineChatList(redisChatInfoSenderList);
					for(RedisChatInfo rs : redisChatInfoList){
						
						HashMap<String,Object> data = null;
						if(null!=infomap.get(rs.getPersonId())){
							data = infomap.get(rs.getPersonId());
						}else{
							data = new HashMap<String,Object>();
						}		
						data.put("PERSONID", rs.getPersonId());
						data.put("CCHATCOUNT", rs.getTotalMsgCount());
						data.put("CCHATCONTENT", JSONArray.fromObject(rs.getSenderList()).toString());
						infomap.put(rs.getPersonId(), data);	
					}
					
				}
				
				Map<String,Object> notReadMsgMap = new HashMap<String, Object>();
				
log.error("infomapSize="+infomap.size());		
				Set<String> keyset = infomap.keySet();
				Iterator<String> ite = keyset.iterator();
				if(!setDataInRedis){
					while(ite.hasNext()){
						String key = ite.next();
							//如果不是设置到redis的话，则将这些数据缓存到map里面去，后面插入到数据库
							notReadMsgMap.put(key, infomap.get(key));	//将信息放入到未读消息的map中					
					}
				}else{
					RedisUtil redisUtil = ApplicationContextAware.getApplicationContext().getBean(RedisUtil.class);
					if(!redisUtil.useRedis()) return;
					Jedis jedis = redisUtil.getConnection();
					while(ite.hasNext()){		
						String key = ite.next();
						jedis.set(TransCoderUtil.serialString(SystemUtil.getCustomerOnlySign()+key), TransCoderUtil.serial(infomap.get(key), HashMap.class));					
					}
					redisUtil.closeConnection(jedis);
				}
//log.error("定时取消息的数据！！！");
				if(!setDataInRedis){	//如果插入到数据库
//log.error("insert into DB");
					this.queryExecutor.executeDelete("com.wuyizhiye.basedata.info.dao.TimingGetNotReadInfoDao.deleteNotReadMsgInfo", "");	//删除所有的数据
					Set<String> notReadMsgMapSet = notReadMsgMap.keySet();
					Iterator<String> notReadMsgMapIter = notReadMsgMapSet.iterator();
log.error("dataSize:"+notReadMsgMap.size());
					while(notReadMsgMapIter.hasNext()){
						String key = notReadMsgMapIter.next();
						Map<String,Object> tmpMap = (Map<String, Object>) notReadMsgMap.get(key);
						tmpMap.put("ID", UUID.randomUUID().toString());
						//插入数据库
						this.queryExecutor.executeInsert("com.wuyizhiye.basedata.info.dao.TimingGetNotReadInfoDao.insertNotReadMsgInfo", tmpMap);
					}
				} 
			}
		}finally{
			//执行完了之后，再将其2秒之后执行数据
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					try{
					    TimingGetNotReadInfoService 
					    	timingGetNotReadInfoService = ApplicationContextAware.getApplicationContext().getBean(TimingGetNotReadInfoService.class); 
					    timingGetNotReadInfoService.getData();
					}catch(Exception e){
						log.error("==MsgCountTask==ERROR=="+e.getMessage());
					}
				}
			}, 1000*2);
		}
	}
	
	/**
	 * 处理消息
	 * @param redisMsgInfoList
	 */
	private void dealMsgData(List<RedisMsgInfo> redisMsgInfoList){
		if(null != redisMsgInfoList && redisMsgInfoList.size() > 0){
			for(RedisMsgInfo tmpInfo : redisMsgInfoList){
				String lastContentAllData =tmpInfo.getLastContentAllData();
				if(StringUtils.isNotNull(lastContentAllData)){
					String[] tmpArr = lastContentAllData.split("★");
					if(tmpArr.length >= 3){
						tmpInfo.setMsgNeedFloatDlg(RemindTypeEnum.FLOAT_BOX.toString().equalsIgnoreCase(tmpArr[0])?1:0);
						tmpInfo.setMsgTitle(tmpArr[1]);
						tmpInfo.setMsgLastContent(tmpArr[2]);
					} else {
						tmpInfo.setMsgLastContent(lastContentAllData);
					}
				}
			}
		}
	}
	
	/**
	 * 组装聊聊的主体信息表
	 * @param redisChatInfoSenderList
	 * @return
	 */
	private List<RedisChatInfo> combineChatList(List<RedisChatInfoSender>  redisChatInfoSenderList){
		List<RedisChatInfo> chatInfoList = new ArrayList<RedisChatInfo>();
		if(null != redisChatInfoSenderList && redisChatInfoSenderList.size() > 0){
			for(RedisChatInfoSender tmpSender : redisChatInfoSenderList){
				RedisChatInfo data = this.judgeHasExist(tmpSender, chatInfoList);
				if(null != data){
					List<RedisChatInfoSender> details = data.getSenderList();
					details.add(tmpSender);
					data.setTotalMsgCount(data.getTotalMsgCount()+tmpSender.getNotReadCount());	//设置所有未读的数量
					data.setSenderList(details);
					chatInfoList.add(data);
				} else {
					RedisChatInfo redisChatInfo = new RedisChatInfo();
					redisChatInfo.setTotalMsgCount(tmpSender.getNotReadCount());	//设置未读的数量
					redisChatInfo.setPersonId(tmpSender.getReceivePersonId());
					List<RedisChatInfoSender> details = new ArrayList<RedisChatInfoSender>();
					details.add(tmpSender);
					redisChatInfo.setSenderList(details);
					chatInfoList.add(redisChatInfo);
				}
			}
		}
		
		return chatInfoList;
	}
	
	/**
	 * 判断是否在集合中存在
	 * @param tmpSender
	 * @param chatInfoList
	 * @return
	 */
	private RedisChatInfo judgeHasExist(RedisChatInfoSender tmpSender, List<RedisChatInfo> chatInfoList){
		if(null != chatInfoList && chatInfoList.size() > 0){
			for(RedisChatInfo chatInfo : chatInfoList){
				if(chatInfo.getPersonId().equalsIgnoreCase(tmpSender.getReceivePersonId())){	
					return chatInfo;
				}
			}
		}
		return null;
	}
}
