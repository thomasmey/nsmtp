package de.m3y3r.nstmp.handler.codec.smtp;

import java.nio.charset.StandardCharsets;

import de.m3y3r.nstmp.Config;
import de.m3y3r.nstmp.handler.codec.smtp.model.MailTransaction;
import de.m3y3r.nstmp.handler.codec.smtp.model.SmtpCommandReply;
import de.m3y3r.nstmp.handler.codec.smtp.model.SmtpReplyStatus;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * RFC2821 SMTP server - Handle data lines
 *
 * https://tools.ietf.org/html/rfc2821
 * @author thomas
 *
 */
public class SmtpDataHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf frame = (ByteBuf) msg;
		String line = frame.toString(StandardCharsets.US_ASCII);
		System.out.println(line);

		Attribute<MailTransaction> mailTx = ctx.channel().attr(AttributeKey.valueOf("mailTransaction"));
		MailTransaction mailTxR = mailTx.get();

		// TODO: check for escape sequence and reinstall CommandHandler

		// TODO: implement "4.5.2 Transparency"
		String transformedLine = line;
		mailTxR.addBodyLine(transformedLine);
	}
}
