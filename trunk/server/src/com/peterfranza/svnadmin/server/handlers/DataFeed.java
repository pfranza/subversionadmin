package com.peterfranza.svnadmin.server.handlers;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface DataFeed {

	void respond(PrintWriter writer, HttpServletRequest request, HttpServletResponse response);

}
