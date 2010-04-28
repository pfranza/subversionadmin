package com.peterfranza.gwt.svnadmin.server.handlers.projectmgmt;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import com.google.inject.Inject;
import com.peterfranza.gwt.svnadmin.client.actions.MessageResult;
import com.peterfranza.gwt.svnadmin.client.actions.projectmanagement.ProjectSetMemberAccess;
import com.peterfranza.gwt.svnadmin.server.entitydata.GroupManager;
import com.peterfranza.gwt.svnadmin.server.repositorydata.RepositoryManager;
import com.peterfranza.gwt.svnadmin.server.repositorydata.RepositoryManager.ACCESS;

public class ProjectSetMemberAccessHandler implements ActionHandler<ProjectSetMemberAccess, MessageResult>{

	private RepositoryManager reposManager;
	private GroupManager groupManager;

	@Inject
	public ProjectSetMemberAccessHandler(RepositoryManager reposManager,
			GroupManager groupManager) {
		this.reposManager = reposManager;
		this.groupManager = groupManager;
	}
	
	@Override
	public MessageResult execute(ProjectSetMemberAccess arg0,
			ExecutionContext arg1) throws ActionException {
		reposManager.setReadWrite(arg0.getProject(), 
				groupManager.getGroup(arg0.getUsername()), 
				arg0.isCanWrite() ? ACCESS.WRITE : arg0.isCanRead() ? ACCESS.READ : ACCESS.NONE);
		return new MessageResult("", "Permissions Changed");
	}

	@Override
	public Class<ProjectSetMemberAccess> getActionType() {
		return ProjectSetMemberAccess.class;
	}

	@Override
	public void rollback(ProjectSetMemberAccess arg0, MessageResult arg1,
			ExecutionContext arg2) throws ActionException {}

}
