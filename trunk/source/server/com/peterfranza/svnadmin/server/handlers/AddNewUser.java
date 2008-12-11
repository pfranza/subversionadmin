package com.peterfranza.svnadmin.server.handlers;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.peterfranza.svnadmin.server.AdminServer;
import com.peterfranza.svnadmin.server.acldb.ACLOperationsDelegate;

public class AddNewUser implements DataFeed {

	public void respond(PrintWriter writer, HttpServletRequest request, HttpServletResponse response) {
		if(	AdminServer.isAdmin(request.getParameter("username"))) {
			
			if(ACLOperationsDelegate.getInstance().getUserOperations().getUsernames()
					.contains(request.getParameter("newUsername"))) {
				writer.println("User Already Exists");
			} else {

				ACLOperationsDelegate.getInstance().getUserOperations().addNewUser(
						request.getParameter("newUsername"),
						request.getParameter("newPassword"),
						request.getParameter("newEmail"));
				
				writer.println("ok");
			}
		} else {
			writer.println("operation not allowed");
		}

	}

}
