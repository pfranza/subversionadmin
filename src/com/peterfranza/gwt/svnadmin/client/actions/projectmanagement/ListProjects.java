package com.peterfranza.gwt.svnadmin.client.actions.projectmanagement;

import java.util.ArrayList;

import net.customware.gwt.dispatch.shared.Action;
import net.customware.gwt.dispatch.shared.Result;

public class ListProjects implements Action<ListProjects.ProjectsList> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3102691808839075537L;

	public ListProjects() {
	}

	public static class ProjectsList implements Result {

		/**
		 * 
		 */
		private static final long serialVersionUID = -629158764976692888L;
		
		private ArrayList<String> names;
		
		public ProjectsList(ArrayList<String> names) {
			this.names = names;
		}
		
		protected ProjectsList() {}
		
		public ArrayList<String> getProjectNames() {
			return names;
		}
	}
}
