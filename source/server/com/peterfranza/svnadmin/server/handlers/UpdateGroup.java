package com.peterfranza.svnadmin.server.handlers;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.peterfranza.svnadmin.server.acldb.ACLOperationsDelegate;

public class UpdateGroup implements DataFeed {

	public void respond(PrintWriter writer, HttpServletRequest request,
			HttpServletResponse response) {
		
		ACLOperationsDelegate.getInstance().getGroupOperations().modifyGroup(
				request.getParameter("groupName"),
				splitUsernames(request.getParameter("addUsers")),
				splitUsernames(request.getParameter("removeUsers"))
				);
		

	}

	private List<String> splitUsernames(String s) {
		List<String> l = new ArrayList<String>();
		for(String n: s.split("\\s*,\\s*")) {
			l.add(n);
		}
		return l;
	}
	
}
