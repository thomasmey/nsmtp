package de.m3y3r.nstmp.model;

public class SmtpCommandReply {

	private final SmtpReplyStatus replyCode;
	private final CharSequence text;

	public SmtpCommandReply(SmtpReplyStatus statusCode, CharSequence text) {
		replyCode = statusCode;
		this.text = text;
	}

	public SmtpReplyStatus getReplyCode() {
		return replyCode;
	}

	public CharSequence getText() {
		return text;
	}
}
