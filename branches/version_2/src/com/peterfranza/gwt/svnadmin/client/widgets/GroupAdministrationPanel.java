package com.peterfranza.gwt.svnadmin.client.widgets;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

public class GroupAdministrationPanel extends ContentPanel {

	private Button addGroup = new Button("Add Group");
	
	public GroupAdministrationPanel() {
		super(new FitLayout());
		setHeaderVisible(false);
		addText("add content");
		
		addButton(addGroup);
	}
	
}
