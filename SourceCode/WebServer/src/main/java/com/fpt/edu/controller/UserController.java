package com.fpt.edu.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fpt.edu.constant.Constant;
import com.fpt.edu.entities.Book;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fpt.edu.entities.User;
import com.fpt.edu.services.UserServices;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.HandlerMapping;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController extends BaseController {
    @Autowired
    private UserServices userServices;


    @ApiOperation(value = "View a list of available products", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })

//    @RequestMapping(value = "/test", method = RequestMethod.GET, produces = "application/json")
//    public String test() throws Exception {
//        User u = new User();
//        u.setUsername("phongdv");
//        u.setPassword("12345678");
//        JSONObject result = new JSONObject();
//        result.put("Message", userServices.save(u));
//        throw new Exception("ec ");
//
//
////		return result.toString();
//    }

    @RequestMapping(value = "/current/{id}", method = RequestMethod.GET, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<String> getCurrentBookOfUser(@PathVariable Long id) throws JsonProcessingException {
        try {
            String requestPattern = httpServletRequest.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString();
            LOGGER.info("START Controller : " + requestPattern);
            List<Book> currentBookList = userServices.getCurrentBookListOfUser(id);
            JSONObject jsonResult = utils.buildListEntity(currentBookList, httpServletRequest);
            return new ResponseEntity<>(jsonResult.toString(), HttpStatus.OK);
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return null;
    }

    @RequestMapping(value = "/requiring/{id}", method = RequestMethod.GET, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<String> getRequiringBookList(@PathVariable Long id) throws JsonProcessingException {
        String requestPattern = httpServletRequest.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString();
        LOGGER.info("START Controller : " + requestPattern);
        List<Book> requiringBookList = userServices.getRequiringBookList(id);
        JSONObject jsonResult = utils.buildListEntity(requiringBookList, httpServletRequest);
        return new ResponseEntity<>(jsonResult.toString(), HttpStatus.OK);
    }

}
