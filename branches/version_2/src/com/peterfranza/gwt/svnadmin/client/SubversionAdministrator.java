package com.peterfranza.gwt.svnadmin.client;

import com.google.gwt.core.client.EntryPoint;
import com.peterfranza.gwt.svnadmin.client.widgets.LoginWindow;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SubversionAdministrator implements EntryPoint {

	private LoginWindow login = new LoginWindow();
	

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {		
		login.show();
		login.center();
	}
}
