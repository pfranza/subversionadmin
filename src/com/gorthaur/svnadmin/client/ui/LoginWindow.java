package com.gorthaur.svnadmin.client.ui;

import java.util.ArrayList;
import java.util.List;

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
	
	public LoginWindow() {
		LoginPanel userdata = new LoginPanel(); 
		setTitle("Login");  
		setClosable(false);
		setResizable(false);
		setWidth(220);  
		setHeight(130);  
		setPlain(true); 
		setCloseAction(Window.CLOSE);	
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
				for(LoginWindowListener l: listeners) {
					l.loginSuccess();
				}
			}		
		});
		
	}
	
	public void addListener(LoginWindowListener listener) {
		listeners.add(listener);
	}
	
	public interface LoginWindowListener {
		void loginSuccess();
	}
	
}
