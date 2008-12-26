package com.gorthaur.svnadmin.client.rpcinterface.beans;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UserInfo implements IsSerializable, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1719126603082446519L;
	
	private String name;
	private String email;
	private boolean isAdmin;

	private String password = null;

	private String subscriptions;

	private String groups;
	
	public UserInfo() {}

	public UserInfo(String name, String email, boolean isAdmin, String subscriptions, String groups) {
		super();
		this.name = name;
		this.email = email;
		this.isAdmin = isAdmin;
		this.subscriptions = subscriptions;
		this.groups = groups;
	}

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public final String getEmail() {
		return email;
	}

	public final void setEmail(String email) {
		this.email = email;
	}

	public final boolean isAdmin() {
		return isAdmin;
	}

	public final void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	public final void setNewPassword(String passwd) {
		this.password = passwd;
	}
	
	public final String getNewPassword() {
		return password;
	}

	public String getSubscriptionsCSVString() {
		return subscriptions;
	}

	public String getGroupMembershipCSVString() {
		return groups;
	}
	
	

}
