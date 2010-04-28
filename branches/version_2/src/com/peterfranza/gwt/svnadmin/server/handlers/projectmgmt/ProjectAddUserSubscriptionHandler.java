package com.peterfranza.gwt.svnadmin.server.handlers.projectmgmt;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import com.google.inject.Inject;
import com.peterfranza.gwt.svnadmin.client.actions.MessageResult;
import com.peterfranza.gwt.svnadmin.client.actions.projectmanagement.ProjectAddUserSubscription;
import com.peterfranza.gwt.svnadmin.server.entitydata.UserManager;
import com.peterfranza.gwt.svnadmin.server.repositorydata.RepositoryManager;

public class ProjectAddUserSubscriptionHandler implements ActionHandler<ProjectAddUserSubscription, MessageResult>{

	private RepositoryManager reposManager;
	private UserManager userManager;

	@Inject
	public ProjectAddUserSubscriptionHandler(RepositoryManager reposManager,
			UserManager userManager) {
		this.reposManager = reposManager;
		this.userManager = userManager;
	}
	
	@Override
	public MessageResult execute(ProjectAddUserSubscription arg0,
			ExecutionContext arg1) throws ActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<ProjectAddUserSubscription> getActionType() {
		return ProjectAddUserSubscription.class;
	}

	@Override
	public void rollback(ProjectAddUserSubscription arg0, MessageResult arg1,
			ExecutionContext arg2) throws ActionException {}

}
