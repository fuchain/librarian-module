package com.fpt.edu.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fpt.edu.constant.Constant;
import com.fpt.edu.entities.Role;
import com.fpt.edu.entities.User;
import com.fpt.edu.services.RoleServices;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.fpt.edu.security.SecurityConstants.EXPIRATION_TIME;
import static com.fpt.edu.security.SecurityConstants.SECRET;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
	private final UserServices userServices;
	private final RoleServices roleServices;

	@Autowired
	public AuthenticationController(UserServices userServices, RoleServices roleServices) {
		this.userServices = userServices;
		this.roleServices = roleServices;
	}

	private static String generateJwtTokenResponse(User user, String picture) {
		// Generate JWT token
		long expireDateUnixTime = System.currentTimeMillis() / 1000L + EXPIRATION_TIME;
		Date expireDate = new Date(expireDateUnixTime * 1000);

		Role role;
		if (user.getRole() == null) {
			role = new Role("reader");
		} else {
			role = user.getRole();
		}

		String[] authorities = new String[1];
		authorities[0] = role.getName();

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

	@ApiOperation(value = "Add new user", response = String.class)
	@PostMapping("new")
	public ResponseEntity<String> signUp(@RequestBody User user) {
		User newUser = userServices.addNewUser(user);

		return UserController.getJSONResponseUserProfile(newUser);
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
			String email = jsonGoogleResponse.getBody().getObject().get("email").toString().toLowerCase();
			String fullName;
			fullName = jsonGoogleResponse.getBody().getObject().get("name").toString();
			for (String prefix : Constant.FPT_EMAIL_PREFIXS) {
				fullName = fullName.replace(prefix, Constant.EMPTY_VALUE);
			}
			String picture = jsonGoogleResponse.getBody().getObject().get("picture").toString();
			Optional<User> loggedUser = userServices.findUserByEmail(email);

			// If email is not in database
			String tokenResponse;

			if (!loggedUser.isPresent()) {
				if (getEmailDomain(email).equalsIgnoreCase("fpt.edu.vn")) {
					User newUser = userServices.addNewUser(new User(email, null, fullName, null));
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
