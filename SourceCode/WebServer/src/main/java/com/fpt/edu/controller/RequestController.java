package com.fpt.edu.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fpt.edu.constant.Constant;
import com.fpt.edu.entities.Request;
import com.fpt.edu.services.RequestServices;
import io.swagger.annotations.ApiOperation;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("requests")
public class RequestController extends BaseController {

    @Autowired
    private RequestServices requestServices;

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
    @RequestMapping(value = "", method = RequestMethod.POST, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<String> requestBook(@RequestBody String request) throws IOException {
        Request requestResult = requestServices.requestBook(request);

        JSONObject jsonObject = utils.convertObjectToJSONObject(requestResult);
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }


}
