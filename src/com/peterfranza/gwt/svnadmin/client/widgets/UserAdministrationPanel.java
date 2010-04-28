package com.peterfranza.gwt.svnadmin.client.widgets;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;
import com.peterfranza.gwt.svnadmin.client.SubversionAdministrator;
import com.peterfranza.gwt.svnadmin.client.actions.MessageResult;
import com.peterfranza.gwt.svnadmin.client.actions.MessageResultHandler;
import com.peterfranza.gwt.svnadmin.client.actions.usermanagement.CreateUser;
import com.peterfranza.gwt.svnadmin.client.actions.usermanagement.FetchUserDetails;
import com.peterfranza.gwt.svnadmin.client.actions.usermanagement.ListUsers;
import com.peterfranza.gwt.svnadmin.client.actions.usermanagement.RemoveUser;
import com.peterfranza.gwt.svnadmin.client.actions.usermanagement.FetchUserDetails.UserDetails;
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
		
		userList.addChangeHandler(new ChangeHandler() {			
			@Override
			public void onChange(ChangeEvent event) {
				modUser.setEnabled(userList.getSelectedIndex() != -1);
				delUser.setEnabled(userList.getSelectedIndex() != -1);
			}
		});
		
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
		
		delUser.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				MessageBox.confirm("Confirm", "Are you sure you want to do that?", new Listener<MessageBoxEvent>() {
					
					@Override
					public void handleEvent(MessageBoxEvent be) {
						SubversionAdministrator.dispatcher.execute(
								new RemoveUser(userList.getItemText(userList.getSelectedIndex())), 
								new MessageResultHandler(){
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
		
		modUser.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				SubversionAdministrator.dispatcher.execute(
						new FetchUserDetails(userList.getItemText(userList.getSelectedIndex())),
						new AsyncCallback<FetchUserDetails.UserDetails>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(UserDetails result) {
								new ModifyUserWindow(result).show();
							}
						});
			}
		});
		
	}
	
	private void refresh() {
		userList.clear();
		modUser.setEnabled(false);
		delUser.setEnabled(false);
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
