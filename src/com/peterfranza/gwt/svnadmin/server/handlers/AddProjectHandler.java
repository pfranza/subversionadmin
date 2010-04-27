package com.peterfranza.gwt.svnadmin.server.handlers;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import com.google.inject.Inject;
import com.peterfranza.gwt.svnadmin.client.actions.AddProjectRequest;
import com.peterfranza.gwt.svnadmin.client.actions.MessageResult;
import com.peterfranza.gwt.svnadmin.server.repositorydata.RepositoryManager;

public class AddProjectHandler implements ActionHandler<AddProjectRequest, MessageResult>{

	private RepositoryManager reposManager;

	@Inject
	public AddProjectHandler(RepositoryManager reposManager) {
		this.reposManager = reposManager;
	}
	
	@Override
	public MessageResult execute(AddProjectRequest arg0, ExecutionContext arg1)
			throws ActionException {
		if(arg0.getProjectPath().startsWith("/")) {
			reposManager.addProject(arg0.getProjectPath());
			return new MessageResult("", "Project ["+arg0.getProjectPath()+"] added.");
		} else {
			return new MessageResult("Error", "Bad Path");
		}
	}

	@Override
	public Class<AddProjectRequest> getActionType() {
		return AddProjectRequest.class;
	}

	@Override
	public void rollback(AddProjectRequest arg0, MessageResult arg1,
			ExecutionContext arg2) throws ActionException {}

}
