package com.gorthaur.svnadmin.client.ui.forms;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gorthaur.svnadmin.client.SvnAdministration;
import com.gorthaur.svnadmin.client.rpcinterface.UserOperationsInterface;
import com.gorthaur.svnadmin.client.rpcinterface.UserOperationsInterfaceAsync;
import com.gorthaur.svnadmin.client.rpcinterface.beans.UserInfo;
import com.gorthaur.svnadmin.client.ui.listeners.ClickListener;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.ValidationException;
import com.gwtext.client.widgets.form.Validator;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;

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
	
	private UserPreferencesPanel userPrefs = new UserPreferencesPanel(new UserPreferencesPanelListener() {

		public void doRefresh() {
			refreshDataStore();
		}	
		
	});

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
								"",
								""
						};					
					}
					populateStore(data);
					
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
		form.add(userPrefs);

		add(form);
		
		usersList.addListener(new ComboBoxListenerAdapter() {
			@Override
			public void onSelect(ComboBox comboBox, Record record, int index) {
				userPrefs.loadSettings(record);
				super.onSelect(comboBox, record, index);
			}
		});

	}
	
	private void populateStore(Object[][] data) {

		mStore = new SimpleStore(new String[]{"name", "email", "admin", "subscriptions", "group"},
				data); 
		
		usersList.setStore(mStore);
		mStore.load();
	}
	
	private static class UserPreferencesPanel extends FieldSet {
		
		private TextField name = new TextField("User", "name") {{
			setReadOnly(true);
			setDisabled(true);
		}};
		
		private TextField email = new TextField("Email", "email") {{
			setValidator(new Validator() {
				public boolean validate(String value) throws ValidationException {
					return value.trim().length() > 0 && value.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
				}		
			});
		}};
		
		private TextField password = new TextField("Password", "password", 210){{
			setAllowBlank(true);
			setInvalidText("Invalid Password");
			setMaxLength(8);
			setPassword(true);
			setValidator(new Validator() {
				public boolean validate(String value) throws ValidationException {
					return (value.trim().length() > 2 && value.trim().length() < 9) || value.trim().length() == 0;
				}
			});
			
		}};
		private TextField password_again = new TextField("Password Again", "password_again", 210){{
			setAllowBlank(true);
			setInvalidText("Passwords don't match.");
			setMaxLength(8);
			setPassword(true);
			setValidator(new Validator() {
				public boolean validate(String value) throws ValidationException {
					return value.equals(password.getText());
				}
			});
					
		}};
		
		private Checkbox isAdmin = new Checkbox("Administrator", "isadmin") {{
			setValue(false);
		}};
		
		
		private Button save = new Button("Save Changes"){{
			addListener(new ClickListener() {

				public void onClick(Button button, EventObject e) {

					UserOperationsInterfaceAsync user = GWT
								.create(UserOperationsInterface.class);
						user.updateUser(SvnAdministration.getInstance()
								.getCredentials(), new UserInfo(name
								.getValueAsString(), email.getValueAsString(),
								isAdmin.getValue()) {
							private static final long serialVersionUID = -7619127437319717230L;
							{
								setNewPassword(password.getValueAsString());
							}
						}, new AsyncCallback<Void>() {

							public void onFailure(Throwable caught) {
							}

							public void onSuccess(Void result) {
								listener.doRefresh();
								reset();
							}

						});
					
				}
			
			});
		}};
		private Button delete = new Button("Delete User"){{
			addListener(new ClickListener() {

				public void onClick(Button button, EventObject e) {
					if (Window.confirm("Continue deleting user "
								+ name.getValueAsString() + "?")) {

							UserOperationsInterfaceAsync user = GWT
									.create(UserOperationsInterface.class);
							user.deleteUser(SvnAdministration.getInstance()
									.getCredentials(), name.getValueAsString(),
									new AsyncCallback<Void>() {

										public void onFailure(Throwable caught) {
										}

										public void onSuccess(Void result) {
											listener.doRefresh();
											reset();
										}

									});

						}
				}
				
			});
		}};

		private UserPreferencesPanelListener listener;
		
		public UserPreferencesPanel(UserPreferencesPanelListener listener) {
			add(name);
			add(email);
			add(isAdmin);
			
			FieldSet pwset = new FieldSet("Leave Blank To Keep Unchanged");
			pwset.add(password);
			pwset.add(password_again);
			add(pwset);			
			
			addButton(delete);
			addButton(save);
			this.listener = listener;
		}
		
		public void loadSettings(Record record) {
			name.setValue(record.getAsString("name"));
			email.setValue(record.getAsString("email"));
			isAdmin.setValue(Boolean.valueOf(record.getAsString("admin")));		
		}
		
		private void reset() {
			name.setValue("");
			email.setValue("");
			isAdmin.setValue(false);
			password.setValue("");
			password_again.setValue("");
		}
		
	}
	

	
	private interface UserPreferencesPanelListener {
		void doRefresh();
	}
	
}
