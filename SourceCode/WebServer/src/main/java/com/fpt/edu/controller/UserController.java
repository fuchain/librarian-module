package com.fpt.edu.controller;

import com.fpt.edu.constant.Constant;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fpt.edu.entities.User;
import com.fpt.edu.services.UserServices;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("users")
public class UserController {
	@Autowired
	private UserServices userServices;
	
	
	@RequestMapping(value = "/{userId}/books/addABook",method = RequestMethod.PATCH, produces = Constant.APPLICATION_JSON)
	public ResponseEntity<User> AddBooktoUser (@PathVariable String userId, @RequestBody String body){



		return null;
	}





}
