package com.gorthaur.svnadmin.client.ui;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.HistoryListener;
import com.google.gwt.user.client.ui.Hyperlink;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.AccordionLayout;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.CardLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;

public class ActionPanel extends Panel {

	private class UserAdminMenu extends Panel {
		
		private Hyperlink addUser = new Hyperlink("Add User", "0"); 
		private Hyperlink modUser = new Hyperlink("Modify User", "1");
		private Hyperlink delUser = new Hyperlink("Delete User", "2");
		
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
		
		private Hyperlink configureNotifications = new Hyperlink("Configure", "3");
		
		public NotificationsMenu() {
			super("Notifications");
			setLayout(new VerticalLayout(10));
			setPaddings(15);			
			add(configureNotifications);
		}
		
	}
	
	private class InformationMenu extends Panel {
		
		private Hyperlink stats = new Hyperlink("Statistics", "4"); 
		private Hyperlink backups = new Hyperlink("Backups", "5");
		
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
	
	private class ContentPanel extends Panel implements HistoryListener {
		
		private CardLayout layout = new CardLayout(false);
		private AddUserFormPanel addUserForm = new AddUserFormPanel();
		
		public ContentPanel() {
			setLayout(layout);
			History.addHistoryListener(this);
			add(addUserForm);			
		}

		public void onHistoryChanged(String historyToken) {
			setActiveItem(Integer.valueOf(historyToken));
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
