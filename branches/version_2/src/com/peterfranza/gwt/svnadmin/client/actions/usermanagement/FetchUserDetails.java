package com.peterfranza.gwt.svnadmin.client.actions.usermanagement;

import net.customware.gwt.dispatch.shared.Action;
import net.customware.gwt.dispatch.shared.Result;

public class FetchUserDetails implements Action<FetchUserDetails.UserDetails>{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1871255749644723644L;
	
	private String username;
	
	public FetchUserDetails(String username) {
		super();
		this.username = username;
	}

	public String getUsername() {
		return username;
	}


	protected FetchUserDetails() {
		super();
	}

	public static class UserDetails implements Result {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -2971019921590710425L;
		
		private String username;
		private String emailAddress;
		private boolean isAdministrator;
		
		public UserDetails(String username, String emailAddress,
				boolean isAdministrator) {
			super();
			this.username = username;
			this.emailAddress = emailAddress;
			this.isAdministrator = isAdministrator;
		}

		protected UserDetails() {
			super();
		}

		/**
		 * @return the username
		 */
		public final String getUsername() {
			return username;
		}

		/**
		 * @return the emailAddress
		 */
		public final String getEmailAddress() {
			return emailAddress;
		}

		/**
		 * @return the isAdministrator
		 */
		public final boolean isAdministrator() {
			return isAdministrator;
		}
				
	}
	
}
