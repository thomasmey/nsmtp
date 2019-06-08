package de.m3y3r.nsmtp.command;

import java.util.List;

import de.m3y3r.nsmtp.handler.codec.smtp.SmtpDataHandler;
import de.m3y3r.nsmtp.model.SessionContext;
import de.m3y3r.nsmtp.model.SmtpCommandReply;
import de.m3y3r.nsmtp.model.SmtpReplyStatus;
import io.netty.channel.ChannelHandlerContext;

public class Data extends AbstractSmtpCommand {

	@Override
	public CharSequence getCommandVerb() {
		return "DATA";
	}

	@Override
	public SmtpCommandReply processCommand(SessionContext ctxMailSession, ChannelHandlerContext ctxChannel,
			CharSequence argument) {
		SmtpCommandReply reply = new SmtpCommandReply(SmtpReplyStatus.R354, "Start mail input; end with <CRLF>.<CRLF>");

		// switch handlers
		ctxChannel.pipeline().replace("smptInCommand", "smptInData", new SmtpDataHandler());

		return reply;
	}
}
