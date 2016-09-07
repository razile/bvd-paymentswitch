package com.bvd.paymentswitch.jpa.service;

import com.bvd.paymentswitch.models.BinRange;
import com.bvd.paymentswitch.models.KardallHostAuthorization;
import com.bvd.paymentswitch.models.PaymentProcessor;
import com.bvd.paymentswitch.models.PriorPostAuthorization;

public interface AuthorizationService {
	
	KardallHostAuthorization saveAuthorization(KardallHostAuthorization k);
	
	PriorPostAuthorization saveAuthorization(PriorPostAuthorization p);
	
	PaymentProcessor savePaymentProcessor(PaymentProcessor p);
	
	BinRange saveBinRange(BinRange b);
	
	PaymentProcessor findPaymentProcessor(int bin);

}
