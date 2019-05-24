package com.fpt.edu.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("users")
public class UserController {

	 @ApiOperation(value = "View a list of available products",response = Iterable.class)
//	    @ApiResponses(value = {
//	            @ApiResponse(code = 200, message = "Successfully retrieved list"),
//	            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
//	            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
//	            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
//	    })
	    
	    @RequestMapping(value = "/test", method= RequestMethod.GET, produces = "application/json")
	    public String test(){
	      
	        return "{'key':'value'}";
	    }
	
	
	
}
