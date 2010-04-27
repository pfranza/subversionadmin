package com.peterfranza.gwt.svnadmin.client.widgets;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.MarginData;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.peterfranza.gwt.svnadmin.client.SubversionAdministrator;
import com.peterfranza.gwt.svnadmin.client.actions.AuthenticationRequest;
import com.peterfranza.gwt.svnadmin.client.actions.AuthenticationRequest.AuthenticationResult;

public class LoginWindow extends Window{

	private LoginPanel panel = new LoginPanel();
	
	{
		setSize(335, 150);  
		setPlain(true);  
		setModal(true);  
		setBlinkModal(true); 
		setClosable(false);
		setHeading("Subversion Administration Login");  
		setLayout(new FitLayout());
		add(panel);
		
		panel.addLoginListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(final ButtonEvent ce) {
				SubversionAdministrator.dispatcher.execute(
						new AuthenticationRequest(panel.getUsername(), panel.getPassword()),
						new AsyncCallback<AuthenticationResult>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(AuthenticationResult result) {
								if(result.isAuthenticated()) {
									if(result.isAdministrator()) {
										Viewport viewport = new Viewport();
										viewport.setLayout(new FitLayout());
										viewport.add(new MainPanel(), new MarginData(5));
										RootPanel.get().add(viewport);
										hide(ce.getButton());
									} else {
										//TODO show change password dialog
									}
								} else {
									com.google.gwt.user.client.Window.alert("Authentication Failed");
								}
							}
							
						});
			}
		});
		
	}
	
}
