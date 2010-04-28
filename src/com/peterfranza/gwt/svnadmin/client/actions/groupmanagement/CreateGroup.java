package com.peterfranza.gwt.svnadmin.client.actions.groupmanagement;

import net.customware.gwt.dispatch.shared.Action;

import com.peterfranza.gwt.svnadmin.client.actions.MessageResult;

public class CreateGroup implements Action<MessageResult>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2628585395089764787L;
	
	private String groupName;
	
	public CreateGroup(String groupName) {
		super();
		this.groupName = groupName;
	}

	protected CreateGroup() {
		super();
	}
	
	public String getGroupName() {
		return groupName;
	}
	
}
