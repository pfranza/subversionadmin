package com.gorthaur.svnadmin.client.ui.forms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.gorthaur.svnadmin.client.SvnAdministration;
import com.gorthaur.svnadmin.client.ui.forms.widgets.DualSelector;
import com.gorthaur.svnadmin.client.ui.listeners.ClickListener;
import com.gwtext.client.core.EventObject;
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
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.ValidationException;
import com.gwtext.client.widgets.form.Validator;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;

public class AddGroupPanel extends Panel {
	
	private TextField groupName = new TextField("Group Name"){{
		setValidateOnBlur(true);
		setValidationDelay(750);
				
		setValidator(new Validator() {
			
			public boolean validate(final String value) throws ValidationException {
				String rsp = responseMap.get(value);
				if(rsp == null) {
					populateMap(value);
				} else {
					if(rsp.equalsIgnoreCase("ok")) {
						return true;
					} else {
						groupName.setInvalidText(rsp);
					}
				}		
				
				return false;
			}
			
		});
		
		addListener(new FieldListenerAdapter() {
			
			@Override
			public void onChange(Field field, Object newVal, Object oldVal) {
				populateMap(newVal.toString());
				super.onChange(field, newVal, oldVal);
			}
			
			@Override
			public void onValid(Field field) {
				setButtonDisabled(false);
				super.onValid(field);
			}
			
			@Override
			public void onInvalid(Field field, String msg) {
				setButtonDisabled(true);
				super.onInvalid(field, msg);
			}
			
		});
		
	}
	
	private final Map<String, String> responseMap = new HashMap<String, String>();
	
	private void populateMap(final String value) {
		RequestBuilder rb = new RequestBuilder(RequestBuilder.GET, URL.encode("/rest/checkGroupName?username="+
				SvnAdministration.getInstance().getUsername()+
				"&passwd=" + SvnAdministration.getInstance().getPassword() +
				"&groupName=" + value.trim()));
		try {
			rb.sendRequest("", new RequestCallback() {

				public void onError(Request request, Throwable exception) {
					groupName.isValid(false);
					groupName.setInvalidText("Error Validating Field");
				}

				public void onResponseReceived(Request request,
						Response response) {
					responseMap.put(value, response.getText());
				}
				
			});
		} catch (RequestException e) {
			Window.alert("Error: " + e.getMessage());
		}
	}
	
	};
	
	private DualSelector selector = new DualSelector();
	
	private Button add = new Button(getFormTitle()){{
		addListener(new ClickListener() {
			public void onClick(Button button, com.gwtext.client.core.EventObject e) {
				submitForm();
			};
		});
	}};
	
	private Button reset = new Button("Reset") {{
		addListener(new ClickListener() {
			public void onClick(Button button, EventObject e) {
				resetForm();
			}
		});
	}};

	private FieldSet set;
	
	private void setButtonDisabled(boolean b) {
		add.setDisabled(b);
		set.setDisabled(b);
	}
	
	public AddGroupPanel() {
		setBorder(false);
		setPaddings(15);
		
		FormPanel form = new FormPanel();
		
		form.setTitle(getFormTitle());  
		form.setFrame(false);  
		form.setPaddings(5, 5, 5, 0);  
		form.setWidth(550);  
		form.setLabelWidth(75);  
		
		
		set = new FieldSet("Members");
		set.add(selector);
		
		
		form.add(groupName);
		form.add(set);
		
		form.addButton(reset);
		form.addButton(add);
		
		add(form);
		
		addListener(new PanelListenerAdapter() {
			@Override
			public void onShow(Component component) {
				resetForm();
			}
		});
		
	}
	
	protected String getFormTitle() {
		return "Add Group";
	}

	private void resetForm() {
		groupName.setValue(getDefaultGroupName());
		selector.reset();
		setButtonDisabled(true);

		createInclusionStore().load();
		createExclusionStore().load();
	}

	private Store createExclusionStore() {
		return new Store(
				new HttpProxy(URL.encode(getFeedUrl())),
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

	private Store createInclusionStore() {
		return new Store(
				new HttpProxy(URL.encode(getFeedUrl())),
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
	
	private String getFeedUrl() {
		return "/rest/listGroupMembership?username="+SvnAdministration.getInstance().getUsername()+
		"&passwd=" + SvnAdministration.getInstance().getPassword();
	}
	
	protected String getDefaultGroupName() {
		return "";
	}

	protected void submitForm() {
		
		StringBuffer b = new StringBuffer();
		for (Iterator<String> iterator = selector.getItemsToAdd().iterator(); iterator.hasNext();) {
			String string = iterator.next();
			b.append(string);
			if(iterator.hasNext()) {
				b.append(",");
			}
		}
		
		RequestBuilder rb = new RequestBuilder(RequestBuilder.GET, URL.encode("/rest/createGroup?username="+SvnAdministration.getInstance().getUsername()+
				"&passwd=" + SvnAdministration.getInstance().getPassword()+
				"&groupName=" + groupName.getText() +
				"&members=" + b.toString()));
		try {
			rb.sendRequest("", new RequestCallback() {

				public void onError(Request request, Throwable exception) {
					Window.alert("Error: " + exception.getMessage());
				}

				public void onResponseReceived(Request request, Response response) {
					resetForm();
				}
				
			});
		} catch (RequestException e) {
			Window.alert("Error: " + e.getMessage());
		}
		setButtonDisabled(true);
		
	}
	
	

}
