package com.peterfranza.gwt.svnadmin.client.widgets;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.peterfranza.gwt.svnadmin.client.SubversionAdministrator;
import com.peterfranza.gwt.svnadmin.client.actions.MessageResultHandler;
import com.peterfranza.gwt.svnadmin.client.actions.usermanagement.ChangeUserEmail;
import com.peterfranza.gwt.svnadmin.client.actions.usermanagement.ChangeUserPassword;
import com.peterfranza.gwt.svnadmin.client.actions.usermanagement.ChangeUserRights;
import com.peterfranza.gwt.svnadmin.client.actions.usermanagement.FetchUserDetails.UserDetails;

public class ModifyUserWindow extends Window{

	private TextField<String> email = new TextField<String>(){{
		setAutoWidth(true);
		setFieldLabel("Email Address");
	}};
	
	private CheckBox isAdmin = new CheckBox(){{
		setFieldLabel("Administrator");
		setAutoWidth(true);
	}};
	
	private TextField<String> password = new TextField<String>(){{
		setAutoWidth(true);
		setFieldLabel("Password");
		setPassword(true);
	}};
	
	private TextField<String> password2 = new TextField<String>(){{
		setAutoWidth(true);
		setFieldLabel("Password Again");
		setPassword(true);
	}};
	
	private Button save = new Button("Save");
	
	public ModifyUserWindow(final UserDetails result) {
		setHeading("Modify User " + result.getUsername());
		setLayout(new FitLayout());
		setPlain(true);  
		setModal(true);  
		setBlinkModal(true); 
		setClosable(false);
		setSize(335, 250);  
		
		email.setValue(result.getEmailAddress());
		isAdmin.setValue(result.isAdministrator());
		
		FormPanel form = new FormPanel();
		form.setHeaderVisible(false);
		form.setAutoWidth(true);
		form.add(email);
		form.add(isAdmin);
		
		form.add(password);
		form.add(password2);
		
		add(form);
		addButton(save);
		
		save.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
								
				if(password.getValue() != null && password2.getValue() != null 
						&& password.getValue().trim().length() > 1) {
					if(password.getValue().equals(password2.getValue())) {
						SubversionAdministrator.dispatcher.execute(
								new ChangeUserPassword(result.getUsername(), password.getValue()), new MessageResultHandler());						
					} else {
						MessageBox.alert("Alert", "Passwords do not match.", new Listener<MessageBoxEvent>() {						
							@Override
							public void handleEvent(MessageBoxEvent be) {}
						});  
						return;
					}
				}
				
				if(!email.getValue().equals(result.getEmailAddress())) {
					SubversionAdministrator.dispatcher.execute(
							new ChangeUserEmail(result.getUsername(), email.getValue()), new MessageResultHandler());
				}
				
				if(!isAdmin.getValue().equals(result.isAdministrator())) {
					SubversionAdministrator.dispatcher.execute(
							new ChangeUserRights(result.getUsername(), isAdmin.getValue()), new MessageResultHandler());
				}
				
				hide();
			}
		});
	}

}
