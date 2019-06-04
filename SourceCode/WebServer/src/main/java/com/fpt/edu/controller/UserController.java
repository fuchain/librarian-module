package com.fpt.edu.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fpt.edu.constant.Constant;
import com.fpt.edu.entities.Book;
import com.fpt.edu.entities.User;
import com.fpt.edu.services.UserServices;
import io.swagger.annotations.ApiOperation;
import org.glassfish.grizzly.compression.lzma.impl.Base;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("users")
public class UserController extends BaseController {
    @Autowired
    private UserServices userServices;

    @RequestMapping(value = "/{userId}/books/addABook", method = RequestMethod.PATCH, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<User> AddBooktoUser(@PathVariable Long userId, @RequestBody String body) {
        JSONObject jsonBody = new JSONObject(body);
        return null;
    }

    @ApiOperation(value = "get a list of current book", response = String.class)
    @RequestMapping(value = "current_books", method = RequestMethod.GET, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<List<Book>> getCurrentBookOfUser() throws JsonProcessingException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = (String) authentication.getPrincipal();
            User user = userServices.getUserByEmail(email);

            List<Book> currentBookList = userServices.getCurrentBookListOfUser(user.getId());

            return new ResponseEntity<>(currentBookList, HttpStatus.OK);
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return null;
    }

}
