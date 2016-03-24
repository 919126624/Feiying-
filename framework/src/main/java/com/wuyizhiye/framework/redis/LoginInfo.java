package com.wuyizhiye.framework.redis;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;

import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.framework.qqmial.model.QQToken;

/**
 * @ClassName LoginInfo
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
public class LoginInfo implements Serializable{
	
	private static final long serialVersionUID = 230918537239549951L;
	
	private String currentDataSource;
	private Org currentCU;
	private Org currentOrg;
	private Position currentPosition;
	private String currentUserType;
	private Person currentUser;
	private String currentSkin;
	private Set<String> personPermission;
	private Map<String,String> personPermissionMap;
	private String signature;
	private String browser;
	private String singlesource;
	private QQToken qqmail_token;
	private String qqmail_authKey;
	private String roomlistingids;
	private String customerList;
	private String ispass;
	public String getCurrentDataSource() {
		return currentDataSource;
	}
	public void setCurrentDataSource(String currentDataSource) {
		this.currentDataSource = currentDataSource;
	}
	public Org getCurrentCU() {
		return currentCU;
	}
	public void setCurrentCU(Org currentCU) {
		this.currentCU = currentCU;
	}
	public Org getCurrentOrg() {
		return currentOrg;
	}
	public void setCurrentOrg(Org currentOrg) {
		this.currentOrg = currentOrg;
	}
	public Position getCurrentPosition() {
		return currentPosition;
	}
	public void setCurrentPosition(Position currentPosition) {
		this.currentPosition = currentPosition;
	}
	public String getCurrentUserType() {
		return currentUserType;
	}
	public void setCurrentUserType(String currentUserType) {
		this.currentUserType = currentUserType;
	}
	public Person getCurrentUser() {
		return currentUser;
	}
	public void setCurrentUser(Person currentUser) {
		this.currentUser = currentUser;
	}
	
	public Set<String> getPersonPermission() {
		return personPermission;
	}
	public void setPersonPermission(Set<String> personPermission) {
		this.personPermission = personPermission;
	}
	public String getCurrentSkin() {
		return currentSkin;
	}
	public void setCurrentSkin(String currentSkin) {
		this.currentSkin = currentSkin;
	}
	

	public Map<String, String> getPersonPermissionMap() {
		return personPermissionMap;
	}
	public void setPersonPermissionMap(Map<String, String> personPermissionMap) {
		this.personPermissionMap = personPermissionMap;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getBrowser() {
		return browser;
	}
	public void setBrowser(String browser) {
		this.browser = browser;
	}
	public String getSinglesource() {
		return singlesource;
	}
	public void setSinglesource(String singlesource) {
		this.singlesource = singlesource;
	}
	public QQToken getQqmail_token() {
		return qqmail_token;
	}
	public void setQqmail_token(QQToken qqmail_token) {
		this.qqmail_token = qqmail_token;
	}
	public String getQqmail_authKey() {
		return qqmail_authKey;
	}
	public void setQqmail_authKey(String qqmail_authKey) {
		this.qqmail_authKey = qqmail_authKey;
	}
	public String getRoomlistingids() {
		return roomlistingids;
	}
	public void setRoomlistingids(String roomlistingids) {
		this.roomlistingids = roomlistingids;
	}
	public String getCustomerList() {
		return customerList;
	}
	public void setCustomerList(String customerList) {
		this.customerList = customerList;
	}
	public String getIspass() {
		return ispass;
	}
	public void setIspass(String ispass) {
		this.ispass = ispass;
	}
	public static void main(String[] args){
		Set<String> s = new HashSet<String>();
		for(int i=0;i<10;i++){
			s.add(String.valueOf(i));
		}
		System.out.println(JSONArray.fromObject(s));
	}
	
	
}
