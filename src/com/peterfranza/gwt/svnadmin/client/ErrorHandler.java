package com.peterfranza.gwt.svnadmin.client;

import com.google.gwt.user.client.Window;

public class ErrorHandler {

	public static void onFailure(Throwable caught) {
		caught.printStackTrace();
		Window.alert(caught.getMessage());
	}
	
}
