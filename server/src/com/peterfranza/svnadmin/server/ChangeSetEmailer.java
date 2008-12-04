package com.peterfranza.svnadmin.server;

import com.peterfranza.svnadmin.server.ChangeMiner.ChangeSummary;

public class ChangeSetEmailer {

	public static void main(String[] args) {
		
		ChangeSummary set = ChangeMiner.getSummary("http://subversionadmin.googlecode.com/svn/trunk", "", "", 27);
		System.out.println(set);
	}
	
}
