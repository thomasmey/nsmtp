package de.m3y3r.nstmp.command;

import de.m3y3r.nstmp.model.SessionContext;
import de.m3y3r.nstmp.model.SmtpCommandReply;
import de.m3y3r.nstmp.model.SmtpReplyStatus;
import io.netty.channel.ChannelHandlerContext;

public class Reset implements SmtpCommand {

	@Override
	public CharSequence getCommandWord() {
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
