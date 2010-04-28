package com.peterfranza.gwt.svnadmin.server.handlers.projectmgmt;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import com.google.inject.Inject;
import com.peterfranza.gwt.svnadmin.client.actions.projectmanagement.ListProjects;
import com.peterfranza.gwt.svnadmin.client.actions.projectmanagement.ListProjects.ProjectsList;
import com.peterfranza.gwt.svnadmin.server.repositorydata.RepositoryManager;

public class ListProjectsHandler implements ActionHandler<ListProjects, ListProjects.ProjectsList>{

	private RepositoryManager reposManager;

	@Inject
	public ListProjectsHandler(RepositoryManager reposManager) {
		this.reposManager = reposManager;
	}
	
	@Override
	public ProjectsList execute(ListProjects arg0, ExecutionContext arg1)
			throws ActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<ListProjects> getActionType() {
		return ListProjects.class;
	}

	@Override
	public void rollback(ListProjects arg0, ProjectsList arg1,
			ExecutionContext arg2) throws ActionException {}

}
