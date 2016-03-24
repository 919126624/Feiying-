package com.wuyizhiye.basedata.org.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.org.dao.OrgDao;
import com.wuyizhiye.basedata.org.enums.OrgStatusEnum;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.org.service.OrgHistoryService;
import com.wuyizhiye.basedata.org.service.OrgService;
import com.wuyizhiye.basedata.org.service.PositionService;
import com.wuyizhiye.basedata.service.impl.DataEntityService;
import com.wuyizhiye.basedata.util.OrgUtils;

/**
 * @ClassName OrgServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="orgService")
@Transactional
public class OrgServiceImpl extends DataEntityService<Org> implements OrgService {
	@Autowired
	private OrgDao orgDao;
	@Override
	protected BaseDao getDao() {
		return orgDao;
	}
	@Autowired
	private OrgHistoryService orgHistoryService;
	
	@Autowired
	private PositionService positionService;
	
	@Override
	public void addEntity(Org entity) {
		entity.setLongNumber(entity.getNumber());
		entity.setDisplayName(entity.getName());
		entity.setLevel(1);
		entity.setLeaf(true);
		entity.setStatus(OrgStatusEnum.ENABLE);
		DateFormat  timesTampFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timesTampFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		try {
			entity.setDisabledDate(timesTampFormat.parse("2100-01-01 01:01:00"));
		} catch (ParseException e) {
			new  RuntimeException(e);
		}

//		entity.setDisabledDate();
		Org parent = null;
		if(entity.getParent()!=null && !StringUtils.isEmpty(entity.getParent().getId())){
			parent = getEntityById(entity.getParent().getId());
			entity.setLongNumber(parent.getLongNumber() + "!" + entity.getNumber());
			entity.setDisplayName(parent.getDisplayName() + "_" + entity.getName());
			entity.setLevel(parent.getLevel() + 1);
			parent.setLeaf(false);
			super.updateEntity(parent);
		}
		
		//设置组织所在的CU,by lxl 14.11.18
		if(!OrgUtils.isCU(entity) && parent!=null){
			entity.setControlUnit(OrgUtils.getCUByOrg(parent.getId()));
		}
		
//		if(OrgTypeEnum.GROUP.equals(entity.getOrgType()) || OrgTypeEnum.COMPANY.equals(entity.getOrgType())){
//			entity.setControlUnit(entity);
//		}else{
//			if(parent!=null){
//				entity.setControlUnit(parent.getControlUnit());
//			}
//		}
		entity.setUUID();
		super.addEntity(entity);
		
		//如果id不为空,则来自于复制新增,保存职级数据
		if(!StringUtils.isEmpty(entity.getCopyOrgId())){
			List<Position> positions = positionService.getByOrg(entity.getCopyOrgId());
			for(Position po:positions){
				po.setUUID();
				po.setBelongOrg(entity);
			}
			positionService.addBatch(positions);
		}
	}
	
	@Override
	public void updateEntity(Org entity) {
		Org oldEntity = getEntityById(entity.getId());
		entity.setLongNumber(entity.getNumber());
		entity.setDisplayName(entity.getName());
		Org parent = null;
		if(entity.getParent()!=null){
			parent = getEntityById(entity.getParent().getId());
			entity.setLongNumber(parent.getLongNumber() + "!" + entity.getNumber());
			entity.setDisplayName(parent.getDisplayName() + "_" + entity.getName());
			entity.setLevel(parent.getLevel() + 1);
			parent.setLeaf(false);
			super.updateEntity(parent);
		}
		
		//设置组织所在的CU,by lxl 14.11.18
		if(!OrgUtils.isCU(entity) && parent!=null){
			entity.setControlUnit(OrgUtils.getCUByOrg(parent.getId()));
		}else{
			if(OrgUtils.isCU(entity)){
				entity.setControlUnit(null);
			}
		}
//		if(OrgTypeEnum.GROUP.equals(entity.getOrgType()) || OrgTypeEnum.COMPANY.equals(entity.getOrgType())){
//			entity.setControlUnit(entity);
//		}else{
//			if(parent!=null){
//				entity.setControlUnit(parent.getControlUnit());
//			}
//		}
		if(entity.getParent()!=null && !(null==oldEntity.getParent()?"":oldEntity.getParent().getId()).equals(entity.getParent().getId())){
			//判断是否为组织迁移
			if(!(entity.getParent().equals(oldEntity.getParent()))){
				orgHistoryService.addEntity(oldEntity);  //将历时数据迁移到历史组织架构
				updateOrgChild(entity);  //修改下级节点的所有信息
			}
			entity.setEffectiveDate(new Date());
		}
		super.updateEntity(entity);
	}
	
	
	/**
	 * 修改 entity  下级节点的所有信息
	 * @param entity
	 */
	private void updateOrgChild(Org entity){
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("parent", entity.getId());
		List<Org> orgItems = (List<Org>)orgDao.getOrgByOrg(param);
		if (orgItems.size()>0) {
			for (int i = 0; i < orgItems.size(); i++) {
				Org childOrg = orgItems.get(i);
				childOrg.setLongNumber(entity.getLongNumber() + "!" + childOrg.getNumber());
				childOrg.setDisplayName(entity.getDisplayName() + "_" + childOrg.getName());
				childOrg.setLevel(entity.getLevel() + 1);
				childOrg.setEffectiveDate(new Date());
				childOrg.setControlUnit(OrgUtils.getCUByOrg(entity.getId()));
				super.updateEntity(childOrg);
				if(!childOrg.isLeaf()){
					updateOrgChild(childOrg);
				}
			}
		}
	}
	@Override
	public void deleteEntity(Org entity) {
		getDao().deleteEntity(entity);
		orgHistoryService.addEntity(entity);//保存历史
	}
	
	@Override
	public List<Org> autoGetOrgByName(Map<String, Object> param) {
		return  orgDao.autoGetOrgByName(param);
	}
	
	@Override
	public Org getOrgByTypeOrLevel(Map<String, Object> param) {
		return orgDao.getOrgByTypeOrLevel(param);
	}

	@Override
	public Org getParent(String orgId) {
		return this.orgDao.getParent(orgId);
	}
	@Override
	public Org getOrgByType(String number) {
		// TODO Auto-generated method stub
		Map<String,Object> param=new HashMap<String, Object>();
		param.put("orgType", number);
		List<Org>  olist=this.orgDao.getOrgByType(param);
		Org org=null;
		if(null != olist && olist.size()>0){
			for(int i=0;i<olist.size();i++){
				Org o=olist.get(i);
				if(i == 0){
					org=o;
				}else{
					if(o.getLevel()<org.getLevel()){
						org=o;
					}
				}
			}
		}
		return org;
	}

	@Override
	public void updateBatchChildren(Org porg, List<Org> updateChildrens) {
		Map<String, Object> param=new HashMap<String, Object>();
		param.put("number", porg.getNumber());
		param.put("longNumber", porg.getLongNumber());
		param.put("id", porg.getId());
		//更新父组织的长编码
		queryExecutor.executeUpdate("com.wuyizhiye.basedata.org.dao.OrgDao.updateOrgP", param);
		param.clear();//清空掉
		//更新子组织的长编码
		for (int i = 0; i < updateChildrens.size(); i++) {
			Org child=updateChildrens.get(i);
			param.clear();
			param.put("longNumber", child.getLongNumber());
			param.put("id", child.getId());
			queryExecutor.executeUpdate("com.wuyizhiye.basedata.org.dao.OrgDao.updateOrgC", param);
		}
	}
}
