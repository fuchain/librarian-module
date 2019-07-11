package com.fpt.edu.repositories;

import org.springframework.data.repository.CrudRepository;

import com.fpt.edu.entities.Publisher;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends CrudRepository<Publisher, Long>{



	public Publisher getPublisherByName(String name);

}
