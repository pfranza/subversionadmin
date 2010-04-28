package com.peterfranza.gwt.svnadmin.server.handlers.projectmgmt;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import com.google.inject.Inject;
import com.peterfranza.gwt.svnadmin.client.actions.MessageResult;
import com.peterfranza.gwt.svnadmin.client.actions.projectmanagement.AddProject;
import com.peterfranza.gwt.svnadmin.server.repositorydata.RepositoryManager;

public class AddProjectHandler implements ActionHandler<AddProject, MessageResult>{

	private RepositoryManager reposManager;

	@Inject
	public AddProjectHandler(RepositoryManager reposManager) {
		this.reposManager = reposManager;
	}
	
	@Override
	public MessageResult execute(AddProject arg0, ExecutionContext arg1)
			throws ActionException {
		if(arg0.getProjectPath().startsWith("/")) {
			reposManager.addProject(arg0.getProjectPath());
			return new MessageResult("", "Project ["+arg0.getProjectPath()+"] added.");
		} else {
			return new MessageResult("Error", "Bad Path");
		}
	}

	@Override
	public Class<AddProject> getActionType() {
		return AddProject.class;
	}

	@Override
	public void rollback(AddProject arg0, MessageResult arg1,
			ExecutionContext arg2) throws ActionException {}

}
