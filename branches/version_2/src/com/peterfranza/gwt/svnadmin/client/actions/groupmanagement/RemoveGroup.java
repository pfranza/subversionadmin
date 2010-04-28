package com.peterfranza.gwt.svnadmin.client.actions.groupmanagement;

import net.customware.gwt.dispatch.shared.Action;

import com.peterfranza.gwt.svnadmin.client.actions.MessageResult;

public class RemoveGroup implements Action<MessageResult>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8227327786454342387L;
	
	private String groupName;
	
	public RemoveGroup(String groupName) {
		super();
		this.groupName = groupName;
	}

	protected RemoveGroup() {
		super();
	}
	
	public String getGroupName() {
		return groupName;
	}
	
}
