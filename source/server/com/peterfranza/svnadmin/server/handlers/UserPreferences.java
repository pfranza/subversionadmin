package com.peterfranza.svnadmin.server.handlers;

import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.peterfranza.svnadmin.server.acldb.ACLOperationsDelegate;
import com.peterfranza.svnadmin.server.acldb.ACLDB.Subscription;
import com.peterfranza.svnadmin.server.acldb.ACLDB.User;

public class UserPreferences implements DataFeed {

	public void respond(PrintWriter writer, HttpServletRequest request,
			HttpServletResponse response) {
		
		User u = ACLOperationsDelegate.getInstance().getUser(request.getParameter("query"));
		
		response.setContentType("text/xml");
		writer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		writer.append("<dataset>");
		writer.append("  <results>1</results>");
		writer.append("  <row>");
		writer.append("    <name>"+u.getUsername()+"</name>");
		writer.append("    <email>"+u.getEmail()+"</email>");
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
		writer.append(" </dataset>");
		
		
	}

}
