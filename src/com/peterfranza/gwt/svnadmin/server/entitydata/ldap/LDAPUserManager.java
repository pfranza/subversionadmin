package com.peterfranza.gwt.svnadmin.server.entitydata.ldap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.peterfranza.gwt.svnadmin.server.entitydata.User;
import com.peterfranza.gwt.svnadmin.server.entitydata.UserManager;

public class LDAPUserManager implements UserManager {

	private String url;
	private String ldapUsername;
	private String ldapPassword;
	private String adminGroup;
	
	@Inject
	public LDAPUserManager(
			@Named("ldapUrl") String url, 
			@Named("ldapUsername") String ldapUsername, 
			@Named("ldapPassword") String ldapPassword,
			@Named("ldapAdminGroup") String adminGroup) {
		this.url = url;
		this.ldapUsername = ldapUsername;
		this.ldapPassword = ldapPassword;
		this.adminGroup = adminGroup;
	}
	
	@Override
	public boolean authenticate(String username, String password) {
		try {
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			env.put(Context.PROVIDER_URL, url);
			env.put(Context.REFERRAL, "follow");
			env.put(Context.SECURITY_AUTHENTICATION, "simple");
			env.put(Context.SECURITY_PRINCIPAL, username);
			env.put(Context.SECURITY_CREDENTIALS, password);
			DirContext context = new InitialDirContext(env);
			return context != null;
		} catch(Exception e){}
		return false;
	}

	@Override
	public void createUser(String username) {
		throw new RuntimeException("Unsupported make changes on LDAP server");
	}

	@Override
	public User getUserForName(String username) {
		for(LDAPUser u: getUsers()) {
			if(u.getEmailAddress().equalsIgnoreCase(username) ||
					u.getPrincipal().equalsIgnoreCase(username) ||
					u.getName().equalsIgnoreCase(username)) {
				return u;
			}
		}
		System.out.println("name not found: " + username);
		return null;
	}

	@Override
	public Collection<LDAPUser> getUsers() {
        try {           
            Hashtable<String, String> env = new Hashtable<String, String>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, url);
            env.put(Context.REFERRAL, "follow");         
    		env.put(Context.SECURITY_AUTHENTICATION, "simple");
    		env.put(Context.SECURITY_PRINCIPAL, ldapUsername);
    		env.put(Context.SECURITY_CREDENTIALS, ldapPassword);           
            DirContext context = new InitialDirContext(env);
            
            Attributes attributes = context.getAttributes(context.getNameInNamespace() );
            Attribute dna = attributes.get("defaultNamingContext");
            System.out.println(dna.get());
            
            String query = "(&(objectClass=user)(mail=*))";
    		String[] attributes1 = {"cn","userPrincipalName","name","mail","memberOf","sAMAccountName"};
    		
            
            SearchControls ctrl = new SearchControls();
            ctrl.setSearchScope(SearchControls.SUBTREE_SCOPE);
            ctrl.setReturningAttributes(attributes1);
            
            NamingEnumeration<?> enumeration = context.search(dna.get().toString(), query, ctrl);
            ArrayList<LDAPUser> users = new ArrayList<LDAPUser>();
            while (enumeration.hasMoreElements()) {
    			SearchResult result = (SearchResult) enumeration.next();
    			Attributes attribs = result.getAttributes();
    			if (null != attribs) {
    				users.add(new LDAPUser(attribs, adminGroup));
    			}
            }
            return users;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
	}

	@Override
	public void removeUser(String username) {
		throw new RuntimeException("Unsupported make changes on LDAP server");
	}

	@Override
	public void setAdministrator(String username, boolean isAdmin) {
		throw new RuntimeException("Unsupported make changes on LDAP server");
	}

	@Override
	public void setEmailAddress(String username, String email) {
		throw new RuntimeException("Unsupported make changes on LDAP server");
	}

	@Override
	public void setPassword(String username, String password) {
		throw new RuntimeException("Unsupported make changes on LDAP server");
	}

	@Override
	public boolean isMutable() {
		return false;
	}
	
	public static void main(String[] args) {
		LDAPUserManager um = new LDAPUserManager("ldap://192.168.100.200", 
				"username", 
				"password", 
				"SvnAdmin");
		for(User u: um.getUsers()) {
			System.out.println(u.getName() + "  " + u.getEmailAddress() + " " + u.isAdministrator());
		}
	}
	
}
