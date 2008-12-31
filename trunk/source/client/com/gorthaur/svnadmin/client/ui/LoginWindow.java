package com.gorthaur.svnadmin.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gorthaur.svnadmin.client.rpcinterface.AuthenticationInterface;
import com.gorthaur.svnadmin.client.rpcinterface.AuthenticationInterfaceAsync;
import com.gorthaur.svnadmin.client.ui.listeners.ClickListener;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.ValidationException;
import com.gwtext.client.widgets.form.Validator;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.layout.FormLayout;

public class LoginWindow extends Window {

	private List<LoginWindowListener> listeners = new ArrayList<LoginWindowListener>();
	
	private LoginPanel userdata = new LoginPanel(); 
	private Button loginButton = new Button("Login");
	private Button changePassword = new Button("Change Password");
	
	public LoginWindow() {
		
		setTitle("Login");  
		setClosable(false);
		setResizable(false);
		setModal(true);
		setWidth(275); 
		
		
		setLayout(new FormLayout());

		add(userdata);
		addButton(loginButton);

		loginButton.addListener(new ClickListener() {
			
			public void onClick(Button button, EventObject e) {
				AuthenticationInterfaceAsync auth = GWT.create(AuthenticationInterface.class);
				auth.authenticate(userdata.getUsername(), userdata.getPassword(), new AsyncCallback<Boolean>() {

					public void onFailure(Throwable caught) {
						com.google.gwt.user.client.Window.alert(caught.getMessage());
					}

					public void onSuccess(Boolean result) {
						if(result.booleanValue()) {
							for(LoginWindowListener l: listeners) {
								l.loginSuccess();
							}
						} else {
							com.google.gwt.user.client.Window.alert("Access Denied: You must have server admin rights");
						}
					}
					
				});
			}		
		});
		
		changePassword.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				
				AuthenticationInterfaceAsync auth = GWT.create(AuthenticationInterface.class);
				auth.authenticateBasic(userdata.getUsername(), userdata.getPassword(), new AsyncCallback<Boolean>() {

					public void onFailure(Throwable caught) {
						com.google.gwt.user.client.Window.alert(caught.getMessage());
					}

					public void onSuccess(Boolean result) {
						if(result.booleanValue()) {
							displayChangePassword();
						} else {
							com.google.gwt.user.client.Window.alert("Invalid Username/Password");
						}
					}
					
				});
				
				
				super.onClick(button, e);
			}
		});
		
		addButton(changePassword);
	}
	
	public void addListener(LoginWindowListener listener) {
		listeners.add(listener);
	}
	
	public interface LoginWindowListener {
		void loginSuccess();
	}

	public String getUsername() {
		return userdata.getUsername();
	}
	
	public String getPassword() {
		return userdata.getPassword();
	}

	private void displayChangePassword() {
		final Window w = new Window("Change Password", true, false);

		
		final Button ok = new Button("OK");
		ok.setDisabled(true);
		
		final TextField password = new TextField("New Password", "password", 210){{
			setAllowBlank(false);
			setInvalidText("Invalid Password");
			setMaxLength(8);
			setPassword(true);
			setValidator(new Validator() {
				public boolean validate(String value) throws ValidationException {
					return value.trim().length() > 2 && value.trim().length() < 9;
				}
			});			
			
		}};
		TextField password_again = new TextField("New Password Again", "password_again", 210){{
			setAllowBlank(false);
			setInvalidText("Passwords don't match.");
			setMaxLength(8);
			setPassword(true);
			setValidator(new Validator() {
				public boolean validate(String value) throws ValidationException {
					return value.equals(password.getText()) && password.isValid();
				}
			});
			addListener(new FieldListenerAdapter() {
				@Override
				public void onValid(Field field) {
					ok.setDisabled(false);
					super.onValid(field);
				}
				@Override
				public void onInvalid(Field field, String msg) {
					ok.setDisabled(true);
					super.onInvalid(field, msg);
				}
			});
			
		}};				
		
		Button cancel = new Button("Cancel");
		cancel.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				w.setVisible(false);
				super.onClick(button, e);
			}
		});
		
		ok.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				AuthenticationInterfaceAsync auth = GWT.create(AuthenticationInterface.class);
				auth.changePasswordBasic(userdata.getUsername(), userdata.getPassword(), 
						password.getValueAsString(), new AsyncCallback<String>() {

							public void onFailure(Throwable caught) {}

							public void onSuccess(String result) {
								com.google.gwt.user.client.Window.alert(result);
								userdata.clearPassword();
								w.setVisible(false);
							}
					
				});
				super.onClick(button, e);
			}
		});
		

		
		FieldSet set = new FieldSet();
			set.setPaddings(15);
			set.setBorder(false);

			set.add(password);
			set.add(password_again);

		w.setLayout(new FormLayout());	
		w.add(set);
		w.addButton(cancel);
		w.addButton(ok);
		w.setWidth(400);
		
		w.doLayout();
		w.setVisible(true);
	}

	
}
