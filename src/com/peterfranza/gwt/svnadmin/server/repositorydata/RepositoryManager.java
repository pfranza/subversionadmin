package com.peterfranza.gwt.svnadmin.server.repositorydata;

import java.util.Collection;

public interface RepositoryManager {

	Collection<Project> getProjects();
	Project getProjectForName(String name);
	void scanForProjects();
	void addProject(Project project);
	
}
