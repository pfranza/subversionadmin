package com.gorthaur.svnadmin.client.ui.forms;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gorthaur.svnadmin.client.SvnAdministration;
import com.gorthaur.svnadmin.client.rpcinterface.GroupOperationsInterface;
import com.gorthaur.svnadmin.client.rpcinterface.GroupOperationsInterfaceAsync;
import com.gorthaur.svnadmin.client.ui.forms.widgets.DualSelector;
import com.gorthaur.svnadmin.client.ui.listeners.ClickListener;
import com.gwtext.client.core.EventObject;
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
				
				if(value.trim().length() < 2) {
					return false;
				}
				
				return !existingNames.contains(value);
			}
			
		});
		
		addListener(new FieldListenerAdapter() {
			
			@Override
			public void onChange(Field field, Object newVal, Object oldVal) {
				populateMap();
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
	
	private final List<String> existingNames = new ArrayList<String>();
	
	private void populateMap() {
		
		GroupOperationsInterfaceAsync group = GWT.create(GroupOperationsInterface.class);
		group.listGroups(SvnAdministration.getInstance().getCredentials(), new AsyncCallback<List<String>>() {

			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}

			public void onSuccess(List<String> result) {
				existingNames.clear();
				existingNames.addAll(result);
			}
			
		});
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
		setAutoScroll(true);
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
		groupName.setValue("");
		selector.reset();
		setButtonDisabled(true);

		GroupOperationsInterfaceAsync group = GWT.create(GroupOperationsInterface.class);
		group.listGroupNonMembers(SvnAdministration.getInstance().getCredentials(), groupName.getValueAsString(), new AsyncCallback<List<String>>() {

			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}

			public void onSuccess(List<String> result) {
				selector.reset();
				selector.populateExcluded(result.toArray(new String[result.size()]));
			}
			
		});
		
	}

	private void submitForm() {
	
		
		GroupOperationsInterfaceAsync group = GWT.create(GroupOperationsInterface.class);
		group.createGroup(SvnAdministration.getInstance().getCredentials(), 
				groupName.getText(), selector.getItemsToAdd(), new AsyncCallback<String>() {

					public void onFailure(Throwable caught) {
						Window.alert("Error: " + caught.getMessage());
					}

					public void onSuccess(String result) {
						// TODO Auto-generated method stub
						if(result.equalsIgnoreCase("ok")) {
							resetForm();
						} else {
							Window.alert(result);
						}
					}
			
		});
		
		setButtonDisabled(true);
		
	}
	
	

}
