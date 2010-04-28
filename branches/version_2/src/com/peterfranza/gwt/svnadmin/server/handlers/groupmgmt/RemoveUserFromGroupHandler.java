package com.peterfranza.gwt.svnadmin.server.handlers.groupmgmt;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import com.google.inject.Inject;
import com.peterfranza.gwt.svnadmin.client.actions.MessageResult;
import com.peterfranza.gwt.svnadmin.client.actions.groupmanagement.RemoveUserFromGroup;
import com.peterfranza.gwt.svnadmin.server.entitydata.GroupManager;
import com.peterfranza.gwt.svnadmin.server.entitydata.UserManager;

public class RemoveUserFromGroupHandler implements ActionHandler<RemoveUserFromGroup, MessageResult>{

	private GroupManager groupManager;
	private UserManager userManager;

	@Inject
	public RemoveUserFromGroupHandler(GroupManager groupManager,
			UserManager userManager) {
		this.groupManager = groupManager;
		this.userManager = userManager;
	}
	
	@Override
	public MessageResult execute(RemoveUserFromGroup arg0, ExecutionContext arg1)
			throws ActionException {
		groupManager.removeMemberFromGroup(arg0.getGroupName(), 
				userManager.getUserForName(arg0.getUserName()));
		return new MessageResult("", "Member Removed");
	}

	@Override
	public Class<RemoveUserFromGroup> getActionType() {
		return RemoveUserFromGroup.class;
	}

	@Override
	public void rollback(RemoveUserFromGroup arg0, MessageResult arg1,
			ExecutionContext arg2) throws ActionException {}

}
