package com.peterfranza.gwt.svnadmin.client.actions.usermanagement;

import java.util.ArrayList;

import net.customware.gwt.dispatch.shared.Action;
import net.customware.gwt.dispatch.shared.Result;

public class ListUsers implements Action<ListUsers.UserList>{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3783595149063100731L;

	public static class UserList implements Result {

		/**
		 * 
		 */
		private static final long serialVersionUID = -1360241198108123727L;
		
		private ArrayList<String> usernames;
		
		public UserList(ArrayList<String> usernames) {
			this.usernames = usernames;
		}
		
		protected UserList() {}
		
		public ArrayList<String> getUsernames() {
			return usernames;
		}
	}
	
}
