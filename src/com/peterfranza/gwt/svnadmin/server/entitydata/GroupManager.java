package com.peterfranza.gwt.svnadmin.server.entitydata;

import java.util.Collection;

public interface GroupManager {

	void createGroup(String groupName);
	void removeGroup(String groupName);
	
	Group getGroup(String groupName);
	Collection<? extends Group> getGroups();
	
	void addMemberToGroup(String groupName, Entity user);
	void removeMemberFromGroup(String groupName, Entity user);
	
}
