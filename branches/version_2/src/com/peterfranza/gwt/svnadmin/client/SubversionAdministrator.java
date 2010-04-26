package com.peterfranza.gwt.svnadmin.client;

import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.EntryPoint;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SubversionAdministrator implements EntryPoint {


	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
		
//		RootLayoutPanel.get().add(new)
		
		
		final Window window = new Window();  
		window.setSize(500, 300);  
		window.setPlain(true);  
		window.setModal(true);  
		window.setBlinkModal(true);  
		window.setHeading("Hello Window");  
		window.setLayout(new FitLayout());

		window.show();
	}
}
