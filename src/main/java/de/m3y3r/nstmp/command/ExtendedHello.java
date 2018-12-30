package de.m3y3r.nstmp.command;

import java.util.ArrayList;
import java.util.List;

import de.m3y3r.nstmp.model.SessionContext;
import de.m3y3r.nstmp.model.SmtpCommandReply;
import de.m3y3r.nstmp.model.SmtpReplyStatus;
import io.netty.channel.ChannelHandlerContext;

public class ExtendedHello extends AbstractSmtpCommand {

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
		for(SmtpCommand cmd: SmtpRegistry.INSTANCE.getCommands()) {
			if(cmd.getHelloKeyword() != null) {
				String line = cmd.getHelloKeyword().toString();
				for(CharSequence param: cmd.getHelloParams(ctxMailSession)) {
					line += " " + param;
				}
				lines.add(line);
			}
		}
		SmtpCommandReply reply = new SmtpCommandReply(SmtpReplyStatus.R250, lines);
		return reply;
	}

}
