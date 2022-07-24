package com.eka.farmerconnect.service;

import org.springframework.stereotype.Service;

import com.eka.service.CommonOperationService;

@Service
public class AbstractCommonService {
	
	// TODO:: Get this service as spring bean
	protected CommonOperationService commonOperationService = new CommonOperationService();

}
