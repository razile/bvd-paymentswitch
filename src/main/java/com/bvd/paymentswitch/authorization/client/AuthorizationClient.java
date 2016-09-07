package com.bvd.paymentswitch.authorization.client;

import javax.inject.Provider;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.bvd.paymentswitch.models.KardallHostAuthorization;
import com.bvd.paymentswitch.models.PaymentProcessor;
import com.bvd.paymentswitch.models.PriorPostAuthorization;

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

	public void authorize(KardallHostAuthorization request, PaymentProcessor p, ChannelHandlerContext bvdCtx) {

		 EventLoopGroup group = new NioEventLoopGroup(1);
	        try {
	            Bootstrap b = new Bootstrap();
	            b.group(group)
	             .channel(NioSocketChannel.class)
	             .handler(authorizationInitializer(request, p, bvdCtx));

	    		// TODO: look up connection details for payment processor
	    		
	            // Make the connection attempt.
	            //Channel ch = b.connect(host,port).sync().channel();
	            //Channel ch = b.connect("www.tch.com",8130).sync().channel();
	            Channel ch = b.connect(p.getHost(),p.getPort()).sync().channel();
	            
	            logger.debug("Connected to: " + p.getHost() + ":" + p.getPort());
	            // Create the request
	       
	            PriorPostAuthorization auth = new PriorPostAuthorization(request);
	            auth.setLanguage(p.getLanguage());
	            auth.setSoftwareSystem(p.getSoftwareSystem());
	            auth.setUnitofMeasure(p.getUnitOfMeasure());
	            // Send the  request
	            String authreq = auth.toString();
	            // String authreq = "0097|IC|055431|01.02|1|SSVR:ALPH|CCLM:100,100|CUST:I|CARD:14712321231,S|EXPD:1197|INVN:123A1234";
	            logger.debug("SEND: " + authreq);	            	            

	            ch.writeAndFlush(authreq);

	            // Wait for the server to close the connection.
	            ch.closeFuture().sync();

	        } catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage());
			} finally {
	            // The connection is closed automatically on shutdown.
	            group.shutdownGracefully();
	        }
		
	}

	public AuthorizationInitializer authorizationInitializer(KardallHostAuthorization request, PaymentProcessor p, ChannelHandlerContext bvdCtx) {
		AuthorizationInitializer authInit = authInitProvider.get();
		authInit.setBvdCtx(bvdCtx);
		authInit.setRequest(request);
		authInit.setPaymentProcessor(p);
		return authInit;
	}
	
}
