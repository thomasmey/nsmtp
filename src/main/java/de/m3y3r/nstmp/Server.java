package de.m3y3r.nstmp;

import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.m3y3r.nstmp.handler.codec.smtp.SessionInitiationHandler;
import de.m3y3r.nstmp.handler.codec.smtp.SmtpCommandHandler;
import de.m3y3r.nstmp.handler.codec.smtp.SmtpReplyEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

/**
 * SMTP delivery system
 *
 * @author thomas
 *
 */
public class Server implements Runnable {

	private int port;
	private static final Logger logger = Logger.getLogger(Server.class.getName());

	public Server(int port) {
		this.port = port;
	}

	public static void main(String... args) {
		int smtpPort = 2525;
		Server s = new Server(smtpPort);
		s.run();
	}

	@Override
	public void run() {
		EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			byte[] CRLF = {'\r', '\n'};
			ByteBuf delimiter = Unpooled.buffer(2).writeBytes(CRLF);

			b.group(eventLoopGroup)
				.channel(NioServerSocketChannel.class)
				.localAddress(new InetSocketAddress(port))
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast("smtpOutReply", new SmtpReplyEncoder());
						ch.pipeline().addLast("smptInSession", new SessionInitiationHandler());
						ch.pipeline().addLast("smtpInLine", new DelimiterBasedFrameDecoder(Config.INSTANCE.getDefaultCommandLen(), true, delimiter));
						ch.pipeline().addLast("smptInCommand", new SmtpCommandHandler());
					}
				});
			ChannelFuture f = b.bind().sync();
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			logger.log(Level.SEVERE, "Int1", e);
		} finally {
			try {
				eventLoopGroup.shutdownGracefully().sync();
			} catch (InterruptedException e) {
				logger.log(Level.SEVERE, "Int2", e);
			}
		}
	}
}
