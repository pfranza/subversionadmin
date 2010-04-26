package com.peterfranza.gwt.svnadmin.server.entitydata;

import java.util.Collection;


public interface Group extends Entity{

	Collection<Entity> getMembers();
	
}
