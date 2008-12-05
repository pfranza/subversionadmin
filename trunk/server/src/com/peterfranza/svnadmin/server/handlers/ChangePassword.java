package com.peterfranza.svnadmin.server.handlers;

import java.io.File;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;

import com.peterfranza.svnadmin.server.AdminServer;
import com.peterfranza.svnadmin.server.ApplicationProperties;
import com.peterfranza.svnadmin.server.SupplementalData;
import com.peterfranza.svnadmin.server.SupplementalData.User;
import com.peterfranza.svnadmin.server.SupplementalData.User.ACCESS_LEVEL;
import com.peterfranza.svnadmin.server.htpasswd.HtPasswordFileWrapper;

public class ChangePassword implements DataFeed {

//	/rest/changepassword?username=pfranza&passwd=password&targetuser=u1&newpassword=newpass
	
	public void respond(PrintWriter writer, HttpServletRequest request) {
		try {
			HtPasswordFileWrapper passwd = new HtPasswordFileWrapper(new File(ApplicationProperties.getProperty("access_file")));
			if(request.getParameter("targetuser").equals(request.getParameter("username")) ||
					isAdmin(request.getParameter("username"))) {
				passwd.setUserPassword(request.getParameter("targetuser"), request.getParameter("newpassword"));
				writer.println("ok");
			} else {
				writer.println("operation not allowed");
			}
		} catch (Exception e) {
			AdminServer.outputError(e, writer);
		}

	}

	private boolean isAdmin(String username) {
		try {
			SupplementalData data = SupplementalData.getInstance();
			for(User u: data.getUsers()) {
				if(u.getUsername().equals(username)){
					return u.getAccessLevel().equals(ACCESS_LEVEL.ADMIN);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
