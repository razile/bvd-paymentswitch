package com.bvd.paymentswitch.jpa.service;


import org.springframework.data.repository.CrudRepository;
import com.bvd.paymentswitch.models.FuelCode;


public interface FuelCodeRepository extends CrudRepository<FuelCode, String> {
	
	FuelCode findByEfsCode(int efsCode);
	
	FuelCode findByComdataCode(String comdataCode);
	
}
