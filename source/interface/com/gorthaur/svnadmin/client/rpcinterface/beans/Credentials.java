package com.gorthaur.svnadmin.client.rpcinterface.beans;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Credentials  implements IsSerializable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7883329844642777349L;
	
	private String username;
	private String password;
	
	public Credentials() {}
	
	public Credentials(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
	public final String getUsername() {
		return username;
	}
	public final void setUsername(String username) {
		this.username = username;
	}
	public final String getPassword() {
		return password;
	}
	public final void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
