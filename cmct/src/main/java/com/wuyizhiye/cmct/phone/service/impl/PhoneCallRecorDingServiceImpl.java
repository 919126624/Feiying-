package com.wuyizhiye.cmct.phone.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.base.util.SystemConfig;
import com.wuyizhiye.cmct.phone.dao.PhoneCallRecorDingDao;
import com.wuyizhiye.cmct.phone.model.PhoneCallRecorDing;
import com.wuyizhiye.cmct.phone.service.PhoneCallRecorDingService;

/**
 * @ClassName PhoneCallRecorDingServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="phoneCallRecorDingService")
@Transactional
public class PhoneCallRecorDingServiceImpl extends BaseServiceImpl<PhoneCallRecorDing> implements PhoneCallRecorDingService {
	@Autowired
	private PhoneCallRecorDingDao phoneCallRecorDingDao;
	@Override
	protected BaseDao getDao() {
		return phoneCallRecorDingDao;
	}
	
	/**
	 * 批量转存音频文件
	 */
	@Override
	@Transactional
	public Map<String, Object> batchTurnSave(String[] ids, String year,
			String month) {
		
		Map<String, Object>rest=new HashMap<String, Object>();
		/**
		 * 设置文件夹
		 */
		File voiceFile = new File(SystemConfig.getParameter("voice_path"));
		
		if(!voiceFile.exists() && !voiceFile.isDirectory()){
			voiceFile.mkdirs();
		}
		
		File yearFile = new File(voiceFile.getPath() + voiceFile.separator + year);
		if(!yearFile.exists() && !yearFile.isDirectory()){
			yearFile.mkdirs();
		}
		
		File monthFile = new File(yearFile.getPath() + yearFile.separator + month);
		if(!monthFile.exists() && !monthFile.isDirectory()){
			monthFile.mkdirs();
		}
		List<PhoneCallRecorDing>pcrds=new ArrayList<PhoneCallRecorDing>();
		for(String id:ids){
			PhoneCallRecorDing pcr=this.phoneCallRecorDingDao.getEntityById(id);
			if(!StringUtils.isEmpty(pcr.getSessionId())){
				String fileName=pcr.getSessionId()+".wav";
				try {
					//VoiceUtil.copy(pcr.getSlowRdUrl(), monthFile.getPath()+monthFile.separator+fileName);
					pcr.setDownStatus("YES");//已下载
					pcr.setCallRdUrl("/voice/"+year+"/"+month+"/"+fileName);
					pcrds.add(pcr);
				} catch (Exception e) {
					rest.put("STATE", "FAIL");
					rest.put("MSG", e.getMessage());
					e.printStackTrace();
				}
			}
		}
		if(null!=pcrds && pcrds.size()>0){
			this.phoneCallRecorDingDao.updateBatch(pcrds);
			rest.put("STATE", "SUCCESS");
			rest.put("MSG", "成功转存"+pcrds.size()+"个文件");
		}else{
			rest.put("STATE", "FAIL");
		}
		return rest;
	}	
}