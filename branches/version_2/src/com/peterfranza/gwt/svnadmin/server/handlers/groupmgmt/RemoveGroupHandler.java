package com.peterfranza.gwt.svnadmin.server.handlers.groupmgmt;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import com.google.inject.Inject;
import com.peterfranza.gwt.svnadmin.client.actions.MessageResult;
import com.peterfranza.gwt.svnadmin.client.actions.groupmanagement.RemoveGroup;
import com.peterfranza.gwt.svnadmin.server.entitydata.GroupManager;

public class RemoveGroupHandler implements ActionHandler<RemoveGroup, MessageResult>{

	private GroupManager groupManager;

	@Inject
	public RemoveGroupHandler(GroupManager groupManager) {
		this.groupManager = groupManager;
	}
	
	@Override
	public MessageResult execute(RemoveGroup arg0, ExecutionContext arg1)
			throws ActionException {
		groupManager.removeGroup(arg0.getGroupName());
		return new MessageResult("", "Group removed.");
	}

	@Override
	public Class<RemoveGroup> getActionType() {
		return RemoveGroup.class;
	}

	@Override
	public void rollback(RemoveGroup arg0, MessageResult arg1,
			ExecutionContext arg2) throws ActionException {}

}
