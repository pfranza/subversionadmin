package com.gorthaur.svnadmin.client.ui.forms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.gorthaur.svnadmin.client.SvnAdministration;
import com.gorthaur.svnadmin.client.ui.forms.widgets.DualSelector;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.HttpProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.data.XmlReader;
import com.gwtext.client.data.event.StoreListenerAdapter;
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
				
				StringBuffer b = new StringBuffer();
				for (Iterator<String> iterator = selector.getItemsToAdd().iterator(); iterator.hasNext();) {
					String string = iterator.next();
					b.append(string);
					if(iterator.hasNext()) {
						b.append(",");
					}
				}
				
				StringBuffer c = new StringBuffer();
				for (Iterator<String> iterator = selector.getItemsToRemove().iterator(); iterator.hasNext();) {
					String string = iterator.next();
					c.append(string);
					if(iterator.hasNext()) {
						c.append(",");
					}
				}
				
				RequestBuilder rb = new RequestBuilder(RequestBuilder.GET, URL.encode("/rest/updateGroup?username="+SvnAdministration.getInstance().getUsername()+
						"&passwd=" + SvnAdministration.getInstance().getPassword()+
						"&groupName=" + groupsList.getValue() +
						"&addUsers=" + b.toString() + 
						"&removeUsers=" + c.toString()));
				try {
					rb.sendRequest("", new RequestCallback() {

						public void onError(Request request, Throwable exception) {
							Window.alert("Error: " + exception.getMessage());
						}

						public void onResponseReceived(Request request, Response response) {
							resetForm();
						}
						
					});
				} catch (RequestException ex) {
					Window.alert("Error: " + ex.getMessage());
				}
				
			};
		});
	}};
	private Button delete = new Button("Delete Group"){{
		addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				
				RequestBuilder rb = new RequestBuilder(RequestBuilder.GET, URL.encode("/rest/deleteGroup?username="+SvnAdministration.getInstance().getUsername()+
						"&passwd=" + SvnAdministration.getInstance().getPassword()+
						"&groupName=" + groupsList.getValue()));
				try {
					rb.sendRequest("", new RequestCallback() {

						public void onError(Request request, Throwable exception) {
							Window.alert("Error: " + exception.getMessage());
						}

						public void onResponseReceived(Request request, Response response) {
							resetForm();
						}
						
					});
				} catch (RequestException ex) {
					Window.alert("Error: " + ex.getMessage());
				}
			};
		});
	}};

	private void refreshDataStore() {
		try {
			groupsList.clearValue();
			mStore.reload();
			groupsList.reset();
		} catch (Exception e) {
			Window.alert("doRefresh: " + e.getMessage());
		}
	}
	
	
	public ModifyGroupFormPanel() {
		
		addListener(new PanelListenerAdapter() {

			boolean done = false;
			
			public void onShow(Component component) {
				if(!done) {
					mStore = new Store(
							new HttpProxy(URL.encode("/rest/listGroups?username="+SvnAdministration.getInstance().getUsername()+
									"&passwd=" + SvnAdministration.getInstance().getPassword())),
									new XmlReader("row", new RecordDef(new FieldDef[] {
											new StringFieldDef("name", "name")
									})){{
										setTotalRecords("results");
									}});

					groupsList.setStore(mStore);
					mStore.load(0, 10);
					done = true;
					groupsList.autoSize();
				} else {
					refreshDataStore();
				}
				
				super.onShow(component);
			}
		});

		setBorder(false);
		setPaddings(15);
		
		FormPanel form = new FormPanel();

		form.setTitle("Modify Group");  
		form.setFrame(false);  
		form.setPaddings(5, 5, 5, 0);  
		form.setWidth(500);  
		form.setLabelWidth(75);  
		
		FieldSet set = new FieldSet();
		set.setCollapsible(false);  
		set.setAutoHeight(true); 
		set.add(groupsList);
		
		form.add(set);
		FieldSet set2 = new FieldSet("Members");
		set2.add(selector);
		form.add(set2);

		form.addButton(delete);
		form.addButton(save);
		
		add(form);
		
		groupsList.addListener(new ComboBoxListenerAdapter() {
			@Override
			public void onSelect(ComboBox comboBox, Record record, int index) {
				createInclusionStore(getFeedUrl() + "&groupName=" + record.getAsString("name")).load();
				createExclusionStore(getFeedUrl() + "&groupName=" + record.getAsString("name")).load();						
				super.onSelect(comboBox, record, index);
			}
		});

	}
	
	private String getFeedUrl() {
		return "/rest/listGroupMembership?username="+SvnAdministration.getInstance().getUsername()+
		"&passwd=" + SvnAdministration.getInstance().getPassword();
	}
	
	private Store createExclusionStore(String feed) {
		return new Store(
				new HttpProxy(URL.encode(feed)),
						new XmlReader("excname", new RecordDef(new FieldDef[] {
								new StringFieldDef("name", "name")
						}))) {{
							addStoreListener(new StoreListenerAdapter() {
								@Override
								public void onLoad(Store store, Record[] records) {
									List<String> exc = new ArrayList<String>();
									for(Record r: records) {
										exc.add(r.getAsString("name"));
									}

									selector.populateExcluded(exc.toArray(new String[exc.size()]));
									super.onLoad(store, records);
								}
							});
						}};
	}

	private Store createInclusionStore(String feed) {
		return new Store(
				new HttpProxy(URL.encode(feed)),
						new XmlReader("incname", new RecordDef(new FieldDef[] {
								new StringFieldDef("name", "name")
						}))) {{
							addStoreListener(new StoreListenerAdapter() {
								@Override
								public void onLoad(Store store, Record[] records) {
									List<String> exc = new ArrayList<String>();
									for(Record r: records) {
										exc.add(r.getAsString("name"));
									}

									selector.populateIncluded(exc.toArray(new String[exc.size()]));
									super.onLoad(store, records);
								}
							});
						}};
	}

	private void resetForm() {
		refreshDataStore();
		selector.reset();
		
	}
	
}
