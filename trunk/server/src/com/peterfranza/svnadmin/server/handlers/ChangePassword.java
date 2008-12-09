package com.peterfranza.svnadmin.server.handlers;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;

import com.peterfranza.svnadmin.server.AdminServer;
import com.peterfranza.svnadmin.server.acldb.ACLOperationsDelegate;

public class ChangePassword implements DataFeed {

//	/rest/changepassword?username=pfranza&passwd=password&targetuser=u1&newpassword=newpass
	
	public void respond(PrintWriter writer, HttpServletRequest request) {
		try {
			
			if(request.getParameter("targetuser").equals(request.getParameter("username")) ||
					isAdmin(request.getParameter("username"))) {
				ACLOperationsDelegate.getInstance().setPassword(request.getParameter("targetuser"), request.getParameter("newpassword"));
				writer.println("ok");
			} else {
				writer.println("operation not allowed");
			}
		} catch (Exception e) {
			AdminServer.outputError(e, writer);
		}

	}

	private boolean isAdmin(String username) {	
		return ACLOperationsDelegate.getInstance().isAdmin(username);
	}

}
