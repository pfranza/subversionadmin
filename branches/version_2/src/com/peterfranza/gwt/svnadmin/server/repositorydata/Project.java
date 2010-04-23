package com.peterfranza.gwt.svnadmin.server.repositorydata;

import com.peterfranza.gwt.svnadmin.server.entitydata.Entity;

public interface Project {

	String getName();
	String getPath();
	
	boolean canRead(Entity entity);
	boolean canWrite(Entity entity);
	
	boolean isSubscribed(Entity entity);
	void subscribe(Entity entity);
	void unsubscribe(Entity entity);
	
}
