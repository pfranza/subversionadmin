package com.peterfranza.gwt.svnadmin.server.entitydata.ldap;

import java.util.Enumeration;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import com.peterfranza.gwt.svnadmin.server.entitydata.User;

public class LDAPUser implements User {

	private String principal;
	private String email;
	private String name;
	private boolean isAdministrator = false;
	
	public LDAPUser(Attributes attribs, String administratorsGroup) throws NamingException {
		
		principal = attribs.get("userPrincipalName").get().toString();
		email = attribs.get("mail").get().toString();
		name = attribs.get("sAMAccountName").get().toString();
		
		for (Enumeration<?> vals = attribs.get("memberOf").getAll(); 
				vals.hasMoreElements(); 
				isAdministrator |= vals.nextElement().toString().toLowerCase().contains(administratorsGroup.toLowerCase()));
	}
	
	@Override
	public String getEmailAddress() {
		return email;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public boolean isAdministrator() {
		return isAdministrator;
	}

	@Override
	public String getName() {
		return name;
	}
	
	public String getPrincipal() {
		return principal;
	}
	
	@Override
	public void setAdministrator(boolean isAdministrator) {
		throw new RuntimeException("Unsupported make changes on LDAP server");
	}

	@Override
	public void setEmailAddress(String emailAddress) {
		throw new RuntimeException("Unsupported make changes on LDAP server");
	}

	@Override
	public void setPassword(String password) {
		throw new RuntimeException("Unsupported make changes on LDAP server");
	}


}
