package de.m3y3r.nsmtp.model.imf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InternetMessageBuilder {

	private static final Logger log = LoggerFactory.getLogger(InternetMessageBuilder.class);

	private enum State {HEADER, BODY}
	private static final int MAX_LINE_LENGTH = 998;
	private static final String LINE_TOO_LONG = "Line length exceeds max specified length in RFC";
	private State state = State.HEADER;

	private Headers headers = new Headers();
	private Body body = new Body();

	/*
	 * unlike the RFC the CRLF delimiter will be stripped of in this implementation
	 */
	public InternetMessageBuilder addLine(CharSequence line) {
		if(line.length() == 0) {
			state = State.BODY;
			return this;
		}

		//TODO: where to put all these constraints?
		if(line.length() > MAX_LINE_LENGTH) {
			log.error("Line too long {}", line.length());
			throw new IllegalArgumentException(LINE_TOO_LONG);
		}

		switch(state) {
		case HEADER:
			headers.addHeader(Header.parse(line));
			break;
		case BODY:
//			body.addLine(line);
			break;
		}
		return this;
	}

	public InternetMessage build() {
//		headers.validate();
//		body.validate();
		return new InternetMessage(headers, body);
	}

}
