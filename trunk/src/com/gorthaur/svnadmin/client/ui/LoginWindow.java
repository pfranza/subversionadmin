package com.gorthaur.svnadmin.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.gorthaur.svnadmin.client.ui.listeners.ClickListener;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;

public class LoginWindow extends Window {

	private List<LoginWindowListener> listeners = new ArrayList<LoginWindowListener>();
	
	private LoginPanel userdata = new LoginPanel(); 
	
	public LoginWindow() {
		
		setTitle("Login");  
		setClosable(false);
		setResizable(false);
		setWidth(220);  
		setHeight(130);  
			
		setLayout(new BorderLayout());  
		add(userdata, new BorderLayoutData(RegionPosition.CENTER));
		
		Panel buttons = new Panel();
		buttons.setBorder(false);
		buttons.setPaddings(0, 145, 5, 15);
		Button loginButton = new Button("Login");
		buttons.add(loginButton);
		
		add(buttons, new BorderLayoutData(RegionPosition.SOUTH));

		loginButton.addListener(new ClickListener() {
			
			public void onClick(Button button, EventObject e) {
				RequestBuilder rb = new RequestBuilder(RequestBuilder.GET, 
						URL.encode("/rest/auth?username="+userdata.getUsername()+"&passwd="+userdata.getPassword()));
				try {
					rb.sendRequest("", new RequestCallback() {

						public void onError(Request request, Throwable exception) {
							com.google.gwt.user.client.Window.alert("Error Processing Login");
						}

						public void onResponseReceived(Request request,
								Response response) {
							if(response.getText().trim().equalsIgnoreCase("ok")) {
								for(LoginWindowListener l: listeners) {
									l.loginSuccess();
								}
							} else {
								com.google.gwt.user.client.Window.alert("Access Denied");
							}
						}
						
					});
				} catch (RequestException e1) {
					com.google.gwt.user.client.Window.alert("Error: " + e1.getMessage());
				}
//				for(LoginWindowListener l: listeners) {
//					l.loginSuccess();
//				}
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
