package com.peterfranza.gwt.svnadmin.client.actions.groupmanagement;

import net.customware.gwt.dispatch.shared.Action;

import com.peterfranza.gwt.svnadmin.client.actions.usermanagement.ListUsers.UserList;

public class ListUsersInGroup implements Action<UserList>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7901448840683944968L;
	
	private String groupName;

	public ListUsersInGroup(String groupName) {
		super();
		this.groupName = groupName;
	}

	protected ListUsersInGroup() {
		super();
	}
	
	public String getGroupName() {
		return groupName;
	}
	
}
