package com.peterfranza.gwt.svnadmin.client.widgets;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.layout.MarginData;
import com.google.gwt.user.client.Element;

public class MainPanel extends ContentPanel {

	private TabPanel folder = new TabPanel();
	
	{
		setHeaderVisible(false);
		folder.setAutoWidth(true);
		folder.setAutoHeight(true);  
		
		TabItem userAdmin = new TabItem("User Administration");   
		userAdmin.add(new UserAdministrationPanel(), new MarginData(10));
		folder.add(userAdmin);
		
		TabItem groupAdmin = new TabItem("Group Administration");   
		groupAdmin.add(new GroupAdministrationPanel(), new MarginData(10));
		folder.add(groupAdmin); 
		
		TabItem projectAdmin = new TabItem("Project Administration");   
		projectAdmin.add(new ProjectAdministrationPanel(), new MarginData(10));
		folder.add(projectAdmin);
		
		
	}
	
	@Override
	protected void onRender(Element parent, int pos) {
		super.onRender(parent, pos);
		add(folder);
	}
	
}
