package com.bvd.paymentswitch.processing.client;



import javax.inject.Provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bvd.paymentswitch.models.PosAuthorization;
import com.bvd.paymentswitch.processing.provider.ProcessingProvider;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

import io.netty.handler.ssl.SslContext;


@Component
@Qualifier("authorizationInitializer")
@Scope("prototype")
public class AuthorizationInitializer extends ChannelInitializer<SocketChannel> {

    private PosAuthorization posRequest;
    private ProcessingProvider provider;
    
	@Autowired
	private Provider<AuthorizationHandler> authHandlerProvider;
    
	@Autowired
	@Qualifier("sslClientContext")
	private SslContext sslCtx;
	
        
    public void intializeContext(PosAuthorization request, ProcessingProvider provider) {
    	this.posRequest = request;
    	this.provider = provider;
    }
	
   

	@Override
    public void initChannel(SocketChannel ch) throws Exception {
    	ChannelPipeline p = ch.pipeline();
     
    	if (provider.getPaymentProcessor().isSslRequired()) {
	    	if (sslCtx != null) {
	    		p.addLast(sslCtx.newHandler(ch.alloc()));
	    	}
    	}
    	// the first decoder will look for the <ETX> end of text ASCII char.
		// This will produce a substring starting with <STX>
		p.addLast(new DelimiterBasedFrameDecoder(32768, provider.getFrameDelimiter()));

		// The STX/ETX codecs will ensure the message is wrapped/stripped of
		// <STX> start and <ETX> end chars
		p.addLast(provider.getProtocolDecoder());
		p.addLast(provider.getProtocolEncoder());
		
      
        // add the handler class containing processing business logic
        p.addLast(authorizationHandler(posRequest, provider));
    }
	
    
	
	public AuthorizationHandler authorizationHandler(PosAuthorization posRequest, ProcessingProvider provider) {
		AuthorizationHandler handler = authHandlerProvider.get();
		handler.initializePOSContext(posRequest, provider);
		return handler;
	}
	
}
