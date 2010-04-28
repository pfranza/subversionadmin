package com.peterfranza.gwt.svnadmin.server.handlers.usermgmt;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import com.google.inject.Inject;
import com.peterfranza.gwt.svnadmin.client.actions.MessageResult;
import com.peterfranza.gwt.svnadmin.client.actions.usermanagement.ChangeUserPassword;
import com.peterfranza.gwt.svnadmin.server.entitydata.UserManager;

public class ChangeUserPasswordHandler implements ActionHandler<ChangeUserPassword, MessageResult>{

	private UserManager userManager;

	@Inject
	public ChangeUserPasswordHandler(UserManager userManager) {
		this.userManager = userManager;
	}

	@Override
	public MessageResult execute(ChangeUserPassword arg0, ExecutionContext arg1)
			throws ActionException {
		userManager.setPassword(arg0.getUsername(), arg0.getPassword());
		return new MessageResult("", arg0.getUsername() + " password changed.");
	}

	@Override
	public Class<ChangeUserPassword> getActionType() {
		return ChangeUserPassword.class;
	}

	@Override
	public void rollback(ChangeUserPassword arg0, MessageResult arg1,
			ExecutionContext arg2) throws ActionException {}
	
}
