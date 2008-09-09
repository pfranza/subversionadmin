package com.gorthaur.svnadmin.client.ui;

import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.layout.AccordionLayout;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.CardLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;

public class ActionPanel extends Panel {

	public ActionPanel() {
		setLayout(new BorderLayout());
		setBorder(false);
		
		
		Panel menu = new Panel();
			menu.setCollapsible(false);
			menu.setWidth(200);
			menu.setLayout(new AccordionLayout(true));
			
			Panel userAdministration = new Panel("Users");
				userAdministration.setLayout(new VerticalLayout(10));
				userAdministration.setPaddings(15);
				Label addUser = new Label("Add User"); 
				Label modUser = new Label("Modify User");
				Label delUser = new Label("Delete User");
				userAdministration.add(addUser);
				userAdministration.add(modUser);
				userAdministration.add(delUser);
				
			Panel postCommitAdmin = new Panel("Notifications");
				postCommitAdmin.setLayout(new VerticalLayout(10));
				postCommitAdmin.setPaddings(15);
				Label configureNotifications = new Label("Configure");
				postCommitAdmin.add(configureNotifications);
			
			Panel info = new Panel("Server Info");
				info.setLayout(new VerticalLayout(10));
				info.setPaddings(15);
				Label stats = new Label("Statistics"); 
				Label backups = new Label("Backups"); 
				info.add(stats);
				info.add(backups);

			menu.add(userAdministration);
			menu.add(postCommitAdmin);
			menu.add(info);

			
		add(menu, new BorderLayoutData(RegionPosition.WEST));
		
		Panel main = new Panel();
			main.setCollapsible(false);
			main.setLayout(new CardLayout(true));
			
		add(main, new BorderLayoutData(RegionPosition.CENTER));
		
	}
	
}
