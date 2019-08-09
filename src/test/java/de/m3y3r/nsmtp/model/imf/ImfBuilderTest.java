package de.m3y3r.nsmtp.model.imf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class ImfBuilderTest {

	private enum State {TEXT, CR, LF};

	@Test
	public void testImf() {
		InternetMessageBuilder imb = new InternetMessageBuilder();
		File rawMail = new File("/home/thomas/db-mail.dump.txt");
		State state = State.TEXT;
		boolean firstLine = true;
		try(InputStream is = new FileInputStream(rawMail)) {
			StringBuffer line = new StringBuffer();
			int b = is.read();
			while(b >= 0) {
				boolean appendChar = true;
				if(state == State.TEXT) {
//					if(b == 13) {
//						state = State.CR;
//						appendChar = false;
//					}
//				} else if(state == State.CR) {
					if(b == 10) {
						state = State.LF;
						appendChar = false;

						if(!firstLine) {
							imb.addLine(line);
						} else {
							firstLine = false;
						}
						line = new StringBuffer();
						state = State.TEXT;
//					} else {
//						throw new IllegalStateException("CR without LF, is this allowed according to RFC?!");
					}
				}

				if(appendChar) {
					line.append((char)b);
				}
				b = is.read();
			}

			imb.build();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
