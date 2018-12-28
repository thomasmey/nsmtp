package de.m3y3r.nstmp.command;

import de.m3y3r.nstmp.model.SessionContext;
import de.m3y3r.nstmp.model.SmtpCommandReply;
import de.m3y3r.nstmp.model.SmtpReplyStatus;
import io.netty.channel.ChannelHandlerContext;

public class ExtendedHello implements SmtpCommand {

	@Override
	public CharSequence getCommandWord() {
		return "EHLO";
	}

	@Override
	public SmtpCommandReply processCommand(SessionContext ctxMailSession, ChannelHandlerContext ctxChannel,
			CharSequence argument) {

		ctxMailSession.resetMailTransaction();
		CharSequence domainOrAddressLiteral = argument;
		String greeting = "Greetings from Netty SMTP server";

		SmtpCommandReply reply = new SmtpCommandReply(SmtpReplyStatus.R250, domainOrAddressLiteral + " " + greeting);
		return reply;
	}

}
