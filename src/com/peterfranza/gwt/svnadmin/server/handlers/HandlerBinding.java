package com.peterfranza.gwt.svnadmin.server.handlers;

import com.peterfranza.gwt.svnadmin.server.handlers.projectmgmt.AddProjectHandler;
import com.peterfranza.gwt.svnadmin.server.handlers.projectmgmt.ScanProjectHandler;

import net.customware.gwt.dispatch.server.guice.ActionHandlerModule;

public class HandlerBinding extends ActionHandlerModule {

	@Override
	protected void configureHandlers() {
		bindHandler(CapabilitiesRequestHandler.class);
		bindHandler(AuthenticationHandler.class);
		bindHandler(AddProjectHandler.class);
		bindHandler(ScanProjectHandler.class);
	}

}
