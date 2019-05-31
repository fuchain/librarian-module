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
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
    public ResponseEntity<User> signUp(@RequestBody User user) {
        User result = userServices.addNewUser(user);
        return ResponseEntity.ok().body(result);
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
            Optional<User> loggedUser = userServices.findUserByUsername(email);

            // If email is not in database
            if (loggedUser.isEmpty()) {
                return new ResponseEntity<>("User is not in database", HttpStatus.BAD_REQUEST);
            }

            // Generate JWT token
            Date expireDate = new Date(System.currentTimeMillis() / 1000L + EXPIRATION_TIME);
            String responseToken = JWT.create()
                    .withClaim("id", loggedUser.get().getId())
                    .withSubject(email)
                    .withExpiresAt(expireDate)
                    .sign(Algorithm.HMAC512(SECRET.getBytes()));

            // Response token JSON
            JSONObject responseObj = new JSONObject();
            responseObj.put("token", responseToken);
            responseObj.put("email", email);
            responseObj.put("expire", expireDate.getTime());

            return new ResponseEntity<>(responseObj.toString(), HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(ex.toString(), HttpStatus.BAD_REQUEST);
        }
    }
}
