package com.gorthaur.svnadmin.client.ui;

import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.layout.TableLayout;

public class LoginPanel extends Panel {

	private Label userLabel = new Label("Username: ");
	private TextField username = new TextField();
	
	private Label passLabel = new Label("Password: ");
	private TextField password = new TextField();
	
	public LoginPanel() {
		setLayout(new TableLayout(2));
		setBorder(false);
		setPaddings(10, 5, 5, 5);		
		
		password.setPassword(true);
		password.setAllowBlank(false);
		username.setAllowBlank(false);
		
		add(userLabel);
		add(username);	
		add(passLabel);
		add(password);
		
		clear();
	}
	
	public void clear() {
		username.setRawValue("");
		password.setRawValue("");
	}
	
	public String getUsername() {
		return username.getText().trim();
	}
	
	public String getPassword() {
		return password.getText().trim();
	}
	
}
