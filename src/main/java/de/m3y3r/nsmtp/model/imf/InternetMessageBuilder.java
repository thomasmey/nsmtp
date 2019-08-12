package de.m3y3r.nsmtp.model.imf;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class InternetMessageBuilder {

	private static final Logger log = LoggerFactory.getLogger(InternetMessageBuilder.class);

	private enum State {HEADER, BODY}
	private static final int MAX_LINE_LENGTH = 998;
	private State state = State.HEADER;

	private Headers headers = new Headers();
	private AbstractBody body;

	private ByteBuf unfoldedLine = ByteBufAllocator.DEFAULT.buffer(); // TODO: somehow restrict this regarding max length?!

	/*
	 * unlike the RFC the CRLF delimiter is assumed to be already stripped of of the end of line in this implementation!
	 */
	public InternetMessageBuilder addLine(ByteBuf line) {
		int lineLength = line.readableBytes();

		if(lineLength > MAX_LINE_LENGTH) {
			log.error("Line too long {}", lineLength);
			throw new LineTooLongException();
		}

		switch(state) {
		case HEADER:
			if(lineLength == 0) { // empty line switches from headers to body
				finishHeaders();
				state = State.BODY;
				break;
			}

			if(!Header.isFolded(line)) {
				processUnfoldedLine();
			}
			unfoldedLine.writeBytes(line);
			break;

		case BODY:
			ByteBuf l = line.copy();
			System.out.println("body: " + l.getCharSequence(l.readerIndex(), l.readableBytes(), StandardCharsets.US_ASCII));
			body.addLine(l);
			break;
		}
		return this;
	}

	private void finishHeaders() {
		processUnfoldedLine(); // process any remaining data
		headers.validate();

		// Decide what body parser/processor to use
		if(headers.getHeaders().contains(new Header("MIME-Version", "1.0"))) {
//			body = new RFC2045Body(); //RFC2045
		} else {
			body = new RFC5322Body();
		}
	}

	private void processUnfoldedLine() {
		if(unfoldedLine.isReadable()) { // process any previous folded lines
			headers.addHeader(Header.parse(unfoldedLine));
			unfoldedLine.clear();
		}		
	}

	public InternetMessage build() {
		if(state == State.HEADER) {
			finishHeaders();
		}
		body.validate();
		return new InternetMessage(headers, body);
	}

}
