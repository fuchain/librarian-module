package com.fpt.edu.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fpt.edu.common.ERequestStatus;
import com.fpt.edu.common.ETransferType;
import com.fpt.edu.common.EMatchingStatus;
import com.fpt.edu.common.ERequestType;
import com.fpt.edu.common.RequestQueueSimulate.Message;
import com.fpt.edu.common.RequestQueueSimulate.PublishSubscribe;
import com.fpt.edu.common.RequestQueueSimulate.RequestQueueManager;
import com.fpt.edu.constant.Constant;
import com.fpt.edu.entities.*;
import com.fpt.edu.exception.*;
import com.fpt.edu.services.*;
import io.swagger.annotations.ApiOperation;
import org.hibernate.Hibernate;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

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


    @Autowired
    PublishSubscribe publishSubscribe;

    @Autowired
    RequestQueueManager requestQueueManager;


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
            throw new EntityNotFoundException("Request id: " + id + " not found");
        }

        JSONObject jsonObject = utils.convertObjectToJSONObject(request);

        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    @ApiOperation(value = "Get a list of book request", response = String.class)
    @RequestMapping(value = "/get_list", method = RequestMethod.GET, produces = Constant.APPLICATION_JSON)
    @Transactional
    public ResponseEntity<List<Request>> getBookRequestList(@RequestParam int type) {
        //get user information
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        User user = userServices.getUserByEmail(email);
        Hibernate.initialize(user.getListBooks());
        List<Request> requestList = requestServices.findByUserIdAndType(user.getId(), type, ERequestStatus.COMPLETED.getValue());

        for (Request r : requestList) {
            if (r.getStatus() == ERequestStatus.MATCHING.getValue()) {
                Matching matching = matchingServices.getMatchingByRequestId(r.getId(), EMatchingStatus.CONFIRMED.getValue());
                if (matching != null) {
                    if (r.getType() == ERequestType.RETURNING.getValue()) {
                        User borrower = matching.getBorrowerRequest().getUser();
                        r.setPairedUser(borrower);
                    } else if (r.getType() == ERequestType.BORROWING.getValue()) {
                        User returner = matching.getReturnerRequest().getUser();
                        r.setPairedUser(returner);
                    }
                }
            }
        }

        return new ResponseEntity<>(requestList, HttpStatus.OK);
    }

    @ApiOperation(value = "Create a book request", response = String.class)
    @RequestMapping(value = "", method = RequestMethod.POST, produces = Constant.APPLICATION_JSON)
    @Transactional
    public ResponseEntity<String> requestBook(@RequestBody String body) throws EntityNotFoundException, TypeNotSupportedException, EntityAldreayExisted, NotFoundException {
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
                if (b.getId().equals(bookId)) {
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
        Message message = new Message();
        message.setMessage(request);
        message.setAction(Constant.ACTION_ADD_NEW);
        publishSubscribe.setMessage(message);
        publishSubscribe.notifyToSub();
        Request matchRequest = requestQueueManager.findTheMatch(request);
        if (matchRequest != null) {
            // update for request
            message.setAction(Constant.ACTION_UPDATE);
            request.setStatus(ERequestStatus.MATCHING.getValue());
            publishSubscribe.notifyToSub();
            matchRequest.setStatus(ERequestStatus.MATCHING.getValue());
            Message matchMessage = new Message();
            matchMessage.setAction(Constant.ACTION_UPDATE);
            matchMessage.setMessage(matchRequest);
            publishSubscribe.setMessage(matchMessage);
            publishSubscribe.notifyToSub();
            Matching matching = new Matching();
            matching.setStatus(EMatchingStatus.PENDING.getValue());
            matching.setBook(matchRequest.getBook());
            if (request.getType() == ERequestType.BORROWING.getValue()) {
                matching.setBorrowerRequest(request);
                matching.setReturnerRequest(matchRequest);
            } else {
                matching.setBorrowerRequest(matchRequest);
                matching.setReturnerRequest(request);
            }
            matchingServices.updateMatching(matching);
        }
//        requestServices.saveRequest(request);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "success");
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    @ApiOperation(value = "Transfer book for returner and receiver", response = String.class)
    @RequestMapping(value = "/transfer", method = RequestMethod.PUT, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<String> transferBook(@RequestBody String body) throws Exception {
        // Get user information
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        User sender = userServices.getUserByEmail(email);

        JSONObject bodyObject = new JSONObject(body);
        int type = bodyObject.getInt("type");
        Long matchingId = bodyObject.getLong("matchingId");

        JSONObject jsonResult = new JSONObject();

        // Check whether matchingId is existed or not
        Matching matching = matchingServices.getMatchingById(matchingId);
        if (matching == null) {
            throw new EntityNotFoundException("Matching id: " + matchingId + " not found");
        }

        if (type == ETransferType.RETURNER.getValue()) { // If this is returner
            User matchingUser = matching.getReturnerRequest().getUser();
            if (matchingUser.getId().longValue() != sender.getId().longValue()) {
                throw new EntityIdMismatchException("User id: " + matchingUser.getId() + " does not match with user id from authentication");
            }

            // Update matching fields(pin, matchingDate, status)
            String generatedPin = utils.getPin();
            Date createdAt = new Date();
            int status = EMatchingStatus.PENDING.getValue();

            matching.setPin(generatedPin);
            matching.setMatchingStartDate(createdAt);
            matching.setStatus(status);

            matchingServices.updateMatching(matching);

            // Response to user
            jsonResult.put("pin", generatedPin);
            jsonResult.put("created_at", createdAt);
            jsonResult.put("status", status);

            return new ResponseEntity<>(jsonResult.toString(), HttpStatus.OK);

        } else if (type == ETransferType.RECEIVER.getValue()) { // If this is receiver
            User matchingUser = matching.getBorrowerRequest().getUser();
            if (matchingUser.getId().longValue() != sender.getId().longValue()) {
                throw new EntityIdMismatchException("User id: " + matchingUser.getId() + " does not match with user id from authentication");
            }

            // Check whether matching status equals completed or not
            if (matching.getStatus() == ERequestStatus.COMPLETED.getValue()) {
                throw new EntityAldreayExisted("The pin has have sent");
            }

            // Get pin entered from receiver
            String pin = bodyObject.getString("pin");

            // Check expired time of pin
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

            Date sendTime = new Date();

            // Update current_keeper
            book.setUser(receiver);

            // Add transaction to BigchainDB
            BigchainTransactionServices services = new BigchainTransactionServices();
            services.doTransfer(
                    book.getLastTxId(),
                    book.getAssetId(), book.getMetadata(),
                    String.valueOf(returner.getEmail()), String.valueOf(receiver.getEmail()),
                    (transaction, response) -> { // Success
                        // Turn on the flag success and callback
                        success.set(true);
                        callback.set(true);

                        String tracsactionId = transaction.getId();
                        book.setLastTxId(tracsactionId);

                        // Update status of request to "completed"
                        returnerRequest.setStatus(ERequestStatus.COMPLETED.getValue());
                        receiverRequest.setStatus(ERequestStatus.COMPLETED.getValue());
                        requestServices.updateRequest(returnerRequest);
                        requestServices.updateRequest(receiverRequest);

                        //update status of matching
                        matching.setStatus(EMatchingStatus.CONFIRMED.getValue());
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
                    }
            );

            Date now;

            while (true) {
                now = new Date();
                duration = utils.getDuration(sendTime, now, TimeUnit.SECONDS);

                if (duration > 30 || callback.get()) {
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
        if (request.getId().equals(id)) {
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


    @ApiOperation(value = "Get Match ID", response = JSONObject.class)
    @RequestMapping(value = "/{id}/matched", method = RequestMethod.GET, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<String> getMatchedIdOfRequest(@PathVariable Long id) throws Exception {
        Matching matched = matchingServices.getMatchingByRequestId(id);
        JSONObject jsonResponse = new JSONObject();
        if (matched != null) {
            jsonResponse.put("matching_id", matched.getId());
        } else {
            throw new Exception("Cannot find matching_id with that request_id");
        }
        return new ResponseEntity<>(jsonResponse.toString(), HttpStatus.OK);
    }
}
