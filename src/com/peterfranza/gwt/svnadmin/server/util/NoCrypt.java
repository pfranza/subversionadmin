package com.peterfranza.gwt.svnadmin.server.util;

public class NoCrypt implements Crypt {

	@Override
	public String crypt(String original) {
		return original;
	}

	@Override
	public boolean matches(String encryptedPassword, String enteredPassword) {
		return encryptedPassword.equals(enteredPassword);
	}

}
