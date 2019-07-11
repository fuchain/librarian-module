package com.fpt.edu.services;

import com.fpt.edu.entities.Author;
import com.fpt.edu.entities.Publisher;
import com.fpt.edu.repositories.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublisherServices {


	private PublisherRepository publisherRepository;
	@Autowired
	public PublisherServices(PublisherRepository publisherRepository) {
		this.publisherRepository = publisherRepository;
	}



	public Publisher getandAddIfNotExist(String name){
		Publisher publisher = publisherRepository.getPublisherByName(name);
		if(publisher==null){
			publisher= new Publisher();
			publisher.setName(name);
			publisherRepository.save(publisher);
		}
		return publisher;
	}


}
