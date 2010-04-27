package com.peterfranza.gwt.svnadmin.server.handlers;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import com.google.inject.Inject;
import com.peterfranza.gwt.svnadmin.client.actions.CapabilitiesRequest;
import com.peterfranza.gwt.svnadmin.client.actions.CapabilitiesRequest.CapabilitiesResult;
import com.peterfranza.gwt.svnadmin.server.entitydata.UserManager;

public class CapabilitiesRequestHandler implements ActionHandler<CapabilitiesRequest, CapabilitiesResult>{

	private UserManager userManager;

	@Inject
	public CapabilitiesRequestHandler(UserManager userManager) {
		this.userManager = userManager;
	}
	
	@Override
	public CapabilitiesResult execute(CapabilitiesRequest arg0,
			ExecutionContext arg1) throws ActionException {
		CapabilitiesResult result = new CapabilitiesResult();
		result.setLocalAccounts(userManager.isMutable());
		return result;
	}

	@Override
	public Class<CapabilitiesRequest> getActionType() {
		return CapabilitiesRequest.class;
	}

	@Override
	public void rollback(CapabilitiesRequest arg0, CapabilitiesResult arg1,
			ExecutionContext arg2) throws ActionException {}

}
