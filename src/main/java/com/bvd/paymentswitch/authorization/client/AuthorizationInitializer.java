package com.bvd.paymentswitch.authorization.client;


import javax.inject.Provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bvd.paymentswitch.models.KardallHostAuthorization;
import com.bvd.paymentswitch.models.PaymentProcessor;
import com.bvd.paymentswitch.protocol.STX_ETX_Encoder;
import com.bvd.paymentswitch.utils.ASCIIChars;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;


@Component
@Qualifier("authorizationInitializer")
@Scope("prototype")
public class AuthorizationInitializer extends ChannelInitializer<SocketChannel> {

    private ChannelHandlerContext bvdCtx;
    private KardallHostAuthorization request;
    private PaymentProcessor paymentProcessor;
    
	@Autowired
	private Provider<AuthorizationHandler> authHandlerProvider;

	@Autowired
	@Qualifier("decoder")
	private StringDecoder DECODER;
	
	@Autowired
	@Qualifier("sslClientContext")
	private SslContext sslCtx;
	
        
    
    public ChannelHandlerContext getBvdCtx() {
		return bvdCtx;
	}

	public void setBvdCtx(ChannelHandlerContext bvdCtx) {
		this.bvdCtx = bvdCtx;
	}
	

	public KardallHostAuthorization getRequest() {
		return request;
	}

	public void setRequest(KardallHostAuthorization request) {
		this.request = request;
	}
	
	public PaymentProcessor getPaymentProcessor() {
		return paymentProcessor;
	}

	public void setPaymentProcessor(PaymentProcessor paymentProcessor) {
		this.paymentProcessor = paymentProcessor;
	}

	@Override
    public void initChannel(SocketChannel ch) throws Exception {
    	ChannelPipeline p = ch.pipeline();
     
    	if (paymentProcessor.isSslRequired()) {
	    	if (sslCtx != null) {
	    		p.addLast(sslCtx.newHandler(ch.alloc()));
	    	}
    	}
    	// the first decoder will look for the <ETX> end of text ASCII char.
		// This will produce a substring starting with <STX>
		p.addLast(new DelimiterBasedFrameDecoder(32768, ASCIIChars.ETX_DELIMITER));

		// The STX/ETX codecs will ensure the message is wrapped/stripped of
		// <STX> start and <ETX> end chars
		p.addLast(DECODER);
		p.addLast(encoder());
		
      
        // add the handler class containing processing business logic
        p.addLast(authorizationHandler());
    }
	
	
	public AuthorizationHandler authorizationHandler() {
		AuthorizationHandler authHandler = authHandlerProvider.get();
		authHandler.setBvdCtx(bvdCtx);
		authHandler.setRequest(request);
		authHandler.setPaymentProcessor(paymentProcessor);
		return authHandler;
	}
	
	public StringEncoder encoder() {
	   	try {
			Class<?> encoder = Class.forName(paymentProcessor.getEncoder());
			return (StringEncoder) encoder.newInstance();
		} catch (Exception e) {
			return new STX_ETX_Encoder();
		} 
	}
    
}
