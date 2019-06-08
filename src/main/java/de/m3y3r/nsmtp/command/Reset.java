package de.m3y3r.nsmtp.command;

import java.util.List;

import de.m3y3r.nsmtp.model.SessionContext;
import de.m3y3r.nsmtp.model.SmtpCommandReply;
import de.m3y3r.nsmtp.model.SmtpReplyStatus;
import io.netty.channel.ChannelHandlerContext;

public class Reset extends AbstractSmtpCommand {

	@Override
	public CharSequence getCommandVerb() {
		return "RSET";
	}

	@Override
	public SmtpCommandReply processCommand(SessionContext ctxMailSession, ChannelHandlerContext ctxChannel,
			CharSequence argument) {
		ctxMailSession.resetMailTransaction(); // abort any ongoing mail transaction
		SmtpCommandReply reply = new SmtpCommandReply(SmtpReplyStatus.R250, "OK");
		return reply;
	}
}
