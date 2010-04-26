package com.peterfranza.gwt.svnadmin.server.repositorydata;

import java.util.Collection;

import com.peterfranza.gwt.svnadmin.server.entitydata.Entity;

public interface RepositoryManager {

	enum ACCESS {READ, WRITE, NONE}
	
	Collection<Project> getProjects();
	Project getProjectForName(String name);
	void scanForProjects();
	void addProject(Project project);
	
	void setReadWrite(String name, Entity entity, ACCESS access);
	boolean canRead(String name, Entity entity);
	boolean canWrite(String name, Entity entity);
	
	boolean isSubscribed(String name, Entity entity);
	void subscribe(String name, Entity entity);
	void unsubscribe(String name, Entity entity);
	
}
