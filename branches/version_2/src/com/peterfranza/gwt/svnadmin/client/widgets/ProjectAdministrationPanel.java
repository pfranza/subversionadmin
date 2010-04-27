package com.peterfranza.gwt.svnadmin.client.widgets;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.peterfranza.gwt.svnadmin.client.SubversionAdministrator;
import com.peterfranza.gwt.svnadmin.client.actions.AddProjectRequest;
import com.peterfranza.gwt.svnadmin.client.actions.MessageResultHandler;
import com.peterfranza.gwt.svnadmin.client.actions.ScanProjectsRequest;
import com.peterfranza.gwt.svnadmin.client.res.Resources;

public class ProjectAdministrationPanel extends ContentPanel {

	private FlexTable table = new FlexTable();
	private Button addProject = new Button("Add Project");
	private Button scanProject = new Button("Scan Projects");
	
	public ProjectAdministrationPanel() {
		super(new FitLayout());
		setAutoHeight(true);
		setHeaderVisible(false);
				
		table.getColumnFormatter().setWidth(0, "100%");
		table.getColumnFormatter().setWidth(1, "20px");
				
		for(int i = 0; i < 15; i++) {
			addRow("/path/" + i);
		}
		
		add(table);		
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
	
	private void addRow(final String path) {

		Image cfg = new Image(Resources.INSTANCE.cog());
		cfg.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				System.out.println("configure " + path);
			}
		});
		
		int row = table.getRowCount();		
		table.setText(row, 0, path);
		table.setWidget(row, 1, cfg);
	}
	

	
}
