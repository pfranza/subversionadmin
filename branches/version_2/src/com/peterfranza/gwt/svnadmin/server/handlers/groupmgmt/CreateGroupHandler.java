package com.peterfranza.gwt.svnadmin.server.handlers.groupmgmt;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import com.google.inject.Inject;
import com.peterfranza.gwt.svnadmin.client.actions.MessageResult;
import com.peterfranza.gwt.svnadmin.client.actions.groupmanagement.CreateGroup;
import com.peterfranza.gwt.svnadmin.server.entitydata.GroupManager;

public class CreateGroupHandler implements ActionHandler<CreateGroup, MessageResult>{

	private GroupManager groupManager;

	@Inject
	public CreateGroupHandler(GroupManager groupManager) {
		this.groupManager = groupManager;
	}
	
	@Override
	public MessageResult execute(CreateGroup arg0, ExecutionContext arg1)
			throws ActionException {
		groupManager.createGroup(arg0.getGroupName());
		return new MessageResult("", "Group created.");
	}

	@Override
	public Class<CreateGroup> getActionType() {
		return CreateGroup.class;
	}

	@Override
	public void rollback(CreateGroup arg0, MessageResult arg1,
			ExecutionContext arg2) throws ActionException {}

}
