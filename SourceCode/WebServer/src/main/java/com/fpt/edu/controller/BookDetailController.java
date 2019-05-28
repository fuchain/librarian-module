package com.fpt.edu.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fpt.edu.constant.Constant;
import com.fpt.edu.exception.EntityNotFoundException;
import com.fpt.edu.repository.BookDetailRepository;
import com.fpt.edu.services.BookDetailsServices;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import javax.swing.text.html.parser.Entity;
import java.io.IOException;

@RestController
@RequestMapping("bookdetails")
public class BookDetailController extends BaseController {

    @Autowired
    BookDetailsServices bookDetailsServices;

    @RequestMapping(value = "", method = RequestMethod.GET, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<String> findBookDetailsById() throws EntityNotFoundException, JsonProcessingException {
        String requestPattern=httpServletRequest.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString();
        LOGGER.info("START Controller :" + requestPattern);


        JSONObject raw = utils.buildListEntity(bookDetailsServices.getAllBookDetails());



        return new ResponseEntity<>(utils.buildRelatedLink(requestPattern,httpServletRequest.getMethod(),raw).toString(), HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<String> createBookDetails(@RequestBody String body) throws IOException {
        bookDetailsServices.saveBookDetail(body);

        return new ResponseEntity<>("{'200':'Success'}", HttpStatus.OK);
    }


}
