package com.peterfranza.gwt.svnadmin.client.widgets;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

public class UserAdministrationPanel extends ContentPanel {

	private Button addUser = new Button("Add User");
	
	public UserAdministrationPanel() {
		super(new FitLayout());
		setHeaderVisible(false);
		addText("add content");
		
		addButton(addUser);
	}
	
}
