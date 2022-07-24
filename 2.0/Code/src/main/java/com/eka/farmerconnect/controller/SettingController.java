package com.eka.farmerconnect.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eka.farmerconnect.model.GeneralSettings;

@RestController
@RequestMapping("${fc.api.context}")
public class SettingController extends FCBaseController{

	@GetMapping("/general-settings")
	public ResponseEntity<GeneralSettings> getGeneralSettings(HttpServletRequest request){
		
		return new ResponseEntity<>(getAppGeneralSettings(request), HttpStatus.OK);
	}
}
