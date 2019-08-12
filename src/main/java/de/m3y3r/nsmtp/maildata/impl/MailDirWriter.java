package de.m3y3r.nsmtp.maildata.impl;

import java.io.File;
import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.StandardOpenOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.m3y3r.nsmtp.Config;
import de.m3y3r.nsmtp.maildata.MailDataProcessor;
import io.netty.buffer.ByteBuf;

public class MailDirWriter implements MailDataProcessor {

	private static Logger log = LoggerFactory.getLogger(MailDirWriter.class);

	private AsynchronousFileChannel channel;
	private long filepos;

	public MailDirWriter() {
		log.info("going to use mail dir {}", Config.INSTANCE.getMailDir());

		try {
			File mailFile = File.createTempFile("mail-", ".raw", Config.INSTANCE.getMailDir());
			channel = AsynchronousFileChannel.open(mailFile.toPath(), StandardOpenOption.WRITE);
		} catch(IOException e) {
			log.error("Failed to open file", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean finish() {
		try {
			channel.close();
			return true;
		} catch (IOException e) {
			log.error("failed to close file {}", e);
		}
		return false;
	}

	@Override
	public void addDataLine(ByteBuf lineWithoutCRLF) {
		lineWithoutCRLF.writeByte('\n'); // add an extra line feed
		int lineLength = lineWithoutCRLF.readableBytes();

		//TODO: do i need to take a monitor lock here? Is concurrent access possible?
		long pos;
		synchronized (this) {
			pos = this.filepos;
			this.filepos = pos + lineLength;
		}
		channel.write(lineWithoutCRLF.nioBuffer(), pos);
	}

}
