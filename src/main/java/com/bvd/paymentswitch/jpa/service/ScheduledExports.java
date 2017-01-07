package com.bvd.paymentswitch.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component
public class ScheduledExports {

	 static final Logger logger = LoggerFactory.getLogger(ScheduledExports.class);
	 
	 @Autowired
	 private AuthorizationService authService;
		
	 @Value("${card.export.dir}")
	 private String cardExportDir;
	
	 
	 @Scheduled(fixedRate=50000)
	 public void exportBVDCards() {
		 logger.info("Starting BVD card export to " + cardExportDir + " at {}", new Date());
	 }
	 
	 
	 
}
