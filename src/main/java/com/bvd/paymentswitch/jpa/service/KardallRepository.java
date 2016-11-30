package com.bvd.paymentswitch.jpa.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bvd.paymentswitch.models.PosAuthorization;

public interface KardallRepository extends CrudRepository<PosAuthorization, Long> {
	
	List<PosAuthorization> findBySiteId(String siteId);
	
	List<PosAuthorization> findByAuthId(String authId);
	
	List<PosAuthorization> findByDateTime(Timestamp dateTime);
}
