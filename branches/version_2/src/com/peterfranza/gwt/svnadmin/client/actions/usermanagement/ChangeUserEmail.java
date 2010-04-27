package com.peterfranza.gwt.svnadmin.client.actions.usermanagement;

import net.customware.gwt.dispatch.shared.Action;

import com.peterfranza.gwt.svnadmin.client.actions.MessageResult;

public class ChangeUserEmail implements Action<MessageResult>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1992825336948318475L;
	
	private String username;
	private String email;
	
	public ChangeUserEmail(String username, String email) {
		super();
		this.username = username;
		this.email = email;
	}
	
	protected ChangeUserEmail() {}
	
	public String getUsername() {
		return username;
	}
	
	public String getEmail() {
		return email;
	}
}
