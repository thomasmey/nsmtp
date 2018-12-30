package de.m3y3r.nstmp.command;

import java.util.List;

import de.m3y3r.nstmp.model.SessionContext;
import de.m3y3r.nstmp.model.SmtpCommandReply;
import io.netty.channel.ChannelHandlerContext;

public interface SmtpCommand {

	CharSequence getCommandVerb();
	CharSequence getHelloKeyword();
	List<CharSequence> getHelloParams(SessionContext ctx);
	List<CharSequence> getMailParams(SessionContext ctx);
	List<CharSequence> getRecipentParams(SessionContext ctx);
	int getAdditionalDataLen();
	int getAdditionalCommandLen();
	SmtpCommandReply processCommand(SessionContext ctxMailSession, ChannelHandlerContext ctxChannel, CharSequence argument);
}
