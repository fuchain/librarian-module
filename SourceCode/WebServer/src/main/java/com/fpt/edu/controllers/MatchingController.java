package com.fpt.edu.controllers;

import com.fpt.edu.common.enums.EMatchingStatus;
import com.fpt.edu.entities.Matching;
import com.fpt.edu.entities.User;
import com.fpt.edu.exceptions.EntityIdMismatchException;
import com.fpt.edu.services.MatchingServices;
import com.fpt.edu.services.UserServices;
import io.swagger.annotations.ApiOperation;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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
	public ResponseEntity<String> confirmBookTransfer(
		@PathVariable("id") Long matchingId, Principal principal
	) throws EntityIdMismatchException, EntityIdMismatchException {

		Matching matching = matchingServices.getMatchingById(matchingId);
		User matchingUser = matching.getReturnerRequest().getUser();

		// Get user information
		User user = userServices.getUserByEmail(principal.getName());

		// The returner does not send the request
		if (!user.getId().equals(matchingUser.getId())) {
			throw new EntityIdMismatchException(
				"User id of matching: " + matchingUser.getId() + " does not match to user id from authentication"
			);
		}

		// Receiver has not imported pin yet
		if (matching.getStatus() == EMatchingStatus.PENDING.getValue()) {
			throw new EntityIdMismatchException("Receiver has not imported pin yet");
		}

		// Check the receiver rejected or not
		if (matching.getStatus() == EMatchingStatus.REJECTED.getValue()) {
			return new ResponseEntity<>("Receiver has rejected to the book", HttpStatus.EXPECTATION_FAILED);
		}

		// Return message
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("message", "confirmed");
		return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
	}
}
