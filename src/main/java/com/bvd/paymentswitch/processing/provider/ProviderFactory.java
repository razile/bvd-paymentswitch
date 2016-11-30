package com.bvd.paymentswitch.processing.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bvd.paymentswitch.jpa.service.AuthorizationService;
import com.bvd.paymentswitch.models.BinPaymentProcessor;
import com.bvd.paymentswitch.models.PaymentProcessor;

@Component
public class ProviderFactory {
	
	static final Logger logger = LoggerFactory.getLogger(ProviderFactory.class);
	private Map<String, ProcessingProvider> providers;
	
	@Autowired
	AuthorizationService authService;

	public ProviderFactory() {}
	
	
	public void loadProviders() {
		Iterable<PaymentProcessor> processors = authService.getAllPaymentProcessors();
		providers = new HashMap<String,ProcessingProvider>();
    	for(PaymentProcessor p : processors) {
			try {
				Class<?> clazz = Class.forName(p.getProviderClass());
				ProcessingProvider provider = (ProcessingProvider) clazz.newInstance();
				provider.initialize(p, authService);
				List<BinPaymentProcessor> bins = p.getBins();
				
				if (bins != null) {
					for(BinPaymentProcessor bp:bins) {
						providers.put(bp.getBin(), provider);
					}
				}
			} catch (Exception e) {
				logger.error("Unable to create provider: " + p.getProviderClass());
			}
		}
	}
	
	
	public ProcessingProvider getProvider(String bin) {
		if (providers != null) {
			return providers.get(bin);
		} else {
			return null;
		}
	}
}
