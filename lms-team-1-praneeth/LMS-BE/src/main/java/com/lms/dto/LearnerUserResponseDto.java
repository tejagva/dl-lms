package com.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LearnerUserResponseDto {

	private String name;

	private String token;

	private String roles;

	private String image;

}
