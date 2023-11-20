package com.lms.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;
import java.util.zip.DataFormatException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lms.dto.LearnerUserDto;
import com.lms.dto.LearnerUserResponseDto;
import com.lms.entity.LearnerUser;
import com.lms.exception.details.EmailNotFoundException;
import com.lms.exception.details.NameFound;
import com.lms.service.LearnerUserService;
import com.lms.serviceImpl.JwtService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

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

	@PostMapping(value = "/login")
	public ResponseEntity<?> getJwtToken(@RequestBody @Valid LearnerUserDto jwt) {

		try {
			Authentication authenticate = am
					.authenticate(new UsernamePasswordAuthenticationToken(jwt.getEmail(), jwt.getPassword()));

			if (authenticate.isAuthenticated()) {

				String genJwtToken = js.genJwtToken(jwt.getEmail());
				Optional<LearnerUser> output = lus.fingbyemail(jwt.getEmail());

				byte[] imageData = lus.decompressImage(output.get().getImage());

				String s = Base64.getEncoder().encodeToString(imageData);


				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);

				LearnerUserResponseDto ld2 = new LearnerUserResponseDto(output.get().getName(), genJwtToken,
						output.get().getRoles(), s);

				return ResponseEntity.ok().headers(headers).body(ld2);
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

	@PostMapping("/upload")
	public ResponseEntity<?> uploadImage(
			@RequestPart("file") @Valid @Size(max = 50000, message = "Image size is greater than 5MB") MultipartFile mp,
			String email) throws Exception {

		String uploadImage = "";

		uploadImage = lus.saveImg(mp, email);

		if (uploadImage.equals(null)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed");
		} else {

			return ResponseEntity.status(HttpStatus.OK).body(uploadImage);
		}

	}

	@GetMapping("/{email}")
	public ResponseEntity<?> downloadImage(@PathVariable("email") String email)
			throws IOException, DataFormatException {
		byte[] imageData = lus.downloadImage(email);

		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(imageData);

	}

}
