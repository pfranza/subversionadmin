package com.peterfranza.gwt.svnadmin.server.handlers.usermgmt;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import com.google.inject.Inject;
import com.peterfranza.gwt.svnadmin.client.actions.usermanagement.FetchUserDetails;
import com.peterfranza.gwt.svnadmin.client.actions.usermanagement.FetchUserDetails.UserDetails;
import com.peterfranza.gwt.svnadmin.server.entitydata.User;
import com.peterfranza.gwt.svnadmin.server.entitydata.UserManager;

public class FetchUserDetailsHandler implements ActionHandler<FetchUserDetails, FetchUserDetails.UserDetails>{

	private UserManager userManager;

	@Inject
	public FetchUserDetailsHandler(UserManager userManager) {
		this.userManager = userManager;
	}
	
	@Override
	public UserDetails execute(FetchUserDetails arg0, ExecutionContext arg1)
			throws ActionException {
		User u = userManager.getUserForName(arg0.getUsername());
		return new UserDetails(u.getName(), u.getEmailAddress(), u.isAdministrator());
	}

	@Override
	public Class<FetchUserDetails> getActionType() {
		return FetchUserDetails.class;
	}

	@Override
	public void rollback(FetchUserDetails arg0, UserDetails arg1,
			ExecutionContext arg2) throws ActionException {}

}
