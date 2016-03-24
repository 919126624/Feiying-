package com.wuyizhiye.basedata.code.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.bank.model.City;
import com.wuyizhiye.basedata.code.enums.AreaTypeEnum;
import com.wuyizhiye.basedata.person.model.Person;

/**
 * @ClassName Area
 * @Description  片区（地理，商业，路段） t_bd_area
 * @author li.biao
 * @date 2015-4-2
 */
public class Area extends CoreEntity {
	private static final long serialVersionUID = 5331130166177550649L;
	private String name; // 片区名
	private String fullPinyin; // 全拼
	private String simplePinyin; // 简拼
	private String number; // 片区编号
	private String description; // 片区描述
	private AreaTypeEnum areaType; //片区类型  1代表城市 2代表大区。 3代表片区。4 代表商圈。
	private Area parent; // 片区父节点
	private String mapx;//园区X坐标
	private String mapy;//园区Y坐标
	private Integer zoom;//缩放级别
	private Integer mapoint;//是否地图标点
	private Person pointPerson;//标点人
	private Date   pointime;//标点时间
	private String longNumber; //长编码
	private String displayName; //显示名称
	private City city;
	public String getName() {
		return name;
	}
	public City getCity() {
		return city;
	}
	public void setCity(City city) {
		this.city = city;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFullPinyin() {
		return fullPinyin;
	}
	public void setFullPinyin(String fullPinyin) {
		this.fullPinyin = fullPinyin;
	}
	public String getSimplePinyin() {
		return simplePinyin;
	}
	public void setSimplePinyin(String simplePinyin) {
		this.simplePinyin = simplePinyin;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public AreaTypeEnum getAreaType() {
		return areaType;
	}
	public void setAreaType(AreaTypeEnum areaType) {
		this.areaType = areaType;
	}
	public Area getParent() {
		return parent;
	}
	public void setParent(Area parent) {
		this.parent = parent;
	}
	public String getMapx() {
		return mapx;
	}
	public void setMapx(String mapx) {
		this.mapx = mapx;
	}
	public String getMapy() {
		return mapy;
	}
	public void setMapy(String mapy) {
		this.mapy = mapy;
	}
	public Integer getMapoint() {
		return mapoint;
	}
	public void setMapoint(Integer mapoint) {
		this.mapoint = mapoint;
	}
	public Person getPointPerson() {
		return pointPerson;
	}
	public void setPointPerson(Person pointPerson) {
		this.pointPerson = pointPerson;
	}
	public Date getPointime() {
		return pointime;
	}
	public void setPointime(Date pointime) {
		this.pointime = pointime;
	}
	public Integer getZoom() {
		return zoom;
	}
	public void setZoom(Integer zoom) {
		this.zoom = zoom;
	}
	public String getLongNumber() {
		return longNumber;
	}
	public void setLongNumber(String longNumber) {
		this.longNumber = longNumber;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
}
