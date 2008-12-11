package com.peterfranza.svnadmin.server.handlers;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.peterfranza.svnadmin.server.AdminServer;
import com.peterfranza.svnadmin.server.acldb.ACLOperationsDelegate;

public class DeleteUser implements DataFeed {

	public void respond(PrintWriter writer, HttpServletRequest request,
			HttpServletResponse response) {
		if(AdminServer.isAdmin(request.getParameter("username"))) {
			ACLOperationsDelegate.getInstance().getUserOperations().deleteUser(request.getParameter("targetUser"));
			writer.append("User Deleted");
		} else {
			writer.append("Invalid Privledge For Operation");
		}
	}

}
