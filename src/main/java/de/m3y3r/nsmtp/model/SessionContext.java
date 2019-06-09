package de.m3y3r.nsmtp.model;

import io.netty.util.AttributeKey;

/**
 * models the current mail session, i.e. connection
 * @author thomas
 *
 */
public class SessionContext {

	public static final AttributeKey<SessionContext> ATTRIBUTE_KEY = AttributeKey.valueOf("sessionContext");

	public MailTransaction mailTransaction;

//	public CharSequence currentCmd;
	public CharSequence lastCmd;

	public void resetMailTransaction() {
		this.mailTransaction = null;
	}

}
