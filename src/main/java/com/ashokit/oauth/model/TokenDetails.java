package com.ashokit.oauth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenDetails {

	private String accessToken;
	
	private String refreshToken;
}
