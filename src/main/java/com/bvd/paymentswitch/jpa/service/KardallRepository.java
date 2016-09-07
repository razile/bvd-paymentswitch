package com.bvd.paymentswitch.jpa.service;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bvd.paymentswitch.models.KardallHostAuthorization;

public interface KardallRepository extends CrudRepository<KardallHostAuthorization, Long> {
	
	List<KardallHostAuthorization> findBySiteId(String siteId);
	
	List<KardallHostAuthorization> findByAuthId(String authId);
	
	List<KardallHostAuthorization> findByDateTime(String dateTime);
}
