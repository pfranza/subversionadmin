package com.gorthaur.svnadmin.client.ui;

import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.TextField;

public class LoginPanel extends FieldSet {

	private TextField username = new TextField("<B>Username</B>");
	private TextField password = new TextField("<B>Password</B>");
	
	public LoginPanel() {
		setBorder(false);
		add(username);	
		add(password);
		setPaddings(10, 0, 0, 0);
		username.setAutoWidth(true);
		password.setAutoWidth(true);
		setAutoWidth(true);
	}
	
	
	public String getUsername() {
		return username.getText().trim();
	}
	
	public String getPassword() {
		return password.getText().trim();
	}
	
}
