package com.peterfranza.gwt.svnadmin.client.actions.projectmanagement;

import java.util.ArrayList;

import net.customware.gwt.dispatch.shared.Action;
import net.customware.gwt.dispatch.shared.Result;

public class ProjectListUserSubscriptions implements Action<ProjectListUserSubscriptions.SubscriptionList>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7483862313118646693L;
	
	private String project;
	
	public ProjectListUserSubscriptions(String project) {
		super();
		this.project = project;
	}

	protected ProjectListUserSubscriptions() {
	}

	public String getProject() {
		return project;
	}

	public static class SubscriptionList implements Result {

		/**
		 * 
		 */
		private static final long serialVersionUID = 2604098503117975594L;
		
		private ArrayList<String> names;
		
		public SubscriptionList(ArrayList<String> names) {
			this.names = names;
		}
		
		protected SubscriptionList() {}
		
		public ArrayList<String> getSubscriptionNames() {
			return names;
		}
	}
	
}
