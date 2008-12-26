package com.peterfranza.svnadmin.server.rpc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.mortbay.gwt.AsyncRemoteServiceServlet;

import com.gorthaur.svnadmin.client.rpcinterface.UserOperationsInterface;
import com.gorthaur.svnadmin.client.rpcinterface.beans.Credentials;
import com.gorthaur.svnadmin.client.rpcinterface.beans.UserInfo;
import com.peterfranza.svnadmin.server.ApplicationProperties;
import com.peterfranza.svnadmin.server.RepositoryListing;
import com.peterfranza.svnadmin.server.acldb.ACLOperationsDelegate;
import com.peterfranza.svnadmin.server.acldb.ACLDB.Group;
import com.peterfranza.svnadmin.server.acldb.ACLDB.Subscription;
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
				l.add(new UserInfo(user.getUsername(), user.getEmail(), user.isAdmin(), asCSV(user.getSubscriptions()), asGroupCSV(user.getGroups())));
			}
			return l;
		} else {
			throw new RuntimeException("Insufficient Access");
		}
	}

	private String asGroupCSV(List<Group> groups) {
		StringBuffer b = new StringBuffer();
		for (Iterator<Group> iterator = groups.iterator(); iterator.hasNext();) {
			Group group = iterator.next();
			b.append(group.getName());
			if(iterator.hasNext()) {
				b.append(",");
			}
		}
		return b.toString();
	}

	private String asCSV(List<Subscription> subscriptions) {
		StringBuffer b = new StringBuffer();
		for (Iterator<Subscription> iterator = subscriptions.iterator(); iterator.hasNext();) {
			Subscription subscription = iterator.next();
			b.append(subscription.getPath());
			if(iterator.hasNext()) {
				b.append(",");
			}
		}
		return b.toString();
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

	@Override
	public void joinGroup(Credentials requestor, String username,
			String groupName) {
		if(ACLOperationsDelegate.getInstance().getUserOperations().isAdmin(requestor.getUsername())) {
			ArrayList<String> list = new ArrayList<String>();
				list.add(username);
			ACLOperationsDelegate.getInstance().getGroupOperations().modifyGroup(groupName, list, new ArrayList<String>());
 		} else {
 			throw new RuntimeException("Insufficient Access");
 		}
		
	}

	@Override
	public void leaveGroup(Credentials requestor, String username,
			String groupName) {
		if(ACLOperationsDelegate.getInstance().getUserOperations().isAdmin(requestor.getUsername())) {
			ArrayList<String> list = new ArrayList<String>();
				list.add(username);
			ACLOperationsDelegate.getInstance().getGroupOperations().modifyGroup(groupName, new ArrayList<String>(), list);
		} else {
			throw new RuntimeException("Insufficient Access");
		}
	}

	@Override
	public void addSubscription(Credentials requestor, String username, String subscription) {
		if(ACLOperationsDelegate.getInstance().getUserOperations().isAdmin(requestor.getUsername())) {
			ACLOperationsDelegate.getInstance().getUserOperations().addSubscription(username, subscription);
		} else {
			throw new RuntimeException("Insufficient Access");
		}
	}

	@Override
	public void removeSubscription(Credentials requestor, String username, String subscription) {
		if(ACLOperationsDelegate.getInstance().getUserOperations().isAdmin(requestor.getUsername())) {
			ACLOperationsDelegate.getInstance().getUserOperations().removeSubscription(username, subscription);
		} else {
			throw new RuntimeException("Insufficient Access");
		}
	}


}
