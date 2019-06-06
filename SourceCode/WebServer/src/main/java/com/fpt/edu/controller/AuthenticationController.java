package com.fpt.edu.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fpt.edu.constant.Constant;
import com.fpt.edu.entities.User;
import com.fpt.edu.services.UserServices;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.swagger.annotations.ApiOperation;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

import static com.fpt.edu.security.SecurityConstants.EXPIRATION_TIME;
import static com.fpt.edu.security.SecurityConstants.SECRET;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
    private final UserServices userServices;

    public AuthenticationController(UserServices userServices) {
        this.userServices = userServices;
    }

    @ApiOperation(value = "Add new user", response = String.class)
    @PostMapping("new")
    public ResponseEntity<String> signUp(@RequestBody User user) {
        userServices.addNewUser(user);

        return UserController.getJSONResponseUserProfile(user);
    }

    private static String generateJwtTokenResponse(User user) {
        // Generate JWT token
        long expireDateUnixTime = System.currentTimeMillis() / 1000L + EXPIRATION_TIME;
        Date expireDate = new Date(expireDateUnixTime * 1000);

        String responseToken = JWT.create()
                .withClaim("id", user.getId())
                .withSubject(user.getEmail())
                .withExpiresAt(expireDate)
                .sign(Algorithm.HMAC512(SECRET.getBytes()));

        // Response token JSON
        JSONObject responseObj = new JSONObject();
        responseObj.put("token", responseToken);
        responseObj.put("email", user.getEmail());
        responseObj.put("fullname", user.getFullName());
        responseObj.put("phone", user.getPhone());
        responseObj.put("expire", expireDateUnixTime);

        return responseObj.toString();
    }

    private String getEmailDomain(String email) {
        return  email.substring(email.indexOf("@") + 1);
    }

    @ApiOperation(value = "Login with Google", response = String.class)
    @PostMapping("google")
    public ResponseEntity<String> googleLogin(@RequestBody String body) throws UnirestException {
        // Get token from request body
        JSONObject jsonBody = new JSONObject(body);
        String token = (String)jsonBody.get("token");

        // Validate by Google API
        HttpResponse<JsonNode> jsonGoogleResponse = Unirest.get(Constant.GOOGLE_AUTH_API + token).asJson();
        try {
            String email = jsonGoogleResponse.getBody().getObject().get("email").toString();
            Optional<User> loggedUser = userServices.findUserByEmail(email);

            // If email is not in database
            String tokenResponse;

            if (!loggedUser.isPresent()) {
                if (getEmailDomain(email).equalsIgnoreCase("fpt.edu.vn")) {
                    User newUser = new User(email, null, null, null);
                    userServices.addNewUser(newUser);
                    tokenResponse = generateJwtTokenResponse(newUser);
                } else {
                    return new ResponseEntity<>("Google account must be of FPT University", HttpStatus.BAD_REQUEST);
                }
            } else {
                tokenResponse = generateJwtTokenResponse(loggedUser.get());
            }

            return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(ex.toString(), HttpStatus.BAD_REQUEST);
        }
    }
}
