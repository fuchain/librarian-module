package com.fpt.edu.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fpt.edu.constant.Constant;
import com.fpt.edu.repository.RequestRepository;
import com.fpt.edu.services.RequestServices;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;
import springfox.documentation.RequestHandler;

import javax.xml.ws.Response;

@RestController
@RequestMapping("requests")
public class RequestController extends BaseController {

    @Autowired
    private RequestServices requestServices;

    @RequestMapping(value = "", method = RequestMethod.GET, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<String> getListOfRequest() throws JsonProcessingException {
        LOGGER.info("START Controller :" + httpServletRequest.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString());
            JSONObject jsonResult = utils.buildListEntity(requestServices.getListRequest(), httpServletRequest);
        return new ResponseEntity<>("Success", HttpStatus.OK);

    }


}
