package com.peterfranza.gwt.svnadmin.client.widgets;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

public class GroupAdministrationPanel extends ContentPanel {

	public GroupAdministrationPanel() {
		super(new FitLayout());
		setHeaderVisible(false);
		addText("add content");
	}
	
}
