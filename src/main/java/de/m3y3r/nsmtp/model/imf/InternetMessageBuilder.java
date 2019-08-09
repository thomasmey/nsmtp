package de.m3y3r.nsmtp.model.imf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InternetMessageBuilder {

	private static final Logger log = LoggerFactory.getLogger(InternetMessageBuilder.class);

	private enum State {HEADER, BODY}
	private static final int MAX_LINE_LENGTH = 998;
	private State state = State.HEADER;

	private Headers headers = new Headers();
	private AbstractBody body = new RFC5322Body();

	private StringBuilder unfoldedLine;
	private CharSequence lastLine;

	/*
	 * unlike the RFC the CRLF delimiter is assumed to be already stripped of in this implementation!
	 */
	public InternetMessageBuilder addLine(CharSequence line) {
		if(line.length() > MAX_LINE_LENGTH) {
			log.error("Line too long {}", line.length());
			throw new LineTooLongException();
		}

		switch(state) {
		case HEADER:
			if(lastLine != null) { // this is the first header line we do process
				if(Header.isFolded(line)) { // is the current line folded?
					if(unfoldedLine == null) { // are we unfolding the first line
						unfoldedLine = new StringBuilder(lastLine); // append previous line to unfolded line
					} else {
						unfoldedLine.append(lastLine);
					}
				} else {
					if(unfoldedLine != null) { // process any previous folded lines
						unfoldedLine.append(lastLine);
						headers.addHeader(Header.parse(unfoldedLine));
						unfoldedLine = null;
					} else {
						headers.addHeader(Header.parse(lastLine));
					}
				}
			}

			if(line.length() == 0) { // empty line switches from headers to body
				/* TODO: is this empty line mandatory?
				 * NO! The empty line is optional and belongs to the body! see 3.5.  Overall Message Syntax!
				 * FIXME: how to ensure that the last field/header line is processed correctly?
				 * introduce an finish() method?!
				 */
				state = State.BODY;
				lastLine = null;
			} else {
				lastLine = line;
			}
			break;
		case BODY:
			System.out.println("body: " + line);
			body.addLine(line);
			break;
		}
		return this;
	}

	public InternetMessage build() {
//		headers.validate(); // check for duplicate headers or not existing mandatory headers
//		body.validate();
		return new InternetMessage(headers, body);
	}

}
