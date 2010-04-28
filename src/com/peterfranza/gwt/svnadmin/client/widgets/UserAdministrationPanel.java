package com.peterfranza.gwt.svnadmin.client.widgets;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;
import com.peterfranza.gwt.svnadmin.client.SubversionAdministrator;
import com.peterfranza.gwt.svnadmin.client.actions.MessageResult;
import com.peterfranza.gwt.svnadmin.client.actions.MessageResultHandler;
import com.peterfranza.gwt.svnadmin.client.actions.usermanagement.CreateUser;
import com.peterfranza.gwt.svnadmin.client.actions.usermanagement.ListUsers;
import com.peterfranza.gwt.svnadmin.client.actions.usermanagement.ListUsers.UserList;

public class UserAdministrationPanel extends ContentPanel {

	private Button addUser = new Button("Add User");
	private Button modUser = new Button("Modify User");
	private Button delUser = new Button("Delete User");
	
	private ListBox userList = new ListBox();
	
	public UserAdministrationPanel() {
		super(new FitLayout());
		setHeaderVisible(false);
		
		userList.setVisibleItemCount(10);
		add(userList);
		
		addButton(addUser);
		addButton(modUser);
		addButton(delUser);
		refresh();
		
		addUser.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				final MessageBox box = MessageBox.prompt("Add User", "Please enter user name:");  
				box.addCallback(new Listener<MessageBoxEvent>() {  
					public void handleEvent(MessageBoxEvent be) {  
						SubversionAdministrator.dispatcher.execute(
								new CreateUser(be.getValue()), new MessageResultHandler(){
									@Override
									public void onSuccess(MessageResult result) {
										super.onSuccess(result);
										refresh();
									}
								});
					}  
				});  
			}
		});
		
	}
	
	private void refresh() {
		userList.clear();
		SubversionAdministrator.dispatcher.execute(new ListUsers(), new AsyncCallback<UserList>() {
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(UserList result) {
				for(String s: result.getUsernames()) {
					userList.addItem(s);
				}
			}
		});
	}
	
}
