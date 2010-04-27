package com.peterfranza.gwt.svnadmin.client.actions.usermanagement;

import net.customware.gwt.dispatch.shared.Action;

import com.peterfranza.gwt.svnadmin.client.actions.MessageResult;

public class RemoveUser implements Action<MessageResult>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5575111852400233197L;
	
	private String username;

	public RemoveUser(String username) {
		super();
		this.username = username;
	}
	
	protected RemoveUser() {
	}
	
	public String getUsername() {
		return username;
	}
}
