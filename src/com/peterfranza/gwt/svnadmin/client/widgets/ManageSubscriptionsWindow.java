package com.peterfranza.gwt.svnadmin.client.widgets;

import java.util.ArrayList;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;
import com.peterfranza.gwt.svnadmin.client.SubversionAdministrator;
import com.peterfranza.gwt.svnadmin.client.actions.MessageResult;
import com.peterfranza.gwt.svnadmin.client.actions.MessageResultHandler;
import com.peterfranza.gwt.svnadmin.client.actions.groupmanagement.AddUserToGroup;
import com.peterfranza.gwt.svnadmin.client.actions.projectmanagement.ProjectAddUserSubscription;
import com.peterfranza.gwt.svnadmin.client.actions.projectmanagement.ProjectListUserSubscriptions;
import com.peterfranza.gwt.svnadmin.client.actions.projectmanagement.ProjectRemoveUserSubscription;
import com.peterfranza.gwt.svnadmin.client.actions.projectmanagement.ProjectListUserSubscriptions.SubscriptionList;
import com.peterfranza.gwt.svnadmin.client.actions.usermanagement.ListUsers;
import com.peterfranza.gwt.svnadmin.client.actions.usermanagement.ListUsers.UserList;

public class ManageSubscriptionsWindow extends Window {

	private Button addMember = new Button("Add Subscriber");
	private Button removeMember = new Button("Remove Subscriber");
	private Button close = new Button("Close");
	
	private ListBox userList = new ListBox();
	private ListBox availableUserList = new ListBox();
	
	private String projectName;
	
	public ManageSubscriptionsWindow(String project) {
		this.projectName = project;
		
		setHeading("Modify Project Subscriptions");
		setLayout(new FitLayout());
		setPlain(true);  
		setModal(true);  
		setBlinkModal(true); 
		setClosable(false);
		setSize(335, 250);
		
		userList.setVisibleItemCount(10);
		availableUserList.setVisibleItemCount(1);

		LayoutContainer p = new LayoutContainer(new BorderLayout());
		p.add(userList, new BorderLayoutData(LayoutRegion.CENTER));
		p.add(availableUserList, new BorderLayoutData(LayoutRegion.SOUTH, 25));
		
		add(p);
		
		addButton(addMember);
		addButton(removeMember);
		addButton(close);
		refresh();
		
		userList.addChangeHandler(new ChangeHandler() {			
			@Override
			public void onChange(ChangeEvent event) {
				removeMember.setEnabled(userList.getSelectedIndex() != -1);
			}
		});
		
		close.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				hide();
			}
		});
		
		removeMember.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				SubversionAdministrator.dispatcher.execute(
						new ProjectRemoveUserSubscription(projectName, 
								userList.getItemText(userList.getSelectedIndex())),
						new MessageResultHandler(){
							@Override
							public void onSuccess(MessageResult result) {
								super.onSuccess(result);
								refresh();
							}
						});	
			}
		});
		
		addMember.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				SubversionAdministrator.dispatcher.execute(
						new ProjectAddUserSubscription(projectName, 
								availableUserList.getItemText(availableUserList.getSelectedIndex())),
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
	
	private void refresh() {
		userList.clear();
		availableUserList.clear();
		availableUserList.setEnabled(false);
		removeMember.setEnabled(false);
		addMember.setEnabled(false);
		SubversionAdministrator.dispatcher.execute(
				new ProjectListUserSubscriptions(projectName), 
				new AsyncCallback<SubscriptionList>() {
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(final SubscriptionList result) {
				for(String s: result.getSubscriptionNames()) {
					userList.addItem(s);
				}
				
				SubversionAdministrator.dispatcher.execute(new ListUsers(), 
						new AsyncCallback<UserList>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(UserList availResult) {
						for(String s: availResult.getUsernames()) {
							if(!isInList(s, result.getSubscriptionNames())) {
								availableUserList.addItem(s);
							}
						}
						availableUserList.setEnabled(availableUserList.getItemCount() > 0);
						addMember.setEnabled(availableUserList.getItemCount() > 0);
					}


				});
				
			}
		});
	}
	
	private static boolean isInList(String s,
			ArrayList<String> usernames) {
		for(String u: usernames) {
			if(u.equals(s)) {
				return true;
			}
		}
		return false;
	}

}
