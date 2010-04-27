package com.peterfranza.gwt.svnadmin.server.handlers;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import com.google.inject.Inject;
import com.peterfranza.gwt.svnadmin.client.actions.AuthenticationRequest;
import com.peterfranza.gwt.svnadmin.client.actions.AuthenticationRequest.AuthenticationResult;
import com.peterfranza.gwt.svnadmin.server.entitydata.User;
import com.peterfranza.gwt.svnadmin.server.entitydata.UserManager;

public class AuthenticationHandler implements ActionHandler<AuthenticationRequest, AuthenticationResult>{

	private UserManager userManager;

	@Inject
	public AuthenticationHandler(UserManager userManager) {
		this.userManager = userManager;
	}
	
	@Override
	public AuthenticationResult execute(AuthenticationRequest arg0,
			ExecutionContext arg1) throws ActionException {

		if(!adminsExists()) {
			if(arg0.getUsername().equals("admin") && 
				arg0.getPassword().equals("admin")) {
				return new AuthenticationResult(true, true);
			}
		}
		
		if(userManager.authenticate(arg0.getUsername(), arg0.getPassword())) {
			User u = userManager.getUserForName(arg0.getUsername());
			if(u != null) {
				return new AuthenticationResult(true, u.isAdministrator());
			}
		} 
		return new AuthenticationResult(false, false);
	}

	private boolean adminsExists() {
		for(User u: userManager.getUsers()) {
			if(u.isAdministrator()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Class<AuthenticationRequest> getActionType() {
		return AuthenticationRequest.class;
	}

	@Override
	public void rollback(AuthenticationRequest arg0, AuthenticationResult arg1,
			ExecutionContext arg2) throws ActionException {}

}
