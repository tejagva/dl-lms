package com.lms.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lms.entity.LearnerUser;
import com.lms.exception.details.EmailNotFoundException;
import com.lms.repository.LearnerUserRepo;

@Service
public class LearnerUserUds implements UserDetailsService {

	@Autowired
	private LearnerUserRepo lur;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<LearnerUser> findByemail = lur.findByemail(username);

		return findByemail.map(details -> new LearnerUserUd(details)).orElseThrow(() -> new EmailNotFoundException());
	}

}
