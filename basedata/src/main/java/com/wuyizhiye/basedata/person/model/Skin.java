package com.wuyizhiye.basedata.person.model;

import com.wuyizhiye.basedata.DataEntity;

/**
 * @ClassName Skin
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public class Skin extends DataEntity{
	
	private static final long serialVersionUID = 4002130462652639382L;
	public static final String MAPPER="com.wuyizhiye.basedata.person.dao.SkinDao";
	
	//人员
	private Person person ;
	
	//皮肤名称
	private String skinName ;

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getSkinName() {
		return skinName;
	}

	public void setSkinName(String skinName) {
		this.skinName = skinName;
	}
	
	
}
