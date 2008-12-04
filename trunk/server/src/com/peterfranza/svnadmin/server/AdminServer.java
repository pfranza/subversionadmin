package com.peterfranza.svnadmin.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.jetty.Request;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;

import com.peterfranza.svnadmin.server.handlers.Authenticate;
import com.peterfranza.svnadmin.server.handlers.ChangePassword;
import com.peterfranza.svnadmin.server.handlers.DataFeed;
import com.peterfranza.svnadmin.server.handlers.ListUsers;
import com.peterfranza.svnadmin.server.htpasswd.HtPasswordFileWrapper;

public class AdminServer {

	private Map<String, DataFeed> feeds = new HashMap<String, DataFeed>() {
		private static final long serialVersionUID = 532606649367845419L;

		{
			put("/rest/auth", new Authenticate());
			put("/rest/listUsers", new ListUsers());
			put("/rest/changepassword", new ChangePassword());
		}
	};

	public AdminServer(int port) throws Exception {
		Server server = new Server(port);
		server.addHandler(new AbstractHandler() {
			public void handle(String target, HttpServletRequest request,
					HttpServletResponse response, int dispatch)
					throws IOException, ServletException {
				if (target.startsWith("/rest") ) {
					if(authenticate(request.getParameter("username"), request.getParameter("passwd"))) {
						DataFeed feed = feeds.get(target);
						if (feed != null) {
//							response.setContentType("text/x-json");
							response.setStatus(HttpServletResponse.SC_OK);
							feed.respond(response.getWriter(), request);
							((Request) request).setHandled(true);
						}
					} else {
						response.setContentType("text/html");
						response.getWriter().println("<h2>Access Denied</h2>");
						response.setStatus(HttpServletResponse.SC_FORBIDDEN);
						((Request) request).setHandled(true);
					}
				}
			}
		});
		
		server.addHandler(new AbstractHandler() {

			@Override
			public void handle(String target, HttpServletRequest request,
					HttpServletResponse response, int dispatch) throws IOException,
					ServletException {
				
				if(target.equals("/")) {
					target = "SvnAdministration.html";
				}
				
				File f = new File("www/com.gorthaur.svnadmin.SvnAdministration", target);
				if(f.exists()) {
					response.setStatus(HttpServletResponse.SC_OK);
					
				    FileInputStream fis  = new FileInputStream(f);
				    try {
				        byte[] buf = new byte[1024];
				        int i = 0;
				        while ((i = fis.read(buf)) != -1) {
				            response.getOutputStream().write(buf, 0, i);
				        }
				    } catch(Exception e) {
				    	e.printStackTrace();
				    }
		
					((Request) request).setHandled(true);
				}
			}
			
		});

		server.start();
	}
	
	public static boolean authenticate(String username, String password) {
		try {
			HtPasswordFileWrapper passwd = new HtPasswordFileWrapper(new File(ApplicationProperties.getProperty("access_file")));
			return passwd.authenticate(username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}

	public static void main(String[] args) throws Exception {
		new AdminServer(Integer.valueOf(ApplicationProperties
				.getProperty("server_port")));
	}
	
	public static void outputError(Exception e, PrintWriter out) {
		for(StackTraceElement ste: e.getStackTrace()) {
			out.println(ste.toString());
		}
	}

}
