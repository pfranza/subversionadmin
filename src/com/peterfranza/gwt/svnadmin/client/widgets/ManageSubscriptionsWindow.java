package com.peterfranza.gwt.svnadmin.client.widgets;

import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

public class ManageSubscriptionsWindow extends Window {

	public ManageSubscriptionsWindow(String project) {
		setHeading("Modify Project Subscriptions");
		setLayout(new FitLayout());
		setPlain(true);  
		setModal(true);  
		setBlinkModal(true); 
		setClosable(false);
		setSize(335, 250);
	}

}
