package com.gorthaur.svnadmin.client.ui;

import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.layout.FitLayout;

public class MainPanel extends Panel {

	public MainPanel() {
	
		setLayout(new FitLayout());
		setPaddings(15);
		setBorder(true);
		
		Toolbar toolbar = new Toolbar();
//			toolbar.addButton(new ToolbarButton("Login"));
			
		setTopToolbar(toolbar);
		


	}
	
}
