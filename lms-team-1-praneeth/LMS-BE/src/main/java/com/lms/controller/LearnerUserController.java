package com.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
import com.lms.serviceImpl.JwtService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class LearnerUserController {

	@Autowired
	private LearnerUserService lus;

	@Autowired
	private JwtService js;

	@Autowired
	private AuthenticationManager am;

	@GetMapping("/connect")
	public String login() {

		return "Connected To Back-End";

	}

	@PostMapping("/jwt")
	public String getJwtToken(@RequestBody @Valid LearnerUserDto jwt) {

		try {
			Authentication authenticate = am
					.authenticate(new UsernamePasswordAuthenticationToken(jwt.getEmail(), jwt.getPassword()));

			if (authenticate.isAuthenticated()) {
				return js.genJwtToken(jwt.getEmail());
			} else {
				throw new EmailNotFoundException("Email Not Found");
			}

		}

		catch (BadCredentialsException ex) {
			throw new EmailNotFoundException("Password Incorrect");
		}

	}

	@PostMapping("/signup")
	@PreAuthorize("hasAuthority('superadmin')")
	public ResponseEntity<LearnerUser> signUp(@RequestBody @Valid LearnerUser lu) {

		LearnerUser saveLU = lus.saveLU(lu);

		if (saveLU.equals(null))
			throw new NameFound();
		else {
			return new ResponseEntity<LearnerUser>(saveLU, HttpStatus.CREATED);
		}
	}

//	@GetMapping("/login")
//	public ResponseEntity<String> login1(@RequestBody @Valid LearnerUserDto lud) {
//
//		LearnerUser lu = LearnerUser.build(0, null, lud.getEmail(), lud.getPassword(), null);
//		ResponseEntity<?> getby = lus.getby(lu);
//
//		if (getby.equals(null)) {
//			throw new EmailNotFoundException();
//
//		} else {
//
//			return new ResponseEntity<String>("Welcome" + getby.getBody(), getby.getStatusCode());
//
//		}
//
//	}

	@GetMapping("/api1")
	public String api() {
		return "Hello From Jwt Token";
	}

}
