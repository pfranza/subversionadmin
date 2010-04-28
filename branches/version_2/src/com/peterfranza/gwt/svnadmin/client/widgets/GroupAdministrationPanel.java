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
import com.peterfranza.gwt.svnadmin.client.actions.groupmanagement.CreateGroup;
import com.peterfranza.gwt.svnadmin.client.actions.groupmanagement.ListGroups;
import com.peterfranza.gwt.svnadmin.client.actions.groupmanagement.RemoveGroup;
import com.peterfranza.gwt.svnadmin.client.actions.groupmanagement.ListGroups.GroupsList;

public class GroupAdministrationPanel extends ContentPanel {

	private Button addGroup = new Button("Add Group");
	private Button modGroup = new Button("Modify Group");
	private Button delGroup = new Button("Delete Group");
	
	private ListBox groupList = new ListBox();
	
	public GroupAdministrationPanel() {
		super(new FitLayout());
		setHeaderVisible(false);
		
		groupList.setVisibleItemCount(10);
		add(groupList);
		
		addButton(addGroup);
		addButton(modGroup);
		addButton(delGroup);
		refresh();
		
		groupList.addChangeHandler(new ChangeHandler() {			
			@Override
			public void onChange(ChangeEvent event) {
				modGroup.setEnabled(groupList.getSelectedIndex() != -1);
				delGroup.setEnabled(groupList.getSelectedIndex() != -1);
			}
		});
		
		addGroup.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				final MessageBox box = MessageBox.prompt("Add Group", "Please enter group name:");  
				box.addCallback(new Listener<MessageBoxEvent>() {  
					public void handleEvent(MessageBoxEvent be) {  
						SubversionAdministrator.dispatcher.execute(
								new CreateGroup(be.getValue()), new MessageResultHandler(){
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
		
		delGroup.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				MessageBox.confirm("Confirm", "Are you sure you want to do that?", new Listener<MessageBoxEvent>() {
					
					@Override
					public void handleEvent(MessageBoxEvent be) {
						SubversionAdministrator.dispatcher.execute(
								new RemoveGroup(groupList.getItemText(groupList.getSelectedIndex())), 
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
		
	}
	
	private void refresh() {
		groupList.clear();
		modGroup.setEnabled(false);
		delGroup.setEnabled(false);
		SubversionAdministrator.dispatcher.execute(new ListGroups(), new AsyncCallback<ListGroups.GroupsList>() {
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(GroupsList result) {
				for(String s: result.getGroups()) {
					groupList.addItem(s);
				}
			}
		});
	}
	
}
