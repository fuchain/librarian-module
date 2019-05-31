package com.fpt.edu.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fpt.edu.common.RequestType;
import com.fpt.edu.constant.Constant;
import com.fpt.edu.entities.Book;
import com.fpt.edu.entities.BookDetail;
import com.fpt.edu.entities.Request;
import com.fpt.edu.entities.User;
import com.fpt.edu.exception.EntityAldreayExisted;
import com.fpt.edu.exception.EntityNotFoundException;
import com.fpt.edu.exception.StatusNotSupportedException;
import com.fpt.edu.services.BookDetailsServices;
import com.fpt.edu.services.BookServices;
import com.fpt.edu.services.RequestServices;
import com.fpt.edu.services.UserServices;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.EAN;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("requests")
public class RequestController extends BaseController {

    @Autowired
    private RequestServices requestServices;

    @Autowired
    private UserServices userServices;

    @Autowired
    private BookDetailsServices bookDetailsServices;

    @Autowired
    private BookServices bookServices;

  /*  @RequestMapping(value = "", method = RequestMethod.GET, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<String> getListOfRequest() throws JsonProcessingException {
        LOGGER.info("START Controller :" + httpServletRequest.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString());
            JSONObject jsonResult = utils.buildListEntity(requestServices.getListRequest(), httpServletRequest);
        return new ResponseEntity<>("Success", HttpStatus.OK);

    }
*/

    @ApiOperation(value = "Get a request by its id", response = String.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<String> getRequestById(@PathVariable Long id) throws JsonProcessingException {
        Request request = requestServices.getRequestById(id);
        JSONObject jsonObject = utils.convertObjectToJSONObject(request);

        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    @ApiOperation(value = "Create a book request", response = String.class)
    @RequestMapping(value = "create", method = RequestMethod.POST, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<String> requestBook(@RequestBody String body) throws IOException, EntityNotFoundException, StatusNotSupportedException, EntityAldreayExisted {
        //get user information
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        User user = userServices.getUserByUsername(username);

        //get response body
        JSONObject bodyObject = new JSONObject(body);
        //get status
        int status = bodyObject.getInt("status");

        Request request = null;

        //if request is requiring
        if (status == RequestType.BORROWING.getValue()) {
            //get book name
            String bookName = bodyObject.getString("book_name");
            //get book object
            BookDetail bookDetail = bookDetailsServices.getBookDetailByName(bookName);

            if (bookDetail == null) {
                throw new EntityNotFoundException("Book name: " + bookName + " not found");
            }

            //check existed request
            boolean existed = requestServices.checkExistedRequest(status, user.getId(), bookDetail.getId(), (long) 0);
            if (existed) {
                throw new EntityAldreayExisted("Request's already existed");
            }

            //create a request and fill data
            request = new Request();
            request.setStatus(status);
            request.setUser(user);
            request.setBookDetail(bookDetail);

        } else if (status == RequestType.RETURNING.getValue()) {//if request is returning
            //get book id
            Long bookId = bodyObject.getLong("book_id");
            //get book object
            Book book = bookServices.getBookById(bookId);

            //check whether user is keeping this book
            boolean keeping = false;
            List<Book> currentBookList = userServices.getCurrentBookListOfUser(user.getId());
            for(Book b : currentBookList){
                if(b.getId() == bookId){
                    keeping = true;
                    break;
                }
            }

            if(keeping == false){
                throw new EntityNotFoundException("This book is not found in the current book list");
            }

            //check whether the request is already existed
            boolean existed = requestServices.checkExistedRequest(status, user.getId(), (long) 0, book.getId());
            if (existed) {
                throw new EntityAldreayExisted("Request's already existed");
            }

            //create a request and fill data
            request = new Request();
            request.setStatus(status);
            request.setUser(user);
            request.setBook(book);
        } else {
            throw new StatusNotSupportedException("Status " + status + " is not supported");
        }

        //save request
        requestServices.saveRequest(request);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "success");

        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    @ApiOperation(value = "Get a list of book request", response = String.class)
    @RequestMapping(value = "/{status}/get_list", method = RequestMethod.POST, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<String> getBookRequestList(@PathVariable int status) throws JsonProcessingException {
        //get user information
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        User user = userServices.getUserByUsername(username);

        List<Request> requestList = requestServices.findByUserIdAndStatus(user.getId(), status);
        JSONObject jsonObject = utils.buildListEntity(requestList, httpServletRequest);

        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

}
