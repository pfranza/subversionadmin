package com.peterfranza.gwt.svnadmin.server.entitydata;

import java.util.Collection;

public interface UserManager {

	Collection<? extends User> getUsers();
	User getUserForName(String username);
	boolean authenticate(String username, String password);
	void removeUser(String username);
	
	void createUser(String username);
	void setPassword(String username, String password);
	void setEmailAddress(String username, String email);
	void setAdministrator(String username, boolean isAdmin);
	
	boolean isMutable();
}
