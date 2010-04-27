package com.peterfranza.gwt.svnadmin.client.actions.usermanagement;

import net.customware.gwt.dispatch.shared.Action;

import com.peterfranza.gwt.svnadmin.client.actions.MessageResult;

public class ChangeUserPassword implements Action<MessageResult>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3330059090489962929L;
	
	private String username;
	private String password;
	
	public ChangeUserPassword(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
	protected ChangeUserPassword() {}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
}
