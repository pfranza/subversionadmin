package com.peterfranza.gwt.svnadmin.client.widgets;

import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

public class LoginWindow extends Window{

	{
		setSize(350, 150);  
		setPlain(true);  
		setModal(true);  
		setBlinkModal(true); 
		setClosable(false);
		setHeading("Subversion Administration Login");  
		setLayout(new FitLayout());
		add(new LoginPanel());
	}
	
}
