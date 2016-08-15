package com.bvd.server;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.bvd.authorization.client.AuthorizationInitializer;
import com.bvd.models.KardallHostAuthorization;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


public class PaymentSwitchHandler extends SimpleChannelInboundHandler<String> {

	private volatile Channel outboundChannel;

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, String message) {
		// parse the request
		System.err.println(message);
		KardallHostAuthorization request = new KardallHostAuthorization(message);

		// persist the request
		String response = null;
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("bvd-paymentswitch");
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(request);
		em.getTransaction().commit();
		
		// TODO: figure out payment processor from request

		// TODO: look up connection details for payment processor
		String remoteHost = "127.0.0.1";
		int remotePort = 10080;

		// wire up selected processor

		final Channel inboundChannel = ctx.channel();

		// Start the connection attempt.
		Bootstrap b = new Bootstrap();
		
		b.group(inboundChannel.eventLoop()).channel(ctx.channel().getClass())
			.handler(new AuthorizationInitializer(null, inboundChannel));
				
		//.handler(new PaymentSwitchAuthorizationHandler(inboundChannel)).option(ChannelOption.AUTO_READ, false);
	
		
		ChannelFuture f = b.connect(remoteHost, remotePort);
		outboundChannel = f.channel();
		f.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) {
				if (future.isSuccess()) {
					// connection complete start to read first data
					inboundChannel.read();
				} else {
					// Close the connection if the connection attempt has
					// failed.
					inboundChannel.close();
				}
			}
		});

		if (outboundChannel.isActive()) {
			outboundChannel.writeAndFlush(request.toString()).addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) {
					if (future.isSuccess()) {
						// was able to flush out data, start to read the next
						// chunk
						ctx.channel().read();
					} else {
						future.channel().close();
					}
				}
			});
		}

		// generate response (ie. business logic here)
		// String response = "KARDALLC1.0„ÿELMVALE 20160728114738AXj885DCJ
		// 57768J4371LW3M875<C1606017000000027365/><N11017/><PN05/><F1300/><H11/><TD4264.9/><TV5390.58/><Q172.04/><A157.56/><PR0.7990/><O1571802/>V";
		// String response = pojo.toString();
		// write the response
		// ctx.write(response);

		// close the channel once the content is fully written
		// ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);

	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		if (outboundChannel != null) {
			closeOnFlush(outboundChannel);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		closeOnFlush(ctx.channel());
	}

	/**
	 * Closes the specified channel after all queued write requests are flushed.
	 */
	public static void closeOnFlush(Channel ch) {
		if (ch.isActive()) {
			ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
		}
	}

}
