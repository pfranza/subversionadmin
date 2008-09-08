package com.gorthaur.svnadmin.client;

import com.google.gwt.core.client.EntryPoint;
import com.gorthaur.svnadmin.client.ui.MainPanel;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Viewport;
import com.gwtext.client.widgets.layout.FitLayout;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SvnAdministration extends Panel implements EntryPoint {

	public SvnAdministration() {
		setBorder(false);  
		setPaddings(15);  
		setLayout(new FitLayout());
		add(new MainPanel());
	}

	public void onModuleLoad() {
		new Viewport(this);
	}
}
