package com.peterfranza.svnadmin.server.handlers;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.peterfranza.svnadmin.server.acldb.ACLOperationsDelegate;

public class CheckGroupName implements DataFeed {

	public void respond(PrintWriter writer, HttpServletRequest request,
			HttpServletResponse response) {
		
		if(ACLOperationsDelegate.getInstance().getGroupOperations().isGroup(request.getParameter("groupName"))){
			writer.append("group already exists");
		} else {
			writer.append("ok");
		}
		
	}

}
