package com.peterfranza.svnadmin.server.handlers;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;

public class Authenticate implements DataFeed {

//	/rest/auth?username=pfranza&passwd=password
	
	public void respond(PrintWriter writer, HttpServletRequest request) {
		writer.println("ok");
	}

}
