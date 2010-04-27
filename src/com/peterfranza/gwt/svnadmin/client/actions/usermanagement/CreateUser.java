package com.peterfranza.gwt.svnadmin.client.actions.usermanagement;

import net.customware.gwt.dispatch.shared.Action;

import com.peterfranza.gwt.svnadmin.client.actions.MessageResult;

public class CreateUser implements Action<MessageResult>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2182983902206898125L;

	private String username;

	public CreateUser(String username) {
		super();
		this.username = username;
	}
	
	protected CreateUser() {
	}
	
	public String getUsername() {
		return username;
	}
	
}
