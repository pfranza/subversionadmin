package com.peterfranza.gwt.svnadmin.client.actions;

import net.customware.gwt.dispatch.shared.Result;

public class MessageResult implements Result{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4034569546492040885L;
	
	private String message;
	private String title;
	
	protected MessageResult() {}

	public MessageResult(String title, String message) {
		super();
		this.title = title;
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

	public String getTitle() {
		return title;
	}
	
}
