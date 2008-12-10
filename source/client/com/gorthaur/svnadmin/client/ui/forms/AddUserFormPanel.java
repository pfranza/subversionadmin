package com.gorthaur.svnadmin.client.ui.forms;


import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.gorthaur.svnadmin.client.SvnAdministration;
import com.gorthaur.svnadmin.client.ui.listeners.ClickListener;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.ValidationException;
import com.gwtext.client.widgets.form.Validator;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;

public class AddUserFormPanel extends Panel {

	private TextField username = new TextField("Username", "username", 210){{
		setAllowBlank(false);
		setInvalidText("Invalid Username.");
		setValidator(new Validator() {
			public boolean validate(String value) throws ValidationException {
				return value.trim().length() > 2;
			}
		});
		addListener(new TextFieldListenerAdapter() {
			public void onBlur(Field field) {
				setButtonStatus();
				super.onBlur(field);
			}
		});
		
	}};
	private TextField password = new TextField("Password", "password", 210){{
		setAllowBlank(false);
		setInvalidText("Invalid Password");
		setMaxLength(8);
		setValidator(new Validator() {
			public boolean validate(String value) throws ValidationException {
				return value.trim().length() > 2 && value.trim().length() < 9;
			}
		});
		addListener(new TextFieldListenerAdapter() {
			public void onBlur(Field field) {
				setButtonStatus();
				super.onBlur(field);
			}
		});
		
	}};
	private TextField password_again = new TextField("Password Again", "password_again", 210){{
		setAllowBlank(false);
		setInvalidText("Passwords don't match.");
		setMaxLength(8);
		setValidator(new Validator() {
			public boolean validate(String value) throws ValidationException {
				return value.equals(password.getText());
			}
		});
		addListener(new TextFieldListenerAdapter() {
			public void onBlur(Field field) {
				setButtonStatus();
				super.onBlur(field);
			}
		});
		
	}};
	private TextField email = new TextField("Email", "email", 210){{
		setAllowBlank(false);
		setInvalidText("Invalid Email Address.");
		setValidator(new Validator() {
			public boolean validate(String value) throws ValidationException {
				return value.trim().length() > 0 && value.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
			}
		});
		addListener(new TextFieldListenerAdapter() {
			public void onBlur(Field field) {
				setButtonStatus();
				super.onBlur(field);
			}
		});
		
	}};
	
	private Button submit = new Button("Add User"){{
		setDisabled(true);
	}};
	private Button clear = new Button("Clear");
	
	public AddUserFormPanel() {

		setBorder(false);
		setPaddings(15);
		
		FormPanel form = new FormPanel();
		
		form.setTitle("Add User");  
		form.setFrame(false);  
		form.setPaddings(5, 5, 5, 0);  
		form.setWidth(350);  
		form.setLabelWidth(75);  
		
		password.setPassword(true);
		password_again.setPassword(true);
		
		FieldSet set = new FieldSet("User Info"); 
		set.setCollapsible(false);  
		set.setAutoHeight(true); 
		set.setPaddings(5);
		
		set.add(username);
		set.add(email);
		set.add(password);
		set.add(password_again);
		
		
		set.addButton(clear);
		set.addButton(submit);
		
		form.add(set);
				
		clear.addListener(new ClickListener() {
			public void onClick(Button button, EventObject e) {
				clearForm();
			}
		});
		
		submit.addListener(new ClickListener() {
			public void onClick(Button button, EventObject e) {
				RequestBuilder rb = new RequestBuilder(RequestBuilder.GET, URL.encode("/rest/adduser?username="+
						SvnAdministration.getInstance().getUsername()+
						"&passwd="+SvnAdministration.getInstance().getPassword()+
						"&newUsername="+username.getText()+
						"&newPassword="+password.getText()+
						"&newEmail="+email.getText()
						));
				try {
					rb.sendRequest("", new RequestCallback() {

						public void onError(Request request, Throwable exception) {
							com.google.gwt.user.client.Window.alert("Error Processing Form");
						}

						public void onResponseReceived(Request request,
								Response response) {
							if(response.getText().trim().equalsIgnoreCase("ok")) {
								com.google.gwt.user.client.Window.alert("User Added");
								clearForm();
							} else {
								com.google.gwt.user.client.Window.alert("Access Denied");
							}
						}
						
					});
				} catch (RequestException e1) {
					com.google.gwt.user.client.Window.alert("Error: " + e1.getMessage());
				}
			}
		});
		
		add(form);
		clearForm();
		
	}

	private void clearForm() {
		username.setValue("");
		password.setValue("");
		password_again.setValue("");
		email.setValue("");
		setButtonStatus();
	}
	
	private void setButtonStatus() {
		boolean valid = username.isValid() && email.isValid() && 
			password.isValid() && password_again.isValid();
		submit.setDisabled(!valid);
	}
	
}
