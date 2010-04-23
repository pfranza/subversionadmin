package com.peterfranza.gwt.svnadmin.server.entitydata.groupdata;

import java.util.Collection;

import com.peterfranza.gwt.svnadmin.server.entitydata.Entity;

public interface Group extends Entity{

	Collection<Entity> getMembers();
	
}
