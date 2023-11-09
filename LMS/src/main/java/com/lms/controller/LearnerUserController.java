package com.lms.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.entity.LearnerUser;

@RestController
public class LearnerUserController {

	@PostMapping("/save")
	public LearnerUser save()
	{
		return null;
	}
	
	
	
	
}
