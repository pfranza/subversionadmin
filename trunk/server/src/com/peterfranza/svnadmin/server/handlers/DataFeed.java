package com.peterfranza.svnadmin.server.handlers;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;

public interface DataFeed {

	void respond(PrintWriter writer, HttpServletRequest request);

}
