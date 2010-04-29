package com.peterfranza.gwt.svnadmin.client.widgets;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.peterfranza.gwt.svnadmin.client.SubversionAdministrator;
import com.peterfranza.gwt.svnadmin.client.actions.MessageResult;
import com.peterfranza.gwt.svnadmin.client.actions.MessageResultHandler;
import com.peterfranza.gwt.svnadmin.client.actions.usermanagement.ChangeUserPassword;

public class ChangePasswordWindow extends Window {

	private Button ok;
	private TextField<String> password;
	private TextField<String> password_again;

	public ChangePasswordWindow(final String username) {
		
		setHeading("Change Password");
		
		ok = new Button("OK");
		ok.setEnabled(false);
		
		password = new TextField<String>(){{
			setFieldLabel("New Password");
			setAllowBlank(false);
			setMaxLength(8);
			setPassword(true);
			setValidator(new Validator() {
				@Override
				public String validate(Field<?> field, String value) {
					return (value.trim().length() > 2 && value.trim().length() < 9) ? null : "Invalid Password";
				}
			});	
		}};
		
		password_again = new TextField<String>(){{
			setFieldLabel("New Password Again");
			setAllowBlank(false);
			setMaxLength(8);
			setPassword(true);
			setValidator(new Validator() {
				@Override
				public String validate(Field<?> field, String value) {
					return (value.equals(password.getValue()) && password.isValid()) ? null : "Password Doesn't Match";
				}
			});	
		}};
			
		KeyListener kl = new KeyListener() {
		@Override
			public void componentKeyPress(ComponentEvent event) {
				DeferredCommand.addCommand(new Command() {
					
					@Override
					public void execute() {
						setState();	
					}
				});
			}
		};
		
		password.addKeyListener(kl);
		password_again.addKeyListener(kl);
		
		Button cancel = new Button("Cancel");
		cancel.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				hide();
				new LoginWindow().show();
			}
		});
		
		ok.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				SubversionAdministrator.dispatcher.execute(new ChangeUserPassword(username, 
						password.getValue()),
						new MessageResultHandler(){
					@Override
					public void onSuccess(MessageResult result) {
						super.onSuccess(result);
						hide();
						new LoginWindow().show();
					}
				});
			}
		});
		

		
		FormPanel set = new FormPanel();
		set.setHeaderVisible(false);
			set.add(password);
			set.add(password_again);

		setLayout(new FormLayout());	
		add(set);
		addButton(cancel);
		addButton(ok);
		setWidth(400);
		
		doLayout();
	}

	private void setState() {
		ok.setEnabled(password.isValid() && password_again.isValid());
	}
	
}
