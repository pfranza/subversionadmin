package com.peterfranza.gwt.svnadmin.server.handlers;

import net.customware.gwt.dispatch.server.guice.ActionHandlerModule;

import com.peterfranza.gwt.svnadmin.server.handlers.projectmgmt.AddProjectHandler;
import com.peterfranza.gwt.svnadmin.server.handlers.projectmgmt.ScanProjectHandler;
import com.peterfranza.gwt.svnadmin.server.handlers.usermgmt.ChangeUserEmailHandler;
import com.peterfranza.gwt.svnadmin.server.handlers.usermgmt.ChangeUserPasswordHandler;
import com.peterfranza.gwt.svnadmin.server.handlers.usermgmt.ChangeUserRightsHandler;
import com.peterfranza.gwt.svnadmin.server.handlers.usermgmt.CreateUserHandler;
import com.peterfranza.gwt.svnadmin.server.handlers.usermgmt.FetchUserDetailsHandler;
import com.peterfranza.gwt.svnadmin.server.handlers.usermgmt.ListUsersHandler;
import com.peterfranza.gwt.svnadmin.server.handlers.usermgmt.RemoveUserHandler;

public class HandlerBinding extends ActionHandlerModule {

	@Override
	protected void configureHandlers() {
		bindHandler(CapabilitiesRequestHandler.class);
		bindHandler(AuthenticationHandler.class);
		
		bindHandler(AddProjectHandler.class);
		bindHandler(ScanProjectHandler.class);
		
		bindHandler(ChangeUserEmailHandler.class);
		bindHandler(ChangeUserPasswordHandler.class);
		bindHandler(ChangeUserRightsHandler.class);
		bindHandler(CreateUserHandler.class);
		bindHandler(FetchUserDetailsHandler.class);
		bindHandler(ListUsersHandler.class);
		bindHandler(RemoveUserHandler.class);
	}

}
