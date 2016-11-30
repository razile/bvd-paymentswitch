package com.bvd.paymentswitch.jpa.service;

import org.springframework.data.repository.CrudRepository;
import com.bvd.paymentswitch.models.BinPaymentProcessor;

public interface BinPaymentProcessorRepository extends CrudRepository<BinPaymentProcessor, Integer> {

}
