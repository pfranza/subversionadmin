package com.peterfranza.gwt.svnadmin.client.actions.groupmanagement;

import net.customware.gwt.dispatch.shared.Action;

import com.peterfranza.gwt.svnadmin.client.actions.MessageResult;

public class RemoveUserFromGroup implements Action<MessageResult>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2781635621813036905L;
	
	private String groupName;
	private String userName;
	
	public RemoveUserFromGroup(String groupName, String userName) {
		super();
		this.groupName = groupName;
		this.userName = userName;
	}

	protected RemoveUserFromGroup() {
		super();
	}
	
	public String getGroupName() {
		return groupName;
	}
	
	public String getUserName() {
		return userName;
	}
}
