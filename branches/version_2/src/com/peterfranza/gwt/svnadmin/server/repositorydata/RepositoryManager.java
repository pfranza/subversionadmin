package com.peterfranza.gwt.svnadmin.server.repositorydata;

import java.util.Collection;

import com.peterfranza.gwt.svnadmin.server.entitydata.Entity;
import com.peterfranza.gwt.svnadmin.server.entitydata.User;

public interface RepositoryManager {

	enum ACCESS {READ, WRITE, NONE}
	
	Collection<Project> getProjects();
	Project getProjectForName(String name);
	void scanForProjects();
	void addProject(String projectName);
	
	void setReadWrite(String name, Entity entity, ACCESS access);
	boolean canRead(String name, Entity entity);
	boolean canWrite(String name, Entity entity);
	
	boolean isSubscribed(String name, User entity);
	void subscribe(String name, User entity);
	void unsubscribe(String name, User entity);
	
}
