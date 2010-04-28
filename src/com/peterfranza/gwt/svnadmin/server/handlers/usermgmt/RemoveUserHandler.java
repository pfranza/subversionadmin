package com.peterfranza.gwt.svnadmin.server.handlers.usermgmt;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import com.google.inject.Inject;
import com.peterfranza.gwt.svnadmin.client.actions.MessageResult;
import com.peterfranza.gwt.svnadmin.client.actions.usermanagement.RemoveUser;
import com.peterfranza.gwt.svnadmin.server.entitydata.UserManager;

public class RemoveUserHandler implements ActionHandler<RemoveUser, MessageResult>{

	private UserManager userManager;

	@Inject
	public RemoveUserHandler(UserManager userManager) {
		this.userManager = userManager;
	}
	
	@Override
	public MessageResult execute(RemoveUser arg0, ExecutionContext arg1)
			throws ActionException {
		userManager.removeUser(arg0.getUsername());
		return new MessageResult("", "User " + arg0.getUsername() + " removed");
	}

	@Override
	public Class<RemoveUser> getActionType() {
		return RemoveUser.class;
	}

	@Override
	public void rollback(RemoveUser arg0, MessageResult arg1,
			ExecutionContext arg2) throws ActionException {}

}
