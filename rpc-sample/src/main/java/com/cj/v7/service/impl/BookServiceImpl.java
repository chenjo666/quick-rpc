package com.cj.v7.service.impl;

import com.cj.v7.pojo.Book;
import com.cj.v7.service.BookService;

public class BookServiceImpl implements BookService {

    @Override
    public Book getBook(String id) {
        return Book.builder().id(id).name("java开发").price(39.9).build();
    }

    @Override
    public boolean deleteBook(String id) {
        return true;
    }
}
