package com.peterfranza.gwt.svnadmin.server.handlers.projectmgmt;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import com.google.inject.Inject;
import com.peterfranza.gwt.svnadmin.client.actions.MessageResult;
import com.peterfranza.gwt.svnadmin.client.actions.projectmanagement.ProjectRemoveUserSubscription;
import com.peterfranza.gwt.svnadmin.server.entitydata.UserManager;
import com.peterfranza.gwt.svnadmin.server.repositorydata.RepositoryManager;

public class ProjectRemoveUserSubscriptionHandler implements ActionHandler<ProjectRemoveUserSubscription, MessageResult> {

	private RepositoryManager reposManager;
	private UserManager userManager;

	@Inject
	public ProjectRemoveUserSubscriptionHandler(RepositoryManager reposManager,
			UserManager userManager) {
		this.reposManager = reposManager;
		this.userManager = userManager;
	}
	
	@Override
	public MessageResult execute(ProjectRemoveUserSubscription arg0,
			ExecutionContext arg1) throws ActionException {
		reposManager.unsubscribe(arg0.getProject(), userManager.getUserForName(arg0.getMemberName()));
		return new MessageResult("", "Subscription Removed");
	}

	@Override
	public Class<ProjectRemoveUserSubscription> getActionType() {
		return ProjectRemoveUserSubscription.class;
	}

	@Override
	public void rollback(ProjectRemoveUserSubscription arg0,
			MessageResult arg1, ExecutionContext arg2) throws ActionException {}

}
