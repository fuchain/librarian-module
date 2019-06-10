package com.fpt.edu.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fpt.edu.constant.Constant;
import com.fpt.edu.entities.Role;
import com.fpt.edu.entities.User;
import com.fpt.edu.services.UserServices;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.swagger.annotations.ApiOperation;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static com.fpt.edu.security.SecurityConstants.EXPIRATION_TIME;
import static com.fpt.edu.security.SecurityConstants.SECRET;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
	private final UserServices userServices;

	@Autowired
	public AuthenticationController(UserServices userServices) {
		this.userServices = userServices;
	}

	@ApiOperation(value = "Add new user", response = String.class)
	@PostMapping("new")
	public ResponseEntity<String> signUp(@RequestBody User user) {
		userServices.addNewUser(user);

		return UserController.getJSONResponseUserProfile(user);
	}

	private static String generateJwtTokenResponse(User user, String picture) {
		// Generate JWT token
		long expireDateUnixTime = System.currentTimeMillis() / 1000L + EXPIRATION_TIME;
		Date expireDate = new Date(expireDateUnixTime * 1000);
		List<Role> roles =  user.getRoles();
		String[] authorities = new String[roles.size()];
		for (int i = 0; i < roles.size() ; i++) {
			authorities[i]=roles.get(i).getName();
		}
		String responseToken = JWT.create()
			.withClaim("id", user.getId())
			.withSubject(user.getEmail())
			.withExpiresAt(expireDate)
			.withArrayClaim(Constant.AUTHORITIES_HEADER, authorities)
			.sign(Algorithm.HMAC512(SECRET.getBytes()));

		// Response token JSON
		JSONObject responseObj = new JSONObject();
		responseObj.put("token", responseToken);
		responseObj.put("email", user.getEmail());
		responseObj.put("fullname", user.getFullName());
		responseObj.put("picture", picture);
		responseObj.put("phone", user.getPhone());
		responseObj.put("expire", expireDateUnixTime);

		return responseObj.toString();
	}

	private String getEmailDomain(String email) {
		return email.substring(email.indexOf("@") + 1);
	}

	@ApiOperation(value = "Login with Google", response = String.class)
	@PostMapping("google")
	public ResponseEntity<String> googleLogin(@RequestBody String body) throws UnirestException {
		// Get token from request body
		JSONObject jsonBody = new JSONObject(body);
		String token = (String) jsonBody.get("token");

		// Validate by Google API
		String bearer = "Bearer " + token;
		HttpResponse<JsonNode> jsonGoogleResponse = Unirest.get(Constant.GOOGLE_AUTH_API)
			.header("Accept", "application/json")
			.header("Authorization", bearer)
			.asJson();

		try {
			String email = jsonGoogleResponse.getBody().getObject().get("email").toString();
			String fullName = jsonGoogleResponse.getBody().getObject().get("name").toString();
			String picture = jsonGoogleResponse.getBody().getObject().get("picture").toString();
			Optional<User> loggedUser = userServices.findUserByEmail(email);

			// If email is not in database
			String tokenResponse;

			if (!loggedUser.isPresent()) {
				if (getEmailDomain(email).equalsIgnoreCase("fpt.edu.vn")) {
					User newUser = new User(email, null, fullName, null);
					userServices.addNewUser(newUser);
					tokenResponse = generateJwtTokenResponse(newUser, picture);
				} else {
					return new ResponseEntity<>("Google account must be of FPT University", HttpStatus.BAD_REQUEST);
				}
			} else {
				tokenResponse = generateJwtTokenResponse(loggedUser.get(), picture);
			}

			return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex.toString(), HttpStatus.BAD_REQUEST);
		}
	}
}
