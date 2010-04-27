package com.peterfranza.gwt.svnadmin.client.actions;

import net.customware.gwt.dispatch.shared.Action;
import net.customware.gwt.dispatch.shared.Result;


public class CapabilitiesRequest implements Action<CapabilitiesRequest.CapabilitiesResult> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8749670641063769377L;

	
	
	
	
	public static class CapabilitiesResult implements Result {

		/**
		 * 
		 */
		private static final long serialVersionUID = -6439093997787310136L;

		private boolean localAccounts;
		
		public void setLocalAccounts(boolean b) {
			localAccounts = b;
		}
		
		public boolean getLocalAccounts() {
			return localAccounts;
		}

	}
}
