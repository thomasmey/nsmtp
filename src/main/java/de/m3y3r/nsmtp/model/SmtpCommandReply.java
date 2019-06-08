package de.m3y3r.nsmtp.model;

import java.util.ArrayList;
import java.util.List;

public class SmtpCommandReply {

	private final SmtpReplyStatus replyCode;
	private final List<CharSequence> text;

	public SmtpCommandReply(SmtpReplyStatus statusCode, CharSequence text) {
		replyCode = statusCode;
		this.text = new ArrayList<>();
		this.text.add(text);
	}

	public SmtpCommandReply(SmtpReplyStatus statusCode, List<String> lines) {
		replyCode = statusCode;
		this.text = new ArrayList<>();
		this.text.addAll(lines);
	}

	public SmtpReplyStatus getReplyCode() {
		return replyCode;
	}

	public List<CharSequence> getLines() {
		return this.text;
	}
}
