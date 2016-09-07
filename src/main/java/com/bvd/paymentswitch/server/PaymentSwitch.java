package com.bvd.paymentswitch.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.bvd.paymentswitch.jpa.service.AuthorizationService;
import com.bvd.paymentswitch.models.BinRange;
import com.bvd.paymentswitch.models.PaymentProcessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

@Component
public class PaymentSwitch {

	    static final Logger logger = LoggerFactory.getLogger(PaymentSwitch.class);
	  	@Autowired
	    @Qualifier("serverBootstrap")
	    private ServerBootstrap serverBootstrap;

	    @Autowired
	    @Qualifier("tcpSocketAddress")
	    private InetSocketAddress tcpPort;

	    private Channel serverChannel;
	    
		@Autowired
		private AuthorizationService authService;

	    public void start() throws Exception {
	    	logger.info("Payment Switch listening on port " + tcpPort.getPort() );
	        serverChannel =  serverBootstrap.bind(tcpPort).sync().channel().closeFuture().sync().channel();
	    }

	    @PreDestroy
	    public void stop() throws Exception {
	    	if (serverChannel != null) {
		        serverChannel.close();
		        serverChannel.parent().close();
	    	}
	    }

	    public ServerBootstrap getServerBootstrap() {
	        return serverBootstrap;
	    }

	    public void setServerBootstrap(ServerBootstrap serverBootstrap) {
	        this.serverBootstrap = serverBootstrap;
	    }

	    public InetSocketAddress getTcpPort() {
	        return tcpPort;
	    }

	    public void setTcpPort(InetSocketAddress tcpPort) {
	        this.tcpPort = tcpPort;
	    }
	    
	    
	    public void seedData() {
			
			// EFS
			// String remoteHost = "www.tch.com";
			// int remotePort = 8130;
			
			//TCHEK
			//Test:
			//IP:  198.137.246.120
			//Port:  6080
			 
		    //PROD:
			//IP:  198.137.246.119
			//Port:  6080
	    	try {
	    		PaymentProcessor  p = new PaymentProcessor();
	    		p.setName("EFS");
	    		p.setHost("test.gw.efsllc.com");
	    		p.setPort(8983);
	    		p.setEncoder("com.bvd.paymentswitch.protocol.EFSEncoder");
	    		p.setSoftwareSystem("FJPOS");
	    		p.setUnitOfMeasure("GAL");
	    		p.setClientDisconnect(true);
	    		p.setSslRequired(true);
	    		p = authService.savePaymentProcessor(p);
	    		
	    		PaymentProcessor y = new PaymentProcessor();
	    		y.setName("TCHECK");
	    		y.setHost("198.137.246.120");
	    		y.setPort(6080);
	    		y.setEncoder("com.bvd.paymentswitch.protocol.STX_ETX_Encoder");
	    		y.setSoftwareSystem("FJPOS");
	    		y.setUnitOfMeasure("1");
	    		y = authService.savePaymentProcessor(y);
	    		
	    		BinRange b1 = new BinRange();
	    		b1.setStartRange(100000);
	    		b1.setEndRange(199999);
	    		b1.setPaymentProcessor(p);
	    		authService.saveBinRange(b1);
	    		
	    		BinRange b2 = new BinRange();
	    		b2.setStartRange(200000);
	    		b2.setEndRange(299999);
	    		b2.setPaymentProcessor(y);
	    		authService.saveBinRange(b2);
	    		
	    	} catch (Exception e) {
	    		logger.error(e.getMessage());
	    	}
	    }
	
}
