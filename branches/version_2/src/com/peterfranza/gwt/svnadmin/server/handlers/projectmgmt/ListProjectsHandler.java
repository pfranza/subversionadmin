package com.peterfranza.gwt.svnadmin.server.handlers.projectmgmt;

import java.util.ArrayList;
import java.util.Collection;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import com.google.inject.Inject;
import com.peterfranza.gwt.svnadmin.client.actions.projectmanagement.ListProjects;
import com.peterfranza.gwt.svnadmin.client.actions.projectmanagement.ListProjects.ProjectsList;
import com.peterfranza.gwt.svnadmin.server.repositorydata.Project;
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
		return new ProjectsList(asStrings(reposManager.getProjects()));
	}

	private ArrayList<String> asStrings(Collection<Project> projects) {
		ArrayList<String> l = new ArrayList<String>();
		for(Project s: projects) {
			l.add(s.getPath());
		}
		return l;
	}

	@Override
	public Class<ListProjects> getActionType() {
		return ListProjects.class;
	}

	@Override
	public void rollback(ListProjects arg0, ProjectsList arg1,
			ExecutionContext arg2) throws ActionException {}

}
