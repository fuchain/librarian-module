package com.fpt.edu.repository;


import org.springframework.data.repository.CrudRepository;

import com.fpt.edu.entities.Category;

public interface CategoryRepository extends CrudRepository<Category, Long> {

}
