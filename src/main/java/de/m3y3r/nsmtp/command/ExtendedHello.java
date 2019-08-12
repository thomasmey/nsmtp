package de.m3y3r.nsmtp.command;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.m3y3r.nsmtp.model.SessionContext;
import de.m3y3r.nsmtp.model.SmtpCommandReply;
import de.m3y3r.nsmtp.model.SmtpReplyStatus;
import io.netty.channel.ChannelHandlerContext;

public class ExtendedHello extends AbstractSmtpCommand {

	private static final Logger logger = LoggerFactory.getLogger(ExtendedHello.class);

	@Override
	public CharSequence getCommandVerb() {
		return "EHLO";
	}

	@Override
	public SmtpCommandReply processCommand(SessionContext ctxMailSession, ChannelHandlerContext ctxChannel,
			CharSequence argument) {

		ctxMailSession.resetMailTransaction();
		CharSequence domainOrAddressLiteral = argument;
		String greeting = "Greetings from Netty SMTP server";

		List<String> lines = new ArrayList<String>();
		lines.add(domainOrAddressLiteral + " " + greeting);
		for(SmtpCommand cmd: SmtpRegistry.INSTANCE.getHelloKeywords(ctxMailSession)) {
			CharSequence helloKeyword = cmd.getHelloKeyword(ctxMailSession);
			if(helloKeyword != null) {
				String line = helloKeyword.toString();
				if(cmd.getHelloParams(ctxMailSession) != null) {
					for(CharSequence param: cmd.getHelloParams(ctxMailSession)) {
						line += " " + param;
					}
				}
				lines.add(line);
			}
		}
		logger.info("using lines: {}", lines);
		SmtpCommandReply reply = new SmtpCommandReply(SmtpReplyStatus.R250, lines);
		return reply;
	}

}
