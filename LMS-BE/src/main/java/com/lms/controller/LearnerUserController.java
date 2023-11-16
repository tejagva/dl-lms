package com.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.dto.LearnerUserDto;
import com.lms.entity.LearnerUser;
import com.lms.exception.details.EmailNotFoundException;
import com.lms.exception.details.NameFound;
import com.lms.service.LearnerUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class LearnerUserController {

	@Autowired
	private LearnerUserService lus;

	@GetMapping("/connect")
	public String login() {

		// throw new NameFound();
		return "Connected To Back-End";

	}

	@GetMapping("/signup")
	public ResponseEntity<LearnerUser> signUp(@RequestBody LearnerUser lu) {

		if (lu.getName().equals("raju"))
			throw new NameFound();
		else {
			return lus.saveLU(lu);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<String> login1(@RequestBody @Valid LearnerUserDto lud) {

		LearnerUser lu = LearnerUser.build(0, null, lud.getEmail(), lud.getPassword());
		ResponseEntity<?> getby = lus.getby(lu);

		if (getby.equals(null)) {
			throw new EmailNotFoundException();

		} else {
			return new ResponseEntity<String>("Welcome " + getby.getBody(), getby.getStatusCode());

		}

	}

}
