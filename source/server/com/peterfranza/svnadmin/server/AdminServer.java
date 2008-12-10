package com.peterfranza.svnadmin.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.jetty.Request;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;

import com.peterfranza.svnadmin.server.acldb.ACLOperationsDelegate;
import com.peterfranza.svnadmin.server.handlers.AddNewUser;
import com.peterfranza.svnadmin.server.handlers.Authenticate;
import com.peterfranza.svnadmin.server.handlers.ChangePassword;
import com.peterfranza.svnadmin.server.handlers.DataFeed;
import com.peterfranza.svnadmin.server.handlers.ListUsers;
import com.peterfranza.svnadmin.server.handlers.UserPreferences;

public class AdminServer {

	private Map<String, DataFeed> feeds = new HashMap<String, DataFeed>() {
		private static final long serialVersionUID = 532606649367845419L;

		{
			put("/rest/auth", new Authenticate());
			put("/rest/listUsers", new ListUsers());
			put("/rest/changepassword", new ChangePassword());
			put("/rest/adduser", new AddNewUser());
			put("/rest/userPrefs", new UserPreferences());
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
							response.setStatus(HttpServletResponse.SC_OK);
							feed.respond(response.getWriter(), request, response);
							((Request) request).setHandled(true);
						}
					} else {
						response.setContentType("text/html");
						response.getWriter().println("Access Denied");
						response.setStatus(HttpServletResponse.SC_FORBIDDEN);
						((Request) request).setHandled(true);
					}
				}
			}
		});
		
		server.addHandler(new AbstractHandler() {

			public void handle(String target, HttpServletRequest request,
					HttpServletResponse response, int dispatch) throws IOException,
					ServletException {
				
				if(target.equals("/")) {
					target = "/SvnAdministration.html";
				}

				String str = "/com.gorthaur.svnadmin.SvnAdministration" + target;
			    InputStream fis  = AdminServer.class.getResourceAsStream(str);
				    if(fis != null) {
				    	response.setStatus(HttpServletResponse.SC_OK);
				    	

				    	try {
				    		byte[] buf = new byte[1024];
				    		int i = 0;
				    		while ((i = fis.read(buf)) != -1) {
				    			response.getOutputStream().write(buf, 0, i);
				    		}
				    	} catch(Exception e) {
						    System.out.println(str);
				    		e.printStackTrace();
				    	}
				    	((Request) request).setHandled(true);
				    }

			}
			
		});

		server.start();
	}
	
	public static boolean authenticate(String username, String password) {
		return ACLOperationsDelegate.getInstance().authenticate(username, password);
	}
	
	public static void outputError(Exception e, PrintWriter out) {
		for(StackTraceElement ste: e.getStackTrace()) {
			out.println(ste.toString());
		}
	}
	
	public static boolean isAdmin(String username) {	
		return ACLOperationsDelegate.getInstance().isAdmin(username);
	}
	
	public static void main(String[] args) throws Exception {
		if(args.length == 0) {
			System.out.println("Starting Server.");
			new AdminServer(Integer.valueOf(ApplicationProperties
					.getProperty("server_port")));
		} else if(args.length == 2 && args[0].equals("--notify")) {
			ChangeSetEmailer.sendChangeSetEmail(Long.valueOf(args[1]));
		} else {
			System.out.println("Usage: <noargs>        - start server");
			System.out.println("       --notify <changeset number> - send notification hook");
		}	
	}

}
