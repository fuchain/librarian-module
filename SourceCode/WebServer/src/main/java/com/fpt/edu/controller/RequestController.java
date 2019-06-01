package com.fpt.edu.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fpt.edu.common.MatchingStatus;
import com.fpt.edu.common.RequestStatus;
import com.fpt.edu.common.RequestType;
import com.fpt.edu.constant.Constant;
import com.fpt.edu.entities.*;
import com.fpt.edu.exception.*;
import com.fpt.edu.services.*;
import io.swagger.annotations.ApiOperation;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private MatchingServices matchingServices;



  /*  @RequestMapping(value = "", method = RequestMethod.GET, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<String> getListOfRequest() throws JsonProcessingException {
        LOGGER.info("START Controller :" + httpServletRequest.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString());
            JSONObject jsonResult = utils.buildListEntity(requestServices.getListRequest(), httpServletRequest);
        return new ResponseEntity<>("Success", HttpStatus.OK);

    }
*/

    @ApiOperation(value = "Get a request by its id", response = String.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<String> getRequestById(@PathVariable Long id) throws JsonProcessingException, EntityNotFoundException {
        Request request = requestServices.getRequestById(id);
        if (request == null) {
            throw new EntityNotFoundException("Request id: " + request.getId() + " not found");
        }

        JSONObject jsonObject = utils.convertObjectToJSONObject(request);

        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    @ApiOperation(value = "Get a list of book request", response = String.class)
    @RequestMapping(value = "/get_list", method = RequestMethod.GET, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<String> getBookRequestList(@RequestParam int type) throws JsonProcessingException {
        //get user information
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        User user = userServices.getUserByEmail(email);

        List<Request> requestList = requestServices.findByUserIdAndType(user.getId(), type);
        JSONObject jsonObject = utils.buildListEntity(requestList, httpServletRequest);

        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    @ApiOperation(value = "Create a book request", response = String.class)
    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<String> requestBook(@RequestBody String body) throws IOException, EntityNotFoundException, TypeNotSupportedException, EntityAldreayExisted {
        //get user information
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        User user = userServices.getUserByEmail(email);

        //get response body
        JSONObject bodyObject = new JSONObject(body);
        //get type
        int type = bodyObject.getInt("type");

        Request request = null;

        //if request is requiring
        if (type == RequestType.BORROWING.getValue()) {
            //get book name
            String bookName = bodyObject.getString("book_name");
            //get book object
            BookDetail bookDetail = bookDetailsServices.getBookDetailByName(bookName);

            if (bookDetail == null) {
                throw new EntityNotFoundException("Book name: " + bookName + " not found");
            }

            //check existed request
            boolean existed = requestServices.checkExistedRequest(type, user.getId(), bookDetail.getId(), (long) 0);
            if (existed) {
                throw new EntityAldreayExisted("Request's already existed");
            }

            //create a request and fill data
            request = new Request();
            request.setStatus(RequestStatus.PENDING.getValue());
            request.setType(type);
            request.setUser(user);
            request.setBookDetail(bookDetail);

        } else if (type == RequestType.RETURNING.getValue()) {//if request is returning
            //get book id
            Long bookId = bodyObject.getLong("book_id");
            //get book object
            Book book = bookServices.getBookById(bookId);

            //check whether user is keeping this book
            boolean keeping = false;
            List<Book> currentBookList = userServices.getCurrentBookListOfUser(user.getId());
            for (Book b : currentBookList) {
                if (b.getId() == bookId) {
                    keeping = true;
                    break;
                }
            }

            if (keeping == false) {
                throw new EntityNotFoundException("This book is not found in the current book list");
            }

            //check whether the request is already existed
            boolean existed = requestServices.checkExistedRequest(type, user.getId(), (long) 0, book.getId());
            if (existed) {
                throw new EntityAldreayExisted("Request's already existed");
            }

            //create a request and fill data
            request = new Request();
            request.setStatus(RequestStatus.PENDING.getValue());
            request.setType(type);
            request.setUser(user);
            request.setBook(book);
        } else {
            throw new TypeNotSupportedException("Type " + type + " is not supported");
        }

        //save request
        requestServices.saveRequest(request);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "success");

        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    @ApiOperation(value = "Returner returns a book", response = String.class)
    @RequestMapping(value = "/return", method = RequestMethod.GET, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<String> returnBook(@RequestParam Long matchingId) throws EntityNotFoundException {

        Matching matching = matchingServices.getMatchingById(matchingId);
        if (matching == null) {
            throw new EntityNotFoundException("Matching id: " + matchingId + " not found");
        }

        String pin = utils.getPin();
        Date createdAt = new Date();
        int status = MatchingStatus.PENDING.getValue();

        matching.setPin(pin);
        matching.setMatchingStartDate(createdAt);
        matching.setStatus(status);

        matchingServices.updateMatching(matching);

        JSONObject jsonResult = new JSONObject();
        jsonResult.put("pin", pin);
        jsonResult.put("created_at", createdAt);
        jsonResult.put("status", status);

        return new ResponseEntity<>(jsonResult.toString(), HttpStatus.OK);
    }

    @ApiOperation(value = "Receiver receives a book", response = String.class)
    @RequestMapping(value = "/receive", method = RequestMethod.GET, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<String> receiveBook(@RequestParam String pin, @RequestParam Long matchingId) throws EntityNotFoundException, EntityPinMisMatchException, PinExpiredException {
        Matching matching = matchingServices.getMatchingById(matchingId);
        if (matching == null) {
            throw new EntityNotFoundException("Matching id: " + matchingId + " not found");
        }

        Date now = new Date();
        long duration = utils.getDuration(matching.getMatchingStartDate(), now, TimeUnit.MINUTES);

        if (duration > Constant.PIN_EXPIRED_MINUTE) {
            throw new PinExpiredException("Pin: " + pin + " has been expired");
        }

        if (!matching.getPin().equals(pin)) {
            throw new EntityPinMisMatchException("Pin: " + pin + " does not match to Matching");
        }

        matching.setStatus(MatchingStatus.CONFIRMED.getValue());
        matchingServices.updateMatching(matching);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "confirmed");
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    @ApiOperation(value = "Returner confirms book transfer", response = String.class)
    @RequestMapping(value = "/confirm", method = RequestMethod.GET, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<String> confirmBookTransfer(@RequestParam Long matchingId) throws Exception {
        Matching matching = matchingServices.getMatchingById(matchingId);
        if (matching == null) {
            throw new EntityNotFoundException("Matching id: " + matchingId + " not found");
        }

        if (matching.getStatus() != MatchingStatus.CONFIRMED.getValue()) {
            throw new Exception("Receiver has not imported pin yet");
        }

        //update status of request to "completed"
        Request returnerRequest = matching.getReturnerRequest();
        Request receiverRequest = matching.getBorrowerRequest();

        returnerRequest.setStatus(RequestStatus.COMPLETED.getValue());
        receiverRequest.setStatus(RequestStatus.COMPLETED.getValue());

        requestServices.updateRequest(returnerRequest);
        requestServices.updateRequest(receiverRequest);

        //transfer book from returner to receiver
        Book book = matching.getBook();
        User receiver = receiverRequest.getUser();
        book.setUser(receiver);

        //return message to client
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "confirm book transfer");
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    @ApiOperation(value = "Update request status", response = String.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<String> updateRequest(@PathVariable Long id, @RequestBody Request request) throws EntityNotFoundException, EntityIdMismatchException {
        if (request.getId() != id) {
            throw new EntityIdMismatchException("Request id: " + id + " and " + request.getId() + " is not matched");
        }

        Request existedRequest = requestServices.getRequestById(request.getId());
        if (existedRequest == null) {
            throw new EntityNotFoundException("Request id: " + request + " not found");
        }

        Request requestResult = requestServices.updateRequest(request);

        JSONObject jsonObject = new JSONObject(requestResult);
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

}
