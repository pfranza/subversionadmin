package com.peterfranza.svnadmin.server.handlers;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.peterfranza.svnadmin.server.acldb.ACLOperationsDelegate;

public class ListGroupMembers implements DataFeed {

	public void respond(PrintWriter writer, HttpServletRequest request,
			HttpServletResponse response) {
		
		List<String> included = getIncluded(request.getParameter("groupName"));
		List<String> excluded = getExcluded(request.getParameter("groupName"));
		
		response.setContentType("text/xml");
		writer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		writer.append("<dataset>");
		writer.append("<included>");
		for(String i: included) {
			writer.append("<incname><name>").append(i).append("</name></incname>");
		}
		writer.append("</included>");
		
		writer.append("<excluded>");
		for(String i: excluded) {
			writer.append("<excname><name>").append(i).append("</name></excname>");
		}
		writer.append("</excluded>");
		
		writer.append(" </dataset>");
		
		
	}

	private List<String> getExcluded(String parameter) {
		List<String> l = new ArrayList<String>();
		if(parameter != null) {
			
		} else {
			l.addAll(ACLOperationsDelegate.getInstance().getUserOperations().getUsernames());
			for(String s: ACLOperationsDelegate.getInstance().getGroupOperations().getGroupNames()) {
				l.add("@" + s);
			}
		}
		return l;
	}

	private List<String> getIncluded(String parameter) {
		List<String> l = new ArrayList<String>();
		if(parameter != null) {
			
		}
		return l;
	}

}
