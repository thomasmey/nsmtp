package de.m3y3r.nstmp.handler.codec.smtp.model;

import io.netty.util.AsciiString;

public class SmtpCommandReply {

	private final SmtpReplyStatus replyCode;
	private final String text;

	public SmtpCommandReply(SmtpReplyStatus statusCode, String text) {
		replyCode = statusCode;
		this.text = text;
	}

	public SmtpReplyStatus getReplyCode() {
		return replyCode;
	}

	public String getText() {
		return text;
	}
}
