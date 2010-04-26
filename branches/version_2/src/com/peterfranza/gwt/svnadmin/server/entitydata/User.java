package com.peterfranza.gwt.svnadmin.server.entitydata;


public interface User  extends Entity{

	String getEmailAddress();
	boolean isAdministrator();
	
	boolean authenticate(String password);
	
	void setPassword(String password);
	void setEmailAddress(String emailAddress);
	void setAdministrator(boolean isAdministrator);
	
}
