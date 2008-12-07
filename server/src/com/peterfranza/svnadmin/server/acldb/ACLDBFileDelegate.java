package com.peterfranza.svnadmin.server.acldb;

import java.io.File;


import com.peterfranza.svnadmin.server.SupplementalData;
import com.peterfranza.svnadmin.server.acldb.ACLDB.User;
import com.peterfranza.svnadmin.server.acldb.filedelegates.HtPasswordFileWrapper;
import com.peterfranza.svnadmin.server.acldb.filedelegates.HtPasswd.UserNamePasswordSet;

public class ACLDBFileDelegate {
	
	private ACLDB acl = new ACLDB();
	
	public ACLDBFileDelegate(File htpasswd) throws Exception {
		
		SupplementalData supplement = SupplementalData.getInstance();
		
		//populate username data
		HtPasswordFileWrapper htpasswdWrapper = new HtPasswordFileWrapper(htpasswd);
		for (UserNamePasswordSet u : htpasswdWrapper.getAllUsers()) {
			acl.getUsers().add(new User(u.getUsername(), u.getPassword(), supplement.getUserEmailAddress(u.getUsername())));
		}
		
		
		
	}
	
}
