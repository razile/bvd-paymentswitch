package com.bvd.paymentswitch.jpa.service;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.bvd.paymentswitch.models.BinRange;

public interface BinRangeRepository extends CrudRepository<BinRange, Integer> {
	
	@Query("select b from BinRange b where ?1 between b.startRange and b.endRange")
	BinRange findByBin(int bin);
}
