package com.gorthaur.svnadmin.client.ui.forms;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gorthaur.svnadmin.client.SvnAdministration;
import com.gorthaur.svnadmin.client.rpcinterface.GroupOperationsInterface;
import com.gorthaur.svnadmin.client.rpcinterface.GroupOperationsInterfaceAsync;
import com.gorthaur.svnadmin.client.rpcinterface.UserOperationsInterface;
import com.gorthaur.svnadmin.client.rpcinterface.UserOperationsInterfaceAsync;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;

public class ChangeAccessRulesPanel extends Panel {

	private Store mStore;

	private final ComboBox usersList = new ComboBox() {{
		setMinChars(1);  
		setFieldLabel("Select Project");   
		setMode(ComboBox.REMOTE);  
		setTriggerAction(ComboBox.ALL);  
		setEmptyText("Select Username");  
		setLoadingText("Searching...");  
		setTypeAhead(true);  
		setSelectOnFocus(true);  
		setWidth(300);
		setPageSize(10);
		setGrow(true);
		setDisplayField("name");	
		
	}};
	
	private final FieldSet permsset = new FieldSet();
	
	public ChangeAccessRulesPanel() {
		
		final FormPanel form = new FormPanel();
		
		final FieldSet set = new FieldSet();
		set.setCollapsible(false);  
		set.setAutoHeight(true);

		addListener(new PanelListenerAdapter() {

			boolean done = false;
			
			public void onShow(Component component) {
				if(!done) {					
					set.add(usersList);
					set.doLayout();
					
					done = true;
				} 
					refreshDataStore();
				
				super.onShow(component);
			}
		});
		
		form.setTitle("Modify Group");  
		form.setFrame(false);  
		form.setPaddings(5, 5, 5, 0);  
		form.setWidth(500);  
		form.setLabelWidth(75);  
						
		form.add(set);
		form.add(permsset);
		add(form);
		usersList.addListener(new ComboBoxListenerAdapter() {
			@Override
			public void onSelect(ComboBox comboBox, Record record, int index) {
				loadData(record);
				super.onSelect(comboBox, record, index);
			}
		});
		loadData(null);
		
	}
	
	private void refreshDataStore() {
		try {
			usersList.clearValue();
			usersList.reset();
			
			
			final UserOperationsInterfaceAsync user = GWT.create(UserOperationsInterface.class);
			user.getAllProjects(SvnAdministration.getInstance().getCredentials(), new AsyncCallback<List<String>>() {

				public void onFailure(Throwable caught) {
					Window.alert(caught.getMessage());
				}

				public void onSuccess(List<String> result) {
					
					Object[][] data = new Object[result.size()][5];
					for(int i = 0; i < result.size(); i++) {
						String u = result.get(i);
						data[i] = new Object[] {u};					
					}
					populateStore(data);
					loadData(null);
				}
				
			});
			

		} catch (Exception e) {
			Window.alert("doRefresh: " + e.getMessage());
		}
	}
	
	private void populateStore(Object[][] data) {

		mStore = new SimpleStore(new String[]{"name"},
				data); 
		
		usersList.setStore(mStore);
		mStore.load();
	}
	
	private void loadData(Record record) {
		permsset.clear();
		if(record != null) {

			final UserOperationsInterfaceAsync user = GWT.create(UserOperationsInterface.class);
			GroupOperationsInterfaceAsync groups = GWT.create(GroupOperationsInterface.class);
			groups.listGroups(SvnAdministration.getInstance().getCredentials(), new AsyncCallback<List<String>>() {

				public void onFailure(Throwable caught) {}

				public void onSuccess(List<String> result) {
					for(final String project: result) {
						final PermWidget widget = new PermWidget(project, false, false);
						user.canRead(SvnAdministration.getInstance().getCredentials(), project, usersList.getValue(), new AsyncCallback<Boolean>() {
							public void onFailure(Throwable caught) {}

							public void onSuccess(Boolean result) {
								widget.canRead.setValue(result);
							}
						});
						user.canWrite(SvnAdministration.getInstance().getCredentials(), project, usersList.getValue(), new AsyncCallback<Boolean>() {
							public void onFailure(Throwable caught) {}

							public void onSuccess(Boolean result) {
								widget.canWrite.setValue(result);
							}
						});
						permsset.add(widget);
					}
					permsset.doLayout();
				}
				
			});
			
		} 
	}
	
	private class PermWidget extends FieldSet {

		private Checkbox canRead = new Checkbox("Read");
		private Checkbox canWrite = new Checkbox("Write");
		private Button save = new Button("Save");
		
		public PermWidget(final String groupName, boolean read, boolean write) {
			setTitle(groupName);
			add(canRead);
			add(canWrite);
			addButton(save);
			canRead.setValue(read);
			canWrite.setValue(write);
			
			save.addListener(new ButtonListenerAdapter() {
				@Override
				public void onClick(Button button, EventObject e) {
					UserOperationsInterfaceAsync user = GWT.create(UserOperationsInterface.class);
					user.setAccessPermissions(SvnAdministration.getInstance().getCredentials(),
							usersList.getValue(), groupName, canRead.getValue(), canWrite.getValue(), new AsyncCallback<Void>() {

								public void onFailure(Throwable caught) {}

								public void onSuccess(Void result) {}
						
					});
					super.onClick(button, e);
				}
			});
			
		}


	}

}
