package com.peterfranza.svnadmin.server.handlers;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.peterfranza.svnadmin.server.acldb.ACLOperationsDelegate;

public class ListGroups implements DataFeed {

	public void respond(PrintWriter writer, HttpServletRequest request,
			HttpServletResponse response) {
		
		List<String> names = ACLOperationsDelegate.getInstance().getGroupOperations().getGroupNames();	
		
		response.setContentType("text/xml");
		writer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		writer.append("<dataset>");
		writer.append("  <results>"+names.size()+"</results>");
		
		int start = request.getParameter("start") != null ? Integer.valueOf(request.getParameter("start")) : 0;
		int limit = request.getParameter("limit") != null ? Integer.valueOf(request.getParameter("limit")) : 10;
		
		for (int i = start; i < start + limit && i < names.size(); i++ ) {
			String s = names.get(i);
			writer.append("  <row>");
			writer.append("    <name>"+s+"</name>");
			writer.append("  </row>");	
		}

		writer.append(" </dataset>");

	}

}
