package com.peterfranza.gwt.svnadmin.server.mailer;

import java.util.Collection;

import com.peterfranza.gwt.svnadmin.server.entitydata.Entity;

public interface MailSender{
	void sendMail(Collection<Entity> entities, String subject, String body);
}
