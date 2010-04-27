package com.peterfranza.gwt.svnadmin.client.actions;

import net.customware.gwt.dispatch.shared.Action;
import net.customware.gwt.dispatch.shared.Result;

public class AuthenticationRequest implements Action<AuthenticationRequest.AuthenticationResult>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5341643138932247202L;

	private String username;
	private String password;
	
	protected AuthenticationRequest() {}
	
	public AuthenticationRequest(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public static class AuthenticationResult implements Result{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1377470689300176275L;

		private boolean authenticated;
		private boolean isAdministrator;
		
		public AuthenticationResult(){}
		
		public AuthenticationResult(boolean authenticated, boolean isAdministrator) {
			super();
			this.authenticated = authenticated;
			this.isAdministrator = isAdministrator;
		}

		/**
		 * @return the authenticated
		 */
		public final boolean isAuthenticated() {
			return authenticated;
		}

		/**
		 * @param authenticated the authenticated to set
		 */
		public final void setAuthenticated(boolean authenticated) {
			this.authenticated = authenticated;
		}

		/**
		 * @return the isAdministrator
		 */
		public final boolean isAdministrator() {
			return isAdministrator;
		}

		/**
		 * @param isAdministrator the isAdministrator to set
		 */
		public final void setAdministrator(boolean isAdministrator) {
			this.isAdministrator = isAdministrator;
		}
		
	}
	
	
}
