package com.fpt.edu.repositories;


import com.fpt.edu.entities.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {
	@Query("Select category from Category  category where category.name =:name")
	Category findByName(@Param("name") String name);


}
