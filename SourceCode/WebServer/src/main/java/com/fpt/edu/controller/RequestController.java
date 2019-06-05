package com.fpt.edu.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fpt.edu.common.ERequestStatus;
import com.fpt.edu.common.ETransferType;
import com.fpt.edu.common.MatchingStatus;
import com.fpt.edu.common.ERequestType;
import com.fpt.edu.constant.Constant;
import com.fpt.edu.entities.*;
import com.fpt.edu.exception.*;
import com.fpt.edu.services.*;
import io.swagger.annotations.ApiOperation;
import org.hibernate.Hibernate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

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

    @Autowired
    private TransactionServices transactionServices;


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
    @Transactional
    public ResponseEntity<List<Request>> getBookRequestList(@RequestParam int type) throws JsonProcessingException {
        //get user information
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        User user = userServices.getUserByEmail(email);
        Hibernate.initialize(user.getListBooks());
        List<Request> requestList = requestServices.findByUserIdAndType(user.getId(), type);
        return new ResponseEntity<>(requestList, HttpStatus.OK);
    }

    @Transactional
    @ApiOperation(value = "Create a book request", response = String.class)
    @RequestMapping(value = "", method = RequestMethod.POST, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<String> requestBook(@RequestBody String body) throws EntityNotFoundException, TypeNotSupportedException, EntityAldreayExisted {
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
        if (type == ERequestType.BORROWING.getValue()) {
            //get book name
            String bookName = bodyObject.getString("book_name");
            //get book object
            BookDetail bookDetail = bookDetailsServices.getBookDetailByName(bookName);

            if (bookDetail == null) {
                throw new EntityNotFoundException("Book name: " + bookName + " not found");
            }

            //check existed request
            boolean existed = requestServices.checkExistedRequest(type, user.getId(), ERequestStatus.COMPLETED.getValue(),
                    bookDetail.getId(), (long) 0);
            if (existed) {
                throw new EntityAldreayExisted("Request's already existed");
            }

            //create a request and fill data
            request = new Request();
            request.setStatus(ERequestStatus.PENDING.getValue());
            request.setType(type);
            request.setUser(user);
            request.setBookDetail(bookDetail);

        } else if (type == ERequestType.RETURNING.getValue()) {//if request is returning
            //get book id
            Long bookId = bodyObject.getLong("book_id");
            //get book object
            Book book = bookServices.getBookById(bookId);

            BookDetail bookDetail = book.getBookDetail();


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
            boolean existed = requestServices.checkExistedRequest(type, user.getId(), ERequestStatus.COMPLETED.getValue(),
                    (long) 0, book.getId());
            if (existed) {
                throw new EntityAldreayExisted("Request's already existed");
            }

            //create a request and fill data
            request = new Request();
            request.setStatus(ERequestStatus.PENDING.getValue());
            request.setType(type);
            request.setUser(user);
            request.setBook(book);
            request.setBookDetail(bookDetail);
        } else {
            throw new TypeNotSupportedException("Type " + type + " is not supported");
        }

        //save request
        requestServices.saveRequest(request);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "success");

        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    @ApiOperation(value = "Transfer book for returner and receiver", response = String.class)
    @RequestMapping(value = "/transfer", method = RequestMethod.PUT, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<String> transferBook(@RequestBody String body) throws Exception {
        JSONObject bodyObject = new JSONObject(body);
        int type = bodyObject.getInt("type");

        Long matchingId = bodyObject.getLong("matchingId");

        JSONObject jsonResult = new JSONObject();

        //check whether matchingId is existed or not
        Matching matching = matchingServices.getMatchingById(matchingId);
        if (matching == null) {
            throw new EntityNotFoundException("Matching id: " + matchingId + " not found");
        }

        if (type == ETransferType.RETURNER.getValue()) { // if returner returns book
            //update matching fields(pin, matchingDate, status)
            String generatedPin = utils.getPin();
            Date createdAt = new Date();
            int status = MatchingStatus.PENDING.getValue();

            matching.setPin(generatedPin);
            matching.setMatchingStartDate(createdAt);
            matching.setStatus(status);

            matchingServices.updateMatching(matching);

            //response to user
            jsonResult.put("pin", generatedPin);
            jsonResult.put("created_at", createdAt);
            jsonResult.put("status", status);

            return new ResponseEntity<>(jsonResult.toString(), HttpStatus.OK);

        } else if (type == ETransferType.RECEIVER.getValue()) { // if receiver receives book
            //check whether matching status equals completed or not
            if (matching.getStatus() == ERequestStatus.COMPLETED.getValue()) {
                throw new EntityAldreayExisted("The pin has have sent");
            }

            //get pin from receiver
            String pin = bodyObject.getString("pin");

            //check expired time of pin
            long duration = utils.getDuration(matching.getMatchingStartDate(), new Date(), TimeUnit.MINUTES);
            if (duration > Constant.PIN_EXPIRED_MINUTE) {
                throw new PinExpiredException("Pin: " + pin + " has been expired");
            }

            if (!matching.getPin().equals(pin)) {
                throw new EntityPinMisMatchException("Pin: " + pin + " does not match to Matching");
            }

            Book book = bookServices.getBookById(matching.getBook().getId());
            Request returnerRequest = matching.getReturnerRequest();
            Request receiverRequest = matching.getBorrowerRequest();
            User returner = returnerRequest.getUser();
            User receiver = receiverRequest.getUser();
            AtomicBoolean success = new AtomicBoolean(false);
            AtomicBoolean callback = new AtomicBoolean(false);
            AtomicReference<ResponseEntity> responseEntity = null;
            Object asset = book.getAsset();
            Date sendTime = new Date();

            //update current_keeper
            book.setUser(receiver);

            //add transaction to bigchainDB
            BigchainTransactionServices services = new BigchainTransactionServices();
            services.doTransfer(
                    book.getLastTxId(),
                    book.getAssetId(), book.getMetadata(),
                    String.valueOf(returner.getId()), String.valueOf(receiver.getId()),
                    (transaction, response) -> { //success

                        //turn on the flag success and callback
                        success.set(true);
                        callback.set(true);

                        String tracsactionId = transaction.getId();
                        book.setAssetId(tracsactionId);
                        book.setLastTxId(tracsactionId);
                        LOGGER.info("Create tx success: " + response);

                        //update status of request to "completed"
                        returnerRequest.setStatus(ERequestStatus.COMPLETED.getValue());
                        receiverRequest.setStatus(ERequestStatus.COMPLETED.getValue());
                        requestServices.updateRequest(returnerRequest);
                        requestServices.updateRequest(receiverRequest);

                        //update status of matching
                        matching.setStatus(MatchingStatus.CONFIRMED.getValue());
                        matchingServices.updateMatching(matching);

                        //transfer book from returner to receiver
                        bookServices.updateBook(book);

                        //insert a transaction to DB Postgresql
                        Transaction tran = new Transaction();
                        tran.setBook(book);
                        tran.setReturner(returner);
                        tran.setBorrower(receiver);
                        transactionServices.insertTransaction(tran);

                    },
                    (transaction, response) -> { //failed
                        callback.set(true);
                        LOGGER.error("We have a trouble: " + response);
                    }
            );

            Date now;

            while (true) {
                now = new Date();
                duration = utils.getDuration(sendTime, now, TimeUnit.SECONDS);

                if (duration > 30 || callback.get() == true) {
                    jsonResult.put("message", "confirm book transfer successfully");
                    return new ResponseEntity<>(jsonResult.toString(), HttpStatus.OK);
                }
            }
        } else {
            throw new TypeNotSupportedException("Type: " + type + " is not supported");
        }
    }

    @ApiOperation(value = "Update request", response = String.class)
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
