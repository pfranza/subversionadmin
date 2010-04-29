package com.peterfranza.gwt.svnadmin.client;

import net.customware.gwt.dispatch.client.DispatchAsync;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.peterfranza.gwt.svnadmin.client.actions.CapabilitiesRequest;
import com.peterfranza.gwt.svnadmin.client.actions.CapabilitiesRequest.CapabilitiesResult;
import com.peterfranza.gwt.svnadmin.client.widgets.LoginWindow;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SubversionAdministrator implements EntryPoint {

	public static DispatchAsync dispatcher;
	public static CapabilitiesResult result;
	private LoginWindow login = new LoginWindow();
	private SInjector inject;
	

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {		
		inject = GWT.create(SInjector.class);
		dispatcher = inject.getDispatcher();
		
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {	
			@Override
			public void onUncaughtException(Throwable e) {
				ErrorHandler.onFailure(e);
			}
		});
		
		dispatcher.execute(new CapabilitiesRequest(), new AsyncCallback<CapabilitiesResult>() {
			@Override
			public void onFailure(Throwable caught) {
				ErrorHandler.onFailure(caught);
			}

			@Override
			public void onSuccess(CapabilitiesResult result) {
				SubversionAdministrator.result = result;
				login.show();
				login.center();
			}
		});

	}
}
