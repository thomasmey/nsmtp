package de.m3y3r.nsmtp.command;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.m3y3r.nsmtp.Config;
import de.m3y3r.nsmtp.model.SessionContext;
import de.m3y3r.nsmtp.model.SmtpCommandReply;
import de.m3y3r.nsmtp.model.SmtpReplyStatus;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;

/**
 * STARTTLS extension
 * https://tools.ietf.org/html/rfc3207
 * @author thomas
 *
 */
public class StartTLS extends AbstractSmtpCommand {

	private static final Logger logger = LoggerFactory.getLogger(StartTLS.class);

	@Override
	public CharSequence getHelloKeyword(SessionContext ctx) {
		if(!ctx.tlsActive)
			return "STARTTLS";
		return null;
	}

	@Override
	public CharSequence getCommandVerb() {
		return "STARTTLS";
	}

	@Override
	public SmtpCommandReply processCommand(SessionContext ctxMailSession, ChannelHandlerContext ctxChannel,
			CharSequence argument) {

		if(argument != null)
			return new SmtpCommandReply(SmtpReplyStatus.R501, "Syntax error (no parameters allowed)");

		try {
			File key = new File(Config.INSTANCE.getTlsKeyFile());
			String keyPassword = Config.INSTANCE.getTlsKeyPassword();
			File trustStore = new File(Config.INSTANCE.getTlsTrustStoreFile());
			SslContext sslCtx = SslContextBuilder.forServer(key, trustStore, keyPassword).build();
			ctxChannel.pipeline().addFirst(new SslHandler(sslCtx.newEngine(ctxChannel.alloc()), true));

			//TODO: drop state
			ctxMailSession.resetMailTransaction();
			ctxMailSession.tlsActive = true; // TODO: wait for handshake to finish, otherwise abort connection
			return new SmtpCommandReply(SmtpReplyStatus.R220, "Ready to start TLS");
		} catch(Exception e) {
			logger.error("Failed to establish TLS!", e);
			return new SmtpCommandReply(SmtpReplyStatus.R454, "TLS not available due to temporary reason");
		}
	} 
}
