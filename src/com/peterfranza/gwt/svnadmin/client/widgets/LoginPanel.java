package com.peterfranza.gwt.svnadmin.client.widgets;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;

public class LoginPanel extends FormPanel {

	private TextField<String> username = new TextField<String>(){{
		setAutoWidth(true);
		setFieldLabel("Username");
	}};
	
	private TextField<String> password = new TextField<String>() {{
		setPassword(true);
		setAutoWidth(true);
		setFieldLabel("Password");
	}};
	
	private Button login = new Button("Login");
//	private Button changePassword = new Button("Change Password");
	
	public LoginPanel() {
		setBorders(false);
		setAutoWidth(true);
		setHeaderVisible(false);
		
		
		add(username);	
		add(password);
		
		addButton(login);
//		addButton(changePassword);
	}
	
	
	public String getUsername() {
		return username.getValue().trim();
	}
	
	public String getPassword() {
		return password.getValue().trim();
	}
	
	public void clearPassword() {
		password.setValue("");
	}
	
	public void addLoginListener(SelectionListener<ButtonEvent> listener) {
		login.addSelectionListener(listener);
	}
	
}
