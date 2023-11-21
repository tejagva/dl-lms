package com.lms.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lms.dto.UserVerifyDto;
import com.lms.entity.User;
import com.lms.exception.details.EmailNotFoundException;
import com.lms.repository.OtpRepo;
import com.lms.repository.UserRepo;
import com.lms.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo lur;

	@Autowired
	private PasswordEncoder pe;

	@Autowired
	private OtpRepo or;

	@Override
	public User saveLU(User lu) {

		User lu1 = new User();

		lu1.setName(lu.getName());
		lu1.setEmail(lu.getEmail());
		lu1.setPassword(pe.encode(lu.getPassword()));
		lu1.setRoles(lu.getRoles());

		if (getByemail(lu1)) {
			return null;
		} else {
			return lur.save(lu1);
		}
	}

	@Override
	public Boolean getByemail(User lu) {
		boolean findByName = lur.existsByemail(lu.getEmail());
		return findByName;
	}

	@Override
	public Optional<User> fingbyemail(String email) {

		Optional<User> findByemail;
		try {
			findByemail = lur.findByemail(email);
			return findByemail;
		} catch (Exception e) {
			throw new EmailNotFoundException("Email not found");
		}
	}

	@Override
	public List<User> getLU(long id) {
		return null;
	}

	@Override
	public User updateLU(User lu) {
		return null;
	}

	@Override
	public void deleteLU(long id) {
		return;
	}

	@Override
	public ResponseEntity<?> getby(User lu) {

		if (lur.findByemail(lu.getEmail()).isEmpty()) {
			throw new EmailNotFoundException("Email Not Found");
		} else {
			return new ResponseEntity<Object>(lur.findByemail(lu.getEmail()).get(), HttpStatus.OK);
		}
	}

	@Override
	public String saveImg(MultipartFile file, String email) throws Exception {

		String filePath = "/Users/desettipraneeth/Desktop/images/" + file.getOriginalFilename();
		Path filePathurl = Paths.get(filePath);
		file.transferTo(filePathurl.toFile());

		try {
			Optional<User> lu = lur.findByemail(email);
			User lu1 = lu.get();
			lu1.setImageurl(filePath);
			User imageData = lur.save(lu1);
			if (imageData != null) {
				return "File Uploaded Successfully :" + file.getOriginalFilename().toLowerCase();
			} else {
				throw new Exception("Failed To Upload File");
			}
		} catch (Exception e) {
			throw new EmailNotFoundException("Email not Found");
		}
	}

	@Override
	public byte[] downloadImage(String email) throws IOException, DataFormatException {

		User img = lur.findByemail(email).orElseThrow(() -> new EmailNotFoundException("Email not found"));

		String filePath = img.getImageurl();
		byte[] images = Files.readAllBytes(new File(filePath).toPath());

		return images;
	}

	@Override
	public User Luupdate(User lu) {

		try {
			Optional<User> fingbyemail = lur.findByemail(lu.getEmail());

			User lu1 = fingbyemail.get();

			if (!(lu.getEmail()).isBlank()) {
				lu1.setEmail(lu.getEmail());
			} else if (!(lu.getPassword()).isBlank()) {
				lu1.setPassword(lu.getPassword());
			} else if (!(lu.getName()).isBlank()) {
				lu1.setName(lu.getName());

			} else if (!(lu.getImageurl()).isBlank()) {
				lu1.setImageurl(lu.getImageurl());
			}

			return lur.save(lu1);
		}

		catch (Exception e) {
			throw new EmailNotFoundException("Email Not Found");
		}
	}

	@Override
	public boolean saveotp(UserVerifyDto uvt) {

		if (!or.save(uvt).equals(null)) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public boolean verifyAccount(String email, String otp) {

		Optional<UserVerifyDto> findByemail;
		try {
			findByemail = or.findByemail(email);

			if (findByemail.get().getOtp().equals(otp) && Duration
					.between(findByemail.get().getOtpGeneratedTime(), LocalDateTime.now()).getSeconds() < (1 * 60)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new EmailNotFoundException("Email Not Found");
		}
	}

}
