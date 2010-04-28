package com.peterfranza.gwt.svnadmin.server.handlers.usermgmt;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import com.google.inject.Inject;
import com.peterfranza.gwt.svnadmin.client.actions.MessageResult;
import com.peterfranza.gwt.svnadmin.client.actions.usermanagement.CreateUser;
import com.peterfranza.gwt.svnadmin.server.entitydata.UserManager;

public class CreateUserHandler implements ActionHandler<CreateUser, MessageResult>{

	private UserManager userManager;

	@Inject
	public CreateUserHandler(UserManager userManager) {
		this.userManager = userManager;
	}
	
	@Override
	public MessageResult execute(CreateUser arg0, ExecutionContext arg1)
			throws ActionException {
		userManager.createUser(arg0.getUsername());
		return new MessageResult("", "User " + arg0.getUsername() + " created.");
	}

	@Override
	public Class<CreateUser> getActionType() {
		return CreateUser.class;
	}

	@Override
	public void rollback(CreateUser arg0, MessageResult arg1,
			ExecutionContext arg2) throws ActionException {}

}
