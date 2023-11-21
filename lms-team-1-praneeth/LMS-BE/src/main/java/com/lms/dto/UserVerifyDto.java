package com.lms.dto;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVerifyDto {

	@Id
	private String email;
	private String otp;
	private LocalDateTime otpGeneratedTime;

}
