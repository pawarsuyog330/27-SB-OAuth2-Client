package com.ashokit.oauth.controller;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.ashokit.oauth.model.TokenDetails;
import com.ashokit.oauth.model.UserProfile;

@Controller
public class OAuth2ClientController {
	
	static Logger logger=LoggerFactory.getLogger(OAuth2ClientController.class);
	
	@GetMapping("/index")
	public String getIndexPage()
	{
		return "index";
	}
	
	@GetMapping("/auth_code")
	public String UserProfile(@RequestParam("code")String code, Model model)
	{
		HttpHeaders headers=new HttpHeaders();
		headers.setBasicAuth("clientapp", "654321");
		try {
			RequestEntity<Void> reqEntity=RequestEntity.post(new URI("http://localhost:5858/oauth/token"+"?code="+code+"&scope=read"+"&grant_type=authorization_code")).headers(headers).build();
			ResponseEntity<TokenDetails> respEntity=new RestTemplate().exchange(reqEntity, TokenDetails.class);
			TokenDetails td=respEntity.getBody();
			logger.info("Access Token received from OAuth Server : " + td.getAccessToken());
			logger.info("Refresh Token received from OAuth Server : " + td.getRefreshToken());
			
			HttpHeaders headers1=new HttpHeaders();
			headers1.setBearerAuth(td.getAccessToken());
			
			RequestEntity<Void> reqEntity1=RequestEntity.get(new URI("http://localhost:5858/api/users/me")).headers(headers1).build();
			ResponseEntity<UserProfile> respEntity1=new RestTemplate().exchange(reqEntity1, UserProfile.class);
			UserProfile userProfile=respEntity1.getBody();
			model.addAttribute("userProfile", userProfile);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "show";
	}

}
