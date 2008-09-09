package com.gorthaur.svnadmin.client.ui;

import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.CardLayout;

public class ActionPanel extends Panel {

	public ActionPanel() {
		setLayout(new BorderLayout());
		setBorder(false);
		
		Panel menu = new Panel("Menu");
			menu.setCollapsible(true);
			menu.setWidth(100);
			menu.setPaddings(5);
			
		add(menu, new BorderLayoutData(RegionPosition.WEST));
		
		Panel main = new Panel();
			main.setCollapsible(false);
			main.setLayout(new CardLayout(true));
			
		add(main, new BorderLayoutData(RegionPosition.CENTER));
		
	}
	
}
