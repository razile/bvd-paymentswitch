package com.bvd.paymentswitch.jpa.service;


import org.springframework.data.repository.CrudRepository;
import com.bvd.paymentswitch.web.service.models.fuelcard.CompletedAuthorization;

public interface CompletedAuthorizationRepository extends CrudRepository<CompletedAuthorization, String> {
	
	
	
}
