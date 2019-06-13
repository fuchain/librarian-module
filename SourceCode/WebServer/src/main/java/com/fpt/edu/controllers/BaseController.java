package com.fpt.edu.controllers;

import com.fpt.edu.common.helpers.UtilHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class BaseController {
    protected final Logger LOGGER = LogManager.getLogger(getClass());

    @Autowired
    protected UtilHelper utils;

    @Autowired
    HttpServletRequest httpServletRequest;
}
