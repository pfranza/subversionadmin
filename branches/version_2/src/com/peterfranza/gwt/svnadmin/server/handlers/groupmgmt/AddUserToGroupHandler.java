package com.peterfranza.gwt.svnadmin.server.handlers.groupmgmt;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import com.google.inject.Inject;
import com.peterfranza.gwt.svnadmin.client.actions.MessageResult;
import com.peterfranza.gwt.svnadmin.client.actions.groupmanagement.AddUserToGroup;
import com.peterfranza.gwt.svnadmin.server.entitydata.GroupManager;
import com.peterfranza.gwt.svnadmin.server.entitydata.UserManager;

public class AddUserToGroupHandler implements ActionHandler<AddUserToGroup, MessageResult>{

	private GroupManager groupManager;
	private UserManager userManager;

	@Inject
	public AddUserToGroupHandler(GroupManager groupManager,
			UserManager userManager) {
		this.groupManager = groupManager;
		this.userManager = userManager;
	}
	
	@Override
	public MessageResult execute(AddUserToGroup arg0, ExecutionContext arg1)
			throws ActionException {
		groupManager.addMemberToGroup(arg0.getGroupName(), 
				userManager.getUserForName(arg0.getUserName()));
		return new MessageResult("", "Member Added");
	}

	@Override
	public Class<AddUserToGroup> getActionType() {
		return AddUserToGroup.class;
	}

	@Override
	public void rollback(AddUserToGroup arg0, MessageResult arg1,
			ExecutionContext arg2) throws ActionException {}

}
