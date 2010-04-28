package com.peterfranza.gwt.svnadmin.server.handlers.usermgmt;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import com.google.inject.Inject;
import com.peterfranza.gwt.svnadmin.client.actions.MessageResult;
import com.peterfranza.gwt.svnadmin.client.actions.usermanagement.ChangeUserRights;
import com.peterfranza.gwt.svnadmin.server.entitydata.UserManager;

public class ChangeUserRightsHandler implements ActionHandler<ChangeUserRights, MessageResult>{

	private UserManager userManager;

	@Inject
	public ChangeUserRightsHandler(UserManager userManager) {
		this.userManager = userManager;
	}

	@Override
	public MessageResult execute(ChangeUserRights arg0, ExecutionContext arg1)
			throws ActionException {
		userManager.setAdministrator(arg0.getUsername(), arg0.isAdmin());
		return new MessageResult("", arg0.getUsername() + " rights changed.");
	}

	@Override
	public Class<ChangeUserRights> getActionType() {
		return ChangeUserRights.class;
	}

	@Override
	public void rollback(ChangeUserRights arg0, MessageResult arg1,
			ExecutionContext arg2) throws ActionException {}
	
}
