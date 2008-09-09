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

	private class UserAdminMenu extends Panel {
		
		private Label addUser = new Label("Add User"); 
		private Label modUser = new Label("Modify User");
		private Label delUser = new Label("Delete User");
		
		public UserAdminMenu() {
			super("Users");
			setLayout(new VerticalLayout(10));
			setPaddings(15);
			add(addUser);
			add(modUser);
			add(delUser);
		}
		
	}
	
	private class NotificationsMenu extends Panel {
		
		private Label configureNotifications = new Label("Configure");
		
		public NotificationsMenu() {
			super("Notifications");
			setLayout(new VerticalLayout(10));
			setPaddings(15);			
			add(configureNotifications);
		}
		
	}
	
	private class InformationMenu extends Panel {
		
		private Label stats = new Label("Statistics"); 
		private Label backups = new Label("Backups");
		
		public InformationMenu() {
			super("Server Info");
			setLayout(new VerticalLayout(10));
			setPaddings(15); 
			add(stats);
			add(backups);
		}
	}
	
	private class MenuPanel extends Panel {
		public MenuPanel() {
			setCollapsible(false);
			setWidth(150);
			setLayout(new AccordionLayout(true));

			add(new UserAdminMenu());
			add(new NotificationsMenu());
			add(new InformationMenu());
		}
	}
	
	private class ContentPanel extends Panel {
		public ContentPanel() {
			setCollapsible(false);
			setLayout(new CardLayout(true));
		}
	}
	
	private MenuPanel menu = new MenuPanel();
	private ContentPanel content = new ContentPanel();
	
	public ActionPanel() {
		setLayout(new BorderLayout());
		setBorder(false);
			
		add(menu, new BorderLayoutData(RegionPosition.WEST));			
		add(content, new BorderLayoutData(RegionPosition.CENTER));		
	}
	
}
