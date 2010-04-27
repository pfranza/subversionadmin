package com.peterfranza.gwt.svnadmin.client.widgets;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.peterfranza.gwt.svnadmin.client.SubversionAdministrator;
import com.peterfranza.gwt.svnadmin.client.actions.AddProjectRequest;
import com.peterfranza.gwt.svnadmin.client.actions.MessageResultHandler;
import com.peterfranza.gwt.svnadmin.client.actions.ScanProjectsRequest;

public class ProjectAdministrationPanel extends ContentPanel {

	private Button addProject = new Button("Add Project");
	private Button scanProject = new Button("Scan Projects");
	
	public ProjectAdministrationPanel() {
		super(new FitLayout());
		setHeaderVisible(false);
		addText("add content");
		
		addButton(addProject);
		addButton(scanProject);
		
		addProject.addSelectionListener(new SelectionListener<ButtonEvent>() {			
			@Override
			public void componentSelected(ButtonEvent ce) {
				final MessageBox box = MessageBox.prompt("Project Path", "Please enter project path:");  
				box.addCallback(new Listener<MessageBoxEvent>() {  
					public void handleEvent(MessageBoxEvent be) {  
						SubversionAdministrator.dispatcher.execute(
								new AddProjectRequest(be.getValue()), new MessageResultHandler());
					}  
				});  
			}
		});
		
		scanProject.addSelectionListener(new SelectionListener<ButtonEvent>() {		
			@Override
			public void componentSelected(ButtonEvent ce) {
				SubversionAdministrator.dispatcher.execute(
						new ScanProjectsRequest(), 
						new MessageResultHandler());
			}
		});
	}
	
}
