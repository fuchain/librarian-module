package com.fpt.edu.controller;

import com.fpt.edu.common.helper.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class BaseController {

    protected   final Logger LOGGER = LogManager.getLogger(getClass());


    @Autowired
    protected Utils utils;
    @Autowired
    HttpServletRequest httpServletRequest;



}
