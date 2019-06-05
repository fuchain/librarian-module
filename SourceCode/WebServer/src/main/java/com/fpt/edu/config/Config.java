package com.fpt.edu.config;

import com.fpt.edu.common.RequestQueueSimulate.Observer;
import com.fpt.edu.common.RequestQueueSimulate.PublishSubscribe;
import com.fpt.edu.common.RequestQueueSimulate.RequestQueueManager;
import com.fpt.edu.services.RequestServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class Config {

   @Autowired
    RequestQueueManager requestQueueManager;

    @Autowired
    RequestServices requestServices;


    @Bean
    PublishSubscribe getPublisher(){

        List<Observer> list = new ArrayList<Observer>();
        list.add(requestServices);
        list.add(requestQueueManager);
        return new PublishSubscribe(list);


    }



}
