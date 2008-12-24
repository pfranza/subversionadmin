package com.peterfranza.svnadmin.server.rpc;

import java.util.ArrayList;
import java.util.List;

import org.mortbay.gwt.AsyncRemoteServiceServlet;

import com.gorthaur.svnadmin.client.rpcinterface.UserOperationsInterface;
import com.gorthaur.svnadmin.client.rpcinterface.beans.Credentials;
import com.gorthaur.svnadmin.client.rpcinterface.beans.UserInfo;
import com.peterfranza.svnadmin.server.ApplicationProperties;
import com.peterfranza.svnadmin.server.RepositoryListing;
import com.peterfranza.svnadmin.server.acldb.ACLOperationsDelegate;
import com.peterfranza.svnadmin.server.acldb.ACLDB.User;

public class UserOperations extends AsyncRemoteServiceServlet implements UserOperationsInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7101937577981436861L;

	public String createNewUser(Credentials requestor, String username,
			String password, String email) {
		if(ACLOperationsDelegate.getInstance().getUserOperations().isAdmin(requestor.getUsername())) {
			ACLOperationsDelegate.getInstance().getUserOperations()
				.addNewUser(username, password, email);
			return "ok";
		} else {
			return "Insufficiant Access";
		}
		
	}

	public boolean isUser(Credentials requestor, String username) {
		return ACLOperationsDelegate.getInstance().getUserOperations().getUser(username) != null;
	}

	@Override
	public List<UserInfo> getAllUsers(Credentials requestor) {
		if(ACLOperationsDelegate.getInstance().getUserOperations().isAdmin(requestor.getUsername())) {
			List<UserInfo> l = new ArrayList<UserInfo>();
			for(String uname: ACLOperationsDelegate.getInstance().getUserOperations().getUsernames()){
				User user = ACLOperationsDelegate.getInstance().getUserOperations().getUser(uname);
				l.add(new UserInfo(user.getUsername(), user.getEmail(), user.isAdmin()));
			}
			return l;
		} else {
			throw new RuntimeException("Insufficient Access");
		}
	}

	@Override
	public void deleteUser(Credentials requestor, String username) {
		if(ACLOperationsDelegate.getInstance().getUserOperations().isAdmin(requestor.getUsername())) {
			ACLOperationsDelegate.getInstance().getUserOperations().deleteUser(username);
		} else {
			throw new RuntimeException("Insufficient Access");
		}
	}

	@Override
	public void updateEmailAddress(Credentials requestor, String username,
			String newEmail) {
		if(ACLOperationsDelegate.getInstance().getUserOperations().isAdmin(requestor.getUsername())) {
			User u = ACLOperationsDelegate.getInstance().getUserOperations().getUser(username);
			ACLOperationsDelegate.getInstance().getUserOperations().updateUser(u.getUsername(), newEmail, Boolean.toString(u.isAdmin()), u.getPassword());
		} else {
			throw new RuntimeException("Insufficient Access");
		}
	}

	@Override
	public void updateIsAdmin(Credentials requestor, String username,
			boolean isAdmin) {
		if(ACLOperationsDelegate.getInstance().getUserOperations().isAdmin(requestor.getUsername())) {
			User u = ACLOperationsDelegate.getInstance().getUserOperations().getUser(username);
			ACLOperationsDelegate.getInstance().getUserOperations().updateUser(u.getUsername(), u.getEmail(), Boolean.toString(isAdmin), u.getPassword());
		} else {
			throw new RuntimeException("Insufficient Access");
		}
	}

	@Override
	public void updatePassword(Credentials requestor, String username,
			String newPassword) {
		if(ACLOperationsDelegate.getInstance().getUserOperations().isAdmin(requestor.getUsername())) {
			ACLOperationsDelegate.getInstance().getUserOperations().setPassword(username, newPassword);
		}  else {
			throw new RuntimeException("Insufficient Access");
		}
	}

	@Override
	public List<String> getAllProjects(Credentials requestor) {
		if(ACLOperationsDelegate.getInstance().getUserOperations().isAdmin(requestor.getUsername())) {
			return RepositoryListing.getProjectPaths(ApplicationProperties
					.getProperty("repository_url"), ApplicationProperties
					.getProperty("repository_username"), ApplicationProperties
					.getProperty("repository_password"));
		}
		throw new RuntimeException("Insufficient Access");
	}

	@Override
	public List<String> getSubscriptions(Credentials requestor, String username) {
		if(ACLOperationsDelegate.getInstance().getUserOperations().isAdmin(requestor.getUsername())) {
			return ACLOperationsDelegate.getInstance().getUserOperations().getSubscriptions(username);
		} 
		throw new RuntimeException("Insufficient Access");
	}


}
