package com.bvd.paymentswitch.processing.client;

import javax.inject.Provider;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.bvd.paymentswitch.models.PosAuthorization;
import com.bvd.paymentswitch.models.ProcessorAuthorization;
import com.bvd.paymentswitch.models.PaymentProcessor;
import com.bvd.paymentswitch.processing.provider.ProcessingProvider;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//  901-619-3902
// "test.gw.efsllc.com",8983
@Component
@Qualifier("authorizationClient")
public class AuthorizationClient {
	
	static final Logger logger = LoggerFactory.getLogger(AuthorizationClient.class);
    
	@Autowired
	private Provider<AuthorizationInitializer> authInitProvider;

	public void authorize(PosAuthorization posRequest, ProcessingProvider provider, String merchantCode, ChannelHandlerContext posCtx) throws Exception  {

		 EventLoopGroup group = new NioEventLoopGroup(1);
		 Channel ch = null;
	        try {
	        	PaymentProcessor p = provider.getPaymentProcessor();
	        	ProcessorAuthorization processorRequest = provider.createProcessorRequest(posRequest, merchantCode);
	        	
	        	//provider.saveProcessorAuthorization(processorRequest);
	            
	        	
	        	Bootstrap b = new Bootstrap();
	            b.group(group)
	             .channel(NioSocketChannel.class)
	             .handler(authorizationInitializer(posRequest, provider, posCtx));
	    		
	            // Make the connection attempt.
	            ch = b.connect(p.getHost(),p.getPort()).sync().channel();
	            
	            logger.debug("Connected to: " + p.getHost() + ":" + p.getPort());
	            
	       
	            // write the request
	            String request = provider.formatProcessorRequest(processorRequest);
	            
	            // logger.debug("SEND: " + request);
	            ch.writeAndFlush(request);

	            // Wait for the server to close the connection.
	            ch.closeFuture().sync();

	        } catch (Exception e) {
				// logger.error(e.getMessage());
	        	if (ch != null) ch.close();
				throw e;
			} finally {
	            // The connection is closed automatically on shutdown.
	            group.shutdownGracefully();
	        }
		
	}

	public AuthorizationInitializer authorizationInitializer(PosAuthorization posRequest, ProcessingProvider provider, ChannelHandlerContext posCtx) {
		AuthorizationInitializer authInit = authInitProvider.get();
		authInit.intializeContext(posRequest, provider, posCtx);
		return authInit;
	}
	
}
