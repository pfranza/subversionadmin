package com.peterfranza.svnadmin.server.handlers;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.peterfranza.svnadmin.server.acldb.ACLOperationsDelegate;

public class CreateGroup implements DataFeed {

	public void respond(PrintWriter writer, HttpServletRequest request,
			HttpServletResponse response) {
		
		ACLOperationsDelegate.getInstance().getGroupOperations()
			.createGroup(request.getParameter("groupName"), splitList(request.getParameter("members")));
			
	}

	private List<String> splitList(String parameter) {
		List<String> l = new ArrayList<String>();
		for(String s: parameter.split("\\s*,\\s*")) {
			l.add(s);
		}
		return l;
	}

}
