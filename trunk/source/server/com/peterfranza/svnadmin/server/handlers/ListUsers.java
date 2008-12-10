package com.peterfranza.svnadmin.server.handlers;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.peterfranza.svnadmin.server.AdminServer;
import com.peterfranza.svnadmin.server.acldb.ACLOperationsDelegate;
import com.peterfranza.svnadmin.server.acldb.ACLDB.Subscription;
import com.peterfranza.svnadmin.server.acldb.ACLDB.User;

public class ListUsers implements DataFeed {

	public void respond(PrintWriter writer, HttpServletRequest request, HttpServletResponse response) {
		
		try {
						
			List<String> names = ACLOperationsDelegate.getInstance().getUsernames();	
			
			response.setContentType("text/xml");
			writer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			writer.append("<dataset>");
			writer.append("  <results>"+names.size()+"</results>");
			
			int start = request.getParameter("start") != null ? Integer.valueOf(request.getParameter("start")) : 0;
			int limit = request.getParameter("limit") != null ? Integer.valueOf(request.getParameter("limit")) : 10;
			
			for (int i = start; i < start + limit && i < names.size(); i++ ) {
				User u = ACLOperationsDelegate.getInstance().getUser(names.get(i));
				writer.append("  <row>");
				writer.append("    <name>"+u.getUsername()+"</name>");
				writer.append("    <email>"+u.getEmail()+"</email>");
				writer.append("    <admin>"+Boolean.toString(AdminServer.isAdmin(u.getUsername()))+"</admin>");
				writer.append("    <subscriptions>");

				for (Iterator<Subscription> iterator = u.getSubscriptions().iterator(); iterator.hasNext();) {
					Subscription subscription = iterator.next();
					writer.append(subscription.getPath());
					if(iterator.hasNext()) {
						writer.append(",");
					}
				}
				
				writer.append("    </subscriptions>");
				writer.append("    <groups>");
				for (Iterator<String> iterator = ACLOperationsDelegate.getInstance().getGroupMembership(u.getUsername()).iterator(); iterator.hasNext();) {
					String group = iterator.next();
					writer.append(group);
					if(iterator.hasNext()) {
						writer.append(",");
					}
				}
				writer.append("    </groups>");
				writer.append("  </row>");	
			}
			
			writer.append(" </dataset>");

			
		} catch (Exception e) {
			AdminServer.outputError(e, writer);
		}
	}

}
