package com.gorthaur.svnadmin.client.ui;

import com.google.gwt.user.client.ui.Label;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.FormLayout;

public class AddUserFormPanel extends Panel {

	public AddUserFormPanel() {
		setLayout(new FormLayout());
		setPaddings(15);
		
		add(new Label("Add user"));
		
	}
	
}
