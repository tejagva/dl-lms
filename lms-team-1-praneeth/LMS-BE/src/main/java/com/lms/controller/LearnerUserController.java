package com.lms.controller;

import java.io.IOException;
import java.util.Optional;
import java.util.zip.DataFormatException;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.lms.exception.details.NameFoundException;
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

	/*
	 * 
	 * API used to test the connect for connection of back-end with front-end
	 * 
	 */

	@GetMapping("/connect")
	public String login() {
		return "Connected To Back-End";
	}

	/*
	 * 
	 * API used to generate the and send the JWT token on login and name email
	 * 
	 */

	@PostMapping(value = "/login")
	public ResponseEntity<?> getJwtToken(@RequestBody @Valid LearnerUserDto jwt)
			throws IOException, DataFormatException {

		try {
			Authentication authenticate = am
					.authenticate(new UsernamePasswordAuthenticationToken(jwt.getEmail(), jwt.getPassword()));

			if (authenticate.isAuthenticated()) {

				String genJwtToken = js.genJwtToken(jwt.getEmail());
				Optional<LearnerUser> output = lus.fingbyemail(jwt.getEmail());

				byte[] downloadImage = lus.downloadImage(jwt.getEmail());

				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);

				LearnerUserResponseDto ld2 = new LearnerUserResponseDto(output.get().getName(), genJwtToken,
						output.get().getRoles(), downloadImage);

				return ResponseEntity.ok().headers(headers).body(ld2);
			} else {
				throw new EmailNotFoundException("Email Not Found");
			}
		} catch (BadCredentialsException ex) {
			throw new EmailNotFoundException("Password Incorrect");
		}
	}

	/*
	 * 
	 * API takes the data from client and creates a account and store the data in Db
	 * 
	 */

	@PostMapping("/signup")
	@PreAuthorize("hasAuthority('superadmin')")
	public ResponseEntity<LearnerUser> signUp(@RequestBody @Valid LearnerUser lu) {

		LearnerUser saveLU = lus.saveLU(lu);
		if (saveLU.equals(null)) {
			throw new NameFoundException();
		} else {
			return new ResponseEntity<LearnerUser>(saveLU, HttpStatus.CREATED);
		}
	}

	/*
	 * 
	 * API used to upload the image files to the DB based on the email
	 * 
	 */

	@PostMapping("/upload")
	public ResponseEntity<?> uploadImage(
			@RequestPart("file") @Valid @Size(max = 50000, message = "Image size is greater than 5MB") MultipartFile mp,
			String email) throws Exception {

		String uploadImage = lus.saveImg(mp, email);

		if (uploadImage.equals(null)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed");
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(uploadImage);
		}
	}

	/*
	 * 
	 * API used to retrieve the image from the DB using the email as parameter
	 * 
	 */

	@GetMapping("/{email}")
	public ResponseEntity<?> downloadImage(@PathVariable("email") String email)
			throws IOException, DataFormatException {
		byte[] imageData = lus.downloadImage(email);

		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/jpeg")).body(imageData);

	}

	@PostMapping("/update")
	public ResponseEntity<String> learnerUserUpdate(@RequestBody LearnerUser lu) {

		LearnerUser luupdate = lus.Luupdate(lu);

		if (luupdate.equals(null)) {
			throw new EmailNotFoundException("Email not Found");
		} else {
			return new ResponseEntity<String>("User details updated", HttpStatus.ACCEPTED);
		}

	}

}
