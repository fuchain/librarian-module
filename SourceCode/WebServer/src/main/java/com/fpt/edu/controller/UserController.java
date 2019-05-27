package com.fpt.edu.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
	
	
	@ApiOperation(value = "View a list of available products", response = String.class)
	@ApiResponses(value = {
		    @ApiResponse(code = 200, message = "Successfully retrieved list"),
		    @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
		})

	@RequestMapping(value = "/test", method = RequestMethod.GET, produces = "application/json")
	public String test() throws Exception {
		User u = new User();
		u.setUsername("phongdv");
		u.setPassword("12345678");
		JSONObject result = new JSONObject();
		result.put("Message",userServices.save(u) );
		throw new Exception("ec ");


//		return result.toString();
	}

}
