package com.lms.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lms.entity.LearnerUser;
import com.lms.exception.details.EmailNotFoundException;
import com.lms.repository.LearnerUserRepo;
import com.lms.service.LearnerUserService;

@Service
public class LearnerUserServiceImpl implements LearnerUserService {

	@Autowired
	private LearnerUserRepo lur;

	@Autowired
	private PasswordEncoder pe;

	@Override
	public LearnerUser saveLU(LearnerUser lu) {

		LearnerUser lu1 = new LearnerUser();

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
	public Boolean getByemail(LearnerUser lu) {
		boolean findByName = lur.existsByemail(lu.getEmail());
		return findByName;
	}

	@Override
	public Optional<LearnerUser> fingbyemail(String email) {

		Optional<LearnerUser> findByemail;
		try {
			findByemail = lur.findByemail(email);
			return findByemail;
		} catch (Exception e) {
			throw new EmailNotFoundException("Email not found");
		}
	}

	@Override
	public List<LearnerUser> getLU(long id) {
		return null;
	}

	@Override
	public LearnerUser updateLU(LearnerUser lu) {
		return null;
	}

	@Override
	public void deleteLU(long id) {
		return;
	}

	@Override
	public ResponseEntity<?> getby(LearnerUser lu) {

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
			Optional<LearnerUser> lu = lur.findByemail(email);
			LearnerUser lu1 = lu.get();
			lu1.setImageurl(filePath);
			LearnerUser imageData = lur.save(lu1);
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

		LearnerUser img = lur.findByemail(email).orElseThrow(() -> new EmailNotFoundException("Email not found"));

		String filePath = img.getImageurl();
		byte[] images = Files.readAllBytes(new File(filePath).toPath());

		return images;
	}

	@Override
	public LearnerUser Luupdate(LearnerUser lu) {

		try {
			Optional<LearnerUser> fingbyemail = lur.findByemail(lu.getEmail());

			LearnerUser lu1 = fingbyemail.get();

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

}
