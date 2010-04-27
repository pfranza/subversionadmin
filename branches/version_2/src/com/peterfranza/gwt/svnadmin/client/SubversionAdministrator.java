package com.peterfranza.gwt.svnadmin.client;

import net.customware.gwt.dispatch.client.DefaultDispatchAsync;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.peterfranza.gwt.svnadmin.client.actions.CapabilitiesRequest;
import com.peterfranza.gwt.svnadmin.client.actions.CapabilitiesRequest.CapabilitiesResult;
import com.peterfranza.gwt.svnadmin.client.widgets.LoginWindow;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SubversionAdministrator implements EntryPoint {

	public static DefaultDispatchAsync dispatcher = new DefaultDispatchAsync();
	private LoginWindow login = new LoginWindow();
	

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {		
		dispatcher.execute(new CapabilitiesRequest(), new AsyncCallback<CapabilitiesResult>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(CapabilitiesResult result) {
				login.show();
				login.center();
			}
		});

	}
}
