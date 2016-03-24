package com.wuyizhiye.basedata.apiCenter.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.common.enums.DataTypeEnum;
import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.base.util.SystemConfig;
import com.wuyizhiye.basedata.apiCenter.dao.APIItemDao;
import com.wuyizhiye.basedata.apiCenter.dao.APIParameterDao;
import com.wuyizhiye.basedata.apiCenter.model.APIItem;
import com.wuyizhiye.basedata.apiCenter.model.APIParameter;
import com.wuyizhiye.basedata.apiCenter.service.APIItemService;
import com.wuyizhiye.basedata.service.impl.DataEntityService;
import com.wuyizhiye.basedata.util.SystemUtil;

/**
 * @ClassName APIItemServiceImpl
 * @Description 接口参数service实现类
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value = "apiItemService")
@Transactional
public class APIItemServiceImpl extends DataEntityService<APIItem>
		implements APIItemService {
	@Autowired
	private APIItemDao apiItemDao;
	@Autowired
	private APIParameterDao apiParameterDao;
	@Override
	protected BaseDao getDao() {
		return apiItemDao;
	}
	
	@Override
	public void addEntity(APIItem entity) {
		if(entity!=null){
			entity.setId(UUID.randomUUID().toString());
			if(entity.getCreateTime()==null){
				entity.setCreateTime(new Date());
			}
			if(entity.getCreator()==null){
				entity.setCreator(SystemUtil.getCurrentUser());
			}
			if(entity.getControlUnit()==null){
				entity.setControlUnit(SystemUtil.getCurrentControlUnit());
			}
			if(entity.getLastUpdateTime()==null){
				entity.setLastUpdateTime(new Date());
			}
			if(entity.getUpdator()==null){
				entity.setUpdator(SystemUtil.getCurrentUser());
			}
			if(entity.getOrg()==null){
				entity.setOrg(SystemUtil.getCurrentOrg());
			}
			
			  String entityJson=entity.getEntityJson();
			  if(!StringUtils.isEmpty(entityJson)){
				  JSONArray arr=JSONArray.fromObject(entityJson);
				  if(arr!=null && arr.size()>0){
					  for(int i=0;i<arr.size();i++){
						  APIParameter p =new APIParameter();
						  p.setId(UUID.randomUUID().toString());
						  p.setItem(entity);
						  p.setType(DataTypeEnum.valueOf(arr.getJSONObject(i).getString("type")));
						  p.setName(arr.getJSONObject(i).getString("name"));
						  p.setDescription(arr.getJSONObject(i).getString("desc"));
						  p.setIdx(arr.getJSONObject(i).getInt("idx"));
						  p.setIsNotNull(arr.getJSONObject(i).getInt("isNotNull"));
						  apiParameterDao.addEntity(p);
					  }
				  }
			  }
		}
		super.addEntity(entity);
	}
	
	@Override
	public void updateEntity(APIItem entity) {
		if(entity!=null && !StringUtils.isEmpty(entity.getId())){
			entity.setLastUpdateTime(new Date());
			entity.setUpdator(SystemUtil.getCurrentUser());
			if(entity.getControlUnit()==null){
				entity.setControlUnit(SystemUtil.getCurrentControlUnit());
			}
			Map<String,Object> param=new HashMap<String,Object>();
			param.put("id", entity.getId());
		   queryExecutor.executeDelete("com.wuyizhiye.basedata.apiCenter.dao.APIParameterDao.deleteByItemId", param);
		   String entityJson=entity.getEntityJson();
		   if(!StringUtils.isEmpty(entityJson)){
			  JSONArray arr=JSONArray.fromObject(entityJson);
			  if(arr!=null && arr.size()>0){
				  for(int i=0;i<arr.size();i++){
					  APIParameter p =new APIParameter();
					  p.setId(UUID.randomUUID().toString());
					  p.setItem(entity);
					  p.setType(DataTypeEnum.valueOf(arr.getJSONObject(i).getString("type")));
					  p.setName(arr.getJSONObject(i).getString("name"));
					  p.setDescription(arr.getJSONObject(i).getString("desc"));
					  p.setIdx(arr.getJSONObject(i).getInt("idx"));
					  p.setIsNotNull(arr.getJSONObject(i).getInt("isNotNull"));
					  apiParameterDao.addEntity(p);
				  }
			  }
		   }
		   super.updateEntity(entity);
		}
		
	}
	@Override
	public void deleteById(String id) {
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("id", id);
	   queryExecutor.executeDelete("com.wuyizhiye.basedata.apiCenter.dao.APIParameterDao.deleteByItemId", param);
		getDao().deleteById(id);
	}
	
	
	public Map apiTest(String id,int idx){
		Map<String,Object> param=new HashMap<String,Object>();
		
//		System.out.println(id);
		param.put("id", id);
		param.put("idx", idx);
		
		return param;
	}
	@Override
	public List<APIItem> getList(Map<String, Object> param) {
		return apiItemDao.getList(param);
	}

	/**
	 * 检查是否有新版本
	 * @param 当前版本
	 * @return version 最新版本号，downloadUrl 下载地址，更新状态
	 */
	@Override
	public Map getVersion(String version) {
		Map dataMap = new HashMap();
		String version_path = SystemConfig.getParameter("version_path");
		if(!StringUtils.isEmpty(version_path)){
			String tempName = version_path.substring(version_path.lastIndexOf("/") + 1, version_path.lastIndexOf("."));
			if(!version.equals(tempName)){
				dataMap.put("version", tempName);
				dataMap.put("downloadUrl",version_path);
				dataMap.put("status", "1");
				return dataMap;
			}
		}
		dataMap.put("version", version);
		dataMap.put("downloadUrl","");
		dataMap.put("status", "0");//无版本更新
		return dataMap;
	}

}