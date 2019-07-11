package com.fpt.edu.services;


import com.fpt.edu.entities.BookDetail;
import com.fpt.edu.entities.Category;
import com.fpt.edu.repositories.BookDetailRepository;
import com.fpt.edu.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryServices {

	private CategoryRepository categoryRepository;

	public CategoryServices(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public Category addIfNotExist(String categoryName){
		Category result= this.categoryRepository.findByName(categoryName);
		if(result==null){
			result = new Category();
			result.setName(categoryName);
			this.categoryRepository.save(result);
		}
		return result;

	}




}
