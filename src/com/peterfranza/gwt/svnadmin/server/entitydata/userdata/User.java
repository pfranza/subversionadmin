package com.peterfranza.gwt.svnadmin.server.entitydata.userdata;

import com.peterfranza.gwt.svnadmin.server.entitydata.Entity;

public interface User  extends Entity{

	String getEmailAddress();
	boolean isAdministrator();
	
	boolean authenticate(String password);
	
	void setPassword(String password);
	void setEmailAddress(String emailAddress);
	void setAdministrator(boolean isAdministrator);
	void save();
	
}
