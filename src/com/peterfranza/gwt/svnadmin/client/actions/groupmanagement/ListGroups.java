package com.peterfranza.gwt.svnadmin.client.actions.groupmanagement;

import java.util.ArrayList;

import net.customware.gwt.dispatch.shared.Action;
import net.customware.gwt.dispatch.shared.Result;

public class ListGroups implements Action<ListGroups.GroupsList>{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7545616619625977649L;

	public static class GroupsList implements Result {

		/**
		 * 
		 */
		private static final long serialVersionUID = -1360241198108123727L;
		
		private ArrayList<String> names;
		
		public GroupsList(ArrayList<String> names) {
			this.names = names;
		}
		
		protected GroupsList() {}
		
		public ArrayList<String> getGroups() {
			return names;
		}
	}
}
