package com.fpt.edu.controller;

import com.fpt.edu.common.EMatchingStatus;
import com.fpt.edu.entities.Matching;
import com.fpt.edu.entities.User;
import com.fpt.edu.exception.EntityIdMismatchException;
import com.fpt.edu.services.MatchingServices;
import com.fpt.edu.services.UserServices;
import io.swagger.annotations.ApiOperation;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("matchings")
public class MatchingController extends BaseController {
    private final MatchingServices matchingServices;
    private final UserServices userServices;

    @Autowired
    public MatchingController(MatchingServices matchingServices, UserServices userServices) {
        this.matchingServices = matchingServices;
        this.userServices = userServices;
    }

    @ApiOperation(value = "Returner confirms book transfer", response = String.class)
    @GetMapping("/{id}/confirm")
    public ResponseEntity<String> confirmBookTransfer(@PathVariable("id") Long matchingId) throws Exception {

        Matching matching = matchingServices.getMatchingById(matchingId);
        User matchingUser = matching.getReturnerRequest().getUser();

        // Get user information
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        User user = userServices.getUserByEmail(email);

        // The returner does not send the request
        if (!user.getId().equals(matchingUser.getId())) {
            throw new EntityIdMismatchException("User id of matching: " + matchingUser.getId() + " does not match to user id from authentication");
        }
        // Receiver has not imported pin yet
        if (matching.getStatus() != EMatchingStatus.CONFIRMED.getValue()) {
            throw new Exception("Receiver has not imported pin yet");
        }

        // Return message
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "confirmed");
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }
}
