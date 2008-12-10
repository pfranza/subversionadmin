package com.gorthaur.svnadmin.client.ui;

import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.TextField;

public class LoginPanel extends FieldSet {

	private TextField username = new TextField("<B>Username</B>"){{
		setAutoWidth(true);
	}};
	
	private TextField password = new TextField("<B>Password</B>") {{
		setPassword(true);
		setAutoWidth(true);
	}};
	
	public LoginPanel() {
		setBorder(false);
		setPaddings(10, 0, 0, 0);
		setAutoWidth(true);
		
		add(username);	
		add(password);
		
	}
	
	
	public String getUsername() {
		return username.getText().trim();
	}
	
	public String getPassword() {
		return password.getText().trim();
	}
	
}
