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
import com.gorthaur.svnadmin.client.rpcinterface.beans.UserInfo;
import com.gorthaur.svnadmin.client.ui.forms.widgets.DualSelector;
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
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.ValidationException;
import com.gwtext.client.widgets.form.Validator;
import com.gwtext.client.widgets.form.event.CheckboxListenerAdapter;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;

public class ModifyUserFormPanel extends Panel {

	private Store mStore;

	private final ComboBox usersList = new ComboBox() {{
		setMinChars(1);  
		setFieldLabel("Select User");   
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
	
	
	private final ChangeEmailPanel emailPanel = new ChangeEmailPanel();
	private final ChangePasswordPanel passwordPanel = new ChangePasswordPanel();
	private final ChangeAdminPanel adminPanel = new ChangeAdminPanel();
	private final ChangeGroupMembersipPanel membersipPanel = new ChangeGroupMembersipPanel();
	private final ChangeSubscriptionPanel subscriptionPanel = new ChangeSubscriptionPanel();
	
	private final Button deleteUser = new Button("Delete User") {{
		addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				UserOperationsInterfaceAsync user = GWT.create(UserOperationsInterface.class);
				user.deleteUser(SvnAdministration.getInstance().getCredentials(), usersList.getValue(), voidCallback);
				refreshDataStore();
				super.onClick(button, e);
			}
		});
	}};
	private final AsyncCallback<Void> voidCallback = new AsyncCallback<Void>() {

		public void onFailure(Throwable caught) {
			Window.alert("Error: " + caught.getMessage());
		}

		public void onSuccess(Void result) {
			Window.alert("Saved");
		}
		
	};

	private void refreshDataStore() {
		try {
			usersList.clearValue();
			usersList.reset();
			
			UserOperationsInterfaceAsync user = GWT.create(UserOperationsInterface.class);
			user.getAllUsers(SvnAdministration.getInstance().getCredentials(), new AsyncCallback<List<UserInfo>>() {

				public void onFailure(Throwable caught) {
					Window.alert(caught.getMessage());
				}

				public void onSuccess(List<UserInfo> result) {
					
					Object[][] data = new Object[result.size()][5];
					for(int i = 0; i < result.size(); i++) {
						UserInfo u = result.get(i);
						data[i] = new Object[] {
								u.getName(),
								u.getEmail(),
								Boolean.toString(u.isAdmin()),
								u.getSubscriptionsCSVString(),
								u.getGroupMembershipCSVString()
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
	
	
	public ModifyUserFormPanel() {

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

		setBorder(false);
		setPaddings(15);
		
		FormPanel form = new FormPanel();

		form.setTitle("Modify User");  
		form.setFrame(false);  
		form.setPaddings(5, 5, 5, 0);  
		form.setWidth(500);  
		form.setLabelWidth(75);  
		
		form.add(set);
		form.add(emailPanel);
		form.add(passwordPanel);
		form.add(adminPanel);
		form.add(membersipPanel);
		form.add(subscriptionPanel);
		
		form.addButton(deleteUser);
		
		add(form);
		setAutoScroll(true);
		
		usersList.addListener(new ComboBoxListenerAdapter() {
			@Override
			public void onSelect(ComboBox comboBox, Record record, int index) {
				loadData(record);
				super.onSelect(comboBox, record, index);
			}
		});
		loadData(null);
	}
	
	private void populateStore(Object[][] data) {

		mStore = new SimpleStore(new String[]{"name", "email", "admin", "subscriptions", "group"},
				data); 
		
		usersList.setStore(mStore);
		mStore.load();
	}
	
	private void loadData(Record record) {
		emailPanel.loadSettings(record);
		passwordPanel.loadSettings(record);
		adminPanel.loadSettings(record);
		membersipPanel.loadSettings(record);
		subscriptionPanel.loadSettings(record);
		deleteUser.setDisabled(record == null);
	}

	private class ChangePasswordPanel extends FieldSet {
		
		private TextField password = new TextField("Password", "password") {{
			setValidator(new Validator() {

				public boolean validate(String value)
						throws ValidationException {
			
					return value.length() > 2;
				}
				
			});
			setPassword(true);
		}};
		
		private TextField password_again = new TextField("Password Again", "password") {{
			setValidator(new Validator() {

				public boolean validate(String value)
						throws ValidationException {
			
					return value.equals(password.getValueAsString());
				}
				
			});
			setPassword(true);
			addListener(new FieldListenerAdapter() {
				@Override
				public void onValid(Field field) {
					save.setDisabled(false);
					super.onValid(field);
				}
				
				@Override
				public void onInvalid(Field field, String msg) {
					save.setDisabled(true);
					super.onInvalid(field, msg);
				}
			});
		}};
		
		private Button save = new Button("Save") {{
			addListener(new ButtonListenerAdapter() {
				@Override
				public void onClick(Button button, EventObject e) {
					UserOperationsInterfaceAsync user = GWT.create(UserOperationsInterface.class);
					user.updatePassword(SvnAdministration.getInstance().getCredentials(), 
							usersList.getValue(), password.getValueAsString(), voidCallback);
					refreshDataStore();
					super.onClick(button, e);
				}
			});
			setDisabled(true);
		}};
		
		public ChangePasswordPanel() {
			setTitle("Password");
			setCollapsible(true);
			add(password);
			add(password_again);
			addButton(save);
		}

		public void loadSettings(Record record) {
			save.setDisabled(!(password_again.isValid() && password.isValid()) || record == null);
			password.setValue("");
			password_again.setValue("");
			password.setDisabled(record == null);
			password_again.setDisabled(record == null);
		}
		
		
	}
	
	private class ChangeEmailPanel extends FieldSet {
	
		private TextField email = new TextField("Email", "email") {{
			setValidator(new Validator() {
				public boolean validate(String value) throws ValidationException {
					return value.trim().length() > 3 && value.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
				}		
			});
			
			addListener(new FieldListenerAdapter() {
				@Override
				public void onValid(Field field) {
					save.setDisabled(false);
					super.onValid(field);
				}
				
				@Override
				public void onInvalid(Field field, String msg) {
					save.setDisabled(true);
					super.onInvalid(field, msg);
				}
			});
			
		}};
		
		private Button save = new Button("Save") {{
			setDisabled(true);
			
			addListener(new ButtonListenerAdapter() {
				@Override
				public void onClick(Button button, EventObject e) {
					
					UserOperationsInterfaceAsync user = GWT.create(UserOperationsInterface.class);
					user.updateEmailAddress(SvnAdministration.getInstance().getCredentials(),
							usersList.getValue(), email.getValueAsString(), voidCallback);
					refreshDataStore();
					super.onClick(button, e);
				}
			});
			
		}};
		
		public ChangeEmailPanel() {
			setTitle("Email");
			setCollapsible(true);
			add(email);
			addButton(save);
		}

		public void loadSettings(Record record) {
			email.setValue(record != null ? record.getAsString("email") : "");
			save.setDisabled(!email.isValid() || record == null);
			email.setDisabled(record == null);
		}
		
	}
	
	private class ChangeAdminPanel extends FieldSet {
		private Checkbox isAdmin = new Checkbox("Is Administrator", "isadmin") {{
			setValue(false);
			addListener(new CheckboxListenerAdapter() {
				@Override
				public void onChange(Field field, Object newVal, Object oldVal) {
					save.setDisabled(false);
					super.onChange(field, newVal, oldVal);
				}
			});
		}};
		
		private Button save = new Button("Save") {{
			setDisabled(true);
			
			addListener(new ButtonListenerAdapter() {
				@Override
				public void onClick(Button button, EventObject e) {
					
					UserOperationsInterfaceAsync user = GWT.create(UserOperationsInterface.class);
					user.updateIsAdmin(SvnAdministration.getInstance().getCredentials(),
							usersList.getValue(), isAdmin.getValue(), voidCallback);
					refreshDataStore();
					super.onClick(button, e);
				}
			});
			
		}};
		
		public ChangeAdminPanel() {
			setTitle("Admin");
			setCollapsible(true);
			add(isAdmin);
			addButton(save);
		}

		public void loadSettings(Record record) {
			isAdmin.setValue(record != null ? Boolean.valueOf(record.getAsString("admin")) : false);
			save.setDisabled(record == null);
			isAdmin.setDisabled(record == null);
		}
	}
	
	private class ChangeGroupMembersipPanel extends FieldSet {
		private DualSelector selector = new DualSelector();
		
		
		private Button save = new Button("Save") {{
//			setDisabled(true);
			
			addListener(new ButtonListenerAdapter() {
				@Override
				public void onClick(Button button, EventObject e) {
					
					UserOperationsInterfaceAsync user = GWT.create(UserOperationsInterface.class);
					for(String s: selector.getItemsToAdd()) {
						user.joinGroup(SvnAdministration.getInstance().getCredentials(), usersList.getValue(), s, voidCallback);
					}
					for(String r: selector.getItemsToRemove()) {
						user.leaveGroup(SvnAdministration.getInstance().getCredentials(), usersList.getValue(), r, voidCallback);
					}

					refreshDataStore();
					super.onClick(button, e);
				}
			});
			
		}};
		
		public ChangeGroupMembersipPanel() {
			setTitle("Group Membership");
			setCollapsible(true);
			setCollapsed(true);
			add(selector);
			addButton(save);
		}
		
		public void loadSettings(Record record) {
			selector.reset();
			if(record != null) {
				final String[] inc = record.getAsString("group").split(",");
				selector.populateIncluded(inc);

				GroupOperationsInterfaceAsync groups = GWT.create(GroupOperationsInterface.class);

				groups.listGroups(SvnAdministration.getInstance().getCredentials(), new AsyncCallback<List<String>>() {

					public void onFailure(Throwable caught) {}

					public void onSuccess(List<String> result) {
						for(String i: inc) {
							result.remove(i);
						}
						selector.populateExcluded(result.toArray(new String[result.size()]));
					}

				});
			}
			save.setDisabled(record == null);

		}
			
		
	}
	
	private class ChangeSubscriptionPanel extends FieldSet {
		private DualSelector selector = new DualSelector();
		
		
		private Button save = new Button("Save") {{

			addListener(new ButtonListenerAdapter() {
				@Override
				public void onClick(Button button, EventObject e) {
					
					UserOperationsInterfaceAsync user = GWT.create(UserOperationsInterface.class);
					for(String s: selector.getItemsToAdd()) {
						user.addSubscription(SvnAdministration.getInstance().getCredentials(), usersList.getValue(), s, voidCallback);
					}
					for(String r: selector.getItemsToRemove()) {
						user.removeSubscription(SvnAdministration.getInstance().getCredentials(), usersList.getValue(), r, voidCallback);
					}

					refreshDataStore();
					super.onClick(button, e);
				}
			});
			
		}};
		
		public ChangeSubscriptionPanel() {
			setTitle("Subscriptions");
			setCollapsible(true);
			setCollapsed(true);
			add(selector);
			addButton(save);
		}
		
		public void loadSettings(Record record) {
			selector.reset();
			if(record != null) {
				final String[] inc = record.getAsString("subscriptions").split(",");
				selector.populateIncluded(inc);
				UserOperationsInterfaceAsync user = GWT.create(UserOperationsInterface.class);
				user.getAllProjects(SvnAdministration.getInstance().getCredentials(), new AsyncCallback<List<String>>() {

					public void onFailure(Throwable caught) {}

					public void onSuccess(List<String> result) {
						for(String i: inc) {
							result.remove(i);
						}
						selector.populateExcluded(result.toArray(new String[result.size()]));
					}

				});
			}
			save.setDisabled(record == null);
		}
			
		
	}
	
}
