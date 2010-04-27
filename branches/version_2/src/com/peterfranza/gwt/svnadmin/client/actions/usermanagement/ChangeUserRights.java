package com.peterfranza.gwt.svnadmin.client.actions.usermanagement;

import net.customware.gwt.dispatch.shared.Action;

import com.peterfranza.gwt.svnadmin.client.actions.MessageResult;

public class ChangeUserRights implements Action<MessageResult>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3034771881705765507L;
	
	private String username;
	private boolean isAdmin;
	
	public ChangeUserRights(String username, boolean isAdmin) {
		super();
		this.username = username;
		this.isAdmin = isAdmin;
	}
	
	protected ChangeUserRights() {
	}
	
	public String getUsername() {
		return username;
	}
	
	public boolean isAdmin() {
		return isAdmin;
	}
}
