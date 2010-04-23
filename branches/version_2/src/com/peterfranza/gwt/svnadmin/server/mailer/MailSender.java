package com.peterfranza.gwt.svnadmin.server.mailer;

import java.util.Collection;

import com.peterfranza.gwt.svnadmin.server.entitydata.userdata.User;

public interface MailSender{
	void sendMail(Collection<User> entities, String subject, String body);
}
