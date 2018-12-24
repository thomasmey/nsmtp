package de.m3y3r.nstmp.handler.codec.smtp.model;

public class SmtpReplyStatus {

	public static final SmtpReplyStatus R220 = new SmtpReplyStatus((short) 220);
	public static final SmtpReplyStatus R221 = new SmtpReplyStatus((short) 221);
	public static final SmtpReplyStatus R250 = new SmtpReplyStatus((short) 250);

	private short status;

	public SmtpReplyStatus(short status) {
		this.setStatus(status);
	}

	public short getStatus() {
		return status;
	}

	public void setStatus(short status) {
		//TODO: check for valid status codes
		this.status = status;
	}
}
