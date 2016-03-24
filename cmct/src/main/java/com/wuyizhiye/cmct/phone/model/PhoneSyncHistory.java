package com.wuyizhiye.cmct.phone.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.person.model.Person;

/**
 * @ClassName PhoneSyncHistory
 * @Description 话单同步历史记录
 * @author li.biao
 * @date 2015-5-26
 */
public class PhoneSyncHistory extends CoreEntity implements Comparable<PhoneSyncHistory>{
	
	private static final long serialVersionUID = 1L;
	
	public static final String MAPPER = "com.wuyizhiye.cmct.phone.dao.PhoneSyncHistoryDao";
	
	//同步人
	private Person person ;
	
	//同步时间
	private Date syncTime ;
	
	//同步开始时间
	private Date startTime ;
	
	//同步结束时间
	private Date endTime ;
	
	//本次同步数量
	private Integer syncCount ;
	
	//同步的组织
	private String syncOrgId ;

	//-----------不对应数据表字段
	private boolean flag;//同步状态,
	private String dateStr;//显示日期
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Date getSyncTime() {
		return syncTime;
	}

	public void setSyncTime(Date syncTime) {
		this.syncTime = syncTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getSyncCount() {
		return syncCount;
	}

	public void setSyncCount(Integer syncCount) {
		this.syncCount = syncCount;
	}

	public String getSyncOrgId() {
		return syncOrgId;
	}

	public void setSyncOrgId(String syncOrgId) {
		this.syncOrgId = syncOrgId;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	@Override
	public int compareTo(PhoneSyncHistory o) {
		if (o == null) {                                                
            try {                                                       
                throw new Exception("该对象为空！");                          
            } catch (Exception e) {                                     
                e.printStackTrace();                                    
            }                                                           
        }                                                               
                                                                        
        if (!(this.getClass().getName().equals(o.getClass().getName()   
                .toString()))) {                                        
            try {                                                       
                throw new Exception("该对象的类名不一致！");                      
            } catch (Exception e) {                                     
                e.printStackTrace();                                    
            }                                                           
        }              
      
        if (!(o instanceof PhoneSyncHistory)) {                                  
            try {                                                       
                throw new Exception("该对象不是PhoneSyncHistory的实例！");                
            } catch (Exception e) {                                     
                e.printStackTrace();                                    
            }                                                           
        } 
        
        PhoneSyncHistory phoneSyncHistory = (PhoneSyncHistory) o;
        return Integer.parseInt(this.getDateStr())-Integer.parseInt(phoneSyncHistory.getDateStr());
	}

}
