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
import com.gwtext.client.widgets.layout.FormLayout;

public class LoginWindow extends Window {

	private List<LoginWindowListener> listeners = new ArrayList<LoginWindowListener>();
	
	private LoginPanel userdata = new LoginPanel(); 
	private Button loginButton = new Button("Login");
	
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
							com.google.gwt.user.client.Window.alert("Access Denied");
						}
					}
					
				});
			}		
		});
		
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

	
}
