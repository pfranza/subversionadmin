package com.peterfranza.gwt.svnadmin.client.widgets;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;
import com.peterfranza.gwt.svnadmin.client.SubversionAdministrator;
import com.peterfranza.gwt.svnadmin.client.actions.MessageResult;
import com.peterfranza.gwt.svnadmin.client.actions.MessageResultHandler;
import com.peterfranza.gwt.svnadmin.client.actions.projectmanagement.AddProject;
import com.peterfranza.gwt.svnadmin.client.actions.projectmanagement.ListProjects;
import com.peterfranza.gwt.svnadmin.client.actions.projectmanagement.ScanProjectsRequest;
import com.peterfranza.gwt.svnadmin.client.actions.projectmanagement.ListProjects.ProjectsList;

public class ProjectAdministrationPanel extends ContentPanel {

	private ListBox projects = new ListBox();
	private Button addProject = new Button("Add Project");
	private Button scanProject = new Button("Scan Projects");
	
	private Button manageSubscriptions = new Button("Manage Subscriptions");
	private Button manageAccess = new Button("Manage Access");
	
	
	public ProjectAdministrationPanel() {
		super(new FitLayout());
		setAutoHeight(true);
		setHeaderVisible(false);
			
		projects.setVisibleItemCount(10);
		add(projects);
		
		addButton(manageAccess);
		addButton(manageSubscriptions);		
		addButton(addProject);
		addButton(scanProject);
		
		projects.addChangeHandler(new ChangeHandler() {			
			@Override
			public void onChange(ChangeEvent event) {
				manageAccess.setEnabled(projects.getSelectedIndex() != -1);
				manageSubscriptions.setEnabled(projects.getSelectedIndex() != -1);
			}
		});
		
		refresh();
		
		manageAccess.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				new ManageAccessWindow(projects.getItemText(projects.getSelectedIndex())).show();
			}
		});
		
		manageSubscriptions.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				new ManageSubscriptionsWindow(projects.getItemText(projects.getSelectedIndex())).show();
			}
		});
		
		addProject.addSelectionListener(new SelectionListener<ButtonEvent>() {			
			@Override
			public void componentSelected(ButtonEvent ce) {
				final MessageBox box = MessageBox.prompt("Project Path", "Please enter project path:");  
				box.addCallback(new Listener<MessageBoxEvent>() {  
					public void handleEvent(MessageBoxEvent be) {  
						SubversionAdministrator.dispatcher.execute(
								new AddProject(be.getValue()), 
								new MessageResultHandler(){
									@Override
									public void onSuccess(MessageResult result) {
										super.onSuccess(result);
										refresh();
									}
								});
					}  
				});  
			}
		});
		
		scanProject.addSelectionListener(new SelectionListener<ButtonEvent>() {		
			@Override
			public void componentSelected(ButtonEvent ce) {
				SubversionAdministrator.dispatcher.execute(
						new ScanProjectsRequest(), 
						new MessageResultHandler(){
							@Override
							public void onSuccess(MessageResult result) {
								super.onSuccess(result);
								refresh();
							}
						});
			}
		});
	}
	
	private void refresh() {
		projects.clear();
		manageAccess.setEnabled(false);
		manageSubscriptions.setEnabled(false);
		SubversionAdministrator.dispatcher.execute(
				new ListProjects(),
				new AsyncCallback<ProjectsList>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(ProjectsList result) {
						for(String s: result.getProjectNames()) {
							projects.addItem(s);
						}
					}
				});
		
	}
	
}
