package com.peterfranza.gwt.svnadmin.client.widgets;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;
import com.peterfranza.gwt.svnadmin.client.SubversionAdministrator;
import com.peterfranza.gwt.svnadmin.client.actions.MessageResult;
import com.peterfranza.gwt.svnadmin.client.actions.MessageResultHandler;
import com.peterfranza.gwt.svnadmin.client.actions.groupmanagement.ListGroups;
import com.peterfranza.gwt.svnadmin.client.actions.groupmanagement.ListGroups.GroupsList;
import com.peterfranza.gwt.svnadmin.client.actions.projectmanagement.ListProjectMembers;
import com.peterfranza.gwt.svnadmin.client.actions.projectmanagement.ProjectSetMemberAccess;
import com.peterfranza.gwt.svnadmin.client.actions.projectmanagement.ListProjectMembers.MembersList;

public class ManageAccessWindow extends Window {

	private ListBox groupList = new ListBox();
	
	private Button accessNone = new Button("None");
	private Button accessRead = new Button("Read");
	private Button accessWrite = new Button("Write");
	private Button close = new Button("Close");

	private String projectName;
	
	public ManageAccessWindow(final String projectName) {
		
		this.projectName = projectName;
		
		setHeading("Modify Project Access");
		setLayout(new FitLayout());
		setPlain(true);  
		setModal(true);  
		setBlinkModal(true); 
		setClosable(false);
		setSize(500, 250);
		
		groupList.setVisibleItemCount(10);
		add(groupList);
		
		addButton(accessNone);
		addButton(accessRead);
		addButton(accessWrite);
		addButton(close);
		
		refresh();
		
		groupList.addChangeHandler(new ChangeHandler() {			
			@Override
			public void onChange(ChangeEvent event) {
				accessNone.setEnabled(groupList.getSelectedIndex() != -1);
				accessRead.setEnabled(groupList.getSelectedIndex() != -1);
				accessWrite.setEnabled(groupList.getSelectedIndex() != -1);
			}
		});
		
		close.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				hide();
			}
		});
		
		accessNone.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				SubversionAdministrator.dispatcher.execute(
						new ProjectSetMemberAccess(projectName, 
								groupList.getValue(groupList.getSelectedIndex()), false, false),
								new MessageResultHandler(){
							@Override
							public void onSuccess(MessageResult result) {
								super.onSuccess(result);
								refresh();
							}
						});
			}
		});
		
		accessRead.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				SubversionAdministrator.dispatcher.execute(
						new ProjectSetMemberAccess(projectName, 
								groupList.getValue(groupList.getSelectedIndex()), true, false),
								new MessageResultHandler(){
							@Override
							public void onSuccess(MessageResult result) {
								super.onSuccess(result);
								refresh();
							}
						});
			}
		});
		
		accessWrite.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				SubversionAdministrator.dispatcher.execute(
						new ProjectSetMemberAccess(projectName, 
								groupList.getValue(groupList.getSelectedIndex()), true, true),
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
		groupList.clear();
		accessNone.setEnabled(false);
		accessRead.setEnabled(false);
		accessWrite.setEnabled(false);
		
		SubversionAdministrator.dispatcher.execute(
				new ListGroups(),
				new AsyncCallback<GroupsList>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(final GroupsList groupResult) {
						SubversionAdministrator.dispatcher.execute(
								new ListProjectMembers(projectName),
								new AsyncCallback<MembersList>() {

									@Override
									public void onFailure(Throwable caught) {
										caught.printStackTrace();
									}

									@Override
									public void onSuccess(MembersList perms) {
										for(String g: groupResult.getGroups()) {
											groupList.addItem(createString(g, perms), g);
										}
									}

								});
					}
				});
		
	}
	

	private String createString(
			String g,
			MembersList perms) {
		return g + "  (" + (canWrite(g, perms) ? "W" : canRead(g, perms) ? "R" : "N") +")";
	}

	private boolean canRead(String g, MembersList perms) {
		for(String p: perms.getReaders()) {
			if(g.equals(p)) {
				return true;
			}
		}
		return false;
	}

	private boolean canWrite(String g, MembersList perms) {
		for(String p: perms.getWriters()) {
			if(g.equals(p)) {
				return true;
			}
		}
		return false;
	}

}
