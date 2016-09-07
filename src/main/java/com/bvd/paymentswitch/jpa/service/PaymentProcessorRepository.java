package com.bvd.paymentswitch.jpa.service;

import org.springframework.data.repository.CrudRepository;
import com.bvd.paymentswitch.models.PaymentProcessor;

public interface PaymentProcessorRepository extends CrudRepository<PaymentProcessor, Short> {
	PaymentProcessor findByName(String name);
}
