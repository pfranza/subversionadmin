package com.gorthaur.svnadmin.client.ui.forms;

import com.google.gwt.http.client.URL;
import com.gorthaur.svnadmin.client.SvnAdministration;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.HttpProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.data.XmlReader;
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
		setDisplayField("name");		
	}};
	
	private UserPreferencesPanel userPrefs = new UserPreferencesPanel();
	
	public ModifyUserFormPanel() {

		addListener(new PanelListenerAdapter() {

			boolean done = false;
			
			public void onShow(Component component) {
				if(!done) {
					Store mStore = new Store(
							new HttpProxy(URL.encode("/rest/listUsers?username="+SvnAdministration.getInstance().getUsername()+
									"&passwd=" + SvnAdministration.getInstance().getPassword())),
									new XmlReader("row", new RecordDef(new FieldDef[] {
											new StringFieldDef("name", "name"),
											new StringFieldDef("email"),
											new StringFieldDef("admin"),
											new StringFieldDef("subscriptions"),
											new StringFieldDef("group")
									})){{
										setTotalRecords("results");
									}});

					usersList.setStore(mStore);
					mStore.load(0, 10);
					done = true;
				}
				
				super.onShow(component);
			}
		});

		setBorder(false);
		setPaddings(15);
		
		FormPanel form = new FormPanel();

		form.setTitle("Modify User");  
		form.setFrame(false);  
		form.setPaddings(5, 5, 5, 0);  
		form.setWidth(460);  
		form.setLabelWidth(75);  
		
		FieldSet set = new FieldSet();
		set.setCollapsible(false);  
		set.setAutoHeight(true); 
		set.add(usersList);
		
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
		
		
		private Button save = new Button("Save Changes");
		
		public void loadSettings(Record record) {
			name.setValue(record.getAsString("name"));
			email.setValue(record.getAsString("email"));
			isAdmin.setValue(Boolean.valueOf(record.getAsString("admin")));
		}

		
		public UserPreferencesPanel() {
			add(name);
			add(email);
			add(isAdmin);
			FieldSet pwset = new FieldSet("Leave Blank To Keep Unchanged");
			pwset.add(password);
			pwset.add(password_again);
			add(pwset);
			addButton(save);
		}
		
	}
	
}
