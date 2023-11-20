package com.lms.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.lms.entity.LearnerUser;

public interface LearnerUserService {

	public LearnerUser saveLU(LearnerUser lu);

	public List<LearnerUser> getLU(long id);

	public LearnerUser updateLU(LearnerUser lu);

	public void deleteLU(long id);

	Boolean getByemail(LearnerUser lu);

	public ResponseEntity<?> getby(LearnerUser lu);

	public String saveImg(MultipartFile file, String name) throws Exception;

	byte[] downloadImage(String email) throws IOException, DataFormatException;

	Optional<LearnerUser> fingbyemail(String email);

	public LearnerUser Luupdate(LearnerUser lu1);

}
