package com.peterfranza.gwt.svnadmin.client.widgets;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.MarginData;
import com.peterfranza.gwt.svnadmin.client.SubversionAdministrator;

public class MainPanel extends ContentPanel {

	private TabPanel folder = new TabPanel();
	
	public MainPanel(){
		super(new FitLayout());
		setHeaderVisible(false);


		if(SubversionAdministrator.result.getLocalAccounts()) {
			TabItem userAdmin = new TabItem("User Administration");  
			userAdmin.add(new UserAdministrationPanel(), new MarginData(10));
			folder.add(userAdmin);
		}
		
		TabItem groupAdmin = new TabItem("Group Administration");   
		groupAdmin.add(new GroupAdministrationPanel(), new MarginData(10));
		folder.add(groupAdmin); 
		
		TabItem projectAdmin = new TabItem("Project Administration");  
		projectAdmin.add(new ProjectAdministrationPanel(), new MarginData(10));
		folder.add(projectAdmin);
		
		add(folder);
		
//		ContentPanel cp = new ContentPanel(new BorderLayout());
//		
//		add(cp);
	}
	
}
