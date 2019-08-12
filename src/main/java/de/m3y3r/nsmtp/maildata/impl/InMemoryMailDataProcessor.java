package de.m3y3r.nsmtp.maildata.impl;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.m3y3r.nsmtp.maildata.MailDataProcessor;
import de.m3y3r.nsmtp.model.imf.InternetMessageBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class InMemoryMailDataProcessor implements MailDataProcessor {

	private List<ByteBuf> mailData;

	public InMemoryMailDataProcessor() {
		this.mailData = new ArrayList<>();
	}

	@Override
	public void addDataLine(ByteBuf lineWithoutCRLF) {
		mailData.add(lineWithoutCRLF);
	}

	@Override
	public boolean finish() {
//		ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
//		InternetMessageBuilder builder = new InternetMessageBuilder();
//		for(ByteBuf line : mailData) {
//			byteBuf.clear();
//			byteBuf.writeBytes(line);
//			builder.addLine(byteBuf);
//		}
//		builder.build();
		return true;
	}

	public List<ByteBuf> getMailData() {
		return Collections.unmodifiableList(mailData);
	}
}
