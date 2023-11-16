package com.lms.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

		Boolean byemail = getByemail(lu1);

		if (byemail) {

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
	public List<LearnerUser> getLU(long id) {
		return null;
	}

	@Override
	public LearnerUser updateLU(LearnerUser lu) {
		return null;
	}

	@Override
	public void deleteLU(long id) {

	}

	@Override
	public ResponseEntity<?> getby(LearnerUser lu) {

		if (lur.findByemail(lu.getEmail()).isEmpty()) {

			throw new EmailNotFoundException();
		} else {

			return new ResponseEntity<Object>(lur.findByemail(lu.getEmail()).get(), HttpStatus.OK);
		}

	}

}
