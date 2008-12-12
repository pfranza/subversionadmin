package com.peterfranza.svnadmin.server.rpc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mortbay.gwt.AsyncRemoteServiceServlet;

import com.gorthaur.svnadmin.client.rpcinterface.GroupOperationsInterface;
import com.gorthaur.svnadmin.client.rpcinterface.beans.Credentials;
import com.peterfranza.svnadmin.server.acldb.ACLOperationsDelegate;

public class GroupOperations extends AsyncRemoteServiceServlet implements GroupOperationsInterface{

	private static final long serialVersionUID = -6756172774921001097L;

	public String createGroup(Credentials requestor, String groupName,
			List<String> members) {
		
		if(ACLOperationsDelegate.getInstance().getUserOperations().isAdmin(requestor.getUsername())) {
			ACLOperationsDelegate.getInstance().getGroupOperations().createGroup(groupName, members);
			return "ok";
		} else {
			return "Insufficiant Access";
		}
		
	}

	public boolean isGroup(Credentials requestor, String groupName) {
		return ACLOperationsDelegate.getInstance().getGroupOperations().isGroup(groupName);
	}

	public List<String> listGroups(Credentials requestor) {
		if(ACLOperationsDelegate.getInstance().getUserOperations().isAdmin(requestor.getUsername())) {
			return ACLOperationsDelegate.getInstance().getGroupOperations().getGroupNames();
		} 
		throw new RuntimeException("Insufficiant Access");
	}

	public List<String> listGroupMembers(Credentials requestor, String groupName) {
		if(ACLOperationsDelegate.getInstance().getUserOperations().isAdmin(requestor.getUsername())) {
			return ACLOperationsDelegate.getInstance().getGroupOperations().getGroupMembers(groupName);
		} 
		throw new RuntimeException("Insufficiant Access");
	}

	public List<String> listGroupNonMembers(Credentials requestor,
			String groupName) {
		if(ACLOperationsDelegate.getInstance().getUserOperations().isAdmin(requestor.getUsername())) {
			ArrayList<String> l = new ArrayList<String>();
			for(String s: listGroups(requestor)) {
				l.add("@" + s);
			}
			l.addAll(ACLOperationsDelegate.getInstance().getUserOperations().getUsernames());
			l.remove("@" + groupName);
			l.removeAll(listGroupMembers(requestor, groupName));
			Collections.sort(l);
			return l;
		} 
		throw new RuntimeException("Insufficiant Access");
	}
	
	

}
