package de.m3y3r.nstmp.handler.codec.smtp;

import java.nio.charset.StandardCharsets;

import de.m3y3r.nstmp.model.SessionContext;
import de.m3y3r.nstmp.model.SmtpCommandReply;
import de.m3y3r.nstmp.model.SmtpReplyStatus;
import de.m3y3r.nstmp.util.CharSequenceComparator;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

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
		SessionContext sessionContext = ctx.channel().attr(SessionContext.ATTRIBUTE_KEY).get();

		CharSequence line = frame.readCharSequence(frame.readableBytes(), StandardCharsets.US_ASCII);

		CharSequence transformedLine = transformLine(line);
		if(CharSequenceComparator.equals(".", transformedLine)) {
			ctx.pipeline().replace(this, "smptInCommand", new SmtpCommandHandler());

			boolean rc = sessionContext.mailTransaction.mailFinished();
			//FIXME: reset mailtransaction?!
			sessionContext.mailTransaction = null;
			if(rc) {
				Object reply = new SmtpCommandReply(SmtpReplyStatus.R250, "OK");
				ctx.writeAndFlush(reply);
			} else {
				Object reply = new SmtpCommandReply(SmtpReplyStatus.R450, "FAILED");
				ctx.writeAndFlush(reply);
			}
		} else {
			sessionContext.mailTransaction.addDataLine(transformedLine);
		}
	}

	// TODO: implement "4.5.2 Transparency"
	private CharSequence transformLine(CharSequence line) {
		if(line.charAt(0) == '.') {
			
		}
		return line;
	}
}
