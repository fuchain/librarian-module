package com.fpt.edu.controller;

import com.fpt.edu.common.helper.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BaseController {

    @Autowired
    protected Utils utils;


}
