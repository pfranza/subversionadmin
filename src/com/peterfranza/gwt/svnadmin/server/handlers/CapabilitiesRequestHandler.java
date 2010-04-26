package com.peterfranza.gwt.svnadmin.server.handlers;

import com.peterfranza.gwt.svnadmin.client.actions.CapabilitiesRequest;
import com.peterfranza.gwt.svnadmin.client.actions.CapabilitiesResult;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

public class CapabilitiesRequestHandler implements ActionHandler<CapabilitiesRequest, CapabilitiesResult>{

	@Override
	public CapabilitiesResult execute(CapabilitiesRequest arg0,
			ExecutionContext arg1) throws ActionException {
		CapabilitiesResult result = new CapabilitiesResult();
		result.setLocalAccounts(true);
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
