package com.peterfranza.gwt.svnadmin.client.actions.projectmanagement;

import java.util.ArrayList;

import net.customware.gwt.dispatch.shared.Action;
import net.customware.gwt.dispatch.shared.Result;

public class ListProjectMembers implements Action<ListProjectMembers.MembersList> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8393647258499729972L;
	
	private String project;
	
	public ListProjectMembers(String project) {
		super();
		this.project = project;
	}

	protected ListProjectMembers() {
	}

	public String getProject() {
		return project;
	}

	public static class MembersList implements Result {

		/**
		 * 
		 */
		private static final long serialVersionUID = 8685394047867362720L;
		
		private ArrayList<String> readers;
		private ArrayList<String> writers;
		
		public MembersList(ArrayList<String> readers,
				ArrayList<String> writers) {
			this.readers = readers;
			this.writers = writers;
		}
		
		protected MembersList() {}
		
		public ArrayList<String> getReaders() {
			return readers;
		}
		
		public ArrayList<String> getWriters() {
			return writers;
		}
	}
	
}
