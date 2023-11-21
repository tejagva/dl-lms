package com.lms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lms.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

	boolean existsByemail(String email);

	Optional<User> findByemail(String email);
	

}
