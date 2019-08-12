package de.m3y3r.nsmtp.model.imf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Ignore;
import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

@Ignore
public class ImfBuilderTest {

	private enum State {TEXT, CR, LF};

	@Test
	public void testImf() {
		InternetMessageBuilder imb = new InternetMessageBuilder();
		File rawMail = new File("/home/thomas/db-mail.dump.txt");
		State state = State.TEXT;
		boolean firstLine = true;
		ByteBuf bb = ByteBufAllocator.DEFAULT.buffer();
		try(InputStream is = new FileInputStream(rawMail)) {
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
							imb.addLine(bb);
						} else {
							firstLine = false;
						}
						bb.clear();
						state = State.TEXT;
//					} else {
//						throw new IllegalStateException("CR without LF, is this allowed according to RFC?!");
					}
				}

				if(appendChar) {
					bb.writeByte(b);
				}
				b = is.read();
			}

			imb.build();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
