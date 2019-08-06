package de.m3y3r.nsmtp.model.imf;

/**
 * https://tools.ietf.org/html/rfc5322
 * 
 * @author thomas
 *
 */
public class InternetMessage {
	private final Headers headers;
	private final Body body;

	public InternetMessage(Headers headers, Body body) {
		this.headers = headers;
		this.body = body;
	}

	public Headers getHeaders() {
		return headers;
	}
	public Body getBody() {
		return body;
	}
}
