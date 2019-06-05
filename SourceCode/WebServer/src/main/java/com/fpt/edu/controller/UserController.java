package com.fpt.edu.controller;

import com.fpt.edu.common.ERequestStatus;
import com.fpt.edu.common.ERequestType;
import com.fpt.edu.constant.Constant;
import com.fpt.edu.entities.Book;
import com.fpt.edu.entities.Request;
import com.fpt.edu.entities.User;
import com.fpt.edu.services.RequestServices;
import com.fpt.edu.services.UserServices;
import io.swagger.annotations.ApiOperation;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("users")
public class UserController extends BaseController {
    @Autowired
    private UserServices userServices;

    @Autowired
    private RequestServices requestServices;

    @ApiOperation(value = "Get a list of current book", response = String.class)
    @RequestMapping(value = "current_books", method = RequestMethod.GET, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<List<Book>> getCurrentBook() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = (String) authentication.getPrincipal();
            User user = userServices.getUserByEmail(email);

            List<Book> currentBookList = userServices.getCurrentBookListOfUser(user.getId());
            List<Request> returningList = requestServices.findByUserIdAndType(user.getId(),
                    ERequestType.RETURNING.getValue(), ERequestStatus.COMPLETED.getValue());

            Iterator iterator = currentBookList.iterator();
            while (iterator.hasNext()) {
                Book currentBook = (Book) iterator.next();

                for (int j = 0; j < returningList.size(); j++) {
                    Book returningBook = returningList.get(j).getBook();

                    if (currentBook.getId().equals(returningBook.getId())) {
                        iterator.remove();
                    }
                }
            }

            return new ResponseEntity<>(currentBookList, HttpStatus.OK);
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return null;
    }

    static ResponseEntity<String> getJSONResponseUserProfile(User user) {
        JSONObject responseJSON = new JSONObject();
        responseJSON.put("id", user.getId());
        responseJSON.put("email", user.getEmail());
        responseJSON.put("fullname", user.getFullName());
        responseJSON.put("phone", user.getPhone());

        return ResponseEntity.ok().body(responseJSON.toString());
    }

    @ApiOperation(value = "Get user information", response = String.class)
    @GetMapping("profile")
    public ResponseEntity<String> getMe(Principal principal) {
        String email = principal.getName();
        User user = userServices.getUserByEmail(email);

        return getJSONResponseUserProfile(user);
    }

    @ApiOperation(value = "Update user profile", response = String.class)
    @RequestMapping(value = "update_profile", method = RequestMethod.PUT, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<User> updateUser(@RequestBody String body) throws Exception {
        // get user information
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        User user = userServices.getUserByEmail(email);

        JSONObject bodyObject = new JSONObject(body);
        String fullName = bodyObject.getString("fullname");
        String phone = bodyObject.getString("phone");

        if (phone.length() != Constant.PHONE_NUMBER) {
            throw new Exception("Phone number must be 10 digits");
        }

        user.setFullName(fullName);
        user.setPhone(phone);

        User updatedUser = userServices.updateUser(user);

        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

}
