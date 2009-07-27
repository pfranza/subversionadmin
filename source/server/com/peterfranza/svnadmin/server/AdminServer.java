package com.peterfranza.svnadmin.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.gwt.AsyncRemoteServiceServlet;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.resource.FileResource;
import org.mortbay.resource.Resource;

import com.peterfranza.svnadmin.server.acldb.ACLOperationsDelegate;
import com.peterfranza.svnadmin.server.rpc.AdminOperations;
import com.peterfranza.svnadmin.server.rpc.Authenticator;
import com.peterfranza.svnadmin.server.rpc.GroupOperations;
import com.peterfranza.svnadmin.server.rpc.UserOperations;

public class AdminServer {

	private Map<String, Class<? extends AsyncRemoteServiceServlet>> feeds = new HashMap<String, Class<? extends AsyncRemoteServiceServlet>>() {
		private static final long serialVersionUID = 532606649367845419L;
		{
			put("/rpc/auth", Authenticator.class);
			put("/rpc/user", UserOperations.class);
			put("/rpc/group", GroupOperations.class);
			put("/rpc/admin", AdminOperations.class);
		}
	};

	public AdminServer(int port) throws Exception {
		Server server = new Server(port);
		
		ServletHandler handler=new ServletHandler();
		handler.addServletWithMapping(new ServletHolder(), "/");

		Context root = new Context(server,"/",Context.SESSIONS){

			@Override
			public Resource getResource(String res)
					throws MalformedURLException {
				
				Resource r =  super.getResource(res);
				if(r == null) {
					try {
						r = new FileResource(AdminServer.class.getResource("/com.gorthaur.svnadmin.SvnAdministration" + res));
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
				return r;
			}
		};
		root.addServlet(new ServletHolder(new HttpServlet() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 3668249982363221393L;

			@Override
			protected void doGet(HttpServletRequest req,
					HttpServletResponse response) throws ServletException,
					IOException {
				String username = req.getParameter("username");
				
				if(isAdmin(username)){
					String filename = req.getPathInfo().replaceAll("/", "");

					File backup = BackupListings.getFile(filename);
					if(backup.exists()) {
						response.setStatus(HttpServletResponse.SC_OK);
						response.setContentType("binary/octet-stream");
						try {
							FileInputStream fis = new FileInputStream(backup);
							byte[] buf = new byte[1024];
							int i = 0;
							while ((i = fis.read(buf)) != -1) {
								response.getOutputStream().write(buf, 0, i);
							}
						} catch(Exception e) {
							System.out.println(filename);
							e.printStackTrace();
						}

					}
				} else {
					super.doGet(req, response);
				}

			}
		}), "/backups/*");
		

		
		
		root.addServlet(new ServletHolder(new HttpServlet() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 7286815973900226146L;

			@Override
			protected void doGet(HttpServletRequest request,
					HttpServletResponse response) throws ServletException,
					IOException {
				
				String target = request.getPathInfo();
				
				
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
				    } else {
				    	System.out.println("can't find " + str);
				    }
				    
				
			}
		}), "/*");


		for(Entry<String, Class<? extends AsyncRemoteServiceServlet>> e: feeds.entrySet()) {
			root.addServlet(e.getValue(), e.getKey());
		}
			
		server.start();
		server.join();
	}
	

	public static void outputError(Exception e, PrintWriter out) {
		for(StackTraceElement ste: e.getStackTrace()) {
			out.println(ste.toString());
		}
	}
	
	public static boolean isAdmin(String username) {	
		return ACLOperationsDelegate.getInstance().getUserOperations().isAdmin(username);
	}
	
	public static void main(String[] args) throws Exception {
		if(args.length == 0) {
			System.out.println("Starting Server.");
			new AdminServer(Integer.valueOf(ApplicationProperties
					.getProperty("server_port")));
			ACLOperationsDelegate.getInstance().getGroupOperations();
		} else if(args.length == 2 && args[0].equals("--notify")) {
			ChangeSetEmailer.sendChangeSetEmail(Long.valueOf(args[1]));
		} else {
			System.out.println("Usage: <noargs>        - start server");
			System.out.println("       --notify <changeset number> - send notification hook");
		}	
	}

}
