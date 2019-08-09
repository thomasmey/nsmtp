package de.m3y3r.nsmtp.model.imf;

/**
 * https://tools.ietf.org/html/rfc5322
 *
 */
public class InternetMessage {

	private final Headers headers;
	private final AbstractBody body;

	public InternetMessage(Headers headers, AbstractBody body) {
		this.headers = headers;
		this.body = body;
	}

	public Headers getHeaders() {
		return headers;
	}
	public AbstractBody getBody() {
		return body;
	}
}
