package com.bvd.paymentswitch.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.bvd.paymentswitch.jpa.service.AuthorizationService;
import com.bvd.paymentswitch.models.BinPaymentProcessor;
import com.bvd.paymentswitch.models.FuelCode;
import com.bvd.paymentswitch.models.PaymentProcessor;
import com.bvd.paymentswitch.processing.provider.ProviderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.util.ArrayList;

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
		
		@Autowired
		ProviderFactory providerFactory;
		
		@Value("${data.seed:false}")
		private boolean seedData;
		
		@Value("${tchek.host}")
		private String tchekHost;
		
		@Value("${efs.host}")
		private String efsHost;
		
		@Value("${comdata.host}")
		private String comdataHost;

	    public void start() throws Exception {  
	    	
	    	if (seedData) {
	    		seedData();
	    	}
	    	
	    	logger.info("Loading processing providers...");
	    	providerFactory.loadProviders();
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
			
	    	try {
	    		
	    		
	    		PaymentProcessor  efs = new PaymentProcessor();
	    		efs.setName("EFS");
	    		String[] eparams = efsHost.split(":");
	    		efs.setHost(eparams[0]);
	    		efs.setPort(Short.parseShort(eparams[1]));
	    		efs.setProviderClass("com.bvd.paymentswitch.processing.provider.EFSProcessingProvider");
	    		efs.setSoftwareSystem("FJPOS");
	    		efs.setUnitOfMeasure("GAL");
	    		efs.setClientDisconnect(true);
	    		efs.setSslRequired(true);
	    		efs = authService.savePaymentProcessor(efs);
	    		
	    		authService.saveMerchantCode("BVDBRAM55477",efs.getId(),"4551");
	    		authService.saveMerchantCode("BVDCOMB55324",efs.getId(),"6053");
	    		authService.saveMerchantCode("BVDCORN55400",efs.getId(),"7548");
	    		authService.saveMerchantCode("BVDNIAG55664",efs.getId(),"6055");
	    		authService.saveMerchantCode("BVDMEDU33119",efs.getId(),"6052");
	    		authService.saveMerchantCode("BVDCALG88210",efs.getId(),"0012");
	    		authService.saveMerchantCode("BVDMILT55981",efs.getId(),"6025");
	    		authService.saveMerchantCode("BVDMISS55976",efs.getId(),"5450");
	    		authService.saveMerchantCode("BVDSARN55089",efs.getId(),"7429");
	    		authService.saveMerchantCode("BVDNORT55816",efs.getId(),"6051");
	    		authService.saveMerchantCode("BVDNIPI55530",efs.getId(),"7100");
	    		authService.saveMerchantCode("BVDWINN86790",efs.getId(),"6026");
	    		authService.saveMerchantCode("T2222A", efs.getId(), "T2222A");

	    		
	    		PaymentProcessor tchek = new PaymentProcessor();
	    		tchek.setName("T-CHEK");
	    		String[] tparams = tchekHost.split(":");
	    		tchek.setHost(tparams[0]);
	    		tchek.setPort(Short.parseShort(tparams[1]));
	    		tchek.setProviderClass("com.bvd.paymentswitch.processing.provider.TCheckProcessingProvider");
	    		tchek.setSoftwareSystem("FJPOS");
	    		tchek.setUnitOfMeasure("1");
	    		tchek = authService.savePaymentProcessor(tchek);
	    		
	    		authService.saveMerchantCode("BVDBRAM55477",tchek.getId(),"58055");
	    		authService.saveMerchantCode("BVDCOMB55324",tchek.getId(),"58156");
	    		authService.saveMerchantCode("BVDCORN55400",tchek.getId(),"58037");
	    		authService.saveMerchantCode("BVDNIAG55664",tchek.getId(),"58073");
	    		authService.saveMerchantCode("BVDMEDU33119",tchek.getId(),"61062");
	    		authService.saveMerchantCode("BVDCALG88210",tchek.getId(),"53035");
	    		authService.saveMerchantCode("BVDMILT55981",tchek.getId(),"58088");
	    		authService.saveMerchantCode("BVDMISS55976",tchek.getId(),"58062");
	    		authService.saveMerchantCode("BVDSARN55089",tchek.getId(),"58057");
	    		authService.saveMerchantCode("BVDNORT55816",tchek.getId(),"58011");
	    		authService.saveMerchantCode("BVDNIPI55530",tchek.getId(),"58078");
	    		authService.saveMerchantCode("BVDWINN86790",tchek.getId(),"55012");
	    		
	    		
	    		PaymentProcessor comdata = new PaymentProcessor();
	    		comdata.setName("COMDATA");
	    		String[] cparams = comdataHost.split(":");
	    		comdata.setHost(cparams[0]);
	    		comdata.setPort(Short.parseShort(cparams[1]));
	    		comdata.setSoftwareSystem("TCPI");
	    		comdata.setProviderClass("com.bvd.paymentswitch.processing.provider.ComdataProcessingProvider");
	    		comdata = authService.savePaymentProcessor(comdata);
	    		
	    		authService.saveMerchantCode("BVDBRAM55477",comdata.getId(),"ON301");
	    		authService.saveMerchantCode("BVDCOMB55324",comdata.getId(),"ON302");
	    		authService.saveMerchantCode("BVDCORN55400",comdata.getId(),"ON303");
	    		authService.saveMerchantCode("BVDNIAG55664",comdata.getId(),"ON306");
	    		authService.saveMerchantCode("BVDMEDU33119",comdata.getId(),"NB301");
	    		authService.saveMerchantCode("BVDCALG88210",comdata.getId(),"AB301");
	    		authService.saveMerchantCode("BVDMILT55981",comdata.getId(),"ON304");
	    		authService.saveMerchantCode("BVDMISS55976",comdata.getId(),"ON305");
	    		authService.saveMerchantCode("BVDSARN55089",comdata.getId(),"ON309");
	    		authService.saveMerchantCode("BVDNORT55816",comdata.getId(),"ON308");
	    		authService.saveMerchantCode("BVDNIPI55530",comdata.getId(),"ON307");
	    		authService.saveMerchantCode("BVDWINN86790",comdata.getId(),"MB301");

	    		
	    		BinPaymentProcessor e1 = new BinPaymentProcessor();
	    		e1.setBin("199999");
	    		e1.setPaymentProcessor(efs);
	    		authService.saveBin(e1);
	    		
	    		BinPaymentProcessor e2 = new BinPaymentProcessor();
	    		e2.setBin("589355");
	    		e2.setPaymentProcessor(efs);
	    		authService.saveBin(e2);
	    		
	    		BinPaymentProcessor e3 = new BinPaymentProcessor();
	    		e3.setBin("708305");
	    		e3.setPaymentProcessor(efs);
	    		authService.saveBin(e3);
	    		
	    		
	    		BinPaymentProcessor tc1 = new BinPaymentProcessor();
	    		tc1.setBin("600296");
	    		tc1.setPaymentProcessor(tchek);
	    		authService.saveBin(tc1);
	    		
	    		BinPaymentProcessor cd1 = new BinPaymentProcessor();
	    		cd1.setBin("162020");
	    		cd1.setPaymentProcessor(comdata);
	    		authService.saveBin(cd1);
	    		
	    		BinPaymentProcessor cd2 = new BinPaymentProcessor();
	    		cd2.setBin("556735");
	    		cd2.setPaymentProcessor(comdata);
	    		authService.saveBin(cd2);
	    		
	    		BinPaymentProcessor cd3 = new BinPaymentProcessor();
	    		cd3.setBin("560017");
	    		cd3.setPaymentProcessor(comdata);
	    		authService.saveBin(cd3);
	    		
	    		BinPaymentProcessor cd4 = new BinPaymentProcessor();
	    		cd4.setBin("161953");
	    		cd4.setPaymentProcessor(comdata);
	    		authService.saveBin(cd4);
	    		
	    		
	    		ArrayList<FuelCode> fuelCodes = new ArrayList<FuelCode>();
	    		fuelCodes.add(new FuelCode("010",1,"019","Diesel #1",1));  //
	    		fuelCodes.add(new FuelCode("001",2,"020","Diesel #2",2));
	    		fuelCodes.add(new FuelCode("051",4,"001","Unleaded Regular",0));
	    		fuelCodes.add(new FuelCode("053",8,"002","Unleaded Plus",0));
	    		fuelCodes.add(new FuelCode("055",16,"003","Unleaded Premium",0));
	    		fuelCodes.add(new FuelCode("080",32,"023","Propane",0));
	    		fuelCodes.add(new FuelCode("048",64,"305","Kerosene",0));
	    		fuelCodes.add(new FuelCode("091",128,"059","Dyed/Marked Diesel",3));
	    		fuelCodes.add(new FuelCode("256",256,"825","Bio Diesel",2));
	    		fuelCodes.add(new FuelCode("100",512,"033","Car Diesel",3));
	    		fuelCodes.add(new FuelCode("086",1024,"399","AG Fuel",0));
	    		fuelCodes.add(new FuelCode("085",388,"012","Gasohol",0));
	    		fuelCodes.add(new FuelCode("101",4096,"022","Natural Gas",0));
	    		fuelCodes.add(new FuelCode("200",8192,"053","Ultra Low Sulfur Diesel",2));
	    		fuelCodes.add(new FuelCode("201",16384,"076","Ultra Low Sulfur Diesel #1",1));  //
	    		fuelCodes.add(new FuelCode("102",32768,"034","Ultra Low Sulfur Car Diesel",3));
	    		fuelCodes.add(new FuelCode("095",65536,"399","Cardlock Gasoline",0));
	    		fuelCodes.add(new FuelCode("096",131072,"157","Dyed/Marked Unleaded",0));
	    		fuelCodes.add(new FuelCode("097",262144,"158","Dyed/Marked Plus",0));
	    		fuelCodes.add(new FuelCode("098",524288,"159","Dyed/Marked Premium",0));
	    		fuelCodes.add(new FuelCode("103",1048576,"036","Racing Fuel",0));
	    		fuelCodes.add(new FuelCode("104",2097152,"302","Furnace/Heating Oil",0));
	    		fuelCodes.add(new FuelCode("099",4194304,"819","UREA/DEF",0));
	    		fuelCodes.add(new FuelCode("082",8388608,"022","CNG (Compressed Natural Gas)",0));
	    		fuelCodes.add(new FuelCode("105",16777216,"024","LNG (Liquid Natural Gas)",0));
	    		fuelCodes.add(new FuelCode("106",33554432,"311","LCNG",0));
	    		
	    		authService.saveFuelCodes(fuelCodes);

	    		
	    	} catch (Exception e) {
	    		logger.error(e.getMessage());
	    	}
	    }
	
}
