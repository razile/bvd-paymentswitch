package com.bvd.paymentswitch.processing.client;

import java.util.concurrent.CompletableFuture;

import javax.inject.Provider;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bvd.paymentswitch.models.PosAuthorization;
import com.bvd.paymentswitch.models.ProcessorAuthorization;
import com.bvd.paymentswitch.models.PaymentProcessor;
import com.bvd.paymentswitch.processing.provider.ProcessingProvider;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.GenericFutureListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//  901-619-3902
// "test.gw.efsllc.com",8983
@Component
@Qualifier("authorizationClient")
@Scope("prototype")
public class AuthorizationClient {
	
	static final Logger logger = LoggerFactory.getLogger(AuthorizationClient.class);
    
	@Autowired
	private Provider<AuthorizationInitializer> authInitProvider;
	
	private ChannelFuture channelFuture = null;
	private EventLoopGroup group = null;


	public void connect(PosAuthorization posRequest, ProcessorAuthorization processorRequest, ProcessingProvider provider) throws Exception {

		group = new NioEventLoopGroup(1);

		PaymentProcessor p = provider.getPaymentProcessor();
		Bootstrap b = new Bootstrap();
		b.group(group).channel(NioSocketChannel.class).handler(authorizationInitializer(posRequest, processorRequest, provider));

		channelFuture = b.connect(p.getHost(), p.getPort());
		//channelFuture = b.connect("localhost", 10090);

		logger.debug("Connected to: " + p.getHost() + ":" + p.getPort());

	}


	public void close() {
		if (channelFuture != null) channelFuture.channel().close();	
		if (group != null) group.shutdownGracefully();
	}

	
	public CompletableFuture<String> authorize(ProcessorAuthorization processorRequest, ProcessingProvider provider) throws Exception {

		if (channelFuture.channel().isWritable()) {
			String request = provider.formatProcessorRequest(processorRequest);
			//final AuthorizationFuture authorizationFuture = new AuthorizationFuture();
			final CompletableFuture<String> authorizationFuture = new CompletableFuture<String>();
			
			channelFuture.addListener(new GenericFutureListener<ChannelFuture>() {
				@Override
				public void operationComplete(ChannelFuture arg0) throws Exception {
					channelFuture.channel().pipeline().get(AuthorizationHandler.class).setAuthorizationFuture(authorizationFuture);
					channelFuture.channel().writeAndFlush(request);				
				}
			
			});
			
			return authorizationFuture;
		} else {
			channelFuture.channel().close();
			throw new Exception("Unable to connect to payment processor");
		}
	}

	public AuthorizationInitializer authorizationInitializer(PosAuthorization posRequest, ProcessorAuthorization  processorRequest, ProcessingProvider provider) {
		AuthorizationInitializer authInit = authInitProvider.get();
		authInit.initializePOSContext(posRequest, processorRequest, provider);
		return authInit;
	}
	
}
