package com.fpt.edu.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.edu.entities.BookDetail;
import com.fpt.edu.repository.BookDetailRepository;
import org.apache.commons.collections.IteratorUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class BookDetailsServices {
    @Autowired
    private BookDetailRepository bookDetailRepository;

    public List<BookDetail> getAllBookDetails() {
        return IteratorUtils.toList(bookDetailRepository.findAll().iterator());
    }


    public boolean saveBookDetail(String obj) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        BookDetail bookDetail = objectMapper.readValue(obj, BookDetail.class);
        bookDetailRepository.save(bookDetail);
        return true;
    }


}
