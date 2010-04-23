package com.peterfranza.gwt.svnadmin.server.entitydata.userdata;

import java.util.Collection;

public interface UserManager {

	Collection<User> getUsers();
	User getUserForName(String username);
	
}
