package de.m3y3r.nstmp.handler.codec.smtp.model;

import io.netty.util.AttributeKey;

public class SessionContext {

	public static final AttributeKey<SessionContext> ATTRIBUTE_KEY = AttributeKey.valueOf("sessionContext");

	public MailTransaction mailTransaction;

//	public CharSequence currentCmd;
	public CharSequence lastCmd;

}
