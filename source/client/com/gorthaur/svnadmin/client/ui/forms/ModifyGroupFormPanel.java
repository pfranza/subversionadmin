package com.gorthaur.svnadmin.client.ui.forms;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gorthaur.svnadmin.client.SvnAdministration;
import com.gorthaur.svnadmin.client.rpcinterface.GroupOperationsInterface;
import com.gorthaur.svnadmin.client.rpcinterface.GroupOperationsInterfaceAsync;
import com.gorthaur.svnadmin.client.ui.forms.widgets.DualSelector;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;

public class ModifyGroupFormPanel extends Panel {

	private Store mStore;
	
	private final ComboBox groupsList = new ComboBox() {{
		setMinChars(1);  
		setFieldLabel("Select Group");   
		setMode(ComboBox.REMOTE);  
		setTriggerAction(ComboBox.ALL);  
		setEmptyText("Select Group");  
		setLoadingText("Searching...");  
		setTypeAhead(true);  
		setSelectOnFocus(true);  
		setWidth(300);
		setPageSize(10);
		setGrow(true);
		setDisplayField("name");	
		
	}};
	
	private final DualSelector selector = new DualSelector();
	
	private Button save = new Button("Save Changes"){{
		addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				
				GroupOperationsInterfaceAsync groups = GWT.create(GroupOperationsInterface.class);
				for (Iterator<String> iterator = selector.getItemsToAdd().iterator(); iterator.hasNext();) {
					String string = iterator.next();
					groups.addMember(SvnAdministration.getInstance().getCredentials(), groupsList.getValue(), string, new AsyncCallback<List<Void>>() {

						public void onFailure(Throwable caught) {
							Window.alert(caught.getMessage());
						}

						public void onSuccess(List<Void> result) {}
						
					});
				}
				
				for (Iterator<String> iterator = selector.getItemsToRemove().iterator(); iterator.hasNext();) {
					String string = iterator.next();
					groups.removeMember(SvnAdministration.getInstance().getCredentials(), groupsList.getValue(), string, new AsyncCallback<List<Void>>() {

						public void onFailure(Throwable caught) {
							Window.alert(caught.getMessage());
						}

						public void onSuccess(List<Void> result) {}
						
					});
				}
				
				
				resetForm();
			};
		});
	}};
	private Button delete = new Button("Delete Group"){{
		addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				
				GroupOperationsInterfaceAsync groups = GWT.create(GroupOperationsInterface.class);
				groups.deleteGroup(SvnAdministration.getInstance().getCredentials(), groupsList.getValue(), new AsyncCallback<List<Void>>() {

					public void onFailure(Throwable caught) {
						Window.alert(caught.getMessage());
					}

					public void onSuccess(List<Void> result) {
						resetForm();
					}
					
				});
			};
		});
	}};

	private void refreshDataStore() {
		try {
			groupsList.clearValue();
			groupsList.reset();
			
			GroupOperationsInterfaceAsync groups = GWT.create(GroupOperationsInterface.class);
			groups.listGroups(SvnAdministration.getInstance().getCredentials(), new AsyncCallback<List<String>>() {

				public void onFailure(Throwable caught) {
					Window.alert(caught.getMessage());
				}

				public void onSuccess(List<String> result) {
					
					Object[][] data = new Object[result.size()][5];
					for(int i = 0; i < result.size(); i++) {
						String u = result.get(i);
						data[i] = new Object[] {
								u
						};					
					}
					populateStore(data);
					loadData(null);
				}
				
			});
			

		} catch (Exception e) {
			Window.alert("doRefresh: " + e.getMessage());
		}
	}
	
	
	protected void loadData(Record object) {
		if(object != null) {
			GroupOperationsInterfaceAsync groups = GWT.create(GroupOperationsInterface.class);
			groups.listGroupMembers(SvnAdministration.getInstance().getCredentials(), object.getAsString("name"),
					new AsyncCallback<List<String>>() {

				public void onFailure(Throwable caught) {
					Window.alert(caught.getMessage());
				}

				public void onSuccess(List<String> result) {
					selector.populateIncluded(result.toArray(new String[result.size()]));
				}

			});

			groups.listGroupNonMembers(SvnAdministration.getInstance().getCredentials(), object.getAsString("name"),
					new AsyncCallback<List<String>>() {

				public void onFailure(Throwable caught) {
					Window.alert(caught.getMessage());
				}

				public void onSuccess(List<String> result) {
					selector.populateExcluded(result.toArray(new String[result.size()]));
				}

			});
		}
		save.setDisabled( object == null );
	}


	protected void populateStore(Object[][] data) {
		mStore = new SimpleStore(new String[]{"name"},
				data); 
		
		groupsList.setStore(mStore);
		mStore.load();
	}


	public ModifyGroupFormPanel() {
		
		final FormPanel form = new FormPanel();
		
		final FieldSet set = new FieldSet();
		set.setCollapsible(false);  
		set.setAutoHeight(true);
		
		addListener(new PanelListenerAdapter() {

			boolean done = false;
			
			public void onShow(Component component) {
				if(!done) {					
					set.add(groupsList);
					set.doLayout();
					
					done = true;
				} 
					refreshDataStore();
				
				super.onShow(component);
			}
		});

		setBorder(false);
		setPaddings(15);
		
		

		form.setTitle("Modify Group");  
		form.setFrame(false);  
		form.setPaddings(5, 5, 5, 0);  
		form.setWidth(500);  
		form.setLabelWidth(75);  
				
		
		form.add(set);
		FieldSet set2 = new FieldSet("Members");
		set2.add(selector);
		form.add(set2);

		form.addButton(delete);
		form.addButton(save);
		setAutoScroll(true);
		add(form);

		groupsList.addListener(new ComboBoxListenerAdapter() {
			@Override
			public void onSelect(ComboBox comboBox, Record record, int index) {
				loadData(record);
				super.onSelect(comboBox, record, index);
			}
		});
		loadData(null);

	}
	


	private void resetForm() {
		refreshDataStore();
		selector.reset();		
	}
	
}
