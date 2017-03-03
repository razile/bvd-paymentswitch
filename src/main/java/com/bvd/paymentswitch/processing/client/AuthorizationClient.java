package com.bvd.paymentswitch.processing.client;

import javax.inject.Provider;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bvd.paymentswitch.models.PosAuthorization;
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


	
	public void connect(PosAuthorization posRequest, ProcessingProvider provider) throws Exception{

		EventLoopGroup group = new NioEventLoopGroup(1);

		try {
			
			PaymentProcessor p = provider.getPaymentProcessor();
			Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(NioSocketChannel.class)
             .handler(authorizationInitializer(posRequest, provider));
    		
            // Make the connection attempt.
            channelFuture = b.connect(p.getHost(),p.getPort());
          //  ch = channelFuture.sync().channel();
            
            logger.debug("Connected to: " + p.getHost() + ":" + p.getPort());
        } catch (Exception e) {
			// logger.error(e.getMessage());
        	if (channelFuture != null) channelFuture.channel().close();
			throw e;
		} finally {
            // The connection is closed automatically on shutdown.
            group.shutdownGracefully();
		}
	}
	


	public void close() {
		channelFuture.channel().close();		
	}

	
	public AuthorizationFuture authorize(String processorRequest) throws Exception {

		final AuthorizationFuture authorizationFuture = new AuthorizationFuture();
		channelFuture.addListener(new GenericFutureListener<ChannelFuture>() {
			@Override
			public void operationComplete(ChannelFuture arg0) throws Exception {
				channelFuture.channel().pipeline().get(AuthorizationHandler.class).setAuthorizationFuture(authorizationFuture);
				channelFuture.channel().writeAndFlush(processorRequest);				
			}
		
		});
		
		return authorizationFuture;
	}

	public AuthorizationInitializer authorizationInitializer(PosAuthorization posRequest, ProcessingProvider provider) {
		AuthorizationInitializer authInit = authInitProvider.get();
		authInit.intializeContext(posRequest, provider);
		return authInit;
	}
	
}
