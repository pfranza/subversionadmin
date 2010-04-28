package com.peterfranza.gwt.svnadmin.client.actions.groupmanagement;

import net.customware.gwt.dispatch.shared.Action;

import com.peterfranza.gwt.svnadmin.client.actions.MessageResult;

public class AddUserToGroup implements Action<MessageResult>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7691356649239472199L;
	
	private String groupName;
	private String userName;
	
	public AddUserToGroup(String groupName, String userName) {
		super();
		this.groupName = groupName;
		this.userName = userName;
	}

	protected AddUserToGroup() {
		super();
	}
	
	public String getGroupName() {
		return groupName;
	}
	
	public String getUserName() {
		return userName;
	}
	
}
