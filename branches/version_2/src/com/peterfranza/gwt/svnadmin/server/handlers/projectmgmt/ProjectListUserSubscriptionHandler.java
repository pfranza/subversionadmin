package com.peterfranza.gwt.svnadmin.server.handlers.projectmgmt;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import com.google.inject.Inject;
import com.peterfranza.gwt.svnadmin.client.actions.projectmanagement.ProjectListUserSubscriptions;
import com.peterfranza.gwt.svnadmin.client.actions.projectmanagement.ProjectListUserSubscriptions.SubscriptionList;
import com.peterfranza.gwt.svnadmin.server.entitydata.UserManager;
import com.peterfranza.gwt.svnadmin.server.repositorydata.RepositoryManager;

public class ProjectListUserSubscriptionHandler implements ActionHandler<ProjectListUserSubscriptions, ProjectListUserSubscriptions.SubscriptionList>{

	private RepositoryManager reposManager;
	private UserManager userManager;

	@Inject
	public ProjectListUserSubscriptionHandler(RepositoryManager reposManager,
			UserManager userManager) {
		this.reposManager = reposManager;
		this.userManager = userManager;
	}
	
	@Override
	public SubscriptionList execute(ProjectListUserSubscriptions arg0,
			ExecutionContext arg1) throws ActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<ProjectListUserSubscriptions> getActionType() {
		return ProjectListUserSubscriptions.class;
	}

	@Override
	public void rollback(ProjectListUserSubscriptions arg0,
			SubscriptionList arg1, ExecutionContext arg2)
			throws ActionException {}

}
