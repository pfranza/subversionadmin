package com.peterfranza.gwt.svnadmin.server.handlers.usermgmt;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import com.google.inject.Inject;
import com.peterfranza.gwt.svnadmin.client.actions.MessageResult;
import com.peterfranza.gwt.svnadmin.client.actions.usermanagement.ChangeUserEmail;
import com.peterfranza.gwt.svnadmin.server.entitydata.UserManager;

public class ChangeUserEmailHandler implements ActionHandler<ChangeUserEmail, MessageResult>{

	private UserManager userManager;

	@Inject
	public ChangeUserEmailHandler(UserManager userManager) {
		this.userManager = userManager;
	}

	@Override
	public MessageResult execute(ChangeUserEmail arg0, ExecutionContext arg1)
			throws ActionException {
		userManager.setEmailAddress(arg0.getUsername(), arg0.getEmail());
		return new MessageResult("", arg0.getUsername() + " email changed.");
	}

	@Override
	public Class<ChangeUserEmail> getActionType() {
		return ChangeUserEmail.class;
	}

	@Override
	public void rollback(ChangeUserEmail arg0, MessageResult arg1,
			ExecutionContext arg2) throws ActionException {}
	
}
