package com.peterfranza.gwt.svnadmin.server.entitydata.local;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.peterfranza.gwt.svnadmin.server.entitydata.User;

@Entity
public class HbmUserImpl implements Serializable, User {


	/**
	 * 
	 */
	private static final long serialVersionUID = 8288681953280166737L;

	@GeneratedValue
	@Id
	private long id;
	
	private String name;
	private String password;
	private String email;
	private boolean administrator;
	
	@Override
	public boolean authenticate(String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getEmailAddress() {
		return email;
	}

	@Override
	public boolean isAdministrator() {
		return administrator;
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAdministrator(boolean isAdministrator) {
		this.administrator = isAdministrator;
	}

	@Override
	public void setEmailAddress(String emailAddress) {
		this.email = emailAddress;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getName() {
		return name;
	}
	
	public long getId() {
		return id;
	}

}
