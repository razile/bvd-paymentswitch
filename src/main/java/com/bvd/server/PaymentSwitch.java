package com.bvd.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

public class PaymentSwitch {

	public static void main(String[] args) throws Exception {
		
		int PORT;
		if (args.length > 0) {
			PORT = Integer.parseInt(args[0]);
		} else {
			PORT = 10085;
		}
		
		boolean SSL = false;
		
		if (args.length > 1) {
			if (args[1].equalsIgnoreCase("ssl")) {
				SSL = true;
			}
		}

		// Configure SSL.
		final SslContext sslCtx;
		if (SSL) {
			SelfSignedCertificate ssc = new SelfSignedCertificate();
			sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
		} else {
			sslCtx = null;
		}
		
		
		// Configure the server.
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new PaymentSwitchInitializer(sslCtx));

			Channel ch = b.bind(PORT).sync().channel();

			System.err.println("BVD Payment Switch is RUNNING on Port: " + PORT + ". SSL ON: " + SSL);

			ch.closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}
