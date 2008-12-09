package com.peterfranza.svnadmin.server.handlers;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.peterfranza.svnadmin.server.AdminServer;
import com.peterfranza.svnadmin.server.acldb.ACLOperationsDelegate;

public class ListUsers implements DataFeed {

	public void respond(PrintWriter writer, HttpServletRequest request) {
		
		try {
						
			List<String> names = ACLOperationsDelegate.getInstance().getUsernames();
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
