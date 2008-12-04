package com.peterfranza.svnadmin.server.handlers;

import java.io.File;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.peterfranza.svnadmin.server.AdminServer;
import com.peterfranza.svnadmin.server.ApplicationProperties;
import com.peterfranza.svnadmin.server.htpasswd.HtPasswordFileWrapper;

public class ListUsers implements DataFeed {

	@Override
	public void respond(PrintWriter writer, HttpServletRequest request) {
		
		try {
			HtPasswordFileWrapper passwd = new HtPasswordFileWrapper(new File(ApplicationProperties.getProperty("access_file")));			
			List<String> names = passwd.getUsernames();
			for (Iterator<String> iterator = names.iterator(); iterator.hasNext();) {
				String string = iterator.next();
				writer.write(string);
				if(iterator.hasNext()) {
					writer.append(",");
				}
			}		
		} catch (Exception e) {
			AdminServer.outputError(e, writer);
		}
	}

}
