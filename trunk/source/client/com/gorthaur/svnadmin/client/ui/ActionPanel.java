package com.gorthaur.svnadmin.client.ui;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.HistoryListener;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.gorthaur.svnadmin.client.ui.forms.AddGroupPanel;
import com.gorthaur.svnadmin.client.ui.forms.AddUserFormPanel;
import com.gorthaur.svnadmin.client.ui.forms.ModifyUserFormPanel;
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
		private Hyperlink manageSubscriptions = new Hyperlink("Manage Subscriptions", "2");
		
		public UserAdminMenu() {
			super("Users");
			setLayout(new VerticalLayout(10));
			setPaddings(15);
			add(addUser);
			add(modUser);
			add(manageSubscriptions);
		}
		
	}
	
	private class GroupAdminMenu extends Panel {
		
		private Hyperlink add = new Hyperlink("Add Group", "3"); 
		private Hyperlink mod = new Hyperlink("Modify Group", "4");
		private Hyperlink manageSubscriptions = new Hyperlink("Manage Subscriptions", "5");

		
		public GroupAdminMenu() {
			super("Groups");
			setLayout(new VerticalLayout(10));
			setPaddings(15);
			add(add);
			add(mod);	
			add(manageSubscriptions);
		}
		
	}
	
	
	private class InformationMenu extends Panel {
		
		private Hyperlink stats = new Hyperlink("Statistics", "6"); 
		private Hyperlink backups = new Hyperlink("Backups", "7");
		
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
			add(new GroupAdminMenu());
			add(new InformationMenu());
		}
	}
	
	private class ContentPanel extends Panel {
		
		private CardLayout layout = new CardLayout();
		
		private AddUserFormPanel addUserForm = new AddUserFormPanel();
		private ModifyUserFormPanel modifyUserForm = new ModifyUserFormPanel();
		private AddGroupPanel addGroup = new AddGroupPanel();
		
		public ContentPanel() {
			setLayout(layout);
			History.newItem("");
			History.addHistoryListener(new HistoryListener() {
				public void onHistoryChanged(String historyToken) {
					if(historyToken.trim().length() > 0) {
						setActiveItem(Integer.valueOf(historyToken));
					}
				}
			});
			
			add(addUserForm);
			add(modifyUserForm);
			add(new Label("Manage Subscriptions"));
			add(addGroup);
			add(new Label("4"));
			add(new Label("5"));
			add(new Label("6"));
			add(new Label("7"));
			
			
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
